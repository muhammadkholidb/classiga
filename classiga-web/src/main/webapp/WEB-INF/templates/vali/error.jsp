<%@page session="false"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/vali" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="titleCode" value="title.error" scope="request" />

<t:layoutBasic 
    languageCode="${languageCode}" 
    titleCode="${titleCode}" 
    showAppInfo="${showAppInfo}" 
    appName="${appName}" 
    appVersion="${appVersion}" 
    notify="${notify}" >
    
    <jsp:body>
        <div class="page-error">
            <h1>
                ${errorCode}
            </h1>
            <p>${errorMessage}</p>
            <p>
                <a class="btn btn-default icon-btn" href="javascript:window.history.back()">
                    <i class="fa fa-fw fa-lg fa-arrow-circle-left "></i><s:message code="button.back" />
                </a>
            </p>
        </div>
    </jsp:body>

</t:layoutBasic>
