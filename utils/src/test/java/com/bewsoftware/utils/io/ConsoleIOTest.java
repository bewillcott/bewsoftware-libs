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

import java.io.Writer;
import java.util.Scanner;
import org.junit.jupiter.api.*;

import static com.bewsoftware.utils.io.DisplayDebugLevel.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 */
public class ConsoleIOTest
{

    public ConsoleIOTest()
    {
    }

    @BeforeAll
    public static void setUpClass()
    {
    }

    @AfterAll
    public static void tearDownClass()
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
        Display display = ConsoleIO.consoleWriterDisplay("", sw, "SW1");
        display.println(text1);
        display.println(text2);
        String expResult = new StringBuffer()
                .append(text1).append('\n')
                .append(text2).append('\n')
                .toString();
        String result = sw.toString();

        assertEquals(expResult, result);
    }

    @BeforeEach
    public void setUp()
    {
    }

    @AfterEach
    public void tearDown()
    {
    }

    /**
     * Test of append method, of class ConsoleIO.
     */
    // @Test
    public void testAppend_3args()
    {
        System.out.println("append");
        DisplayDebugLevel level = null;
        String format = "";
        Object[] args = null;
        ConsoleIO instance = null;
        Display expResult = null;
        Display result = instance.append(level, format, args);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of append method, of class ConsoleIO.
     */
    // @Test
    public void testAppend_DisplayDebugLevel_String()
    {
        System.out.println("append");
        DisplayDebugLevel level = null;
        String text = "";
        ConsoleIO instance = null;
        Display expResult = null;
        Display result = instance.append(level, text);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of blankDisplay method, of class ConsoleIO.
     */
    // @Test
    public void testBlankDisplay()
    {
        System.out.println("blankDisplay");
        Display expResult = null;
        Display result = ConsoleIO.blankDisplay();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clear method, of class ConsoleIO.
     */
    // @Test
    public void testClear()
    {
        System.out.println("clear");
        ConsoleIO instance = null;
        Display expResult = null;
        Display result = instance.clear();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clearExceptions method, of class ConsoleIO.
     */
    // @Test
    public void testClearExceptions()
    {
        System.out.println("clearExceptions");
        ConsoleIO instance = null;
        instance.clearExceptions();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of close method, of class ConsoleIO.
     */
    // @Test
    public void testClose() throws Exception
    {
        System.out.println("close");
        ConsoleIO instance = null;
        instance.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of consoleDisplay method, of class ConsoleIO.
     */
    // @Test
    public void testConsoleDisplay()
    {
        System.out.println("consoleDisplay");
        String linePrefix = "";
        Display expResult = null;
        Display result = ConsoleIO.consoleDisplay(linePrefix);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of consoleFileDisplay method, of class ConsoleIO.
     */
    // @Test
    public void testConsoleFileDisplay()
    {
        System.out.println("consoleFileDisplay");
        String linePrefix = "";
        String filename = "";
        Display expResult = null;
        Display result = ConsoleIO.consoleFileDisplay(linePrefix, filename);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of consoleWriterDisplay method, of class ConsoleIO.
     */
    // @Test
    public void testConsoleWriterDisplay()
    {
        System.out.println("consoleWriterDisplay");
        String linePrefix = "";
        Writer writer = null;
        String ident = "";
        Display expResult = null;
        Display result = ConsoleIO.consoleWriterDisplay(linePrefix, writer, ident);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of debugLevel method, of class ConsoleIO.
     */
    // @Test
    public void testDebugLevel_0args()
    {
        System.out.println("debugLevel");
        ConsoleIO instance = null;
        DisplayDebugLevel expResult = null;
        DisplayDebugLevel result = instance.debugLevel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of debugLevel method, of class ConsoleIO.
     */
    // @Test
    public void testDebugLevel_DisplayDebugLevel()
    {
        System.out.println("debugLevel");
        DisplayDebugLevel level = null;
        ConsoleIO instance = null;
        instance.debugLevel(level);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of displayOK method, of class ConsoleIO.
     */
    // @Test
    public void testDisplayOK_0args()
    {
        System.out.println("displayOK");
        ConsoleIO instance = null;
        boolean expResult = false;
        boolean result = instance.displayOK();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of displayOK method, of class ConsoleIO.
     */
    // @Test
    public void testDisplayOK_DisplayDebugLevel()
    {
        System.out.println("displayOK");
        DisplayDebugLevel level = null;
        ConsoleIO instance = null;
        boolean expResult = false;
        boolean result = instance.displayOK(level);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fileDisplay method, of class ConsoleIO.
     */
    // @Test
    public void testFileDisplay()
    {
        System.out.println("fileDisplay");
        String linePrefix = "";
        String filename = "";
        Display expResult = null;
        Display result = ConsoleIO.fileDisplay(linePrefix, filename);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of flush method, of class ConsoleIO.
     */
    // @Test
    public void testFlush()
    {
        System.out.println("flush");
        ConsoleIO instance = null;
        instance.flush();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isException method, of class ConsoleIO.
     */
    // @Test
    public void testIsException()
    {
        System.out.println("isException");
        ConsoleIO instance = null;
        boolean expResult = false;
        boolean result = instance.isException();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of level method, of class ConsoleIO.
     */
    // @Test
    public void testLevel()
    {
        System.out.println("level");
        DisplayDebugLevel level = null;
        ConsoleIO instance = null;
        Display expResult = null;
        Display result = instance.level(level);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of newScanner method, of class ConsoleIO.
     */
    // @Test
    public void testNewScanner()
    {
        System.out.println("newScanner");
        ConsoleIO instance = null;
        Scanner expResult = null;
        Scanner result = instance.newScanner();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of popException method, of class ConsoleIO.
     */
    // @Test
    public void testPopException()
    {
        System.out.println("popException");
        ConsoleIO instance = null;
        Exception expResult = null;
        Exception result = instance.popException();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readLine method, of class ConsoleIO.
     */
    // @Test
    public void testReadLine_0args()
    {
        System.out.println("readLine");
        ConsoleIO instance = null;
        String expResult = "";
        String result = instance.readLine();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readLine method, of class ConsoleIO.
     */
    // @Test
    public void testReadLine_String_ObjectArr()
    {
        System.out.println("readLine");
        String fmt = "";
        Object[] args = null;
        ConsoleIO instance = null;
        String expResult = "";
        String result = instance.readLine(fmt, args);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readPassword method, of class ConsoleIO.
     */
    // @Test
    public void testReadPassword_0args()
    {
        System.out.println("readPassword");
        ConsoleIO instance = null;
        char[] expResult = null;
        char[] result = instance.readPassword();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readPassword method, of class ConsoleIO.
     */
    // @Test
    public void testReadPassword_String_ObjectArr()
    {
        System.out.println("readPassword");
        String fmt = "";
        Object[] args = null;
        ConsoleIO instance = null;
        char[] expResult = null;
        char[] result = instance.readPassword(fmt, args);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writerDisplay method, of class ConsoleIO.
     */
    // @Test
    public void testWriterDisplay()
    {
        System.out.println("writerDisplay");
        String linePrefix = "";
        Writer writer = null;
        String ident = "";
        Display expResult = null;
        Display result = ConsoleIO.writerDisplay(linePrefix, writer, ident);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
