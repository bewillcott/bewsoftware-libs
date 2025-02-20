/*
 *  File Name:    XmlDocumentImpl.java
 *  Project Name: bewsoftware-files
 *
 *  Copyright (c) 2025 Bradley Willcott
 *
 *  bewsoftware-files is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  bewsoftware-files is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bewsoftware.fileio.xml;

import com.bewsoftware.fileio.property.XmlProperty;
import com.bewsoftware.utils.string.MessageBuilder;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;
import java.util.regex.Pattern;

import static com.bewsoftware.fileio.xml.SpecialTag.get;

/**
 * This class stores the contents of an <u>xml</u> type file.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.1.0
 * @version 3.1.0
 */
public class XmlDocumentImpl implements XmlDocument
{
    /**
     * The common name for <u>comment</u> tags.
     *
     * @since 3.1.0
     */
    public static final String COMMENT_NAME = "++Comment++";

    /**
     * Will capture the tag's attributes.
     * <p>
     * Provides:<br>
     * - key: This is the attribute's name.
     * - value: This is the attribute's setting.
     * <p>
     * For example: {@snippet :
     * xmlns="http://maven.apache.org/POM/4.0.0"}
     *
     * @since 3.1.0
     */
    public static final Pattern XML_ATTRIBS;

    /**
     * Will capture single and multi-line comments.
     * <p>
     * Provides:<br>
     * - ctag: This is the comment text.
     * <p>
     * For example: {@snippet :
     * <!-- Hi there! -->}
     *
     * @since 3.1.0
     */
    public static final Pattern XML_COMMENT;

    /**
     * Will capture: {@snippet :
     * <?xml version="1.0" encoding="UTF-8"?>}
     * <p>
     * Provides:<br>
     * - xml: the full <u>xml</u> text string.<br>
     * - tag: the tag's name.<br>
     * - attribs: the tag's attributes.
     *
     * @since 3.1.0
     */
    public static final Pattern XML_SPECIAL;

    /**
     * Will capture any opening tag, and any tag that closes,
     * with no children.
     * <p>
     * Provides:<br>
     * - tag: the tag's name.<br>
     * - attribs: the tag's attributes.<br>
     * - guts: the stuff between the opening and closing tags, yet to be processed.<br>
     * - tage: This is the closing tag's name.<br>
     * - tail: The remainder of the <u>xml</u>, yet to be processed.
     * <p>
     * For example: {@snippet :
     * <project xmlns="http://maven.apache.org/POM/4.0.0"
     * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     * xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
     * >
     * }
     * or {@snippet :
     * <modelVersion>4.0.0</modelVersion>}
     *
     * @since 3.1.0
     */
    public static final Pattern XML_TAG;

    /**
     * The array of all the regex expressions.
     *
     * @since 3.1.0
     */
    private static final String[] XML_PATTERNS =
    {
        // SpecialTag
        "(?s)(?<xml><\\?(?<tag>[\\p{Alpha}][\\p{Alnum}]*)(?<attribs>.*?)\\?>)",
        // Tag
        "(?s)<(?<tag>[\\p{Alpha}][\\p{Alnum}]*)(?<attribs>.*?)?>((?<guts>.*?)</(?<tage>\\k<tag>)>)?|</(?<tage2>[\\p{Alpha}][\\p{Alnum}]*)>",
        // Attribute
        "(?<key>[\\w:]+)=\"(?<value>.*?)\"",
        // Comment
        "(?s)(?<xml><!--\\s*(?<ctext>\\S.*\\S)\\s*-->)",
        // Tail
        "(?<tail>.*)"
    };

    // (?s)(?<xml><\?(?<stag>[\p{Alpha}][\p{Alnum}]*)(?<sattribs>.*?)\?>)(?<tail>.*)
    // (?s)<(?<tag>[\p{Alpha}][\p{Alnum}]*)(?<attribs>.*?)?>((?<guts>.*?)</(?<tage>\k<tag>)>)?|</(?<tage2>[\p{Alpha}][\p{Alnum}]*)>
    // (?<key>[\w:]+)="(?<value>.*?)"
    private final AtomicInteger idCounter = new AtomicInteger(0);

    private final IntSupplier idSupplier = () -> idCounter.incrementAndGet();

    private final Tag rootTag;

    // Initialise final fields.
    static
    {
        XML_SPECIAL = Pattern.compile(XML_PATTERNS[0]);//  + XML_PATTERNS[4]);
        XML_TAG = Pattern.compile(XML_PATTERNS[1]);// + XML_PATTERNS[4]);
        XML_ATTRIBS = Pattern.compile(XML_PATTERNS[2]);
        XML_COMMENT = Pattern.compile(XML_PATTERNS[3]);//  + XML_PATTERNS[4]);
    }

    /**
     * Creates an instance of the XmlDocumentImpl class.
     *
     * @since 3.1.0
     */
    public XmlDocumentImpl()
    {
        rootTag = Tag.get(null, idSupplier, "/");
    }

    @Override
    public Tag getRootTag()
    {
        return rootTag;
    }

    @Override
    public Tag getTag(final Tag context, final String expression)
    {
        return null;
    }

    @Override
    public XmlProperty newProperty(final String key, final String value)
    {
        return new XmlProperty(idSupplier.getAsInt(), key, value);
    }

    @Override
    public SpecialTag newSpecialTag(final Tag parent, final String name, final String xml)
    {
        return get(parent, idSupplier, name, xml);
    }

    @Override
    public Tag newTag(final Tag parent, final String name)
    {
        return Tag.get(parent, idSupplier, name);
    }

    @Override
    public String toString()
    {
        final MessageBuilder mb = new MessageBuilder();

        mb.appendln("XmlDocumentImpl")
                .appendln("------------------------------------------------------------------------------")
                .appendln().appendln(rootTag)
                .appendln("------------------------------------------------------------------------------");

        return mb.toString();
    }
}
