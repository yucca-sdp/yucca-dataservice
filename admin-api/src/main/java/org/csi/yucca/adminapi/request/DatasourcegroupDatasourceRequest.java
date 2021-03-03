/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.request;

import java.util.List;

public class DatasourcegroupDatasourceRequest {
	
	private Long idDatasourceGroup;
	
	private Integer datasourcegroupversion;
	
	private List<DataSourceRequest> datasources;

	public Long getIdDatasourceGroup() {
		return idDatasourceGroup;
	}

	public void setIdDatasourceGroup(Long idDatasourceGroup) {
		this.idDatasourceGroup = idDatasourceGroup;
	}

	public List<DataSourceRequest> getDatasources() {
		return datasources;
	}

	public void setDatasources(List<DataSourceRequest> datasources) {
		this.datasources = datasources;
	}

	public Integer getDatasourcegroupversion() {
		return datasourcegroupversion;
	}

	public void setDatasourcegroupversion(Integer datasourcegroupversion) {
		this.datasourcegroupversion = datasourcegroupversion;
	}
	
}
