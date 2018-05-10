<%@page session="false"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/vali" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<t:layoutAdmin titleCode="title.editprofile" >
    
    <jsp:attribute name="styles">
        
    </jsp:attribute>
    
    <jsp:attribute name="scripts">
        <script src="${contextPath}/resources/vali/js/jquery-validation/jquery.validate.min.js"></script>
        <script src="${contextPath}/resources/vali/js/jquery-validation/localization/messages_${languageCode}.js"></script>
        <script>
            // Accept a value from a file input based on a required mimetype
            $.validator.addMethod( "accept", function( value, element, param ) {
                
                // Split mime on commas in case we have multiple types we can accept
                var typeParam = typeof param === "string" ? param.replace( /\s/g, "" ) : "image/*",
                	optionalValue = this.optional( element ),
                	i, file, regex;
                
                // Element is optional
                if ( optionalValue ) {
                	return optionalValue;
                }
                
                if ( $( element ).attr( "type" ) === "file" ) {
                
                	// Escape string to be used in the regex
                	// see: http://stackoverflow.com/questions/3446170/escape-string-for-use-in-javascript-regex
                	// Escape also "/*" as "/.*" as a wildcard
                	typeParam = typeParam
                			.replace( /[\-\[\]\/\{\}\(\)\+\?\.\\\^\$\|]/g, "\\$&" )
                			.replace( /,/g, "|" )
                			.replace( /\/\*/g, "/.*" );
                
                	// Check if the element has a FileList before checking each file
                	if ( element.files && element.files.length ) {
                		regex = new RegExp( ".?(" + typeParam + ")$", "i" );
                		for ( i = 0; i < element.files.length; i++ ) {
                			file = element.files[ i ];
                
                			// Grab the mimetype from the loaded file, verify it matches
                			if ( !file.type.match( regex ) ) {
                				return false;
                			}
                		}
                	}
                }
                
                // Either return true because we've validated each file, or because the
                // browser does not support element.files and the FileList feature
                return true;
            }, $.validator.messages.accept);
         	
            // Add validation file upload size https://github.com/jquery-validation/jquery-validation/blob/master/src/additional/maxsize.js
            $.validator.addMethod( "maxsize", function( value, element, param ) {
                if ( this.optional( element ) ) {
                    return true;
                }
                if ( $( element ).attr( "type" ) === "file" ) {
                    if ( element.files && element.files.length ) {
                        for ( var i = 0; i < element.files.length; i++ ) {
                            if ( element.files[ i ].size > param ) {
                                return false;
                            }
                        }
                    }
                }
                return true;
            }, $.validator.messages.maxsize);
            
            utils.jqueryValidate("#formEditProfile", {
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
                avatar: {
                    accept: "image/*",
                    maxsize: 409600
                }
            });
        </script>
    </jsp:attribute>
    
    <jsp:body>
        <div class="content-wrapper">
            <div class="page-title">
                <div>
                    <h1>
                        <i class="fa fa-user"></i>
                        <s:message code="title.editprofile" />
                    </h1>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="card">
                        <h3 class="card-title"><s:message code="label.editprofile" /></h3>
                        <div class="card-body">
                            <form id="formEditProfile" class="form-horizontal" method="post" accept-charset="utf-8" enctype="multipart/form-data" >
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
                                <div class="form-group">
                                    <label class="control-label col-md-3"><s:message code="label.avatar" /></label>
                                    <div class="col-md-9">
                                        <input class="form-control" type="file" name="avatar" >
                                    </div>
                                </div>
                                <button type="submit" class="btn btn-primary icon-btn" ><s:message code="button.save" /></button>
                                <a href="javascript:history.back()" class="btn btn-default icon-btn" id="btnCancel" ><s:message code="button.cancel" /></a>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
    
</t:layoutAdmin>
