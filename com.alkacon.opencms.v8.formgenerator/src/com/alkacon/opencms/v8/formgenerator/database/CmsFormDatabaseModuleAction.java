/*
 * File   : $Source: /alkacon/cvs/alkacon/com.alkacon.opencms.v8.formgenerator/src/com/alkacon/opencms/v8/formgenerator/database/CmsFormDatabaseModuleAction.java,v $
 * Date   : $Date: 2010/05/21 13:49:28 $
 * Version: $Revision: 1.7 $
 *
 * This file is part of the Alkacon OpenCms Add-On Module Package
 *
 * Copyright (c) 2010 Alkacon Software GmbH (http://www.alkacon.com)
 *
 * The Alkacon OpenCms Add-On Module Package is free software: 
 * you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * The Alkacon OpenCms Add-On Module Package is distributed 
 * in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Alkacon OpenCms Add-On Module Package.  
 * If not, see http://www.gnu.org/licenses/.
 *
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com.
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org.
 */

package com.alkacon.opencms.v8.formgenerator.database;

import org.opencms.configuration.CmsConfigurationManager;
import org.opencms.file.CmsObject;
import org.opencms.main.CmsLog;
import org.opencms.module.A_CmsModuleAction;
import org.opencms.module.CmsModule;

import java.sql.SQLException;

import org.apache.commons.logging.Log;

/**
 * Module action class used to ensure that the required database tables for form data are there.<p>
 * 
 * @author Achim Westermann
 * 
 * @version $Revision: 1.7 $
 * 
 * @since 7.0.4
 *
 */
public class CmsFormDatabaseModuleAction extends A_CmsModuleAction {

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsFormDatabaseModuleAction.class);

    /**
     * Empty constructor.<p>
     */
    public CmsFormDatabaseModuleAction() {

        // noop
    }

    /**
     * @see org.opencms.module.A_CmsModuleAction#initialize(org.opencms.file.CmsObject, org.opencms.configuration.CmsConfigurationManager, org.opencms.module.CmsModule)
     */
    @Override
    public void initialize(CmsObject adminCms, CmsConfigurationManager configurationManager, CmsModule module) {

        super.initialize(adminCms, configurationManager, module);
        try {
            CmsFormDataAccess.getInstance().setCms(adminCms);
            CmsFormDataAccess.getInstance().ensureDBTablesExistance();
        } catch (SQLException sqlex) {
            if (LOG.isErrorEnabled()) {
                LOG.error(sqlex.getLocalizedMessage(), sqlex);
            }
        }
    }
}
