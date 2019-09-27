/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.delegate;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.util.WebServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Configuration
@PropertySource(value = { "classpath:adminapi.properties" })
public class IdentityServerDelegate {
	private static final Logger logger = Logger.getLogger(IdentityServerDelegate.class);

	@Value("${identityServer.url}")
	private String identityServerUrl;

	@Value("${identityServer.user}")
	private String identityServerUser;

	@Value("${identityServer.password}")
	private String identityServerPassword;

	private static IdentityServerDelegate identityServerDelegate;

	private static final String SOAP_ENVELOP_HEADER = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://org.apache.axis2/xsd\">"
			+ "<soapenv:Header/><soapenv:Body>";

	private static final String SOAP_ENVELOP_FOOTER = "</soapenv:Body></soapenv:Envelope>";

	public IdentityServerDelegate() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		logger.info("[IdentityServerDelegate::IdentityServerDelegate]  identity server url " + identityServerUrl);
	}

	public static IdentityServerDelegate build() {
		if (identityServerDelegate == null)
			identityServerDelegate = new IdentityServerDelegate();
		return identityServerDelegate;
	}

	public WebServiceResponse getAllRolesNames(String filter, Integer limit) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		logger.info("[IdentityServerDelegate::getAllRolesNames]  getAllRolesNames");
		if (filter == null)
			filter = "*";
		if (limit == null)
			limit = -1;
		String soapAction = "getAllRolesNames";
		String xmlInput = SOAP_ENVELOP_HEADER + "<xsd:getAllRolesNames><xsd:filter>" + filter + "</xsd:filter><xsd:limit>" + limit + "</xsd:limit></xsd:getAllRolesNames>"
				+ SOAP_ENVELOP_FOOTER;

		return WebServiceDelegate.callWebService(identityServerUrl + "/services/UserAdmin", identityServerUser, identityServerPassword, xmlInput, soapAction,
				WebServiceDelegate.CONTENT_TYPE_XML);

	}

	public WebServiceResponse listUsers(String filter, Integer limit) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		logger.info("[IdentityServerDelegate::listUser]  listUser");
		if (filter == null)
			filter = "*";
		if (limit == null)
			limit = -1;
		String soapAction = "listUsers";
		String xmlInput = SOAP_ENVELOP_HEADER + "<xsd:listUsers><xsd:filter>" + filter + "</xsd:filter><xsd:limit>" + limit + "</xsd:limit></xsd:listUsers>"
				+ SOAP_ENVELOP_FOOTER;

		return WebServiceDelegate.callWebService(identityServerUrl + "/services/UserAdmin", identityServerUser, identityServerPassword, xmlInput, soapAction,
				WebServiceDelegate.CONTENT_TYPE_XML);

	}
	
	public WebServiceResponse getRolesOfUser(String userName, String filter, Integer limit) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		logger.info("[IdentityServerDelegate::getRolesOfUser]  getRolesOfUser " + userName);
		if (filter == null)
			filter = "*";
		if (limit == null)
			limit = -1;
		String soapAction = "listUser";
		String xmlInput = SOAP_ENVELOP_HEADER + "<xsd:getRolesOfUser><xsd:userName>"+userName+"</xsd:userName>\r\n" + 
				"<xsd:filter>" + filter + "</xsd:filter><xsd:limit>" + limit + "</xsd:limit></xsd:getRolesOfUser>"
				+ SOAP_ENVELOP_FOOTER;

		return WebServiceDelegate.callWebService(identityServerUrl + "/services/UserAdmin", identityServerUser, identityServerPassword, xmlInput, soapAction,
				WebServiceDelegate.CONTENT_TYPE_XML);

	}


	public WebServiceResponse addRole(String roleName, String isSharedRole) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		logger.info("[IdentityServerDelegate::addRole]  roleName: " + roleName + ", isSharedRole: " + isSharedRole);
		String soapAction = "addRole";
		String xmlInput = SOAP_ENVELOP_HEADER + "<xsd:addRole><xsd:roleName>" + roleName + "</xsd:roleName><xsd:isSharedRole>" + isSharedRole + "</xsd:isSharedRole></xsd:addRole>"
				+ SOAP_ENVELOP_FOOTER;

		return WebServiceDelegate.callWebService(identityServerUrl + "/services/UserAdmin", identityServerUser, identityServerPassword, xmlInput, soapAction,
				WebServiceDelegate.CONTENT_TYPE_XML);

	}
	
	public WebServiceResponse deleteRole(String roleName) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		logger.info("[IdentityServerDelegate::deleteRole]  roleName: " + roleName);
		String soapAction = "deleteRole";
		String xmlInput = SOAP_ENVELOP_HEADER + "<xsd:deleteRole><xsd:roleName>" + roleName + "</xsd:roleName></xsd:deleteRole>"
				+ SOAP_ENVELOP_FOOTER;

		return WebServiceDelegate.callWebService(identityServerUrl + "/services/UserAdmin", identityServerUser, identityServerPassword, xmlInput, soapAction,
				WebServiceDelegate.CONTENT_TYPE_XML);

	}

	public WebServiceResponse addUser(String userName, String password, String roles) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		logger.info("[IdentityServerDelegate::addUser]  userName: " + userName + ", password: " + password + ", roles: " + roles);
		String soapAction = "addUser";
		String xmlInput = SOAP_ENVELOP_HEADER + "<xsd:addUser><xsd:userName>" + userName + "</xsd:userName><xsd:password>" + password + "</xsd:password><xsd:roles>" + roles
				+ "</xsd:roles></xsd:addUser>" + SOAP_ENVELOP_FOOTER;

		return WebServiceDelegate.callWebService(identityServerUrl + "/services/UserAdmin", identityServerUser, identityServerPassword, xmlInput, soapAction,
				WebServiceDelegate.CONTENT_TYPE_XML);

	}

	public WebServiceResponse deleteUser(String userName) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		logger.info("[IdentityServerDelegate::deleteUser]  userName: " + userName );
		String soapAction = "deleteUser";
		String xmlInput = SOAP_ENVELOP_HEADER + "<xsd:deleteUser><xsd:userName>" + userName + "</xsd:userName></xsd:deleteUser>" + SOAP_ENVELOP_FOOTER;

		return WebServiceDelegate.callWebService(identityServerUrl + "/services/UserAdmin", identityServerUser, identityServerPassword, xmlInput, soapAction,
				WebServiceDelegate.CONTENT_TYPE_XML);

	}
	
	public WebServiceResponse addRemoveUsersOfRole(String roleName, List<String> newUsers, List<String> deletedUsers)
			throws KeyManagementException, NoSuchAlgorithmException, IOException {
		logger.info("[IdentityServerDelegate::addRole]  roleName: " + roleName + ", newUsers: " + newUsers + ", deletedUsers: " + deletedUsers);
		String soapAction = "addRemoveUsersOfRole";
		String xmlInput = SOAP_ENVELOP_HEADER + "<xsd:addRemoveUsersOfRole><xsd:roleName>" + roleName + "</xsd:roleName>";
		if (newUsers != null && newUsers.size() > 0) {
			for (String newUser : newUsers) {
				xmlInput += "<xsd:newUsers>" + newUser + "</xsd:newUsers>";
			}
		} else
			xmlInput += "<xsd:newUsers></xsd:newUsers>";
		if (deletedUsers != null && deletedUsers.size() > 0) {
			for (String deletedUser : deletedUsers) {
				xmlInput += "<xsd:deletedUsers>" + deletedUser + "</xsd:deletedUsers>";
			}
		} else
			xmlInput += "<xsd:deletedUsers></xsd:deletedUsers>";

		xmlInput += "</xsd:addRemoveUsersOfRole>" + SOAP_ENVELOP_FOOTER;

		return WebServiceDelegate.callWebService(identityServerUrl + "/services/UserAdmin", identityServerUser, identityServerPassword, xmlInput, soapAction,
				WebServiceDelegate.CONTENT_TYPE_XML);

	}

	public WebServiceResponse addRemoveRolesOfUser(String userName, List<String> newRoles, List<String> deletedRoles)
			throws KeyManagementException, NoSuchAlgorithmException, IOException {
		logger.info("[IdentityServerDelegate::addRemoveRolesOfUser]  userName: " + userName);
		String soapAction = "addRemoveRolesOfUser";
		String xmlInput = SOAP_ENVELOP_HEADER + "<xsd:addRemoveRolesOfUser><xsd:userName>" + userName + "</xsd:userName>";
		if (newRoles != null && newRoles.size() > 0) {
			for (String newRole : newRoles) {
				xmlInput += "<xsd:newRoles>" + newRole + "</xsd:newRoles>";
			}
		} else
			xmlInput += "<xsd:newRoles></xsd:newRoles>";
		if (deletedRoles != null && deletedRoles.size() > 0) {
			for (String deletedRole : deletedRoles) {
				xmlInput += "<xsd:deletedRoles>" + deletedRole + "</xsd:deletedRoles>";
			}
		} else
			xmlInput += "<xsd:deletedRoles></xsd:deletedRoles>";

		xmlInput += "</xsd:addRemoveRolesOfUser> " + SOAP_ENVELOP_FOOTER;

		return WebServiceDelegate.callWebService(identityServerUrl + "/services/UserAdmin", identityServerUser, identityServerPassword, xmlInput, soapAction,
				WebServiceDelegate.CONTENT_TYPE_XML);

	}

	
	public static void main(String[] args) {
		String soapMessage ="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
				"<soapenv:Body>" +
				"<ns:getAllRolesNamesResponse xmlns:ns=\"http://org.apache.axis2/xsd\" xmlns:ax2644=\"http://common.mgt.user.carbon.wso2.org/xsd\">" +
				"<ns:return xsi:type=\"ax2644:FlaggedName\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<ax2644:dn xsi:nil=\"true\"/>" +
				"<ax2644:domainName xsi:nil=\"true\"/>" +
				"<ax2644:editable>true</ax2644:editable>" +
				"<ax2644:itemDisplayName xsi:nil=\"true\"/>" +
				"<ax2644:itemName>mb-topic-all-input.trial0055</ax2644:itemName>" +
				"<ax2644:readOnly>false</ax2644:readOnly>" +
				"<ax2644:roleType>External</ax2644:roleType>" +
				"<ax2644:selected>true</ax2644:selected>" +
				"<ax2644:shared>false</ax2644:shared>" +
				"</ns:return>" +
				"<ns:return xsi:type=\"ax2644:FlaggedName\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<ax2644:dn xsi:nil=\"true\"/>" +
				"<ax2644:domainName xsi:nil=\"true\"/>" +
				"<ax2644:editable>true</ax2644:editable>" +
				"<ax2644:itemDisplayName xsi:nil=\"true\"/>" +
				"<ax2644:itemName>trial0055_subscriber</ax2644:itemName>" +
				"<ax2644:readOnly>false</ax2644:readOnly>" +
				"<ax2644:roleType>External</ax2644:roleType>" +
				"<ax2644:selected>false</ax2644:selected>" +
				"<ax2644:shared>false</ax2644:shared>" +
				"</ns:return>" +
				"<ns:return xsi:type=\"ax2644:FlaggedName\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<ax2644:dn xsi:nil=\"true\"/>" +
				"<ax2644:domainName xsi:nil=\"true\"/>" +
				"<ax2644:editable>false</ax2644:editable>" +
				"<ax2644:itemDisplayName/>" +
				"<ax2644:itemName>false</ax2644:itemName>" +
				"<ax2644:readOnly>false</ax2644:readOnly>" +
				"<ax2644:roleType xsi:nil=\"true\"/>" +
				"<ax2644:selected>false</ax2644:selected>" +
				"<ax2644:shared>false</ax2644:shared>" +
				"</ns:return>" +
				"</ns:getAllRolesNamesResponse>" +
				"</soapenv:Body>" +
				"</soapenv:Envelope>";
		
//		soapMessage = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
//				"<soapenv:Body>" +
//				"<ns:listUsersResponse xmlns:ns=\"http://org.apache.axis2/xsd\" xmlns:ax2644=\"http://common.mgt.user.carbon.wso2.org/xsd\">" +
//				"<ns:return>AAAAAA00A11B000J</ns:return>" +
//				"<ns:return>AAAAAA00A11C000K</ns:return>" +
//				"<ns:return>AAAAAA00A11K000S</ns:return>" +
//				"<ns:return>AAAAAA00A11Q000Y</ns:return>" +
//				"<ns:return>AAAAAA00A11R000Z</ns:return>" +
//				"<ns:return>AAAAAA00A11S000A</ns:return>" +
//				"<ns:return>AAAAAA00A11T000B</ns:return>" +
//				"<ns:return>AAAAAA00A11V000D</ns:return>" +
//				"<ns:return>AAAAAA00B77B000F</ns:return>" +
//				"<ns:return>admin</ns:return>" +
//				"</ns:listUsersResponse>" +
//				"</soapenv:Body>" +
//				"</soapenv:Envelope>";
		try {
//			IdentityServerDelegate.parseUserList(soapMessage);
			List<String> lista = IdentityServerDelegate.parseRoleNames(soapMessage, true);
			System.out.println(lista.get(0));
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<String> parseRoleNames(String soapMessage, boolean onlySelected) throws ParserConfigurationException, SAXException, IOException {
		List<String> result = new LinkedList<>();
		if (soapMessage != null) {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource(new StringReader(soapMessage));
			Document doc = db.parse(is);

			NodeList roleNamesNodeList = doc.getFirstChild().getFirstChild().getFirstChild().getChildNodes();
			if (roleNamesNodeList != null) {
				for (int i = 0; i < roleNamesNodeList.getLength(); i++) {
					Node roleNamesNode = roleNamesNodeList.item(i);
					NodeList childNodes = roleNamesNode.getChildNodes();
					String itemName = null;
					String selected = null;
					for (int j= 0; j < childNodes.getLength(); j++) {
						if(childNodes.item(j).getLocalName().equals("itemName"))
							itemName = childNodes.item(j).getTextContent();
						if(childNodes.item(j).getLocalName().equals("selected"))
							selected =  childNodes.item(j).getTextContent();
					}
					if(!onlySelected)
						selected = "true";
					
					if(itemName!=null && "true".equals(selected))
						result.add(itemName);
				}
			}

		}
//		System.out.println("roles");
//		for (String string : result) {
//			System.out.println(string);
//		}
		return result;
	}
	
	public static List<String> parseUserList(String soapMessage) throws ParserConfigurationException, SAXException, IOException {
		List<String> result = new LinkedList<>();
		if (soapMessage != null) {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource(new StringReader(soapMessage));
			Document doc = db.parse(is);

			NodeList userList = doc.getFirstChild().getFirstChild().getFirstChild().getChildNodes();
			if (userList != null) {
				for (int i = 0; i < userList.getLength(); i++) {
					result.add(userList.item(i).getTextContent());
				}
			}

		}
		System.out.println("roles");
		for (String string : result) {
			System.out.println(string);
		}
		return result;
	}


}
