<%@ page session="false"
	import="com.alkacon.opencms.v8.survey.CmsFormReportingBean"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="org.opencms.jsp.*" %>
<%@ page import="org.opencms.file.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@page import="org.opencms.util.*" %>
<%@ page import="com.alkacon.opencms.v8.survey.*" %>

<%
	// initialize the bean
	CmsFormReportingBean svReport = new CmsFormReportingBean(pageContext, request, response);
	pageContext.setAttribute("svReport", svReport);
%>


<%@page import="com.alkacon.opencms.v8.survey.CmsFormWorkBean"%>

<%-- set the parameters --%>
<c:set var="curPage">
	<c:if test="${!empty param.page}">${param.page}</c:if>
	<c:if test="${empty param.page}">1</c:if>
</c:set>

<c:set var="locale">
	<c:if test="${!empty cms:vfs(pageContext).context.locale}">${cms:vfs(pageContext).context.locale}</c:if>
	<c:if test="${ empty cms:vfs(pageContext).context.locale}">
		<cms:property name="locale" file="search" default="en" />
	</c:if>
</c:set>

<%-- start --%>
<fmt:setLocale value="${locale}" />
<fmt:bundle basename="com.alkacon.opencms.v8.survey.frontend">
<cms:formatter var="rootContent">
<div>
	<c:catch var="error">
		

			<c:set var="content" value="${rootContent.value.SurveyReport}" />
			<c:set var="webform" value="${rootContent.value.Form}" />
			<c:out value="${ content }" />

			<c:set var="color" value="${content.valueList['Color']}" />
			<c:choose>
				<c:when test="${content.value.AverageColor.exists}">
					<c:set var="avgcolor" value="${content.value.AverageColor.stringValue}" />
				</c:when>
				<c:otherwise>
					<c:set var="avgcolor"  value="#ff0000" />
				</c:otherwise>
			</c:choose>
			<c:if test="${empty avgcolor}"><c:set var="avgcolor" value="#ffff00" /></c:if>
			<c:set var="group" value="${content.value['DetailGroup'] }" />
			<c:set var="showCount" value="${content.value['ShowCount']}" />
			<c:set var="showCount" value="${(showCount == 'true') || svReport.showDetail[group]}" />
			<c:set var="AddText" value="${content.value['AddText']}" />
			<c:if test="${surveyIsClosed}">${content.value['SurveyClosedText']}</c:if>

			<c:if
				test="${!empty content.value['Text'] && (!param.detail || !svReport.showDetail[group])}">
				<c:out value="${content.value['Text']}" escapeXml="false" />
			</c:if>

			<% 
				CmsJspActionElement cjae = new CmsJspActionElement(pageContext, request, response);
				CmsRequestContext req = cjae.getRequestContext();
				pageContext.setAttribute("webformpath", req.getUri());
			%>
				


					<%-- look if the work bean already initialized --%>
					<c:if test="${empty workBean}">
								<c:if
							test="${!empty webform.value['DataTarget'] && fn:indexOf(webform.value['DataTarget/Transport'],'database') >= 0}">
							<c:set var="formid" value="${webform.value['DataTarget/FormId']}" />
							<c:if test="${!empty formid}">
						
								<%--special case if resource filter: <c:set var="itemParam" value="${formid}${svReport.separator}${svReport.requestContext.siteRoot}${resPath}"/>--%>
								<%
					            CmsFormWorkBean workbean = svReport.getReporting(String.valueOf(pageContext.getAttribute("formid")), String.valueOf(pageContext.getAttribute("webformpath")));
						        pageContext.setAttribute("workBean", workbean);
						        %>
							</c:if>
						</c:if>
					</c:if>

					<%-- print the content --%>
					<c:if test="${!empty workBean}">
						<div id="webformReport"><%-- special caption for the overview page --%>
						<c:if test="${!param.detail || !svReport.showDetail[group]}">
							<c:if test="${showCount == 'true'}">
								<h2><fmt:message key="report.count.headline">
									<fmt:param value="${fn:length(workBean.list)}" />
								</fmt:message></h2>
							</c:if>
							<c:if test="${svReport.showDetail[group] && fn:length(workBean.list) > 0}">
								<a class="linkDetail"
									href="<cms:link>${svReport.requestContext.uri}?report=true&detail=true</cms:link>"
									title="<fmt:message key='report.next.detail.title'/>"><fmt:message
									key="report.next.detail.headline" /></a>
							</c:if>
						</c:if> <%-- special caption for the detail page --%> <c:if
							test="${param.detail && svReport.showDetail[group]}">
							<h2><fmt:message key="report.detail.headline">
								<fmt:param value="${curPage}" />
								<fmt:param value="${fn:length(workBean.list)}" />
							</fmt:message></h2>
							<a class="linkDetail"
								href="<cms:link>${svReport.requestContext.uri}?report=true</cms:link>"
								title="<fmt:message key='report.back.overview.title'/>"><fmt:message
								key="report.back.overview.headline" /></a>
							<%@include
								file="%(link.strong:/system/modules/com.alkacon.opencms.v8.survey/elements/include_paging.jsp:a88fb03b-175a-11e1-99d4-9b778fa0dc42)"%>
						</c:if> <%-- for each field print the answers --%> <c:forEach var="field"
							items="${webform.valueList['InputField']}">
							<c:set var="fieldOptionsBean" value="${field.value.FieldParams}" />
							<c:set var="fieldOptionsStr" value="" />
							<c:if test="${fieldOptionsBean.exists}">
								<c:set var="fieldOptionsStr" value="${fieldOptionsBean.stringValue}" />
								
							</c:if>
							<% String fieldOptionsString = (String)pageContext.getAttribute("fieldOptionsStr");
							   							   
							   Map fieldOptions = CmsStringUtil.splitAsMap(fieldOptionsString, "|", "=");
							   
							   pageContext.setAttribute("fieldOptions", fieldOptions);
							   
							%>
							<c:set var="averageDigits" value="1" />
							<%
								Long averageDigits = CmsAverageUtil.parseLong((String)fieldOptions.get("averageDigits"));
								if (averageDigits != null) pageContext.setAttribute("averageDigits", averageDigits);
								
							%>

							<c:if
								test="${(param.detail && svReport.showDetail[group]) || svReport.fieldTypeCorrect[field.value['FieldType']]}">

								<%-- get the label for the field --%>
								<c:set var="labeling"
									value="${svReport.labeling[field.value['FieldLabel']]}" />
								<h3><c:out value="${labeling[0]}" escapeXml="false"/></h3>
								<br />

								<%-- is the detail page --%>
								
								<c:if test="${param.detail && svReport.showDetail[group]}">
								
									<c:set var="itemParam"
										value="${labeling[1]}${svReport.separator}${curPage}${svReport.separator}${field.value['FieldType']}" />
									<c:forEach var="item"
										items="${workBean.answerByField[itemParam]}">
										
										<div class="reportitem"><c:set var="defValue"
											value="${item}" /> <c:forTokens
											items="${field.value['FieldDefault']}" delims="|" var="def">
											<c:if
												test="${fn:contains(def, ':') && fn:substringBefore(def, ':') == item}">
												<c:set var="defValue" value="${fn:substringAfter(def, ':')}" />
											</c:if>
										</c:forTokens>
										<p class="reportanswer"><c:out value="${defValue}" /></p>
										</div>
									</c:forEach>
								</c:if>
									<c:set var="showAverage" value="${fieldOptions.showAverage}" />

								<%-- is the overview page --%>
																
								<c:if test="${(!param.detail || !svReport.showDetail[group])}">
									
									<c:if test="${showAverage eq 'true' || showAverage eq 'only'}">
										<c:set var="avg" value="${workBean.averagesForFields[labeling[1]]}" />
									
										<c:if test="${!empty avg}">
											<c:set var="displayAvg"><fmt:formatNumber value="${avg}" minFractionDigits="${averageDigits}" maxFractionDigits="${averageDigits}" /></c:set>
											<c:out value="${fieldOptions.averageText}" />											
											<c:set var="barColor" value="${content.value.Color.stringValue}" />
											<c:set var="fieldDefault" value="${field.value.FieldDefault.stringValue}" />

											<%
												String color = (String)pageContext.getAttribute("avgcolor");
												if (color == null) color = "#000000";
												String fieldDefault = (String)pageContext.getAttribute("fieldDefault");
												if (fieldDefault == null) fieldDefault = "";												
												String avg = (String)pageContext.getAttribute("avg").toString();
												String displayAvg = (String)pageContext.getAttribute("displayAvg");
												if ("desc".equals(fieldOptions.get("orientation"))) {
													out.println(CmsAverageUtil.getBarHtmlReverse(fieldDefault, avg, displayAvg, color));
												} else {
													out.println(CmsAverageUtil.getBarHtml(fieldDefault, avg, displayAvg, color));
												}
											%> 
<c:if test="${showCount == 'true'}">	
<c:set var="answerCounts" value="${workBean.answers[labeling[1]]}" />
<%
// just count all the answers for a particular question regardless of what answer was given
// 13.05.11 - AK

int answerCount = 0;
Map theAnswers = (Map)pageContext.getAttribute("answerCounts");

Iterator entries = theAnswers.entrySet().iterator();
while(entries.hasNext()) {
Map.Entry answer = (Map.Entry)entries.next();
String key = String.valueOf(answer.getKey());
if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(key)) {
	answerCount += ((Integer)(answer.getValue())).intValue();
}
}

pageContext.setAttribute("allAnswerCount", String.valueOf(answerCount));
%>
</c:if>																						
											<span class="reportcount">
											<c:if test="${showCount == 'true'}">(<c:out value="${allAnswerCount}" />)</c:if>
											</span>
											<br /><br /><br />

										
										</c:if>
										
									</c:if>
									<c:if test="${showAverage ne 'only'}">									
										<c:set var="answerCounts" value="${workBean.answers[labeling[1]]}" />
										<c:set var="answerList"	value="${fn:split(field.value['FieldDefault'], '|')}" />
	
<c:if test="${field.value['FieldType'] eq 'checkbox' }">	
<%
// this fixes the problem with the following use case:
// allowing a multiple choice with checkboxes will create a comma separated value list in case more then one entry is selected
// the bean internal parser however treats all entries as literals instead of splitting the value along the comma ","
// 11.05.11 - AK

Map correctAnswers = new HashMap();
Map theAnswers = (Map)pageContext.getAttribute("answerCounts");

Iterator entries = theAnswers.entrySet().iterator();

while(entries.hasNext()) {
Map.Entry answer = (Map.Entry)entries.next();
String key = String.valueOf(answer.getKey());
int count = ((Integer)(answer.getValue())).intValue();

String[] splitKey = key.split(",");
for (int i = 0; i < splitKey.length; i++) {
	String k = splitKey[i];
	if (k != null) {
		k = k.trim();
		if (k.length() > 0) {
			Integer oldCount = (Integer)correctAnswers.get(k);
			Integer newCount = Integer.valueOf(count);
			if (oldCount != null) {
				newCount = Integer.valueOf(count + oldCount.intValue());
			}
			correctAnswers.put(k, newCount);
		}
	}

}
}
pageContext.setAttribute("answerCounts", correctAnswers);
%>
</c:if>
										<c:set var="colorIndex" value="0" />
										<c:forEach var="item" items="${answerList}" varStatus="status">
											
											<c:set var="itemKey" value="${item}" />
											<c:if test="${fn:contains(itemKey, '%(row)')}">
												<c:set var="itemKey" value="${fn:substringAfter(itemKey, '%(row)')}" />
											</c:if>
											<c:if test="${fn:contains(itemKey, ':')}">
												<c:set var="itemKey" value="${fn:substringBefore(itemKey, ':')}" />
											</c:if>
											<c:set var="itemValue" value="${answerCounts[itemKey]}" />	
											<c:if test="${!empty itemValue}">
												<div class="reportitem">
												
												<c:set var="width" value="${ (itemValue/fn:length(workBean.list)) }" /> 													
												<c:set var="defValue" value="${itemKey}" /> 
												
												<c:forTokens items="${field.value['FieldDefault']}" delims="|" var="def">
													<c:if
														test="${fn:contains(def, ':') && fn:substringBefore(def, ':') == itemKey}">
														<c:set var="defValue"
															value="${fn:substringAfter(def, ':')}" />
													</c:if>
												</c:forTokens>
												
												<p class="reportanswer"><c:out value="${defValue}" /></p>
												
												<span class="processbar"> 
												
												<c:set var="curColor"
													value="${color[(colorIndex%fn:length(color))]}" />
													
												 <span class="bar" style="width:${width * 100}%; background-color:${curColor}; color:${svReport.textColor[curColor]};">
												
												<fmt:formatNumber value="${width}" type="percent" /> 
												</span></span></span></span>
												<span class="reportcount">
												<c:if test="${showCount == 'true'}">(<c:out value="${itemValue}" />)</c:if>
												</span>
												<br /><br />												
												</div>
												<c:set var="colorIndex" value="${colorIndex + 1}" />
											</c:if>
										</c:forEach>
									</c:if>
								</c:if>
							</c:if>
						</c:forEach></div>
					</c:if>

			<c:if
				test="${!empty content.value['AddText'] && (!param.detail || !svReport.showDetail[group])}">
				<c:out value="${content.value['AddText']}" escapeXml="false" />
			</c:if>

	</c:catch> 

	<c:if test="${empty workBean && empty error}">
		<h1><fmt:message key="report.error.headline" /></h1>
		<p><fmt:message key="report.nodatabase.text" /></p>
	</c:if>

	<c:if test="${!empty error}">
		errorpage
		<h1><fmt:message key="report.error.headline" /></h1>
		<p><fmt:message key="report.error.text" /><c:out
			value="${ error }" /></p>
	</c:if>
</div>
</cms:formatter>
</fmt:bundle>

