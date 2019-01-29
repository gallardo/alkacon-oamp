package com.alkacon.opencms.v8.calendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsProperty;
import org.opencms.file.CmsRequestContext;
import org.opencms.file.CmsResource;
import org.opencms.main.CmsException;
import org.mockito.Mock;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@RunWith(MockitoJUnitRunner.class)
public class TestCmsCalendarEntryDateSerial {
    @Mock
    private CmsObject cmsObject;
    @Mock
    private CmsResource calResource;
    @Mock
    private CmsProperty cmsProperty;
    @Mock
    private CmsRequestContext context;

    private CmsCalendarEntryDateSerial entryDateSerial;

    @Before
    public void setup() {
        cmsObject = mock(CmsObject.class);
        calResource = mock(CmsResource.class);
        cmsProperty = mock(CmsProperty.class);
        context = mock(CmsRequestContext.class);

        try {
            when(cmsObject.readPropertyObject(eq(calResource), anyString(), eq(false))).thenReturn(cmsProperty);
        } catch (CmsException e) {
            e.printStackTrace();
        }
        when(cmsProperty.getValue()).thenReturn("");

        when(cmsObject.getRequestContext()).thenReturn(context);
        when(context.getLocale()).thenReturn(new Locale("en"));
    }

    @Test
    public void test_daily_event_repeating_everyday() {
        final String TIME_BEGIN = "17:00";
        final String TIME_END = "18:00";
        final String FIRST_DAY = "03.03.2017";
        final String LAST_DAY = "13.03.2017";
        final String FREQUENCY = "Daily";
        final String TIME_ZONE = "CET";

        when(cmsProperty.getValueMap(new HashMap<>())).thenReturn(buildMap("test_daily_event_repeating_everyday"));

        assertCalendarEntry(TIME_BEGIN, TIME_END, FIRST_DAY, LAST_DAY, FREQUENCY, TIME_ZONE);
    }

    @Test
    public void test_daily_event_repeating_everyday_no_end() {
        final String TIME_BEGIN = "09:00";
        final String TIME_END = "13:00";
        final String FIRST_DAY = "12.02.2017";
        final String FREQUENCY = "Daily";
        final String TIME_ZONE = "CET";

        when(cmsProperty.getValueMap(new HashMap<>())).thenReturn(buildMap("test_daily_event_repeating_everyday_no_end"));

        entryDateSerial = CmsSerialDateContentBean.getSerialEntryFrom(cmsObject, calResource);
        assert entryDateSerial != null;
        String formattedEntryDetails = entryDateSerial.getFormattedEntryDetails();

        assertThat(formattedEntryDetails, containsString(TIME_BEGIN));
        assertThat(formattedEntryDetails, containsString(TIME_END));
        assertThat(formattedEntryDetails, containsString(TIME_ZONE));
        assertThat(formattedEntryDetails, containsString(FIRST_DAY));
        assertThat(formattedEntryDetails, containsString(FREQUENCY));
    }

    @Test
    public void test_daily_event_repeating_every_two_days() {
        final String TIME_BEGIN = "11:00";
        final String TIME_END = "12:00";
        final String FIRST_DAY = "08.04.2017";
        final String LAST_DAY = "26.04.2017";
        final String FREQUENCY = "Every two days";
        final String TIME_ZONE = "CEST";

        when(cmsProperty.getValueMap(new HashMap<>())).thenReturn(buildMap("test_daily_event_repeating_every_two_days"));

        assertCalendarEntry(TIME_BEGIN, TIME_END, FIRST_DAY, LAST_DAY, FREQUENCY, TIME_ZONE);
    }

    @Test
    public void test_weekly_event_repeating_every_week_monday_wednesday_friday() {
        final String TIME_BEGIN = "14:00";
        final String TIME_END = "14:00 +1";
        final String FIRST_DAY = "12.03.2017";
        final String OCCURRENCES = "8";
        final String FREQUENCY = "Weekly";
        final String DAYS = "Mondays, Wednesdays, Fridays";
        final String TIME_ZONE = "CET";

        when(cmsProperty.getValueMap(new HashMap<>())).thenReturn(buildMap("test_weekly_event_repeating_every_week_monday_wednesday_friday"));

        entryDateSerial = CmsSerialDateContentBean.getSerialEntryFrom(cmsObject, calResource);
        assert entryDateSerial != null;
        String formattedEntryDetails = entryDateSerial.getFormattedEntryDetails();

        assertThat(formattedEntryDetails, containsString(TIME_BEGIN));
        assertThat(formattedEntryDetails, containsString(TIME_END));
        assertThat(formattedEntryDetails, containsString(TIME_ZONE));
        assertThat(formattedEntryDetails, containsString(FIRST_DAY));
        assertThat(formattedEntryDetails, containsString(OCCURRENCES));
        assertThat(formattedEntryDetails, containsString(FREQUENCY));
        assertThat(formattedEntryDetails, containsString(DAYS));
    }

    @Test
    public void test_weekly_event_repeating_every_two_weeks_tuesday_thursday() {
        final String TIME_BEGIN = "14:00";
        final String TIME_END = "15:00";
        final String FIRST_DAY = "12.05.2017";
        final String LAST_DAY = "05.10.2017";
        final String FREQUENCY = "Every two weeks";
        final String DAYS = "Tuesdays, Thursdays";
        final String TIME_ZONE = "CEST";

        when(cmsProperty.getValueMap(new HashMap<>())).thenReturn(buildMap("test_weekly_event_repeating_every_two_weeks_tuesday_thursday"));

        entryDateSerial = CmsSerialDateContentBean.getSerialEntryFrom(cmsObject, calResource);
        assert entryDateSerial != null;
        String formattedEntryDetails = entryDateSerial.getFormattedEntryDetails();

        assertThat(formattedEntryDetails, containsString(TIME_BEGIN));
        assertThat(formattedEntryDetails, containsString(TIME_END));
        assertThat(formattedEntryDetails, containsString(TIME_ZONE));
        assertThat(formattedEntryDetails, containsString(FIRST_DAY));
        assertThat(formattedEntryDetails, containsString(LAST_DAY));
        assertThat(formattedEntryDetails, containsString(FREQUENCY));
        assertThat(formattedEntryDetails, containsString(DAYS));
    }

    @Test
    public void test_monthly_event_on_seventh_day_every_month() {
        final String TIME_BEGIN = "17:00";
        final String TIME_END = "18:00";
        final String FIRST_DAY = "07.01.2017";
        final String FREQUENCY = "Monthly";
        final String DAYS = "7th";
        final String TIME_ZONE = "CET";

        when(cmsProperty.getValueMap(new HashMap<>())).thenReturn(buildMap("test_monthly_event_on_seventh_day_every_month"));

        assertCalendarEntry(TIME_BEGIN, TIME_END, FIRST_DAY, FREQUENCY, DAYS, TIME_ZONE);
    }

    @Test
    public void test_monthly_event_at_second_monday_every_two_months() {
        final String TIME_BEGIN = "17:00";
        final String TIME_END = "18:00";
        final String FIRST_DAY = "07.01.2017";
        final String FREQUENCY = "Every two months";
        final String DAYS = "2nd Monday";
        final String TIME_ZONE = "CET";

        when(cmsProperty.getValueMap(new HashMap<>())).thenReturn(buildMap("test_monthly_event_at_second_monday_every_two_months"));

        assertCalendarEntry(TIME_BEGIN, TIME_END, FIRST_DAY, FREQUENCY, DAYS, TIME_ZONE);
    }

    @Test
    public void test_yearly_event_on_january_seventh() {
        final String TIME_BEGIN = "08:00";
        final String TIME_END = "10:00";
        final String FIRST_DAY = "07.01.2017";
        final String FREQUENCY = "Yearly";
        final String MONTH = "January";
        final String DAY = "7th";
        final String TIME_ZONE = "CET";

        when(cmsProperty.getValueMap(new HashMap<>())).thenReturn(buildMap("test_yearly_event_on_january_seventh"));

        entryDateSerial = CmsSerialDateContentBean.getSerialEntryFrom(cmsObject, calResource);
        assert entryDateSerial != null;
        String formattedEntryDetails = entryDateSerial.getFormattedEntryDetails();

        assertThat(formattedEntryDetails, containsString(TIME_BEGIN));
        assertThat(formattedEntryDetails, containsString(TIME_END));
        assertThat(formattedEntryDetails, containsString(TIME_ZONE));
        assertThat(formattedEntryDetails, containsString(FIRST_DAY));
        assertThat(formattedEntryDetails, containsString(FREQUENCY));
        assertThat(formattedEntryDetails, containsString(MONTH));
        assertThat(formattedEntryDetails, containsString(DAY));
    }

    @Test
    public void test_daily_event_repeating_weekdays() {
        // XXX: Unfortunately this option is broken, this is a placeholder in case it gets fixed in the future.
        System.out.println("option not available");
    }

    @Test
    public void test_yearly_event_at_first_sunday_in_march() {
        // XXX: Unfortunately this option is broken, this is a placeholder in case it gets fixed in the future.
        System.out.println("option not available");
    }

    private void assertCalendarEntry(String TIME_BEGIN, String TIME_END, String FIRST_DAY, String LAST_DAY, String FREQUENCY, String TIME_ZONE) {
        entryDateSerial = CmsSerialDateContentBean.getSerialEntryFrom(cmsObject, calResource);
        assert entryDateSerial != null;
        String formattedEntryDetails = entryDateSerial.getFormattedEntryDetails();

        assertThat(formattedEntryDetails, containsString(TIME_BEGIN));
        assertThat(formattedEntryDetails, containsString(TIME_END));
        assertThat(formattedEntryDetails, containsString(TIME_ZONE));
        assertThat(formattedEntryDetails, containsString(FIRST_DAY));
        assertThat(formattedEntryDetails, containsString(LAST_DAY));
        assertThat(formattedEntryDetails, containsString(FREQUENCY));
    }

    private HashMap<String, String> buildMap(String testType) {
        final String EVENT_ENDING_BEHAVIOUR = "endtype";
        final String EVENT_FIRST_DATE = "startdate";
        final String EVENT_DURATION_UPPER_BOUND = "enddate";
        final String EVERY_WORKING_DAY = "everyworkingday";
        // since "everyworkingday" is broken, no other boolean value is needed.
        final String FALSE = "" + Boolean.FALSE;
        final String EVENT_INTERVAL_TYPE = "type";
        final String EVENT_LAST_DATE = "serialenddate";
        final String EVENT_INTERVAL = "interval";
        final String EVENT_OCCURRENCES = "occurrences";
        final String EVENT_DAYS = "weekdays";
        final String EVENT_DAY_OF_MONTH = "dayofmonth";
        final String EVENT_MONTH = "month";

        final String NO_ENDING = "1";
        final String END_AFTER_GIVEN_OCCURRENCES = "2";
        final String END_ON_GIVEN_DATE = "3";
        final String EVERYDAY = "1";
        final String EVERY_TWO_DAYS = "2";
        final String WEEKLY = "1";
        final String TWO_WEEKS = "2";
        final String MONTHLY = "1";
        final String TWO_MONTHS = "2";
//        final String SUNDAYS = "1";
        final String MONDAYS = "2";
        final String TUESDAYS = "3";
        final String WEDNESDAYS = "4";
        final String THURSDAYS = "5";
        final String FRIDAYS = "6";
//        final String SATURDAYS = "7";
        final String JANUARY = "0";
        final String TYPE_DAILY = "1";
        final String TYPE_WEEKLY = "2";
        final String TYPE_MONTHLY = "3";
        final String TYPE_YEARLY = "4";

        // CET (UTC+1)
        final String DATE_01_JAN_2017_1400H = "1262350800000";
        final String DATE_01_JAN_2017_1430H = "1262352600000";
        final String DATE_07_JAN_2017_1700H = "1483804800000";
        final String DATE_07_JAN_2017_1800H = "1483808400000";
        final String DATE_03_MAR_2017_1700H = "1488556800000";
        final String DATE_03_MAR_2017_1800H = "1488560400000";
        final String DATE_12_MAR_2017_1400H = "1489323600000";
        final String DATE_13_MAR_2017_1400H = "1489410000000";
        final String DATE_13_MAR_2017_1700H = "1489420800000";
        final String DATE_12_FEB_2017_0900H = "1486886400000";
        final String DATE_12_FEB_2017_1300H = "1486900800000";
        final String DATE_07_JAN_2017_0800H = "1483772400000";
        final String DATE_07_JAN_2017_1000H = "1483779600000";

        // CEST (UTC+2)
        final String DATE_08_APR_2017_1100H = "1491642000000";
        final String DATE_08_APR_2017_1200H = "1491645600000";
        final String DATE_26_APR_2017_1642H = "1493217720000";
        final String DATE_12_MAY_2017_1400H = "1494590400000";
        final String DATE_12_MAY_2017_1500H = "1494594000000";
        final String DATE_05_OCT_2017_1500H = "1507208400000";

        HashMap<String, String> values = new HashMap<>();
        switch (testType) {
            case "test_daily_event_repeating_everyday":
                values.put(EVENT_ENDING_BEHAVIOUR, END_ON_GIVEN_DATE);
                values.put(EVENT_FIRST_DATE, DATE_03_MAR_2017_1700H);
                values.put(EVENT_DURATION_UPPER_BOUND, DATE_03_MAR_2017_1800H);
                values.put(EVERY_WORKING_DAY, FALSE);
                values.put(EVENT_INTERVAL, EVERYDAY);
                values.put(EVENT_INTERVAL_TYPE, TYPE_DAILY);
                values.put(EVENT_LAST_DATE, DATE_13_MAR_2017_1700H);
                break;
            case "test_daily_event_repeating_everyday_no_end":
                values.put(EVENT_ENDING_BEHAVIOUR, NO_ENDING);
                values.put(EVENT_FIRST_DATE, DATE_12_FEB_2017_0900H);
                values.put(EVENT_DURATION_UPPER_BOUND, DATE_12_FEB_2017_1300H);
                values.put(EVERY_WORKING_DAY, FALSE);
                values.put(EVENT_INTERVAL, EVERYDAY);
                values.put(EVENT_INTERVAL_TYPE, TYPE_DAILY);
                break;
            case "test_daily_event_repeating_every_two_days":
                values.put(EVENT_ENDING_BEHAVIOUR, END_ON_GIVEN_DATE);
                values.put(EVENT_FIRST_DATE, DATE_08_APR_2017_1100H);
                values.put(EVENT_DURATION_UPPER_BOUND, DATE_08_APR_2017_1200H);
                values.put(EVERY_WORKING_DAY, FALSE);
                values.put(EVENT_INTERVAL, EVERY_TWO_DAYS);
                values.put(EVENT_INTERVAL_TYPE, TYPE_DAILY);
                values.put(EVENT_LAST_DATE, DATE_26_APR_2017_1642H);
                break;
            case "test_weekly_event_repeating_every_week_monday_wednesday_friday":
                values.put(EVENT_ENDING_BEHAVIOUR, END_AFTER_GIVEN_OCCURRENCES);
                values.put(EVENT_FIRST_DATE, DATE_12_MAR_2017_1400H);
                values.put(EVENT_DURATION_UPPER_BOUND, DATE_13_MAR_2017_1400H);
                values.put(EVENT_INTERVAL, WEEKLY);
                values.put(EVENT_INTERVAL_TYPE, TYPE_WEEKLY);
                values.put(EVENT_DAYS, MONDAYS + "," + WEDNESDAYS + "," + FRIDAYS);
                values.put(EVENT_OCCURRENCES, "8");
                break;
            case "test_weekly_event_repeating_every_two_weeks_tuesday_thursday":
                values.put(EVENT_ENDING_BEHAVIOUR, END_ON_GIVEN_DATE);
                values.put(EVENT_FIRST_DATE, DATE_12_MAY_2017_1400H);
                values.put(EVENT_DURATION_UPPER_BOUND, DATE_12_MAY_2017_1500H);
                values.put(EVENT_INTERVAL, TWO_WEEKS);
                values.put(EVENT_INTERVAL_TYPE, TYPE_WEEKLY);
                values.put(EVENT_DAYS, TUESDAYS + "," + THURSDAYS);
                values.put(EVENT_LAST_DATE, DATE_05_OCT_2017_1500H);
                break;
            case "test_monthly_event_on_seventh_day_every_month":
                values.put(EVENT_ENDING_BEHAVIOUR, NO_ENDING);
                values.put(EVENT_FIRST_DATE, DATE_07_JAN_2017_1700H);
                values.put(EVENT_DURATION_UPPER_BOUND, DATE_07_JAN_2017_1800H);
                values.put(EVENT_INTERVAL, MONTHLY);
                values.put(EVENT_INTERVAL_TYPE, TYPE_MONTHLY);
                values.put(EVENT_DAY_OF_MONTH, "7");
                break;
            case "test_monthly_event_at_second_monday_every_two_months":
                values.put(EVENT_ENDING_BEHAVIOUR, NO_ENDING);
                values.put(EVENT_FIRST_DATE, DATE_07_JAN_2017_1700H);
                values.put(EVENT_DURATION_UPPER_BOUND, DATE_07_JAN_2017_1800H);
                values.put(EVENT_INTERVAL, TWO_MONTHS);
                values.put(EVENT_INTERVAL_TYPE, TYPE_MONTHLY);
                values.put(EVENT_DAYS, MONDAYS);
                values.put(EVENT_DAY_OF_MONTH, "2");
                break;
            case "test_yearly_event_on_january_seventh":
                values.put(EVENT_ENDING_BEHAVIOUR, NO_ENDING);
                values.put(EVENT_FIRST_DATE, DATE_07_JAN_2017_0800H);
                values.put(EVENT_DURATION_UPPER_BOUND, DATE_07_JAN_2017_1000H);
                values.put(EVENT_INTERVAL_TYPE, TYPE_YEARLY);
                values.put(EVENT_MONTH, JANUARY);
                values.put(EVENT_DAY_OF_MONTH, "7");
                break;
            case "test_yearly_event_at_first_sunday_in_march":
                values.put(EVENT_ENDING_BEHAVIOUR, NO_ENDING);
                values.put(EVENT_FIRST_DATE, DATE_01_JAN_2017_1400H);
                values.put(EVENT_DURATION_UPPER_BOUND, DATE_01_JAN_2017_1430H);
                values.put(EVENT_INTERVAL_TYPE, TYPE_YEARLY);
                values.put(EVENT_MONTH, JANUARY);
                values.put(EVENT_DAY_OF_MONTH, "7");
                break;
        }
        return values;
    }
}
