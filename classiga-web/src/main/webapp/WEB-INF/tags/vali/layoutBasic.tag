<%@tag description="Layout Tag" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>

<%@attribute name="styles" fragment="true" %>
<%@attribute name="scripts" fragment="true" %>

<%@attribute name="languageCode" required="true" %>
<%@attribute name="titleCode" required="true" %>
<%@attribute name="showAppInfo" required="true" %>
<%@attribute name="appName" required="true" %>
<%@attribute name="appVersion" required="true" %>
<%@attribute name="notify" type="java.lang.Object" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" scope="request" />

<%-- Read https://stackoverflow.com/questions/1296235/jsp-tricks-to-make-templating-easier --%>

<!DOCTYPE html>
<html lang="${languageCode}" >
    <head>
        <c:if test="${showAppInfo}">
        <title><s:message code="${titleCode}" /> - ${appName}</title>
        </c:if>
        <c:if test="${not showAppInfo}">
        <title><s:message code="${titleCode}" /></title>
        </c:if>
        <%-- 
        http://www.mkyong.com/spring-mvc/spring-mvc-how-to-include-js-or-css-files-in-a-jsp-page/ 
        --%>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/vali/css/vali.css" />
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/common/css/overrides.css" />
        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries-->
        <!--if lt IE 9
        script(src='https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js')
        script(src='https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js')
        -->
        <jsp:invoke fragment="styles"/>
    </head>
    <body>
        <jsp:doBody />
        <!-- Javascripts-->
        <script src="${contextPath}/resources/vali/js/jquery-2.1.4.min.js"></script>
        <script src="${contextPath}/resources/vali/js/essential-plugins.js"></script>
        <script src="${contextPath}/resources/vali/js/bootstrap.min.js"></script>
        <script src="${contextPath}/resources/vali/js/plugins/pace.min.js"></script>
        <script src="${contextPath}/resources/vali/js/plugins/bootstrap-notify.min.js"></script>
        <script src="${contextPath}/resources/vali/js/vali.js"></script>
        <script src="${contextPath}/resources/common/js/utils.js"></script>
        <script>
            $(document).ready(function() {
                <c:if test="${not empty notify}">
                $.notify({
                    title : "${notify.title}",
                    message : "${notify.message}",
                    icon : ""
                }, {
                    type : "${notify.type}",
                    delay : 0
                });
                </c:if>
                $('[data-toggle="tooltip"]').tooltip({
                    trigger : 'hover'
                });
            });
        </script>
        <jsp:invoke fragment="scripts"/>
    </body>
</html>
