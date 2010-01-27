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
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.Activator;
import org.openmrs.module.ModuleException;
import org.openmrs.util.OpenmrsClassLoader;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.Role;

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
        // Upgrade tribe column to person attribute.
        extractTribeColumn();

        // Tribe module version 1.6 adds uuid column to tribe.
        // This cannot go into sqldiff.xml because TribeActivator.extractTribeColumn() must execute first
        // and sqldiff.xml executes before activator.startup()
        addUuidColumn();
		
    }

    private void extractTribeColumn() {
        // check whether tribe module is installed in an old OpenMRS
        // throw exception if it is
        boolean isOldOpenMRS = true;
        try {
            Class cls = OpenmrsClassLoader.getInstance().loadClass("org.openmrs.Tribe");
            if (cls.isAnnotationPresent(Deprecated.class))
                isOldOpenMRS = false;
        } catch (ClassNotFoundException e) {
            // OpenMRS version ok
            isOldOpenMRS = false;
        }
        if (isOldOpenMRS) {
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
            Context.addProxyPrivilege(OpenmrsConstants.PRIV_SQL_LEVEL_ACCESS);
            as.executeSQL("SELECT distinct tribe from patient where 1 = 0", true);
        } catch (Exception e) {
            // tribe column not found
            isTribeColumnExists = false;
        } finally {
            Context.removeProxyPrivilege(OpenmrsConstants.PRIV_SQL_LEVEL_ACCESS);
        }

        if (isTribeColumnExists) {
            // create tribe attributes
            try {
                log.info("Transforming tribe details");
                Context.addProxyPrivilege(OpenmrsConstants.PRIV_SQL_LEVEL_ACCESS);
                as.executeSQL(
                        "INSERT INTO person_attribute (person_id, value, person_attribute_type_id, creator, date_created, uuid)"
                                + " SELECT patient_id, tribe,"
                                + " (SELECT person_attribute_type_id FROM person_attribute_type WHERE name = 'Tribe')"
                                + " , 1, now(), UUID() FROM patient WHERE tribe IS NOT NULL;"
                        , false);
            }
            finally {
                Context.removeProxyPrivilege(OpenmrsConstants.PRIV_SQL_LEVEL_ACCESS);
            }

            // drop tribe column in patient, this will make these scripts not run again
            log.info("Dropping old tribe column");
            try {
                Context.addProxyPrivilege(OpenmrsConstants.PRIV_SQL_LEVEL_ACCESS);
                as.executeSQL("ALTER TABLE patient DROP FOREIGN KEY belongs_to_tribe;", false);
            } catch (Exception e) {
                log.warn("Unable to drop foreign key patient.belongs_to_tribe", e);
            } finally {
                Context.removeProxyPrivilege(OpenmrsConstants.PRIV_SQL_LEVEL_ACCESS);
            }

            try {
                Context.addProxyPrivilege(OpenmrsConstants.PRIV_SQL_LEVEL_ACCESS);
                as.executeSQL("ALTER TABLE patient DROP COLUMN tribe;", false);
            }
            catch (Exception e) {
                log.warn("Unable to drop column patient.tribe", e);
            }
            finally {
                Context.removeProxyPrivilege(OpenmrsConstants.PRIV_SQL_LEVEL_ACCESS);
            }

            log.info("Tribe data conversion complete");
        }

        // add View Tribes privilege to Authenticated role if not exists
        try {
            Context.addProxyPrivilege(OpenmrsConstants.PRIV_SQL_LEVEL_ACCESS);
            UserService us = Context.getUserService();
            Role authenticatedRole = us.getRole(OpenmrsConstants.AUTHENTICATED_ROLE);
            if (!authenticatedRole.hasPrivilege(TribeConstants.PRIV_VIEW_TRIBES)) {
                as.executeSQL("INSERT INTO role_privilege (role, privilege) VALUES ('"
                        + OpenmrsConstants.AUTHENTICATED_ROLE + "', '"
                        + TribeConstants.PRIV_VIEW_TRIBES + "')", false);
            }
        }
        finally {
            Context.removeProxyPrivilege(OpenmrsConstants.PRIV_SQL_LEVEL_ACCESS);
        }
    }

    /**
     * Adds uuid column and fills uuid values. This cannot be done from the sqldiff.xml because this has to be done after
     * startup() sql changes. This way we make sure all the previous changes are performed before this new change.
     */
    private void addUuidColumn() {
        AdministrationService as = Context.getAdministrationService();

        // Check if uuid column exists.
        boolean isUuidColumnExists = true;
		try {
            log.info("Ignore the error if occurs: " +
                "Error while running sql: SELECT distinct uuid from tribe where 1 = 0 . Message: Unknown column 'uuid' in 'field list'");
            Context.addProxyPrivilege(OpenmrsConstants.PRIV_SQL_LEVEL_ACCESS);
			as.executeSQL("SELECT distinct uuid from tribe where 1 = 0", true);
		} catch (Exception e) {
			// uuid column not found
			isUuidColumnExists = false;
		} finally {
            Context.removeProxyPrivilege(OpenmrsConstants.PRIV_SQL_LEVEL_ACCESS);
        }

        // If exists that means this has already run once. Nothing to do further.
        if(isUuidColumnExists) {
            log.info("tribe.uuid column exists. Nothing to do further.");
            return;
        }

        // Otherwise,
        // Add uuid column (with not null which makes the default value blank string for existing rows in MySQL.)
        log.info("Add tribe.uuid column");
        try {
            Context.addProxyPrivilege(OpenmrsConstants.PRIV_SQL_LEVEL_ACCESS);
            as.executeSQL("alter table tribe add column uuid char(36) not null", false);
        } finally {
            Context.removeProxyPrivilege(OpenmrsConstants.PRIV_SQL_LEVEL_ACCESS);
        }

        // Generate tribe uuids
        log.info("Generate tribe.uuid values");
        try {
            Context.addProxyPrivilege(OpenmrsConstants.PRIV_SQL_LEVEL_ACCESS);
            as.executeSQL("update tribe set uuid = uuid()", false);
        } finally {
            Context.removeProxyPrivilege(OpenmrsConstants.PRIV_SQL_LEVEL_ACCESS);
        }
    }

    /**
	 *  @see org.openmrs.module.Activator#shutdown()
	 */
	public void shutdown() {
		log.info("Shutting down Tribe Module");
	}
}
