package com.github.rmannibucau.comparator.properties;

import java.util.Iterator;
import java.util.Properties;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PropertyIteratorTest {
	@Test
	public void property() {
		final Properties props = new Properties();
		props.setProperty("z", "y");
		props.setProperty("a", "b");
		props.setProperty("m", "l");
		props.setProperty("c", "d");

		final Iterator<Property> it = new PropertiesIterator(props);
		assertEqual(it.next(), "a", "b");
		assertEqual(it.next(), "c", "d");
		assertEqual(it.next(), "m", "l");
		assertEqual(it.next(), "z", "y");
	}

	private void assertEqual(final Property next, final String key, final String value) {
		assertEquals(key, next.getKey());
		assertEquals(value, next.getValue());
	}
}
