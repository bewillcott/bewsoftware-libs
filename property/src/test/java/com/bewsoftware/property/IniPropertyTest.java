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
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 */
public class IniPropertyTest
{

    public IniPropertyTest()
    {
    }

    /**
     * Test of constructors, of class IniProperty.
     */
    @Test
    public void testConstructors()
    {
        println("IniPropertyTest.testConstructors");

        IniProperty<Integer> propA = new IniProperty<>("one", 1);
        assertNotNull(propA);
        assertEquals("one", propA.key(), "propA.key().equals(\"one\")");
        assertEquals(1L, (long) propA.value(), "propA.value().equals(1)");
        assertNull(propA.comment());

        IniProperty<Integer> propB = new IniProperty<>("two", 2, "This is a two");
        assertNotNull(propB);
        assertEquals("two", propB.key(), "propB.key().equals(\"two\")");
        assertEquals(2L, (long) propB.value(), "propB.value().equals(2)");
        assertNotNull(propB.comment());
        assertEquals("This is a two", propB.comment(), "propB.comment().equals(\"This is a two\")");

        IniProperty<Integer> propC = new IniProperty<>(propB);
        assertNotNull(propC);
        assertEquals("two", propC.key(), "propC.key().equals(\"two\")");
        assertEquals(2L, (long) propC.value(), "propC.value().equals(1)");
        assertNotNull(propC.comment());
        assertEquals("This is a two", propC.comment(), "propC.comment().equals(\"This is a two\")");
        assertTrue(true);

        println("IniPropertyTest.testConstructors: Completed");
    }
}
