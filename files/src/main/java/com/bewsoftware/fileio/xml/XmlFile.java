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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static com.bewsoftware.fileio.xml.XmlDocumentImpl.*;
import static com.bewsoftware.utils.string.Strings.notBlank;
import static com.bewsoftware.utils.string.Strings.notEmpty;
import static java.lang.String.format;

/**
 * This class provides access to the <i>tags</i> within an <b>xml</b> file.
 * <p>
 * It is assumed that the file is already properly formed, formatted and validated
 * for any <i>dtd</i> or <i>xsd</i> that is associated with it. This class
 * expects a single <i>tag</i> per line. Both the opening and closing <i>tags</i>
 * may be on the same line though. No other validation checking is done beyond this.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.1.0
 * @version 3.1.0
 */
public class XmlFile
{
    private boolean fileIsLoaded;

    private final XmlDocument xmlDoc;

    private final Path xmlFilePath;

    /**
     * This constructor uses the <b>Path</b> object passed in the <i>path</i>
     * parameter.
     *
     * @param xmlFilePath The <b>Path</b> object to use.
     *
     * @throws NullPointerException If the path parameter is
     *                              {@code null}.
     *
     * @since 3.1.0
     */
    public XmlFile(final Path xmlFilePath)
    {
        if (xmlFilePath == null)
        {
            throw new NullPointerException("path is null");
        } else
        {
            this.xmlFilePath = xmlFilePath;
            xmlDoc = new XmlDocumentImpl();
        }
    }

    /**
     * Returns path to source <u>xml</u> file.
     *
     * @return path to source <u>xml</u> file.
     *
     * @since 3.1.0
     */
    public Path getFilePath()
    {
        return xmlFilePath;
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
     * @throws FileAlreadyLoadedException The file cannot be reloaded.
     * @throws XmlFileFormatException     The format of the <u>xml</u> file is
     *                                    non-conforming.
     * @throws IOException                An I/O error occurs opening the file.
     *
     * @since 3.1.0
     */
    public XmlFile loadFile()
            throws FileAlreadyLoadedException, XmlFileFormatException,
            IOException
    {
        MessageBuilder mb = new MessageBuilder();

        if (fileIsLoaded)
        {
            throw new FileAlreadyLoadedException(xmlFilePath.toString());
        }

        final List<String> xmlLines = new ArrayList<>();
        boolean tagOpen = false;

        try (BufferedReader in = Files.newBufferedReader(xmlFilePath))
        {
            String line;

            while ((line = in.readLine()) != null)
            {
                final String sLine = line.trim();

                if (tagOpen)// < ...
                {
                    if (notBlank(sLine))
                    {
                        mb.appendln(sLine);

                        if (sLine.endsWith(">")) // ... >
                        {
                            tagOpen = false;
                            final String tLine = mb.toString().trim();
                            xmlLines.add(tLine);
                            mb.clear();
                        }
                    }
                } else if (notBlank(sLine))
                {
                    if (sLine.startsWith("<"))
                    {
                        if (sLine.endsWith(">"))
                        {
                            xmlLines.add(sLine);
                        } else
                        {
                            mb.appendln(sLine);
                            tagOpen = true;
                        }
                    }
                }
            }
        }

        fileIsLoaded = parseXml(xmlLines);

        return this;
    }

    @Override
    public String toString()
    {
        final MessageBuilder mb = new MessageBuilder();

        mb.append("XmlFile (").append(xmlFilePath).appendln(')')
                .appendln("========================================================")
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
            final String keyString = matcher.group("key");
            final String valueString = matcher.group("value");
            final XmlProperty prop = xmlDoc.newProperty(keyString, valueString);
            tag.getAttributes().add(prop);
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
     * @return any <i>tail</i> left-over.
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
                final String tageString = matcher.group("tage");
                final String tage2String = matcher.group("tage2");

                if (notEmpty(tagString))
                {
                    final Tag tag = xmlDoc.newTag(parent, tagString);

                    if (notEmpty(attribsString))
                    {
                        processAttribs(tag, attribsString);
                    }

                    if (notEmpty(guts))
                    {
                        processTag(tag, null, guts);
                    }

                    if (tageString == null)
                    {
                        processTag(tag, xmlLines);
                    }
                } else if (notEmpty(tage2String))
                {
                    if (parent.getName().equals(tage2String))
                    {
                        tagDone = true;
                    } else
                    {
                        throw new XmlFileFormatException(xmlFilePath.toString(),
                                format("Expected: '%s', got: '%s", parent.getName(), tage2String));
                    }
                }
            } else
            {
                parent.setText(xml);
            }
        }

        return tagDone;
    }
}
