/*
 *  File Name:    GetTest.java
 *  Project Name: bewsoftware-optional
 *
 *  Copyright (c) 2021 Bradley Willcott
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.bewsoftware.optional.java.util.optional.map;

import com.bewsoftware.optional.java.util.OptionalMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * GetTest class description.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.1.0
 * @version 1.1.0
 */
public class GetTest
{

    private static Map<String, String> testMap;

    public GetTest()
    {
    }

    @BeforeAll
    public static void setUpClass()
    {
        testMap = new TreeMap<>();
        testMap.put("A", "Alpha");
        testMap.put("B", "Beta");
        testMap.put("C", "Charlie");
        testMap.put("D", "Delta");
    }

    @AfterAll
    public static void tearDownClass()
    {
        testMap.clear();
        testMap = null;
        System.gc();
    }

    @Test
    public void testInvalidText()
    {
        String s = "Fred Smith";
        Optional<String> expResult = Optional.empty();
        Optional<String> result = OptionalMap.get(testMap, s);
        assertEquals(expResult, result);
    }

    @Test
    public void testValidTextA()
    {
        String s = "A";
        Optional<String> expResult = Optional.of("Alpha");
        Optional<String> result = OptionalMap.get(testMap, s);
        assertEquals(expResult, result);
    }

    @Test
    public void testValidTextB()
    {
        String s = "B";
        Optional<String> expResult = Optional.of("Beta");
        Optional<String> result = OptionalMap.get(testMap, s);
        assertEquals(expResult, result);
    }

}
