package com.github.rmannibucau.comparator.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class PropertiesIterator implements Iterator<Property> {
	private final Properties properties;
	private final Iterator<String> keys;

	public PropertiesIterator(final Properties properties) {
		this.properties = properties;

		final List<String> keysList = new ArrayList<String>(properties.stringPropertyNames());
		Collections.sort(keysList);
		this.keys = keysList.iterator();
	}

	@Override
	public boolean hasNext() {
		return keys.hasNext();
	}

	@Override
	public Property next() {
		final String key = keys.next();
		return new Property(key, properties.getProperty(key));
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
