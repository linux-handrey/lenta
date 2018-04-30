<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:layout>

	<form method="POST" action="/lenta/posts/store" >
		<div style="border: 1px solid black; width:800px; height: 450px; margin: 20 0 0 50px; padding-left: 20px">
			<h2>Create new post</h2>
			<input name="title" type="text" style="width: 750px; margin-bottom: 15px" placeholder="Title">
			<textarea name="content" style="width: 750px; height: 300px; margin-bottom: 15px" placeholder="Content"></textarea>
			<input type="submit" name="store" value="Store" style="text-align: center;">
		</div>
	</form>
</t:layout>