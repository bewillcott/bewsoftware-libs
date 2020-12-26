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
public class IniPropertyTest {

    public IniPropertyTest() {
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
     * Test of constructors, of class IniProperty.
     */
    @Test
    public void testConstructors() {
        IniProperty<Integer> propA = new IniProperty<>("one", 1);
        assertNotNull(propA);
        assertEquals("propA.key().equals(\"one\")", "one", propA.key());
        assertEquals("propA.value().equals(1)", 1L, (long) propA.value());
        assertNull(propA.comment());

        IniProperty<Integer> propB = new IniProperty<>("two", 2, "This is a two");
        assertNotNull(propB);
        assertEquals("propB.key().equals(\"two\")", "two", propB.key());
        assertEquals("propB.value().equals(1)", 2L, (long) propB.value());
        assertNotNull(propB.comment());
        assertEquals("propB.comment().equals(\"This is a two\")", "This is a two", propB.comment());

        IniProperty<Integer> propC = new IniProperty<>(propB);
        assertNotNull(propC);
        assertEquals("propC.key().equals(\"two\")", "two", propC.key());
        assertEquals("propC.value().equals(1)", 2L, (long) propC.value());
        assertNotNull(propC.comment());
        assertEquals("propC.comment().equals(\"This is a two\")", "This is a two", propC.comment());
    }

}
