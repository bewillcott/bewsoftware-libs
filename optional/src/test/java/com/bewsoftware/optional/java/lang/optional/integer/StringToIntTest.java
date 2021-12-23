/*
 *  File Name:    StringToIntTest.java
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

package com.bewsoftware.optional.java.lang.optional.integer;

import com.bewsoftware.optional.java.lang.OptionalInteger;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * StringToIntTest class description.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public class StringToIntTest
{
    public StringToIntTest()
    {
    }

    @Test
    public void testInvalidText()
    {
        String s = "Fred Smith";
        Optional<Integer> expResult = Optional.empty();
        Optional<Integer> result = OptionalInteger.stringToInt(s);
        assertEquals(expResult, result);
    }

    @Test
    public void testNegitiveNumber()
    {
        String s = "-10";
        Optional<Integer> expResult = Optional.of(-10);
        Optional<Integer> result = OptionalInteger.stringToInt(s);
        assertEquals(expResult, result);
    }

    @Test
    public void testNull()
    {
        String s = null;
        Optional<Integer> expResult = Optional.empty();
        Optional<Integer> result = OptionalInteger.stringToInt(s);
        assertEquals(expResult, result);
    }

    @Test
    public void testPositiveNumber()
    {
        String s = "10";
        Optional<Integer> expResult = Optional.of(10);
        Optional<Integer> result = OptionalInteger.stringToInt(s);
        assertEquals(expResult, result);
    }

}
