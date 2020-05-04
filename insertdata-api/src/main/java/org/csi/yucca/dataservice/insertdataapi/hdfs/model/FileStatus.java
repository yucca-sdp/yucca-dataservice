/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.hdfs.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FileStatus {

	private String fileId;

	private String accessTime;

	private String replication;

	private String length;

	private String owner;

	private String permission;

	private String blockSize;

	private String modificationTime;

	private String group;

	private String type;

	private String childrenNum;

	private String pathSuffix;

	private String storagePolicy;

	public FileStatus() {
		super();
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(String accessTime) {
		this.accessTime = accessTime;
	}

	public String getReplication() {
		return replication;
	}

	public void setReplication(String replication) {
		this.replication = replication;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(String blockSize) {
		this.blockSize = blockSize;
	}

	public String getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(String modificationTime) {
		this.modificationTime = modificationTime;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChildrenNum() {
		return childrenNum;
	}

	public void setChildrenNum(String childrenNum) {
		this.childrenNum = childrenNum;
	}

	public String getPathSuffix() {
		return pathSuffix;
	}

	public void setPathSuffix(String pathSuffix) {
		this.pathSuffix = pathSuffix;
	}

	public String getStoragePolicy() {
		return storagePolicy;
	}

	public void setStoragePolicy(String storagePolicy) {
		this.storagePolicy = storagePolicy;
	}

	@Override
	public String toString() {
		return "ClassPojo [fileId = " + fileId + ", accessTime = " + accessTime + ", replication = " + replication + ", length = " + length + ", owner = " + owner
				+ ", permission = " + permission + ", blockSize = " + blockSize + ", modificationTime = " + modificationTime + ", group = " + group + ", type = " + type
				+ ", childrenNum = " + childrenNum + ", pathSuffix = " + pathSuffix + ", storagePolicy = " + storagePolicy + "]";
	}
}