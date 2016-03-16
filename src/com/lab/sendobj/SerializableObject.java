package com.lab.sendobj;

import java.io.Serializable;

public class SerializableObject implements Serializable {
	private static final long serialVersionUID = 1L;
	private String topic;
	private String content;

	public SerializableObject(String topic, String content) {
		super();
		this.topic = topic;
		this.content = content;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "topic=" + topic + ", content=" + content;
	}
}
