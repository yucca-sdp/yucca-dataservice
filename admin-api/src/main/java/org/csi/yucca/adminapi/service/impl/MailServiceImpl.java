/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.service.impl;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.request.PostTenantSocialRequest;
import org.csi.yucca.adminapi.request.TenantRequest;
import org.csi.yucca.adminapi.response.PostDatasetResponse;
import org.csi.yucca.adminapi.service.MailService;
import org.csi.yucca.adminapi.util.EmailInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@PropertySources({ @PropertySource("classpath:email-service.properties") })
public class MailServiceImpl implements MailService {

	@Value("${req.action.mail-to}")
	private String requestActionMailTo;

	@Value("${req.action.mail-from}")
	private String requestActionMailfrom;

	@Value("${req.action.mail-name}")
	private String requestActionMailName;

	@Value("${info.opendata.creation.mail-to}")
	private String infoOpendataCreationMailTo;
	
	@Autowired
	JavaMailSender mailSender;

	private static final Logger logger = Logger.getLogger(MailServiceImpl.class);

	@Override
	public void sendEmail(EmailInfo emailInfo) {
		
		logger.info("[MailServiceImpl::sendEmail] - protocol " + ((JavaMailSenderImpl)mailSender).getProtocol());

		MimeMessagePreparator preparator = getMessagePreparator(emailInfo);

		try {
			mailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}

	}

	/**
	 * 
	 * @param emailInfo
	 * @return
	 */
	private MimeMessagePreparator getMessagePreparator(final EmailInfo emailInfo) {

		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {
				// MimeMessageHelper mimeMsgHelperObj = new
				// MimeMessageHelper(mimeMessage, true, "UTF-8");
				MimeMessageHelper mimeMsgHelperObj = new MimeMessageHelper(mimeMessage,emailInfo.getMessageHtml()!=null, "UTF-8");
				mimeMsgHelperObj.setTo(emailInfo.getEmail());
				mimeMsgHelperObj.setFrom(emailInfo.getFrom());
				if(emailInfo.getMessageHtml()!=null) {
					mimeMsgHelperObj.setText(emailInfo.getMessage(), emailInfo.getMessageHtml());
				}
				else
					mimeMsgHelperObj.setText(emailInfo.getMessage());
				mimeMsgHelperObj.setSubject(emailInfo.getSubject());
				
			}

		};

		return preparator;
	}

	@Override
	public void sendOpendataCreationInformative(PostDatasetResponse response,
			Tenant tenant, JwtUser authorizedUser, String userportalUrl) {
		sendOpendataCreationInformative(
				response, 
				userportalUrl + "/userportal/#/dataexplorer/detail/" + tenant.getTenantcode() + "/" + response.getDatasetcode(), 
				tenant.getTenantcode(), 
				authorizedUser.getGivenname() + " " + authorizedUser.getLastname());
	}
	
	@Override
	public void sendOpendataCreationInformative(PostDatasetResponse response, String dataSetStoreLink, String tenantCode, String user) {
		
		logger.info("[MailServiceImpl::sendOpendataCreationInformative] START ");
		String datasetCode = response.getDatasetcode();
		StringBuilder emailMessage = new StringBuilder().append("E' stato creato o aggiornato il dataset pubblico" + datasetCode  + "\n\n" +  
				"Dettaglio: ").append("\n")
				.append("Dataset Code: " + datasetCode).append("\n")
				.append("Dataset Name: " + response.getDatasetname()).append("\n")
				.append("Dataset Store Link:" + dataSetStoreLink).append("\n")
				.append("Tenant Code: " + tenantCode).append("\n")
				.append("User: " + user).append("\n");

		String emailMessageHtmlContent = "<h2>E' stato creato o aggiornato il dataset pubblico Datasetcomplesso_3243</h2>\r\n" + 
				"    <h3>Dettaglio</h3> \r\n" + 
				"    <table style=' font-size: 14px;'><tbody>\r\n" + 
				"    	<tr><td style='white-space: nowrap;padding: 0 20px 10px 0;;'><b>Dataset Code</b></td><td> "+datasetCode+"</td></tr>\r\n" + 
				"    	<tr><td style='white-space: nowrap;padding: 0 20px 10px 0;;'><b>Dataset Name</b></td><td> "+response.getDatasetname()+"</td></tr>\r\n" + 
				"    	<tr><td style='white-space: nowrap;padding: 0 20px 10px 0;;'><b>Dataset Store Link</b></td><td> <a target=\"_blank\" href='"+dataSetStoreLink+"'>"+dataSetStoreLink+"</a></td></tr>\r\n" + 
				"    	<tr><td style='white-space: nowrap;padding: 0 20px 10px 0;;'><b>Tenant Code</b></td><td> " + tenantCode + "</td></tr>\r\n" + 
				"    	<tr><td style='white-space: nowrap;padding: 0 20px 10px 0;;'><b>User</b></td><td> "+user+"</td></tr>\r\n" + 
				"    </tbody></table>";
		
		String emailMessageHtml = formatHtmlMail(emailMessageHtmlContent);
		StringBuilder emailSubject = new StringBuilder().append("Smartdataplatform - Creazione/Aggiornamento dataset pubblico: ").append(" [")
				.append(datasetCode).append("]");
		
		logger.info("[DatasetServiceImpl::sendOpendataCreationInformative] subject: " + emailSubject + " - to " + infoOpendataCreationMailTo);

		sendEmail(emailSubject.toString(), emailMessage.toString(), emailMessageHtml.toString(), requestActionMailfrom, infoOpendataCreationMailTo);
	}
	
	
	@Override
	public void sendTenantRequestInstallationEmail(final PostTenantSocialRequest tenantRequest) {

		String emailMessage = buildTenantRequestInstallationEmailMessage(tenantRequest);

		String emailSubject = buildTenantRequestInstallationEmailSubject(tenantRequest);

		sendEmail(emailSubject, emailMessage, requestActionMailfrom, requestActionMailTo);

	}

	@Override
	public void sendTenantCreationEmail(final TenantRequest tenantRequest) {

		String emailMessage = buildTenantCreationEmailMessage(tenantRequest);

		String emailSubject = buildTenantCreationEmailSubject(tenantRequest);

		sendEmail(emailSubject, emailMessage, requestActionMailfrom, requestActionMailTo);

	}

	@Override
	public void sendStreamRequestInstallationEmail(final DettaglioStream dettaglioStream) {
		sendStreamRrequestActionEmail(dettaglioStream, "Stream installation request");
	}

	@Override
	public void sendStreamRequestUninstallationEmail(final DettaglioStream dettaglioStream) {
		sendStreamRrequestActionEmail(dettaglioStream, "Stream uninstallation request");
	}

	/**
	 * 
	 * @param subject
	 * @param message
	 */
	private void sendEmail(String subject, String message,  String mailFrom, String mailTo) {
		sendEmail(EmailInfo.build(mailTo).from(mailFrom).message(message)
				.name(requestActionMailName).subject(subject));
	}
	
	/**
	 * 
	 * @param subject
	 * @param message
	 * @param messageHtml
	 */
	private void sendEmail(String subject, String message, String messageHtml,  String mailFrom, String mailTo) {
		sendEmail(EmailInfo.build(mailTo).from(mailFrom).message(message)
				.name(requestActionMailName).subject(subject).messageHtml(messageHtml));
	}

	/**
	 * 
	 * @param dettaglioStream
	 * @param actionDescription
	 */
	private void sendStreamRrequestActionEmail(final DettaglioStream dettaglioStream, String actionDescription) {

		String emailMessage = buildStreamRrequestActionEmailMessage(dettaglioStream, actionDescription);

		String emailSubject = buildStreamRrequestActionEmailSubject(dettaglioStream, actionDescription);

		sendEmail(emailSubject, emailMessage, requestActionMailfrom, requestActionMailTo);
	}

	/**
	 * 
	 * @param dettaglioStream
	 * @param actionDescription
	 * @return
	 */
	private String buildStreamRrequestActionEmailMessage(DettaglioStream dettaglioStream, String actionDescription) {
		StringBuilder emailMessage = new StringBuilder().append(actionDescription).append(": ").append("\n\n")
				.append("Stream Code: ").append(dettaglioStream.getStreamcode()).append("\n")
				.append("Smart Object Code: ").append(dettaglioStream.getSmartObjectCode()).append("\n")
				.append("Version: ").append(dettaglioStream.getDatasourceversion()).append("\n").append("Tenant Code: ")
				.append(dettaglioStream.getTenantCode()).append("\n");

		return emailMessage.toString();
	}

	/**
	 * 
	 * @param dettaglioStream
	 * @param actionDescription
	 * @return
	 */
	private String buildStreamRrequestActionEmailSubject(DettaglioStream dettaglioStream, String actionDescription) {
		StringBuilder emailSubject = new StringBuilder().append(actionDescription).append(": ").append("stream code")
				.append(" [").append(dettaglioStream.getStreamcode()).append("], ").append("tenant code").append(" [")
				.append(dettaglioStream.getTenantCode()).append("], ").append("version").append(" [")
				.append(dettaglioStream.getDatasourceversion()).append("]");
		return emailSubject.toString();
	}

	/**
	 * 
	 * @param tenantRequest
	 * @return
	 */
	private String buildTenantRequestInstallationEmailMessage(PostTenantSocialRequest tenantRequest) {
		StringBuilder emailMessage = new StringBuilder().append("Tenant installation request: ").append("\n\n")
				.append("Tenant Code: " + tenantRequest.getTenantcode()).append("\n")
				.append("Username: " + tenantRequest.getUsername()).append("\n")
				.append("Name: " + tenantRequest.getUserfirstname()).append("\n")
				.append("Surname: " + tenantRequest.getUserlastname()).append("\n")
				.append("Email: " + tenantRequest.getUseremail()).append("\n");
		return emailMessage.toString();
	}

	/**
	 * 
	 * @param tenantRequest
	 * @return
	 */
	private String buildTenantCreationEmailMessage(TenantRequest tenantRequest) {
		StringBuilder emailMessage = new StringBuilder().append("Created tenant: ").append("\n\n")
				.append("Tenant Code: " + tenantRequest.getTenantcode()).append("\n")
				.append("Username: " + tenantRequest.getUsername()).append("\n")
				.append("Name: " + tenantRequest.getUserfirstname()).append("\n")
				.append("Surname: " + tenantRequest.getUserlastname()).append("\n")
				.append("Email: " + tenantRequest.getUseremail()).append("\n");
		return emailMessage.toString();
	}

	/**
	 * 
	 * @param tenantRequest
	 * @return
	 */
	private String buildTenantRequestInstallationEmailSubject(PostTenantSocialRequest tenantRequest) {
		StringBuilder emailSubject = new StringBuilder().append("Tenant installation request: ").append("tenant code")
				.append(" [").append(tenantRequest.getTenantcode()).append("]");

		return emailSubject.toString();
	}

	/**
	 * 
	 * @param tenantRequest
	 * @return
	 */
	private String buildTenantCreationEmailSubject(TenantRequest tenantRequest) {
		StringBuilder emailSubject = new StringBuilder().append("Created tenant: ").append("tenant code").append(" [")
				.append(tenantRequest.getTenantcode()).append("]");

		return emailSubject.toString();
	}
	
	private String  formatHtmlMail(String content) {
		String out = "	<table style='border-collapse: collapse; border-collapse: collapse; width: 100%; font-family: sans-serif; background-color: #f5f5f5;'>\r\n" + 
				"		<tbody>\r\n" + 
				"			<tr>\r\n" + 
				"				<td align='center' style=\"padding: 20px 10px 10px;\">\r\n" + 
				"					<table style='border-collapse: collapse; border-collapse: collapse; width: 600px; font-family: sans-serif; line-height: 1.500;'>\r\n" + 
				"						<tbody>\r\n" + 
				"							<tr>\r\n" + 
				"								<td style='padding: 10px 20px; border: solid #ddd 1px; background-color: #fff; font-weight: normal; line-height: 1.200;'>\r\n" + 
				content +
				"								</td>\r\n" + 
				"							</tr>\r\n" + 
				"						</tbody>\r\n" + 
				"					</table>\r\n" + 
				"					<p style='padding: 12px;font-size: 12px; text-align: center;color:#707070'>This message is automatically generated by <b><a href='https://userportal.smartdatanet.it/userportal'>Smartdataplatform</a></b></p>	\r\n" + 
				"				</td>\r\n" + 
				"			</tr>\r\n" + 
				"		</tbody>\r\n" + 
				"	</table>";
		return out;
	}

}