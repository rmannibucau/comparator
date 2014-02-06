package com.github.rmannibucau.comparator;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

public class DifferenceAnalyzer {
    /**
     * NOTE: newStream and existingStream should be sorted using the same order.
     *
     * @param newStream input data stream
     * @param existingStream current data stream
     * @param keyComparator comparator used to test existence of beans in both streams
     * @param comparator normal bean comparator (keys can be skipped)
     * @param <T> the bean type
     * @return the differences between both streams
     */
    public static <T> Difference<T> compare(final Iterator<T> newStream, final Iterator<T> existingStream,
                                            final Comparator<T> keyComparator, final Comparator<T> comparator) {

        final Collection<T> missing = new LinkedList<T>();
        final Collection<T> added = new LinkedList<T>();
        final Collection<T> updated = new LinkedList<T>();

        T existingData = null;
        T newData = null;
        try {
            boolean oneMoreIteration = existingStream.hasNext() && newStream.hasNext();

            if (oneMoreIteration) {
                existingData = existingStream.next();
                newData = newStream.next();
            }

            while (oneMoreIteration) {
                oneMoreIteration = false;

                final int diff = keyComparator.compare(existingData, newData);
                if (diff > 0) {
                    added.add(newData);
                    oneMoreIteration = newStream.hasNext();
                    if (oneMoreIteration) {
                        newData = newStream.next();
                    } else {
                        missing.add(existingData);
                    }
                }
                if (diff < 0) {
                    missing.add(existingData);
                    oneMoreIteration = existingStream.hasNext();
                    if (oneMoreIteration) {
                        existingData = existingStream.next();
                    } else {
                        added.add(newData);
                    }
                }
                if (diff == 0) {
                    if (comparator.compare(existingData, newData) != 0) {
                        updated.add(newData);
                    } // else no diff
                    oneMoreIteration = existingStream.hasNext() && newStream.hasNext();
                    if (oneMoreIteration) {
                        existingData = existingStream.next();
                        newData = newStream.next();
                    }
                }
            }
            while (newStream.hasNext()) {
                added.add(newStream.next());
            }
            while (existingStream.hasNext()) {
                missing.add(existingStream.next());
            }
        } finally {
            closeIfNeeded(newStream);
            closeIfNeeded(existingStream);
        }

        return new Difference<T>(missing, added, updated);
    }

    private static void closeIfNeeded(final Iterator<?> it) {
        if (Closeable.class.isInstance(it)) {
            try {
                Closeable.class.cast(it).close();
            } catch (final IOException e) {
                // no-op
            }
        }
    }

    public static class Difference<T> {
        private final Collection<T> missing;
        private final Collection<T> added;
        private final Collection<T> updated;

        public Difference(final Collection<T> missing, final Collection<T> added, final Collection<T> updated) {
            this.missing = Collections.unmodifiableCollection(missing);
            this.added = Collections.unmodifiableCollection(added);
            this.updated = Collections.unmodifiableCollection(updated);
        }

        public Collection<T> missing() {
            return missing;
        }

        public Collection<T> added() {
            return added;
        }

        public Collection<T> updated() {
            return updated;
        }
    }

    private DifferenceAnalyzer() {
        // no-op
    }
}
