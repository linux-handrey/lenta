package com.lenta.app.entity;

import java.util.Date;

public class Post
{
	private Long id;
	private String title;
	private String content;
	private Date createdAt;

	public Post() {
		
	}

	public Post(Long id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}

	public Post(Long id, String title, String content, Date createdAt) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return id + "\t" + title + "\t" + createdAt;
	}

	@Override
	public int hashCode() {
		int result = 31 * title.hashCode();
		result += 31 * content.hashCode();
		result += 31 * createdAt.hashCode();
		result += id;

		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (null == o || this.getClass() != o.getClass()) {
			return false;
		}

		Post post = (Post)o;

		if (id != post.getId()) {
			return false;
		}

		if (!title.equals(post.getTitle())) {
			return false;
		}

		if (!content.equals(post.getContent())) {
			return false;
		}

		if (!createdAt.equals(post.getCreatedAt())) {
			return false;
		}

		return true;
	}
}