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

import org.opencms.acacia.client.widgets.I_CmsEditWidget;
import com.alkacon.opencms.v8.calendar.client.input.CmsSerialDate;
import com.alkacon.opencms.v8.calendar.client.widget.css.I_CmsLayoutBundle;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.ui.Composite;

/**
 * Provides a DHTML calendar widget, for use on a widget dialog.<p>
 * 
 * */
public class CmsSerialDateWidget extends Composite implements I_CmsEditWidget {

    /** Value of the activation. */
    private boolean m_active = true;

    /** JSON of all labels for this widget. */
    private JSONObject m_labels;

    /** The global select box. */
    private CmsSerialDate m_serialDate;

    /**
     * Constructs an CmsComboWidget with the in XSD schema declared configuration.<p>
     * @param config The configuration string given from OpenCms XSD.
     */
    public CmsSerialDateWidget(String config) {

        parseConfiguration(config);
        m_serialDate = new CmsSerialDate(m_labels);
        // All composites must call initWidget() in their constructors.
        initWidget(m_serialDate);

        ValueChangeHandler<String> handler = new ValueChangeHandler<String>() {

            @Override
            public void onValueChange(ValueChangeEvent<String> arg0) {

                fireChangeEvent();

            }

        };
        I_CmsLayoutBundle.INSTANCE.widgetCss().ensureInjected();
        m_serialDate.addStyleName(org.opencms.ade.contenteditor.client.css.I_CmsLayoutBundle.INSTANCE.generalCss().cornerAll());
        m_serialDate.addStyleName(I_CmsLayoutBundle.INSTANCE.widgetCss().serialDataWidget());
        m_serialDate.addValueChangeHandler(handler);
    }

    @Override
    public HandlerRegistration addFocusHandler(FocusHandler handler) {

        return null;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {

        return addHandler(handler, ValueChangeEvent.getType());
    }

    /**
     * Represents a value change event.<p>
     * 
     */
    public void fireChangeEvent() {

        ValueChangeEvent.fire(this, m_serialDate.getFormValueAsString());

    }

    @Override
    public String getValue() {

        return m_serialDate.getFormValueAsString();
    }

    @Override
    public boolean isActive() {

        return m_active;
    }

    @Override
    public void onAttachWidget() {

        super.onAttach();
    }

    @Override
    public boolean owns(Element element) {

        return getElement().isOrHasChild(element);
    }

    @Override
    public void setActive(boolean active) {

        if (active == m_active) {
            return;
        }
        m_active = active;

        if (m_active) {
            getElement().removeClassName(org.opencms.acacia.client.css.I_CmsLayoutBundle.INSTANCE.form().inActive());
            getElement().focus();
        } else {
            getElement().addClassName(org.opencms.acacia.client.css.I_CmsLayoutBundle.INSTANCE.form().inActive());
            m_serialDate.clearFealds();
        }
        m_serialDate.setActive(m_active);
        if (active) {
            fireChangeEvent();
        }
    }

    @Override
    public void setName(String name) {

        //m_serialDate.setName(name);
    }

    @Override
    public void setValue(String value) {

        setValue(value, false);

    }

    @Override
    public void setValue(String value, boolean fireEvents) {

        m_serialDate.setFormValueAsString(value);
        if (fireEvents) {
            fireChangeEvent();
        }

    }

    /**
     * Parse the configuration into a JSON.
     * @param config the configuration string
     * */
    private void parseConfiguration(String config) {

        m_labels = (JSONObject)JSONParser.parseStrict(config);

    }

}
