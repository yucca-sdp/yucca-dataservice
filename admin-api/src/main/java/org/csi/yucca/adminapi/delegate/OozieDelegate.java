/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.delegate;



import java.io.IOException;

import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.request.ActionOozieRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/*import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonInclude;*/



@Configuration
@PropertySources({ @PropertySource("classpath:adminapi.properties"), @PropertySource("classpath:adminapiSecret.properties") })
public class OozieDelegate {

	private static final Logger logger = Logger.getLogger(OozieDelegate.class);

	private static OozieDelegate oozieDelegate;

	@Value("${oozie.xml.metastore}")
	private String xmlMetastore;

	@Value("${oozie.zookeper.quorum}")
	private String zookeperQuorum;

	@Value("${oozie.libpath}")
	private String libpath;

	@Value("${oozie.user}")
	private String user;

	@Value("${oozie.hbase.principal}")
	private String hbasePrincipal;

	@Value("${oozie.mapreduce.user}")
	private String mapreduceUser;

	@Value("${oozie.master}")
	private String master;

	@Value("${oozie.wf.oo.path}")
	private String wfOOPath;

	@Value("${oozie.hive.principal}")
	private String hivePrincipal;

	@Value("${oozie.queue.name}")
	private String queueName;

	@Value("${oozie.wf.path}")
	private String wfPath;

	@Value("${oozie.jdbc.url}")
	private String jdbcUrl;

	@Value("${oozie.job.tracker}")
	private String jobTracker;

	@Value("${oozie.name.node}")
	private String nameNode;

	@Value("${oozie.promotion.wf.application.path}")
	private String promotionWfApplicationPath;
	
	@Value("${oozie.pubblication.wf.application.path}")
	private String pubblicationWfApplicationPath;
	
	@Value("${oozie.pubblication.align.date}")
	private String pubblicationAlignDate;
	
	
	
	@Value("${oozie.url}")
	private String oozieUrl;
	
	public OozieDelegate() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public static OozieDelegate build() {
		if (oozieDelegate == null)
			oozieDelegate = new OozieDelegate();
		return oozieDelegate;
	}

	public String  makePromotion(ActionOozieRequest actionOozieRequest) throws  NotFoundException, Exception {
		
		String wfName = actionOozieRequest.getTenantCode() + "_" + actionOozieRequest.getOperationCode();
		
		StringBuffer xmlInput = new StringBuffer();
		xmlInput.append("<configuration>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>metastore</name>");
		xmlInput.append("  <value>"+xmlMetastore+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>zookeeperQuorum</name>");
		xmlInput.append("  <value>"+zookeperQuorum+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>oozie.libpath</name>");
		xmlInput.append("  <value>"+libpath+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>oozie.use.system.libpath</name>");
		xmlInput.append("  <value>true</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>user.name</name>");
		xmlInput.append("  <value>"+user+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>hbasePrincipal</name>");
		xmlInput.append("  <value>"+hbasePrincipal+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>mapreduce.job.user.name</name>");
		xmlInput.append("  <value>"+mapreduceUser+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>master</name>");
		xmlInput.append("  <value>"+master+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>wf_oo_path</name>");
		xmlInput.append("  <value>"+wfOOPath+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>hivePrincipal</name>");
		xmlInput.append("  <value>"+hivePrincipal+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>queueName</name>");
		xmlInput.append("  <value>"+queueName+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>wf_path</name>");
		xmlInput.append("  <value>"+wfPath+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>jdbcURL</name>");
		xmlInput.append("  <value>"+jdbcUrl+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>jobTracker</name>");
		xmlInput.append("  <value>"+jobTracker+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>nameNode</name>");
		xmlInput.append("  <value>"+nameNode+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>wfname</name>");
		xmlInput.append("  <value>"+wfName+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>eleIds</name>");
		xmlInput.append("  <value>"+actionOozieRequest.getEleIds()+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>addbdaInfo</name>");
		xmlInput.append("  <value>"+actionOozieRequest.getAddBdaInfo()+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>addBdaUniqueId</name>");
		xmlInput.append("  <value>"+actionOozieRequest.getAddBdaUniqueId()+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>prjName</name>");
		xmlInput.append("  <value>"+actionOozieRequest.getPrjName()+"</value>");
		xmlInput.append("</property> ");
		xmlInput.append("<property>");
		xmlInput.append("  <name>oozie.wf.application.path</name>");
		xmlInput.append("  <value>"+promotionWfApplicationPath+"</value>");
		xmlInput.append("</property>");
		xmlInput.append(" </configuration>");
		
		
		 CloseableHttpClient httpclient = HttpClients.createDefault();
			String oozieCompleteUrl = oozieUrl + "/v1/jobs?action=start";
			String response = HttpDelegate.makeHttpPost(httpclient, oozieCompleteUrl, null, null, null, xmlInput.toString(), ContentType.APPLICATION_XML);
			
			return response;
		}
	
public String  makePubblication(ActionOozieRequest actionOozieRequest) throws  NotFoundException, Exception {
		
		String wfName = actionOozieRequest.getTenantCode() + "_" + actionOozieRequest.getOperationCode();
		
		StringBuffer xmlInput = new StringBuffer();
		xmlInput.append("<configuration>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>metastore</name>");
		xmlInput.append("  <value>"+xmlMetastore+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>zookeeperQuorum</name>");
		xmlInput.append("  <value>"+zookeperQuorum+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>oozie.libpath</name>");
		xmlInput.append("  <value>"+libpath+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>oozie.use.system.libpath</name>");
		xmlInput.append("    <value>true</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>user.name</name>");
		xmlInput.append("  <value>"+user+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>hbasePrincipal</name>");
		xmlInput.append("  <value>"+hbasePrincipal+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>mapreduce.job.user.name</name>");
		xmlInput.append("  <value>"+mapreduceUser+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>master</name>");
		xmlInput.append("  <value>"+master+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>wf_oo_path</name>");
		xmlInput.append("  <value>"+wfOOPath+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>hivePrincipal</name>");
		xmlInput.append("  <value>"+hivePrincipal+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>queueName</name>");
		xmlInput.append("  <value>"+queueName+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>wf_path</name>");
		xmlInput.append("  <value>"+wfPath+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>jdbcURL</name>");
		xmlInput.append("  <value>"+jdbcUrl+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>jobTracker</name>");
		xmlInput.append("  <value>"+jobTracker+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>nameNode</name>");
		xmlInput.append("  <value>"+nameNode+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>wfname</name>");
		xmlInput.append("  <value>"+wfName+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>eleIds</name>");
		xmlInput.append("  <value>"+actionOozieRequest.getEleIds()+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>prjName</name>");
		xmlInput.append("  <value>"+actionOozieRequest.getPrjName()+"</value>");
		xmlInput.append("</property> ");
		xmlInput.append("<property>");
		xmlInput.append("    <name>tenantCode</name>");
		xmlInput.append("    <value>"+actionOozieRequest.getTenantCode()+"</value>");
		xmlInput.append("</property>"); 
		xmlInput.append("<property>");
		xmlInput.append("    <name>toWrite</name>");
		xmlInput.append("    <value>"+actionOozieRequest.getToWrite()+"</value>");
		xmlInput.append("</property> ");
		xmlInput.append("<property>");
		xmlInput.append("    <name>alignDate</name>");
		xmlInput.append("    <value>"+pubblicationAlignDate+"</value>");
		xmlInput.append("</property>"); 
		xmlInput.append("<property>");
		xmlInput.append("    <name>makeHiveTOCsv</name>");
		xmlInput.append("    <value>"+actionOozieRequest.getMakeHiveToCsv()+"</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>oozie.wf.application.path</name>");
		xmlInput.append("  <value>"+pubblicationWfApplicationPath+"</value>");
		xmlInput.append("</property>");  
		xmlInput.append("</configuration>");
		
		
		 CloseableHttpClient httpclient = HttpClients.createDefault();
			String oozieCompleteUrl = oozieUrl + "/v1/jobs?action=start";
			String response = HttpDelegate.makeHttpPost(httpclient, oozieCompleteUrl, null, null, null, xmlInput.toString(), ContentType.APPLICATION_XML);
			
			return response;
		}
	
}
