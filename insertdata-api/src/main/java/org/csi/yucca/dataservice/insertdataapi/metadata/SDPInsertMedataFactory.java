/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.metadata;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csi.yucca.dataservice.insertdataapi.adminapi.SDPAdminApiAccess;

public class SDPInsertMedataFactory {
	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");
	
	public static SDPInsertMetadataApiAccess getSDPInsertMetadataApiAccess()
	{
		return new SDPAdminApiAccess();
			
	}
	
	
}
