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

import java.util.List;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.tribe.db.TribeDAO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TribeService extends OpenmrsService {
	/**
	 * Used by Spring to set the specific/chosen database access implementation
	 * 
	 * @param dao The dao implementation to use
	 */
	public void setTribeDAO(TribeDAO dao);
	
	/**
	 */
	public void createTribe(Tribe tribe) throws APIException;

	/**
	 * Update Tribe
	 */
	public void updateTribe(Tribe tribe) throws APIException;

	/**
	 * Delete Tribe
	 */
	public void deleteTribe(Tribe tribe) throws APIException;

	/**
	 * Retire Tribe
	 */
	public void retireTribe(Tribe tribe) throws APIException;

	/**
	 * Unretire Tribe
	 */
	public void unretireTribe(Tribe tribe) throws APIException;
	

	/**
	 * Get tribe by internal tribe identifier
	 * 
	 * @return Tribe
	 * @param tribeId
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized( { TribeConstants.PRIV_VIEW_TRIBES })
	public Tribe getTribe(Integer tribeId) throws APIException;

	/**
	 * Get list of tribes that are not retired
	 * 
	 * @return non-retired Tribe list
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized( { TribeConstants.PRIV_VIEW_TRIBES })
	public List<Tribe> getTribes() throws APIException;

	/**
	 * Find tribes by partial name lookup
	 * 
	 * @return non-retired Tribe list
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized( { TribeConstants.PRIV_VIEW_TRIBES })
	public List<Tribe> findTribes(String search) throws APIException;	
}
