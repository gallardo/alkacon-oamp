/*
 * File   : $Source: /alkacon/cvs/alkacon/com.alkacon.opencms.v8.calendar/src/com/alkacon/opencms/v8/calendar/CmsCalendarEntryDateSerial.java,v $
 * Date   : $Date: 2008/04/25 14:50:41 $
 * Version: $Revision: 1.1 $
 *
 * This file is part of the Alkacon OpenCms Add-On Module Package
 *
 * Copyright (c) 2008 Alkacon Software GmbH (http://www.alkacon.com)
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

package com.alkacon.opencms.v8.calendar;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Stores the serial date information of a single calendar entry.<p>
 * 
 * @author Andreas Zahner
 * 
 * @version $Revision: 1.1 $ 
 * 
 * @since 6.0.1
 */
public class CmsCalendarEntryDateSerial extends CmsCalendarEntryDate {

    /** The occurrences of a series interval. */
    private int m_occurrences;

    /** The end date of a series. */
    private Calendar m_serialEndDate;

    /** The end type of a series. */
    private int m_serialEndType;

    /** The serial date options. */
    private I_CmsCalendarSerialDateOptions m_serialOptions;

    /**
     * Constructor that initializes the members using the given start and end date.<p>
     * 
     * @param startDate the start date of the entry
     * @param endDate the end date of the entry
     */
    public CmsCalendarEntryDateSerial(Calendar startDate, Calendar endDate) {

        super(startDate, endDate);
        m_serialEndType = -1;
    }

    /**
     * Constructor that directly initializes the members.<p>
     * 
     * @param startDay the start day of the entry
     * @param startTime the start time of the entry
     * @param endTime the end time of the entry
     * @param duration the duration of the entry (in days)
     */
    public CmsCalendarEntryDateSerial(long startDay, long startTime, long endTime, int duration) {

        super(startDay, startTime, endTime, duration);
        m_serialEndType = -1;
    }

    /**
     * Warning: This method currently breaks the {@link Object#clone() }
     * contract as it uses this class constructor to create the copy instead of
     * <tt>Object.clone()</tt>. <p>
     * <strong>Children of this class overriding this method mustn't invoke
     * <tt>super.clone()</tt>!!!</strong>
     * 
     * @return new instance of {@link CmsCalendarEntryDateSerial} (shallow
     *          + deep copy of this object!)
     */
    @Override
    public CmsCalendarEntryDateSerial clone() {

        CmsCalendarEntryDateSerial clone = new CmsCalendarEntryDateSerial(getStartDate(), getEndDate());
        clone.setSerialEndType(m_serialEndType);
        clone.setOccurrences(m_occurrences);
        clone.setSerialOptions(m_serialOptions);
        return clone;
    }

    /**
     * Returns the configuration values for the serial date as Map, including the individual serial options depending on the type.<p>
     * 
     * This Map can be used to store the configured options as property value on VFS resources.<p>
     * 
     * @return the configuration values for the serial date as Map
     */
    public Map<String, String> getConfigurationValuesAsMap() {

        // create the Map containing the date settings
        Map<String, String> values = new HashMap<String, String>();

        // first put the values of serial date fields used by all serial types

        // fetch the start date and time
        values.put(I_CmsCalendarSerialDateOptions.CONFIG_STARTDATE, String.valueOf(getStartDate().getTimeInMillis()));
        // the end date and time (this means the duration of a single entry)
        values.put(I_CmsCalendarSerialDateOptions.CONFIG_ENDDATE, String.valueOf(getEndDate().getTimeInMillis()));

        // store the serial type
        values.put(I_CmsCalendarSerialDateOptions.CONFIG_TYPE, String.valueOf(getSerialOptions().getSerialType()));

        // set the end type specific values
        int endType = getSerialEndType();
        values.put(I_CmsCalendarSerialDateOptions.CONFIG_END_TYPE, String.valueOf(endType));
        if (endType == I_CmsCalendarSerialDateOptions.END_TYPE_TIMES) {
            // end type: after a number of occurrences
            values.put(I_CmsCalendarSerialDateOptions.CONFIG_OCCURRENCES, String.valueOf(getOccurrences()));
        } else if (endType == I_CmsCalendarSerialDateOptions.END_TYPE_DATE) {
            // end type: ends at a specified date
            values.put(
                I_CmsCalendarSerialDateOptions.CONFIG_SERIAL_ENDDATE,
                String.valueOf(getSerialEndDate().getTimeInMillis()));
        }

        // now put the individual serial date options to the Map
        values.putAll(getSerialOptions().getConfigurationValuesAsMap());

        return values;
    }

    /**
     * Returns the occurrences of a defined series interval, used for the series end type.<p>
     *
     * @return the occurrences of a defined series interval, used for the series end type
     */
    public int getOccurrences() {

        return m_occurrences;
    }

    /**
     * Returns the serial end date if the series is of type: ending at specific date.<p>
     *
     * @return the serial end date if the series is of type: ending at specific date
     */
    public Calendar getSerialEndDate() {

        return m_serialEndDate;
    }

    /**
     * Returns the end type of the date series (never, n times, specific date).<p>
     * 
     * @return the end type of the date series
     */
    public int getSerialEndType() {

        return m_serialEndType;
    }

    /**
     * Returns the serial date options for the date series.<p>
     * 
     * @return the serial date options for the date series
     */
    public I_CmsCalendarSerialDateOptions getSerialOptions() {

        return m_serialOptions;
    }

    /**
     * Initializes the serial date options with the given values.<p>
     * 
     * @param endType the end type of the serial date
     * @param options the serial date options
     */
    public void initSerialDate(int endType, I_CmsCalendarSerialDateOptions options) {

        m_serialEndType = endType;
        m_serialOptions = options;
    }

    /**
     * Initializes the serial date options with the given values.<p>
     * 
     * @param endType the end type of the serial date
     * @param occurrences the number of occurrences for the serial type that defines n occurrences of the series
     * @param options the serial date options
     */
    public void initSerialDate(int endType, int occurrences, I_CmsCalendarSerialDateOptions options) {

        m_serialEndType = endType;
        m_occurrences = occurrences;
        m_serialOptions = options;
    }

    /**
     * Returns if the date entry is a serial date or not.<p>
     * 
     * @return true if the date entry is a serial date, otherwise false
     */
    @Override
    public boolean isSerialDate() {

        return ((m_serialEndType != -1) && (m_serialOptions != null));
    }

    /**
     * Returns the list of matching entries for the calendar view.<p>
     * 
     * @param entry the calendar entry to check
     * @param calendarView the calendar view
     * @return the matching calendar entries
     */
    @Override
    public List<CmsCalendarEntry> matchCalendarView(CmsCalendarEntry entry, I_CmsCalendarView calendarView) {

        return matchCalendarView(entry, calendarView, Integer.MAX_VALUE);
    }

    /**
     * Returns the list of matching entries for the calendar view.<p>
     * 
     * @param entry the calendar entry to check
     * @param calendarView the calendar view
     * @param maxCount the maximum count of returned serial entries
     * @return the matching calendar entries
     */
    public List<CmsCalendarEntry> matchCalendarView(CmsCalendarEntry entry, I_CmsCalendarView calendarView, int maxCount) {

        if (isSerialDate()) {
            return m_serialOptions.matchCalendarView(entry, calendarView, maxCount);
        } else {
            return super.matchCalendarView(entry, calendarView);
        }
    }

    public String getFormattedEntryDetails() {
        String result = getFormattedEventDuration() + "\n" + getFormattedEventType() + "\n" + getFormattedDateBoundaries();
        if (result.equals(""))
            result = "No data found.";
        return result;
    }

    /**
     * Converts the duration of the event into a legible String that shows the start and end times for every repetition.
     *
     * @return the formatted version of the corresponding times.
     */
    private String getFormattedEventDuration() {
        Calendar startDate = getStartDate();
        Calendar endDate = getEndDate();
        String pattern = "'From' HH:mm ";
        StringBuilder result = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat(pattern);

        result.append(format.format(startDate.getTime()));

        pattern = "'to' HH:mm ";
        if (!checkSameDate(startDate, endDate)) {
            long diff = endDate.getTimeInMillis() - startDate.getTimeInMillis();
            diff = TimeUnit.MILLISECONDS.toDays(diff);

            pattern += "+" + diff + " ";
        }
        pattern += "zzz";
        format.applyPattern(pattern);
        result.append(format.format(endDate.getTime()));

        return result.toString();
    }

    /**
     * Converts the event type into a legible String that shows how often the event will be repeated throughout the series.
     *
     * @return the formatted version of the event type.
     */
    private String getFormattedEventType() {
        StringBuilder result = new StringBuilder();
        Map<String, String> options = getConfigurationValuesAsMap();
        int type = Integer.parseInt(options.get("type"));
        int offset = 0;
        if (options.containsKey("interval")) {
            if (options.get("interval").equals("2")) {
                result.append("Every two s");
                offset = result.length() - 1;
            } else {
                result.append("ly");
            }
        }
        switch (type) {
            case 1:
                result = offset == 0 ? result.insert(offset, "Dai") : result.insert(offset, "day");
                break;
            case 2:
                result.insert(offset, "week").append(" on ");
                String [] eventDaysArray = options.get("weekdays").split(",");
                Iterator<String> eventDays = Arrays.asList(eventDaysArray).iterator();
                while (eventDays.hasNext()) {
                    String day = eventDays.next();
                    switch (day) {
                        case "1":
                            result.append("Sundays");
                            break;
                        case "2":
                            result.append("Mondays");
                            break;
                        case "3":
                            result.append("Tuesdays");
                            break;
                        case "4":
                            result.append("Wednesdays");
                            break;
                        case "5":
                            result.append("Thursdays");
                            break;
                        case "6":
                            result.append("Fridays");
                            break;
                        case "7":
                            result.append("Saturdays");
                            break;
                    }
                    if (eventDays.hasNext()) {
                        result.append(", ");
                    }
                }
                break;
            case 3:
                result.insert(offset, "month").append(" on ");
                String dayOfMonth = options.get("dayofmonth");
                dayOfMonth = formatDayOfMonth(dayOfMonth);
                result.append("the ");
                result.append(dayOfMonth);
                if (options.containsKey("weekdays")) {
                    String day = options.get("weekdays");
                    switch (day) {
                        case "1":
                            result.append(" Sunday");
                            break;
                        case "2":
                            result.append(" Monday");
                            break;
                        case "3":
                            result.append(" Tuesday");
                            break;
                        case "4":
                            result.append(" Wednesday");
                            break;
                        case "5":
                            result.append(" Thursday");
                            break;
                        case "6":
                            result.append(" Friday");
                            break;
                        case "7":
                            result.append(" Saturday");
                            break;
                    }
                }
                break;
            case 4:
                result.append("Yearly on ");
                String month = options.get("month");
                switch (month) {
                    case "0":
                        result.append("January ");
                        break;
                    case "1":
                        result.append("February ");
                        break;
                    case "2":
                        result.append("March ");
                        break;
                    case "3":
                        result.append("April ");
                        break;
                    case "4":
                        result.append("May ");
                        break;
                    case "5":
                        result.append("June ");
                        break;
                    case "6":
                        result.append("July");
                        break;
                    case "7":
                        result.append("August ");
                        break;
                    case "8":
                        result.append("September ");
                        break;
                    case "9":
                        result.append("October ");
                        break;
                    case "10":
                        result.append("November ");
                        break;
                    case "11":
                        result.append("December ");
                        break;
                }
                dayOfMonth = options.get("dayofmonth");
                dayOfMonth = formatDayOfMonth(dayOfMonth);
                result.append("the ").append(dayOfMonth);
                break;
        }
        result.replace(0, 1, result.substring(0, 1).toUpperCase());
        return result.toString();
    }

    /**
     * Converts the start and end dates of the repeating event into a legible String that shows the first and final date
     * on which the event will be repeated.
     *
     * @return the formatted version of the time boundaries of the repetition.
     */
    private String getFormattedDateBoundaries() {
        StringBuffer result = new StringBuffer();
        Map<String, String> options = getConfigurationValuesAsMap();
        String pattern = "dd.MM.yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        result.append("From ").append(formatter.format(getStartDate().getTime()));
        switch (options.get("endtype")) {
            case "2":
                result.append(" until after ").append(options.get("occurrences")).append(" events.");
                break;
            case "3":
                result.append(" until ").append(formatter.format(getSerialEndDate().getTime()));
                break;
            default:
                result.append(" indefinitely.");
        }

        return result.toString();
    }

    /**
     * Adds the proper suffix to numerals.
     *
     * @param dayOfMonth a day in the form of an integer
     * @return the day with the suffix appended
     */
    private String formatDayOfMonth(String dayOfMonth) {
        switch (dayOfMonth) {
            case "1":
                dayOfMonth += "st";
                break;
            case "2":
                dayOfMonth += "nd";
                break;
            case "3":
                dayOfMonth += "rd";
                break;
            default:
                dayOfMonth += "th";
                break;
        }
        return dayOfMonth;
    }

    /**
     * Checks if two Date objects take place at the same date.
     *
     * @param firstDate first date chronologically
     * @param secondDate second date chronologically
     * @return true if both times take place at the same date, false otherwise
     */
    private boolean checkSameDate(Calendar firstDate, Calendar secondDate) {
        return (firstDate.get(Calendar.DAY_OF_YEAR) == secondDate.get(Calendar.DAY_OF_YEAR)) &&
                (firstDate.get(Calendar.YEAR) == secondDate.get(Calendar.YEAR));
    }

    /**
     * Sets the occurrences of a defined series interval, used for the series end type.<p>
     *
     * @param occurrences the occurrences of a defined series interval, used for the series end type
     */
    public void setOccurrences(int occurrences) {

        m_occurrences = occurrences;
    }

    /**
     * Sets the serial end date if the series is of type: ending at specific date.<p>
     *
     * @param serialEndDate the serial end date if the series is of type: ending at specific date
     */
    public void setSerialEndDate(Calendar serialEndDate) {

        m_serialEndDate = serialEndDate;
    }

    /**
     * Sets the end type of the date series (never, n times, specific date).<p>
     * 
     * @param endType the end type of the date series
     */
    public void setSerialEndType(int endType) {

        m_serialEndType = endType;
    }

    /**
     * Sets the serial date options for the date series.<p>
     * 
     * @param serialOptions the serial date options for the date series
     */
    public void setSerialOptions(I_CmsCalendarSerialDateOptions serialOptions) {

        m_serialOptions = serialOptions;
    }
}
