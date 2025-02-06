/*
 *  File Name:    Strings.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2021 Bradley Willcott
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

package com.bewsoftware.utils.string;

import static java.lang.Character.isWhitespace;

/**
 * This interface contains helper methods for modifying Strings.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.6
 * @version 3.0.2
 */
public interface Strings
{
    /**
     * centre fills the {@code number} within a text string of {@code width}
     * length.
     *
     * @param number to wrap
     * @param width  of required text
     *
     * @return the formatted text
     */
    static String centreFill(final int number, final int width)
    {
        return centreFill("" + number, width);
    }

    /**
     * Formats the {@code text} to be centred within a text string of
     * {@code width} length.
     *
     * @param text  to wrap
     * @param width of required text
     *
     * @return the formatted text
     */
    static String centreFill(final String text, final int width)
    {
        return centreFill(text, width, " ");
    }

    /**
     * centre fills the {@code number} within a text string of {@code width}
     * length.
     *
     * @param number to wrap
     * @param width  of required text
     * @param fill   text
     *
     * @return the formatted text
     */
    static String centreFill(final int number, final int width, final String fill)
    {
        return centreFill("" + number, width, fill);
    }

    /**
     * Formats the {@code text} to be centred within a text string of
     * {@code width} length.
     *
     * @param text  to wrap
     * @param width of required text
     * @param fill  text
     *
     * @return the formatted text
     */
    static String centreFill(final String text, final int width, final String fill)
    {
        String rtn;
        final int length = text.length();

        // Process only if necessary
        if (length < width)
        {
            final int preLen = (width - text.length()) / 2;
            final int postLen = width - text.length() - preLen;

            rtn = new StringBuilder()
                    .append(fill.repeat(preLen))
                    .append(text)
                    .append(fill.repeat(postLen))
                    .toString();

        } else
        {
            rtn = text;
        }

        return rtn;
    }

    /**
     * Fills out a new String with the <i>text</i> repeated <i>count</i> times.
     *
     * @implNote
     * Uses {@code text.repeat(count)} to fill out String.
     *
     * @param text  to repeat
     * @param count number of repeats
     *
     * @return new String
     *
     * @see java.lang.String#repeat(int)
     */
    static String fill(final String text, final int count)
    {
        return text.repeat(count);
    }

    /**
     * Format an array. Each element will be indented on a separate line.
     *
     * @param <T> Type of elements in the array.
     * @param arr of {@literal <T>} elements to layout.
     *
     * @return a String of the formatted array.
     */
    static <T> String formatArray(final T[] arr)
    {
        MessageBuilder mb = new MessageBuilder();

        mb.appendln('[');

        for (int i = 0; i < arr.length;)
        {
            mb.append("    ").append(arr[i++]);

            if (!(i < arr.length))
            {
                mb.appendln().appendln(']');
                break;
            }

            mb.appendln(',');
        }

        return mb.toString();
    }

    /**
     * Process a single 'line', either indenting or out-denting the text by the
     * number of 'spaces' required.
     *
     * @param line   to process.
     * @param spaces the number of spaces required.
     * <p>
     * Explanation of numeric ranges:<br>
     * {@code spaces} {@literal <} 0 outdent,<br>
     * {@code spaces} {@literal >} 0 indent.
     *
     * @return the processed line.
     */
    static String indentLine(final String line, final int spaces)
    {
        String rtn = "";

        if (!line.isEmpty())
        {
            if (spaces > 0)
            {
                rtn = fill(" ", spaces) + line;
            } else if (spaces < 0)
            {
                int i = 0;

                for (; i < Math.abs(spaces); i++)
                {
                    if (!isWhitespace(line.codePointAt(i)))
                    { // First non-whitespace character.
                        break;
                    }
                }

                if (i > 0)
                {
                    rtn = line.substring(i);
                } else
                {
                    rtn = line;
                }
            } else
            {
                rtn = line;
            }
        }

        return rtn;
    }

    /**
     * Process 'obj.toString()' (containing any number of lines), indenting or
     * out-denting the lines by the number of 'spaces' required.
     *
     * @param obj    to process
     * @param spaces the number of spaces required.
     * <p>
     * Explanation of numeric ranges:<br>
     * {@code spaces} {@literal <} 0 outdent,<br>
     * {@code spaces} {@literal >} 0 indent.
     *
     * @return the processed text
     */
    static String indentLines(final Object obj, final int spaces)
    {
        return indentLines((obj != null ? obj.toString() : "null"), spaces);
    }

    /**
     * Process the 'text' string (containing any number of lines), indenting or
     * out-denting the lines by the number of 'spaces' required.
     *
     * @param text   to process
     * @param spaces the number of spaces required.
     * <p>
     * Explanation of numeric ranges:<br>
     * {@code spaces} {@literal <} 0 outdent,<br>
     * {@code spaces} {@literal >} 0 indent.
     *
     * @return the processed text
     */
    static String indentLines(final String text, final int spaces)
    {
        final MessageBuilder mb = new MessageBuilder();

        if (!text.isBlank() && spaces != 0)
        {
            final boolean endsWithNewLine = text.endsWith("\n");
            final String[] arr = text.replace("\n", " \n").split("\n");

            for (int i = 0; i < arr.length; i++)
            {
                final String strLine = arr[i];

                if (i < arr.length - 1 || endsWithNewLine)
                {
                    final String line = strLine.substring(0, strLine.length() - 1);
                    mb.appendln(indentLine(line, spaces));
                } else
                {
                    mb.append(indentLine(strLine, spaces));
                }
            }
        } else
        {
            mb.append(text);
        }

        return mb.toString();
    }

    /**
     * Returns {@code true} if the provided string is either <i>null</i> or
     * {@linkplain String#isBlank() blank}; {@code false} otherwise.
     *
     * @param str a string to be checked.
     *
     * @return {@code true} if the provided string is either <i>null</i> or
     * <i>blank</i>; {@code false} otherwise.
     *
     * @since 3.0.2
     */
    static boolean isBlank(final String str)
    {
        return str == null || str.isBlank();
    }

    /**
     * Returns {@code true} if the provided string is either <i>null</i> or
     * {@linkplain String#isEmpty() empty}; {@code false} otherwise.
     *
     * @param str a string to be checked.
     *
     * @return {@code true} if the provided string is either <i>null</i> or
     * <i>empty</i>; {@code false} otherwise.
     *
     * @since 3.0.2
     */
    static boolean isEmpty(final String str)
    {
        return str == null || str.isEmpty();
    }

    /**
     * Trim all whitespace characters from the beginning of the text string.
     *
     * @deprecated Use {@link String#stripLeading()} instead.
     *
     * @param text to trim
     *
     * @return new String if trimmed, original String if no trimming needed,
     *         or <i>null</i> if {@code text} is <i>null</i>.
     *
     * @see Character#isWhitespace(char)
     */
    @Deprecated(forRemoval = true, since = "3.0.2")
    static String lTrim(final String text)
    {
        String rtn = "";

        if (text == null)
        {
            rtn = null;
        } else if (!text.isBlank())
        {
            int index = 0;

            while (Character.isWhitespace(text.charAt(index)))
            {
                index++;
            }

            rtn = text.substring(index);
        }

        return rtn;
    }

    /**
     * Formats the {@code number} to be left justified within a text string of
     * {@code width} length.
     * <p>
     * Appends spaces to the right of the 'number' so the final String is
     * 'width'
     * long.
     *
     * @param number to wrap
     * @param width  of required text
     *
     * @return the formatted text
     */
    static String leftJustify(final int number, final int width)
    {
        return Strings.leftJustify(Integer.toString(number), width);
    }

    /**
     * Formats the {@code text} to be left justified within a text string of
     * {@code width} length.
     * <p>
     * Appends spaces to the right of the 'text' so the final String is 'width'
     * long.
     *
     * @param text  to wrap
     * @param width of required text
     *
     * @return the formatted text
     */
    static String leftJustify(final String text, final int width)
    {
        String rtn = text.trim();
        int length = rtn.length();

        if (length < width)
        {
            rtn += " ".repeat(width - length);
        }

        return rtn;
    }

    /**
     * Formats the {@code text} to be left justified within a text string of
     * {@code width} length.
     * <p>
     * Appends spaces to the right of the 'text' so the final String is 'width'
     * long.
     *
     * @param text  to wrap
     * @param width of required text
     * @param fill  text
     *
     * @return the formatted text
     */
    static String leftJustify(final String text, final int width, final String fill)
    {
        String rtn = text.trim();
        final int length = rtn.length();

        if (length < width)
        {
            rtn += fill.repeat(width - length);
        }

        return rtn;
    }

    /**
     * Returns {@code true} if the provided string is neither <i>null</i>
     * nor {@linkplain String#isBlank() blank}; {@code false} otherwise.
     *
     * @param str a string to be checked.
     *
     * @return {@code true} if the provided reference is neither <i>null</i>
     * nor <i>blank</i>; {@code false} otherwise.
     *
     * @since 3.0.2
     */
    static boolean notBlank(final String str)
    {
        return str != null && !str.isBlank();
    }

    /**
     * Returns {@code true} if the provided string is neither <i>null</i>
     * nor {@linkplain String#isEmpty() empty}; {@code false} otherwise.
     *
     * @param str a string to be checked.
     *
     * @return {@code true} if the provided reference is neither <i>null</i>
     * nor <i>empty</i>; {@code false} otherwise.
     *
     * @since 3.0.2
     */
    static boolean notEmpty(final String str)
    {
        return str != null && !str.isEmpty();
    }

    /**
     * Trim all whitespace characters from the end of the text string.
     *
     * @deprecated Use {@link String#stripTrailing()} instead.
     *
     * @param text to trim
     *
     * @return new String if trimmed, original String if no trimming needed,
     *         or <i>null</i> if {@code text} is <i>null</i>.
     *
     * @see Character#isWhitespace(char)
     */
    @Deprecated(forRemoval = true, since = "3.0.2")
    static String rTrim(final String text)
    {
        String rtn = "";

        if (text == null)
        {
            rtn = null;
        } else if (!text.isBlank())
        {
            int index = text.length() - 1;

            while (Character.isWhitespace(text.charAt(index)))
            {
                index--;
            }

            rtn = text.substring(0, index + 1);
        }

        return rtn;
    }

    /**
     * Checks that the specified string isn't
     * {@linkplain String#isBlank() blank}.
     *
     * @param str The string to check for blankness.
     *
     * @return {@code str} if not <i>blank</i>.
     *
     * @throws NullPointerException     if {@code str} is <i>null</i>.
     * @throws IllegalArgumentException if {@code str} is <i>blank</i>.
     *
     * @see #requireNonEmpty(java.lang.String)
     */
    static String requireNonBlank(final String str) throws NullPointerException, IllegalArgumentException
    {
        if (str == null)
        {
            throw new NullPointerException();
        } else if (str.isBlank())
        {
            throw new IllegalArgumentException("isBlank.");
        }

        return str;
    }

    /**
     * Checks that the specified string isn't
     * {@linkplain String#isBlank() blank}.
     *
     * @param str  The string to check for blankness.
     * @param name of variable/parameter being tested.
     *
     * @return {@code str} if not <i>blank</i>.
     *
     * @throws NullPointerException     if {@code str} is <i>null</i>.
     * @throws IllegalArgumentException if {@code str} is <i>blank</i>.
     *
     * @see #requireNonEmpty(java.lang.String, java.lang.String)
     */
    static String requireNonBlank(final String str, final String name) throws NullPointerException, IllegalArgumentException
    {
        if (str == null)
        {
            throw new NullPointerException("isNull: " + (name == null
                    ? "" : name));
        } else if (str.isBlank())
        {
            throw new IllegalArgumentException("isBlank: " + (name == null
                    ? "" : name));
        }

        return str;
    }

    /**
     * Checks that the specified string isn't
     * {@linkplain String#isEmpty() empty}.
     *
     * @param str The string to check for emptiness.
     *
     * @return {@code str} if not <i>empty</i>.
     *
     * @throws NullPointerException     if {@code str} is <i>null</i>.
     * @throws IllegalArgumentException if {@code str} is <i>empty</i>.
     */
    static String requireNonEmpty(final String str) throws NullPointerException, IllegalArgumentException
    {
        if (str == null)
        {
            throw new NullPointerException();
        } else if (str.isEmpty())
        {
            throw new IllegalArgumentException("isEmpty.");
        }

        return str;
    }

    /**
     * Checks that the specified string isn't
     * {@linkplain String#isEmpty() empty}.
     *
     * @param str  The string to check for emptiness.
     * @param name of variable/parameter being tested.
     *
     * @return {@code str} if not <i>empty</i>.
     *
     * @throws NullPointerException     if {@code str} is <i>null</i>.
     * @throws IllegalArgumentException if {@code str} is <i>empty</i>.
     */
    static String requireNonEmpty(final String str, final String name) throws NullPointerException, IllegalArgumentException
    {
        if (str == null)
        {
            throw new NullPointerException("isNull: " + (name == null
                    ? "" : name));
        } else if (str.isEmpty())
        {
            throw new IllegalArgumentException("isEmpty: " + (name == null
                    ? "" : name));
        }

        return str;
    }

    /**
     * Right justifies the {@code number} within a text string of {@code width}
     * length.
     *
     * @param number to wrap
     * @param width  of required text
     *
     * @return the formatted text
     */
    static String rightJustify(final int number, final int width)
    {
        return rightJustify("" + number, width);
    }

    /**
     * Formats the {@code text} to be Right justified within a text string of
     * {@code width} length.
     *
     * @param text  to wrap
     * @param width of required text
     *
     * @return the formatted text
     */
    static String rightJustify(final String text, final int width)
    {
        return rightJustify(text, width, " ");
    }

    /**
     * Right justifies the {@code number} within a text string of {@code width}
     * length.
     *
     * @param number to wrap
     * @param width  of required text
     * @param fill   text
     *
     * @return the formatted text
     */
    static String rightJustify(final int number, final int width, final String fill)
    {
        return rightJustify("" + number, width, fill);
    }

    /**
     * Formats the {@code text} to be right justified within a text string of
     * {@code width} length.
     *
     * @param text  to wrap
     * @param width of required text
     * @param fill  text
     *
     * @return the formatted text
     */
    static String rightJustify(final String text, final int width, final String fill)
    {
        String rtn;
        final int length = text.length();

        // Process only if necessary
        if (length < width)
        {
            final int preLen = (width - length);

            rtn = fill.repeat(preLen) + text;

        } else
        {
            rtn = text;
        }

        return rtn;
    }
}
