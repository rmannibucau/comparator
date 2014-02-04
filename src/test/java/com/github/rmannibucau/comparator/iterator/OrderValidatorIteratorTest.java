package com.github.rmannibucau.comparator.iterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class OrderValidatorIteratorTest {
	private static final Comparator<Integer> NATURAL_COMP = Collections.reverseOrder(Collections.<Integer>reverseOrder());

	@Test
	public void correctOrder() {
		final List<Integer> integers = asList(1, 2, 3);
		final Iterator<Integer> it = new OrderValidatorIterator<Integer>(integers.iterator(), NATURAL_COMP);
		assertEquals(integers, iterate(it));
	}

	@Test(expected = OrderValidatorIterator.DuplicatedBeanException.class)
	public void duplicatedNotAccepted() {
		final Iterator<Integer> it = new OrderValidatorIterator<Integer>(asList(1, 1, 2, 3).iterator(), NATURAL_COMP);
		iterate(it);
		fail();
	}

	@Test
	public void duplicatedAccepted() {
		final List<Integer> integers = asList(1, 1, 2, 3);
		final Iterator<Integer> it = new OrderValidatorIterator<Integer>(integers.iterator(), NATURAL_COMP, true);
		assertEquals(integers, iterate(it));
	}

	@Test(expected = OrderValidatorIterator.UnsortedOrderException.class)
	public void notSorted() {
		final List<Integer> integers = asList(1, 3, 2);
		final Iterator<Integer> it = new OrderValidatorIterator<Integer>(integers.iterator(), NATURAL_COMP);
		assertEquals(integers, iterate(it));
	}

	private static List<Integer> iterate(final Iterator<Integer> it) {
		final List<Integer> values = new ArrayList<Integer>(5);
		while (it.hasNext()) {
			values.add(it.next());
		}
		return values;
	}
}
