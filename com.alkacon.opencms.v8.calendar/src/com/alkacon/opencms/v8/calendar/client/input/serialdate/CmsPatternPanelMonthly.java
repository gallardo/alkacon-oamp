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

package com.alkacon.opencms.v8.calendar.client.input.serialdate;

import com.alkacon.opencms.v8.calendar.client.widget.css.I_CmsLayoutBundle;

import org.opencms.gwt.client.ui.input.CmsRadioButton;
import org.opencms.gwt.client.ui.input.CmsRadioButtonGroup;
import org.opencms.gwt.client.ui.input.CmsSelectBox;

import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * */
public class CmsPatternPanelMonthly extends FlowPanel implements HasValueChangeHandlers<String> {

    /** Group off all radio buttons. */
    private CmsRadioButtonGroup m_group = new CmsRadioButtonGroup();

    /** The panel for all values of 'every'. */
    private FlowPanel m_everyPanel = new FlowPanel();

    /** The panel for all values of 'at'. */
    private FlowPanel m_atPanel = new FlowPanel();

    /** The text box for the date input. */
    private TextBox m_everyDay = new TextBox();
    /** The select box for the month selection. */
    private TextBox m_everyMonth = new TextBox();

    /** The select box for the nummeric selection. */
    private CmsSelectBox m_atNummer = new CmsSelectBox();
    /** The select box for the day selection. */
    private CmsSelectBox m_atDay = new CmsSelectBox();
    /** The select box for the month selection. */
    private TextBox m_atMonth = new TextBox();

    /** The array of all radiobuttons. */
    private CmsRadioButton[] m_radio = new CmsRadioButton[2];

    /** The value change handler. */
    private ValueChangeHandler<String> m_handler;

    /** JSON of all needed labels. */
    private JSONObject m_labels;

    /**
     * Default constructor to create the panel.<p>
     * @param labels JSON of all needed labels
     */
    public CmsPatternPanelMonthly(JSONObject labels) {

        m_labels = labels;

        addStyleName(I_CmsLayoutBundle.INSTANCE.widgetCss().serialDateMonth());
        CmsRadioButton sel1 = new CmsRadioButton(
            "sel1",
            m_labels.get("GUI_SERIALDATE_MONTHLY_MONTHDAY_AT_0").isString().stringValue());
        sel1.setGroup(m_group);
        m_radio[0] = sel1;
        sel1.setChecked(true);
        sel1.addStyleName(I_CmsLayoutBundle.INSTANCE.widgetCss().serialDateMonthSelection());
        sel1.addStyleName(org.opencms.acacia.client.css.I_CmsWidgetsLayoutBundle.INSTANCE.widgetCss().radioButtonlabel());
        createEverPanel();
        CmsRadioButton sel2 = new CmsRadioButton(
            "sel2",
            m_labels.get("GUI_SERIALDATE_MONTHLY_WEEKDAY_AT_0").isString().stringValue());
        m_radio[1] = sel2;
        sel2.setGroup(m_group);
        sel2.addStyleName(I_CmsLayoutBundle.INSTANCE.widgetCss().serialDateMonthSelection());
        sel2.addStyleName(org.opencms.acacia.client.css.I_CmsWidgetsLayoutBundle.INSTANCE.widgetCss().radioButtonlabel());
        createAtPanel();
        this.add(sel1);
        this.add(m_everyPanel);

        this.add(sel2);
        this.add(m_atPanel);

    }

    /**
     * @see com.google.gwt.event.logical.shared.HasValueChangeHandlers#addValueChangeHandler(com.google.gwt.event.logical.shared.ValueChangeHandler)
     */
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {

        m_handler = handler;
        m_atNummer.addValueChangeHandler(m_handler);
        m_atDay.addValueChangeHandler(m_handler);
        m_atMonth.addValueChangeHandler(m_handler);
        m_everyDay.addValueChangeHandler(m_handler);
        m_everyMonth.addValueChangeHandler(m_handler);
        for (int i = 0; i < m_radio.length; i++) {
            m_radio[i].addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {

                    fireValueChange();

                }
            });
        }
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /**
     * Represents a value change event.<p>
     */
    public void fireValueChange() {

        ValueChangeEvent.fire(this, getWeekDays());
    }

    /**
     * Returns the day of month.<p>
     * @return the day of month
     * 
     * */
    public String getDayOfMonth() {

        if (m_group.getSelectedButton().getName().equals("sel1")) {
            return m_everyDay.getText();
        } else {
            return m_atNummer.getFormValueAsString();
        }
    }

    /**
     * Returns the interval.<p>
     * @return the interval
     * */
    public String getInterval() {

        if (m_group.getSelectedButton().getName().equals("sel1")) {
            return m_everyMonth.getText();
        } else {
            return m_atMonth.getText();
        }
    }

    /**
     * Returns the week day.<p>
     * @return the week day
     * */
    public String getWeekDays() {

        if (m_group.getSelectedButton().getName().equals("sel1")) {
            return "-1";
        } else {
            return m_atDay.getFormValueAsString();
        }
    }

    /**
     * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
     */
    @Override
    public Iterator<Widget> iterator() {

        Iterator<Widget> result = getChildren().iterator();
        return result;
    }

    /**
     * @see com.google.gwt.user.client.ui.Panel#remove(com.google.gwt.user.client.ui.Widget)
     */
    @Override
    public boolean remove(Widget child) {

        return remove(child);
    }

    /**
     * Sets the day of month.<p>
     * @param dayOfMonthStr the day of month
     */
    public void setDayOfMonth(int dayOfMonthStr) {

        if (m_group.getSelectedButton().getName().equals("sel1")) {
            m_everyDay.setText(dayOfMonthStr + "");
        } else {
            m_atNummer.selectValue(dayOfMonthStr + "");
        }

    }

    /**
     * Sets the interval.<p> 
     * @param intervalStr the interval
     */
    public void setInterval(String intervalStr) {

        if (m_group.getSelectedButton().getName().equals("sel1")) {
            m_everyMonth.setText(intervalStr);
        } else {
            m_atMonth.setText(intervalStr);
        }

    }

    /**
     * Sets the week day.<p>
     * 
     * @param weekDayStr the week day
     * */
    public void setWeekDay(int weekDayStr) {

        if (weekDayStr == -1) {
            m_group.selectButton(m_radio[0]);
        } else {
            m_group.selectButton(m_radio[1]);
            m_atDay.selectValue(weekDayStr + "");
        }

    }

    /**
     * Creates the 'at' selection view.<p>
     * */
    private void createAtPanel() {

        m_atPanel.add(m_atNummer);
        m_atNummer.addStyleName(I_CmsLayoutBundle.INSTANCE.widgetCss().selectBoxPanel());
        m_atNummer.getOpener().setStyleName(
            org.opencms.acacia.client.css.I_CmsWidgetsLayoutBundle.INSTANCE.widgetCss().selectBoxSelected());
        m_atNummer.getSelectorPopup().addStyleName(I_CmsLayoutBundle.INSTANCE.globalWidgetCss().selectBoxPopup());
        m_atNummer.addOption("1", m_labels.get("GUI_SERIALDATE_WEEKDAYNUMBER_1_0").isString().stringValue());
        m_atNummer.addOption("2", m_labels.get("GUI_SERIALDATE_WEEKDAYNUMBER_2_0").isString().stringValue());
        m_atNummer.addOption("3", m_labels.get("GUI_SERIALDATE_WEEKDAYNUMBER_3_0").isString().stringValue());
        m_atNummer.addOption("4", m_labels.get("GUI_SERIALDATE_WEEKDAYNUMBER_4_0").isString().stringValue());
        m_atNummer.addOption("5", m_labels.get("GUI_SERIALDATE_WEEKDAYNUMBER_5_0").isString().stringValue());
        m_atNummer.setWidth("80px");

        m_atPanel.add(m_atDay);
        m_atDay.addStyleName(I_CmsLayoutBundle.INSTANCE.widgetCss().selectBoxPanel());
        m_atDay.getOpener().setStyleName(
            org.opencms.acacia.client.css.I_CmsWidgetsLayoutBundle.INSTANCE.widgetCss().selectBoxSelected());
        m_atDay.getSelectorPopup().addStyleName(I_CmsLayoutBundle.INSTANCE.globalWidgetCss().selectBoxPopup());
        m_atDay.addOption("1", m_labels.get("GUI_SERIALDATE_DAY_SUNDAY_0").isString().stringValue());
        m_atDay.addOption("2", m_labels.get("GUI_SERIALDATE_DAY_MONDAY_0").isString().stringValue());
        m_atDay.addOption("3", m_labels.get("GUI_SERIALDATE_DAY_TUESDAY_0").isString().stringValue());
        m_atDay.addOption("4", m_labels.get("GUI_SERIALDATE_DAY_WEDNESDAY_0").isString().stringValue());
        m_atDay.addOption("5", m_labels.get("GUI_SERIALDATE_DAY_THURSDAY_0").isString().stringValue());
        m_atDay.addOption("6", m_labels.get("GUI_SERIALDATE_DAY_FRIDAY_0").isString().stringValue());
        m_atDay.addOption("7", m_labels.get("GUI_SERIALDATE_DAY_SATURDAY_0").isString().stringValue());
        m_atDay.setWidth("100px");

        Label every = new Label(m_labels.get("GUI_SERIALDATE_MONTHLY_WEEKDAY_EVERY_0").isString().stringValue());
        every.addStyleName(I_CmsLayoutBundle.INSTANCE.widgetCss().serialDateLable());
        m_atPanel.add(every);
        m_atPanel.add(m_atMonth);
        m_atMonth.setStyleName(I_CmsLayoutBundle.INSTANCE.widgetCss().textBoxSerialDate());
        m_atMonth.addKeyPressHandler(new KeyPressHandler() {

            public void onKeyPress(KeyPressEvent event) {

                fireValueChange();

            }
        });
        Label month = new Label(m_labels.get("GUI_SERIALDATE_MONTHLY_MONTH_0").isString().stringValue());
        month.addStyleName(I_CmsLayoutBundle.INSTANCE.widgetCss().serialDateLable());
        m_atPanel.add(month);

    }

    /**
     * Creates the 'every' selection view.<p>
     * 
     * */
    private void createEverPanel() {

        m_everyPanel.add(m_everyDay);
        m_everyDay.setStyleName(I_CmsLayoutBundle.INSTANCE.widgetCss().textBoxSerialDate());
        m_everyDay.setText("1");
        m_everyDay.addKeyPressHandler(new KeyPressHandler() {

            public void onKeyPress(KeyPressEvent event) {

                fireValueChange();

            }
        });
        Label day = new Label(m_labels.get("GUI_SERIALDATE_MONTHLY_MONTHDAY_DAY_EVERY_0").isString().stringValue());
        day.addStyleName(I_CmsLayoutBundle.INSTANCE.widgetCss().serialDateLable());
        m_everyPanel.add(day);
        m_everyPanel.add(m_everyMonth);
        m_everyMonth.setText("1");
        m_everyMonth.setStyleName(I_CmsLayoutBundle.INSTANCE.widgetCss().textBoxSerialDate());
        m_everyMonth.addKeyPressHandler(new KeyPressHandler() {

            public void onKeyPress(KeyPressEvent event) {

                fireValueChange();

            }
        });
        Label month = new Label(m_labels.get("GUI_SERIALDATE_MONTHLY_MONTH_0").isString().stringValue());
        month.addStyleName(I_CmsLayoutBundle.INSTANCE.widgetCss().serialDateLable());
        m_everyPanel.add(month);

    }

}
