/*
 * File   : $Source: /alkacon/cvs/alkacon/com.alkacon.opencms.v8.formgenerator/src/com/alkacon/opencms/v8/formgenerator/database/export/CmsCvsExportBean.java,v $
 * Date   : $Date: 2011/03/24 16:33:49 $
 * Version: $Revision: 1.11 $
 *
 * This file is part of the Alkacon OpenCms Add-On Module Package
 *
 * Copyright (c) 2010 Alkacon Software GmbH (http://www.alkacon.com)
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

package com.alkacon.opencms.v8.formgenerator.database.export;

import com.alkacon.opencms.v8.formgenerator.CmsForm;
import com.alkacon.opencms.v8.formgenerator.CmsFormHandler;
import com.alkacon.opencms.v8.formgenerator.database.CmsFormDataAccess;
import com.alkacon.opencms.v8.formgenerator.database.CmsFormDataBean;

import org.opencms.file.CmsObject;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;
import org.opencms.module.CmsModule;
import org.opencms.util.CmsStringUtil;
import org.opencms.util.CmsUUID;

import java.sql.SQLException;
import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;

/**
 * Bean that supports the data export.<p>
 * 
 * Works together with 
 * /system/modules/com.alkacon.opencms.v8.formgenerator/elements/datadownload.jsp.<p>
 * 
 * @author Achim Westermann
 * 
 * @version $Revision: 1.11 $
 * 
 * @since 7.0.4
 *
 */
public class CmsCvsExportBean {

    /** The default value delimiter for CSV files in Excel. */
    public static final String EXCEL_DEFAULT_CSV_DELMITER = ",";

    /** Request parameter for the start time of the data to export. */
    public static final String PARAM_EXPORT_DATA_TIME_END = "endtime";

    /** Request parameter for the start time of the data to export. */
    public static final String PARAM_EXPORT_DATA_TIME_START = "starttime";

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsCvsExportBean.class);

    /** The end time for data sets to export. */
    private Date m_endTime;

    /** The form that was used to input the data to export. */
    private CmsFormHandler m_formHandler;

    /** Needed to read the resource for the UUID. */
    private CmsObject m_cms;

    /** The start time for data sets to export. */
    private Date m_startTime;

    /**
     * Creates an instance based upon data that was entered with the given form.<p> 
     * 
     * @param formHandler the origin of the data 
     */
    public CmsCvsExportBean(final CmsFormHandler formHandler) {

        this(formHandler.getCmsObject());
        m_formHandler = formHandler;
    }

    /**
     * Creates an instance that is not backed by a form but still offers 
     * export functionality via <code>{@link #exportData(String, Locale)}</code>.<p>
     * 
     * @param cms Needed to read the resource for the UUID
     */
    public CmsCvsExportBean(final CmsObject cms) {

        m_cms = cms;
        m_startTime = new Date(0);
        m_endTime = new Date(Long.MAX_VALUE);
    }

    /**
     * Returns the csv export file content.<p> 
     * 
     * @return the csv export file content
     * 
     * @throws SQLException if sth goes wrong 
     */
    public String exportData() throws SQLException {

        return exportData(getForm().getFormConfiguration().getFormId(), getForm().getRequestContext().getLocale());
    }

    /**
     * Returns the CSV export file content.<p> 
     * 
     * @param formId the current selected web form
     * @param locale the current local
     * 
     * @return the CSV export file content
     * 
     * @throws SQLException if something goes wrong 
     */
    public String exportData(String formId, Locale locale) throws SQLException {

        /*
         * TODO: Access the CmsForm (or CmsFormHandler) and put out all 
         * fields in the exact order - put fields that do not exist any longer 
         * to the back (note: readAllFormFieldNames is required for the old values). 
         */

        StringBuffer result = new StringBuffer();
        CmsModule module = OpenCms.getModuleManager().getModule(CmsForm.MODULE_NAME);

        // time format: 
        DateFormat df = null;
        String formatString = module.getParameter(CmsForm.MODULE_PARAM_EXPORT_TIMEFORMAT);
        if (CmsStringUtil.isNotEmpty(formatString)) {
            try {
                df = new SimpleDateFormat(formatString);
            } catch (IllegalArgumentException iae) {
                LOG.warn(Messages.get().getBundle().key(
                    Messages.LOG_WARN_EXPORT_DATEFORMAT_ILLEGAL_2,
                    new Object[] {CmsForm.MODULE_PARAM_EXPORT_TIMEFORMAT, formatString}));
            }
        }

        // line separator: 
        boolean isWindowsLineSeparator = false;
        boolean isUnixLineSeparator = false;

        String lineSeparatorParam = module.getParameter(CmsForm.MODULE_PARAM_EXPORTLINESEPARATOR);
        if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(lineSeparatorParam)) {
            if (lineSeparatorParam.equals(CmsForm.MODULE_PARAMVALUE_EXPORTLINESEPARATOR_WINDOWS)) {
                isWindowsLineSeparator = true;
            } else if (lineSeparatorParam.equals(CmsForm.MODULE_PARAMVALUE_EXPORTLINESEPARATOR_UNIX)
                || lineSeparatorParam.equals(CmsForm.MODULE_PARAMVALUE_EXPORTLINESEPARATOR_EXCEL)) {
                isUnixLineSeparator = true;
            }
        }

        // export numbers as string:
        String nasParam = module.getParameter(CmsForm.MODULE_PARAM_EXPORT_NUMBERASSTRING, CmsStringUtil.FALSE);
        boolean numberAsString = Boolean.valueOf(nasParam).booleanValue();

        // csv delimiter
        String lineSeparator = EXCEL_DEFAULT_CSV_DELMITER;
        String configLineSeparator = module.getParameter(CmsForm.MODULE_PARAM_CSV_DELIMITER);
        if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(configLineSeparator)) {

            lineSeparator = configLineSeparator.trim();
        }

        // get the column names
        List<String> columnNames = CmsFormDataAccess.getInstance().readFormFieldNames(
            formId,
            getStartTime().getTime(),
            getEndTime().getTime());
        Collections.sort(columnNames, Collator.getInstance(locale));

        // get the entries
        List<CmsFormDataBean> dataEntries = CmsFormDataAccess.getInstance().readForms(
            formId,
            getStartTime().getTime(),
            getEndTime().getTime());

        // loop 1 - write the headers:
        result.append(escapeExcelCsv("Creation date", numberAsString));
        result.append(lineSeparator);
        result.append(escapeExcelCsv("Resource path", numberAsString));
        result.append(lineSeparator);
        result.append(escapeExcelCsv("Resource UUID", numberAsString));
        result.append(lineSeparator);
        Iterator<String> itColumns = columnNames.iterator();
        while (itColumns.hasNext()) {
            String columnName = itColumns.next();
            // skip empty columns (previous versions saved CmsEmptyField with empty values which will not be deleted):
            if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(columnName)) {
                columnName = escapeExcelCsv(columnName, numberAsString);
                result.append(columnName);
                if (itColumns.hasNext()) {
                    result.append(lineSeparator);
                }
            }
        }
        result.append("\r\n");
        // loop 2 - write the data:
        Iterator<CmsFormDataBean> itRows = dataEntries.iterator();
        String path;
        CmsUUID uuid = null;
        while (itRows.hasNext()) {
            CmsFormDataBean row = itRows.next();
            // create an entry for each column, even if some rows (data sets) 
            // do not contain the field value because it was 
            // a) not entered 
            // b) the form was changed in structure over time 
            // c) developer errors,  hw /sw problems... 
            Date creationDate = new Date(row.getDateCreated());
            if (df == null) {
                result.append(creationDate);
            } else {
                result.append(df.format(creationDate));
            }
            DateFormat.getDateTimeInstance();
            result.append(lineSeparator);
            uuid = row.getResourceId();
            try {
                path = m_cms.readResource(uuid).getRootPath();
            } catch (Exception e) {
                path = row.getResourceId().toString();
            }
            result.append(path);
            result.append(lineSeparator);
            result.append(String.valueOf(uuid));
            result.append(lineSeparator);
            itColumns = columnNames.iterator();
            while (itColumns.hasNext()) {
                String columnName = itColumns.next();
                // skip empty columns (previous versions saved CmsEmptyField with empty values which will not be deleted):
                if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(columnName)) {

                    String value = row.getFieldValue(columnName);
                    if (value != null) {
                        if (isWindowsLineSeparator) {
                            value = transformWindowsLineseparator(value);
                        } else if (isUnixLineSeparator) {
                            value = transformUnixLineseparator(value);
                        }
                        value = escapeExcelCsv(value, numberAsString);
                        result.append(value);
                    }
                    if (itColumns.hasNext()) {
                        result.append(lineSeparator);
                    }
                }
            }
            result.append("\r\n");
        }
        return result.toString();
    }

    /**
     * Returns the endTime.<p>
     *
     * @return the endTime
     */
    public Date getEndTime() {

        return m_endTime;
    }

    /**
     * Returns the form handler.<p>
     *
     * @return the form handler
     */
    public CmsFormHandler getForm() {

        return m_formHandler;
    }

    /**
     * Returns the startTime.<p>
     *
     * @return the startTime
     */
    public Date getStartTime() {

        return m_startTime;
    }

    /**
     * Sets the endTime.<p>
     *
     * @param endTime the endTime to set
     */
    public void setEndTime(Date endTime) {

        m_endTime = endTime;
    }

    /**
     * Sets the startTime.<p>
     *
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {

        m_startTime = startTime;
    }

    /** 
     * Escapes CSV values for Excel.<p> 
     * 
     * @param value the value to escape 
     * @param numberAsString flag to determine if numbers should be marked as Strings by prepending a <code>=</code>
     * 
     * @return the escaped Excel value
     */
    private String escapeExcelCsv(final String value, boolean numberAsString) {

        String result = value;
        StringBuffer buffer = new StringBuffer(value.length() + 8);
        // support for Microsoft Excel: If Excel detects numbers, it reformats the numbers 
        // (stealing leading zeros or displaying large numbers in +E syntax:
        if (numberAsString) {
            boolean isNumber = false;
            try {
                Double.valueOf(result);
                isNumber = true;
            } catch (Exception e) {
                // this is no double value
            }
            if (!isNumber) {
                try {
                    Long.valueOf(result);
                    isNumber = true;
                } catch (Exception e) {
                    // this is no long value
                }
            }

            if (isNumber) {
                buffer.append("=");
            }
        }
        buffer.append("\"");
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            // escape double quote escape delimiter within value: 
            if ('"' == chars[i]) {
                buffer.append("\"");
            }
            buffer.append(chars[i]);
        }
        buffer.append("\"");
        result = buffer.toString();
        return result;
    }

    /**
     * Replaces all "\r\n" to "\n".<p>
     * 
     * @param value the value to transform 
     * 
     * @return the input with Unix line separators
     */
    private String transformUnixLineseparator(String value) {

        return value.replaceAll("\r\n", "\n");
    }

    /**
     * Replaces all "\n" to "\r\n".<p>
     * 
     * @param value the value to transform 
     * 
     * @return the input with windows line separators
     */
    private String transformWindowsLineseparator(String value) {

        return value.replaceAll("\n", "\r\n");

    }

}
