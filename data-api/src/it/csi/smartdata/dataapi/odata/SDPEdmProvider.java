/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package it.csi.smartdata.dataapi.odata;

import it.csi.smartdata.dataapi.adminapi.SDPAdminApiOdataCast;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.Association;
import org.apache.olingo.odata2.api.edm.provider.AssociationSet;
import org.apache.olingo.odata2.api.edm.provider.ComplexType;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.edm.provider.EntityContainerInfo;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.FunctionImport;
import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.apache.olingo.odata2.api.exception.ODataException;

public class SDPEdmProvider extends EdmProvider {




	//private ArrayList<DBObject> configObject=null;

	
	private SDPAdminApiOdataCast adminApiAccess= new SDPAdminApiOdataCast();
	private String codiceApi=null;

	public String getCodiceApi() {
		return codiceApi;
	}

	public void setCodiceApi(String codiceApi) {
		this.codiceApi = codiceApi;
	}

	static Logger log = Logger.getLogger(SDPEdmProvider.class.getPackage().getName());



	@Override
	public EntityType getEntityType(final FullQualifiedName edmFQName) throws ODataException {
		log.debug("[SDPEdmProvider::getEntityType] BEGIN - calling SDPMongoOdataCast" );
		return adminApiAccess.getEntityType(edmFQName, this.codiceApi);
	}


	@Override
	public ComplexType getComplexType(final FullQualifiedName edmFQName) throws ODataException {
		log.debug("[SDPEdmProvider::getComplexType] BEGIN - calling SDPMongoOdataCast" );
		
		return adminApiAccess.getComplexType(edmFQName, this.codiceApi);

	}


	@Override
	public AssociationSet getAssociationSet(final String entityContainer, final FullQualifiedName association,
			final String sourceEntitySetName, final String sourceEntitySetRole) throws ODataException {
		log.debug("[SDPEdmProvider::getAssociationSet] BEGIN - calling SDPMongoOdataCast" );
		return adminApiAccess.getAssociationSet(entityContainer, association, sourceEntitySetName, sourceEntitySetRole,  this.codiceApi);
		
	}

	@Override
	public EntitySet getEntitySet(final String entityContainer, final String name) throws ODataException {
		log.debug("[SDPEdmProvider::getEntitySet] BEGIN - calling SDPMongoOdataCast" );
		return adminApiAccess.getEntitySet(entityContainer, name, this.codiceApi);
	}
			
	@Override
	public Association getAssociation(final FullQualifiedName edmFQName) throws ODataException {
		log.debug("[SDPEdmProvider::getAssociation] BEGIN - calling SDPMongoOdataCast" );
		return adminApiAccess.getAssociation(edmFQName, this.codiceApi);
	}



	@Override
	public FunctionImport getFunctionImport(final String entityContainer, final String name) throws ODataException {
		log.debug("[SDPEdmProvider::getFunctionImport] BEGIN - return null" );
		return null;
	}

	@Override
	public EntityContainerInfo getEntityContainerInfo(final String name) throws ODataException {
		log.debug("[SDPEdmProvider::getEntityContainerInfo] BEGIN - calling SDPMongoOdataCast" );
		return adminApiAccess.getEntityContainerInfo(name, this.codiceApi);
	}




	@Override
	public List<Schema> getSchemas() throws ODataException {
		log.debug("[SDPEdmProvider::getSchemas] BEGIN " );
		try {
			return adminApiAccess.getSchemasInternal(this.codiceApi);
		} catch (Exception ex) {
			log.error("[SDPEdmProvider::getSchemas] unexpected exeption",ex);
			ODataException oex = new ODataException("unexpected",ex);
			throw oex;
		} finally {
			log.debug("[SDPEdmProvider::getSchemas] END " );
		}
	}


	

}
