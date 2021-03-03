/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.metadataapi.service.v01;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.exception.UserWebServiceException;
import org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.Result;
import org.csi.yucca.dataservice.metadataapi.service.AbstractService;
import org.csi.yucca.dataservice.metadataapi.service.response.ErrorResponse;
import org.csi.yucca.dataservice.metadataapi.service.response.ListResponse;
import org.csi.yucca.dataservice.metadataapi.util.Constants;


@Path("/")
public class MetadataService extends AbstractService {

	@Context
	ServletContext context;
	static Logger log = Logger.getLogger(MetadataService.class);

	@GET
	@Path("search/full")
	// @Consumes({ "*/*", "application/vnd.sdp-metadata.v1+json" })
	@Produces({ "application/json; charset=UTF-8", "application/vnd.sdp-metadata.v1+json" })
	public Response searchFull(@Context HttpServletRequest request, @QueryParam("q") String q, @QueryParam("start") Integer start, @QueryParam("end") Integer end,
			@QueryParam("sort") String sort, @QueryParam("tenant") String tenant, @QueryParam("organization") String organization, @QueryParam("domain") String domain,
			@QueryParam("subdomain") String subdomain, @QueryParam("opendata") Boolean opendata, @QueryParam("geolocalized") Boolean geolocalized,
			@QueryParam("minLat") Double minLat, @QueryParam("minLon") Double minLon, @QueryParam("maxLat") Double maxLat, @QueryParam("maxLon") Double maxLon,
			@QueryParam("lang") String lang, @QueryParam("tags") String tags, @QueryParam("visibility") String visibility,@QueryParam("isSearchExact") Boolean isSearchExact,
			@QueryParam("includeSandbox") Boolean includeSandbox,@QueryParam("hasStream") Boolean hasStream, @QueryParam("hasDataset") Boolean hasDataset, @QueryParam("externalReference") String externalReference) throws NumberFormatException, UnknownHostException {


		Result searchResult;
		try {
			searchResult = org.csi.yucca.dataservice.metadataapi.delegate.v02.metadata.MetadataDelegate.getInstance().search(request, q, start, end, sort, tenant,
					organization, domain, subdomain, opendata, geolocalized, minLat, minLon, maxLat, maxLon, lang, null, 
					null, hasDataset, hasStream, tags, visibility, isSearchExact, includeSandbox, externalReference); // expose hasDataset, hasStream?
		} catch (UnsupportedEncodingException e) {
			log.error("UnsupportedEncodingException",e);
			return Response.ok(new ErrorResponse("", "Invalid param").toJson()).build();
		} catch (UserWebServiceException e) {
			return e.getResponse();
		} 

		
		ListResponse response = new ListResponse();
		response.setCount(searchResult.getCount());

		if (searchResult!=null && searchResult.getMetadata()!=null)
		{
			ArrayList<Metadata> listMetadataV1 = new ArrayList<Metadata>();
			for (org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata.Metadata metadatav2 : searchResult.getMetadata()) {
				listMetadataV1.addAll(metadatav2.toV01(Constants.OUTPUT_FORMAT_V01_LIST));
			}
			response.setResult(listMetadataV1);
		}
		
		return Response.ok(response.toJson()).build();
	}
	
	@GET
	@Path("detail/{tenant}/{datasetCode}")
	@Produces("application/json; charset=UTF-8")
	public Response getDataset(@Context HttpServletRequest request, @PathParam("tenant") String tenant, @PathParam("datasetCode") String datasetCode,
			@QueryParam("version") String version, @QueryParam("lang") String lang, @QueryParam("callback") String callback) throws NumberFormatException, UnknownHostException {

		log.info("[SearchService::search] START ");

		org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata.Metadata metadatav2;
		try {
			metadatav2 = org.csi.yucca.dataservice.metadataapi.delegate.v02.metadata.MetadataDelegate.getInstance().
					loadDatasetMetadata(request, datasetCode, version, lang);
		} catch (UserWebServiceException e) {
			return e.getResponse();
		} catch (Exception e) {
			log.error("[MetadataService::getStream]"+e.getMessage(),e);
			return Response.serverError().build();
		}

		List<Metadata> v01s = metadatav2.toV01(Constants.OUTPUT_FORMAT_V01_DATASET);
		if (v01s!=null && !v01s.isEmpty())
			return Response.ok(v01s.get(0).toJson()).build();
		else {
			return Response.ok("").build();
		}
	}

	@GET
	@Path("detail/{tenant}/{smartobjectCode}/{streamCode}/")
	@Produces("application/json; charset=UTF-8")
	public Response getStream(@Context HttpServletRequest request, @PathParam("tenant") String tenant, @PathParam("smartobjectCode") String smartobjectCode,
			@PathParam("streamCode") String streamCode, @QueryParam("version") String version, @QueryParam("lang") String lang, @QueryParam("callback") String callback)
			throws NumberFormatException, UnknownHostException {

//		String apiName = tenant + "." + smartobjectCode + "_" + streamCode + "_stream";
		org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata.Metadata metadatav2;
		try {
			metadatav2 = org.csi.yucca.dataservice.metadataapi.delegate.v02.metadata.MetadataDelegate.getInstance().
					loadStreamMetadata(request, tenant,smartobjectCode,streamCode, version, lang);
		} catch (UserWebServiceException e) {
			return e.getResponse();
		} catch (Exception e) {
			log.error("[MetadataService::getStream]"+e.getMessage(),e);
			return Response.serverError().build();
		}
		List<Metadata> v01s = metadatav2.toV01(Constants.OUTPUT_FORMAT_V01_STREAM);
		if (v01s!=null && !v01s.isEmpty())
			return Response.ok(v01s.get(0).toJson()).build();
		else {
			return Response.ok("").build();
		}
	}


}
