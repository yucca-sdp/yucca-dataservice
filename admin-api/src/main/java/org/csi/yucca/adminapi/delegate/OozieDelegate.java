/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.delegate;

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
@PropertySources({ @PropertySource("classpath:adminapi.properties"),
		@PropertySource("classpath:adminapiSecret.properties") })
public class OozieDelegate {

	private static final Logger logger = Logger.getLogger(OozieDelegate.class);

	private static OozieDelegate oozieDelegate;

	@Value("${oozie.hive.metastore}")
	private String hiveMetastoreHdp2;

	@Value("${oozie.zookeper.quorum}")
	private String zookeperQuorumHdp2;

	@Value("${oozie.libpath}")
	private String libpathHdp2;

	@Value("${oozie.username}")
	private String usernameHdp2;

	@Value("${oozie.hbase.principal}")
	private String hbasePrincipalHdp2;

	@Value("${oozie.mapreduce.user}")
	private String mapreduceUserHdp2;

	@Value("${oozie.master}")
	private String masterHdp2;

	@Value("${oozie.wf.oo.path}")
	private String wfOOPathHdp2;

	@Value("${oozie.hive.principal}")
	private String hivePrincipalHdp2;

	@Value("${oozie.queue.name}")
	private String queueNameHdp2;

	@Value("${oozie.wf.path}")
	private String wfPathHdp2;

	@Value("${oozie.jdbc.url}")
	private String jdbcUrlHdp2;

	@Value("${oozie.job.tracker}")
	private String jobTrackerHdp2;

	@Value("${oozie.name.node}")
	private String nameNodeHdp2;

	@Value("${oozie.promotion.wf.application.path}")
	private String promotionWfApplicationPathHdp2;

	@Value("${oozie.pubblication.wf.application.path}")
	private String pubblicationWfApplicationPathHdp2;

	@Value("${oozie.pubblication.align.date}")
	private String pubblicationAlignDateHdp2;

	@Value("${oozie.url}")
	private String oozieUrlHdp2;

	@Value("${oozie.hdp3.xml.metastore}")
	private String hiveMetastoreHdp3;

	@Value("${oozie.hdp3.zookeper.quorum}")
	private String zookeperQuorumHdp3;

	@Value("${oozie.hdp3.libpath}")
	private String libpathHdp3;

	@Value("${oozie.hdp3.username}")
	private String usernameHdp3;

	@Value("${oozie.hdp3.hbase.principal}")
	private String hbasePrincipalHdp3;

	@Value("${oozie.hdp3.mapreduce.user}")
	private String mapreduceUserHdp3;

	@Value("${oozie.hdp3.master}")
	private String masterHdp3;

	@Value("${oozie.hdp3.wf.oo.path}")
	private String wfOOPathHdp3;

	@Value("${oozie.hdp3.hive.principal}")
	private String hivePrincipalHdp3;

	@Value("${oozie.hdp3.queue.name}")
	private String queueNameHdp3;

	@Value("${oozie.hdp3.wf.path}")
	private String wfPathHdp3;

	@Value("${oozie.hdp3.jdbc.url}")
	private String jdbcUrlHdp3;

	@Value("${oozie.hdp3.job.tracker}")
	private String jobTrackerHdp3;

	@Value("${oozie.hdp3.name.node}")
	private String nameNodeHdp3;

	@Value("${oozie.hdp3.promotion.wf.application.path}")
	private String promotionWfApplicationPathHdp3;

	@Value("${oozie.hdp3.pubblication.wf.application.path}")
	private String pubblicationWfApplicationPathHdp3;

	@Value("${oozie.hdp3.pubblication.align.date}")
	private String pubblicationAlignDateHdp3;

	@Value("${oozie.hdp3.url}")
	private String oozieUrlHdp3;

	@Value("${oozie.hdp3.user}")
	private String oozieUserHdp3;

	@Value("${oozie.hdp3.password}")
	private String ooziePasswordHdp3;

	public OozieDelegate() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public static OozieDelegate build() {
		if (oozieDelegate == null)
			oozieDelegate = new OozieDelegate();
		return oozieDelegate;
	}

	public String makePromotion(ActionOozieRequest actionOozieRequest, String hdpVersion)
			throws NotFoundException, Exception {

		logger.info("[OozieDelegate::makePromotion] hdpVersion: " + hdpVersion);

		String wfName = actionOozieRequest.getTenantCode() + "_" + actionOozieRequest.getOperationCode();

		StringBuffer xmlInput = new StringBuffer();
		xmlInput.append("<configuration>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>metastore</name>");
		xmlInput.append("  <value>" + getHiveMetastore(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>zookeeperQuorum</name>");
		xmlInput.append("  <value>" + getZookeperQuorum(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>oozie.libpath</name>");
		xmlInput.append("  <value>" + getLibpath(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>oozie.use.system.libpath</name>");
		xmlInput.append("  <value>true</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>user.name</name>");
		xmlInput.append("  <value>" + getUsername(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>hbasePrincipal</name>");
		xmlInput.append("  <value>" + getHbasePrincipal(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>mapreduce.job.user.name</name>");
		xmlInput.append("  <value>" + getMapreduceUser(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>master</name>");
		xmlInput.append("  <value>" + getMaster(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>wf_oo_path</name>");
		xmlInput.append("  <value>" + getWfOOPath(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>hivePrincipal</name>");
		xmlInput.append("  <value>" + getHivePrincipal(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>queueName</name>");
		xmlInput.append("  <value>" + getQueueName(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>wf_path</name>");
		xmlInput.append("  <value>" + getWfPath(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>jdbcURL</name>");
		xmlInput.append("  <value>" + getJdbcUrl(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>jobTracker</name>");
		xmlInput.append("  <value>" + getJobTracker(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>nameNode</name>");
		xmlInput.append("  <value>" + getNameNode(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>wfname</name>");
		xmlInput.append("  <value>" + wfName + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>eleIds</name>");
		xmlInput.append("  <value>" + actionOozieRequest.getEleIds() + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>addbdaInfo</name>");
		xmlInput.append("  <value>" + actionOozieRequest.getAddBdaInfo() + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>addBdaUniqueId</name>");
		xmlInput.append("  <value>" + actionOozieRequest.getAddBdaUniqueId() + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("  <name>prjName</name>");
		xmlInput.append("  <value>" + actionOozieRequest.getPrjName() + "</value>");
		xmlInput.append("</property> ");
		xmlInput.append("<property>");
		xmlInput.append("  <name>oozie.wf.application.path</name>");
		xmlInput.append("  <value>" + getPromotionWfApplicationPath(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append(" </configuration>");

		CloseableHttpClient httpclient = HttpClients.createDefault();
		String oozieCompleteUrl = getOozieUrl(hdpVersion) + "/v1/jobs?action=start";
		String response = HttpDelegate.makeHttpPost(httpclient, oozieCompleteUrl, null, getOozieUser(hdpVersion),
				getOoziePassword(hdpVersion), xmlInput.toString(), ContentType.APPLICATION_XML);
		return response;
	}

	public String makePubblication(ActionOozieRequest actionOozieRequest, String hdpVersion)
			throws NotFoundException, Exception {

		logger.info("[OozieDelegate::makePubblication] hdpVersion: " + hdpVersion);

		String wfName = actionOozieRequest.getTenantCode() + "_" + actionOozieRequest.getOperationCode();

		StringBuffer xmlInput = new StringBuffer();
		xmlInput.append("<configuration>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>metastore</name>");
		xmlInput.append("  <value>" + getHiveMetastore(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>zookeeperQuorum</name>");
		xmlInput.append("  <value>" + getZookeperQuorum(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>oozie.libpath</name>");
		xmlInput.append("  <value>" + getLibpath(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>oozie.use.system.libpath</name>");
		xmlInput.append("    <value>true</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>user.name</name>");
		xmlInput.append("  <value>" + getUsername(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>hbasePrincipal</name>");
		xmlInput.append("  <value>" + getHbasePrincipal(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>mapreduce.job.user.name</name>");
		xmlInput.append("  <value>" + getMapreduceUser(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>master</name>");
		xmlInput.append("  <value>" + getMaster(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>wf_oo_path</name>");
		xmlInput.append("  <value>" + getWfOOPath(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>hivePrincipal</name>");
		xmlInput.append("  <value>" + getHivePrincipal(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>queueName</name>");
		xmlInput.append("  <value>" + getQueueName(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>wf_path</name>");
		xmlInput.append("  <value>" + getWfPath(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>jdbcURL</name>");
		xmlInput.append("  <value>" + getJdbcUrl(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>jobTracker</name>");
		xmlInput.append("  <value>" + getJobTracker(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>nameNode</name>");
		xmlInput.append("  <value>" + getNameNode(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>wfname</name>");
		xmlInput.append("  <value>" + wfName + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>eleIds</name>");
		xmlInput.append("  <value>" + actionOozieRequest.getEleIds() + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>prjName</name>");
		xmlInput.append("  <value>" + actionOozieRequest.getPrjName() + "</value>");
		xmlInput.append("</property> ");
		xmlInput.append("<property>");
		xmlInput.append("    <name>tenantCode</name>");
		xmlInput.append("    <value>" + actionOozieRequest.getTenantCode() + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>toWrite</name>");
		xmlInput.append("    <value>" + actionOozieRequest.getToWrite() + "</value>");
		xmlInput.append("</property> ");
		xmlInput.append("<property>");
		xmlInput.append("    <name>alignDate</name>");
		xmlInput.append("    <value>" + getPubblicationAlignDate(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>makeHiveTOCsv</name>");
		xmlInput.append("    <value>" + actionOozieRequest.getMakeHiveToCsv() + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("<property>");
		xmlInput.append("    <name>oozie.wf.application.path</name>");
		xmlInput.append("  <value>" + getPubblicationWfApplicationPath(hdpVersion) + "</value>");
		xmlInput.append("</property>");
		xmlInput.append("</configuration>");

		CloseableHttpClient httpclient = HttpClients.createDefault();
		String oozieCompleteUrl = getOozieUrl(hdpVersion) + "/v1/jobs?action=start";
		String response = HttpDelegate.makeHttpPost(httpclient, oozieCompleteUrl, null, getOozieUser(hdpVersion),
				getOoziePassword(hdpVersion), xmlInput.toString(), ContentType.APPLICATION_XML);
		return response;
	}

	public String getHiveMetastore(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? hiveMetastoreHdp3 : hiveMetastoreHdp2;
	}

	public String getZookeperQuorum(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? zookeperQuorumHdp3 : zookeperQuorumHdp2;
	}

	public String getLibpath(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? libpathHdp3 : libpathHdp2;
	}

	public String getUsername(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? usernameHdp3 : usernameHdp2;
	}

	public String getHbasePrincipal(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? hbasePrincipalHdp3 : hbasePrincipalHdp2;
	}

	public String getMapreduceUser(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? mapreduceUserHdp3 : mapreduceUserHdp2;
	}

	public String getMaster(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? masterHdp3 : masterHdp2;
	}

	public String getWfOOPath(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? wfOOPathHdp3 : wfOOPathHdp2;
	}

	public String getHivePrincipal(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? hivePrincipalHdp3 : hivePrincipalHdp2;
	}

	public String getQueueName(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? queueNameHdp3 : queueNameHdp2;
	}

	public String getWfPath(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? wfPathHdp3 : wfPathHdp2;
	}

	public String getJdbcUrl(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? jdbcUrlHdp3 : jdbcUrlHdp2;
	}

	public String getJobTracker(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? jobTrackerHdp3 : jobTrackerHdp2;
	}

	public String getNameNode(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? nameNodeHdp3 : nameNodeHdp2;
	}

	public String getPromotionWfApplicationPath(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? promotionWfApplicationPathHdp3
				: promotionWfApplicationPathHdp2;
	}

	public String getPubblicationWfApplicationPath(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? pubblicationWfApplicationPathHdp3
				: pubblicationWfApplicationPathHdp2;
	}

	public String getPubblicationAlignDate(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? pubblicationAlignDateHdp3 : pubblicationAlignDateHdp2;
	}

	public String getOozieUrl(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? oozieUrlHdp3 : oozieUrlHdp2;
	}

	public String getOozieUser(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? oozieUserHdp3 : null;
	}

	public String getOoziePassword(String hdpVersion) {
		return hdpVersion != null && !hdpVersion.equals("") ? ooziePasswordHdp3 : null;
	}

}
