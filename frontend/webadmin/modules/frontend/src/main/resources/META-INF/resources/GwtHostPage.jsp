<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <script type="text/javascript">
        <c:if test="${requestScope['userInfo'] != null}">
            var userInfo = <c:out value="${requestScope['userInfo']}" escapeXml="false"/>;
        </c:if>
        <c:if test="${requestScope['applicationMode'] != null}">
            var applicationMode = <c:out value="${requestScope['applicationMode']}" escapeXml="false"/>;
        </c:if>
        <c:if test="${requestScope['pluginDefinitions'] != null}">
            var pluginDefinitions = <c:out value="${requestScope['pluginDefinitions']}" escapeXml="false"/>;
        </c:if>
    </script>
</head>
<body>
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
    <script type="text/javascript" src="${requestScope['selectorScript']}"></script>
</body>
</html>
