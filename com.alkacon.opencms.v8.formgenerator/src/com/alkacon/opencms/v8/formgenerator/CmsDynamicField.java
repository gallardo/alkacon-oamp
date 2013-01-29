/*
 * File   : $Source: /alkacon/cvs/alkacon/com.alkacon.opencms.v8.formgenerator/src/com/alkacon/opencms/v8/formgenerator/CmsDynamicField.java,v $
 * Date   : $Date: 2011/03/09 15:14:35 $
 * Version: $Revision: 1.4 $
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

package com.alkacon.opencms.v8.formgenerator;

import org.opencms.i18n.CmsMessages;

/**
 * Represents a dynamic field, i.e. its value will be generated just before committing data.<p>
 * 
 * @author Michael Moossen 
 * 
 * @version $Revision: 1.4 $
 * 
 * @since 7.0.4 
 */
public class CmsDynamicField extends A_CmsField {

    /** Field type: dynamic field. */
    private static final String TYPE = "dynamic";

    /** The already resolved value. */
    private String m_resolvedValue;

    /**
     * Returns the type of the input field, e.g. "text" or "select".<p>
     * 
     * @return the type of the input field
     */
    public static String getStaticType() {

        return TYPE;
    }

    /**
     * @see com.alkacon.opencms.v8.formgenerator.I_CmsField#buildHtml(CmsFormHandler, CmsMessages, String, boolean, String)
     */
    @Override
    public String buildHtml(
        CmsFormHandler formHandler,
        CmsMessages messages,
        String errorKey,
        boolean showMandatory,
        String infoKey) {

        StringBuffer buf = new StringBuffer();
        return buf.toString();
    }

    /**
     * Returns the already resolved value.<p>
     * 
     * @return the already resolved value
     */
    public String getResolvedValue() {

        return m_resolvedValue;
    }

    /**
     * @see com.alkacon.opencms.v8.formgenerator.I_CmsField#getType()
     */
    public String getType() {

        return TYPE;
    }

    /**
     * Sets the already resolved value.<p>
     *
     * @param resolvedValue the resolved value to set
     */
    public void setResolvedValue(String resolvedValue) {

        m_resolvedValue = resolvedValue;
    }
}
