<%@ page session="false"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- Side-Nav-->
<aside class="main-sidebar hidden-print">
    <section class="sidebar">
        <div class="user-panel">
            <div class="pull-left image"><img class="img-circle" src="${contextPath}/resources/vali/images/user.png" height="48" alt="User Image"></div>
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
                                                    
                        <c:if test="${menu.code eq currentMenu.parentCode}">
                        <li class="treeview active">
                        </c:if>
                                                    
                        <c:if test="${menu.code ne currentMenu.parentCode}">
                        <li class="treeview">
                        </c:if>
                        
                            <a href="#"><i class="${menu.faIcon}"></i><span><s:message code="${menu.label}" /></span><i class="fa fa-angle-right"></i></a>
                                                        
                            <c:if test="${menu.code eq currentMenu.parentCode}">
                            <ul class="treeview-menu menu-open">
                            </c:if>
                                                        
                            <c:if test="${menu.code ne currentMenu.parentCode}">
                            <ul class="treeview-menu">
                            </c:if>
                            
                                <c:forEach items="${menu.submenus}" var="submenu">
                                                                    
                                    <c:if test="${submenu.code eq currentMenu.code}">
                                    <li class="active">
                                    </c:if>
                                                                        
                                    <c:if test="${submenu.code ne currentMenu.code}">
                                    <li>
                                    </c:if>
                                    
                                        <a href="${contextPath}${submenu.path}"><i class="fa fa-circle-o"></i><s:message code="${submenu.label}" /></a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </li>
                    </c:if>
                    
                    <c:if test="${empty menu.submenus}">
                    <li>
                        <a href="${contextPath}${menu.path}"><i class="${menu.faIcon}"></i><span><s:message code="${menu.label}" /></span></a>
                    </li>
                    </c:if>
                    
                </c:forEach>
                    
            </ul>
            
        </c:if>
        
    </section>
</aside>
