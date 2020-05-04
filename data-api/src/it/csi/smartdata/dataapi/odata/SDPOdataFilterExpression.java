/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package it.csi.smartdata.dataapi.odata;

public class SDPOdataFilterExpression {
	private String clause=null;

	public String getClause() {
		return clause;
	}

	public void setClause(String clause) {
		this.clause = clause;
	}
	
	public SDPOdataFilterExpression(String clause) {
		this.clause = clause;
	}
	public SDPOdataFilterExpression() {
	}
	
	public String toString() {
		return this.clause;
	}
}
