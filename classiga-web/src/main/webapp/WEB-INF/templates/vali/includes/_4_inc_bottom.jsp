<%@ page session="false"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- Javascripts-->
<script src="${contextPath}/res/${templateCode}/js/jquery-2.1.4.min.js"></script>
<script src="${contextPath}/res/${templateCode}/js/essential-plugins.js"></script>
<script src="${contextPath}/res/${templateCode}/js/bootstrap.min.js"></script>
<script src="${contextPath}/res/${templateCode}/js/plugins/pace.min.js"></script>
<script src="${contextPath}/res/${templateCode}/js/plugins/bootstrap-notify.min.js"></script>
<script src="${contextPath}/res/${templateCode}/js/vali.js"></script>
<script src="${contextPath}/res/common/js/utils.js"></script>

<script>
	$(document).ready(function() {
		<c:if test="${not empty notify}">
		$.notify({
			title : "${notify.title}",
			message : "${notify.message}",
			icon : ""
		}, {
			type : "${notify.type}",
			delay : 0
		});
		</c:if>
		$('[data-toggle="tooltip"]').tooltip({
			trigger : 'hover'
		});
	});

	
</script>