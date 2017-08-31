<%@ page session="false"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="titleCode" value="title.user" scope="request" />

<!DOCTYPE html>
<html lang="${languageCode}" >
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
                    <div>
                        <a class="btn btn-primary " href="${contextPath}/settings/user/add" data-toggle="tooltip" data-placement="top" title="<s:message code="button.add" />">
                            <i class="fa fa-lg fa-plus"></i>
                        </a>
                        <a id="btnDeleteSelected" class="btn btn-danger" href="" data-toggle="tooltip" data-placement="top" title="<s:message code="button.delete" />">
                            <i class="fa fa-lg fa-trash"></i>
                        </a>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="card">
                            <h3 class="card-title"><s:message code="label.userlist" /></h3>
                            <div class="card-body">
                                <div class="table-responsive">
                                <table class="table table-hover table-striped display nowrap" id="tableUser" style="width: 100%">
                                    <thead>
                                        <tr>
                                            <th class="align-center" >
                                                <div class="animated-checkbox">
                                                    <label>
                                                        <input type="checkbox" onclick="$('input[name=\'selected\']').prop('checked', this.checked);" >
                                                        <span class="label-text"></span>
                                                    </label>
                                                </div>
                                            </th>
                                            <th><s:message code="label.name" /></th>
                                            <th><s:message code="label.username" /></th>
                                            <th><s:message code="label.email" /></th>
                                            <th><s:message code="label.active" /></th>
                                            <th><s:message code="label.group" /></th>
                                            <th><s:message code="label.action" /></th>
                                        </tr>
                                    </thead>
                                </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="../includes/_4_inc_bottom.jsp" />
        <%-- Put your additional content (js) here --%>

        <script src="${contextPath}/res/${templateCode}/js/plugins/jquery.dataTables.min.js"></script>
        <script src="${contextPath}/res/${templateCode}/js/plugins/dataTables.bootstrap.min.js"></script>
        <script src="${contextPath}/res/${templateCode}/js/plugins/sweetalert.min.js"></script>
        <script>
            utils.dataTables.language['${languageCode}']['sInfoFiltered'] = "";
            utils.dataTables.language['${languageCode}']['sSearch'] = "";
            utils.dataTables.language['${languageCode}']['sLengthMenu'] = "_MENU_";
            var table = $('#tableUser').DataTable({
                processing: true,
                serverSide: true,
                ajax: {
                    url: "${contextPath}/ajax/settings/user",
                    data: function(d) {
                        d.sortColumnIndex = d.order[0].column;
                        d.sortOrder = d.order[0].dir;
                        d.searchTerm = d.search.value;
                    }
                },
                columns: [
                { 
                    data: null, 
                    render: function ( data, type, row, meta ) {
                        var cb = '<div class="animated-checkbox"><label><input type="checkbox" name="selected" value="' + row.id + '" ><span class="label-text"></span></label></div>';
                        return cb;
                    },
                    orderable: false,
                    className: "align-center"
                },
                { 
                    data: "firstName",
                    render: function ( data, type, row, meta ) {
                        return row.firstName + " " + row.lastName;
                    }
                },
                { data: "username" },
                { data: "email" },
                { 
                    data: "active",
                    render: function ( data, type, row, meta ) {
                        if (row.active === "y") {
                            return '<h4><span class="label label-info"><s:message code="label.yes" /></span></h4>';
                        } else if (row.active === "n") {
                            return '<h4><span class="label label-default"><s:message code="label.no" /></span></h4>';
                        }
                        return row.active;
                    }
                },
                { data: "userGroup.name" },
                { 
                    data: null, 
                    render: function ( data, type, row, meta ) {
                        var btnEdit = '<a class="btn btn-success" href="${contextPath}/settings/user/edit/' + row.id + '" data-toggle="tooltip" data-placement="top" title="<s:message code="button.edit" />"><i class="fa fa-lg fa-pencil"></i></a>';
                        var btnDelete = '<a class="btn btn-danger btn-delete" href="" data-toggle="tooltip" data-placement="top" title="<s:message code="button.delete" />"><i class="fa fa-lg fa-trash"></i></a>';
                        return btnEdit + ' ' + btnDelete;
                    }, 
                    orderable: false 
                }],
                order: [[1, 'asc']],
                language: utils.dataTables.language['${languageCode}'],
                initComplete: function(settings, json) {

                    // Search on ENTER
                    // https://stackoverflow.com/questions/14619498/datatables-global-search-on-keypress-of-enter-key-instead-of-any-key-keypress
                    $('#tableUser_filter input').unbind();
                    $('#tableUser_filter input').bind('keyup', function(e) {
                        if(e.keyCode === 13) {
                            table.search(this.value).draw();
                        }
                    });
                	
                    // Recreate tooltip
                    $('[data-toggle="tooltip"]').tooltip({
                        trigger : 'hover'
                    });
                }
            });
    	
            $(document).delegate("a.btn-delete", "click", function (e) {
                e.preventDefault();
                // Make all checkboxes unchecked first
                $("input[name=selected]").prop("checked", false);
                // Then make this row checkbox checked
                $(this).closest("tr").find("input[name=selected]").prop("checked", true);
                showDeleteConfirmation();
            });
                
            $("a#btnDeleteSelected").on("click", function(e) {
                e.preventDefault();
                showDeleteConfirmation();
            });
        
            function showDeleteConfirmation() {
                swal({
                    title: "<s:message code="dialog.message.areyousure" />",
                    text: "<s:message code="dialog.message.areyousure.description" />",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonText: "<s:message code="button.yes" />",
                    cancelButtonText: "<s:message code="button.no" />",
                    closeOnConfirm: false
                }, function(confirmed){
                    if (confirmed) {
                        var form = $("<form></form>");
                        form.prop("action", "${contextPath}/settings/user/remove");
                        form.prop("method", "post");
                        $("input[name=selected]:checked").each(function() {
                            var input = $("<input />");
                            input.prop("name", "selected");
                            input.prop("type", "hidden");
                            input.prop("value", $(this).val());
                            form.append(input);
                        });    
                        form.appendTo("body");
                        form.submit();
                    }
                });
            }
        </script>
    </body>
</html>