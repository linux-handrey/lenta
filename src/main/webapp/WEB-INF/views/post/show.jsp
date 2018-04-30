<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:layout>
	<div style="border: 1px solid black; margin: 20 0 0 50px; width: 800px; padding: 20px;">
		<h1>${post.getTitle()}</h1>
		<div style="color:gray">${post.getCreatedAt().toString()}</div>
		<div style="margin: 20 0 0 0px;">${post.getContent()}</div>
	</div>
</t:layout>