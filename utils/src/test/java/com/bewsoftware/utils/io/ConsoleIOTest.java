/*
 *  File Name:    ConsoleIOTest.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2024 Bradley Willcott
 *
 *  bewsoftware-utils is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  bewsoftware-utils is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bewsoftware.utils.io;

import org.junit.jupiter.api.Test;

import static com.bewsoftware.utils.io.DisplayDebugLevel.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 */
public class ConsoleIOTest
{
    public ConsoleIOTest()
    {
    }

    @Test
    public void printlnTest()
    {
        System.out.println("printlnTest\n-----------");

        DisplayDebugLevel level = DEFAULT;
        String text1 = "123456";
        String text2 = "7890";
        StringWriter sw = new StringWriter();
        Display display = ConsoleIO.consoleWriterDisplay("", "SW1", sw);
        display.println(text1);
        display.println(text2);
        String expResult = new StringBuffer()
                .append(text1).append('\n')
                .append(text2).append('\n')
                .toString();
        String result = sw.toString();

        System.out.println("\n\nexpResult: [" + expResult + "]");
        System.out.println("   result: [" + result + "]");

        assertEquals(expResult, result);
    }
}
