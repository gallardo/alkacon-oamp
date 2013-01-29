/*
 * File   : $Source: /alkacon/cvs/alkacon/com.alkacon.opencms.v8.excelimport/src/com/alkacon/opencms/v8/excelimport/CmsExcelXmlConfigurationMapping.java,v $
 * Date   : $Date: 2009/04/30 10:52:08 $
 * Version: $Revision: 1.1 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (C) 2002 - 2009 Alkacon Software (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.alkacon.opencms.v8.excelimport;

import org.opencms.util.CmsStringUtil;

/**
 * Includes mappings between excel file and XML content from an excel import configuration file.<p>
 * 
 * On read content items can become accessed.<p> 
 * 
 * @author Mario Jaeger
 * 
 * @version $Revision: 1.1 $ 
 * 
 * @since 7.5.0
 */
public class CmsExcelXmlConfigurationMapping {

    /** The mapped XML tag name. */
    private String m_excelColumnName;

    /** The mandatory flag. */
    private boolean m_mandatory;

    /** The weight. */
    private int m_weight = -1;

    /** The mapped XML tag name. */
    private String m_xmlTagName;

    /**
     * Gets the mapped excel column name.<p>
     * 
     * @return mapped excel column name
     */
    public String getExcelColumnName() {

        if (CmsStringUtil.isNotEmpty(m_excelColumnName)) {
            return m_excelColumnName;
        } else {
            return "";
        }
    }

    /**
     * Gets the mandatory flag.<p>
     * 
     * @return the mandatory flag
     */
    public boolean getMandatory() {

        return m_mandatory;
    }

    /**
     * Gets the weight.<p>
     * 
     * @return weight
     */
    public int getWeight() {

        return m_weight;
    }

    /**
     * Gets the mapped XML tag name.<p>
     * 
     * @return mapped XML tag name
     */
    public String getXmlTagName() {

        if (CmsStringUtil.isNotEmpty(m_xmlTagName)) {
            return m_xmlTagName;
        } else {
            return "";
        }
    }

    /**
     * Sets the mapped excel column name.<p>
     * 
     * @param excelColumnName mapped excel column name
     */
    public void setExcelColumnName(String excelColumnName) {

        m_excelColumnName = excelColumnName;
    }

    /**
     * Sets the mandatory flag.<p>
     * 
     * @param mandatory mandatory flag
     */
    public void setMandatory(boolean mandatory) {

        m_mandatory = mandatory;
    }

    /**
     * Sets the weight.<p>
     * 
     * @param weight weight
     */
    public void setWeight(int weight) {

        m_weight = weight;
    }

    /**
     * Sets the mapped XML tag name.<p>
     * 
     * @param xmlTagName mapped XML tag name
     */
    public void setXmlTagName(String xmlTagName) {

        m_xmlTagName = xmlTagName;
    }
}
