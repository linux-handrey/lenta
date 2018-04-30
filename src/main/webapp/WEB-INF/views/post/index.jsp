<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:layout>
	<a href="/lenta/posts/create"><button>Create Post</button></a>
	<c:choose>
		<c:when test="${0 == posts.size()}">
			No posts found.
		</c:when>
		<c:otherwise>
			<c:forEach var="post" items="${posts}">
			<div class="block_container">
				<div class="block1">${post.getCreatedAt().toString()}</div>
				<div class="block2">
					<a href="/lenta/posts/${post.getId()}">${post.getTitle()}</a>
				</div>
				<div>
					<a href="/lenta/posts/${post.getId()}/edit">Edit</a>
					<a href="/lenta/posts/${post.getId()}/delete">Delete</a>
				</div>
			</div>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</t:layout>