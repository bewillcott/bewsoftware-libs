/*
 * This file is part of the BEWSoftware Utils Library.
 *
 * Copyright (C) 2020, 2021 Bradley Willcott
 *
 * BEWSoftware Utils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BEWSoftware Utils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.bewsoftware.utils.string;

import java.util.ArrayList;
import java.util.List;

/**
 * Diff class provides static methods to assist finding the differences between
 * two Strings.
 * <p>
 * The strings may be lines of text from a file, or simple text strings.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.4
 * @version 1.0.4
 */
public class Diff {

    /**
     * Not meant to be instantiated.
     */
    private Diff() {
    }

    /**
     * Provide a {@link List} of lines that are different, between the two
     * supplied texts.
     *
     * @param orig The original text.
     * @param mod  The modified text.
     *
     * @return list of modified lines.
     */
    public static List<ModifiedLine> lines(final String orig, final String mod) {
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
     */
    public static int strings(final String orig, final String mod) {
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
     * This is an immutable class that holds the line text and attributes
     * recorded for each pair of lines that are different.
     */
    public static class ModifiedLine {

        /**
         * Provide a String filled with spaces.
         *
         * @param count The number of spaces required.
         *
         * @return space filled String.
         */
        private static String fill(int count) {
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
        private ModifiedLine(String orig, String mod, int linenum, int position) {
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
        public String toString() {
            String strLinenum = " [" + linenum + "] ";
            int slLength = strLinenum.length();

            return "-" + strLinenum + orig + "\n"
                   + (position > -1 ? fill(slLength + position) + "^" : "") + "\n"
                   + "+" + fill(slLength) + mod;
        }
    }
}
