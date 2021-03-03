/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.util;

public class AccountingLog {
	private String jwtData = "-";
	private String forwardefor = "-";
	private String uniqueid = "-";

	private String path = "-";
	private String apicode = "-";
	private String querString = "-";
	private int dataIn = -1;
	private int dataOut = -1;
	private long elapsed = -1;
	private String errore = "-";
	private String datasetcode = "-";
	private String tenantcode = "-";

	public AccountingLog() {

	}

	public String getDatasetcode() {
		return datasetcode;
	}

	public void setDatasetcode(String datasetcode) {
		this.datasetcode = datasetcode;
	}

	public String getTenantcode() {
		return tenantcode;
	}

	public void setTenantcode(String tenantcode) {
		this.tenantcode = tenantcode;
	}

	public String getJwtData() {
		return jwtData;
	}

	public void setJwtData(String jwtData) {
		this.jwtData = jwtData;
	}

	public String getForwardefor() {
		return forwardefor;
	}

	public void setForwardefor(String forwardefor) {
		this.forwardefor = forwardefor;
	}

	public String getUniqueid() {
		return uniqueid;
	}

	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getApicode() {
		return apicode;
	}

	public void setApicode(String apicode) {
		this.apicode = apicode;
	}

	public String getQuerString() {
		return querString;
	}

	public void setQuerString(String querString) {
		this.querString = querString;
	}

	public int getDataIn() {
		return dataIn;
	}

	public void setDataIn(int dataIn) {
		this.dataIn = dataIn;
	}

	public int getDataOut() {
		return dataOut;
	}

	public void setDataOut(int dataOut) {
		this.dataOut = dataOut;
	}

	public long getElapsed() {
		return elapsed;
	}

	public void setElapsed(long elapsed) {
		this.elapsed = elapsed;
	}

	public String getErrore() {
		return errore;
	}

	public void setErrore(String errore) {
		this.errore = errore;
	}

	public String toString() {
		String logAccountingMessage = "";

		// id
		logAccountingMessage = logAccountingMessage + "\"" + getUniqueid().replace("\"", "\"\"") + "\"";
		// forwardedfor
		logAccountingMessage = logAccountingMessage + ",\"" + getForwardefor().replace("\"", "\"\"") + "\"";
		// jwt
		logAccountingMessage = logAccountingMessage + ",\"" + getJwtData().replace("\"", "\"\"") + "\"";

		// path
		logAccountingMessage = logAccountingMessage + ",\"" + getPath().replace("\"", "\"\"") + "\"";
		// apicode
		logAccountingMessage = logAccountingMessage + ",\"" + getApicode().replace("\"", "\"\"") + "\"";

		// datasetCode
		logAccountingMessage = logAccountingMessage + ",\"" + getDatasetcode().replace("\"", "\"\"") + "\"";

		// tenantCode
		logAccountingMessage = logAccountingMessage + ",\"" + getTenantcode().replace("\"", "\"\"") + "\"";

		// querString
		logAccountingMessage = logAccountingMessage + ",\"" + getQuerString().replace("\"", "\"\"") + "\"";

		// dataIn
		logAccountingMessage = logAccountingMessage + "," + getDataIn();
		// dataOut
		logAccountingMessage = logAccountingMessage + "," + getDataOut();
		// elapsed
		logAccountingMessage = logAccountingMessage + "," + getElapsed();

		// error
		logAccountingMessage = logAccountingMessage + ",\"" + getErrore() + "\"";

		return logAccountingMessage;
	}

}
