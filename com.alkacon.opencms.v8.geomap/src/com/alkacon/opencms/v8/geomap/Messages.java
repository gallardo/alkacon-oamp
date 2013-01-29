/*
 * File   : $Source: /alkacon/cvs/alkacon/com.alkacon.opencms.v8.geomap/src/com/alkacon/opencms/v8/geomap/Messages.java,v $
 * Date   : $Date: 2011/02/16 13:05:25 $
 * Version: $Revision: 1.1 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * Copyright (c) 2005 Alkacon Software GmbH (http://www.alkacon.com)
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
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.alkacon.opencms.v8.geomap;

import org.opencms.i18n.A_CmsMessageBundle;
import org.opencms.i18n.I_CmsMessageBundle;

/**
 * Convenience class to access the localized messages of this OpenCms package.<p> 
 * 
 * @author Michael Moossen
 * 
 * @version $Revision: 1.1 $
 */
public final class Messages extends A_CmsMessageBundle {

    /** Message constant for key in the resource bundle. */
    public static final String ERR_PARAMETER_OUT_OF_RANGE_2 = "ERR_PARAMETER_OUT_OF_RANGE_2";

    /** Message constant for key in the resource bundle. */
    public static final String ERR_PROPERTY_NOT_FOUND_1 = "ERR_PROPERTY_NOT_FOUND_1";

    /** Message constant for key in the resource bundle. */
    public static final String ERR_UNKNOWN_MAP_MODE_1 = "ERR_UNKNOWN_MAP_MODE_1";

    /** Message constant for key in the resource bundle. */
    public static final String ERR_UNKNOWN_MAP_PROPERTY_1 = "ERR_UNKNOWN_MAP_PROPERTY_1";

    /** Message constant for key in the resource bundle. */
    public static final String ERR_UNKNOWN_MAP_TYPE_1 = "ERR_UNKNOWN_MAP_TYPE_1";

    /** Message constant for key in the resource bundle. */
    public static final String LOG_ERR_FILE_CONTENT_1 = "LOG_ERR_FILE_CONTENT_1";

    /** Name of the used resource bundle. */
    private static final String BUNDLE_NAME = "com.alkacon.opencms.v8.geomap.messages";

    /** Static instance member. */
    private static final I_CmsMessageBundle INSTANCE = new Messages();

    /**
     * Returns an instance of this localized message accessor.<p>
     * 
     * @return an instance of this localized message accessor
     */
    public static I_CmsMessageBundle get() {

        return INSTANCE;
    }

    /**
     * Hides the public constructor for this utility class.<p>
     */
    private Messages() {

        // hide the constructor
    }

    /**
     * Returns the bundle name for this OpenCms package.<p>
     * 
     * @return the bundle name for this OpenCms package
     */
    public String getBundleName() {

        return BUNDLE_NAME;
    }
}
