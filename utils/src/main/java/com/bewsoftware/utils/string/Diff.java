/*
 *  File Name:    Diff.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2020, 2021 Bradley Willcott
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

import com.bewsoftware.utils.Ref;
import java.util.ArrayList;
import java.util.List;

import static com.bewsoftware.utils.string.Strings.indentLines;
import static com.bewsoftware.utils.string.Strings.println;
import static java.lang.String.format;

/**
 * Diff class provides static methods to assist finding the differences between
 * two Strings.
 * <p>
 * The strings may be lines of text from a file, or simple text strings.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.4
 * @version 3.0.2
 */
public interface Diff
{

    /**
     * Provide a {@link List} of lines that are different, between the two
     * supplied texts.
     *
     * @param orig The original text.
     * @param mod  The modified text.
     *
     * @return list of modified lines.
     *
     * @since 1.0.4
     */
    public static List<ModifiedLine> lines(final String orig, final String mod)
    {
        List<ModifiedLine> mlines = new ArrayList<>();
        String[] origLines = orig.split("\n");
        String[] modLines = mod.split("\n");

        int i = 0;
        for (; i < origLines.length; i++)
        {
            if (i < modLines.length)
            {
                int index = strings(origLines[i], modLines[i]);

                if (index != 0)
                {
                    mlines.add(new ModifiedLine(origLines[i], modLines[i], i + 1, index));
                }
            } else
            {
                mlines.add(new ModifiedLine(origLines[i], "", i + 1, -1));
            }
        }

        for (++i; i < modLines.length; i++)
        {
            mlines.add(new ModifiedLine("", modLines[i], i + 1, -1));
        }

        return mlines;
    }

    /**
     * Prints the results of the {@link #strings(String, String, Ref, Ref) strings} operation.
     *
     * @note
     * This method is primarily of use in Unit Testing.
     *
     * @implNote
     * This method uses each parameter's {@code toString()} method to obtain the required
     * string to compare.
     *
     * @param obj1 to be compared to.
     * @param obj2 to be compared by.
     *
     * @since 3.0.2
     */
    public static void printDiff(final Object obj1, final Object obj2)
    {
        printDiff(obj1.toString(), obj2.toString());
    }

    /**
     * Prints the results of the {@link #strings(String, String, Ref, Ref) strings} operation.
     * If the two strings have no differences, then nothing is printed.
     *
     * @note
     * This method is primarily of use in Unit Testing.
     *
     * @param text1 to be compared to.
     * @param text2 to be compared by.
     *
     * @since 3.0.2
     */
    public static void printDiff(final String text1, final String text2)
    {
        final Ref<Integer> index = Ref.val();
        final Ref<String> diffRef = Ref.val();

        if (strings(text1, text2, index, diffRef))
        {
            println("\nprintDiff ============================================");

            index.ifPresent((final Integer idx)
                    -> println(indentLines("index: %d".formatted(idx), 4)));

            diffRef.ifPresent((final String text)
                    -> println(indentLines("diff:%n%s".formatted(text), 4)));

            println("======================================================\n");
        }
    }

    /**
     * Compare two Strings.
     *
     * @param orig The original line/string.
     * @param mod  The modified line/string.
     *
     * @return <ul>
     * <li>'0' - if Strings are equal,</li>
     * <li>'-1' - if not and {@code orig} is empty, and</li>
     * <li>'&gt;0' - is the index into {@code orig} that the first difference
     * was found.</li>
     * </ul>
     *
     * @since 1.0.4
     */
    public static int strings(final String orig, final String mod)
    {
        char[] origChars = orig.toCharArray();
        char[] modChars = mod.toCharArray();
        int rtn = origChars.length == modChars.length ? 0 : -1;

        for (int i = 0; i < origChars.length; i++)
        {
            if (i < modChars.length)
            {
                if (origChars[i] != modChars[i])
                {
                    rtn = i;
                }
            } else
            {
                rtn = i;
            }

            if (rtn > 0)
            {
                break;
            }
        }

        return rtn;
    }

    /**
     * Compares two text strings, character by character.It returns the zero based
     * position of the first character that is different between the two strings.
     *
     * @note
     * This method is primarily of use in Unit Testing.
     *
     * @implNote
     * This implementation is case-sensitive.
     *
     * @param text1   to be compared to.
     * @param text2   to be compared by.
     * @param index   On return from this method call, this parameter will contain, either:<br>
     * - <i>-1</i>, if the strings are of different lengths, or<br>
     * - <i>the zero based position</i> of the first differing characters, or<br>
     * - {@link Ref#isEmpty() nothing}, if there are no differences
     * between the strings.
     * @param diffRef On return from this method call, this parameter will contain, either:<br>
     * - <i>the text</i> up to the differing characters, with the differing characters
     * shown in brackets: (<i>text1</i>)(<i>text2</i>), or<br>
     * - {@link Ref#isEmpty() nothing}, if there are no differences
     * between the strings.
     *
     * @return {@code true} if the strings are different, {@code false} if not.
     *
     * @since 3.0.2
     */
    public static boolean strings(
            final String text1,
            final String text2,
            final Ref<Integer> index,
            final Ref<String> diffRef
    )
    {
        boolean diff = false;

        index.clear();
        diffRef.clear();

        // Check for various causes of different lengths.
        // First: nullity.
        if (text1 == null)
        {
            if (text2 == null)
            {
                return false;
            } else
            {
                index.val = -1;
                return true;
            }
        } else if (text2 == null)
        {
            index.val = -1;
            return true;
        }

        // Second: emptiness.
        if (text1.isEmpty())
        {
            if (text2.isEmpty())
            {
                return false;
            } else
            {
                index.val = -1;
                return true;
            }
        } else if (text2.isEmpty())
        {
            index.val = -1;
            return true;
        }

        // Third: lengths.
        if (text1.length() != text2.length())
        {
            index.val = -1;
            return true;
        }

        // Now we have two strings to process.
        for (int i = 0; i < text1.length(); i++)
        {
            if (text1.charAt(i) != text2.charAt(i))
            {
                index.val = i;
                diff = true;
                break;
            }
        }

        if (diff)
        {
            diffRef.val = format(
                    "%s (%c)(%c)",
                    text1.substring(0, index.val - 1),
                    text1.charAt(index.val),
                    text2.charAt(index.val)
            );
        }

        return diff;
    }

    /**
     * This is an immutable class that holds the line text and attributes
     * recorded for each pair of lines that are different.
     *
     * @since 1.0.4
     */
    @SuppressWarnings("PublicInnerClass")
    public static final class ModifiedLine
    {

        /**
         * Provide a String filled with spaces.
         *
         * @param count The number of spaces required.
         *
         * @return space filled String.
         */
        private static String fill(int count)
        {
            return " ".repeat(count);
        }

        /**
         * The line number within the original text file.
         */
        public final int linenum;

        /**
         * The modified line text.
         */
        public final String mod;

        /**
         * The original unmodified line text.
         */
        public final String orig;

        /**
         * The first character position within the original text that is
         * different from the modified version of the text.
         */
        public final int position;

        /**
         * Only to be instantiated within the {@link Diff} class.
         *
         * @param orig     The original unmodified line text.
         * @param mod      The modified line text.
         * @param linenum  The line number with the original text file.
         * @param position The first character position within the original text that is
         *                 different from the modified version of the text.
         */
        private ModifiedLine(String orig, String mod, int linenum, int position)
        {
            this.orig = orig;
            this.mod = mod;
            this.linenum = linenum;
            this.position = position;
        }

        /**
         * Provide a formatted String that lays out both strings with a
         * carat "^" positioned at the first location of difference
         * between the strings.
         *
         * @return Formatted text.
         */
        @Override
        public String toString()
        {
            String strLinenum = " [" + linenum + "] ";
            int slLength = strLinenum.length();

            return "-" + strLinenum + orig + "\n"
                    + (position > -1 ? fill(slLength + position) + "^" : "") + "\n"
                    + "+" + fill(slLength) + mod;
        }
    }
}
