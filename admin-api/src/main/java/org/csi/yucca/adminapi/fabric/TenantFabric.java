/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.fabric;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.delegate.IdentityServerDelegate;
import org.csi.yucca.adminapi.delegate.RShellDelegate;
import org.csi.yucca.adminapi.delegate.StoreDelegate;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.FabricException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.model.join.DettaglioTenantBackoffice;
import org.csi.yucca.adminapi.request.ActionfeedbackOnTenantRequest;
import org.csi.yucca.adminapi.service.TenantService;
import org.csi.yucca.adminapi.store.response.Apis;
import org.csi.yucca.adminapi.store.response.Application;
import org.csi.yucca.adminapi.store.response.GeneralResponse;
import org.csi.yucca.adminapi.store.response.ListApplicationResponse;
import org.csi.yucca.adminapi.store.response.SubscriptionAPIResponse;
import org.csi.yucca.adminapi.util.Action;
import org.csi.yucca.adminapi.util.FeedbackStatus;
import org.csi.yucca.adminapi.util.Status;
import org.csi.yucca.adminapi.util.TenantType;
import org.csi.yucca.adminapi.util.WebServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.xml.sax.SAXException;

import com.jcraft.jsch.JSchException;

@Configuration
@PropertySource(value = { "classpath:adminapi.properties" })
public class TenantFabric {

	private static final Logger logger = Logger.getLogger(TenantFabric.class);

	private static TenantFabric tenantFabric;
	//private TenantService tenantService;
	
	
//	@Value("${identityServer.user}")
//	private String identityServerSecuser;
//
//	@Value("${identityServer.password}")
//	private String identityServerSecpassword;

	@Value("${rsh.tenant.url}")
	private String rshUrl;

	@Value("${rsh.tenant.user}")
	private String rshUser;

	@Value("${rsh.tenant.password}")
	private String rshPassword;

	@Value("${rsh.tenant.env}")
	private String rshEnv;
	

	private TenantService tenantService;
	public TenantFabric(TenantService tenantService) {
		this.tenantService = tenantService;
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public static TenantFabric build(TenantService tenantService) {
		if (tenantFabric == null)
			tenantFabric = new TenantFabric(tenantService);
		return tenantFabric;
	}

	public FabricResponse execAction(String action, DettaglioTenantBackoffice tenant) {

		FabricResponse fabricResponse = null;
		if (Action.INSTALLATION.code().equals(action))
			fabricResponse = execInstallation(tenant);
		else if (Action.DELETE.code().equals(action))
			fabricResponse = execUninstallation(tenant);
		ActionfeedbackOnTenantRequest actionRequest = new ActionfeedbackOnTenantRequest();
		if(FabricResponse.STATUS_SUCCESS.equals(fabricResponse.getStatus()))
			actionRequest.setStatus(FeedbackStatus.OK.code());
		else
			actionRequest.setStatus(FeedbackStatus.KO.code());
		try {
			tenantService.actionfeedbackOnTenant(actionRequest, tenant.getTenantcode());
			fabricResponse.addLogInfo("STEP LAST - Updated tenant satus - INSTALLED(" + Status.INSTALLED.id() + ") ok ");
			fabricResponse.setStatus(FabricResponse.STATUS_SUCCESS);
		} catch (BadRequestException e) {
			fabricResponse.addLogError("STEP LAST - Updated tenant satus - BadRequestException(" + Status.INSTALLED.id() + ") error: " + e.getMessage());
			fabricResponse.setStatus(FabricResponse.STATUS_ERROR);
			e.printStackTrace();
		} catch (NotFoundException e) {
			fabricResponse.addLogError("STEP LAST - Updated tenant satus - BadRequestException(" + Status.INSTALLED.id() + ") error: " + e.getMessage());
			fabricResponse.setStatus(FabricResponse.STATUS_ERROR);
			e.printStackTrace();
		} catch (Exception e) {
			fabricResponse.addLogError("STEP LAST - Updated tenant satus - BadRequestException(" + Status.INSTALLED.id() + ") error: " + e.getMessage());
			fabricResponse.setStatus(FabricResponse.STATUS_ERROR);
			e.printStackTrace();
		}

		return fabricResponse;
}

	private FabricResponse execInstallation(DettaglioTenantBackoffice tenant) {
		
		FabricResponse fabricResponse  = new FabricResponse("install tenant " + tenant.getTenantcode());
		String tenantcode = tenant.getTenantcode();


		try {
			// update tenant status
			try {
				tenantService.updateTenantStatus(Status.INSTALLATION_IN_PROGRESS.id(), tenantcode);
				fabricResponse.addLogInfo("STEP 0 - Updated tenant satus - " + tenantcode + ", INSTALLATION_IN_PROGRESS("+Status.INSTALLATION_IN_PROGRESS.id()+") ok ");
			} catch (BadRequestException e) {
				logger.error("[TenantFabric::execUninstallation] BadRequestException error " + e.getMessage());
				e.printStackTrace();
				throw new FabricException("Error in execUninstallation: BadRequestException " + e.getMessage());		
			} catch (NotFoundException e) {
				logger.error("[TenantFabric::execUninstallation] NotFoundException error " + e.getMessage());
				e.printStackTrace();
				throw new FabricException("Error in execUninstallation: NotFoundException " + e.getMessage());		
			} catch (Exception e) {
				logger.error("[TenantFabric::execUninstallation] Exception error " + e.getMessage());
				e.printStackTrace();
				throw new FabricException("Error in execUninstallation: Exception " + e.getMessage());		
			}
			// load existing roles
			fabricResponse.addLogDebug("Loading existing roles - start");
			List<String> existingRoles = loadExistingRoles(fabricResponse);
			debugList(fabricResponse, "existingRoles", existingRoles);
			

			// load exiting users
			fabricResponse.addLogDebug("Loading existing users - start");
			List<String> existingUsers = loadExistingUsers(fabricResponse);
			debugList(fabricResponse, "existingUsers", existingUsers);
			
			// add Role for topic
			if (!existingRoles.contains("mb-topic-all-input." + tenantcode)) {
				WebServiceResponse addRoleResponse = IdentityServerDelegate.build().addRole("mb-topic-all-input." + tenantcode, "false");
				logger.info("[TenantFabric::execInstallation] add role mb-topic-all-input." + tenantcode + " response " + addRoleResponse.getMessage());
				if(addRoleResponse.getStatusCode()<200 || addRoleResponse.getStatusCode()>299)
					throw new FabricException("Error in addRole mb-topic-all-input." + tenantcode + " - status: " + addRoleResponse.getStatusCode() + " - soapMessage: " +addRoleResponse.getMessage()); 
				fabricResponse.addLogInfo("STEP 1 Add Role - mb-topic-all-input." + tenantcode + " inserted");
			}
			else
				fabricResponse.addLogInfo("STEP 1 Add Role for Topic - mb-topic-all-input." + tenantcode + " already existing");

		
			// add User 
			if (!existingUsers.contains(tenantcode)) {
				WebServiceResponse addUserResponse = IdentityServerDelegate.build().addUser(tenantcode, tenant.getPassword(), "mb-topic-all-input." + tenantcode); 
				logger.info("[TenantFabric::execInstallation] add user for role mb-topic-all-input." + tenantcode + " response " + addUserResponse.getMessage());
				if(addUserResponse.getStatusCode()<200 || addUserResponse.getStatusCode()>299)
					throw new FabricException("Error in addUser " + tenantcode + " with role mb-topic-all-input." + tenantcode + " - status: " + addUserResponse.getStatusCode() + " - soapMessage: " +addUserResponse.getMessage()); 
				fabricResponse.addLogInfo("STEP 2 Add User - User " + tenantcode + " with role mb-topic-all-input." + tenantcode + " inserted");
			}
			else
				fabricResponse.addLogInfo("STEP 2 Add User - User " + tenantcode + " with role mb-topic-all-input." + tenantcode + " already existing");

			// add Role subscriber
			if (!existingRoles.contains(tenantcode + "_subscriber")) {
				WebServiceResponse addRoleResponse = IdentityServerDelegate.build().addRole(tenantcode + "_subscriber", "false");
				logger.info("[TenantFabric::execInstallation] add role " + tenantcode + "_subscriber response " + addRoleResponse.getMessage());
				if(addRoleResponse.getStatusCode()<200 || addRoleResponse.getStatusCode()>299)
					throw new FabricException("Error in addRole " + tenantcode + "_subscriber - status: " + addRoleResponse.getStatusCode() + " - soapMessage: " +addRoleResponse.getMessage()); 
				fabricResponse.addLogInfo("STEP 3 Add Role Subscriber - Role " + tenantcode + "_subscriber inserted");
			}
			else
				fabricResponse.addLogInfo("STEP 3 Add Role Subscriber - Role " + tenantcode + "_subscriber already existing");

			// add Role subscriber to admin
			List<String> existingAdminRoles = new LinkedList<String>(); 
			WebServiceResponse roleOfAdminResponse = IdentityServerDelegate.build().getRolesOfUser("admin", null, null);
			try {
				existingAdminRoles = IdentityServerDelegate.parseRoleNames(roleOfAdminResponse.getMessage(), true);
			} catch (Exception e1) {
				throw new FabricException("Error in loading existing roles of admin: " +e1.getMessage()); 
			}
			fabricResponse.addLogDebug("Loading existing roles of admin - ok");


			
			if (!existingAdminRoles.contains(tenantcode + "_subscriber")) {
				List<String> newUsers = new LinkedList<String>();
				newUsers.add("admin");
				WebServiceResponse addRemoveUsersOfRoleResponse = IdentityServerDelegate.build().addRemoveUsersOfRole(tenantcode + "_subscriber", newUsers, null);
				logger.info("[TenantFabric::execInstallation] add role " + tenantcode + " to admin response " + addRemoveUsersOfRoleResponse.getMessage());
				if(addRemoveUsersOfRoleResponse.getStatusCode()<200 || addRemoveUsersOfRoleResponse.getStatusCode()>299)
					throw new FabricException("Error in addRole to admin " + tenantcode + "_subscriber - status: " + addRemoveUsersOfRoleResponse.getStatusCode() + " - soapMessage: " +addRemoveUsersOfRoleResponse.getMessage()); 
				fabricResponse.addLogInfo("STEP 4 Add Role Subscriber to Admin - Add role " + tenantcode + "_subscriber to admin inserted");
			}
			else
				fabricResponse.addLogInfo("STEP 4 Add Role Subscriber to Admin - Role " + tenantcode + "_subscriber of admin already existing");

			
			// store
			try {
				CloseableHttpClient c = StoreDelegate.build().registerToStoreInit();
				fabricResponse.addLogDebug("Connection to the store - ok");

				logger.info("[TenantFabric::execInstallation] connect to store ok");
				fabricResponse.addLogInfo("Connect to store ok ");
				
				//get exiting application
				ListApplicationResponse existingApplications = StoreDelegate.build().listApplications(c);
				if(existingApplications.getError())
					throw new FabricException("Error in loading existing application from store");
				fabricResponse.addLogDebug("Loading existing application - ok");

				Application existingApplication = null;
				for (Application application : existingApplications.getApplications()) {
					if(("userportal_" + tenantcode).equals(application.getName())) {
						existingApplication = application;
						break;
					}
				}
				
				// add application userportal_tenantcode
				if(existingApplication == null) {
					StoreDelegate.build().addApplication(c, "userportal_" + tenantcode);
					fabricResponse.addLogInfo("STEP 5 Add Application - AddApplication  userportal_" + tenantcode + " ok ");
				}
				else
					fabricResponse.addLogInfo("STEP 5 Add Application - Application userportal_" + tenantcode + " already existing on store");
					
				
				// add subscription for userportal_tenantcode
				//get exiting subscruotuib
				SubscriptionAPIResponse existingApplicationTenantSubscriptions = StoreDelegate.build().listSubscriptionsByApplication(c, "admin", "userportal_" + tenantcode);
				//SubscriptionByUsernameResponse existingSubscriptions = StoreDelegate.build().listSubscriptionByApiAndUserName(c,"admin","admin_api");
//				if(existingSubscriptions.getError())
//					throw new FabricException("Error in loading existing application from store");
				
				fabricResponse.addLogDebug("Loading existing subscription - ok");

				boolean alreadyPresentSubscription = false;

				for (Apis api : existingApplicationTenantSubscriptions.getApis()) {
						if("admin_api".equals(api.getApiName())) {
							alreadyPresentSubscription = true;
							break;
						}
				}
				if(!alreadyPresentSubscription) {
					StoreDelegate.build().subscribeApi(c, "admin_api", "userportal_" + tenantcode);
					logger.info("[TenantFabric::execInstallation] subscribeApi  subscribe to admin api userportal_" + tenantcode + " ok ");
					fabricResponse.addLogInfo("STEP 6 subscribe API - SubscribeApi  subscribe to admin api userportal_" + tenantcode + " ok ");
				}
				else
					fabricResponse.addLogInfo("STEP 6 subscribe API - SubscribeApi  subscription to admin api userportal_" + tenantcode + " already present ");


				// generate adminkey for userportal_tenantcode
				fabricResponse.addLogDebug("Generate client key");
				try {
					GeneralResponse generetateKeyResponse = StoreDelegate.build().generateKey(c, "userportal_" + tenantcode);
					String clientkey = generetateKeyResponse.getData().getKey().getConsumerKey();
					String clientsecret = generetateKeyResponse.getData().getKey().getConsumerSecret();
					fabricResponse.addLogInfo("STEP 7 Key Generation - Generetate Key userportal_" + tenantcode + " ok ");
					// insert key
					tenantService.updateAdminKey(tenantcode, clientkey, clientsecret);
					fabricResponse.addLogInfo("STEP 8 - Update Client Credential- updateTenantClientCredential userportal_" + tenantcode + " ok ");
					logger.info("INFO - generetate Key userportal_" + tenantcode + " ok ");
				}
				catch(Exception e){
					e.printStackTrace();
					fabricResponse.addLogWarning("STEP 7 Key Generation - Key  userportal_" + tenantcode + " already generated-> verify on db");
				}
				StoreDelegate.build().logoutFromStore(c);
				fabricResponse.addLogDebug("Logout fromstore ok ");

			} catch (Exception e) {
				logger.error("[TenantFabric::execInstallation] store operation failed userportal_" + tenantcode + " error" +  e.getMessage());
				e.printStackTrace();
				throw new FabricException("Error in store operation: " +e.getMessage()); 
			}
			
			// create tenant on hdfs
			fabricResponse.addLogDebug("Tenant type: " + tenant.getIdTenantType() + " | TenantType.DEVELOP: " + TenantType.DEVELOP.id() );
			
			
			String shAction =  (!tenant.getIdTenantType().equals(TenantType.DEVELOP.id()))?"creaTenant.sh":"creaTenantSvil.sh";
			String action  = "cd scripts && ./" + shAction +  " " + tenant.getOrganizationcode()+" "+ tenantcode + " " + rshEnv;
				
			fabricResponse.addLogDebug("Create Tenant Action: " + action);
			try {
				String callRemoteShellResponse = RShellDelegate.callRemoteShell(rshUrl, rshUser, rshPassword, action);
				logger.info("[TenantFabric::execInstallation] rsh creaTenant for tenantcode " + tenantcode + " response " + callRemoteShellResponse);
				fabricResponse.addLogInfo("STEP 9 - create Tenant- rsh creaTenant for tenantcode " + tenantcode + " ok ");

			} catch (JSchException e) {
				logger.error("[TenantFabric::execInstallation] rsh creaTenant for tenantcode " + tenantcode + " error " + e.getMessage());
				e.printStackTrace();
				throw new FabricException("Error in rsh createTenant: " +e.getMessage()); 
			}
			
			// create tenant on phonix solr 
			String actionPhoenixSolr  = "cd scripts && ./creaTenantPhoenixSolr.sh "+tenant.getDataphoenixschemaname()+" " + rshEnv + " " + tenant.getDatasolrcollectionname() + " "+ 
				tenant.getMeasuresolrcollectionname() + " " + tenant.getMediasolrcollectionname() + " " + tenant.getSocialsolrcollectionname();
			fabricResponse.addLogDebug("Create Tenant Action Phoenix Sold: " + actionPhoenixSolr);
			try {
				String callRemoteShellResponse = RShellDelegate.callRemoteShell(rshUrl, rshUser, rshPassword, actionPhoenixSolr);
				logger.info("[TenantFabric::execInstallation] sh creaTenantPhoenixSolr for tenantcode " + tenantcode + " response " + callRemoteShellResponse);
				fabricResponse.addLogInfo("STEP 10 - create Tenant on Phonix Solr- rsh creaTenantPhoenixSolr for tenantcode " + tenantcode + " ok ");

			} catch (JSchException e) {
				logger.error("[TenantFabric::execInstallation] rsh creaTenantPhoenixSolr for tenantcode " + tenantcode + " error " + e.getMessage());
				e.printStackTrace();
				throw new FabricException("Error in rsh creaTenantPhoenixSolr : " +e.getMessage()); 
			}
			
			// add Role subscriber to final user (is already present)
			if (existingUsers.contains(tenant.getUsername())) {
				List<String> newRoles = new LinkedList<String>();
				// add Role subscriber to admin
				//List<String> existingUserRoles = new LinkedList<String>(); 
//				WebServiceResponse roleOfUserResponse = IdentityServerDelegate.build().getRolesOfUser(tenant.getUsername(), null, null);
//				try {
//					existingUserRoles = IdentityServerDelegate.parseRoleNames(roleOfUserResponse.getMessage());
//				} catch (Exception e1) {
//					throw new FabricException("Error in loading existing roles of user: " +e1.getMessage()); 
//				}
//				fabricResponse.addLogDebug("Loading existing roles of admin - ok");
//				if(!existingUserRoles.contains("userportal-superuser")) {
//					newRoles.add("userportal-superuser");
//					fabricResponse.addLogDebug("STEP 11 add role to User - User " + tenant.getUsername() + " add role userportal-superuser");
//				}
//				else
//					fabricResponse.addLogDebug("STEP 11 add role to User - User " + tenant.getUsername() + " role userportal-superuser already existing");
//				
//				if(!existingUserRoles.contains("internal/subscriber")) {
//					newRoles.add("internal/subscriber");
//					fabricResponse.addLogDebug("STEP 11 add role to User - User " + tenant.getUsername() + " add role internal/subscriber");
//				}
//				else
//					fabricResponse.addLogDebug("STEP 11 add role to User - User " + tenant.getUsername() + " role internal/subscriber already existing");

				try {
					checkRoleOfUser(tenant.getUsername(), tenantcode+"_subscriber",newRoles, fabricResponse);
					checkRoleOfUser(tenant.getUsername(), "userportal-superuser",newRoles, fabricResponse);
					checkRoleOfUser(tenant.getUsername(), "Internal/subscriber",newRoles, fabricResponse);
				} catch (Exception e1) {
					throw new FabricException("Error in loading existing roles of user: " +e1.getMessage()); 
				}
				if(newRoles.size()>0) {
					WebServiceResponse addRemoveRolesOfUserResponse = IdentityServerDelegate.build().addRemoveRolesOfUser(tenant.getUsername(), newRoles, null);
					logger.info("[TenantFabric::execInstallation] add role of admin " + tenantcode + " response " + addRemoveRolesOfUserResponse.getMessage());
					fabricResponse.addLogInfo("STEP 11 Add Roles to Tenant - Add roles " + tenantcode + "_subscriber, userportal-superuser, internal/subscriber   to " + tenantcode + " inserted");
					
				}
				else
					fabricResponse.addLogInfo("STEP 11 add role to User - User " + tenant.getUsername() + " all role already existing");
			}
			else
				fabricResponse.addLogInfo("STEP 11 add role to User - User " + tenant.getUsername() + " not exist");

			
//			
//			if (!existingRoles.contains(tenantcode + "_subscriber")) { // TODO 
//				List<String> newRoles = new LinkedList<String>();
//				newRoles.add(tenantcode+"_subscriber");
//				newRoles.add("userportal-superuser");
//				newRoles.add("internal/subscriber");
//
//				WebServiceResponse addRemoveRolesOfUserResponse = IdentityServerDelegate.build().addRemoveRolesOfUser(tenantcode, newRoles, null);
//				logger.info("[TenantFabric::execInstallation] add role of admin " + tenantcode + " response " + addRemoveRolesOfUserResponse.getMessage());
//				fabricResponse.addLogInfo("STEP 11 Add Roles to Tenant - Add roles " + tenantcode + "_subscriber, userportal-superuser, internal/subscriber   to " + tenantcode + " inserted");
//			}
//			else
//				fabricResponse.addLogInfo("STEP 11 Add Roles to Tenant - Roles " + tenantcode + "_subscriber, userportal-superuser, internal/subscriber  alredy existing");
			
			
			// update tenant status
			//tenantService.updateTenantStatus(Status.INSTALLED.id(), tenantcode);
			//fabricResponse.addLogInfo("STEP 12 - Updated tenant satus - " + tenantcode + ", INSTALLED("+Status.INSTALLED.id()+") ok ");

			fabricResponse.setStatus(FabricResponse.STATUS_SUCCESS);
			
			
			
		} catch (KeyManagementException e) {
			fabricResponse.setStatus(FabricResponse.STATUS_ERROR);
			fabricResponse.addLogError("KeyManagementException error " + e.getMessage());
			e.printStackTrace();
			
		} catch (NoSuchAlgorithmException e) {
			fabricResponse.setStatus(FabricResponse.STATUS_ERROR);
			fabricResponse.addLogError("NoSuchAlgorithmException error " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			fabricResponse.setStatus(FabricResponse.STATUS_ERROR);
			fabricResponse.addLogError("IOException error " + e.getMessage());
			e.printStackTrace();
		} catch (FabricException e) {
			fabricResponse.setStatus(FabricResponse.STATUS_ERROR);
			fabricResponse.addLogError("FabricException error " + e.getMessage());
			e.printStackTrace();
		}
		
		return fabricResponse;

	}



	private void debugList(FabricResponse fabricResponse, String listName, List<String> list) {
		if(list != null) {
			fabricResponse.addLogDebug(listName + ".size: " + list.size());
			for (String item : list) {
				fabricResponse.addLogDebug("   " + item);
			}
		}
		else
			fabricResponse.addLogDebug(listName + " is null ");
		
	}

	private void checkRoleOfUser(String username, String role, List<String> newRoles, FabricResponse fabricResponse) throws KeyManagementException, NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException{
		WebServiceResponse roleOfUserResponse = IdentityServerDelegate.build().getRolesOfUser(username, role, null);
		List<String> roles = IdentityServerDelegate.parseRoleNames(roleOfUserResponse.getMessage(), true);
		if(!roles.contains(role)) {		
			newRoles.add(role);
			fabricResponse.addLogDebug("STEP 11 add role to User - User " + username + " add role " + role);
		}
		else
			fabricResponse.addLogDebug("STEP 11 add role to User - User " + username+ " role " + role +" already existing");
	}

	private FabricResponse execUninstallation(DettaglioTenantBackoffice tenant) {
		FabricResponse fabricResponse  = new FabricResponse("uninstall tenant " + tenant.getTenantcode());
		String tenantcode = tenant.getTenantcode();


		try {
			// update tenant status
			//tenantService.updateTenantStatus(Status.UNINSTALLATION_IN_PROGRESS.id(), tenantcode);
			// update tenant status
			try {
				tenantService.updateTenantStatus(Status.UNINSTALLATION_IN_PROGRESS.id(), tenantcode);
				fabricResponse.addLogInfo("STEP 0 - Updated tenant satus - " + tenantcode + ", INSTALLATION_IN_PROGRESS("+Status.UNINSTALLATION_IN_PROGRESS.id()+") ok ");
			} catch (BadRequestException e) {
				logger.error("[TenantFabric::execUninstallation] BadRequestException error " + e.getMessage());
				e.printStackTrace();
				throw new FabricException("Error in execUninstallation: BadRequestException " + e.getMessage());		
			} catch (NotFoundException e) {
				logger.error("[TenantFabric::execUninstallation] NotFoundException error " + e.getMessage());
				e.printStackTrace();
				throw new FabricException("Error in execUninstallation: NotFoundException " + e.getMessage());		
			} catch (Exception e) {
				logger.error("[TenantFabric::execUninstallation] Exception error " + e.getMessage());
				e.printStackTrace();
				throw new FabricException("Error in execUninstallation: Exception " + e.getMessage());		
			}
						// load existing roles

			List<String> existingUsers = loadExistingUsers(fabricResponse);
			debugList(fabricResponse, "existingUsers", existingUsers);

			if (!existingUsers.contains(tenantcode)) {
				WebServiceResponse deleteUserResponse = IdentityServerDelegate.build().deleteUser(tenantcode); 
				logger.info("[TenantFabric::execUninstallation] add user for role mb-topic-all-input." + tenantcode + " response " + deleteUserResponse.getMessage());
				if(deleteUserResponse.getStatusCode()<200 || deleteUserResponse.getStatusCode()>299)
					throw new FabricException("Error in deleteUser " + tenantcode + " with role mb-topic-all-input." + tenantcode + " - status: " + deleteUserResponse.getStatusCode() + " - soapMessage: " + deleteUserResponse.getMessage()); 
				fabricResponse.addLogInfo("STEP 1 Delete User - User " + tenantcode + " deleted");
			}
			else
				fabricResponse.addLogInfo("STEP 1 Delete User - User " + tenantcode + " already deleted");
			
			List<String> existingRoles = loadExistingRoles(fabricResponse);
			debugList(fabricResponse, "existingRoles", existingRoles);
			if (!existingRoles.contains("mb-topic-all-input." + tenantcode)) {
				WebServiceResponse deleteRoleResponse = IdentityServerDelegate.build().deleteRole("mb-topic-all-input." + tenantcode);
				logger.info("[TenantFabric::execUninstallation] add role mb-topic-all-input." + tenantcode + " response " + deleteRoleResponse.getMessage());
				if(deleteRoleResponse.getStatusCode()<200 || deleteRoleResponse.getStatusCode()>299)
					throw new FabricException("Error in deleteRole mb-topic-all-input." + tenantcode + " - status: " + deleteRoleResponse.getStatusCode() + " - soapMessage: " +deleteRoleResponse.getMessage()); 
				fabricResponse.addLogInfo("STEP 2 Delete Role - mb-topic-all-input." + tenantcode + " deleted");
			}
			else
				fabricResponse.addLogInfo("STEP 2 Delete Role for Topic - mb-topic-all-input." + tenantcode + " already deleted");

			
			if (!existingRoles.contains(tenantcode + "_subscriber")) {
				WebServiceResponse deleteRoleResponse = IdentityServerDelegate.build().deleteRole(tenantcode + "_subscriber");
				logger.info("[TenantFabric::execInstallation] add role " + tenantcode + "_subscriber response " + deleteRoleResponse.getMessage());
				if(deleteRoleResponse.getStatusCode()<200 || deleteRoleResponse.getStatusCode()>299)
					throw new FabricException("Error in deleteRole " + tenantcode + "_subscriber - status: " + deleteRoleResponse.getStatusCode() + " - soapMessage: " +deleteRoleResponse.getMessage()); 
				fabricResponse.addLogInfo("STEP 3 Delete Role Subscriber - Role " + tenantcode + "_subscriber deleted");
			}
			else
				fabricResponse.addLogInfo("STEP 3 Delete Role Subscriber - Role " + tenantcode + "_subscriber already deleted");
			
			
			// update tenant status
			//tenantService.updateTenantStatus(Status.UNINSTALLATION.id(), tenantcode);
			//fabricResponse.addLogInfo("STEP 4 - Updated tenant satus - " + tenantcode + ", UNINSTALLATION("+Status.UNINSTALLATION.id()+") ok ");

			fabricResponse.setStatus(FabricResponse.STATUS_SUCCESS);


		} catch (KeyManagementException e) {
			fabricResponse.setStatus(FabricResponse.STATUS_ERROR);
			fabricResponse.addLogError("KeyManagementException error " + e.getMessage());
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			fabricResponse.setStatus(FabricResponse.STATUS_ERROR);
			fabricResponse.addLogError("NoSuchAlgorithmException error " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			fabricResponse.setStatus(FabricResponse.STATUS_ERROR);
			fabricResponse.addLogError("IOException error " + e.getMessage());
			e.printStackTrace();
		} catch (FabricException e) {
			fabricResponse.setStatus(FabricResponse.STATUS_ERROR);
			fabricResponse.addLogError("FabricException error " + e.getMessage());
			e.printStackTrace();
		}
		
		return fabricResponse;

	}
	
	private List<String> loadExistingUsers(FabricResponse fabricResponse) throws FabricException, KeyManagementException, NoSuchAlgorithmException, IOException {
		List<String> existingUsers = new LinkedList<String>(); //FIXME add real method
		WebServiceResponse listUserResponse = IdentityServerDelegate.build().listUsers(null,null);
		try {
			existingUsers = IdentityServerDelegate.parseUserList(listUserResponse.getMessage());
		} catch (Exception e1) {
			throw new FabricException("Error in loading existing users: " +e1.getMessage()); 
		}
		fabricResponse.addLogDebug("Loading existing users - ok");
		return existingUsers;
	}
	
	private List<String> loadExistingRoles(FabricResponse fabricResponse)  throws FabricException, KeyManagementException, NoSuchAlgorithmException, IOException {
		List<String> existingRoles = new LinkedList<String>(); 
		WebServiceResponse roleNamesResponse = IdentityServerDelegate.build().getAllRolesNames(null,null);
		try {
			existingRoles = IdentityServerDelegate.parseRoleNames(roleNamesResponse.getMessage(), false);
		} catch (Exception e1) {
			throw new FabricException("Error in loading existing roles: " +e1.getMessage()); 
		}
		fabricResponse.addLogDebug("Loading existing roles - ok");
		
		return existingRoles;
	}

}
