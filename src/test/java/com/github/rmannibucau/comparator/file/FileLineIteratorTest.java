package com.github.rmannibucau.comparator.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileLineIteratorTest {
	@Test
	public void emptyFile() throws FileNotFoundException {
		final File file = createFile(new File("target/testdata/empty.txt"), "");
		final FileLineIterator it = new FileLineIterator(file);
		assertFalse(it.hasNext());
	}

	@Test
	public void someLines() throws FileNotFoundException {
		final String ln = System.getProperty("line.separator");
		final File file = createFile(new File("target/testdata/3lines.txt"), "l1" + ln + "l2" + ln + "l3" + ln);
		final FileLineIterator it = new FileLineIterator(file);

		for (int i = 1; i <= 3; i++) {
			assertTrue(it.hasNext());
			final Line next = it.next();
			assertEquals("l" + i, next.getContent());
			assertEquals(i, next.getNumber());
		}
		assertFalse(it.hasNext());
	}

	private static File createFile(final File file, final String content) {
		final File parentFile = file.getParentFile();
		if (!parentFile.exists() && !parentFile.mkdirs()) {
			throw new IllegalArgumentException("Can't mkdirs " + parentFile);
		}

		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			writer.write(content);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (final IOException e) {
					// no-op
				}
			}
		}
		return file;
	}
}
