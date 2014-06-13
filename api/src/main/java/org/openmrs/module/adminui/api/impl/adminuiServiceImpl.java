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
package org.openmrs.module.adminui.api.impl;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.adminui.api.adminuiService;
import org.openmrs.module.adminui.api.db.adminuiDAO;

/**
 * It is a default implementation of {@link adminuiService}.
 */
public class adminuiServiceImpl extends BaseOpenmrsService implements adminuiService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private adminuiDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(adminuiDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public adminuiDAO getDao() {
	    return dao;
    }
}