/*
 * This file is part of the BEW Property Library (aka: BEWProperty).
 *
 * Copyright (C) 2020 Bradley Willcott
 *
 * BEWProperty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BEWProperty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.bew.property;

import java.util.Objects;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test the {@link Property} class.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public class PropertyTest {

    private Property<Integer, String> prop1;
    private Property<Integer, String> prop2;
    private Property<Integer, String> prop3;
    private Property<Integer, String> prop4;
    private final String one = "one";
    private final String comment1 = "The first one";
    private final String two = "two";
    private final String comment2 = "The first two";

    public PropertyTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        prop1 = new Property<>(1, one, comment1);
        prop2 = new Property<>(2, two, comment2);
        prop3 = new Property<>(1, one, comment1);
        prop4 = new Property<>(2, two, comment2);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of constructors, of class Property.
     */
    @Test
    public void testConstructors() {
        Property<Integer, String> propA = new Property<>(prop3);
        assertNotNull("propA != null", propA);
        assertEquals("propA == prop1", prop1, propA);

        Property<Integer, String> propB = new Property<>(1, one);
        assertEquals(propA, propB);
        assertNotEquals("propA.comment() != propB.comment()", propA.comment(), propB.comment());
        assertNull("propB.comment() == null", propB.comment());
    }

    /**
     * Test of comment method, of class Property.
     */
    @Test
    public void testComment() {
        assertEquals("prop1", comment1, prop1.comment());
        assertEquals("prop2", comment2, prop2.comment());
        assertNotEquals("prop1", comment2, prop1.comment());
        assertNotEquals("prop2", comment1, prop2.comment());
    }

    /**
     * Test of compareTo method, of class Property.
     */
    @Test
    public void testCompareTo() {
        assertTrue("compareTo: prop1 < prop2", prop1.compareTo(prop2) == -1);
        assertTrue("compareTo: prop1 == prop3", prop1.compareTo(prop3) == 0);

        assertTrue("compareTo: prop2 < prop1", prop2.compareTo(prop1) == 1);
        assertTrue("compareTo: prop2 == prop4", prop2.compareTo(prop4) == 0);
    }

    /**
     * Test of equals method, of class Property.
     */
    @Test
    public void testEquals() {
        assertTrue("equals: prop1 == prop3", prop1.equals(prop3));
        assertTrue("equals: prop2 == prop4", prop2.equals(prop4));
        assertFalse("equals: prop1 != prop4", prop1.equals(prop4));
        assertFalse("equals: prop2 != prop3", prop2.equals(prop3));
    }

    /**
     * Test of hashCode method, of class Property.
     */
    @Test
    public void testHashCode() {
        assertTrue("hashCode: prop1 == prop3", prop1.hashCode() == prop3.hashCode());
        assertTrue("hashCode: prop2 == prop4", prop2.hashCode() == prop4.hashCode());
        assertFalse("hashCode: prop1 != prop4", prop1.hashCode() == prop4.hashCode());
        assertFalse("hashCode: prop2 != prop3", prop2.hashCode() == prop3.hashCode());
    }

    /**
     * Test of key method, of class Property.
     */
    @Test
    public void testKey() {
        assertEquals("prop1", 1L, (long) prop1.key());
        assertEquals("prop2", 2L, (long) prop2.key());
        assertNotEquals("prop1", 2L, (long) prop1.key());
        assertNotEquals("prop2", 1L, (long) prop2.key());
    }

    /**
     * Test of value method, of class Property.
     */
    @Test
    public void testValue() {
        assertEquals("prop1", one, prop1.value());
        assertEquals("prop2", two, prop2.value());
        assertNotEquals("prop1", two, prop1.value());
        assertNotEquals("prop2", one, prop2.value());
    }

}
