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

package test.com.bewsoftware.utils.string;

import com.bewsoftware.utils.string.Strings;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

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

    public static Stream<Arguments> provideArgsForTestLTrim()
    {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", ""),
                Arguments.of("  ", ""),
                Arguments.of("not blank", "not blank"),
                Arguments.of("  left spaces", "left spaces"),
                Arguments.of("right spaces   ", "right spaces   "),
                Arguments.of("   front and back spaces   ", "front and back spaces   ")
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

    public static Stream<Arguments> provideArgsForTestRTrim()
    {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", ""),
                Arguments.of("  ", ""),
                Arguments.of("not blank", "not blank"),
                Arguments.of("   left spaces", "   left spaces"),
                Arguments.of("right spaces   ", "right spaces"),
                Arguments.of("   front and back spaces   ", "   front and back spaces")
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

    /**
     * Test of centreFill method, of class Strings.
     *
     * @param input
     * @param width
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestCentreFill_String_int")
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testCentreFill_String_int(String input, int width, String expected)
    {
        String result = Strings.centreFill(input, width);
        assertEquals(expected, result, () ->
        {
            System.out.println("testCentreFill_String_int");
            System.out.println("  Strings.centreFill(\"" + input + "\", " + width + ")");
            System.out.println("  Expected: <" + expected + "> but was: <" + result + ">");
            return "";
        });
    }

    /**
     * Test of centreFill method, of class Strings.
     *
     * @param number
     * @param width
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestCentreFill_int_int")
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testCentreFill_int_int(int number, int width, String expected)
    {
        String result = Strings.centreFill(number, width);
        assertEquals(expected, result, () ->
        {
            System.out.println("testCentreFill_int_int");
            System.out.println("  Strings.centreFill(" + number + ", " + width + ")");
            System.out.println("  Expected: <" + expected + "> but was: <" + result + ">");
            return "";
        });
    }

    /**
     * Test of fill method, of class Strings.
     *
     * @param text
     * @param count
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestFill_String_int")
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testFill(String text, int count, String expected)
    {
        String result = Strings.fill(text, count);
        assertEquals(expected, result, () ->
        {
            System.out.println("testFill");
            System.out.println("  Strings.fill(\"" + text + "\", " + count + ")");
            System.out.println("  Expected: <" + expected + "> but was: <" + result + ">");
            return "";
        });
    }

    /**
     * Test of lTrim method, of class Strings.
     *
     * @param input
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestLTrim")
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testLTrim(String input, String expected)
    {
        String result = Strings.lTrim(input);
        assertEquals(expected, result, () ->
        {
            System.out.println("testLTrim");
            System.out.println("  Strings.lTrim(\"" + input + "\")");
            System.out.println("  Expected: <" + expected + "> but was: <" + result + ">");
            return "";
        });
    }

    /**
     * Test of leftJustify method, of class Strings.
     *
     * @param text
     * @param width
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestLeftJustify_String_int")
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testLeftJustify_String_int(String text, int width, String expected)
    {
        String result = Strings.leftJustify(text, width);
        assertEquals(expected, result, () ->
        {
            System.out.println("testLeftJustify_String_int");
            System.out.println("  Strings.leftJustify(\"" + text + "\", " + width + ")");
            System.out.println("  Expected: <" + expected + "> but was: <" + result + ">");
            return "";
        });
    }

    /**
     * Test of leftJustify method, of class Strings.
     *
     * @param number
     * @param width
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestLeftJustify_int_int")
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testLeftJustify_int_int(int number, int width, String expected)
    {
        String result = Strings.leftJustify(number, width);
        assertEquals(expected, result, () ->
        {
            System.out.println("testLeftJustify_int_int");
            System.out.println("  Strings.leftJustify(" + number + ", " + width + ")");
            System.out.println("  Expected: <" + expected + "> but was: <" + result + ">");
            return "";
        });
    }

    /**
     * Test of rTrim method, of class Strings.
     *
     * @param input
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestRTrim")
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testRTrim(String input, String expected)
    {
        String result = Strings.rTrim(input);
        assertEquals(expected, result, () ->
        {
            System.out.println("testRTrim");
            System.out.println("  Strings.rTrim(\"" + input + "\")");
            System.out.println("  Expected: <" + expected + "> but was: <" + result + ">");
            return "";
        });
    }

    /**
     * Test of requireNonBlank method, of class Strings.
     *
     * @param text
     */
    @ParameterizedTest
    @ValueSource(
            strings =
            {
                "Non-blank"
            })
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testRequireNonBlank_String(String text)
    {
        String result = Strings.requireNonBlank(text);
        assertEquals(text, result, () ->
        {
            System.out.println("testRequireNonBlank_String");
            System.out.println("  Strings.requireNonBlank(\"" + text + "\")");
            System.out.println("  Expected: <" + text + "> but was: <" + result + ">");
            return "";
        });
    }

    /**
     * Test of requireNonBlank method, of class Strings.
     *
     * @param text
     */
    @ParameterizedTest
    @ValueSource(
            strings =
            {
                "", "  "
            })
    @SuppressWarnings(
            {
                "ThrowableResultIgnored", "UseOfSystemOutOrSystemErr"
            })
    public void testRequireNonBlank_String_IllegalArgumentException(String text)
    {
        assertThrows(
                IllegalArgumentException.class,
                () -> Strings.requireNonBlank(text),
                () ->
        {
            System.out.println("testRequireNonBlank_String_IllegalArgumentException");
            System.out.println("  Strings.requireNonBlank(\"" + text + "\")");
            System.out.println("  Expected: <IllegalArgumentException> but was: <>");
            return "";
        }
        );
    }

    /**
     * Test of requireNonBlank method, of class Strings.
     *
     * @param text
     */
    @ParameterizedTest
    @NullSource
    @SuppressWarnings(
            {
                "ThrowableResultIgnored", "UseOfSystemOutOrSystemErr"
            })
    public void testRequireNonBlank_String_NullPointerException(String text)
    {
        assertThrows(
                NullPointerException.class,
                () -> Strings.requireNonBlank(text),
                () ->
        {
            System.out.println("testRequireNonBlank_String_NullPointerException");
            System.out.println("  Strings.requireNonBlank(\"" + text + "\")");
            System.out.println("  Expected: <IllegalArgumentException> but was: <>");
            return "";
        }
        );
    }

    /**
     * Test of requireNonBlank method, of class Strings.
     *
     * @param text
     * @param msg
     */
    @ParameterizedTest
    @CsvSource(value =
    {
        "test:test", "test2:test", "Java:Java"
    }, delimiter = ':')
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testRequireNonBlank_String_String(String text, String msg)
    {
        String result = Strings.requireNonBlank(text, msg);
        assertEquals(text, text, () ->
        {
            System.out.println("testRequireNonBlank_String_String");
            System.out.println("  Strings.requireNonBlank(\"" + text + "\", \"" + msg + "\")");
            System.out.println("  Expected: <" + text + "> but was: <" + result + ">");
            return "";
        });
    }

    /**
     * Test of requireNonBlank method, of class Strings.
     *
     * @param text
     * @param msg
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestRequireNonBlank_String_String_IllegalArgumentException")
    @SuppressWarnings(
            {
                "ThrowableResultIgnored", "UseOfSystemOutOrSystemErr"
            })
    public void testRequireNonBlank_String_String_IllegalArgumentException(String text, String msg)
    {
        assertThrows(
                IllegalArgumentException.class,
                () -> Strings.requireNonBlank(text, msg),
                () ->
        {
            System.out.println("testRequireNonBlank_String_String_IllegalArgumentException");
            System.out.println("  Strings.requireNonBlank(\"" + text + "\", \"" + msg + "\")");
            System.out.println("  Expected: <IllegalArgumentException> but was: <>");
            return "";
        }
        );
    }

    /**
     * Test of requireNonBlank method, of class Strings.
     *
     * @param text
     * @param msg
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestRequireNonBlank_String_String_NullPointerException")
    @SuppressWarnings(
            {
                "ThrowableResultIgnored", "UseOfSystemOutOrSystemErr"
            })
    public void testRequireNonBlank_String_String_NullPointerException(String text, String msg)
    {
        assertThrows(
                NullPointerException.class,
                () -> Strings.requireNonBlank(text, msg),
                () ->
        {
            System.out.println("testRequireNonBlank_String_String_NullPointerException");
            System.out.println("  Strings.requireNonBlank(\"" + text + "\", \"" + msg + "\")");
            System.out.println("  Expected: <NullPointerException> but was: <>");
            return "";
        }
        );
    }

    /**
     * Test of requireNonBlank method, of class Strings.
     *
     * @param text
     */
    @ParameterizedTest
    @ValueSource(
            strings =
            {
                "Non-Empty"
            })
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testRequireNonEmpty_String(String text)
    {
        String result = Strings.requireNonEmpty(text);
        assertEquals(text, result, () ->
        {
            System.out.println("testRequireNonEmpty_String");
            System.out.println("  Strings.requireNonEmpty(\"" + text + "\")");
            System.out.println("  Expected: <" + text + "> but was: <" + result + ">");
            return "";
        });
    }

    /**
     * Test of requireNonBlank method, of class Strings.
     *
     * @param text
     */
    @ParameterizedTest
    @ValueSource(
            strings =
            {
                ""
            })
    @SuppressWarnings(
            {
                "ThrowableResultIgnored", "UseOfSystemOutOrSystemErr"
            })
    public void testRequireNonEmpty_String_IllegalArgumentException(String text)
    {
        assertThrows(
                IllegalArgumentException.class,
                () -> Strings.requireNonEmpty(text),
                () ->
        {
            System.out.println("testRequireNonEmpty_String_IllegalArgumentException");
            System.out.println("  Strings.requireNonEmpty(\"" + text + "\")");
            System.out.println("  Expected: <IllegalArgumentException> but was: <>");
            return "";
        }
        );
    }

    /**
     * Test of requireNonBlank method, of class Strings.
     *
     * @param text
     */
    @ParameterizedTest
    @NullSource
    @SuppressWarnings(
            {
                "ThrowableResultIgnored", "UseOfSystemOutOrSystemErr"
            })
    public void testRequireNonEmpty_String_NullPointerException(String text)
    {
        assertThrows(
                NullPointerException.class,
                () -> Strings.requireNonEmpty(text),
                () ->
        {
            System.out.println("testRequireNonEmpty_String_NullPointerException");
            System.out.println("  Strings.requireNonEmpty(\"" + text + "\")");
            System.out.println("  Expected: <NullPointerException> but was: <>");
            return "";
        }
        );
    }

    /**
     * Test of requireNonBlank method, of class Strings.
     *
     * @param text
     * @param msg
     */
    @ParameterizedTest
    @CsvSource(value =
    {
        "test:test", "test2:test", "Java:Java"
    }, delimiter = ':')
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testRequireNonEmpty_String_String(String text, String msg)
    {
        String result = Strings.requireNonEmpty(text, msg);
        assertEquals(text, text, () ->
        {
            System.out.println("testRequireNonEmpty_String_String");
            System.out.println("  Strings.requireNonEmpty(\"" + text + "\", \"" + msg + "\")");
            System.out.println("  Expected: <" + text + "> but was: <" + result + ">");
            return "";
        });
    }

    /**
     * Test of requireNonBlank method, of class Strings.
     *
     * @param text
     * @param msg
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestRequireNonEmpty_String_String_IllegalArgumentException")
    @SuppressWarnings(
            {
                "ThrowableResultIgnored", "UseOfSystemOutOrSystemErr"
            })
    public void testRequireNonEmpty_String_String_IllegalArgumentException(String text, String msg)
    {
        assertThrows(
                IllegalArgumentException.class,
                () -> Strings.requireNonEmpty(text, msg),
                () ->
        {
            System.out.println("testRequireNonEmpty_String_String_IllegalArgumentException");
            System.out.println("  Strings.requireNonEmpty(\"" + text + "\", \"" + msg + "\")");
            System.out.println("  Expected: <IllegalArgumentException> but was: <>");
            return "";
        }
        );
    }

    /**
     * Test of requireNonBlank method, of class Strings.
     *
     * @param text
     * @param msg
     */
    @ParameterizedTest
    @MethodSource("provideArgsForTestRequireNonEmpty_String_String_NullPointerException")
    @SuppressWarnings(
            {
                "ThrowableResultIgnored", "UseOfSystemOutOrSystemErr"
            })
    public void testRequireNonEmpty_String_String_NullPointerException(String text, String msg)
    {
        assertThrows(
                NullPointerException.class,
                () -> Strings.requireNonEmpty(text, msg),
                () ->
        {
            System.out.println("testRequireNonEmpty_String_String_NullPointerException");
            System.out.println("  Strings.requireNonEmpty(\"" + text + "\", \"" + msg + "\")");
            System.out.println("  Expected: <NullPointerException> but was: <>");
            return "";
        }
        );
    }

}
