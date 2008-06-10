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
package org.openmrs.module.tribe;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.Activator;
import org.openmrs.module.ModuleException;
import org.openmrs.util.OpenmrsClassLoader;

/**
 * This class contains the logic that is run every time this module
 * is either started or shutdown
 */
public class TribeActivator implements Activator {

	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * @see org.openmrs.module.Activator#startup()
	 */
	public void startup() {
		log.info("Starting Tribe Module");
		
		// check whether tribe module is installed in an old OpenMRS
		// throw exception if it is
		boolean isOldOpenMRS = true;
		try {
			Class cls = OpenmrsClassLoader.getInstance().loadClass("org.openmrs.Tribe");
		} catch (ClassNotFoundException e) {
			// OpenMRS version ok
			isOldOpenMRS = false;
		}
		if(isOldOpenMRS) {
			throw new ModuleException("Tribe module cannot be used with this OpenMRS version. Please upgrade OpenMRS.");
		}
		
		// now convert patient tribes to tribe attributes
		AdministrationService as = Context.getAdministrationService();

		// check whether patient table has tribe column
		log.info("Ignore the error message if occurs: " +
				"Error while running sql: SELECT distinct tribe from patient where 1 = 0");
		log.info("The above message indicates that you are running a new implementation " +
				"which does not have a tribe column in the patient table.");
		boolean isTribeColumnExists = true;
		try {
			as.executeSQL("SELECT distinct tribe from patient where 1 = 0", true);
		} catch (Exception e) {
			// tribe column not found
			isTribeColumnExists = false;
		}
		
		if(isTribeColumnExists) {
			// create tribe attributes
			log.info("Transforming tribe details");
			as.executeSQL(
				"INSERT INTO person_attribute (person_id, value, person_attribute_type_id, creator, date_created)"
				+ " SELECT patient_id, tribe," 
				+ " (SELECT person_attribute_type_id FROM person_attribute_type WHERE name = 'Tribe')"
				+ " , 1, now() FROM patient WHERE tribe IS NOT NULL;"
				, false);
			
			// drop tribe column in patient, this will make these scripts not run again
			log.info("Dropping old tribe column");
			as.executeSQL("ALTER TABLE patient DROP FOREIGN KEY belongs_to_tribe;", false);
			as.executeSQL("ALTER TABLE patient DROP COLUMN tribe;", false);
			
			log.info("Tribe data conversion complete");
		}
		
	}
	
	/**
	 *  @see org.openmrs.module.Activator#shutdown()
	 */
	public void shutdown() {
		log.info("Shutting down Tribe Module");
	}
	
}
