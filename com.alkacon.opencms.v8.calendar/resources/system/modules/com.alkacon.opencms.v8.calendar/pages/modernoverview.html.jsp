<%@ page session="false" buffer="none" %>
<%@ page import="java.util.*, com.alkacon.opencms.v8.calendar.*, org.opencms.jsp.*, org.opencms.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>

<c:set var="locale" value="${cms:vfs(pageContext).context.locale}" />
<fmt:setLocale value="${locale}" />


<%
CmsJspActionElement cms = new CmsJspActionElement(pageContext, request, response);
%>


<cms:formatter var="content" val="value">
    <div>
        <div class="box ${cms.element.settings.boxschema}">

            <%-- show optional text element above calendar entries --%>
            <c:if test="${content.value.Title.exists}">
                ${content.value.Title}
            </c:if>
            <%-- 
                From calendarview.xsd: configuration="0:%(key.calendar.default.view.day)|1:%(key.calendar.default.view.week)|2:%(key.calendar.default.view.month)"
                Default: month view
            --%>
            <c:set var="defaultView">${empty content.value.DefaultView ? 2 : content.value.DefaultView}</c:set>
            <div class="cal_wrapper">
                <%


                Calendar cal = new GregorianCalendar(cms.getRequestContext().getLocale());
                int currDay = cal.get(Calendar.DATE);
                int currMonth = cal.get(Calendar.MONTH);
                int currYear = cal.get(Calendar.YEAR);

                String pDay = request.getParameter("calDay");
                String pMonth = request.getParameter("calMonth");
                String pYear = request.getParameter("calYear");



                if (CmsStringUtil.isNotEmpty(pDay)) {
                currDay = Integer.parseInt(pDay);
                }

                if (CmsStringUtil.isNotEmpty(pMonth)) {
                currMonth = Integer.parseInt(pMonth);
                }

                if (CmsStringUtil.isNotEmpty(pYear)) {
                currYear = Integer.parseInt(pYear);
                }

                // Calendar documentation: http://fullcalendar.io/
                %>
                <script type="text/javascript">

                $(document).ready(function () {
                    var calItemsJsp = '<%= cms.link("/system/modules/com.alkacon.opencms.v8.calendar/pages/modern_items.jsp") %>';

                    function calendarCenterLoad() {
                        calendarCenterShow( <%= currDay %> , <%= currMonth %> , <%= currYear %> );
                    }

                    function calendarCenterShow(cDay, cMonth, cYear) {

                        $("#calendarcenter").fullCalendar({
                            defaultDate: moment({year: cYear, month: cMonth, date: cDay}),
                            editable: false,
                            allDayDefault: false,
                            defaultView: ["basicDay","basicWeek","month"][${defaultView}],
                            lang: '${locale}',
                            timeFormat: 'H:mm',
                            header: {
                                left: 'prev,next today',
                                center: 'title',
                                right: 'month,basicWeek,basicDay'
                            },
                            <%--
                            // XXX: AG 2015-03-17 Doesn't render in its place!
                            eventRender: function (event, element) {
                                element.qtip({
                                    content: {
                                        title: { text: event.title },
                                        text: '<span class="title">Start: </span>'
                                            + event.start.format('hh:mm')
                                            + '<br><span class="title">Description: </span>'
                                            + event.description
                                    },
                                    show: { solo: true },
                                    style: {
                                        width: 200,
                                        padding: 5,
                                        color: 'red',
                                        textAlign: 'left',
                                        border: {
                                           width: 1,
                                           radius: 3
                                        },
                                        corner: "topLeft",
                                        tip: "topLeft",
                                        name: "light"
                                    }
                                });
                            },
                            --%>
                            events: function (start, end, timezone, callback) {
                                $.get(calItemsJsp, 
                                    {
                                        uri: "<%= cms.getRequestContext().getUri() %>",
                                        __locale: "<%= cms.getRequestContext().getLocale() %>",
                                        sYear: start.year(),
                                        sMonth: start.month(),
                                        sDay: start.date(),
                                        eYear: end.year(),
                                        eMonth: end.month(),
                                        eDay: end.date()
                                    },
                                    function (data) {
                                        var events = eval(data);
                                        callback(events);
                                    }
                                );
                            },
                            fixedWeekCount: false
                        });
                    }
                    
                    $("#calendarcenterload").remove();
                    calendarCenterLoad();
                });


                </script>

                <div class="element">
                    <div id="calendarcenter">
                        <div id="calendarcenterload" style="text-align: center;"><img src="<%= cms.link("/system/modules/com.alkacon.opencms.v8.calendar/resources/load.gif") %>" alt="" style="padding: 24px 8px;" /></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</cms:formatter>