/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.fabric;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.service.impl.TenantServiceImpl;

public class FabricResponse {
	public static final String STATUS_ERROR = "error";
	public static final String STATUS_SUCCESS = "success";
	private String status;
	private List<FabricLog> logs;
	private String loggerKey;

	private static final Logger logger = Logger.getLogger("yucca.fabric");

	public FabricResponse(String loggerKey) {
		super();
		this.loggerKey = loggerKey;
	}

	public List<FabricLog> getLogs() {
		return logs;
	}

	public void setLogs(List<FabricLog> logs) {
		this.logs = logs;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private void addLogInfo(String level,String log) {
		if (logs == null)
			logs = new LinkedList<FabricLog>();
		logs.add(new FabricLog(level, log));
	}

	public void addLogDebug(String log) {
		addLogInfo(FabricLog.LEVEL_DEBUG, log);
		logger.info(log);
	}
	public void addLogInfo(String log) {
		addLogInfo(FabricLog.LEVEL_INFO, log);
		logger.info(log);
	}
	public void addLogWarning(String log) {
		addLogInfo(FabricLog.LEVEL_WARNING, log);
		logger.warn(log);
	}
	public void addLogError(String log) {
		addLogInfo(FabricLog.LEVEL_ERROR, log);
		logger.error(log);
	}
	

}
