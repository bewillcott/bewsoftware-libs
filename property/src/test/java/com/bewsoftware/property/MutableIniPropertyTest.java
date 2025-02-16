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

import static com.bewsoftware.utils.string.Strings.println;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 */
public class MutableIniPropertyTest
{

    public MutableIniPropertyTest()
    {
    }

    /**
     * Test of comment method, of class MutableIniProperty.
     */
    @Test
    public void testComment()
    {
        println("MutableIniPropertyTest.testComment");

        MutableIniProperty< String> prop1 = new MutableIniProperty<>("12", "twelve");
        assertNotNull(prop1);
        assertEquals("12", prop1.key(), "prop1.key().equals(\"12\")");
        assertEquals("twelve", prop1.value(), "prop1.value().equals(\"twelve\")");
        assertNull(prop1.comment());

        prop1.comment("Now it has something");
        assertEquals("Now it has something", prop1.comment(),
                "prop1.comment().equals(\"Now it has something\")");

        println("MutableIniPropertyTest.testComment: Completed");
    }

    /**
     * Test of constructors, of class MutableIniProperty.
     */
    @Test
    public void testConstructors()
    {
        println("MutableIniPropertyTest.testConstructors");

        MutableIniProperty<Integer> propA = new MutableIniProperty<>("one", 1);
        assertNotNull(propA);
        assertEquals("one", propA.key(), "propA.key().equals(\"one\")");
        assertEquals(1L, (long) propA.value(), "propA.value().equals(1)");
        assertNull(propA.comment());

        MutableIniProperty<Integer> propB = new MutableIniProperty<>("two", 2, "This is a two");
        assertNotNull(propB);
        assertEquals("two", propB.key(), "propB.key().equals(\"two\")");
        assertEquals(2L, (long) propB.value(), "propB.value().equals(2)");
        assertNotNull(propB.comment());
        assertEquals("This is a two", propB.comment(), "propB.comment().equals(\"This is a two\")");

        MutableIniProperty<Integer> propC = new MutableIniProperty<>(propB);
        assertNotNull(propC);
        assertEquals("two", propC.key(), "propC.key().equals(\"two\")");
        assertEquals(2L, (long) propC.value(), "propC.value().equals(2)");
        assertNotNull(propC.comment());
        assertEquals("This is a two", propC.comment(), "propC.comment().equals(\"This is a two\")");

        println("MutableIniPropertyTest.testConstructors: Completed");
    }

    /**
     * Test of value method, of class MutableIniProperty.
     */
    @Test
    public void testValue()
    {
        println("MutableIniPropertyTest.testValue");

        MutableIniProperty< String> prop1 = new MutableIniProperty<>("12", "twelve");
        assertNotNull(prop1);
        assertEquals("12", prop1.key(), "prop1.key().equals(\"12\")");
        assertEquals("twelve", prop1.value(), "prop1.value().equals(\"twelve\")");

        prop1.value("Now it has changed");
        assertEquals("Now it has changed", prop1.value(),
                "prop1.value().equals(\"Now it has changed\")");

        println("MutableIniPropertyTest.testValue: Completed");
    }
}
