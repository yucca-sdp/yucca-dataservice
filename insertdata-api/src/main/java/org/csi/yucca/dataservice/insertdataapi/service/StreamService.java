/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.service;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiBaseException;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiRuntimeException;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetBulkInsert;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetBulkInsertOutput;

import net.minidev.json.JSONObject;

@Path("/stream")
public class StreamService extends AbstractService {

	@Context
	ServletContext context;
	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");

	@POST
	@Path("/input/{codTenant}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response dataInsert(@Context HttpServletRequest request, String jsonData, @PathParam(value = "codTenant") String codTenant,
			@HeaderParam(value = "UNIQUE_ID") String uniqueid, @HeaderParam(value = "X-Forwarded-For") String forwardfor, @HeaderParam(value = "Authorization") String authInfo,
			@Context final HttpServletResponse response) throws InsertApiBaseException, InsertApiRuntimeException {
		log.debug("forwardfor ->" + forwardfor);
		log.debug("authInfo ->" + authInfo);
		DatasetBulkInsertOutput out = super.dataInsert(jsonData, codTenant, uniqueid, forwardfor, authInfo);
		return Response.status(Status.ACCEPTED).entity(out).build();
	}

	@Override
	protected HashMap<String, DatasetBulkInsert> parseJsonInput(String codTenant, String jsonData) throws Exception {
		return new InsertApiLogic().parseJsonInputStream(codTenant, jsonData);
	}

	public JSONObject getSmartobject_StreamFromJson(String codTenant, String jsonData) throws Exception {
		return InsertApiLogic.getSmartobject_StreamFromJson(codTenant, jsonData);
	}

	@GET
	@Path("/clearcache/{codTenant}/{streamApplication}/{sensor}")
	@Produces("plain/text")
	public String clearStreamCache(@Context HttpServletRequest request, String jsonData, @PathParam(value = "codTenant") String codTenant,
			@PathParam(value = "streamApplication") String streamApplication, @PathParam(value = "sensor") String sensor, @HeaderParam(value = "UNIQUE_ID") String uniqueid,
			@HeaderParam(value = "X-Forwarded-For") String forwardfor, @HeaderParam(value = "Authorization") String authInfo, @Context final HttpServletResponse response)
			throws InsertApiBaseException, InsertApiRuntimeException {
		log.info("clearStreamCache");
		//SDPInsertApiMongoDataAccess.clearCache(codTenant, streamApplication, sensor);
		if (response != null)
			response.setStatus(Status.ACCEPTED.getStatusCode());
		return "Cache clear";
	}

}
