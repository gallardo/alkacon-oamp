/*
 * File   : $Source: /alkacon/cvs/alkacon/com.alkacon.opencms.v8.newsletter/src/com/alkacon/opencms/v8/newsletter/admin/CmsMailinglistDeleteDialog.java,v $
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

package com.alkacon.opencms.v8.newsletter.admin;

import org.opencms.file.CmsObject;
import org.opencms.file.CmsUser;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsException;
import org.opencms.main.CmsRuntimeException;
import org.opencms.util.CmsStringUtil;
import org.opencms.workplace.list.CmsHtmlList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * Mailing list delete dialog.<p>
 * 
 * @author Michael Moossen
 * @author Andreas Zahner
 * 
 * @version $Revision: 1.6 $ 
 * 
 * @since 7.0.3 
 */
public class CmsMailinglistDeleteDialog extends org.opencms.workplace.tools.accounts.CmsGroupDependenciesList {

    /**
     * Public constructor.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsMailinglistDeleteDialog(CmsJspActionElement jsp) {

        super(LIST_ID + "l", jsp);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsMailinglistDeleteDialog(PageContext context, HttpServletRequest req, HttpServletResponse res) {

        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#actionDialog()
     */
    public void actionDialog() throws JspException, ServletException, IOException {

        switch (getAction()) {
            case ACTION_DELETE:
                Iterator it = CmsStringUtil.splitAsList(getGroupName(), CmsHtmlList.ITEM_SEPARATOR, true).iterator();
                CmsObject cms = getCms();
                while (it.hasNext()) {
                    String name = (String)it.next();
                    try {
                        List users = cms.getUsersOfGroup(name);
                        cms.deleteGroup(name);
                        Iterator itUsers = users.iterator();
                        while (itUsers.hasNext()) {
                            CmsUser user = (CmsUser)itUsers.next();
                            if (cms.getGroupsOfUser(user.getName(), true).isEmpty()) {
                                cms.deleteUser(user.getId());
                            }
                        }
                    } catch (CmsException e) {
                        throw new CmsRuntimeException(e.getMessageContainer(), e);
                    }
                }
                setAction(ACTION_CANCEL);
                actionCloseDialog();
                break;
            default:
                super.actionDialog();
        }
    }

    /**
     * @see org.opencms.workplace.tools.accounts.CmsGroupDependenciesList#customHtmlStart()
     */
    protected String customHtmlStart() {

        StringBuffer result = new StringBuffer(512);
        result.append(dialogBlockStart(org.opencms.workplace.tools.accounts.Messages.get().container(
            org.opencms.workplace.tools.accounts.Messages.GUI_GROUP_DEPENDENCIES_NOTICE_0).key(getLocale())));
        result.append(key(Messages.GUI_MAILINGLIST_DELETE_0));
        result.append(dialogBlockEnd());
        return result.toString();
    }

    /**
     * @see org.opencms.workplace.tools.accounts.CmsGroupDependenciesList#getListItems()
     */
    protected List getListItems() {

        // the list should never appear
        return new ArrayList();
    }

    /**
     * @see org.opencms.workplace.CmsWorkplace#initMessages()
     */
    protected void initMessages() {

        // add specific dialog resource bundle
        addMessages(Messages.get().getBundleName());
        // add default resource bundles
        super.initMessages();
    }
}
