/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.metadataapi.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class RestBase {

	protected JSONObject secretObject = new JSONObject();

	public RestBase() {
		super();
	}
	
	public void setUpSecretObject(String file) throws IOException {
		String str = readFile(file);
		secretObject = new JSONObject(str);
	
	}

	protected Iterator<Object[]> getFromJson(String file) {
		ArrayList<Object[]> data = new ArrayList<Object[]>();

		String str = readFile(file);
		JSONObject json = new JSONObject(str);
		JSONArray jsArray = json.getJSONArray("data");

		for (int i = 0; i < jsArray.length(); i++) {
			JSONObject arr = jsArray.getJSONObject(i);

			// merge with secret

			Iterator<String> iterSecret = secretObject.keys();
			String tmp_key;
			while (iterSecret.hasNext()) {
				tmp_key = (String) iterSecret.next();
				if (!arr.has(tmp_key)) {
					arr.put(tmp_key, secretObject.get(tmp_key));
				}
			}

			data.add(new Object[] { arr });
		}

		return data.iterator();

	}

	
	protected String readFile(String file) {
		String jsonData = "";
		BufferedReader br = null;
		try {
			String line;
			InputStream inputStream = this.getClass().getResourceAsStream(file);
			if (inputStream!=null)
			{
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				br = new BufferedReader(inputStreamReader);
				while ((line = br.readLine()) != null) {
					jsonData += line + "\n";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return jsonData;
	}

}