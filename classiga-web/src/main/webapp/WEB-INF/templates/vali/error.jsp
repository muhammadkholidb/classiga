<%@ page session="false"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="titleCode" value="title.error" scope="request" />

<!DOCTYPE html>
<html lang="${languageCode}">
<head>
<jsp:include page="includes/_1_inc_head_main.jsp" />
</head>
<body>
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

    <jsp:include page="includes/_4_inc_bottom.jsp" />

</body>
</html>