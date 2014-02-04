package com.github.rmannibucau.comparator.properties;

import java.util.Comparator;

public class PropertyValueComparator implements Comparator<Property> {
	public static final PropertyValueComparator INSTANCE = new PropertyValueComparator();

	private PropertyValueComparator() {
		// no-op
	}

	@Override
	public int compare(final Property o1, final Property o2) {
		return o1.getValue().compareTo(o2.getValue());
	}
}
