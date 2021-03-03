/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.metadataapi.service.v02;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

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
import org.csi.yucca.dataservice.metadataapi.model.output.v02.Result;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata.Metadata;
import org.csi.yucca.dataservice.metadataapi.service.AbstractService;
import org.csi.yucca.dataservice.metadataapi.service.response.ErrorResponse;
import org.csi.yucca.dataservice.metadataapi.util.FacetParams;

@Path("/v02")
public class MetadataService extends AbstractService {

	@Context
	ServletContext context;
	static Logger log = Logger.getLogger(MetadataService.class);

	@GET
	@Path("/search")
	// @Consumes({ "application/vnd.sdp-metadata.v2+json" })
	@Produces({ "application/json; charset=UTF-8", "application/vnd.sdp-metadata.v2+json" })
	public Response searchFull(@Context HttpServletRequest request, @QueryParam("q") String q, @QueryParam("start") Integer start, @QueryParam("rows") Integer rows,
			@QueryParam("sort") String sort, @QueryParam("tenant") String tenant, @QueryParam("organization") String organization, @QueryParam("domain") String domain,
			@QueryParam("subdomain") String subdomain, @QueryParam("opendata") Boolean opendata, @QueryParam("geolocalized") Boolean geolocalized,
			@QueryParam("minLat") Double minLat, @QueryParam("minLon") Double minLon, @QueryParam("maxLat") Double maxLat, @QueryParam("maxLon") Double maxLon,
			@QueryParam("lang") String lang, @QueryParam("facet.field") String facetFields, @QueryParam("facet.prefix") String facetPrefix,
			@QueryParam("facet.sort") String facetSort, @QueryParam("facet.contains") String facetContains,
			@QueryParam("facet.pivot") String facetPivotFields,@QueryParam("facet.pivot.mincount") String facetPivotMincount,
			@QueryParam("facet.contains.ignoreCase") String facetContainsIgnoreCase, @QueryParam("facet.limit") String facetLimit, @QueryParam("facet.offset") String facetOffset,
			@QueryParam("facet.mincount") String facetMinCount, @QueryParam("facet.missing") String facetMissing,
			@QueryParam("tags") String tags, @QueryParam("visibility") String visibility,@QueryParam("isSearchExact") Boolean isSearchExact,
			@QueryParam("includeSandbox") Boolean includeSandbox, @QueryParam("hasStream") Boolean hasStream, @QueryParam("hasDataset") Boolean hasDataset,
			@QueryParam("externalReference") String externalReference) throws NumberFormatException, UnknownHostException {


		FacetParams facetParams = null;
		if (facetFields != null || facetPivotFields!=null)
			facetParams = new FacetParams(facetFields, facetPrefix, facetSort, facetContains, facetContainsIgnoreCase, facetLimit, facetOffset, facetMinCount, facetMissing, facetPivotFields, facetPivotMincount);

		String result;
		try {
			Result searchResult = org.csi.yucca.dataservice.metadataapi.delegate.v02.metadata.MetadataDelegate.getInstance().search(request, q, start, rows, sort, tenant,
					organization, domain, subdomain, opendata, geolocalized, minLat, minLon, maxLat, maxLon, lang, null, 
					facetParams, hasDataset, hasStream, tags, visibility, isSearchExact, includeSandbox, externalReference); // expose hasDataset, hasStream?
			result = searchResult.toJson();
		} catch (UnsupportedEncodingException e) {
			log.error("UnsupportedEncodingException",e);
			result = new ErrorResponse("", "Invalid param").toJson();
		} catch (UserWebServiceException  e2) {
			return e2.getResponse();
		} catch (Exception e) {
			log.error("[MetadataService::getStream]"+e.getMessage(),e);
			return Response.serverError().build();
		}

		return Response.ok(result).build();
	}

	@GET
	@Path("/detail/{datasetCode}")
	@Produces("application/json; charset=UTF-8")
	public Response getDatasetMetadata(@Context HttpServletRequest request, @PathParam("datasetCode") String datasetCode, @QueryParam("version") String version,
			@QueryParam("lang") String lang, @QueryParam("callback") String callback) throws NumberFormatException, UnknownHostException {

		log.info("[SearchService::search] START");

		Metadata metadata;
		try {
			metadata = org.csi.yucca.dataservice.metadataapi.delegate.v02.metadata.MetadataDelegate.getInstance().loadDatasetMetadata(request, datasetCode, version,  lang);
		} catch (UserWebServiceException e) {
			return e.getResponse();
		} catch (UnsupportedEncodingException e) {
			return Response.ok(new ErrorResponse("", "Invalid param").toJson()).build();
		} catch (Exception e) {
			log.error("[MetadataService::getStream]"+e.getMessage(),e);
			return Response.serverError().build();
		}

		return Response.ok(metadata.toJson()).build();
	}

	@GET
	@Path("/detail/{tenantCode}/{smartobjectCode}/{streamCode}/")
	@Produces("application/json; charset=UTF-8")
	public Response getStreamMetadata(@Context HttpServletRequest request, @PathParam("tenantCode") String tenantCode,
			@PathParam("smartobjectCode") String smartobjectCode, @PathParam("streamCode") String streamCode, @QueryParam("version") String version,
			@QueryParam("lang") String lang, @QueryParam("callback") String callback) throws NumberFormatException, UnknownHostException {

		log.info("[SearchService::search] START -");

		Metadata metadata;
		try {
			metadata = org.csi.yucca.dataservice.metadataapi.delegate.v02.metadata.MetadataDelegate.getInstance().loadStreamMetadata(request, tenantCode,
					smartobjectCode, streamCode, version,  lang);
		} catch (UserWebServiceException e) {
			return e.getResponse();
		} catch (UnsupportedEncodingException e) {
			return Response.ok(new ErrorResponse("", "Invalid param").toJson()).build();
		} catch (Exception e) {
			log.error("[MetadataService::getStream]"+e.getMessage(),e);
			return Response.serverError().build();
		}

		return Response.ok(metadata.toJson()).build();
	}
}
