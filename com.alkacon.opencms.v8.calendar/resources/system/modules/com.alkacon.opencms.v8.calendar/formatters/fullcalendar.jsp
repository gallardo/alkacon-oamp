<%@ page session="false" buffer="none" trimDirectiveWhitespaces="true" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.opencms.main.CmsLog" %>
<%@ page import="org.opencms.util.CmsStringUtil" %>
<%@ page import="org.opencms.jsp.CmsJspActionElement" %>
<%@ page import="com.alkacon.opencms.v8.calendar.CmsCalendarDisplay" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>

<c:set var="locale" value="${cms:vfs(pageContext).context.locale}" />
<fmt:setLocale value="${locale}" />


<%
    Log LOG = CmsLog.getLog(this.getClass());
    LOG.debug("Requested fullcalendar-formatter.jsp");
    CmsJspActionElement cms = new CmsJspActionElement(pageContext, request, response);
%>

<cms:formatter var="content" val="value">
	<div class="margin-bottom-30 <c:out value='${cms.element.settings.wrapperclass}' default='' /> <c:out value='${cms.element.settings.boxlayout}' default='' />">

		<c:choose>
			<c:when test="${cms.element.inMemoryOnly}">
				<%-- Dummy-Demo-Content: Anfang --%>
				<h5>Neuer Kalender, bitte editieren!</h5>
				<%-- Dummy-Demo-Content: Ende --%>
			</c:when>

			<c:otherwise>
				<c:catch var="exception">

					<div class="box ${cms.element.settings.boxschema}">

						<%-- show optional text element above calendar entries --%>
						<c:if test="${value.Title.exists}">
							${value.Title}
						</c:if>
						
						<%--
						From calendarview.xsd:
						configuration=
						"0:%(key.calendar.default.view.day) |
						1:%(key.calendar.default.view.week) |
						2:%(key.calendar.default.view.month)"
						Default: month view
						--%>
						<c:set var="defaultView">${empty value.DefaultView ? 2 : value.DefaultView}</c:set>
						<div class="cal_wrapper">
							<%
								Calendar cal = new GregorianCalendar(cms.getRequestContext().getLocale());
								int currDay = cal.get(Calendar.DATE);
								int currMonth = cal.get(Calendar.MONTH);
								int currYear = cal.get(Calendar.YEAR);

								String pDay = request.getParameter(CmsCalendarDisplay.PARAM_DAY);
								String pMonth = request.getParameter(CmsCalendarDisplay.PARAM_MONTH);
								String pYear = request.getParameter(CmsCalendarDisplay.PARAM_YEAR);

								if (CmsStringUtil.isNotEmpty(pDay)) {
									currDay = Integer.parseInt(pDay);
								}

								if (CmsStringUtil.isNotEmpty(pMonth)) {
									currMonth = Integer.parseInt(pMonth);
								}

								if (CmsStringUtil.isNotEmpty(pYear)) {
									currYear = Integer.parseInt(pYear);
								}
							%>
							
							<script type="text/javascript">
								var contentFilename = "${content.filename}";
								var calDefaultView = ${defaultView};
								var calLocale = "${locale}";
								var __calLocale = "<% cms.getRequestContext().getLocale(); %>";
							</script>
							<div class="element">
								<div id="calendarcenter">
									<div id="calendarcenterload" style="text-align: center;">
										<img src='<%= cms.link("/system/modules/com.alkacon.opencms.v8.calendar/resources/load.gif") %>' alt="" style="padding: 24px 8px;" />
									</div>
								</div>
							</div>
						</div>
						<c:if test="${cms.edited}">
							${cms.enableReload}
						</c:if>

					</div>

				</c:catch>
				<c:if test="${exception != null and not cms.isOnlineProject }">
					<div class="alert alert-danger">
						<strong>Error</strong>
						${exception.message}
					</div>
				</c:if>

			</c:otherwise>
		</c:choose>

	</div>
</cms:formatter>
