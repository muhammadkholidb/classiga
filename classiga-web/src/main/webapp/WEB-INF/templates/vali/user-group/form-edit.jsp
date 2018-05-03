<%@page session="false"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/vali" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<t:layoutAdmin titleCode="title.usergroup" >
    
    <jsp:attribute name="styles">
        
    </jsp:attribute>
    
    <jsp:attribute name="scripts">

        <script src="${contextPath}/resources/vali/js/jquery-validation/jquery.validate.js"></script>
        <script src="${contextPath}/resources/vali/js/jquery-validation/localization/messages_${languageCode}.js"></script>
        <script>
            utils.jqueryValidate("#formEditUserGroup", {
                name: {
                    required: true,
                    minlength: 4
                },
                description: {
                    maxlength: 512
                }
            });
            
            $("#formEditUserGroup").submit(function() {
                var menuPermissions = [];
                $("#tableMenuPermissions tbody tr").each(function() {
                    menuPermissions.push({
                        menuCode: $(this).find("input[type=hidden][class=menu-code]").val(),
                        canView: $(this).find("input[type=checkbox][class=can-view]").prop("checked") ? "y" : "n",
                        canModify: $(this).find("input[type=checkbox][class=can-modify]").prop("checked") ? "y" : "n"
                    });
                });
                $(this).append('<input type="hidden" name="menuPermissions" value="' + encodeURIComponent(JSON.stringify(menuPermissions)) + '">');
            });
            
            $("input[type=checkbox][class=can-view]").on("click", function() {
                var checked = $(this).prop("checked");
                var tr = $(this).closest("tr");
                var code = tr.find("input[type=hidden][class=menu-code]").val();
                var parentCode = tr.find("input[type=hidden][class=menu-parent-code]").val();
                var isParent = (parentCode === undefined) || (parentCode.length === 0);
                if (isParent) {
                    
                    if (checked) {
                        
                        // If parent and checked, set checked to all its children with class can-view
                        tr.closest("table#tableMenuPermissions tbody")
                                .find("input[type=hidden][value='" + code + "']")
                                .closest("td")
                                .find("input[type=checkbox][class=can-view]")
                                .prop("checked", checked);
                    } else {
                        
                        // If parent and not checked, set unchecked to all its children with class can-view and can-modify
                        tr.closest("table#tableMenuPermissions tbody")
                                .find("input[type=hidden][value='" + code + "']")
                                .closest("tr")
                                .find("input[type=checkbox][class=can-view], input[type=checkbox][class=can-modify]")
                                .prop("checked", checked);
                    }
                    
                } else {
                    
                    if (checked) {

                        tr.closest("table#tableMenuPermissions tbody")
                                .find("input[type=hidden][class=menu-code][value='" + parentCode + "']")
                                .closest("tr")
                                .find("input[type=checkbox][class=can-view]")
                                .prop("checked", checked);

                    } else {
                        
                        tr.find("input[type=checkbox][class=can-modify]").prop("checked", checked);

                        var checkedChildrenView = tr.closest("table#tableMenuPermissions tbody")
                                .find("input[type=hidden][class=menu-parent-code][value='" + parentCode + "']")
                                .closest("tr")
                                .find("input[type=checkbox][class=can-view]:checked");

                        if (checkedChildrenView.length === 0) {

                            tr.closest("table#tableMenuPermissions tbody")
                                    .find("input[type=hidden][class=menu-code][value='" + parentCode + "']")
                                    .closest("tr")
                                    .find("input[type=checkbox][class=can-view]")
                                    .prop("checked", checked);
                        }
                        
                        var checkedChildrenModify = tr.closest("table#tableMenuPermissions tbody")
                                .find("input[type=hidden][class=menu-parent-code][value='" + parentCode + "']")
                                .closest("tr")
                                .find("input[type=checkbox][class=can-modify]:checked");

                        if (checkedChildrenModify.length === 0) {
                            
                            tr.closest("table#tableMenuPermissions tbody")
                                    .find("input[type=hidden][class=menu-code][value='" + parentCode + "']")
                                    .closest("tr")
                                    .find("input[type=checkbox][class=can-modify]")
                                    .prop("checked", checked);
                        }
                    }
                }
            });
            
            $("input[type=checkbox][class=can-modify]").on("click", function() {
                var checked = $(this).prop("checked");
                var tr = $(this).closest("tr");
                var code = tr.find("input[type=hidden][class=menu-code]").val();
                var parentCode = tr.find("input[type=hidden][class=menu-parent-code]").val();
                var isParent = (parentCode === undefined) || (parentCode.length === 0);
                if (isParent) {
                    
                    if (checked) {
                        
                        tr.closest("table#tableMenuPermissions tbody")
                                .find("input[type=hidden][value='" + code + "']")
                                .closest("tr")
                                .find("input[type=checkbox][class=can-view], input[type=checkbox][class=can-modify]")
                                .prop("checked", checked);
                    } else {
                        
                        tr.closest("table#tableMenuPermissions tbody")
                                .find("input[type=hidden][value='" + code + "']")
                                .closest("tr")
                                .find("input[type=checkbox][class=can-modify]")
                                .prop("checked", checked);
                    }
                    
                } else {
                    
                    if (checked) {
                            
                        tr.find("input[type=checkbox][class=can-view]").prop("checked", checked);

                        tr.closest("table#tableMenuPermissions tbody")
                                .find("input[type=hidden][class=menu-code][value='" + parentCode + "']")
                                .closest("tr")
                                .find("input[type=checkbox][class=can-view], input[type=checkbox][class=can-modify]")
                                .prop("checked", checked);

                    } else {
                        
                        var checkedChildrenModify = tr.closest("table#tableMenuPermissions tbody")
                                .find("input[type=hidden][class=menu-parent-code][value='" + parentCode + "']")
                                .closest("tr")
                                .find("input[type=checkbox][class=can-modify]:checked");

                        if (checkedChildrenModify.length === 0) {
                            
                            tr.closest("table#tableMenuPermissions tbody")
                                    .find("input[type=hidden][class=menu-code][value='" + parentCode + "']")
                                    .closest("tr")
                                    .find("input[type=checkbox][class=can-modify]")
                                    .prop("checked", checked);
                        }   
                    }
                }
            });
        </script>
        
        <script src="${contextPath}/resources/vali/js/plugins/jquery.dataTables.min.js"></script>
        <script src="${contextPath}/resources/vali/js/plugins/dataTables.bootstrap.min.js"></script>
        <script src="${contextPath}/resources/vali/js/plugins/dataTables.responsive.min.js"></script>
        <script>
            var table = $('#tableMenuPermissions').DataTable({
                responsive: true,
                searching: false,
                paging:   false,
                ordering: false,
                info: false,
                language: utils.dataTables.language['${languageCode}']
            });
        </script>
        <script src="${contextPath}/resources/vali/js/plugins/select2.min.js"></script>
        <script>
            $("#inputActive").select2({
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
                        <s:message code="title.usergroup" />
                    </h1>
                </div>
            </div>
            <div class="row">
                <div class="col-md-8">
                    <div class="card">
                        <h3 class="card-title"><s:message code="label.addusergroup" /></h3>
                        <div class="card-body">
                            <form id="formEditUserGroup" class="form-horizontal" method="post" action="${contextPath}/settings/user-group/edit/${userGroupId}" accept-charset="utf-8">
                                <div class="form-group required">
                                    <label class="control-label col-md-3" for="inputName"><s:message code="label.name" /></label>
                                    <div class="col-md-9">
                                        <input class="form-control" type="text" placeholder="<s:message code="label.name" />" name="name" value="${name}" id="inputName" >
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3" for="inputDescription"><s:message code="label.description" /></label>
                                    <div class="col-md-9">
                                        <textarea class="form-control" placeholder="<s:message code="label.description" />" name="description" id="inputDescription" >${description}</textarea>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3" ><s:message code="label.menupermissions" /></label>
                                    <div class="col-md-9">
                                        <table class="table table-bordered table-hover table-striped display " id="tableMenuPermissions" style="width: 100%">
                                            <thead>
                                                <tr>
                                                    <th data-priority="1"><s:message code="label.name" /></th>
                                                    <th data-priority="4"><s:message code="label.description" /></th>
                                                    <th data-priority="2" class="align-center"><s:message code="label.view" /></th>
                                                    <th data-priority="3" class="align-center"><span data-toggle="tooltip" title="" data-original-title="<s:message code="label.modify.info" />"><s:message code="label.modify" /></span></th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${menuPermissions}" var="menu">
                                                    <tr>
                                                        <td>${menu.name}</td>
                                                        <td>${menu.description}</td>
                                                        <td class="align-center">
                                                            <input type="hidden" value="${menu.code}" class="menu-code" />
                                                            <input type="hidden" value="${menu.parentCode}" class="menu-parent-code" />
                                                            <span class="animated-checkbox">
                                                                <label>
                                                                    <input type="checkbox" class="can-view" ${menu.canView ? 'checked' : ''} > 
                                                                    <span class="label-text"></span>
                                                                </label>
                                                            </span>
                                                        </td>
                                                        <td class="align-center">
                                                            <span class="animated-checkbox">
                                                                <label>
                                                                    <input type="checkbox" class="can-modify" ${menu.canModify ? 'checked' : ''} > 
                                                                    <span class="label-text"></span>
                                                                </label>
                                                            </span>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
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
                                <a href="${contextPath}/settings/user-group" class="btn btn-default icon-btn" id="btnCancel" ><s:message code="button.cancel" /></a>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
    
</t:layoutAdmin>
