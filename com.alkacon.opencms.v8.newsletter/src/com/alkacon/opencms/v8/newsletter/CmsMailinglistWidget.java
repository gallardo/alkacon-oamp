/*
 * File   : $Source: /alkacon/cvs/alkacon/com.alkacon.opencms.v8.newsletter/src/com/alkacon/opencms/v8/newsletter/CmsMailinglistWidget.java,v $
 * Date   : $Date: 2007/11/30 11:57:27 $
 * Version: $Revision: 1.6 $
 *
 * This file is part of the Alkacon OpenCms Add-On Module Package
 *
 * Copyright (c) 2007 Alkacon Software GmbH (http://www.alkacon.com)
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

package com.alkacon.opencms.v8.newsletter;

import org.opencms.file.CmsObject;
import org.opencms.main.OpenCms;
import org.opencms.widgets.CmsGroupWidget;
import org.opencms.widgets.I_CmsWidget;
import org.opencms.widgets.I_CmsWidgetDialog;
import org.opencms.widgets.I_CmsWidgetParameter;
import org.opencms.workplace.CmsWorkplace;

/**
 * Provides a OpenCms mailing list selection widget, for use on a widget dialog.<p>
 *
 * @author Andreas Zahner  
 * 
 * @version $Revision: 1.6 $ 
 * 
 * @since 7.0.3 
 */
public class CmsMailinglistWidget extends CmsGroupWidget {

    /**
     * Creates a new group selection widget.<p>
     */
    public CmsMailinglistWidget() {

        // empty constructor is required for class registration
        this("");
    }

    /**
     * Creates a new group selection widget with the parameters to configure the popup window behaviour.<p>
     * 
     * @param flags the group flags to restrict the group selection, can be <code>null</code>
     * @param userName the user to restrict the group selection, can be <code>null</code>
     */
    public CmsMailinglistWidget(Integer flags, String userName) {

        super(flags, userName);
    }

    /**
     * Creates a new group selection widget with the parameters to configure the popup window behaviour.<p>
     * 
     * @param flags the group flags to restrict the group selection, can be <code>null</code>
     * @param userName the user to restrict the group selection, can be <code>null</code>
     * @param ouFqn the organizational unit to restrict the group selection, can be <code>null</code>
     */
    public CmsMailinglistWidget(Integer flags, String userName, String ouFqn) {

        super(flags, userName, ouFqn);
    }

    /**
     * Creates a new group selection widget with the given configuration.<p>
     * 
     * @param configuration the configuration to use
     */
    public CmsMailinglistWidget(String configuration) {

        super(configuration);
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#getDialogWidget(org.opencms.file.CmsObject, org.opencms.widgets.I_CmsWidgetDialog, org.opencms.widgets.I_CmsWidgetParameter)
     */
    public String getDialogWidget(CmsObject cms, I_CmsWidgetDialog widgetDialog, I_CmsWidgetParameter param) {

        String id = param.getId();
        StringBuffer result = new StringBuffer(128);

        result.append("<td class=\"xmlTd\">");
        result.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"maxwidth\"><tr><td style=\"width: 100%;\">");
        result.append("<input style=\"width: 99%;\" class=\"xmlInput");
        if (param.hasError()) {
            result.append(" xmlInputError");
        }
        result.append("\" value=\"");
        result.append(param.getStringValue(cms));
        result.append("\" name=\"");
        result.append(id);
        result.append("\" id=\"");
        result.append(id);
        result.append("\"></td>");
        result.append(widgetDialog.dialogHorizontalSpacer(10));
        result.append("<td><table class=\"editorbuttonbackground\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr>");

        StringBuffer buttonJs = new StringBuffer(8);
        buttonJs.append("javascript:openGroupWin('");
        buttonJs.append(OpenCms.getSystemInfo().getOpenCmsContext());
        buttonJs.append(CmsWorkplace.VFS_PATH_MODULES);
        buttonJs.append(CmsNewsletterManager.MODULE_NAME);
        buttonJs.append("/widgets/mailinglist.jsp");
        buttonJs.append("','EDITOR',  '");
        buttonJs.append(id);
        buttonJs.append("', document, ");
        if (getFlags() != null) {
            buttonJs.append("'");
            buttonJs.append(getFlags());
            buttonJs.append("'");
        } else {
            buttonJs.append("null");
        }
        buttonJs.append(", ");
        if (getUserName() != null) {
            buttonJs.append("'");
            buttonJs.append(getUserName());
            buttonJs.append("'");
        } else {
            buttonJs.append("null");
        }
        buttonJs.append(", ");
        if (getOufqn() != null) {
            buttonJs.append("'");
            buttonJs.append(getOufqn());
            buttonJs.append("'");
        } else {
            buttonJs.append("null");
        }
        buttonJs.append(");");

        result.append(widgetDialog.button(
            buttonJs.toString(),
            null,
            "mailinglist",
            org.opencms.workplace.Messages.GUI_DIALOG_BUTTON_SEARCH_0,
            widgetDialog.getButtonStyle()));
        result.append("</tr></table>");
        result.append("</td></tr></table>");

        result.append("</td>");

        return result.toString();
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#newInstance()
     */
    public I_CmsWidget newInstance() {

        return new CmsMailinglistWidget(getConfiguration());
    }
}