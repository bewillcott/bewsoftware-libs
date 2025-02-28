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

import static com.bewsoftware.utils.Utils.println;
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

//    @Test
    public void multiThreadedTest()
    {
        println("multiThreadedTest\n-----------");

//        StringWriterPlus sw1 = new StringWriterPlus();
//        TestingTask task1 = new TestingTask("[T1]", "t1", sw1, 1);
//        Thread thread1 = new Thread(task1, "Thread-1");
//        thread1.start();
    }

    @Test
    public void printlnTest() throws Exception
    {
        println("printlnTest\n-----------");

        DisplayDebugLevel level = DEFAULT;
        String text1 = "123456";
        String text2 = "7890";
        StringWriterPlus sw = new StringWriterPlus();

//        final Thread t = new Thread(() ->
//        {
        final Display display = ConsoleIO.consoleWriterDisplay("", "SW1", sw);

        try (display)
        {
            display.println(text1);
            display.println(text2);
        } finally
        {
            if (display.isException())
            {
                println(display.popException());
            }
        }
//        });

//        t.start();
//        t.join(SECONDS.toMillis(5));
        String expResult = new StringBuffer()
                .append(text1).append('\n')
                .append(text2).append('\n')
                .toString();
        String result = sw.toString();

        assertEquals(expResult, result);
    }
}
