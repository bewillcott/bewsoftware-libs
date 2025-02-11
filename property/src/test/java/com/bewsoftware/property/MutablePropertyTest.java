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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 */
public class MutablePropertyTest
{

    public MutablePropertyTest()
    {
    }

    /**
     * Test of comment method, of class MutableProperty.
     */
    @Test
    public void testComment()
    {
        MutableProperty<Integer, String> prop1 = new MutableProperty<>(12, "twelve");
        assertNotNull(prop1);
        assertEquals(12L, (long) prop1.key(), "prop1.key().equals(12)");
        assertEquals("twelve", prop1.value(), "prop1.value().equals(\"twelve\")");
        assertNull(prop1.comment());

        prop1.comment("Now it has something");
        assertEquals("Now it has something", prop1.comment(),
                "prop1.comment().equals(\"Now it has something\")");
    }

    /**
     * Test of constructors, of class MutableProperty.
     */
    @Test
    public void testConstructors()
    {
        MutableProperty<String, Integer> propA = new MutableProperty<>("one", 1);
        assertNotNull(propA);
        assertEquals("one", propA.key(), "propA.key().equals(\"one\")");
        assertEquals(1L, (long) propA.value(), "propA.value().equals(1)");
        assertNull(propA.comment());

        MutableProperty<String, Integer> propB = new MutableProperty<>("two", 2, "This is a two");
        assertNotNull(propB);
        assertEquals("two", propB.key(), "propB.key().equals(\"two\")");
        assertEquals(2L, (long) propB.value(), "propB.value().equals(1)");
        assertNotNull(propB.comment());
        assertEquals("This is a two", propB.comment(), "propB.comment().equals(\"This is a two\")");

        MutableProperty<String, Integer> propC = new MutableProperty<>(propB);
        assertNotNull(propC);
        assertEquals("two", propC.key(), "propC.key().equals(\"two\")");
        assertEquals(2L, (long) propC.value(), "propC.value().equals(1)");
        assertNotNull(propC.comment());
        assertEquals("This is a two", propC.comment(), "propC.comment().equals(\"This is a two\")");
    }

    /**
     * Test of value method, of class MutableProperty.
     */
    @Test
    public void testValue()
    {
        MutableProperty<Integer, String> prop1 = new MutableProperty<>(12, "twelve");
        assertNotNull(prop1);
        assertEquals(12L, (long) prop1.key(), "prop1.key().equals(12)");
        assertEquals("twelve", prop1.value(), "prop1.value().equals(\"twelve\")");

        prop1.value("Now it has changed");
        assertEquals("Now it has changed", prop1.value(),
                "prop1.value().equals(\"Now it has changed\")");
    }

}
