package com.alkacon.opencms.v8.calendar;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsProperty;
import org.opencms.file.CmsRequestContext;
import org.opencms.file.CmsResource;
import org.opencms.main.CmsException;

import java.util.HashMap;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SuppressWarnings("SpellCheckingInspection")
@RunWith(MockitoJUnitRunner.class)
public class TestCmsCalendarEntryDateSerial {
    private static final String EVENT_ENDING_BEHAVIOUR = "endtype";
    private static final String EVENT_FIRST_DATE = "startdate";
    private static final String EVENT_DURATION_UPPER_BOUND = "enddate";
    private static final String EVERY_WORKING_DAY = "everyworkingday";
    // since "everyworkingday" is broken, no other boolean value is needed.
    private static final String FALSE = "" + Boolean.FALSE;
    private static final String EVENT_INTERVAL_TYPE = "type";
    private static final String EVENT_LAST_DATE = "serialenddate";
    private static final String EVENT_INTERVAL = "interval";
    private static final String EVENT_OCCURRENCES = "occurrences";
    private static final String EVENT_DAYS = "weekdays";
    private static final String EVENT_DAY_OF_MONTH = "dayofmonth";
    private static final String EVENT_MONTH = "month";

    private static final String NO_ENDING = "1";
    private static final String END_AFTER_GIVEN_OCCURRENCES = "2";
    private static final String END_ON_GIVEN_DATE = "3";
    private static final String EVERYDAY = "1";
    private static final String EVERY_TWO_DAYS = "2";
    private static final String WEEKLY = "1";
    private static final String TWO_WEEKS = "2";
    private static final String MONTHLY = "1";
    private static final String TWO_MONTHS = "2";
    //      static   final String SUNDAYS = "1";
    private static final String MONDAYS = "2";
    private static final String TUESDAYS = "3";
    private static final String WEDNESDAYS = "4";
    private static final String THURSDAYS = "5";
    private static final String FRIDAYS = "6";
    //      static   final String SATURDAYS = "7";
    private static final String JANUARY = "0";
    private static final String TYPE_DAILY = "1";
    private static final String TYPE_WEEKLY = "2";
    private static final String TYPE_MONTHLY = "3";
    private static final String TYPE_YEARLY = "4";

    // CET (UTC+1)
    private static final String DATE_01_JAN_2017_1400H = "1262350800000";
    private static final String DATE_01_JAN_2017_1430H = "1262352600000";
    private static final String DATE_07_JAN_2017_1700H = "1483804800000";
    private static final String DATE_07_JAN_2017_1800H = "1483808400000";
    private static final String DATE_03_MAR_2017_1700H = "1488556800000";
    private static final String DATE_03_MAR_2017_1800H = "1488560400000";
    private static final String DATE_12_MAR_2017_1400H = "1489323600000";
    private static final String DATE_13_MAR_2017_1400H = "1489410000000";
    private static final String DATE_13_MAR_2017_1700H = "1489420800000";
    private static final String DATE_12_FEB_2017_0900H = "1486886400000";
    private static final String DATE_12_FEB_2017_1300H = "1486900800000";
    private static final String DATE_07_JAN_2017_0800H = "1483772400000";
    private static final String DATE_07_JAN_2017_1000H = "1483779600000";

    // CEST (UTC+2)
    private static final String DATE_08_APR_2017_1100H = "1491642000000";
    private static final String DATE_08_APR_2017_1200H = "1491645600000";
    private static final String DATE_26_APR_2017_1642H = "1493217720000";
    private static final String DATE_12_MAY_2017_1400H = "1494590400000";
    private static final String DATE_12_MAY_2017_1500H = "1494594000000";
    private static final String DATE_05_OCT_2017_1500H = "1507208400000";

    @Mock
    private CmsObject cmsObject;
    @Mock
    private CmsResource calResource;
    @Mock
    private CmsProperty cmsProperty;
    @Mock
    private CmsRequestContext context;

    @Before
    public void setup() throws CmsException {
        cmsObject = mock(CmsObject.class);
        calResource = mock(CmsResource.class);
        cmsProperty = mock(CmsProperty.class);
        context = mock(CmsRequestContext.class);

        when(cmsObject.readPropertyObject(eq(calResource), anyString(), eq(false))).thenReturn(cmsProperty);
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

        when(cmsProperty.getValueMap(new HashMap<>())).thenReturn(new CmsPropertyValueMap()
                .setEventEndingBehaviour(END_ON_GIVEN_DATE)
                .setEventFirstDate(DATE_03_MAR_2017_1700H)
                .setEventDurationUpperBound(DATE_03_MAR_2017_1800H)
                .setEveryWorkingDay(FALSE)
                .setEventInterval(EVERYDAY)
                .setEventIntervalType(TYPE_DAILY)
                .setEventLastDate(DATE_13_MAR_2017_1700H).getValueMap());

        String formattedEntryDetails = CmsSerialDateContentBean.getSerialEntryFrom(cmsObject, calResource).getFormattedEntryDetails();
        assertThat(formattedEntryDetails)
                .contains(TIME_BEGIN)
                .contains(TIME_END)
                .contains(FIRST_DAY)
                .contains(LAST_DAY)
                .contains(FREQUENCY)
                .contains(TIME_ZONE);
    }

    @Test
    public void test_daily_event_repeating_everyday_no_end() {
        final String TIME_BEGIN = "09:00";
        final String TIME_END = "13:00";
        final String FIRST_DAY = "12.02.2017";
        final String FREQUENCY = "Daily";
        final String TIME_ZONE = "CET";

        when(cmsProperty.getValueMap(new HashMap<>())).thenReturn(new CmsPropertyValueMap()
                .setEventEndingBehaviour(NO_ENDING)
                .setEventFirstDate(DATE_12_FEB_2017_0900H)
                .setEventDurationUpperBound(DATE_12_FEB_2017_1300H)
                .setEveryWorkingDay(FALSE)
                .setEventInterval(EVERYDAY)
                .setEventIntervalType(TYPE_DAILY).getValueMap());

        String formattedEntryDetails = CmsSerialDateContentBean.getSerialEntryFrom(cmsObject, calResource).getFormattedEntryDetails();
        assertThat(formattedEntryDetails)
                .contains(TIME_BEGIN)
                .contains(TIME_END)
                .contains(TIME_ZONE)
                .contains(FIRST_DAY)
                .contains(FREQUENCY);
    }

    @Test
    public void test_daily_event_repeating_every_two_days() {
        final String TIME_BEGIN = "11:00";
        final String TIME_END = "12:00";
        final String FIRST_DAY = "08.04.2017";
        final String LAST_DAY = "26.04.2017";
        final String FREQUENCY = "Every two days";
        final String TIME_ZONE = "CEST";

        when(cmsProperty.getValueMap(new HashMap<>())).thenReturn(new CmsPropertyValueMap()
                .setEventEndingBehaviour(END_ON_GIVEN_DATE)
                .setEventFirstDate(DATE_08_APR_2017_1100H)
                .setEventDurationUpperBound(DATE_08_APR_2017_1200H)
                .setEveryWorkingDay(FALSE)
                .setEventInterval(EVERY_TWO_DAYS)
                .setEventIntervalType(TYPE_DAILY)
                .setEventLastDate(DATE_26_APR_2017_1642H).getValueMap());

        String formattedEntryDetails = CmsSerialDateContentBean.getSerialEntryFrom(cmsObject, calResource).getFormattedEntryDetails();
        assertThat(formattedEntryDetails)
                .contains(TIME_BEGIN)
                .contains(TIME_END)
                .contains(FIRST_DAY)
                .contains(LAST_DAY)
                .contains(FREQUENCY)
                .contains(TIME_ZONE);
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

        when(cmsProperty.getValueMap(new HashMap<>())).thenReturn(new CmsPropertyValueMap()
                .setEventEndingBehaviour(END_AFTER_GIVEN_OCCURRENCES)
                .setEventFirstDate(DATE_12_MAR_2017_1400H)
                .setEventDurationUpperBound(DATE_13_MAR_2017_1400H)
                .setEventInterval(WEEKLY)
                .setEventIntervalType(TYPE_WEEKLY)
                .setEventDays(MONDAYS + "," + WEDNESDAYS + "," + FRIDAYS)
                .setEventOccurrences("8").getValueMap());

        String formattedEntryDetails = CmsSerialDateContentBean.getSerialEntryFrom(cmsObject, calResource).getFormattedEntryDetails();
        assertThat(formattedEntryDetails)
                .contains(TIME_BEGIN)
                .contains(TIME_END)
                .contains(TIME_ZONE)
                .contains(FIRST_DAY)
                .contains(OCCURRENCES)
                .contains(FREQUENCY)
                .contains(DAYS);
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

        when(cmsProperty.getValueMap(new HashMap<>())).thenReturn(new CmsPropertyValueMap()
                .setEventEndingBehaviour(END_ON_GIVEN_DATE)
                .setEventFirstDate(DATE_12_MAY_2017_1400H)
                .setEventDurationUpperBound(DATE_12_MAY_2017_1500H)
                .setEventInterval(TWO_WEEKS)
                .setEventIntervalType(TYPE_WEEKLY)
                .setEventDays(TUESDAYS + "," + THURSDAYS)
                .setEventLastDate(DATE_05_OCT_2017_1500H).getValueMap());

        String formattedEntryDetails = CmsSerialDateContentBean.getSerialEntryFrom(cmsObject, calResource).getFormattedEntryDetails();
        assertThat(formattedEntryDetails)
                .contains(TIME_BEGIN)
                .contains(TIME_END)
                .contains(TIME_ZONE)
                .contains(FIRST_DAY)
                .contains(LAST_DAY)
                .contains(FREQUENCY)
                .contains(DAYS);
    }

    @Test
    public void test_monthly_event_on_seventh_day_every_month() {
        final String TIME_BEGIN = "17:00";
        final String TIME_END = "18:00";
        final String FIRST_DAY = "07.01.2017";
        final String FREQUENCY = "Monthly";
        final String DAYS = "7th";
        final String TIME_ZONE = "CET";

        when(cmsProperty.getValueMap(new HashMap<>())).thenReturn(new CmsPropertyValueMap()
                .setEventEndingBehaviour(NO_ENDING)
                .setEventFirstDate(DATE_07_JAN_2017_1700H)
                .setEventDurationUpperBound(DATE_07_JAN_2017_1800H)
                .setEventInterval(MONTHLY)
                .setEventIntervalType(TYPE_MONTHLY)
                .setEventDayOfMonth("7").getValueMap());

        String formattedEntryDetails = CmsSerialDateContentBean.getSerialEntryFrom(cmsObject, calResource).getFormattedEntryDetails();
        assertThat(formattedEntryDetails)
                .contains(TIME_BEGIN)
                .contains(TIME_END)
                .contains(FIRST_DAY)
                .contains(FREQUENCY)
                .contains(DAYS)
                .contains(TIME_ZONE);
    }

    @Test
    public void test_monthly_event_at_second_monday_every_two_months() {
        final String TIME_BEGIN = "17:00";
        final String TIME_END = "18:00";
        final String FIRST_DAY = "07.01.2017";
        final String FREQUENCY = "Every two months";
        final String DAYS = "2nd Monday";
        final String TIME_ZONE = "CET";

        when(cmsProperty.getValueMap(new HashMap<>())).thenReturn(new CmsPropertyValueMap()
                .setEventEndingBehaviour(NO_ENDING)
                .setEventFirstDate(DATE_07_JAN_2017_1700H)
                .setEventDurationUpperBound(DATE_07_JAN_2017_1800H)
                .setEventInterval(TWO_MONTHS)
                .setEventIntervalType(TYPE_MONTHLY)
                .setEventDays(MONDAYS)
                .setEventDayOfMonth("2").getValueMap());

        String formattedEntryDetails = CmsSerialDateContentBean.getSerialEntryFrom(cmsObject, calResource).getFormattedEntryDetails();
        assertThat(formattedEntryDetails)
                .contains(TIME_BEGIN)
                .contains(TIME_END)
                .contains(FIRST_DAY)
                .contains(FREQUENCY)
                .contains(DAYS)
                .contains(TIME_ZONE);
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

        when(cmsProperty.getValueMap(new HashMap<>())).thenReturn(new CmsPropertyValueMap()
                .setEventEndingBehaviour(NO_ENDING)
                .setEventFirstDate(DATE_07_JAN_2017_0800H)
                .setEventDurationUpperBound(DATE_07_JAN_2017_1000H)
                .setEventIntervalType(TYPE_YEARLY)
                .setEventMonth(JANUARY)
                .setEventDayOfMonth("7").getValueMap());

        String formattedEntryDetails = CmsSerialDateContentBean.getSerialEntryFrom(cmsObject, calResource).getFormattedEntryDetails();
        assertThat(formattedEntryDetails)
                .contains(TIME_BEGIN)
                .contains(TIME_END)
                .contains(TIME_ZONE)
                .contains(FIRST_DAY)
                .contains(FREQUENCY)
                .contains(MONTH)
                .contains(DAY);
    }

    @Test
    @Ignore("Currently broken, this is a placeholder in case it gets fixed in the future")
    public void test_daily_event_repeating_weekdays() {
        fail("NYI");
    }

    @Test
    @Ignore("Currently broken, this is a placeholder in case it gets fixed in the future")
    public void test_yearly_event_at_first_sunday_in_march() {
        CmsPropertyValueMap TEST_YEARLY_EVENT_AT_FIRST_SUNDAY_IN_MARCH = new CmsPropertyValueMap()
                .setEventEndingBehaviour(NO_ENDING)
                .setEventFirstDate(DATE_01_JAN_2017_1400H)
                .setEventDurationUpperBound(DATE_01_JAN_2017_1430H)
                .setEventIntervalType(TYPE_YEARLY)
                .setEventMonth(JANUARY)
                .setEventDayOfMonth("7");

        fail("NYI");
    }

    private static final class CmsPropertyValueMap {
        private HashMap<String, String> valueMap = new HashMap<>();

        public CmsPropertyValueMap setEventEndingBehaviour(String value) {
            valueMap.put(EVENT_ENDING_BEHAVIOUR, value);
            return this;
        }

        public CmsPropertyValueMap setEventFirstDate(String value) {
            valueMap.put(EVENT_FIRST_DATE, value);
            return this;
        }

        public CmsPropertyValueMap setEventDurationUpperBound(String value) {
            valueMap.put(EVENT_DURATION_UPPER_BOUND, value);
            return this;
        }

        public CmsPropertyValueMap setEveryWorkingDay(String value) {
            valueMap.put(EVERY_WORKING_DAY, value);
            return this;
        }

        public CmsPropertyValueMap setEventInterval(String value) {
            valueMap.put(EVENT_INTERVAL, value);
            return this;
        }

        public CmsPropertyValueMap setEventIntervalType(String value) {
            valueMap.put(EVENT_INTERVAL_TYPE, value);
            return this;
        }

        public CmsPropertyValueMap setEventDays(String value) {
            valueMap.put(EVENT_DAYS, value);
            return this;
        }

        public CmsPropertyValueMap setEventOccurrences(String value) {
            valueMap.put(EVENT_OCCURRENCES, value);
            return this;
        }

        public CmsPropertyValueMap setEventLastDate(String value) {
            valueMap.put(EVENT_LAST_DATE, value);
            return this;
        }

        public CmsPropertyValueMap setEventMonth(String value) {
            valueMap.put(EVENT_MONTH, value);
            return this;
        }

        public CmsPropertyValueMap setEventDayOfMonth(String value) {
            valueMap.put(EVENT_DAY_OF_MONTH, value);
            return this;
        }

        public HashMap<String, String> getValueMap() {
            return valueMap;
        }
    }
}
