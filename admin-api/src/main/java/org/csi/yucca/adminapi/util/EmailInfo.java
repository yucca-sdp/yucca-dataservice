/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.util;

public class EmailInfo {
	
	private String email; 
	private String name;
	private String message; 
	private String messageHtml;
	private String from;
	private String subject;

	public static EmailInfo build(String email){
		EmailInfo emailInfo = new EmailInfo();
		emailInfo.email = email;
		return emailInfo;
	}
	
	public EmailInfo name(String name){
		this.name = name;
		return this;
	}
	public EmailInfo message(String message){
		this.message = message;
		return this;
	} 
	public EmailInfo from(String from){
		this.from = from;
		return this;
	}
	public EmailInfo subject(String subject){
		this.subject = subject;
		return this;
	}
	public EmailInfo messageHtml(String messageHtml){
		this.messageHtml = messageHtml;
		return this;
	} 
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessageHtml() {
		return messageHtml;
	}

	public void setMessageHtml(String messageHtml) {
		this.messageHtml = messageHtml;
	}
	
	
	
	
}
