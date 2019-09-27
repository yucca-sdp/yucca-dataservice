/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.metadataapi.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.csi.yucca.dataservice.metadataapi.delegate.resources.ResourcesDelegate;
import org.csi.yucca.dataservice.metadataapi.delegate.resources.StreamsForIcon;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class Util {
	public static String nvl(Object o) {
		return o == null ? "" : o.toString();
	}

	public static String nvlt(Object o) {
		return nvl(o).trim();
	}

	public static boolean isEmpty(Object o) {
		return nvlt(o) == "";
	}

	public static String cleanString(String in) {
		if (in != null)
			return in.replaceAll(" ", "").replaceAll("[^\\w\\s]", "");

		return "";
	}

	static String toProperCase(String in) {
		if (in != null && in.length() > 1)
			return in.substring(0, 1).toUpperCase() + in.substring(1).toLowerCase();
		else if (in != null)
			return in.toUpperCase();
		return "";

	}

	public static String safeSubstring(String in, int length) {
		String out = in;
		if (in != null && in.length() > length)
			out = in.substring(0, length);

		return out == null ? "" : out;
	}

	public static String cleanStringCamelCase(String in) {
		String out = "";
		if (in != null) {
			in = in.replaceAll("[-]", " ").replaceAll("[.]", " ").replaceAll("[/]", " ");
			String[] words = in.split(" ");
			for (String word : words) {
				out += toProperCase(cleanString(word));
			}
		}

		return out;
	}

	public static String cleanStringCamelCase(String in, int length) {
		return safeSubstring(cleanStringCamelCase(in), length);
	}

	
	public static String formatDateCkan(Date date) {
		String formattedDate = null;
		if (date != null) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			formattedDate = df.format(date);
		}
		return formattedDate;
	}

	public static void main(String[] args) {
		// String json =
		// "{ configData : { idDataset : \"primoID\", \"tenant\" : \"iotnet\", \"collection\" : \"provaAle\", \"database\": \"db\", \"type\" : \"dataset\", \"subtype\" : \"bulkDataset\", \"entityNameSpace\" : \"it.csi.smartdata.odata.iotnet.iotnetapi002\", \"datasetversion\" : \"1.0.0\", \"current\" : 1, \"archive\" : { \"archiveCollection\" : \"altraCollection\", \"archiveDatabase\": \"altroDB\",  \"archiveInfo\": [ { \"archiveRevision\": 1, \"archiveDate\" : null }, { \"archiveRevision\": 2, \"archiveDate\" : null } ]	 }			 }, \"metadata\" : { \"name\" : \"primodato\", \"description\" : \"Descrizione\", \"license\" : \"CC BY 4.0\", \"disclaimer\" : null, \"copyright\" : \"Copyright (C) 2014, CSP Innovazione nelle ICT. All rights reserved.\", \"visibility\" : \"public\", \"registrationDate\" : null, \"requestorName\" : \"nomeRichiedente\", \"requestorSurname\" : \"cognomeRichiedente\", \"dataDomain\" : \"ENVIRONMENT\", \"requestornEmail\" : \"ciao@email.it\", \"fps\" : 22,  \"startIngestionDate\" : null, \"endIngestionDate\" : null, \"importFileType\" : \"CSV\", \"datasetStatus\": \"Ok\",  \"tags\" : [{ \"tagCode\" : \"AIR\" }, { \"tagCode\" : \"INDOOR\" }, { \"tagCode\" : \"POLLUTION\" }, { \"tagCode\" : \"QUALITY\" } ], \"fields\" : [{ \"fieldName\" : \"campo_1\", \"fieldAlias\" : \"semantica campo 1\", \"dataType\" : \"int\", \"sourceColumn\" : \"1\", \"isKey\" : 0, \"measureUnit\" : \"ppm\" }, { \"fieldName\" : \"campo_2\", \"fieldAlias\" : \"semantica campo 2\", \"dataType\" : \"string\", \"sourceColumn\" : \"2\", \"isKey\" : 0, \"measureUnit\" : \"metri\" } ] } }";
		// String json2 =
		// "{ configData : { idDataset : \"secondoID\", \"tenant\" : \"iotnet\", \"collection\" : \"provaAle\", \"database\": \"db\", \"type\" : \"dataset\", \"subtype\" : \"bulkDataset\", \"entityNameSpace\" : \"it.csi.smartdata.odata.iotnet.iotnetapi002\", \"datasetversion\" : \"1.0.0\", \"current\" : 1, \"archive\" : { \"archiveCollection\" : \"altraCollection\", \"archiveDatabase\": \"altroDB\",  \"archiveInfo\": [ { \"archiveRevision\": 1, \"archiveDate\" : null }, { \"archiveRevision\": 2, \"archiveDate\" : null } ]	 }			 }, \"metadata\" : { \"name\" : \"primodato\", \"description\" : \"Descrizione\", \"license\" : \"CC BY 4.0\", \"disclaimer\" : null, \"copyright\" : \"Copyright (C) 2014, CSP Innovazione nelle ICT. All rights reserved.\", \"visibility\" : \"public\", \"registrationDate\" : null, \"requestorName\" : \"nomeRichiedente\", \"requestorSurname\" : \"cognomeRichiedente\", \"dataDomain\" : \"ENVIRONMENT\", \"requestornEmail\" : \"ciao@email.it\", \"fps\" : 22,  \"startIngestionDate\" : null, \"endIngestionDate\" : null, \"importFileType\" : \"CSV\", \"datasetStatus\": \"Ok\",  \"tags\" : [{ \"tagCode\" : \"AIR\" }, { \"tagCode\" : \"INDOOR\" }, { \"tagCode\" : \"POLLUTION\" }, { \"tagCode\" : \"QUALITY\" } ], \"fields\" : [{ \"fieldName\" : \"campo_1\", \"fieldAlias\" : \"semantica campo 1\", \"dataType\" : \"int\", \"sourceColumn\" : \"1\", \"isKey\" : 0, \"measureUnit\" : \"ppm\" }, { \"fieldName\" : \"campo_2\", \"fieldAlias\" : \"semantica campo 2\", \"dataType\" : \"string\", \"sourceColumn\" : \"2\", \"isKey\" : 0, \"measureUnit\" : \"metri\" } ] } }";
		// Gson gson = new GsonBuilder().setExclusionStrategies(new
		// GSONExclusionStrategy()).create();
		// Metadata d1 = gson.fromJson(json,Metadata.class);
		// Metadata d2 = gson.fromJson(json2,Metadata.class);
		// d1.setId("nuovoID");
		// System.out.println("1 ok" + d1.toJson());
		//
		// List<Metadata> result = new LinkedList<Metadata>();
		// result.add(d1);
		// result.add(d2);
		// System.out.println("2 ok" + gson.toJson(result));
		//
		// String json3 =
		// "{\"dataset\":{\"id\":\"5459050d73456b346624b067\",\"configData\":{\"idDataset\":\"quarto\",\"tenant\":\"sandbox\",\"type\":\"dataset\",\"subtype\":\"bulkDataset\",\"datasetversion\":\"1\",\"current\":\"1\"},\"metadata\":{\"name\":\"aaa\",\"registrationDate\":\"Nov 4, 2014 5:55:41 PM\",\"requestorName\":\"aa\",\"requestorSurname\":\"aa\",\"dataDomain\":\"AGRICULTURE\",\"requestornEmail\":\"aa\",\"tags\":[{}],\"visibility\":\"public\",\"license\":\"lll\"}}}";
		// Metadata d3 = Metadata.fromJson(json3);
		// System.out.println("3ok" + gson.toJson(d3));
		// Metadata m = new Metadata();
		//
		// Info info = new Info();
		// Field[] fields = new Field[1];
		// info.setFields(fields);
		// m.setInfo(info);;

		String out = Util.cleanStringCamelCase("asd-sfd.t/12", 12);
		System.out.println(out);
	}

	public static String join(String[] strings, String glue) {

		String result = "";
		if (glue == null)
			glue = ",";

		if (strings != null) {
			int counter = 0;
			for (String s : strings) {
				result += s;
				counter++;
				if (counter < strings.length)
					result += glue;
			}
		}
		return result;
	}

	public static String join(List<Object> objectList, String glue) {

		String out = "";
		int counter = 0;
		if (objectList != null) {
			for (Object o : objectList) {
				if (o instanceof String)
					out += o;
				else
					out += o.toString();
				if (counter < objectList.size() - 1)
					out += glue;
				counter++;
			}
		}
		return out;
	}
	
	@Deprecated
	public static byte[] extractImageFromStream(String streamJson) throws IOException {
		Gson gson = JSonHelper.getInstance();
		StreamsForIcon streamsForIcon = gson.fromJson(streamJson, StreamsForIcon.class);
		// InputStream streamIconIS = null;
		BufferedImage imag = null;
		if (streamsForIcon != null && streamsForIcon.getStreams() != null && streamsForIcon.getStreams().getStream() != null
				&& streamsForIcon.getStreams().getStream().getStreamIcon() != null) {
			String streamIcon = streamsForIcon.getStreams().getStream().getStreamIcon();

			imag = readStreamImageBuffer(streamIcon);
		} else
			imag = ImageIO.read(ResourcesDelegate.class.getClassLoader().getResourceAsStream("stream-icon-default.png"));

		byte[] iconBytes = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(imag, "png", baos);
		baos.flush();
		iconBytes = baos.toByteArray();
		baos.close();

		return iconBytes;
	}

	public static  BufferedImage readStreamImageBuffer(String imageBase64) throws IOException {
		BufferedImage imag = null;

		if (imageBase64 != null) {
			String[] imageBase64Array = imageBase64.split(",");

			String imageBase64Clean;
			if (imageBase64Array.length > 1) {
				imageBase64Clean = imageBase64Array[1];
			} else {
				imageBase64Clean = imageBase64Array[0];
			}

			byte[] bytearray = Base64.decodeBase64(imageBase64Clean.getBytes());
			imag = ImageIO.read(new ByteArrayInputStream(bytearray));
		}
		if (imageBase64 == null || imag == null) {
			imag = ImageIO.read(ResourcesDelegate.class.getClassLoader().getResourceAsStream("entity-icon-default.png"));

		}
		return imag;
	}

}
