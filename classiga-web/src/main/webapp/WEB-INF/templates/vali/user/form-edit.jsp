<%@page session="false"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/vali" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<t:layoutAdmin titleCode="title.user" >
    
    <jsp:attribute name="styles">
        
    </jsp:attribute>
    
    <jsp:attribute name="scripts">
        <script src="${contextPath}/resources/vali/js/jquery-validation/jquery.validate.js"></script>
        <script src="${contextPath}/resources/vali/js/jquery-validation/localization/messages_${languageCode}.js"></script>
        <script>
            utils.jqueryValidate("#formEditUser", {
                fullName: {
                    required: true,
                    minlength: 4
                },
                username: {
                    required: true,
                    minlength: 4
                },
                email: {
                    required: true,
                    email: true
                },
                password: {
                    minlength: 4
                },
                userGroupId: "required"
            });
            
            $('#btnRandom').on('click', function () {
                $('#inputPassword').val(utils.randomAlphanumeric(8));
                $('#inputPassword').trigger('keyup');
            });
        </script>
    </jsp:attribute>
    
    <jsp:body>
        <div class="content-wrapper">
            <div class="page-title">
                <div>
                    <h1>
                        <i class="${currentMenu.faIcon}"></i>
                        <s:message code="title.user" />
                    </h1>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="card">
                        <h3 class="card-title"><s:message code="label.edituser" /></h3>
                        <div class="card-body">
                            <form id="formEditUser" class="form-horizontal" method="post" action="${contextPath}/settings/user/edit/${userId}" accept-charset="utf-8">
                                <div class="form-group required">
                                    <label class="control-label col-md-3" for="inputFullName"><s:message code="label.fullname" /></label>
                                    <div class="col-md-9">
                                        <input class="form-control" type="text" placeholder="<s:message code="label.fullname" />" name="fullName" value="${fullName}" id="inputFullName" >
                                    </div>
                                </div>
                                <div class="form-group required">
                                    <label class="control-label col-md-3" for="inputUsername"><s:message code="label.username" /></label>
                                    <div class="col-md-9">
                                        <input class="form-control" type="text" placeholder="<s:message code="label.username" />" name="username" value="${username}" id="inputUsername" >
                                    </div>
                                </div>
                                <div class="form-group required">
                                    <label class="control-label col-md-3" for="inputEmail"><s:message code="label.email" /></label>
                                    <div class="col-md-9">
                                        <input class="form-control" type="text" placeholder="<s:message code="label.email" />" name="email" value="${email}" id="inputEmail" >
                                    </div>
                                </div>
                                <div class="form-group required">
                                    <label class="control-label col-md-3" for="inputPassword"><s:message code="label.password" /></label>
                                    <div class="col-md-9">
                                        <div class="input-group">
                                            <input class="form-control" type="text" placeholder="<s:message code="label.password" />" name="password" value="${password}" id="inputPassword" />
                                            <span class="input-group-btn"><button class="btn btn-success" type="button" id="btnRandom" ><s:message code="button.random" /></button></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group required">
                                    <label class="col-md-3 control-label" for="inputUserGroupId"><s:message code="label.usergroup" /></label>
                                    <div class="col-md-9">
                                        <select class="form-control" id="inputUserGroupId" name="userGroupId">
                                            <c:forEach items="${userGroups}" var="userGroup">
                                                <c:choose>
                                                    <c:when test="${userGroup.id eq userGroupId}">
                                                        <option value="${userGroup.id}" selected>${userGroup.name}</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="${userGroup.id}">${userGroup.name}</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group required">
                                    <label class="col-md-3 control-label" for="inputActive"><s:message code="label.active" /></label>
                                    <div class="col-md-9">
                                        <select class="form-control" id="inputActive" name="active" >
                                            <c:choose>
                                                <c:when test="${active eq 'y'}">
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
                                <button type="submit" class="btn btn-primary icon-btn" ><s:message code="button.save" /></button>
                                <a href="${contextPath}/settings/user" class="btn btn-default icon-btn" id="btnCancel" ><s:message code="button.cancel" /></a>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
    
</t:layoutAdmin>
