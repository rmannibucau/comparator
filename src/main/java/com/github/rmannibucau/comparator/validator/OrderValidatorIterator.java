package com.github.rmannibucau.comparator.validator;

import java.util.Comparator;
import java.util.Iterator;

public class OrderValidatorIterator<T> implements Iterator<T> {
	private final Iterator<T> delegate;
	private final Comparator<T> orderComparator;
	private final boolean acceptDuplicated;
	private T last = null;

	public OrderValidatorIterator(final Iterator<T> delegate, final Comparator<T> orderComparator) {
		this(delegate, orderComparator, false);
	}

	public OrderValidatorIterator(final Iterator<T> delegate, final Comparator<T> orderComparator, final boolean acceptDuplicated) {
		this.delegate = delegate;
		this.orderComparator = orderComparator;
		this.acceptDuplicated = acceptDuplicated;
	}

	@Override
	public boolean hasNext() {
		return delegate.hasNext();
	}

	@Override
	public T next() {
		final T instance = delegate.next();
		if (last != null) {
			switch (Math.min(orderComparator.compare(last, instance), 1)) {
				case 0:
					if (!acceptDuplicated) {
						throw new DuplicatedBeanException("Duplicates beans found: " + instance);
					}
					break;
				case 1:
					throw new UnsortedOrderException("Input not sorted as expected, failed on " + instance);
			}
		}
		last = instance;
		return instance;
	}

	@Override
	public void remove() {
		delegate.remove();
	}

	public static class InvalidOrderException extends RuntimeException {
		public InvalidOrderException(final String s) {
			super(s);
		}
	}

	public static class DuplicatedBeanException extends RuntimeException {
		public DuplicatedBeanException(final String s) {
			super(s);
		}
	}

	public static class UnsortedOrderException extends RuntimeException {
		public UnsortedOrderException(final String s) {
			super(s);
		}
	}
}
