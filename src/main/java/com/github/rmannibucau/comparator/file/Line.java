package com.github.rmannibucau.comparator.file;

public class Line {
	private final int number;
	private final String content;

	public Line(final int count, final String value) {
		this.number = count;
		this.content = value;
	}

	public int getNumber() {
		return number;
	}

	public String getContent() {
		return content;
	}
}
