<%@page session="false"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/vali" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<t:layoutAdmin titleCode="title.changepassword" >
    
    <jsp:attribute name="styles">
        
    </jsp:attribute>
    
    <jsp:attribute name="scripts">
        <script src="${contextPath}/resources/vali/js/jquery-validation/jquery.validate.js"></script>
        <script src="${contextPath}/resources/vali/js/jquery-validation/localization/messages_${languageCode}.js"></script>
        <script>
    		utils.jqueryValidate("#formChangePassword", {
                oldPassword: {
                    required: true,
                    minlength: 4
                },
                newPassword: {
                    required: true,
                    minlength: 4
                },
                newPasswordConfirm: {
                    required: true,
                    minlength: 4,
                    equalTo: "#inputNewPassword"
                }
            });
    		$(document).ready(function() {
                <c:if test="${logout}">
                setTimeout(function() {
                    var href = $('#btnLogout').attr('href');
                    window.location.href = href;
                }, 2000);
                </c:if>
            });
        </script>
    </jsp:attribute>
    
    <jsp:body>
        <div class="content-wrapper">
            <div class="page-title">
                <div>
                    <h1>
                        <i class="fa fa-lock"></i>
                        <s:message code="title.changepassword" />
                    </h1>
                </div>
            </div>
            <form id="formChangePassword" class="form-horizontal" method="post" accept-charset="utf-8">
            <div class="row">
                <div class="col-md-8">
                    <div class="card">
                        <h3 class="card-title"><s:message code="label.changepassword" /></h3>
                        <div class="card-body">
                            <div class="form-group required">
                                <label class="control-label col-md-4" for="inputOldPassword"><s:message code="label.oldpassword" /></label>
                                <div class="col-md-8">
                                    <input class="form-control" type="password" placeholder="<s:message code="label.oldpassword" />" name="oldPassword" value="${oldPassword}" id="inputOldPassword" >
                                </div>
                            </div>
                            <div class="form-group required">
                                <label class="control-label col-md-4" for="inputNewPassword"><s:message code="label.newpassword" /></label>
                                <div class="col-md-8">
                                    <input class="form-control" type="password" placeholder="<s:message code="label.newpassword" />" name="newPassword" value="${newPassword}" id="inputNewPassword" >
                                </div>
                            </div>
                            <div class="form-group required">
                                <label class="control-label col-md-4" for="inputNewPasswordConfirm"><s:message code="label.newpasswordconfirm" /></label>
                                <div class="col-md-8">
                                    <input class="form-control" type="password" placeholder="<s:message code="label.newpasswordconfirm" />" name="newPasswordConfirm" value="${newPasswordConfirm}" id="inputNewPasswordConfirm" >
                                </div>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary icon-btn" ><s:message code="button.save" /></button>
                        <a href="javascript:history.back()" class="btn btn-default icon-btn" id="btnCancel" ><s:message code="button.cancel" /></a>
                    </div>
                </div>
            </div>
            </form>
        </div>
    </jsp:body>
    
</t:layoutAdmin>
