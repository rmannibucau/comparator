package com.github.rmannibucau.comparator.file;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class FileLineIterator implements Iterator<Line>, Closeable {
	private final BufferedReader reader;
	private String line = null;
	private int count = 0;

	public FileLineIterator(final File file) throws FileNotFoundException {
		if (!file.exists()) {
			throw new IllegalArgumentException("File '" + file.getAbsolutePath() + "' doesn't exist");
		}
		reader = new BufferedReader(new FileReader(file));
	}

	@Override
	public boolean hasNext() {
		if (line != null) {
			return true;
		}

		try {
			line = reader.readLine();
			count ++;
			return line != null;
		} catch (final IOException e) {
			return false;
		}
	}

	@Override
	public Line next() {
		final String value = line;
		line = null;
		return new Line(count, value);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}
}
