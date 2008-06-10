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

package org.openmrs.module.tribe.db.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.tribe.Tribe;
import org.openmrs.module.tribe.db.TribeDAO;

public class HibernateTribeDAO implements TribeDAO {

	protected Log log = LogFactory.getLog(getClass());

	/**
	 * Hibernate session factory
	 */
	private SessionFactory sessionFactory;

	/**
	 * Set session factory
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) { 
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @see org.openmrs.api.db.AdministrationService#updateTribe(org.openmrs.Tribe)
	 */
	public void saveTribe(Tribe tribe) throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(tribe);
	}	

	/**
	 * @see org.openmrs.api.db.AdministrationService#deleteTribe(org.openmrs.Tribe)
	 */
	public void purgeTribe(Tribe tribe) throws DAOException {
		sessionFactory.getCurrentSession().delete(tribe);
	}
	
	/**
	 * @see org.openmrs.api.db.AdministrationService#retireTribe(org.openmrs.Tribe)
	 */
	public void retireTribe(Tribe tribe) throws DAOException {
		tribe.setRetired(true);
		saveTribe(tribe);
	}

	/**
	 * @see org.openmrs.api.db.AdministrationService#unretireTribe(org.openmrs.Tribe)
	 */
	public void unretireTribe(Tribe tribe) throws DAOException {
		tribe.setRetired(false);
		saveTribe(tribe);
	}
	

	/**
	 * @see org.openmrs.api.PatientService#getTribe()
	 * @deprecated tribe will be moved to patient attribute
	 */
	public Tribe getTribe(Integer tribeId) throws DAOException {
		Tribe tribe = (Tribe) sessionFactory.getCurrentSession()
		                                    .get(Tribe.class, tribeId);
		
		return tribe;
	}
	
	/**
	 * @see org.openmrs.api.PatientService#getTribes()
	 */
	@SuppressWarnings("unchecked")
	public List<Tribe> getTribes() throws DAOException {
		List<Tribe> tribes = sessionFactory.getCurrentSession()
		                                   .createQuery("from Tribe t order by t.name asc")
		                                   .list();
		
		return tribes;
	}
	
	/**
	 * @see org.openmrs.api.PatientService#findTribes()
	 */
	@SuppressWarnings("unchecked")
	public List<Tribe> findTribes(String s) throws DAOException {
		Criteria crit = sessionFactory.getCurrentSession()
		                              .createCriteria(Tribe.class);
		crit.add(Expression.like("name", s, MatchMode.START));
		crit.addOrder(Order.asc("name"));
		
		return crit.list();
	}	
}
