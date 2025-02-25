/*
 *  File Name:    XmlFile.java
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

import com.bewsoftware.fileio.ini.FileAlreadyLoadedException;
import com.bewsoftware.fileio.property.XmlProperty;
import com.bewsoftware.utils.string.MessageBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static com.bewsoftware.fileio.xml.XmlDocumentImpl.*;
import static com.bewsoftware.utils.Clazz.getCallingClass;
import static com.bewsoftware.utils.string.Strings.notBlank;
import static com.bewsoftware.utils.string.Strings.notEmpty;
import static com.bewsoftware.utils.string.Strings.println;
import static com.bewsoftware.utils.string.Strings.requireNonBlank;
import static java.lang.String.format;
import static java.nio.file.Path.of;

/**
 * This class provides access to the <i>tags</i> within an <b>xml</b> file.
 * <p>
 * It is assumed that the file is already properly formed, formatted and validated
 * for any <i>dtd</i> or <i>xsd</i> that is associated with it. This class
 * expects a single <i>tag</i> per line. Both the opening and closing <i>tags</i>
 * may be on the same line though. No other validation checking is done beyond this.
 * <p>
 * To use this xml package in your project (processing "books.xml", for example),
 * the process would be similar to the
 * following:
 * <ol>
 * <li>Get a {@link Path} to the <u>xml</u> file:<br>
 * {@snippet :
 * final Path xmlFilePath = Path.of("books.xml");}</li>
 * <li>Create a new {@code XmlFile} instance:<br>
 * {@snippet :
 * final XmlFile xmlFile = new XmlFile(xmlFilePath);}</Li>
 * <li>Load the <u>xml</u> file and obtain the root tag:<br>
 * {@snippet :
 * final Tag root = xmlFile.loadFile().getXmlDocument().getRootTag();}</Li>
 * <li>To process all of the books in this example:
 * <ol>
 * <li>Get the 'group' tag (a Tag that has multiple child Tags):<br>
 * {@snippet :
 * Tag group = Tags.getTag(root, "/catalog/books/book");}</Li>
 * <li>Then process the 'books':<br>
 * {@snippet :
 * while (group != null)
 * {
 *     final Tag tag = Tags.getTag(group, "title");
 *     System.out.println(String.format("%s: '%s'", tag.getName(), tag.getText()));
 *     // do something with it...
 *
 *     group = Tags.nextTag(group);
 * }
 * }</li>
 * </ol>
 * </Li>
 * <li>To </Li>
 * <li></Li>
 * <li></Li>
 * <li></Li>
 * <li></Li>
 * <li></Li>
 * </ol>
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.1.0
 * @version 3.1.0
 */
public class XmlFile
{
    private boolean fileIsLoaded;

    private final String filename;

    private final Path location;

    private final XmlDocument xmlDoc;

    /**
     * Instantiates a new XmlFile.
     *
     * @param filename of the <u>xml</u> to load.
     *
     * @throws NullPointerException If the path parameter is
     *                              {@code null}.
     *
     * @since 3.1.0
     */
    public XmlFile(final String filename)
    {
        this.location = null;
        this.filename = requireNonBlank(filename);
        xmlDoc = new XmlDocumentImpl();
    }

    /**
     * Instantiates a new XmlFile.
     *
     * @param location Either the directory or the Jar file,
     *                 where the xml file is located.
     * @param filename of the <u>xml</u> to load.
     *
     * @throws NullPointerException If the path parameter is
     *                              {@code null}.
     *
     * @since 3.1.0
     */
    public XmlFile(final Path location, final String filename)
    {
        this.location = location == null ? of("") : location;
        this.filename = requireNonBlank(filename);
        xmlDoc = new XmlDocumentImpl();
    }

    private static void readXmlFile(final BufferedReader in, final List<String> xmlLines) throws IOException

    {
        MessageBuilder mb = new MessageBuilder();
        boolean tagOpen = false;
        String line;

        while ((line = in.readLine()) != null)
        {
            final String tLine = line.trim();

            if (tagOpen)// < ...
            {
                if (notBlank(tLine))
                {
                    mb.appendln(line);

                    if (tLine.endsWith(">")) // ... >
                    {
                        tagOpen = false;
                        xmlLines.add(mb.toString());
                        mb.clear();
                    }
                }
            } else if (notBlank(tLine))
            {
                if (tLine.startsWith("<"))
                {
                    if (tLine.endsWith(">"))
                    {
                        xmlLines.add(tLine);
                    } else
                    {
                        mb.appendln(tLine);
                        tagOpen = true;
                    }
                } else
                {
                    xmlLines.add(tLine);
                }
            }
        }
    }

    /**
     * Returns the filename of the xml file.
     *
     * @return the filename of the xml file.
     *
     * @since 3.1.0
     */
    public String getFilename()
    {
        return filename;
    }

    /**
     * Returns the location of the xml file.
     *
     * @return the location of the xml file.
     *
     * @since 3.1.0
     */
    public Path getLocation()
    {
        return location;
    }

    /**
     * Returns the internal xml document.
     *
     * @return the internal xml document.
     *
     * @since 3.1.0
     */
    public XmlDocument getXmlDocument()
    {
        return xmlDoc;
    }

    /**
     * Returns <i>true</i> if the file has been loaded, <i>false</i> otherwise.
     *
     * @return <i>true</i> if the file has been loaded, <i>false</i> otherwise.
     *
     * @since 3.1.0
     */
    public boolean isLoaded()
    {
        return fileIsLoaded;
    }

    /**
     * Opens and parses the file, based on the <b>Path</b> set-up by the class
     * constructor.
     *
     * @return this instance for chaining.
     *
     * @throws java.lang.ClassNotFoundException Should never happen.
     * @throws FileAlreadyLoadedException       The file cannot be reloaded.
     * @throws IOException                      An I/O error occurs opening the file.
     * @throws XmlFileFormatException           The format of the <u>xml</u> file is
     *                                          non-conforming.
     *
     * @since 3.1.0
     */
    public XmlFile loadFile()
            throws ClassNotFoundException, FileAlreadyLoadedException, IOException, XmlFileFormatException
    {

        if (fileIsLoaded)
        {
            throw new FileAlreadyLoadedException(filename);
        }

        final List<String> xmlLines = new ArrayList<>();

        if (location != null)
        {
            if (location.toFile().isFile())
            {
                try (BufferedReader in
                        = new BufferedReader(
                                new InputStreamReader(
                                        getCallingClass().getClassLoader()
                                                .getResourceAsStream(filename))))
                {
                    readXmlFile(in, xmlLines);
                }
            } else
            {
                println("location: %s", location.toFile().getAbsolutePath());
                println("filename: %s", filename);

                try (BufferedReader in = Files.newBufferedReader(of(location.toFile().getAbsolutePath(), filename)))
                {
                    readXmlFile(in, xmlLines);
                }
            }
        } else
        {
            println("filename: %s", filename);

            try (BufferedReader in = Files.newBufferedReader(of( filename)))
            {
                readXmlFile(in, xmlLines);
            }
        }

        fileIsLoaded = parseXml(xmlLines);

        return this;
    }

    @Override
    public String toString()
    {
        final MessageBuilder mb = new MessageBuilder();

        final String title = "XmlFile (%s)".formatted(of(location.toString(), filename));

        mb.append(title)
                .appendln("=".repeat(title.length()))
                .appendln(xmlDoc);

        return mb.toString();
    }

    /**
     * Parses the <i>xml</i> text.
     *
     * @param xmlLines A list of <u>xml</u> text lines.
     *
     * @throws XmlFileFormatException if error in xml format.
     *
     * @since 3.1.0
     */
    private boolean parseXml(final List<String> xmlLines) throws XmlFileFormatException
    {
        final Tag root = xmlDoc.getRootTag();
        String tail = processFirstLine(root, xmlLines.getFirst());

        if ("".equals(tail))
        {
            xmlLines.removeFirst();
        }

        while (!xmlLines.isEmpty())
        {
            processTag(root, xmlLines);
        }

        return true;
    }

    /**
     * Process the attributes text.
     *
     * @param tag     to add attributes to.
     * @param attribs text to process.
     *
     * @since 3.1.0
     */
    private void processAttribs(final Tag tag, final String attribs)
    {
        final Matcher matcher = XML_ATTRIBS.matcher(attribs);

        while (matcher.find())
        {
            final String leadString = matcher.group("lead");
            final String keyString = matcher.group("key");
            final String valueString = matcher.group("value");
            final String eolString = matcher.group("eol");

            final XmlProperty prop
                    = xmlDoc.newProperty(
                            leadString.length(),
                            keyString,
                            valueString,
                            notEmpty(eolString)
                    );

            tag.addAttribute(prop);
        }
    }

    /**
     * Process the first line of the file.
     *
     * @param parent Should be rootTag.
     * @param xml    text to process.
     *
     * @return tail remaining after processing.
     *
     * @since 3.1.0
     */
    private String processFirstLine(final Tag parent, final String xml)
    {
        String rtn = xml;
        Matcher matcher = XML_SPECIAL.matcher(xml);

        if (matcher.find())
        {
            final String xmlString = matcher.group("xml");
            final String tagString = matcher.group("tag");
            final String attribsString = matcher.group("attribs");

            if (notEmpty(xmlString) && notEmpty(tagString))
            {
                final SpecialTag tag = xmlDoc.newSpecialTag(parent, tagString, xmlString);
                processAttribs(tag, attribsString);
            }

            rtn = "";
        }

        return rtn;
    }

    /**
     * Process the supplied <u>xml</u> text, producing a tag element.
     *
     * @param parent   tag this new tag will be linked to.
     * @param xmlLines A list of <u>xml</u> text lines.
     *
     * @since 3.1.0
     */
    private void processTag(final Tag parent, final List<String> xmlLines) throws XmlFileFormatException
    {
        boolean tagDone;

        do
        {
            final String xml = xmlLines.removeFirst();
            tagDone = processTag(parent, xmlLines, xml);
        } while (!tagDone && !xmlLines.isEmpty());
    }

    private boolean processTag(final Tag parent, final List<String> xmlLines, final String xml) throws XmlFileFormatException
    {
        boolean tagDone = false;
        boolean matched = false;

        // Check for comment line.
        {
            Matcher matcher = XML_COMMENT.matcher(xml);

            if (matcher.find())
            {
                final String xmlString = matcher.group("xml");
                final String ctextString = matcher.group("ctext");

                final SpecialTag comment = xmlDoc.newSpecialTag(parent, COMMENT_NAME, xmlString);
                comment.setText(ctextString.trim());
                matched = true;
            }
        }

        // The main processing part.
        if (!matched)
        {
            Matcher matcher = XML_TAG.matcher(xml);

            if (matcher.find())
            {
                final String tagString = matcher.group("tag");
                final String attribsString = matcher.group("attribs");
                final String guts = matcher.group("guts");
                final String stagString = matcher.group("stag");
                final String tageString = matcher.group("tage");
                final String tage2String = matcher.group("tage2");

                if (notEmpty(tagString))
                {
                    final Tag tag = xmlDoc.newTag(parent, tagString);

                    if (notEmpty(attribsString))
                    {
                        processAttribs(tag, attribsString);
                    }

                    if (!notEmpty(stagString))
                    {
                        if (notEmpty(guts))
                        {
                            processTag(tag, null, guts);
                        }

                        if (tageString == null)
                        {
                            processTag(tag, xmlLines);
                        }
                    } else
                    {
                        tag.setShortTag(true);
                    }

                } else if (notEmpty(tage2String))
                {
                    if (parent.getName().equals(tage2String))
                    {
                        tagDone = true;
                    } else
                    {
                        throw new XmlFileFormatException(filename,
                                format("Expected: '%s', got: '%s'", parent.getName(), tage2String));
                    }
                }
            } else
            {
                parent.setText(xml);
                parent.setTextIsOnSeparateLine(xmlLines != null);
            }
        }

        return tagDone;
    }

}
