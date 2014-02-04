package com.github.rmannibucau.comparator.properties;

import java.util.Comparator;

public class PropertyKeyComparator implements Comparator<Property> {
	public static final PropertyKeyComparator INSTANCE = new PropertyKeyComparator();

	private PropertyKeyComparator() {
		// no-op
	}

	@Override
	public int compare(final Property o1, final Property o2) {
		return o1.getKey().compareTo(o2.getKey());
	}
}
