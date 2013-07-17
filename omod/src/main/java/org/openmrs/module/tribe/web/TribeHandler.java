/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.tribe.web;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.tribe.Tribe;
import org.openmrs.module.tribe.TribeService;
import org.openmrs.web.taglib.fieldgen.AbstractFieldGenHandler;
import org.openmrs.web.taglib.fieldgen.FieldGenHandler;

public class TribeHandler extends AbstractFieldGenHandler implements FieldGenHandler {

	private String defaultUrl = "list.field";
	
	public void run() {
		setUrl(defaultUrl);

		if ( fieldGenTag != null ) {
			String initialValue = "";
			Tribe t = (Tribe)this.fieldGenTag.getVal();
			if ( t != null ) if ( t.getTribeId() != null ) initialValue = t.getTribeId().toString();

			TribeService ts = ((TribeService) Context.getService(TribeService.class));
			List<Tribe> tribes = ts.getTribes();
			if ( tribes == null ) tribes = new ArrayList<Tribe>();

			setParameter("initialValue", initialValue);
			setParameter("list", tribes);
		}
	}
}
