package com.github.rmannibucau.comparator;

import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import static com.github.rmannibucau.comparator.DifferenceAnalyzer.compare;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DifferenceAnalyzerTest {
    @Test
    public void noDiff() {
        final DifferenceAnalyzer.Difference<Person> diff = compare(
                asList(new Person(1, "a"), new Person(2, "b"), new Person(3, "c")).iterator(),
                asList(new Person(1, "a"), new Person(2, "b"), new Person(3, "c")).iterator(),
                new KeyComparator(),
                new BeanComparator()
        );
        assertTrue(diff.added().isEmpty());
        assertTrue(diff.missing().isEmpty());
        assertTrue(diff.updated().isEmpty());
    }

    @Test
    public void noInput() {
        final DifferenceAnalyzer.Difference<Person> diff = compare(
                Collections.<Person>emptyList().iterator(),
                asList(new Person(1, "a"), new Person(2, "b"), new Person(3, "c")).iterator(),
                new KeyComparator(),
                new BeanComparator()
        );
        assertTrue(diff.added().isEmpty());
        assertEquals(3, diff.missing().size());
        assertTrue(diff.updated().isEmpty());
    }

    @Test
    public void noExisting() {
        final DifferenceAnalyzer.Difference<Person> diff = compare(
                asList(new Person(1, "a"), new Person(2, "b"), new Person(3, "c")).iterator(),
                Collections.<Person>emptyList().iterator(),
                new KeyComparator(),
                new BeanComparator()
        );
        assertEquals(3, diff.added().size());
        assertTrue(diff.missing().isEmpty());
        assertTrue(diff.updated().isEmpty());
    }

    @Test
    public void updated() {
        final DifferenceAnalyzer.Difference<Person> diff = compare(
                asList(new Person(1, "a"), new Person(2, "bb"), new Person(3, "c")).iterator(),
                asList(new Person(1, "a"), new Person(2, "b"), new Person(3, "c")).iterator(),
                new KeyComparator(),
                new BeanComparator()
        );
        assertTrue(diff.added().isEmpty());
        assertTrue(diff.missing().isEmpty());
        assertEquals(1, diff.updated().size());
        assertEquals("bb", diff.updated().iterator().next().name);
    }

    @Test
    public void fullWithMoreExistingThanNew() {
        final DifferenceAnalyzer.Difference<Person> diff = compare(
                asList(new Person(2, "b"), new Person(3, "c"), new Person(4, "d")).iterator(),
                asList(new Person(1, "a"), new Person(2, "b"), new Person(3, "cc"), new Person(5, "e")).iterator(),
                new KeyComparator(),
                new BeanComparator()
        );
        assertEquals(1, diff.added().size());
        assertEquals(4, diff.added().iterator().next().id);
        assertEquals(1, diff.updated().size());
        assertEquals(3, diff.updated().iterator().next().id);
        assertEquals(2, diff.missing().size());
        final Iterator<Person> missIt = diff.missing().iterator();
        assertEquals(1, missIt.next().id);
        assertEquals(5, missIt.next().id);
    }

    @Test
    public void missingDataAtTheEnd() {
        final DifferenceAnalyzer.Difference<Person> diff = compare(
                asList(new Person(2, "b")).iterator(),
                asList(new Person(2, "b"), new Person(3, "c"), new Person(4, "d")).iterator(),
                new KeyComparator(),
                new BeanComparator()
        );
        assertTrue(diff.added().isEmpty());
        assertEquals(2, diff.missing().size());
        assertTrue(diff.updated().isEmpty());
    }

    @Test
    public void missingDataAtTheBeginning() {
        final DifferenceAnalyzer.Difference<Person> diff = compare(
                asList(new Person(2, "b"), new Person(3, "c"), new Person(4, "d")).iterator(),
                asList(new Person(2, "b")).iterator(),
                new KeyComparator(),
                new BeanComparator()
        );
        assertEquals(2, diff.added().size());
        assertTrue(diff.missing().isEmpty());
        assertTrue(diff.updated().isEmpty());
    }

    @Test
    public void firstMissingAndNew() {
        final DifferenceAnalyzer.Difference<Person> diff = compare(
                asList(new Person(2, "b"), new Person(3, "c"), new Person(4, "d")).iterator(),
                asList(new Person(1, "b")).iterator(),
                new KeyComparator(),
                new BeanComparator()
        );
        assertEquals(3, diff.added().size());
        assertEquals(1, diff.missing().size());
        assertTrue(diff.updated().isEmpty());
    }

    public static class Person {
        private final long id;
        private final String name;

        public Person(final long id, final String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static class KeyComparator implements Comparator<Person> {
        @Override
        public int compare(final Person o1, final Person o2) {
            return (int) (o1.id - o2.id);
        }
    }

    public static class BeanComparator implements Comparator<Person> {
        @Override
        public int compare(final Person o1, final Person o2) {
            return o1.name.compareTo(o2.name);
        }
    }
}
