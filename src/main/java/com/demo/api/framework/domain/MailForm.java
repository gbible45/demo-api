package com.demo.api.framework.domain;

/**
 * Send Email form.
 * 
 * @author Sanjay Patel
 */
public class MailForm {

	public MailForm() {}

	public MailForm(String category, String title, String contents) {
		this.category = category;
		this.title = title;
		this.contents = contents;
	}

	private String category;

	private String title;

	private String contents;


	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
}
