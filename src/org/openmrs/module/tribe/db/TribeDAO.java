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

package org.openmrs.module.tribe.db;

import java.util.List;

import org.openmrs.api.db.DAOException;
import org.openmrs.module.tribe.Tribe;

public interface TribeDAO {
	/**
	 * Create a new Tribe
	 * @param Tribe to create
	 * @throws DAOException
	 */
	public void createTribe(Tribe tribe) throws DAOException;

	/**
	 * Update Tribe
	 * @param Tribe to update
	 * @throws DAOException
	 */
	public void updateTribe(Tribe tribe) throws DAOException;

	/**
	 * Delete Tribe
	 * @param Tribe to delete
	 * @throws DAOException
	 */
	public void deleteTribe(Tribe tribe) throws DAOException;	
	
	/**
	 * Retire Tribe
	 * @param Tribe to retire
	 * @throws DAOException
	 */
	public void retireTribe(Tribe tribe) throws DAOException;	

	/**
	 * Unretire Tribe
	 * @param Tribe to unretire
	 * @throws DAOException
	 */
	public void unretireTribe(Tribe tribe) throws DAOException;
	

	/**
	 * Get tribe by internal tribe identifier
	 * 
	 * @return Tribe
	 * @param tribeId 
	 * @throws DAOException
	 */
	public Tribe getTribe(Integer tribeId) throws DAOException;
	
	/**
	 * Get list of tribes that are not retired
	 * 
	 * @return non-retired Tribe list
	 * @throws DAOException
	 */
	public List<Tribe> getTribes() throws DAOException;
	
	/**
	 * Get tribes by partial name lookup
	 * 
	 * @param Search string
	 * @return non-retired Tribe list
	 * @throws DAOException
	 */
	public List<Tribe> findTribes(String s) throws DAOException;	
}
