<%@ page session="false"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="titleCode" value="title.user" scope="request" />

<!DOCTYPE html>
<html lang="${languageCode}">
    <head>
        <jsp:include page="../includes/_1_inc_head_main.jsp" />
        <%-- Put your additional head content (css) here --%>

    </head>

    <body class="sidebar-mini fixed">
        <div class="wrapper">
            <jsp:include page="../includes/_2_inc_top.jsp" />
            <jsp:include page="../includes/_3_inc_side.jsp" />

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
                                <form id="formAddUser" class="form-horizontal" method="post" action="${contextPath}/settings/user/edit/${userId}">
                                    <div class="form-group required">
                                        <label class="control-label col-md-3" for="inputFullName"><s:message code="label.fullname" /></label>
                                        <div class="col-md-9">
                                            <input class="form-control" type="text" placeholder="<s:message code="label.fullname" />" name="fullName" value="${fullName}" id="inputFullName" autofocus>
                                        </div>
                                    </div>
                                    <div class="form-group required">
                                        <label class="control-label col-md-3" for="inputUsername"><s:message code="label.username" /></label>
                                        <div class="col-md-9">
                                            <input class="form-control" type="text" placeholder="<s:message code="label.username" />" name="username" value="${username}" id="inputUsername" autofocus>
                                        </div>
                                    </div>
                                    <div class="form-group required">
                                        <label class="control-label col-md-3" for="inputEmail"><s:message code="label.email" /></label>
                                        <div class="col-md-9">
                                            <input class="form-control" type="text" placeholder="<s:message code="label.email" />" name="email" value="${email}" id="inputEmail" autofocus>
                                        </div>
                                    </div>
                                    <div class="form-group required">
                                        <label class="control-label col-md-3" for="inputPassword"><s:message code="label.password" /></label>
                                        <div class="col-md-9">
                                            <div class="input-group">
                                                <input class="form-control" type="text" placeholder="<s:message code="label.password" />" name="password" value="${password}" id="inputPassword" autofocus />
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
                                    <div class="form-group">
                                        <div class="col-md-9 col-md-offset-3" >
                                            <button type="submit" class="btn btn-primary icon-btn" ><s:message code="button.save" /></button>
                                            <a href="${contextPath}/settings/user" class="btn btn-default icon-btn" id="btnCancel" ><s:message code="button.cancel" /></a>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <jsp:include page="../includes/_4_inc_bottom.jsp" />
        <%-- Put your additional content (js) here --%>

        <script src="${contextPath}/resources/vali/js/jquery-validation/jquery.validate.js"></script>
        <script src="${contextPath}/resources/vali/js/jquery-validation/localization/messages_${languageCode}.js"></script>
        <script>

            $("#formAddUser").validate({
                rules: {
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
                },
                errorElement: "em",
                errorPlacement: function (error, element) {
                    // Add the 'help-block' class to the error element
                    error.addClass("help-block");

                    if (element.prop("type") === "checkbox") {
                        error.insertAfter(element.parent("label"));
                    } else {
                        error.insertAfter(element);
                    }
                },
                highlight: function (element, errorClass, validClass) {
                    $(element).parents(".form-group").addClass("has-error");
                },
                unhighlight: function (element, errorClass, validClass) {
                    $(element).parents(".form-group").removeClass("has-error");
                }
            });

            $('#btnRandom').on('click', function () {
                $('#inputPassword').val(utils.randomAlphanumeric(8));
                $('#inputPassword').trigger('keyup');
            });
        </script>

    </body>
</html>