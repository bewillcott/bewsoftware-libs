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
package com.bewsoftware.property;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 */
public class MutableIniPropertyTest {

    public MutableIniPropertyTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of comment method, of class MutableIniProperty.
     */
    @Test
    public void testComment() {
        MutableIniProperty< String> prop1 = new MutableIniProperty<>("12", "twelve");
        assertNotNull(prop1);
        assertEquals("prop1.key().equals(\"12\")", "12", prop1.key());
        assertEquals("prop1.value().equals(\"twelve\")", "twelve", prop1.value());
        assertNull(prop1.comment());

        prop1.comment("Now it has something");
        assertEquals("prop1.comment().equals(\"Now it has something\")",
                     "Now it has something", prop1.comment());
    }

    /**
     * Test of value method, of class MutableIniProperty.
     */
    @Test
    public void testValue() {
        MutableIniProperty< String> prop1 = new MutableIniProperty<>("12", "twelve");
        assertNotNull(prop1);
        assertEquals("prop1.key().equals(\"12\")", "12", prop1.key());
        assertEquals("prop1.value().equals(\"twelve\")", "twelve", prop1.value());

        prop1.value("Now it has changed");
        assertEquals("prop1.value().equals(\"Now it has changed\")",
                     "Now it has changed", prop1.value());
    }

    /**
     * Test of constructors, of class MutableIniProperty.
     */
    @Test
    public void testConstructors() {
        MutableIniProperty<Integer> propA = new MutableIniProperty<>("one", 1);
        assertNotNull(propA);
        assertEquals("propA.key().equals(\"one\")", "one", propA.key());
        assertEquals("propA.value().equals(1)", 1L, (long) propA.value());
        assertNull(propA.comment());

        MutableIniProperty<Integer> propB = new MutableIniProperty<>("two", 2, "This is a two");
        assertNotNull(propB);
        assertEquals("propB.key().equals(\"two\")", "two", propB.key());
        assertEquals("propB.value().equals(1)", 2L, (long) propB.value());
        assertNotNull(propB.comment());
        assertEquals("propB.comment().equals(\"This is a two\")", "This is a two", propB.comment());

        MutableIniProperty<Integer> propC = new MutableIniProperty<>(propB);
        assertNotNull(propC);
        assertEquals("propC.key().equals(\"two\")", "two", propC.key());
        assertEquals("propC.value().equals(1)", 2L, (long) propC.value());
        assertNotNull(propC.comment());
        assertEquals("propC.comment().equals(\"This is a two\")", "This is a two", propC.comment());
    }

}
