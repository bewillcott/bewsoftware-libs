/*
 *  File Name:    XmlFileTest.java
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

import com.bewsoftware.utils.Ternary;
import com.bewsoftware.utils.function.TriFunction;
import com.bewsoftware.utils.string.MessageBuilder;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Function;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static com.bewsoftware.utils.string.Strings.print;
import static com.bewsoftware.utils.string.Strings.println;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 */
public class XmlFileTest implements XmlFileTestStrings
{
    public XmlFileTest()
    {
    }

    @BeforeAll
    public static void setUpClass()
    {
    }

    @AfterAll
    public static void tearDownClass()
    {
    }

    @BeforeEach
    public void setUp()
    {
    }

    @AfterEach
    public void tearDown()
    {
    }

    /**
     * Test of loadFile method, of class XmlFile.
     *
     * @param filename  name of the <u>xml</u> file to load.
     * @param expResult The expected results.
     *
     * @throws XmlFileFormatException
     * @throws java.io.IOException
     *
     * @since 3.1.0
     */
    @ParameterizedTest
    @MethodSource
    public void testLoadFile(final String filename, final Function<Path, String> expResult) throws XmlFileFormatException, IOException
    {
        print("testLoadFile: ");

        final Path xmlFilePath = Path.of(getClass().getResource(filename).getPath());

        println(xmlFilePath.getFileName());

        final XmlFile xmlFile = new XmlFile(xmlFilePath);
        xmlFile.loadFile();
        assertTrue(xmlFile.isLoaded());

        final String er = expResult.apply(xmlFilePath);
        final String result = xmlFile.getXmlDocument().getRootTag().toString();

        assertEquals(er, result);

        println("testLoadFile: completed");
    }

    /**
     * Test XPath access to Tags.
     *
     * @param filename  name of the <u>xml</u> file to load.
     * @param xpath     to search for.
     * @param valueTag  The name of the Tag whose {@code text}
     *                  is to be retrieved.
     * @param expResult The expected results.
     *
     * @throws XmlFileFormatException
     * @throws IOException
     *
     * @since 3.1.0
     */
    @ParameterizedTest
    @MethodSource
    public void testXPath(
            final String filename,
            final String xpath,
            final String valueTag,
            final String expResult
    ) throws XmlFileFormatException, IOException
    {
        print("testXPath: ");

        final MessageBuilder mb = new MessageBuilder();
        final Path xmlFilePath = Path.of(getClass().getResource(filename).getPath());

        println(xmlFilePath.getFileName());

        final XmlFile xmlFile = new XmlFile(xmlFilePath);
        final Tag root = xmlFile.loadFile().getXmlDocument().getRootTag();
        mb.append("[")
                .append(xmlFile.getFilePath().getFileName())
                .appendln("]");

        Tag group = Tags.getTag(root, xpath);

        while (group != null)
        {
            mb.appendln(group.getName());
            final Tag tag = Tags.getTag(group, valueTag);
            mb.appendln("    %s: '%s'", tag.getName(), tag.getText());
            group = Tags.nextTag(group);
        }

        assertEquals(expResult, mb.toString());
    }

    /**
     * Test XPath to find a specific group tag and get
     * the text of a child tag.
     *
     * @param filename    The name of the <u>xml</u> file to load.
     * @param xpath       to search for.
     * @param comparator  Used to decide whether or not the supplied Tag
     *                    is the one you are looking for.<br>
     * <ul>
     * <li>Tag: ({@code group}) The tag found using {@code xpath}.</li>
     * <li>String: The {@code compareTag}.</li>
     * <li>String: The {@code compareText}.</li>
     * </ul>
     * @param compareTag  The name of the child tag to do the test on.
     * @param compareText The text value you wish compare.
     * @param valueTag    The name of the {@code Tag} whose {@code text}
     *                    is to be retrieved.
     * @param expResult   The expected results.
     *
     * @see Tag
     * @see Tags
     *
     * @throws XmlFileFormatException
     * @throws IOException
     *
     * @since 3.1.0
     */
    @ParameterizedTest
    @MethodSource
    public void testXPathFind(
            final String filename,
            final String xpath,
            final TriFunction<Tag, String, String, Ternary> comparator,
            final String compareTag,
            final String compareText,
            final String valueTag,
            final String expResult
    ) throws XmlFileFormatException, IOException
    {
        print("testXPathFind: ");

        final MessageBuilder mb = new MessageBuilder();
        final Path xmlFilePath = Path.of(getClass().getResource(filename).getPath());

        println(xmlFilePath.getFileName());

        final XmlFile xmlFile = new XmlFile(xmlFilePath);
        final Tag root = xmlFile.loadFile().getXmlDocument().getRootTag();

        Tag group = Tags.getTag(root, xpath);
        String result = "";
        int seqNum = 0;

        search:
        while (group != null)
        {
            seqNum++;

            switch (comparator.apply(group, compareTag, compareText))
            {
                case True ->
                {
                    final Tag tag = Tags.getTag(group, valueTag);
                    result = tag.getText();
                    println("%s[%d] - %s: %s", group.getName(), seqNum, valueTag, result);
                    break search;
                }

                case False ->
                {
                }

                case Null ->
                {
                    println("'%s' is not a child tag of '%s'[%d]", compareTag, group.getName(), seqNum);
                }
            }

            group = Tags.nextTag(group);
        }

        assertEquals(expResult, result);
    }
}
