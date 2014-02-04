package com.github.rmannibucau.comparator.properties;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PropertyComparatorTest {
	@Test
	public void key() {
		assertEquals("a".compareTo("b"),
				PropertyKeyComparator.INSTANCE.compare(new Property("a", "b"), new Property("b", "c")));
	}

	@Test
	public void value() {
		assertEquals("c".compareTo("d"),
				PropertyValueComparator.INSTANCE.compare(new Property("a", "c"), new Property("b", "d")));
	}
}
