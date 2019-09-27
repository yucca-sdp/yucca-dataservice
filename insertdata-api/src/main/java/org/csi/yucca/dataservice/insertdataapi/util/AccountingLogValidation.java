/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.util;

public class AccountingLogValidation {
	
	
	//log_timestamp STRING(2147483647),
	private String uniqueid = "-";
	private String connid = "-";
	
	private String protocol = "-";
	private String tenant  = "-";
	private String destination = "-";
	private String operation = "-";
	private String iporigin = "-";
	private String sensor_stream = "-";
	private Integer numevents = -1;
	private long elapsed = -1;
	private String error = "-";
	private Integer unix_ts = -1;
	
//	private String jwtData = "-";
//	private String forwardefor = "-";
//
//	private String path = "-";
//	private String apicode = "-";
//	private String querString = "-";
//	private int dataIn = -1;
//	private int dataOut = -1;
//	private String errore = "-";
//	private String datasetcode = "-";
//	private String tenantcode = "-";

	public AccountingLogValidation() {

	}


	public String getUniqueid() {
		return uniqueid;
	}


	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}


	public String getConnid() {
		return connid;
	}


	public void setConnid(String connid) {
		this.connid = connid;
	}


	public String getProtocol() {
		return protocol;
	}


	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}


	public String getTenant() {
		return tenant;
	}


	public void setTenant(String tenant) {
		this.tenant = tenant;
	}


	public String getDestination() {
		return destination;
	}


	public void setDestination(String destination) {
		this.destination = destination;
	}


	public String getOperation() {
		return operation;
	}


	public void setOperation(String operation) {
		this.operation = operation;
	}


	public String getIporigin() {
		return iporigin;
	}


	public void setIporigin(String iporigin) {
		this.iporigin = iporigin;
	}


	public String getSensor_stream() {
		return sensor_stream;
	}


	public void setSensor_stream(String sensor_stream) {
		this.sensor_stream = sensor_stream;
	}


	public Integer getNumevents() {
		return numevents;
	}


	public void setNumevents(Integer numevents) {
		this.numevents = numevents;
	}


	public long getElapsed() {
		return elapsed;
	}


	public void setElapsed(long elapsed) {
		this.elapsed = elapsed;
	}


	public String getError() {
		return error;
	}


	public void setError(String error) {
		this.error = error;
	}


	public Integer getUnix_ts() {
		return unix_ts;
	}


	public void setUnix_ts(Integer unix_ts) {
		this.unix_ts = unix_ts;
	}


	public String toString() {
		String logAccountingMessage = "";

		// id
		logAccountingMessage = logAccountingMessage + "\"" + getUniqueid().replace("\"", "\"\"") + "\"";
		// connid
		logAccountingMessage = logAccountingMessage + ",\"" + getConnid().replace("\"", "\"\"") + "\"";
		// protocol
		logAccountingMessage = logAccountingMessage + ",\"" + getProtocol().replace("\"", "\"\"") + "\"";

		// tenant
		logAccountingMessage = logAccountingMessage + ",\"" + getTenant().replace("\"", "\"\"") + "\"";
		// destination
		logAccountingMessage = logAccountingMessage + ",\"" + getDestination().replace("\"", "\"\"") + "\"";

		// operation
		logAccountingMessage = logAccountingMessage + ",\"" + getOperation().replace("\"", "\"\"") + "\"";

		// iporigin
		if(getIporigin()==null) 
			setIporigin("-");
		logAccountingMessage = logAccountingMessage + ",\"" + getIporigin().replace("\"", "\"\"") + "\"";

		// sensor_stream
		logAccountingMessage = logAccountingMessage + ",\"" + getSensor_stream().replace("\"", "\"\"") + "\"";

		// numevents 
		logAccountingMessage = logAccountingMessage + "," + getNumevents();
		
		// elapsed
		logAccountingMessage = logAccountingMessage + "," + getElapsed();

		// error
		logAccountingMessage = logAccountingMessage + ",\"" + getError().replace("\"", "\"\"") + "\"";

		// unix_ts
		logAccountingMessage = logAccountingMessage + "," + getUnix_ts();

		return logAccountingMessage;
	}

}
