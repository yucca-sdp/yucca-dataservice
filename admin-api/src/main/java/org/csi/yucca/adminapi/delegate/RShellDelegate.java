/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.delegate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class RShellDelegate {
	private static final Logger logger = Logger.getLogger(RShellDelegate.class);
	
	public static String callRemoteShell(String destination, String username, String password, String action) throws JSchException, IOException {

		JSch jsch = new JSch();
		Session session;
		String msg = "-1";
		String ret = "-1";
		String privateKey = "~/.ssh/id_rsa";
//		try {
			jsch.addIdentity(privateKey);
			session = jsch.getSession(username, destination, 22);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");

			session.setConfig(config);
			session.connect();
			//
			ChannelExec channel = (ChannelExec) session.openChannel("exec");
			//BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
			//BufferedReader ext = new BufferedReader(new InputStreamReader(channel.getExtInputStream()));
			StringBuilder outputBuffer = new StringBuilder();
			StringBuilder errorBuffer = new StringBuilder();

			InputStream in = channel.getInputStream();
			InputStream err = channel.getExtInputStream();

			channel.setCommand(action);
			channel.connect();

//			while ((msg = in.readLine()) != null) {
//				logger.debug("[RShellDelegate::callRemoteShell] - in msg: " + msg);
//			}
//
//			while ((msg = ext.readLine()) != null) {
//				logger.debug("[RShellDelegate::callRemoteShell] - ext msg: " + msg);
//			}
			
			
			byte[] tmp = new byte[1024];
			while (true) {

			    while (in.available() > 0) {
			        int i = in.read(tmp, 0, 1024);
			        if (i < 0) break;
			        logger.info("[RShellDelegate::callRemoteShell] - output"+ new String(tmp, 0, i));
			        outputBuffer.append(new String(tmp, 0, i));
			    }
		        

			    while (err.available() > 0) {
			        int i = err.read(tmp, 0, 1024);
			        if (i < 0) break;
			        logger.info("[RShellDelegate::callRemoteShell] - error"+ new String(tmp, 0, i));
			        errorBuffer.append(new String(tmp, 0, i));
			    }
			    if (channel.isClosed()) {
			        if ((in.available() > 0)) //|| (err.available() > 0)) 
			        {
				        continue; 
			        }
			        logger.info("[RShellDelegate::callRemoteShell] - exit-status: " + channel.getExitStatus());
			        break;
			    }
			    try { 
			      Thread.sleep(1000);
			    } catch (Exception ee) {
			    }
			}
			ret = Integer.toString(channel.getExitStatus());
			logger.info("[RShellDelegate::callRemoteShell] - outputBuffer: " + outputBuffer);
			logger.info("[RShellDelegate::callRemoteShell] - errorBuffer: " + errorBuffer);
			//logger.info("[RShellDelegate::callRemoteShell] - msg: " + msg);
			channel.disconnect();
			session.disconnect();
//		} catch (JSchException e) {
//			logger.error("[RShellDelegate::callRemoteShell] - JSchException " + e);
//		} catch (IOException e) {
//			logger.error("[RShellDelegate::callRemoteShell] - IOException " + e);
//		}
		return ret;

	}
	

	public static void main(String[] args) {
		
	}
	
}
