/*
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) Alkacon Software GmbH (http://www.alkacon.com)
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

package com.alkacon.opencms.v8.calendar.client.widget.css;

import org.opencms.acacia.client.css.I_CmsLayoutBundle.I_Widgets;

import com.google.gwt.core.client.GWT;

/**
 * Content editor CSS resources bundle.<p>
 */
public interface I_CmsLayoutBundle extends org.opencms.gwt.client.ui.css.I_CmsLayoutBundle {

    /** The XML content widget CSS. */
    interface I_CmsWidgetCss extends I_Widgets {

        /**
         * Css class reader.<p>
         * 
         * @return the css class
         */
        String selectBoxPanel();

        /**
         * Css class reader.<p>
         * 
         * @return the css class
         */
        String serialDataTabel();

        /**
         * Css class reader.<p>
         * 
         * @return the css class
         */
        String serialDataTabelBorderBottom();

        /**
         * Css class reader.<p>
         * 
         * @return the css class
         */
        String serialDataTabelBorderRight();

        /**
         * Css class reader.<p>
         * 
         * @return the css class
         */
        String serialDataWidget();

        /**
         * Css class reader.<p>
         * 
         * @return the css class
         */
        String serialDateCheckBox();

        /**
         * Css class reader.<p>
         * 
         * @return the css class
         */
        String serialDateDay();

        /**
         * Css class reader.<p>
         * 
         * @return the css class
         */
        String serialDateLable();

        /**
         * Css class reader.<p>
         * 
         * @return the css class
         */
        String serialDatelowPanel();

        /**
         * Css class reader.<p>
         * 
         * @return the css class
         */
        String serialDatelowPanelSelection();

        /**
         * Css class reader.<p>
         * 
         * @return the css class
         */
        String serialDateMonth();

        /**
         * Css class reader.<p>
         * 
         * @return the css class
         */
        String serialDateMonthSelection();

        /**
         * Css class reader.<p>
         * 
         * @return the css class
         */
        String serialDateWeek();

        /**
         * Css class reader.<p>
         * 
         * @return the css class
         */
        String serialDateYear();

        /**
         * Css class reader.<p>
         * 
         * @return the css class
         */
        String serialDateYearSelection();

        /**
         * Css class reader.<p>
         * 
         * @return the css class
         */
        String textBoxSerialDate();

    }

    /** The bundle instance. */
    I_CmsLayoutBundle INSTANCE = GWT.create(I_CmsLayoutBundle.class);

    /**
     * Access method.<p>
     * 
     * @return the XML content widget CSS
     */
    @Source("widget.css")
    I_CmsWidgetCss widgetCss();
}
