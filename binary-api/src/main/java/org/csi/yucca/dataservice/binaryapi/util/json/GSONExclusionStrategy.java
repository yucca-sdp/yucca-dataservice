/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.binaryapi.util.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class GSONExclusionStrategy implements ExclusionStrategy {

	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

	public boolean shouldSkipField(FieldAttributes f) {
		return f.getAnnotation(IgnoredJSON.class) != null;
	}

}
