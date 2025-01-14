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

import java.util.Formatter;
import java.util.Objects;

import static java.lang.Character.isWhitespace;

/**
 * This interface contains helper methods for modifying Strings.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.6
 * @version 3.0.0
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
        return centreFill(Integer.toString(number), width);
    }

    /**
     * Formats the {@code text} to be centred within a text string of
     * {@code width}
     * length.
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
        return centreFill(Integer.toString(number), width, fill);
    }

    /**
     * Formats the {@code text} to be centred within a text string of
     * {@code width}
     * length.
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
     * Process a single 'line', either indenting or outdenting the text by the
     * number of 'spaces' required.
     *
     * @param line   to process
     * @param spaces required: # {@literal <} 0 outdent, # {@literal >} 0
     *               indent.
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
     * @param spaces required: # {@literal <} 0 outdent, # {@literal >} 0
     *               indent.
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
     * @param spaces required: # {@literal <} 0 outdent, # {@literal >} 0
     *               indent.
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
        }

        return mb.toString();
    }

    /**
     * Trim all whitespace characters from the beginning of the text string.
     *
     * @param text to trim
     *
     * @return new String if trimmed, original String if no trimming needed,
     *         or {@code null} if 'text' is {@code null}..
     *
     * @see Character#isWhitespace(char)
     */
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
     * Trim all whitespace characters from the end of the text string.
     *
     * @param text to trim
     *
     * @return new String if trimmed, original String if no trimming needed,
     *         or {@code null} if 'text' is {@code null}..
     *
     * @see Character#isWhitespace(char)
     */
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
     * Checks that the specified string isn't <i>blank</i>.
     * <dl>
     * <dt>blank:</dt>
     * <dd>The string is either <i>empty</i> or contains only white space
     * code-points.</dd>
     * <dt>empty:</dt>
     * <dd>The string's {@code length} is {@code 0}.</dd>
     * </dl>
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
    static String requireNonBlank(String str)
    {
        if (Objects.requireNonNull(str).isBlank())
        {
            throw new IllegalArgumentException("isBlank.");
        }

        return str;
    }

    /**
     * Checks that the specified string isn't <i>blank</i>.
     * <dl>
     * <dt>blank:</dt>
     * <dd>The string is either <i>empty</i> or contains only white space
     * code-points.</dd>
     * <dt>empty:</dt>
     * <dd>The string's {@code length} is {@code 0}.</dd>
     * </dl>
     *
     * @param str     The string to check for blankness.
     * @param message Detail message to be used in the event that an exception
     *                is thrown.
     *
     * @return {@code str} if not <i>blank</i>.
     *
     * @throws NullPointerException     if {@code str} is <i>null</i>.
     * @throws IllegalArgumentException if {@code str} is <i>blank</i>.
     *
     * @see #requireNonEmpty(java.lang.String, java.lang.String)
     */
    static String requireNonBlank(String str, String message)
    {
        if (Objects.requireNonNull(str, message).isBlank())
        {
            throw new IllegalArgumentException("isBlank: " + message);
        }

        return str;
    }

    /**
     * Checks that the specified string isn't <i>empty</i>.
     * <dl>
     * <dt>empty:</dt>
     * <dd>The string's {@code length} is {@code 0}.</dd>
     * </dl>
     *
     * @param str The string to check for emptiness.
     *
     * @return {@code str} if not <i>empty</i>.
     *
     * @throws NullPointerException     if {@code str} is <i>null</i>.
     * @throws IllegalArgumentException if {@code str} is <i>empty</i>.
     */
    static String requireNonEmpty(String str)
    {
        if (Objects.requireNonNull(str).isEmpty())
        {
            throw new IllegalArgumentException("isEmpty.");
        }

        return str;
    }

    /**
     * Checks that the specified string isn't <i>empty</i>.
     * <dl>
     * <dt>empty:</dt>
     * <dd>The string's {@code length} is {@code 0}.</dd>
     * </dl>
     *
     * @param str     The string to check for emptiness.
     * @param message Detail message to be used in the event that an exception
     *                is thrown.
     *
     * @return {@code str} if not <i>empty</i>.
     *
     * @throws NullPointerException     if {@code str} is <i>null</i>.
     * @throws IllegalArgumentException if {@code str} is <i>empty</i>.
     */
    static String requireNonEmpty(String str, String message)
    {
        if (Objects.requireNonNull(str, message).isEmpty())
        {
            throw new IllegalArgumentException("isEmpty: " + message);
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
        return rightJustify(Integer.toString(number), width);
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
        return rightJustify(Integer.toString(number), width, fill);
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
            final int preLen = (width - text.length());

            rtn = new StringBuilder()
                    .append(fill.repeat(preLen))
                    .append(text)
                    .toString();

        } else
        {
            rtn = text;
        }

        return rtn;
    }

    /**
     * Provide C 'printf'-style formatting of text, only instead of printing it
     * to the console, the resulting text is return as a String object.
     *
     * @param format string
     * @param args   parameters to be used
     *
     * @return a new string containing the formatted text.
     *
     * @deprecated Use String.format(String, Object...) instead.
     * @see String#format(String, Object...)
     */
    @Deprecated(since = "3.0.0", forRemoval = true)
    static String sprintf(final String format, Object... args)
    {
        return new Formatter().format(format, args).toString();
    }
}
