/*
 * This file is part of the BEW Utils Library (aka: BEWUtils).
 *
 * Copyright (C) 2020 Bradley Willcott
 *
 * BEWUtils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BEWUtils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.bew.utils.regexp;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * Pre-designed and tested regular expressions collected from many sources.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public enum Patterns {

    /**
     * Date in format dd/mm/yyyy.
     * <p>
     * Will match dates with dashes, slashes or with spaces (e.g. dd-mm-yyyy
     * dd/mm/yyyy dd mm yyyy), and optional time separated by a space or a
     * dash (e.g. dd-mm-yyyy-hh:mm:ss or dd/mm/yyyy hh:mm:ss).
     * </p>
     * <p>
     * Source:
     * <a href="https://projects.lukehaas.me/regexhub/">https://projects.lukehaas.me/regexhub</a>
     * </p>
     */
    DateTimeFormat("^(0?[1-9]|[12][0-9]|3[01])([ \\/\\-])(0?[1-9]|1[012])\\2([0-9][0-9][0-9][0-9])"
                   + "(([ -])([0-1]?[0-9]|2[0-3]):[0-5]?[0-9]:[0-5]?[0-9])?$", 0),
    /**
     * Date and time in ISO-8601 format.
     * <p>
     * Will match valid dates and times in the ISO-8601 format, excludes
     * durations.
     * </p>
     * <p>
     * Source:
     * <a href="https://projects.lukehaas.me/regexhub/">https://projects.lukehaas.me/regexhub</a>
     * </p>
     */
    DateTime8601Format("^(?![+-]?\\d{4,5}-?(?:\\d{2}|W\\d{2})T)(?:|(\\d{4}|[+-]\\d{5})-?(?:|(0\\d|1[0-2])"
                       + "(?:|-?([0-2]\\d|3[0-1]))|([0-2]\\d{2}|3[0-5]\\d|36[0-6])|W([0-4]\\d|5[0-3])(?:|-?([1-7])))"
                       + "(?:(?!\\d)|T(?=\\d)))(?:|([01]\\d|2[0-4])(?:|:?([0-5]\\d)(?:|:?([0-5]\\d)(?:|\\.(\\d{3})))"
                       + "(?:|[zZ]|([+-](?:[01]\\d|2[0-4]))(?:|:?([0-5]\\d)))))$", 0),
    /**
     * Find doubled words: the the.
     */
    DoubleWordRegex("\\b(\\w+)\\s+\\1\\b", CASE_INSENSITIVE),
    /**
     * Basic email format.
     * <p>
     * An email address format that must be in the following order: <br>
     * characters@characters.domain <br>
     * (characters followed by an @ sign, followed by more characters, and
     * then a ".")
     * </p>
     * <p>
     * Source:
     * <a href="https://www.w3schools.com/TAGS/att_input_pattern.asp">https://www.w3schools.com/TAGS/att_input_pattern.asp</a>
     * </p>
     */
    EmailFormat("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", CASE_INSENSITIVE),
    /**
     * Almost Perfect Email Regex.
     * <p>
     * General Email Regex (RFC 5322 Official Standard).
     * </p>
     * <p>
     * Source:
     * <a href="https://emailregex.com/">https://emailregex.com</a>
     * </p>
     */
    EmailFormat2("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*"
                 + "|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]"
                 + "|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+"
                 + "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]"
                 + "|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?"
                 + "|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]"
                 + "|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", CASE_INSENSITIVE),
    /**
     * Hex Color Value.
     * <p>
     * RGB hex colors.
     * </p>
     * <p>
     * Source:
     * <a href="https://projects.lukehaas.me/regexhub/">https://projects.lukehaas.me/regexhub</a>
     * </p>
     */
    HexColourValue("^#?([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", 0),
    /**
     * <table>
     * <caption>
     * Named Groups
     * </caption>
     * <thead>
     * <tr>
     * <th>Group Name</th>
     * <th></th>
     * <th>Description</th>
     * <th>Empty Value</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>endTag</td>
     * <td>:</td>
     * <td>Captures the '/' if this is an end tag</td>
     * <td>null</td>
     * </tr>
     * <tr>
     * <td>tagName</td>
     * <td>:</td>
     * <td>Captures TagName</td>
     * <td >Required - never empty</td>
     * </tr>
     * <tr>
     * <td>atts</td>
     * <td>:</td>
     * <td>Group attributes</td>
     * <td>"" - Empty String</td>
     * </tr>
     * <tr>
     * <td>att</td>
     * <td>:</td>
     * <td>A single attribute</td>
     * <td>null</td>
     * </tr>
     * <tr>
     * <td>attName</td>
     * <td>:</td>
     * <td>Attribute name</td>
     * <td>null</td>
     * </tr>
     * <tr>
     * <td>attVal1</td>
     * <td>:</td>
     * <td>"Double quoted" value string *</td>
     * <td>null</td>
     * </tr>
     * <tr>
     * <td>attVal2</td>
     * <td>:</td>
     * <td>'Single quoted' value string *</td>
     * <td>null</td>
     * </tr>
     * <tr>
     * <td>completeTag</td>
     * <td>:</td>
     * <td>Captures the '/' if this is a complete tag</td>
     * <td>null</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <strong>Note: *</strong> Normally only <em>attVal1</em> will have content.
     * </p>
     */
    HtmlTagRegex("<"
                 // Captures the '/' if this is an end tag
                 + "(?<endTag>/)?"
                 // Captures TagName
                 + "(?<tagName>\\w+)"
                 // Group attributes
                 + "(?<atts>"
                 // A single attribute
                 + "(?<att>\\s+"
                 // Attribute name
                 + "(?<attName>\\w+)"
                 // Groups attribute '="value"' portion
                 + "(\\s*=\\s*"
                 // attVal1='double quoted'
                 + "(?:\\x22(?<attVal1>[^\\x22]*)\\x22"
                 // attVal2='single quoted'
                 + "|'(?<attVal2>[^']*)')?"
                 // End optional attribute '="value"' portion
                 + ")?"
                 // End attributes grouping
                 + ")+\\s*"
                 // Boolean attribute - No '="value"' portion
                 + "|\\s*)?"
                 // Captures the '/' if this is a complete tag
                 + "(?<completeTag>/)?>",
                 CASE_INSENSITIVE),
    /**
     * HTML tags.
     * <p>
     * Match opening and closing HTML tags with content between.
     * </p>
     * <p>
     * Source:
     * <a href="https://projects.lukehaas.me/regexhub/">https://projects.lukehaas.me/regexhub</a>
     * </p>
     */
    HtmlTags("<(?<tagName>[a-z1-6]+)(?<atts>([^<]+)*)?(?:>(?<text>[^<]*)?<\\/\\1>| *\\/>)", CASE_INSENSITIVE),
    /**
     *
     */
    HtmlTagsAtt("(?<attName>\\w+)" //Attribute name
                + "(" //groups = value portion.
                + "\\s*=\\s*" // =
                + "(?:['\\x22])?"
                + "(" //Groups attribute "value" portion.
                + "(?<attVal>[^'\\x22]*" // attVal='quoted'
                + "|[^'\\x22>\\s]+)" // attVal=urlnospaces
                + ")" //end Groups attribute "value" portion
                //                    + "(?:\\k<quote>)?"
                + ")?", //end optional att value portion.
                0),
    /**
     * SRC of image tag.
     * <p>
     * Match the src attribute of an HTML image tag.
     * </p>
     * <p>
     * Source:
     * <a href="https://projects.lukehaas.me/regexhub/">https://projects.lukehaas.me/regexhub</a>
     * </p>
     */
    ImageTagSRC("^<\\s*img[^>]+src\\s*=\\s*([\"'])(.*?)\\1[^>]*>$", CASE_INSENSITIVE),
    /**
     * IPv4 Address.
     * <p>
     * Match IP v4 addresses.
     * </p>
     * <p>
     * Source:
     * <a href="https://projects.lukehaas.me/regexhub/">https://projects.lukehaas.me/regexhub</a>
     * </p>
     */
    IPv4Address("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", 0),
    /**
     * IPv6 Address.
     * <p>
     * Match IP v6 addresses.
     * </p>
     * <p>
     * Source:
     * <a href="https://projects.lukehaas.me/regexhub/">https://projects.lukehaas.me/regexhub</a>
     * </p>
     */
    IPv6Address("^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:"
                + "|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}"
                + "|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}"
                + "|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})"
                + "|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}"
                + "|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]"
                + "|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]"
                + "|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$", 0),
    /**
     * Standard minimal password.
     * <p>
     * A password that must contain 8 or more characters that are of at
     * least one number, and one uppercase and lowercase letter.
     * </p>
     * <p>
     * Source:
     * <a href="https://www.w3schools.com/TAGS/att_input_pattern.asp">https://www.w3schools.com/TAGS/att_input_pattern.asp</a>
     * </p>
     */
    Password("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}", 0),
    /**
     * Time in 24-hour format.
     * <p>
     * Match times in 24 hour format.
     * </p>
     * <p>
     * Source:
     * <a href="https://projects.lukehaas.me/regexhub/">https://projects.lukehaas.me/regexhub</a>
     * </p>
     */
    Time24HourFormat("^([01]?[0-9]|2[0-3]):[0-5][0-9]$", 0),
    /**
     * Matching a URL.
     * <p>
     * This regex is almost like taking the ending part of the above regex,
     * slapping it between "http://" and some file structure at the end. It
     * sounds a lot simpler than it really is. To start off, we search for
     * the beginning of the line with the caret.
     * </p><p>
     * The first capturing group is all option. It allows the URL to begin
     * with "http://", "https://", or neither of them. I have a question
     * mark after the s to allow URL's that have http or https. In order to
     * make this entire group optional, I just added a question mark to the
     * end of it.
     * </p><p>
     * Next is the domain name: one or more numbers, letters, dots, or
     * hypens followed by another dot then two to six letters or dots. The
     * following section is the optional files and directories. Inside the
     * group, we want to match any number of forward slashes, letters,
     * numbers, underscores, spaces, dots, or hyphens. Then we say that this
     * group can be matched as many times as we want. Pretty much this
     * allows multiple directories to be matched along with a file at the
     * end. I have used the star instead of the question mark because the
     * star says zero or more, not zero or one. If a question mark was to be
     * used there, only one file/directory would be able to be matched.
     * </p><p>
     * Then a trailing slash is matched, but it can be optional. Finally we
     * end with the end of the line.
     * </p><p>
     * Source:
     * <a href="https://code.tutsplus.com/tutorials/8-regular-expressions-you-should-know--net-6149">
     * https://code.tutsplus.com/tutorials/8-regular-expressions-you-should-know</a><p>
     * <b>Note:</b> The regex pattern has been modified as follows:
     * <br>
     * <ul>
     * <li>
     * Added more protocols:
     * <ul>
     * <li>ftp</li>
     * <li>file</li></ul></li>
     * <li>
     * Added Named Groups:
     * ("{@code http://www.sample.com/this/directory/path/file.html}")
     * <ul>
     * <li>protocol: "{@code http}"</li>
     * <li>website : "{@code www.sample.com}"</li>
     * <li>filepath : "{@code /this/directory/path/file.html}"</li></ul></li>
     * </ul>
     */
    URLFormat("^((?<protocol>https?|ftp|file):\\/\\/)?"
              + "(?<website>([\\da-z\\.-]+)\\.([a-z\\.]{2,6}))"
              + "(<filepath>([\\/\\w \\.-]*)*\\/?)$", CASE_INSENSITIVE);

    /**
     * Refer to the {@link java.util.regex.Pattern} class for the values
     * stored here.
     */
    public final int flags;

    /**
     * The text of the regex.
     */
    public final String regex;

    Patterns(String text, int options) {
        this.regex = text;
        this.flags = options;
    }

    /**
     * Simple way of getting a compiled {@link Pattern} object.
     * <p>
     * Any flags stored with the regex, in {@link #flags} will be used in
     * the compilation process.
     * </p>
     *
     * @return new Pattern object with compiled regex.
     */
    public Pattern getPattern() {
        return Pattern.compile(regex, flags);
    }

    /**
     * Simple way of getting a compiled {@link Pattern} object.
     * <p>
     * Any flags stored with the regex in {@link #flags}, will be added to
     * any value you supply through the {@code flags} parameter, and used in
     * the compilation process.
     * </p>
     *
     * @param flags Additional flags.
     *
     * @return new Pattern object with compiled regex.
     */
    public Pattern getPattern(int flags) {
        return Pattern.compile(regex, this.flags | flags);
    }

    @Override
    public String toString() {
        return regex + " (" + flags + ")";
    }
}
