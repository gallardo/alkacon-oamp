/*
 * File   : $Source: /alkacon/cvs/alkacon/com.alkacon.opencms.v8.newsletter/src/com/alkacon/opencms/v8/newsletter/admin/CmsNewsletterListSendToOu.java,v $
 * Date   : $Date: 2010/10/14 13:17:50 $
 * Version: $Revision: 1.2 $
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

import com.alkacon.opencms.v8.newsletter.CmsNewsletterMail;
import com.alkacon.opencms.v8.newsletter.CmsNewsletterManager;
import com.alkacon.opencms.v8.newsletter.I_CmsNewsletterMailData;

import org.opencms.db.CmsUserSettings;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;
import org.opencms.security.CmsOrganizationalUnit;
import org.opencms.workplace.explorer.CmsResourceUtil;
import org.opencms.workplace.list.A_CmsListExplorerDialog;
import org.opencms.workplace.list.CmsListColumnAlignEnum;
import org.opencms.workplace.list.CmsListColumnDefinition;
import org.opencms.workplace.list.CmsListDirectAction;
import org.opencms.workplace.list.CmsListExplorerColumn;
import org.opencms.workplace.list.CmsListMetadata;
import org.opencms.workplace.list.CmsListOrderEnum;
import org.opencms.workplace.list.I_CmsListResourceCollector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;

/**
 * Newsletter list to select which newsletter is sent to the current OU.<p>
 * 
 * @author Andreas Zahner  
 * 
 * @version $Revision $ 
 * 
 * @since 7.0.3  
 */
public class CmsNewsletterListSendToOu extends A_CmsListExplorerDialog {

    /** List column id constant. */
    public static final String LIST_ACTION_SEND = "ease";

    /** List column id constant. */
    public static final String LIST_COLUMN_DATA = "ecda";

    /** List column id constant. */
    public static final String LIST_COLUMN_SCORE = "cs";

    /** List column id constant. */
    public static final String LIST_COLUMN_SEND = "ecse";

    /** list id constant. */
    public static final String LIST_ID = "anlsou";

    /** Path to the list buttons. */
    public static final String PATH_BUTTONS = "tools/v8-newsletter/buttons/";

    /** The internal collector instance. */
    private I_CmsListResourceCollector m_collector;

    /** Stores the value of the request parameter for the organizational unit fqn. */
    private String m_paramOufqn;

    /**
     * Public constructor with JSP action element.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsNewsletterListSendToOu(CmsJspActionElement jsp) {

        super(
            jsp,
            LIST_ID,
            Messages.get().container(Messages.GUI_NEWSLETTER_LIST_NAME_0),
            A_CmsListExplorerDialog.LIST_COLUMN_NAME,
            CmsListOrderEnum.ORDER_ASCENDING,
            null);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsNewsletterListSendToOu(PageContext context, HttpServletRequest req, HttpServletResponse res) {

        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#executeListMultiActions()
     */
    @Override
    public void executeListMultiActions() {

        throwListUnsupportedActionException();
    }

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsNewsletterListSendToOu.class);

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#executeListSingleActions()
     */
    @Override
    public void executeListSingleActions() {

        if (getParamListAction().equals(LIST_ACTION_SEND)) {
            // send the newsletter to the selected list
            String resourceName = (String)getSelectedItem().get(LIST_COLUMN_ROOT_PATH);
            try {
                // generate the newsletter mail and list of recipients
                CmsOrganizationalUnit ou = OpenCms.getOrgUnitManager().readOrganizationalUnit(getCms(), getParamOufqn());
                I_CmsNewsletterMailData mailData = CmsNewsletterManager.getMailData(getJsp(), ou, resourceName);
                String rootPath = resourceName;
                if (mailData.getContent() != null) {
                    rootPath = mailData.getContent().getFile().getRootPath();
                }
                if (mailData.isSendable()) {
                    //send the emails to the mailing list group
                    CmsNewsletterMail nlMail = new CmsNewsletterMail(
                        mailData,
                        mailData.getRecipients(),
                        getCms().getRequestContext().currentUser().getEmail(),
                        rootPath);
                    nlMail.start();
                    getList().clear();
                }
            } catch (Exception e) {
                // should never happen
                if (LOG.isErrorEnabled()) {
                    LOG.error(Messages.get().container(Messages.LOG_NEWSLETTER_SEND_FAILED_0), e);
                }
                throwListUnsupportedActionException();
            }
        } else {
            throwListUnsupportedActionException();
        }
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListExplorerDialog#getCollector()
     */
    @Override
    public I_CmsListResourceCollector getCollector() {

        if (m_collector == null) {
            m_collector = new CmsNewsletterResourcesCollector(this);
            // set the right resource util parameters
            CmsResourceUtil resUtil = getResourceUtil();
            resUtil.setAbbrevLength(50);
            resUtil.setSiteMode(CmsResourceUtil.SITE_MODE_MATCHING);
        }
        return m_collector;
    }

    /**
     * Returns the organizational unit fqn parameter value.<p>
     * 
     * @return the organizational unit fqn parameter value
     */
    public String getParamOufqn() {

        return m_paramOufqn;
    }

    /**
     * Sets the organizational unit fqn parameter value.<p>
     * 
     * @param ouFqn the organizational unit fqn parameter value
     */
    public void setParamOufqn(String ouFqn) {

        if (ouFqn == null) {
            ouFqn = "";
        }
        m_paramOufqn = ouFqn;
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#fillDetails(java.lang.String)
     */
    @Override
    protected void fillDetails(String detailId) {

        // no details
    }

    /**
     * @see org.opencms.workplace.CmsWorkplace#initMessages()
     */
    @Override
    protected void initMessages() {

        // add specific dialog resource bundle
        addMessages(Messages.get().getBundleName());
        // add default resource bundles
        super.initMessages();
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setColumns(org.opencms.workplace.list.CmsListMetadata)
     */
    @Override
    protected void setColumns(CmsListMetadata metadata) {

        super.setColumns(metadata);

        // add column with send icon
        CmsListColumnDefinition sendIconCol = new CmsListColumnDefinition(LIST_COLUMN_SEND);
        sendIconCol.setName(Messages.get().container(Messages.GUI_NEWSLETTER_LIST_COLS_SEND_0));
        sendIconCol.setHelpText(Messages.get().container(Messages.GUI_NEWSLETTER_LIST_COLS_SEND_OU_HELP_0));
        sendIconCol.setWidth("20");
        sendIconCol.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);

        // add enabled send action
        CmsListDirectAction sendAction = new CmsListSendNewsletterAction(LIST_ACTION_SEND, LIST_COLUMN_ROOT_PATH);
        //sendAction.setName(Messages.get().container(Messages.GUI_NEWSLETTER_LIST_ACTION_SEND_0));
        sendAction.setHelpText(Messages.get().container(Messages.GUI_NEWSLETTER_LIST_ACTION_SEND_OU_HELP_0));
        sendAction.setEnabled(true);
        //sendAction.setIconPath("tools/v8-newsletter/buttons/v8-newsletter_send.png");
        sendAction.setConfirmationMessage(Messages.get().container(Messages.GUI_NEWSLETTER_LIST_ACTION_SEND_CONF_0));
        sendIconCol.addDirectAction(sendAction);

        CmsListDirectAction nosendAction = new CmsListSendNewsletterAction(
            LIST_ACTION_SEND + "d",
            LIST_COLUMN_ROOT_PATH);
        nosendAction.setEnabled(false);
        sendIconCol.addDirectAction(nosendAction);

        metadata.addColumn(sendIconCol, 0);

        // add column with information about the send process
        CmsListColumnDefinition newsletterCol = new CmsListExplorerColumn(LIST_COLUMN_DATA);
        newsletterCol.setName(Messages.get().container(Messages.GUI_NEWSLETTER_LIST_COLS_DATA_0));
        newsletterCol.setHelpText(Messages.get().container(Messages.GUI_NEWSLETTER_LIST_COLS_DATA_HELP_0));
        newsletterCol.setVisible(true);
        newsletterCol.setSorteable(false);
        metadata.addColumn(newsletterCol);
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListExplorerDialog#setColumnVisibilities()
     */
    @Override
    protected void setColumnVisibilities() {

        super.setColumnVisibilities();
        setColumnVisibility(LIST_COLUMN_EDIT.hashCode(), LIST_COLUMN_EDIT.hashCode());

        // set visibility of some columns to false, they are not required for the newsletter send list
        setColumnVisibility(CmsUserSettings.FILELIST_TYPE, 0);
        setColumnVisibility(CmsUserSettings.FILELIST_TYPE, 0);
        setColumnVisibility(CmsUserSettings.FILELIST_SIZE, 0);
        setColumnVisibility(CmsUserSettings.FILELIST_PERMISSIONS, 0);
        setColumnVisibility(CmsUserSettings.FILELIST_DATE_CREATED, 0);
        setColumnVisibility(CmsUserSettings.FILELIST_USER_CREATED, 0);
        setColumnVisibility(CmsUserSettings.FILELIST_STATE, 0);
        setColumnVisibility(CmsUserSettings.FILELIST_LOCKEDBY, 0);
        setColumnVisibility(CmsUserSettings.FILELIST_DATE_LASTMODIFIED, 0);
        setColumnVisibility(CmsUserSettings.FILELIST_DATE_RELEASED, 0);
        setColumnVisibility(CmsUserSettings.FILELIST_DATE_EXPIRED, 0);

        // set the visibility of standard columns to false
        setColumnVisibility(LIST_COLUMN_EDIT.hashCode(), 0);
        setColumnVisibility(LIST_COLUMN_TYPEICON.hashCode(), 0);
        setColumnVisibility(LIST_COLUMN_LOCKICON.hashCode(), 0);
        setColumnVisibility(LIST_COLUMN_PROJSTATEICON.hashCode(), 0);
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListExplorerDialog#setIndependentActions(org.opencms.workplace.list.CmsListMetadata)
     */
    @Override
    protected void setIndependentActions(CmsListMetadata metadata) {

        // no LIAs
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setMultiActions(org.opencms.workplace.list.CmsListMetadata)
     */
    @Override
    protected void setMultiActions(CmsListMetadata metadata) {

        // no LMAs
    }
}
