/*
 *  File Name:    StringsTest.java
 *  Project Name: bewsoftware-utils
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
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bewsoftware.utils.string;

import java.util.Date;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 */
public class StringsTest
{

    public StringsTest()
    {
    }

    public static Stream<Arguments> provideArgsForIndentLine_String_int()
    {
        return Stream.of(
                Arguments.of(" ", 0, " "),
                Arguments.of(" ", 1, "  "),
                Arguments.of("X", 2, "  X"),
                Arguments.of("3", 3, "   3"),
                Arguments.of("  .", 4, "      ."),
                Arguments.of("*", 5, "     *"),
                Arguments.of(" ", -1, ""),
                Arguments.of("    X", -2, "  X"),
                Arguments.of("      3", -3, "   3"),
                Arguments.of("    .", -4, "."),
                Arguments.of("     *", -5, "*")
        );
    }

    public static Stream<Arguments> provideArgsForIndentLines_Object_int()
    {
        final Date date = new Date(1738476600000L);

        return Stream.of(
                Arguments.of(date, 0, "Sun Feb 02 14:10:00 AWST 2025"),
                Arguments.of(date, 1, " Sun Feb 02 14:10:00 AWST 2025"),
                Arguments.of(date, 2, "  Sun Feb 02 14:10:00 AWST 2025"),
                Arguments.of(date, 3, "   Sun Feb 02 14:10:00 AWST 2025"),
                Arguments.of("  " + date, 4, "      Sun Feb 02 14:10:00 AWST 2025"),
                Arguments.of(date, 5, "     Sun Feb 02 14:10:00 AWST 2025"),
                Arguments.of(" " + date, -1, "Sun Feb 02 14:10:00 AWST 2025"),
                Arguments.of("    " + date, -2, "  Sun Feb 02 14:10:00 AWST 2025"),
                Arguments.of("      " + date, -3, "   Sun Feb 02 14:10:00 AWST 2025"),
                Arguments.of("    " + date, -4, "Sun Feb 02 14:10:00 AWST 2025"),
                Arguments.of("     " + date, -5, "Sun Feb 02 14:10:00 AWST 2025")
        );
    }

    public static Stream<Arguments> provideArgsForTestCentreFill_String_int()
    {
        return Stream.of(
                Arguments.of("0", 0, "0"),
                Arguments.of("1", 1, "1"),
                Arguments.of("2", 2, "2 "),
                Arguments.of("3", 3, " 3 "),
                Arguments.of("4", 4, " 4  "),
                Arguments.of("5", 5, "  5  ")
        );
    }

    public static Stream<Arguments> provideArgsForTestCentreFill_int_int()
    {
        return Stream.of(
                Arguments.of(0, 0, "0"),
                Arguments.of(1, 1, "1"),
                Arguments.of(2, 2, "2 "),
                Arguments.of(3, 3, " 3 "),
                Arguments.of(4, 4, " 4  "),
                Arguments.of(5, 5, "  5  ")
        );
    }

    public static Stream<Arguments> provideArgsForTestFill_String_int()
    {
        return Stream.of(
                Arguments.of(" ", 0, ""),
                Arguments.of(" ", 1, " "),
                Arguments.of("X", 2, "XX"),
                Arguments.of("3", 3, "333"),
                Arguments.of(".", 4, "...."),
                Arguments.of("*", 5, "*****")
        );
    }

    public static Stream<Arguments> provideArgsForTestFormatArray_Array()
    {
        return Stream.of(
                Arguments.of(
                        new String[]
                        {
                            "This is some text",
                            "which will be",
                            "hopefully layed-out",
                            "properly."
                        },
                        "[\n"
                        + "    This is some text,\n"
                        + "    which will be,\n"
                        + "    hopefully layed-out,\n"
                        + "    properly.\n"
                        + "]\n"
                )
        );
    }

    public static Stream<Arguments> provideArgsForTestLeftJustify_String_int()
    {
        return Stream.of(
                Arguments.of(" ", 0, ""),
                Arguments.of(" ", 1, " "),
                Arguments.of("X", 2, "X "),
                Arguments.of("3", 3, "3  "),
                Arguments.of("  .", 4, ".   "),
                Arguments.of("*", 5, "*    ")
        );
    }

    public static Stream<Arguments> provideArgsForTestLeftJustify_int_int()
    {
        return Stream.of(
                Arguments.of(0, 0, "0"),
                Arguments.of(1, 1, "1"),
                Arguments.of(2, 2, "2 "),
                Arguments.of(3, 3, "3  "),
                Arguments.of(4, 4, "4   "),
                Arguments.of(5, 5, "5    ")
        );
    }

    public static Stream<Arguments> provideArgsForTestRequireNonBlank_String_String_IllegalArgumentException()
    {
        return Stream.of(
                Arguments.of("", ""),
                Arguments.of("  ", "")
        );
    }

    public static Stream<Arguments> provideArgsForTestRequireNonBlank_String_String_NullPointerException()
    {
        return Stream.of(
                Arguments.of(null, "Null pointer")
        );
    }

    public static Stream<Arguments> provideArgsForTestRequireNonEmpty_String_String_IllegalArgumentException()
    {
        return Stream.of(
                Arguments.of("", "")
        );
    }

    public static Stream<Arguments> provideArgsForTestRequireNonEmpty_String_String_NullPointerException()
    {
        return Stream.of(
                Arguments.of(null, "Null pointer")
        );
    }

    public static Stream<Arguments> provideArgsForTestRightJustify_String_int()
    {
        return Stream.of(
                Arguments.of(" ", 0, " "),
                Arguments.of(" ", 1, " "),
                Arguments.of("X", 2, " X"),
                Arguments.of("3", 3, "  3"),
                Arguments.of("  .", 4, "   ."),
                Arguments.of("*", 5, "    *")
        );
    }

    public static Stream<Arguments> provideArgsForTestRightJustify_int_int()
    {
        return Stream.of(
                Arguments.of(0, 0, "0"),
                Arguments.of(1, 1, "1"),
                Arguments.of(2, 2, " 2"),
                Arguments.of(3, 3, "  3"),
                Arguments.of(4, 4, "   4"),
                Arguments.of(5, 5, "    5")
        );
    }

    /**
     * Test of centreFill method.
     *
     * @param input
     * @param width
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestCentreFill_String_int")
    public void testCentreFill_String_int(final String input, final int width, final String expected)
    {
        final String result = Strings.centreFill(input, width);
        assertEquals(expected, result,
                "testCentreFill_String_int\n"
                + format("    Strings.centreFill(\"%s\", %d)%n", input, width)
        );
    }

    /**
     * Test of centreFill method.
     *
     * @param number
     * @param width
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestCentreFill_int_int")
    public void testCentreFill_int_int(final int number, final int width, final String expected)
    {
        final String result = Strings.centreFill(number, width);
        assertEquals(expected, result,
                "testCentreFill_int_int\n"
                + format("    Strings.centreFill(\"%d\", %d)%n", number, width)
        );
    }

    /**
     * Test of fill method.
     *
     * @param text
     * @param count
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestFill_String_int")
    public void testFill_String_int(final String text, final int count, final String expected)
    {
        final String result = Strings.fill(text, count);
        assertEquals(expected, result,
                "testFill_String_int\n"
                + format("    Strings.fill(\"%s\", %d)%n", text, count)
        );
    }

    /**
     * Test of formatArray method.
     *
     * @param <T>
     * @param arr
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestFormatArray_Array")
    public <T> void testFormatArray(final T[] arr, final String expected)
    {
        final String result = Strings.formatArray(arr);
        assertEquals(expected, result,
                "testFormatArray\n"
                + format("    Strings.formatArray(\"%n%s\")%n", arrayToString(arr))
        );
    }

    /**
     * Test of indentLine method.
     *
     * @param text
     * @param width
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("provideArgsForIndentLine_String_int")
    public void testIndentLine_String_int(final String text, final int width, final String expected)
    {
        final String result = Strings.indentLine(text, width);
        assertEquals(expected, result,
                "testIndentLine_String_int\n"
                + format("    Strings.indentLine(\"%s\", %d)%n", text, width)
        );
    }

    /**
     * Test of indentLines method.
     *
     * @param obj
     * @param width
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("provideArgsForIndentLines_Object_int")
    public void testIndentLines_Object_int(final Object obj, final int width, final String expected)
    {
        final String result = Strings.indentLines(obj, width);
        assertEquals(expected, result,
                "testIndentLines_Object_int\n"
                + format("    Strings.indentLines(\"%s\", %d)%n", obj, width)
        );
    }

    /**
     * Test of leftJustify method.
     *
     * @param text
     * @param width
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestLeftJustify_String_int")
    public void testLeftJustify_String_int(final String text, final int width, final String expected)
    {
        final String result = Strings.leftJustify(text, width);
        assertEquals(expected, result,
                "testLeftJustify_String_int\n"
                + format("    Strings.leftJustify(\"%s\", %d)%n", text, width)
        );
    }

    /**
     * Test of leftJustify method.
     *
     * @param number
     * @param width
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestLeftJustify_int_int")
    public void testLeftJustify_int_int(final int number, final int width, final String expected)
    {
        final String result = Strings.leftJustify(number, width);
        assertEquals(expected, result,
                "testLeftJustify_int_int\n"
                + format("    Strings.leftJustify(\"%d\", %d)%n", number, width)
        );
    }

    /**
     * Test of requireNonBlank method.
     *
     * @param text
     */
    @ParameterizedTest
    @ValueSource(
            strings =
            {
                "Non-blank"
            })
    public void testRequireNonBlank_String(final String text)
    {
        final String result = Strings.requireNonBlank(text);
        assertEquals(text, result,
                "testRequireNonBlank_String\n"
                + format("    Strings.requireNonBlank(\"%s\")%n", text)
        );
    }

    /**
     * Test of requireNonBlank method.
     *
     * @param text
     */
    @ParameterizedTest
    @ValueSource(
            strings =
            {
                "", "  "
            })
    @SuppressWarnings("ThrowableResultIgnored")
    public void testRequireNonBlank_String_IllegalArgumentException(final String text)
    {
        assertThrows(
                IllegalArgumentException.class,
                () -> Strings.requireNonBlank(text),
                "testRequireNonBlank_String_IllegalArgumentException\n"
                + format("    Strings.requireNonBlank(\"%s\")%n", text)
        );
    }

    /**
     * Test of requireNonBlank method.
     *
     * @param text
     */
    @ParameterizedTest
    @NullSource
    @SuppressWarnings("ThrowableResultIgnored")
    public void testRequireNonBlank_String_NullPointerException(final String text)
    {
        assertThrows(
                NullPointerException.class,
                () -> Strings.requireNonBlank(text),
                "testRequireNonBlank_String_NullPointerException\n"
                + format("    Strings.requireNonBlank(\"%s\")%n", text)
        );
    }

    /**
     * Test of requireNonBlank method.
     *
     * @param text
     * @param msg
     */
    @ParameterizedTest
    @CsvSource(value =
    {
        "test:test", "test2:test", "Java:Java"
    }, delimiter = ':')
    public void testRequireNonBlank_String_String(final String text, final String msg)
    {
        final String result = Strings.requireNonBlank(text, msg);
        assertEquals(text, text,
                "testRequireNonBlank_String_String\n"
                + format("    Strings.requireNonBlank(\"%s\", \"%s\")%n", text, msg)
        );
    }

    /**
     * Test of requireNonBlank method.
     *
     * @param text
     * @param msg
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestRequireNonBlank_String_String_IllegalArgumentException")
    @SuppressWarnings("ThrowableResultIgnored")
    public void testRequireNonBlank_String_String_IllegalArgumentException(final String text, final String msg)
    {
        assertThrows(
                IllegalArgumentException.class,
                () -> Strings.requireNonBlank(text, msg),
                "testRequireNonBlank_String_String_IllegalArgumentException\n"
                + format("    Strings.requireNonBlank(\"%s\", \"%s\")%n", text, msg)
        );
    }

    /**
     * Test of requireNonBlank method.
     *
     * @param text
     * @param msg
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestRequireNonBlank_String_String_NullPointerException")
    @SuppressWarnings("ThrowableResultIgnored")
    public void testRequireNonBlank_String_String_NullPointerException(final String text, final String msg)
    {
        assertThrows(
                NullPointerException.class,
                () -> Strings.requireNonBlank(text, msg),
                "testRequireNonBlank_String_String_NullPointerException\n"
                + format("    Strings.requireNonBlank(\"%s\", \"%s\")%n", text, msg)
        );
    }

    /**
     * Test of requireNonBlank method.
     *
     * @param text
     */
    @ParameterizedTest
    @ValueSource(
            strings =
            {
                "Non-Empty"
            })
    public void testRequireNonEmpty_String(final String text)
    {
        final String result = Strings.requireNonEmpty(text);
        assertEquals(text, result,
                "testRequireNonEmpty_String\n"
                + format("    Strings.requireNonEmpty(\"%s\")%n", text)
        );
    }

    /**
     * Test of requireNonBlank method.
     *
     * @param text
     */
    @ParameterizedTest
    @ValueSource(
            strings =
            {
                ""
            })
    @SuppressWarnings("ThrowableResultIgnored")
    public void testRequireNonEmpty_String_IllegalArgumentException(final String text)
    {
        assertThrows(
                IllegalArgumentException.class,
                () -> Strings.requireNonEmpty(text),
                "testRequireNonEmpty_String_IllegalArgumentException\n"
                + format("    Strings.requireNonEmpty(\"%s\")%n", text)
        );
    }

    /**
     * Test of requireNonBlank method.
     *
     * @param text
     */
    @ParameterizedTest
    @NullSource
    @SuppressWarnings("ThrowableResultIgnored")
    public void testRequireNonEmpty_String_NullPointerException(final String text)
    {
        assertThrows(
                NullPointerException.class,
                () -> Strings.requireNonEmpty(text),
                "testRequireNonEmpty_String_NullPointerException\n"
                + format("    Strings.requireNonEmpty(\"%s\")%n", text)
        );
    }

    /**
     * Test of requireNonBlank method.
     *
     * @param text
     * @param msg
     */
    @ParameterizedTest
    @CsvSource(value =
    {
        "test:test", "test2:test", "Java:Java"
    }, delimiter = ':')
    public void testRequireNonEmpty_String_String(final String text, final String msg)
    {
        final String result = Strings.requireNonEmpty(text, msg);
        assertEquals(text, text,
                "testRequireNonEmpty_String_String\n"
                + format("    Strings.requireNonEmpty(\"%s\", \"%s\")%n", text, msg)
        );
    }

    /**
     * Test of requireNonBlank method.
     *
     * @param text
     * @param msg
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestRequireNonEmpty_String_String_IllegalArgumentException")
    @SuppressWarnings("ThrowableResultIgnored")
    public void testRequireNonEmpty_String_String_IllegalArgumentException(final String text, final String msg)
    {
        assertThrows(
                IllegalArgumentException.class,
                () -> Strings.requireNonEmpty(text, msg),
                "testRequireNonEmpty_String_String_IllegalArgumentException\n"
                + format("    Strings.requireNonEmpty(\"%s\", \"%s\")%n", text, msg)
        );
    }

    /**
     * Test of requireNonBlank method.
     *
     * @param text
     * @param msg
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestRequireNonEmpty_String_String_NullPointerException")
    @SuppressWarnings("ThrowableResultIgnored")
    public void testRequireNonEmpty_String_String_NullPointerException(final String text, final String msg)
    {
        assertThrows(
                NullPointerException.class,
                () -> Strings.requireNonEmpty(text, msg),
                "testRequireNonEmpty_String_String_NullPointerException\n"
                + format("    Strings.requireNonEmpty(\"%s\", \"%s\")%n", text, msg)
        );
    }

    /**
     * Test of rightJustify method.
     *
     * @param text
     * @param width
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestRightJustify_String_int")
    public void testRightJustify_String_int(final String text, final int width, final String expected)
    {
        final String result = Strings.rightJustify(text, width);
        assertEquals(expected, result,
                "testRightJustify_String_int\n"
                + format("    Strings.rightJustify(\"%s\", %d)%n", text, width)
        );
    }

    /**
     * Test of rightJustify method.
     *
     * @param number
     * @param width
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestRightJustify_int_int")
    public void testRightJustify_int_int(final int number, final int width, final String expected)
    {
        final String result = Strings.rightJustify(number, width);
        assertEquals(expected, result,
                "testRightJustify_int_int\n"
                + format("    Strings.rightJustify(\"%d\", %d)%n", number, width)
        );
    }

    /**
     * Quick convert of an array to a text string for display purposes.
     *
     * @param <T>
     * @param arr
     *
     * @return
     */
    private <T> String arrayToString(final T[] arr)
    {
        final MessageBuilder mb = new MessageBuilder();

        for (T element : arr)
        {
            mb.appendln(element);
        }

        return mb.toString();
    }
}
