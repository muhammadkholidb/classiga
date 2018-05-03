<%@page session="false"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/vali" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<t:layoutAdmin titleCode="title.system" >
    
    <jsp:attribute name="styles">
        
    </jsp:attribute>
    
    <jsp:attribute name="scripts">
        <script src="${contextPath}/resources/vali/js/plugins/select2.min.js"></script>
        <script>
            $("#inputLanguage, #inputOnline").select2({
                width: '100%',
                tags: true
            });
        </script>
    </jsp:attribute>
    
    <jsp:body>
        <div class="content-wrapper">
            <div class="page-title">
                <div>
                    <h1>
                        <i class="${currentMenu.faIcon}"></i>
                        <s:message code="title.system" />
                    </h1>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="card">
                        <h3 class="card-title"><s:message code="label.systemsetting" /></h3>
                        <div class="card-body">
                            <form class="form-horizontal" method="post" action="${contextPath}/settings/system/edit" accept-charset="utf-8">
                                <div class="form-group">
                                    <label class="col-lg-2 control-label" for="inputLanguage"><s:message code="label.language" /></label>
                                    <div class="col-lg-10">
                                        <select class="form-control" id="inputLanguage" name="languageCode" ${isEdit ? '' : 'disabled' }>
                                            <c:forEach items="${languages}" var="language">
                                                <c:choose>
                                                    <c:when test="${language.code eq currentLanguageCode}">
                                                    <option value="${language.code}" selected>${language.name}</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                    <option value="${language.code}">${language.name}</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <%-- 
                                <div class="form-group">
                                    <label class="col-lg-2 control-label" for="inputTemplate"><s:message code="label.template" /></label>
                                    <div class="col-lg-10">
                                        <select class="form-control" id="inputTemplate" name="templateCode" disabled>
                                            <c:forEach items="${templates}" var="template">
                                                <c:choose>
                                                    <c:when test="${template.code eq currentTemplateCode}">
                                                    <option value="${template.code}" selected>${template.name}</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                    <option value="${template.code}">${template.name}</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                --%>
                                <div class="form-group">
                                    <label class="col-lg-2 control-label" for="inputOnline"><s:message code="label.online" /></label>
                                    <div class="col-lg-10">
                                        <select class="form-control" id="inputOnline" name="online" ${isEdit ? '' : 'disabled' }>
                                            <c:choose>
                                                <c:when test="${currentOnline eq 'y'}">
                                                <option value="y" selected><s:message code="label.yes" /></option>
                                                <option value="n"><s:message code="label.no" /></option>
                                                </c:when>
                                                <c:otherwise>
                                                <option value="y"><s:message code="label.yes" /></option>
                                                <option value="n" selected><s:message code="label.no" /></option>
                                                </c:otherwise>
                                            </c:choose>
                                        </select>
                                    </div>
                                </div>
                                <c:choose>
                                    <c:when test="${isEdit}">
                                        <button type="submit" class="btn btn-primary icon-btn" id="btnSave" ><s:message code="button.save" /></button>
                                        <a href="${contextPath}/settings/system" class="btn btn-default icon-btn" id="btnCancel" ><s:message code="button.cancel" /></a>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${canModify}">
                                        <a href="${contextPath}/settings/system/edit" class="btn btn-primary" ><s:message code="button.edit" /></a>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
    
</t:layoutAdmin>
