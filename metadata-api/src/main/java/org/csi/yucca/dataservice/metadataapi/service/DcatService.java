/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.metadataapi.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.delegate.v02.metadata.MetadataDelegate;
import org.csi.yucca.dataservice.metadataapi.exception.UserWebServiceException;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatAgent;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatCatalog;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatDataset;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatDate;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatDistribution;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatLicenseType;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatObject;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatResult;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatVCard;
import org.csi.yucca.dataservice.metadataapi.model.dcat.I18NString;
import org.csi.yucca.dataservice.metadataapi.model.dcat.IdString;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.Result;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata.Metadata;
import org.csi.yucca.dataservice.metadataapi.service.response.ErrorResponse;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.csi.yucca.dataservice.metadataapi.util.DCatSdpHelper;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;
import org.jboss.resteasy.spi.LoggableFailure;

import com.google.gson.Gson;

@Path("/dcat")
public class DcatService extends AbstractService {

	// documantazione:
	// http://linee-guida-cataloghi-dati-profilo-dcat-ap-it.readthedocs.io/it/latest/index.html
	
	@Context
	ServletContext context;
	static Logger log = Logger.getLogger(DcatService.class);

	
	private void addDistribution(DCatDataset dsDCAT, Metadata metadataST, 
			Config cfg, boolean linkedData, Map<String, DCatObject> objectsMap, DCatLicenseType licenseDistribution){
		DCatDistribution distribution = new DCatDistribution();
		distribution.setAccessURL(new IdString(cfg.getUserportalBaseUrl() + "#/dataexplorer/detail/" + metadataST.getTenantCode() + "/"
				+ metadataST.getDataset().getCode()));
		distribution.setTitle_it(new I18NString("it", metadataST.getDataset().getCode()));
		distribution.setDownloadURL(new IdString(cfg.getOauthBaseUrl() + "api/" + metadataST.getDataset().getCode() + "/download/"
				+ metadataST.getDataset().getDatasetId() + "/all"));
		distribution.setId(metadataST.getDataset().getDatasetId()+"");
		
		// https://int-api.smartdatanet.it/api/Inputdataond_567/download/567/all
		// distr.getLicense().setName(metadataST.getLicense());
		
		distribution.setLicense(licenseDistribution);
		
		// distr.setIssued(new
		// DcatDate(metadataST.getRegistrationDate()));
		final String idDistribution = distribution.getId();
		DCatObject distributionToAdd = getDcatObject(linkedData, objectsMap, distribution, new Empty() {
			@Override
			public DCatObject get() {
				DCatDistribution empty = new DCatDistribution();
				empty.setFormat(null);
				empty.cloneId(idDistribution, true);
				return empty;
			}
		});
		dsDCAT.addDistribution((DCatDistribution)distributionToAdd);
	}
	
	private void setPublisher(DCatDataset dsDCAT, Metadata metadataST, boolean linkedData, Map<String, DCatObject> objectsMap){
		DCatAgent publisher = new DCatAgent();
		if (metadataST.getDcat().getDcatNomeOrg() != null) {
			if (DCatSdpHelper.isCSIAgent(metadataST.getDcat().getDcatNomeOrg()))
				publisher = DCatSdpHelper.getCSIAgentDcat();
			else {

				publisher.setName(metadataST.getDcat().getDcatNomeOrg());
				publisher.addDcterms_type(new IdString("http://purl.org/adms/publishertype/Company"));
				publisher.setId(metadataST.getDcat().getDcatNomeOrg());
				publisher.setDcterms_identifier(metadataST.getDcat().getDcatNomeOrg());
			}
		} else {
			publisher = DCatSdpHelper.getCSIAgentDcat();
		}
		
		
		final String publisherId = publisher.getId();
		DCatObject publisherToAdd = getDcatObject(linkedData, objectsMap, publisher, new Empty() {
			
			@Override
			public DCatObject get() {
				DCatAgent empty = new DCatAgent();
				empty.cloneId(publisherId, true);
				return empty;
			}
		});
		dsDCAT.setPublisher((DCatAgent )publisherToAdd);

	}
	
	private void setContactPoint(DCatDataset dsDCAT, Metadata metadataST, boolean linkedData, Map<String, DCatObject> objectsMap){
		DCatVCard publisherVCard = new DCatVCard();
		publisherVCard.setHasEmail(new IdString("mailto:"+metadataST.getDcat().getDcatEmailOrg()));
		publisherVCard.setName(metadataST.getDcat().getDcatNomeOrg());
		publisherVCard.setId(metadataST.getDcat().getDcatNomeOrg());

		final String publisherVCardId = publisherVCard.getId();
		DCatObject publisherVCardToAdd = getDcatObject(linkedData, objectsMap, publisherVCard, new Empty() {
			@Override
			public DCatObject get() {
				DCatVCard empty = new DCatVCard();
				empty.cloneId(publisherVCardId, true);
				return empty;
			}
		});  
		dsDCAT.setContactPoint((DCatVCard)publisherVCardToAdd);
	}
	
	private void setRightsHolder(DCatDataset dsDCAT, Metadata metadataST, boolean linkedData, Map<String, DCatObject> objectsMap){
		DCatAgent rightsHolder = new DCatAgent();
		if (metadataST.getDcat().getDcatRightsHolderName() != null) {
			if (DCatSdpHelper.isCSIAgent(metadataST.getDcat().getDcatRightsHolderName()))
				rightsHolder = DCatSdpHelper.getCSIAgentDcat();
			else {

				rightsHolder.setName(metadataST.getDcat().getDcatRightsHolderName());
				if (metadataST.getDcat().getDcatRightsHolderType() != null) {
					rightsHolder.addDcterms_type(new IdString(DCatSdpHelper.cleanForId(metadataST.getDcat().getDcatRightsHolderType())));
				} else {
					rightsHolder.addDcterms_type(new IdString("http://purl.org/adms/publishertype/Company"));
				}

				if (metadataST.getDcat().getDcatRightsHolderId() != null) {
					rightsHolder.setId(metadataST.getDcat().getDcatRightsHolderId());
					rightsHolder.setDcterms_identifier(metadataST.getDcat().getDcatRightsHolderId());
				} else {
					rightsHolder.setId(metadataST.getDcat().getDcatRightsHolderName());
					rightsHolder.setDcterms_identifier(metadataST.getDcat().getDcatRightsHolderName());
				}
			}
		} else {
			rightsHolder = DCatSdpHelper.getCSIAgentDcat();
		}

		final String rightsHolderId = rightsHolder.getId();
		DCatObject rightsHolderToAdd = getDcatObject(linkedData, objectsMap, rightsHolder, new Empty() {
			@Override
			public DCatObject get() {
				DCatAgent empty = new DCatAgent();
				empty.cloneId(rightsHolderId, true);
				return empty;
			}
		});
		dsDCAT.setRightsHolder((DCatAgent)rightsHolderToAdd);
	}
	
	private void setCreator(DCatDataset dsDCAT, Metadata metadataST, boolean linkedData, Map<String, DCatObject> objectsMap){
		DCatAgent creator = new DCatAgent();
		if (metadataST.getDcat().getDcatCreatorName() != null) {
			if (DCatSdpHelper.isCSIAgent(metadataST.getDcat().getDcatCreatorName()))
				creator = DCatSdpHelper.getCSIAgentDcat();
			else {

				creator.setName(metadataST.getDcat().getDcatCreatorName());
				if (metadataST.getDcat().getDcatCreatorType() != null) {
					creator.addDcterms_type(new IdString(DCatSdpHelper.cleanForId(metadataST.getDcat().getDcatCreatorType())));
				} else {
					creator.addDcterms_type(new IdString("http://purl.org/adms/publishertype/Company"));
				}
				if (metadataST.getDcat().getDcatCreatorId() != null) {
					creator.setId(metadataST.getDcat().getDcatCreatorId());
					creator.setDcterms_identifier(metadataST.getDcat().getDcatCreatorId());
				} else {
					creator.setId(metadataST.getDcat().getDcatCreatorName());
					creator.setDcterms_identifier(metadataST.getDcat().getDcatCreatorName());
				}
			}
		} 
		else {
			creator = DCatSdpHelper.getCSIAgentDcat();
		}
		
		final String idCreator = creator.getId();
		DCatObject creatorToAdd = getDcatObject(linkedData, objectsMap, creator, new Empty() {
			@Override
			public DCatObject get() {
				DCatAgent empty = new DCatAgent();
				empty.cloneId(idCreator, true);
				return empty;
			}
		});
		dsDCAT.setCreator((DCatAgent)creatorToAdd);
		
	}
	
	@GET
	@Path("/dataset_list")
	@Produces("application/ld+json; charset=UTF-8")
	public Response searchDCAT(@Context HttpServletRequest request, @QueryParam("q") String q, @QueryParam("page") Integer page, @QueryParam("start") Integer start,
			@QueryParam("rows") Integer rows, @QueryParam("tenant") String tenant, @QueryParam("organization") String organization, @QueryParam("domain") String domain,
			@QueryParam("subdomain") String subdomain, @QueryParam("opendata") Boolean opendata, @QueryParam("geolocalized") Boolean geolocalized,
			@QueryParam("minLat") Double minLat, @QueryParam("minLon") Double minLon, @QueryParam("maxLat") Double maxLat, @QueryParam("maxLon") Double maxLon,
			@QueryParam("lang") String lang, @QueryParam("tags") String tags, @QueryParam("visibility") String visibility, @QueryParam("isSearchExact") Boolean isSearchExact,
			@QueryParam("includeSandbox") Boolean includeSandbox, @QueryParam("externalReference") String externalReference, @QueryParam("linkedData") Boolean linkedData , @QueryParam("outputFormat") String outputFormat)
			throws NumberFormatException, UnknownHostException {

		// SimpleDateFormat catalogDateFormat = new
		// SimpleDateFormat("yyyy-MM-dd");// dd/MM/yyyy

		if (linkedData == null) linkedData = false;
		
		DCatCatalog catalog = new DCatCatalog();
		catalog.setDescription_it(new I18NString("it", "Catalogo Open Data Piemonte"));
		catalog.setTitle_it(new I18NString("it", "CATALOGO OPEN DATA PIEMONTE"));
		// catalog.setDescription_en(new I18NString("en",
		// "Smart Data Piemonte Catalog"));
		// catalog.setTitle_en(new I18NString("en", "SMART DATA CATALOG"));

		catalog.setModified(new DCatDate(new Date()));
		catalog.setHomepage(new IdString("http://www.dati.piemonte.it"));

		// LicenceTypeDCAT licenseType = new LicenceTypeDCAT();
		// licenseType.setType("http://purl.org/adms/licencetype/PublicDomain");
		// catalog.setLicense(licenseType);

		Map<String, DCatObject> objectsMap = new HashMap<String, DCatObject>();

		if (page == null)
			page = 1;
		if (rows == null)
			rows = 10;

		// Integer end = page * rows;

		if (start == null)
			start = (page * rows - rows);

		log.info("[DcatService::searchDCAT] start: " + start + ", rows: " + rows);
		log.info("[DcatService::searchDCAT] query: " + q);

		Result searchResult;
		try {
			searchResult = MetadataDelegate.getInstance().search(request, q, start, rows, null, tenant, organization, domain, subdomain, opendata, geolocalized, minLat, minLon,
					maxLat, maxLon, lang, true, null, true, null, tags, visibility, isSearchExact, includeSandbox, externalReference);
		} catch (UnsupportedEncodingException e) {
			return Response.ok(new ErrorResponse("", "Invalid param").toJson()).build();
		} catch (UserWebServiceException e) {
			return e.getResponse();
		} catch (Exception e) {
			log.error("[MetadataService::getStream]" + e.getMessage(), e);
			return Response.serverError().build();
		}

		Gson gson = JSonHelper.getInstanceDcat();

		Config cfg = Config.getInstance();

		
// ----------------------------------------------------------------------		
// ITERAZIONE SU RESULT
// ----------------------------------------------------------------------		
		if (searchResult != null && searchResult.getMetadata() != null) {

			for (Metadata metadataST : searchResult.getMetadata()) {
				
				if (metadataST.getDataset() != null && metadataST.getDataset().getDatasetId() != null) {

					DCatDataset dsDCAT = new DCatDataset();
					dsDCAT.setId(metadataST.getDataset().getCode() + "_" + metadataST.getVersion());
					
					// CREATOR è UN OGGETTO DI TIPO AGENTE
					if (metadataST.getDcat() != null) {
						
						// ------------------------------------------------
						// CREATOR 
						// ------------------------------------------------
						setCreator(dsDCAT, metadataST, linkedData, objectsMap);
						
						// ------------------------------------------------
						// RIGHT_HOLDER 
						// ------------------------------------------------
						setRightsHolder(dsDCAT, metadataST, linkedData, objectsMap);
						
						// ------------------------------------------------
						// CONTACT_POINT 
						// ------------------------------------------------
						setContactPoint(dsDCAT, metadataST, linkedData, objectsMap);
						
						// ------------------------------------------------
						// PUBLISHER
						// ------------------------------------------------
						setPublisher(dsDCAT, metadataST, linkedData, objectsMap);
						
					}
					
					dsDCAT.setDescription(new I18NString("it", metadataST.getDescription()));
					dsDCAT.setTitle(new I18NString("it", metadataST.getName()));
					// V01 - fixed value
					// http://publications.europa.eu/resource/authority/frequency/UNKNOWN
					String freqStr ="UNKNOWN";
					if ( metadataST.getOpendata() != null) {
						List<String> freq = metadataST.getOpendata().getUpdateFrequency();
						log.info("FreqList " + freq);
						if (freq != null && freq.size()>0)
						freqStr = freq.get(0);
					}
					
					dsDCAT.setAccrualPeriodicity(new IdString("http://publications.europa.eu/resource/authority/frequency/"+freqStr));
					// dsDCAT.setAccrualPeriodicity(metadata.getFps());

					// String keyWords = "";
					if (metadataST.getTags() != null) {
						for (String tag : metadataST.getTags()) {
							dsDCAT.addKeyword(tag.replaceAll("[&]", "and"));
						}
					}
					dsDCAT.setTheme(DCatSdpHelper.getDcatTheme(metadataST.getDomainCode()));
					// dsDCAT.setAccessRights(metadataST.getVisibility());
					dsDCAT.setIdentifier(metadataST.getDataset().getCode() + "_" + metadataST.getVersion());
					
					if (metadataST.getOpendata() != null && metadataST.getOpendata().getDataUpdateDate() != null){
						dsDCAT.setModified(new DCatDate(metadataST.getOpendata().getDataUpdateDate()));
					}
					else{
						dsDCAT.setModified(new DCatDate(metadataST.getRegistrationDate()));
					}
					
					dsDCAT.setVersionInfo(metadataST.getVersion());

					String dcatSubject = DCatSdpHelper.getDcatSubject(metadataST.getSubdomainCode());
					if (dcatSubject != null)
						dsDCAT.addSubTheme(new IdString(dcatSubject));

					// ------------------------------------------------
					// LICENSE DISTRIBUTION
					// ------------------------------------------------
					DCatLicenseType licenseDistribution = getLicenseDistribution(metadataST, linkedData, objectsMap);
					
					// ------------------------------------------------
					// DISTRIBUTION
					// ------------------------------------------------
					addDistribution(dsDCAT, metadataST, cfg, linkedData, objectsMap, licenseDistribution);
					
					// ------------------------------------------------
					// BINARY DISTRIBUTION
					// ------------------------------------------------
					addBinaryDistribution(dsDCAT, metadataST, cfg, linkedData, objectsMap, licenseDistribution);
					
					//###########################################################àà					
					// AGGIUNGO IL DATA SET AL CATALOGO
					//###########################################################àà					
					final String dsDCATId = dsDCAT.getId();
					DCatObject dsDCATToAdd = getDcatObject(linkedData, objectsMap, dsDCAT, new Empty() {
						@Override
						public DCatObject get() {
							DCatDataset empty = new DCatDataset();
							empty.cloneId(dsDCATId, true);
							empty.setCreator(null);
							empty.setDistributions(null);
							empty.setContactPoint(null);
							return empty;
						}
					});
					catalog.addDataset((DCatDataset)dsDCATToAdd);
					
					if (linkedData) {
						if (!objectsMap.containsKey(DCatSdpHelper.getCSIAgentDcat().getId()))
							objectsMap.put(DCatSdpHelper.getCSIAgentDcat().getId(), DCatSdpHelper.getCSIAgentDcat());
						DCatAgent empty = new DCatAgent();
						empty.cloneId(DCatSdpHelper.getCSIAgentDcat().getId(), true);
						dsDCAT.setPublisher(empty);
					} else
						catalog.setPublisher(DCatSdpHelper.getCSIAgentDcat());

				}
			}
		}
		DCatResult result = new DCatResult();
		result.addItem(catalog);
		if (linkedData) {
			for (String objectKey : objectsMap.keySet()) {
				result.addItem(objectsMap.get(objectKey));
			}

		}
		String json = gson.toJson(result);
		
		//------------------------
//		Model model = ModelFactory.createDefaultModel();
//
//		 // use the FileManager to find the input file
////		 InputStream in = FileManager.get().open( inputFileName );
//		 InputStream in = new ByteArrayInputStream( json.getBytes(  ) );
//		if (in == null) {
//		    throw new IllegalArgumentException("not found");
//		}
//
//		// read the RDF/XML file
//		model.read(in, "JSON-LD");
//
//		// write it to standard out
//		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
//		model.write(arrayOutputStream, "TURTLE");		
		
		
		//------------------------
		
//		return Response.ok(arrayOutputStream.toString()).build();
		
		if (null!=outputFormat && "turtle".equalsIgnoreCase(outputFormat)) {
			return Response.ok(convertToTurtle(json),"text/turtle; charset=UTF-8").build();
		} else {
			return Response.ok(json,"application/ld+json; charset=UTF-8").build();
		}
	}

	/**
	 * 
	 * @param linkedData
	 * @param objectsMap
	 * @param dCatObject
	 * @param empty
	 * @return
	 */
	private DCatObject getDcatObject(boolean linkedData, Map<String, DCatObject> objectsMap, DCatObject dCatObject, Empty empty){
		
		if (linkedData) {
			
			if (!objectsMap.containsKey(dCatObject.getId())){
				objectsMap.put(dCatObject.getId(), dCatObject);
			}

			return empty.get();
		}
		
		return dCatObject;
	}

	
	/**
	 * 
	 * @param metadataST
	 * @param linkedData
	 * @param objectsMap
	 * @return
	 */
	private DCatLicenseType getLicenseDistribution(Metadata metadataST, Boolean linkedData, Map<String, DCatObject> objectsMap){
		
		if (metadataST.getLicense() != null) {
		
			DCatLicenseType licenseDistribution = new DCatLicenseType();
			
			if (metadataST.getLicense().startsWith("CC BY") || metadataST.getLicense().startsWith("CC-BY")) {
				licenseDistribution.setName("CC-BY 4.0 IT");
				String version = metadataST.getLicense().substring(5).trim();
				licenseDistribution.setDcterms_type(new IdString("http://purl.org/adms/licencetype/Attribution"));
				licenseDistribution.setVersion(version);
			} 
			else if (metadataST.getLicense().startsWith("CC 0")) {
				licenseDistribution.setName("CC 0");
				String version = metadataST.getLicense().substring(4).trim();
				licenseDistribution.setDcterms_type(new IdString("http://purl.org/adms/licencetype/PublicDomain"));
				licenseDistribution.setVersion(version);
			}
			else {
				licenseDistribution.setName(metadataST.getLicense());
				licenseDistribution.setDcterms_type(new IdString("http://purl.org/adms/licencetype/UnknownIPR"));
			}

			licenseDistribution.setId(licenseDistribution.getName());
			
			if (linkedData) {
				if (!objectsMap.containsKey(licenseDistribution.getId())){
					objectsMap.put(licenseDistribution.getId(), licenseDistribution);
				}
				DCatLicenseType empty = new DCatLicenseType();
				empty.cloneId(licenseDistribution.getId(), true);
				return empty;
			} 
			else{
				return licenseDistribution;
			}
			
		}

		return null;
	}
	

	/**
	 * 
	 * @param dsDCAT
	 * @param metadataST
	 * @param cfg
	 * @param linkedData
	 * @param objectsMap
	 * @param licenseDistribution
	 */
	private void addBinaryDistribution(DCatDataset dsDCAT, Metadata metadataST, 
			Config cfg, boolean linkedData, Map<String, DCatObject> objectsMap , DCatLicenseType licenseDistribution){
		if (metadataST.isBinary()) {
			DCatDistribution distribution = new DCatDistribution();
			
			distribution.setAccessURL(new IdString(cfg.getUserportalBaseUrl() + "#/dataexplorer/detail/" + metadataST.getTenantCode() + "/"
					+ metadataST.getDataset().getCode()));

			distribution.setDownloadURL(new IdString(cfg.getExposedApiBaseUrl() + metadataST.getDataset().getCode() + "/Binaries?"));

			distribution.setId(metadataST.getDataset().getDatasetId()+"", "binary");
			
			distribution.setLicense(licenseDistribution);
			
			distribution.setJsonFormat();
			
			final String idDistribution = distribution.getId();
			DCatObject distributionToAdd = getDcatObject(linkedData, objectsMap, distribution, new Empty() {
				@Override
				public DCatObject get() {
					DCatDistribution empty = new DCatDistribution();
					empty.setFormat(null);
					empty.cloneId(idDistribution, true);
					return empty;
				}
			});
			dsDCAT.addDistribution( (DCatDistribution) distributionToAdd);
		}
	}
	
	private String convertToTurtle(String json) {
		com.hp.hpl.jena.rdf.model.Model model = com.hp.hpl.jena.rdf.model.ModelFactory.createDefaultModel();
		InputStream in = new ByteArrayInputStream(json.getBytes());
		RDFDataMgr.read(model, in, null, Lang.JSONLD);
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		
		RDFDataMgr.write(arrayOutputStream, model, Lang.TURTLE);
		
		return arrayOutputStream.toString();
	}
	/*
	public static void main(String[] args) {
		com.hp.hpl.jena.rdf.model.Model model = com.hp.hpl.jena.rdf.model.ModelFactory.createDefaultModel();

		 // use the FileManager to find the input file
		 InputStream in = com.hp.hpl.jena.util.FileManager.get().open( "D:\\catalogo.jsonld" );
//		 InputStream in = new ByteArrayInputStream( json.getBytes(  ) );
		if (in == null) {
		    throw new IllegalArgumentException("not found");
		}

		try {
		StringWriter writer = new StringWriter();
		IOUtils.copy(in, writer,"UTF-8");
		String theString = writer.toString();
		

		
		//System.out.println(convertToTurtle(theString));
		} catch (Exception e) {
			e.printStackTrace();
		}
		RDFDataMgr.read(model, in, null, Lang.JSONLD);
		// read the RDF/XML file
		//model.read(in,"json-ld");

		// write it to standard out
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		
		RDFDataMgr.write(arrayOutputStream, model, Lang.TURTLE);
		System.out.println(arrayOutputStream.toString());
		
		
	}
		*/
	
	
	// GIANFRANCO
//	if (linkedData) {
//		if (!objectsMap.containsKey(creator.getId()))
//			objectsMap.put(creator.getId(), creator);
//		DCatAgent empty = new DCatAgent();
//		empty.cloneId(creator.getId(), true);
//		dsDCAT.setCreator(empty);
//	} 
//	else{
//		dsDCAT.setCreator(creator);
//	}
}
