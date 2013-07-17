package org.openmrs.module.tribe.extension.html;

import java.util.Map;

import org.openmrs.PersonAttribute;
import org.openmrs.api.context.Context;
import org.openmrs.module.Extension;
import org.openmrs.module.tribe.Tribe;
import org.openmrs.module.tribe.TribeService;

public class PatientDashboardHeader extends Extension {

	private String patientId = "";

	/**
	 * @see org.openmrs.module.web.extension.AdministrationSectionExt#getMediaType()
	 */
	public Extension.MEDIA_TYPE getMediaType() {
		return Extension.MEDIA_TYPE.html;
	}

	@Override
	public void initialize(Map<String, String> parameters) {
		patientId = parameters.get("patientId");
	}

	@Override
	public String getOverrideContent(String bodyContent) {
		StringBuilder content = new StringBuilder(" &nbsp; Tribe: ");
		PersonAttribute tribeAttribute = Context.getPatientService()
				.getPatient(Integer.valueOf(patientId)).getAttribute("Tribe");
		
		if(tribeAttribute != null) {
			String tribeId = tribeAttribute.getValue();
			if (tribeId != null && (!"".equals(tribeId))) {
				TribeService ts = ((TribeService) Context
						.getService(TribeService.class));
				Tribe tribe = ts.getTribe(Integer.valueOf(tribeId));
				content.append("<b>");
				content.append(tribe.getName());
				content.append("</b>");
			}
		}
		return content.toString();
	}

}
