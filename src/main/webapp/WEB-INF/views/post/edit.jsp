<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:layout>

	<form method="POST" action="/lenta/posts/${post.getId()}/update" >
		<div style="border: 1px solid black; width:800px; height: 450px; margin: 20 0 0 50px; padding-left: 20px">
			<h2>Edit post</h2>
			<input name="title" value="${post.getTitle()}" type="text" style="width: 750px; margin-bottom: 15px" placeholder="Title">
			<textarea name="content" style="width: 750px; height: 300px; margin-bottom: 15px" placeholder="Content">${post.getContent()}</textarea>
			<input type="submit" name="update" value="Update" style="text-align: center;">
		</div>
	</form>
</t:layout>