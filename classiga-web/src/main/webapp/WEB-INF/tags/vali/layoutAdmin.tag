<%@tag description="Layout Tag" pageEncoding="UTF-8"%>

<%@taglib prefix="t" tagdir="/WEB-INF/tags/vali" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>

<%@attribute name="styles" fragment="true" %>
<%@attribute name="scripts" fragment="true" %>

<%@attribute name="titleCode" required="true" %>

<t:layoutBasic titleCode="${titleCode}" >

    <jsp:attribute name="styles">
        <jsp:invoke fragment="styles"/>
    </jsp:attribute>
    <jsp:attribute name="scripts">
        <script>
        document.body.className = "sidebar-mini fixed";
        </script>
        <jsp:invoke fragment="scripts"/>
    </jsp:attribute>
    <jsp:body>
        <div class="wrapper">
            <!-- Navbar-->
            <header class="main-header hidden-print"><a class="logo" href="${contextPath}">${appName}</a>
                <nav class="navbar navbar-static-top">
                    <!-- Sidebar toggle button--><a class="sidebar-toggle" href="#" data-toggle="offcanvas"></a>
                    <!-- Navbar Right Menu-->
                    <div class="navbar-custom-menu">
                        <ul class="top-nav">
                            <c:if test="${userGroup.id ne -1}">
                            <!-- User Menu-->
                            <li class="dropdown">
                                <a class="dropdown-toggle" href="#" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><i class="fa fa-user fa-lg"></i></a>
                                <ul class="dropdown-menu settings-menu">
                                    <li><a id="btnEditProfile" href="${contextPath}/user/edit-profile"><i class="fa fa-user fa-lg"></i> <s:message code="label.editprofile" /></a></li>
                                    <li><a id="btnChangePassword" href="${contextPath}/user/change-password"><i class="fa fa-lock fa-lg"></i> <s:message code="label.changepassword" /></a></li>
                                </ul>
                            </li>
                            </c:if>
                            <li>
                                <a id="btnLogout" href="${contextPath}/logout" role="button" ><i class="fa fa-sign-out fa-lg"></i></a>
                            </li>
                        </ul>
                    </div>
                </nav>
            </header>
            <!-- Side-Nav-->
            <aside class="main-sidebar hidden-print">
                <section class="sidebar">
                    <div class="user-panel">
                        <c:if test="${not empty user.avatar}">
                        <div class="pull-left image"><img class="img-circle" src="${contextPath}/avatar/${user.avatar}" height="48" alt="User Image" onerror="this.src='${contextPath}/resources/vali/images/user.png'"></div>
                        </c:if>
                        <c:if test="${empty user.avatar}">
                        <div class="pull-left image"><img class="img-circle" src="${contextPath}/resources/vali/images/user.png" height="48" alt="User Image"></div>
                        </c:if>
                        <div class="pull-left info">
                            <p>${user.fullName}</p>
                            <p class="designation">${userGroup.name}</p>
                        </div>
                    </div>
                    <!-- Sidebar Menu-->
                    <c:if test="${not empty menus}">

                        <ul class="sidebar-menu">

                            <c:forEach items="${menus}" var="menu">

                                <c:if test="${not empty menu.submenus}">
                                    <c:choose>
                                        <c:when test="${menu.code eq currentMenu.parentCode}">
                                        <li class="treeview active">
                                            <a href="#"><i class="${menu.faIcon}"></i><span><s:message code="${menu.label}" /></span><i class="fa fa-angle-right"></i></a>
                                            <ul class="treeview-menu menu-open">
                                                <c:forEach items="${menu.submenus}" var="submenu">
                                                    <c:if test="${submenu.code eq currentMenu.code}">
                                                    <li class="active">
                                                        <a href="${contextPath}${submenu.path}"><i class="fa fa-circle-o"></i><s:message code="${submenu.label}" /></a>
                                                    </li>
                                                    </c:if>
                                                    <c:if test="${submenu.code ne currentMenu.code}">
                                                    <li>
                                                        <a href="${contextPath}${submenu.path}"><i class="fa fa-circle-o"></i><s:message code="${submenu.label}" /></a>
                                                    </li>
                                                    </c:if>

                                                </c:forEach>
                                            </ul>
                                        </li>
                                        </c:when>
                                        <c:otherwise>
                                        <li class="treeview">
                                            <a href="#"><i class="${menu.faIcon}"></i><span><s:message code="${menu.label}" /></span><i class="fa fa-angle-right"></i></a>
                                            <ul class="treeview-menu">
                                                <c:forEach items="${menu.submenus}" var="submenu">
                                                    <li>
                                                        <a href="${contextPath}${submenu.path}"><i class="fa fa-circle-o"></i><s:message code="${submenu.label}" /></a>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>

                                <c:if test="${empty menu.submenus}">
                                    <c:choose>
                                        <c:when test="${menu.code eq currentMenu.code}">
                                        <li class="active">
                                            <a href="${contextPath}${menu.path}"><i class="${menu.faIcon}"></i><span><s:message code="${menu.label}" /></span></a>
                                        </li>
                                        </c:when>
                                        <c:otherwise>
                                        <li>
                                            <a href="${contextPath}${menu.path}"><i class="${menu.faIcon}"></i><span><s:message code="${menu.label}" /></span></a>
                                        </li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>

                            </c:forEach>

                        </ul>

                    </c:if>

                </section>
            </aside>

            <jsp:doBody/>
        </div>
    </jsp:body>

</t:layoutBasic>