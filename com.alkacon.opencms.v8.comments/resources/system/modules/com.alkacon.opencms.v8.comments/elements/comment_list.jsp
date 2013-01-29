<%@ page import="com.alkacon.opencms.v8.comments.*" %>
<%@ page import="org.opencms.workplace.CmsWorkplace"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms"%><cms:secureparams />
<%
	CmsCommentsAccess alkaconCmt = new CmsCommentsAccess(pageContext, request, response, request.getParameter("configUri"));
	pageContext.setAttribute("alkaconCmt", alkaconCmt);
	if (!alkaconCmt.isUserCanView() && !alkaconCmt.isUserCanManage() && !alkaconCmt.isUserCanPost() && !alkaconCmt.getConfig().isOfferLogin()) {
		return;
	}
%>
<fmt:setLocale value="${cms:vfs(pageContext).requestContext.locale}" />
<fmt:setBundle basename="${alkaconCmt.resourceBundle}" />
<!-- start: header -->
<div class="cmtHeader">
<c:choose>
<c:when test="${not empty param.title}">
	${param.title}
</c:when>
<c:otherwise>
	<fmt:message key="titel.view.comments" />
</c:otherwise>
</c:choose>
</div>
<!-- end: header -->
<!-- start: post form link -->
<p>
<c:choose>
<c:when test="${alkaconCmt.userCanPost}">
	    <a 
	       title="<fmt:message key="form.message.post" />" 
	       href="<cms:link>%(link.weak:/system/modules/com.alkacon.opencms.v8.comments/elements/comment_form.jsp:562b63fd-15df-11e1-aeb4-9b778fa0dc42)?cmturi=${param.cmturi}&cmtminimized=${param.cmtminimized}&cmtlist=${param.cmtlist}&cmtsecurity=${param.cmtsecurity}&configUri=${param.configUri}&__locale=${cms:vfs(pageContext).requestContext.locale}&width=800&height=530</cms:link>" 
	       class="cmt_thickbox" >
			<fmt:message key="post.0" />
		</a>
	</a>
</c:when>
<c:otherwise>
	<c:if test="${alkaconCmt.guestUser && alkaconCmt.config.offerLogin}">
	        <a 
	           title="<fmt:message key="login.message.title" />" 
	           href="<cms:link>%(link.weak:/system/modules/com.alkacon.opencms.v8.comments/elements/comment_login.jsp:563c05e2-15df-11e1-aeb4-9b778fa0dc42)?cmturi=${param.cmturi}&cmtminimized=${param.cmtminimized}&cmtlist=${param.cmtlist}&cmtsecurity=${param.cmtsecurity}&configUri=${param.configUri}&__locale=${cms:vfs(pageContext).requestContext.locale}&width=400&height=200</cms:link>" 
	           class="cmt_thickbox" >
			<fmt:message key="post.user.login.0" />
		</a>
	</c:if>
</c:otherwise>
</c:choose>
<script type="text/javascript">
  $('a.cmt_thickbox').colorbox(colorboxConfig_comments); //pass where to apply thickbox
  imgLoader = new Image(); // preload image
  imgLoader.src = '<%=CmsWorkplace.getSkinUri()%>jquery/css/thickbox/loading.gif';
  
  function reloadComments(state, page) {
  	// empty
  }
</script>
<!-- end: post form link -->
<% if (!alkaconCmt.isUserCanView()) { %>
<script type="text/javascript">
  function reloadComments(state, page) {
  	// empty
  }
</script>
<%	return;
}
%>
<!-- start: comments list -->
<p>
<div id="comments" style="width: 100%;">
	<cms:include file="%(link.strong:/system/modules/com.alkacon.opencms.v8.comments/elements/comment_innerlist.jsp:5634d9e8-15df-11e1-aeb4-9b778fa0dc42)">
		<cms:param name="cmturi" value="${param.cmturi}" />
		<cms:param name="cmtminimized" value="${param.cmtminimized}" />
	    <cms:param name="cmtlist" value="${param.cmtlist}" />
	    <cms:param name="cmtsecurity" value="${param.cmtsecurity}" />
		<cms:param name="configUri" value="${param.configUri}" />
		<cms:param name="cmtpage" value="0" />
	</cms:include>
</div>
<script type="text/javascript">
function reloadComments(state, page) {

	$('body').css("cursor", "wait");
	var data = { 
		        cmturi: '${param.cmturi}',
		        cmtminimized:"${param.cmtminimized}",
		        cmtlist:"${param.cmtlist}",
		        cmtsecurity:"${param.cmtsecurity}",
		        configUri: '${param.configUri}', 
		        __locale: '<cms:info property="opencms.request.locale" />', 
		        cmtstate: '${alkaconCmt.state}', 
		        cmtpage: 0 
	        }; 
	if (state !== "undefined" && page !== "undefined") {
		data.cmtstate= state; 
		data.cmtpage= page;
	} else if (state !== "undefined") {
	    data.cmtstate= state; 
		data.cmtpage= 0;
	}
	$.post(
		'<cms:link>%(link.weak:/system/modules/com.alkacon.opencms.v8.comments/elements/comment_innerlist.jsp:5634d9e8-15df-11e1-aeb4-9b778fa0dc42)</cms:link>',
		data,
		function(html){ $("#comments").html(html); }
	);
	$('body').css("cursor", "auto");
}
</script>
<!-- end: comments list -->
