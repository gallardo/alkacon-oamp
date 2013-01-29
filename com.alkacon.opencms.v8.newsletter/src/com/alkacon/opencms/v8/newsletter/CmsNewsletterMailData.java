/*
 * File   : $Source: /alkacon/cvs/alkacon/com.alkacon.opencms.v8.newsletter/src/com/alkacon/opencms/v8/newsletter/CmsNewsletterMailData.java,v $
 * Date   : $Date: 2010/10/14 13:17:49 $
 * Version: $Revision: 1.10 $
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

import org.opencms.file.CmsFile;
import org.opencms.file.CmsGroup;
import org.opencms.file.CmsResource;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.mail.CmsHtmlMail;
import org.opencms.mail.CmsSimpleMail;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.util.CmsHtmlExtractor;
import org.opencms.util.CmsStringUtil;
import org.opencms.xml.content.CmsXmlContent;
import org.opencms.xml.content.CmsXmlContentFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

/**
 * Generates newsletter emails and the list of recipients from a newsletter structured content VFS file.<p>
 * 
 * Provides some utility methods to generate email previews and get the email contents as string.<p>
 *  
 * @author Andreas Zahner  
 * 
 * @version $Revision: 1.10 $ 
 * 
 * @since 7.0.3 
 */
public class CmsNewsletterMailData extends A_CmsNewsletterMailData {

    /** Resource type name of a newsletter resource. */
    public static final String RESOURCETYPE_NEWSLETTER_NAME = "alkacon-v8-newsletter";

    /** The node name for the ConfFile node. */
    protected static final String NODE_CONFFILE = "ConfFile";

    /** The node name for the HTML node. */
    protected static final String NODE_HTML = "Html";

    /** The node name for the HTML Only node. */
    protected static final String NODE_HTML_ONLY = "HtmlOnly";

    /** The node name for the MailFoot node. */
    protected static final String NODE_MAILFOOT = "MailFoot";

    /** The node name for the MailHead node. */
    protected static final String NODE_MAILHEAD = "MailHead";

    /** The node name for the Text node. */
    protected static final String NODE_TEXT = "Text";

    /** The xpath for the Config node including trailing "/". */
    protected static final String XPATH_CONFIG = "Config/";

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsNewsletterMailData.class);

    /** The email HTML text. */
    private String m_html;

    /** Indicates if the mail is a HTML or text mail. */
    private Boolean m_htmlMail;

    private Boolean m_htmlOnly;

    /** The email plain text. */
    private String m_text;

    /**
     * Empty constructor.<p>
     * 
     * Be sure to call {@link #initialize(CmsJspActionElement, CmsGroup, String)} to get correct results.<p>
     */
    public CmsNewsletterMailData() {

        // noop
    }

    /**
     * Constructor, with parameters.<p>
     * 
     * @param fileName the fileName of the newsletter
     * @param group the group to send the newsletter to
     * @param jsp the JSP action element
     * @throws CmsException if reading or unmarshalling the file fails
     */
    public CmsNewsletterMailData(String fileName, CmsGroup group, CmsJspActionElement jsp)
    throws CmsException {

        initialize(jsp, group, fileName);
    }

    /**
     * Returns the mail to send as newsletter, with set subject, text and from address.<p>
     * 
     * @return the mail to send as newsletter
     * @throws CmsException if generating the email content fails
     */
    @Override
    public Email getEmail() throws CmsException {

        // get the email data from the content fields
        String from = getFrom();
        String fromName = getFromName();
        String subject = getSubject();

        if (isHtmlMail()) {
            // create and send HTML email
            CmsHtmlMail mail = new CmsHtmlMail();
            try {
                if (CmsStringUtil.isEmptyOrWhitespaceOnly(fromName)) {
                    mail.setFrom(from);
                } else {
                    mail.setFrom(from, fromName);
                }
            } catch (EmailException e) {
                // log invalid from email address
                if (LOG.isErrorEnabled()) {
                    LOG.error(Messages.get().getBundle().key(
                        Messages.LOG_ERROR_NEWSLETTER_EMAIL_FROM_2,
                        from,
                        getContent().getFile().getRootPath()));
                }
            }

            mail.setSubject(subject);
            try {
                // create the email content and use it as HTML message
                mail.setHtmlMsg(getHtml());
                // check if HTML only mail should be sent by evaluating the optional element
                if (!isHtmlOnly()) {
                    mail.setTextMsg(getText());
                }
            } catch (EmailException e) {
                // ignore, error creating email texts
            }
            // set the mail encoding
            mail.setCharset(getEncoding());
            return mail;
        } else {
            // create and send text only email
            CmsSimpleMail mail = new CmsSimpleMail();
            try {
                if (CmsStringUtil.isEmptyOrWhitespaceOnly(fromName)) {
                    mail.setFrom(from);
                } else {
                    mail.setFrom(from, fromName);
                }
            } catch (EmailException e) {
                // log invalid from email address
                if (LOG.isErrorEnabled()) {
                    LOG.error(Messages.get().getBundle().key(
                        Messages.LOG_ERROR_NEWSLETTER_EMAIL_FROM_2,
                        from,
                        getContent().getFile().getRootPath()));
                }
            }
            mail.setSubject(subject);
            // extract the text from the HTML field
            try {
                mail.setMsg(getText());
            } catch (Exception e) {
                // cleaning text failed or setting text failed
            }

            // set the mail encoding
            mail.setCharset(getEncoding());
            return mail;
        }
    }

    /**
     * @see com.alkacon.opencms.v8.newsletter.I_CmsNewsletterMailData#getEmailContentPreview(boolean)
     */
    @Override
    public String getEmailContentPreview(boolean onlyPartialHtml) throws CmsException {

        boolean isHtmlMail = Boolean.valueOf(
            getContent().getStringValue(getCms(), XPATH_CONFIG + NODE_HTML, getLocale())).booleanValue();
        String result = getEmailContent(isHtmlMail, onlyPartialHtml);
        if ((result.indexOf("</body>") == -1) && !onlyPartialHtml) {
            StringBuffer previewHtml = new StringBuffer(result.length() + 256);
            previewHtml.append("<html><head></head><body style=\"background-color: #FFF;\">\n<pre style=\"font-family: Courier New, monospace; font-size: 13px; color: #000;\">");
            previewHtml.append(result);
            previewHtml.append("</pre>\n</body></html>");
            result = previewHtml.toString();
        }
        return result;
    }

    /**
     * @see com.alkacon.opencms.v8.newsletter.I_CmsNewsletterMailData#getResourceTypeName()
     */
    @Override
    public String getResourceTypeName() {

        return RESOURCETYPE_NEWSLETTER_NAME;
    }

    /**
     * Returns the email content from the newsletter VFS file.<p>
     * 
     * @param isHtmlMail flag to determine if HTML or plain text content should be generated
     * @param onlyPartialHtml sets if only page parts should be returned instead of a complete HTML page
     * 
     * @return the email content
     * 
     * @throws CmsException if reading or unmarshalling the file fails
     */
    protected String getEmailContent(boolean isHtmlMail, boolean onlyPartialHtml) throws CmsException {

        String text = getContent().getStringValue(getCms(), NODE_TEXT, getLocale());
        if (isHtmlMail) {
            // create the content of the HTML mail
            StringBuffer mailHtml = new StringBuffer(4096);
            String mailHead = "";
            String mailFoot = "";
            if (!onlyPartialHtml) {
                // get mail head and foot HTML if complete HTML page content should be generated
                boolean foundExternalConfig = false;
                if (getContent().hasValue(XPATH_CONFIG + NODE_CONFFILE, getLocale())) {
                    // optional external configuration file specified, use this as mail configuration
                    String path = getContent().getStringValue(getCms(), XPATH_CONFIG + NODE_CONFFILE, getLocale());
                    if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(path)
                        && getCms().existsResource(path)
                        && !CmsResource.isFolder(path)) {
                        CmsFile mailConfig = getCms().readFile(path);
                        CmsXmlContent mailContent = CmsXmlContentFactory.unmarshal(getCms(), mailConfig);
                        // get the mail head and foot from the external configuration file content
                        if (mailContent.hasValue(NODE_MAILHEAD, getLocale())) {
                            mailHead = mailContent.getStringValue(getCms(), NODE_MAILHEAD, getLocale());
                            mailFoot = mailContent.getStringValue(getCms(), NODE_MAILFOOT, getLocale());
                            foundExternalConfig = true;
                        }
                    }
                }
                if (!foundExternalConfig) {
                    // no external configuration specified, use internal configuration values
                    mailHead = getContent().getStringValue(getCms(), XPATH_CONFIG + NODE_MAILHEAD, getLocale());
                    mailFoot = getContent().getStringValue(getCms(), XPATH_CONFIG + NODE_MAILFOOT, getLocale());
                }
            }
            mailHtml.append(mailHead);
            mailHtml.append(text);
            mailHtml.append(mailFoot);
            // resolve eventual macros in result
            return resolveMacros(mailHtml.toString());
        } else {
            // create the content of the text mail
            try {
                return CmsHtmlExtractor.extractText(text, getCms().getRequestContext().getEncoding());
            } catch (Exception e) {
                // error extracting text, return unmodified text                
                return text;
            }
        }
    }

    /**
     * @see com.alkacon.opencms.v8.newsletter.A_CmsNewsletterMailData#getHtml()
     */
    @Override
    protected String getHtml() throws CmsException {

        if (m_html == null) {
            m_html = getEmailContent(true, false);
        }
        return m_html;
    }

    /**
     * @see com.alkacon.opencms.v8.newsletter.A_CmsNewsletterMailData#getText()
     */
    @Override
    protected String getText() throws CmsException {

        if (m_text == null) {
            m_text = getEmailContent(false, false);
        }
        return m_text;
    }

    /**
     * Returns if the mail is a HTML mail.<p>
     * 
     * @return true if the mail is a HTML mail, otherwise false
     */
    protected boolean isHtmlMail() {

        if (m_htmlMail == null) {
            m_htmlMail = Boolean.valueOf(getContent().getStringValue(getCms(), XPATH_CONFIG + NODE_HTML, getLocale()));
        }
        return m_htmlMail.booleanValue();
    }

    /**
     * Returns if the mail is a HTML only mail.<p>
     * 
     * @return true if the mail is a HTML only mail, otherwise false
     */
    protected boolean isHtmlOnly() {

        if (m_htmlOnly == null) {
            m_htmlOnly = Boolean.valueOf(getContent().getStringValue(
                getCms(),
                XPATH_CONFIG + NODE_HTML_ONLY,
                getLocale()));
        }
        return m_htmlOnly.booleanValue();
    }

}
