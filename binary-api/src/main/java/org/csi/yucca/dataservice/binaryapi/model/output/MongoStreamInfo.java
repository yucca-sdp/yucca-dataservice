/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.binaryapi.model.output;

public class MongoStreamInfo extends MongoDatasetInfo{
	
	public static int STREAM_TYPE_UNDEFINED=-1;
	public static int STREAM_TYPE_SENSOR=1;
	public static int STREAM_TYPE_APPLICATION=2; //2 da mongo
	public static int STREAM_TYPE_INTERNAL=0;
	public static int STREAM_TYPE_TWEET=3;
	
	private long streamId=STREAM_TYPE_UNDEFINED;
	private long streamDeploymentVersion=-1;
	private String streamCode=null;
	private String sensorCode=null;
	private int tipoStream=STREAM_TYPE_SENSOR;
	
	public int getTipoStream() {
		return tipoStream;
	}
	public void setTipoStream(int tipoStream) {
		this.tipoStream = tipoStream;
	}
	public String getStreamCode() {
		return streamCode;
	}
	public void setStreamCode(String streamCode) {
		this.streamCode = streamCode;
	}
	public String getSensorCode() {
		return sensorCode;
	}
	public void setSensorCode(String sensorCode) {
		this.sensorCode = sensorCode;
	}
	public long getStreamDeploymentVersion() {
		return streamDeploymentVersion;
	}
	public void setStreamDeploymentVersion(long streamDeploymentVersion) {
		this.streamDeploymentVersion = streamDeploymentVersion;
	}
	public long getStreamId() {
		return streamId;
	}
	public void setStreamId(long streamId) {
		if (streamId==STREAM_TYPE_APPLICATION ||
				streamId==STREAM_TYPE_INTERNAL || 
				streamId==STREAM_TYPE_SENSOR || 
				streamId==STREAM_TYPE_TWEET  
				) this.streamId = streamId;
		
		else this.streamId=STREAM_TYPE_UNDEFINED;
	}
	
}
