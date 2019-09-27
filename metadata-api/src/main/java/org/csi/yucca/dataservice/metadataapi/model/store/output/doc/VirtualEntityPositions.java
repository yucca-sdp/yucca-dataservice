/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.metadataapi.model.store.output.doc;

import java.util.List;

public class VirtualEntityPositions {

	private List<Position> position;

	public VirtualEntityPositions() {
	}

	public List<Position> getPosition() {
		return position;
	}

	public void setPosition(List<Position> position) {
		this.position = position;
	}

}
