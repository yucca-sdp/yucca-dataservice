/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package it.csi.smartdata.dataapi.test;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OdataTotalRecordCountTest extends OdataTestBase {
	
	
	
	@BeforeClass
	public void setUpSecretObject() throws IOException {
		super.setUpSecretObject("/testSecret.json");
	}	

	@DataProvider(name="json")
	public Iterator<Object[]> getFromJson()
	{
		return super.getFromJson("/OdataTestRecordCount_dataIn.json");
	}
	
	
	@Test(dataProvider="json")
	public void odataCheckTotalCount(JSONObject dato) {

		//verifica unicamente il conteggio totale dei record
		
		RequestSpecification rs =  given();

		if (StringUtils.isNotEmpty(dato.optString("odata.username")))
		{
			rs = rs.auth().basic(dato.getString("odata.username"), dato.getString("odata.password"));
		}	 

		
		Response rsp = rs.when().get(makeUrl(dato,"json"));

		
		rsp.then().assertThat().body("d.__count",  Matchers.equalTo(dato.getString("odata.retdata.totalCount")));
		
		
		
	}
	
	
}
