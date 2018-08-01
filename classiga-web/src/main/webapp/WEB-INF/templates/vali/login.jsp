<%@page session="false"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/vali" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<t:layoutBasic titleCode="title.login" >
    
    <jsp:attribute name="scripts">
        <script src="${contextPath}/resources/vali/js/jquery-validation/jquery.validate.js"></script>
        <script src="${contextPath}/resources/vali/js/jquery-validation/localization/messages_${languageCode}.js"></script>
        <script>
            $(document).ready(function () {
                $("#loginForm").validate({
                    rules: {
                        username: {
                            required: true,
                            minlength: 4
                        },
                        password: "required"
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

            });
        </script>
    </jsp:attribute>
    
    <jsp:body>
        <section class="material-half-bg">
            <div class="cover"></div>
        </section>
        <section class="login-content">
            <div class="logo">
                <h1>${appName}</h1>
            </div>
            <div class="login-box">
                <form class="login-form" id="loginForm" method="post" accept-charset="utf-8" > 
                    <h3 class="login-head" style="text-transform: uppercase;">
                        <span class="fa-stack fa-lg">
                            <i class="fa fa-circle fa-stack-2x"></i>
                            <i class="fa fa-lock fa-stack-1x fa-inverse"></i>
                        </span><br/>
                        <s:message code="label.login" />
                    </h3>
                    <div class="form-group">
                        <label class="control-label"><s:message code="label.usernameemail" /></label> 
                        <input class="form-control" type="text" placeholder="<s:message code="label.usernameemail" />" name="username" value="${username}" >
                    </div>
                    <div class="form-group">
                        <label class="control-label"><s:message code="label.password" /></label> 
                        <input class="form-control" type="password" placeholder="<s:message code="label.password" />" value="${password}" name="password">
                    </div>
                    <div class="form-group btn-container">
                        <button class="btn btn-primary btn-block">
                            <s:message code="button.login" /> <i class="fa fa-sign-in fa-lg"></i>
                        </button>
                    </div>
                </form>
            </div>
            <div class="row">
                <div class="col-md-12" style="text-align: left;margin-top: 20px;margin-bottom: 20px;color: #888;">${appName} ${appVersion} (${appBuildNumber})</div>
            </div>
        </section>
    </jsp:body>
    
</t:layoutBasic>