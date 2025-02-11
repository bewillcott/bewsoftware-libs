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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.bewsoftware.property;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the {@link Property} class.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public class PropertyTest
{
    private final String comment1 = "The first one";

    private final String comment2 = "The first two";

    private final String one = "one";

    private Property<Integer, String> prop1;

    private Property<Integer, String> prop2;

    private Property<Integer, String> prop3;

    private Property<Integer, String> prop4;

    private final String two = "two";

    public PropertyTest()
    {
    }

    @BeforeEach
    public void setUp()
    {
        prop1 = new Property<>(1, one, comment1);
        prop2 = new Property<>(2, two, comment2);
        prop3 = new Property<>(1, one, comment1);
        prop4 = new Property<>(2, two, comment2);
    }

    /**
     * Test of comment method, of class Property.
     */
    @Test
    public void testComment()
    {
        assertEquals(comment1, prop1.comment(), "prop1");
        assertEquals(comment2, prop2.comment(), "prop2");
        assertNotEquals(comment2, prop1.comment(), "prop1");
        assertNotEquals(comment1, prop2.comment(), "prop2");
    }

    /**
     * Test of compareTo method, of class Property.
     */
    @Test
    public void testCompareTo()
    {
        assertTrue(prop1.compareTo(prop2) == -1, "compareTo: prop1 < prop2");
        assertTrue(prop1.compareTo(prop3) == 0, "compareTo: prop1 == prop3");

        assertTrue(prop2.compareTo(prop1) == 1, "compareTo: prop2 < prop1");
        assertTrue(prop2.compareTo(prop4) == 0, "compareTo: prop2 == prop4");
    }

    /**
     * Test of constructors, of class Property.
     */
    @Test
    public void testConstructors()
    {
        Property<Integer, String> propA = new Property<>(prop3);
        assertNotNull(propA, "propA != null");
        assertEquals(prop1, propA, "propA == prop1");

        Property<Integer, String> propB = new Property<>(1, one);
        assertEquals(propA, propB);
        assertNotEquals(propA.comment(), propB.comment(), "propA.comment() != propB.comment()");
        assertNull(propB.comment(), "propB.comment() == null");
    }

    /**
     * Test of equals method, of class Property.
     */
    @Test
    public void testEquals()
    {
        assertTrue(prop1.equals(prop3), "equals: prop1 == prop3");
        assertTrue(prop2.equals(prop4), "equals: prop2 == prop4");
        assertFalse(prop1.equals(prop4), "equals: prop1 != prop4");
        assertFalse(prop2.equals(prop3), "equals: prop2 != prop3");
    }

    /**
     * Test of hashCode method, of class Property.
     */
    @Test
    public void testHashCode()
    {
        assertTrue(prop1.hashCode() == prop3.hashCode(), "hashCode: prop1 == prop3");
        assertTrue(prop2.hashCode() == prop4.hashCode(), "hashCode: prop2 == prop4");
        assertFalse(prop1.hashCode() == prop4.hashCode(), "hashCode: prop1 != prop4");
        assertFalse(prop2.hashCode() == prop3.hashCode(), "hashCode: prop2 != prop3");
    }

    /**
     * Test of key method, of class Property.
     */
    @Test
    public void testKey()
    {
        assertEquals(1L, (long) prop1.key(), "prop1");
        assertEquals(2L, (long) prop2.key(), "prop2");
        assertNotEquals(2L, (long) prop1.key(), "prop1");
        assertNotEquals(1L, (long) prop2.key(), "prop2");
    }

    /**
     * Test of value method, of class Property.
     */
    @Test
    public void testValue()
    {
        assertEquals(one, prop1.value(), "prop1");
        assertEquals(two, prop2.value(), "prop2");
        assertNotEquals(two, prop1.value(), "prop1");
        assertNotEquals(one, prop2.value(), "prop2");
    }

}
