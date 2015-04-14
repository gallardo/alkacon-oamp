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

package com.alkacon.opencms.v8.calendar.client.widget;

import org.opencms.acacia.client.widgets.CmsFormWidgetWrapper;
import org.opencms.acacia.client.widgets.I_CmsEditWidget;
import org.opencms.acacia.client.widgets.I_CmsFormEditWidget;

import org.opencms.ade.contenteditor.widgetregistry.client.A_NativeWidgetFactory;

import com.google.gwt.dom.client.Element;

/**
 * Factory for the serial date widget.<p>
 */
public class CmsSerialDateWidgetFactory extends A_NativeWidgetFactory {

    @Override
    public I_CmsFormEditWidget createFormWidget(String configuration) {

        return new CmsFormWidgetWrapper(new CmsSerialDateWidget(configuration));
    }

    @Override
    public I_CmsEditWidget createInlineWidget(String configuration, Element element) {

        return null;
    }

    @Override
    protected String getInitCallName() {

        return "initSerialDateWidget";
    }

    @Override
    protected String getWidgetName() {

        return "com.alkacon.opencms.v8.calendar.CmsSerialDateWidget";
    }
}
