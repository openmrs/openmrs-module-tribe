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

package org.openmrs.module.tribe.impl;

import java.util.List;

import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.tribe.Tribe;
import org.openmrs.module.tribe.TribeConstants;
import org.openmrs.module.tribe.TribeService;
import org.openmrs.module.tribe.db.TribeDAO;

public class TribeServiceImpl extends BaseOpenmrsService implements TribeService {
	
	protected TribeDAO dao;
	
	/**
	 */
	public void setTribeDAO(TribeDAO dao) {
		this.dao = dao;
	}	

	/**
	 * Create a new Tribe
	 * 
	 * @param Tribe to create
	 * @throws APIException
	 */
	public void createTribe(Tribe tribe) throws APIException {
		if (!Context.hasPrivilege(TribeConstants.PRIV_MANAGE_TRIBES))
			throw new APIAuthenticationException("Privilege required: "
			        + TribeConstants.PRIV_MANAGE_TRIBES);

		dao.createTribe(tribe);
	}

	/**
	 * Update Tribe
	 * 
	 * @param Tribe to update
	 * @throws APIException
	 */
	public void updateTribe(Tribe tribe) throws APIException {
		if (!Context.hasPrivilege(TribeConstants.PRIV_MANAGE_TRIBES))
			throw new APIAuthenticationException("Privilege required: "
			        + TribeConstants.PRIV_MANAGE_TRIBES);

		dao.updateTribe(tribe);
	}

	/**
	 * Delete Tribe
	 * 
	 * @param Tribe to delete
	 * @throws APIException
	 */
	public void deleteTribe(Tribe tribe) throws APIException {
		if (!Context.hasPrivilege(TribeConstants.PRIV_MANAGE_TRIBES))
			throw new APIAuthenticationException("Privilege required: "
			        + TribeConstants.PRIV_MANAGE_TRIBES);

		dao.deleteTribe(tribe);
	}
	
	/**
	 * Retire Tribe
	 * 
	 * @param Tribe to retire
	 * @throws APIException
	 */
	public void retireTribe(Tribe tribe) throws APIException {
		if (!Context.hasPrivilege(TribeConstants.PRIV_MANAGE_TRIBES))
			throw new APIAuthenticationException("Privilege required: "
			        + TribeConstants.PRIV_MANAGE_TRIBES);

		dao.retireTribe(tribe);
	}

	/**
	 * Unretire Tribe
	 * 
	 * @param Tribe to unretire
	 * @throws APIException
	 */
	public void unretireTribe(Tribe tribe) throws APIException {
		if (!Context.hasPrivilege(TribeConstants.PRIV_MANAGE_TRIBES))
			throw new APIAuthenticationException("Privilege required: "
			        + TribeConstants.PRIV_MANAGE_TRIBES);

		dao.unretireTribe(tribe);
	}
	
	// tribe section

	/**
	 * @deprecated tribe will be moved to patient attribute
	 * @see org.openmrs.api.PatientService#getTribe(java.lang.Integer)
	 */
	public Tribe getTribe(Integer tribeId) throws APIException {
		return dao.getTribe(tribeId);
	}
	
	/**
	 * @deprecated tribe will be moved to patient attributes
	 * @see org.openmrs.api.PatientService#getTribes()
	 */
	public List<Tribe> getTribes() throws APIException {
		return dao.getTribes();
	}
		
	/**
	 * @deprecated tribe will be moved to patient attributes
	 * @see org.openmrs.api.PatientService#findTribes(java.lang.String)
	 */
	public List<Tribe> findTribes(String search) throws APIException {
		return dao.findTribes(search);
	}
	
	// end tribe section
}
