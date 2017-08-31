<%@ page session="false"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" scope="request" />

<%-- 
http://www.mkyong.com/spring-mvc/spring-mvc-how-to-include-js-or-css-files-in-a-jsp-page/ 
--%>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="${contextPath}/res/${templateCode}/css/vali.css" />
<link rel="stylesheet" type="text/css" href="${contextPath}/res/common/css/overrides.css" />
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries-->
<!--if lt IE 9
script(src='https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js')
script(src='https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js')
-->

<c:if test="${showAppInfo}">
    <title><s:message code="${titleCode}" /> :: ${appName} ${appVersion}</title>
</c:if>
<c:if test="${not showAppInfo}">
    <title><s:message code="${titleCode}" /></title>
</c:if>
