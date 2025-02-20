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

import com.bewsoftware.utils.string.MessageBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.bewsoftware.utils.string.Strings.notEmpty;
import static com.bewsoftware.utils.string.Strings.print;
import static com.bewsoftware.utils.string.Strings.println;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 */
public class XmlFileTest implements XmlFileTestStrings
{
    /**
     * This function is used with the <i>test_pom.xml</i> file.
     * <p>
     * It supplies the previously processed text string to compare with.
     * The reason for this, is that I wanted to keep is simple.
     *
     * @since 3.1.0
     */
    private static final Function<Path, String> readString = (final Path xmlPath) ->
    {
        return TEST_POM;
    };

    /**
     * This function is used with the files other than the <i>test_pom.xml</i> file.
     * <p>
     * The source file is read-in and supplied as a text string to compare
     * with the test results.
     *
     * @since 3.1.0
     */
    private static final Function<Path, String> readXmlFile = (final Path xmlPath) ->
    {
        final MessageBuilder mb = new MessageBuilder();

        try (BufferedReader in = Files.newBufferedReader(xmlPath))
        {
            String line;

            while ((line = in.readLine()) != null)
            {
                final String sLine = line.trim();

                if (notEmpty(sLine))
                {
                    mb.appendln(line);
                }
            }
        } catch (IOException ignored)
        {
            mb.appendln(ignored);
        }

        return mb.toString();
    };

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

    /**
     * Provides the testing arguments for:
     * {@link #testLoadFile(String, Function) testLoadFile(filename, expResult)}
     *
     * @return a stream of arguments.
     *
     * @since 3.1.0
     */
    static Stream<Arguments> testLoadFile()
    {
        return Stream.of(
                arguments("cd_catalog.xml", readXmlFile),
                arguments("company.xml", readXmlFile),
                arguments("plant_catalog.xml", readXmlFile),
                arguments("sample-xml-files-sample-4.xml", readXmlFile),
                arguments("sample-xml-files-sample-5.xml", readXmlFile),
                arguments("sample-xml-files-sample-6.xml", readXmlFile),
                arguments("test_pom.xml", readString)
        );
    }

    /**
     * Provides the testing arguments for:
     * {@link #testXPath(String, String, String, String)
     * testXPath(filename, xpath, valueTag, expResult)}.
     *
     * @return a stream of arguments.
     *
     * @since 3.1.0
     */
    static Stream<Arguments> testXPath()
    {
        return Stream.of(
                arguments("cd_catalog.xml", "/CATALOG/CD", "TITLE", CATALOG_TITLES),
                arguments("company.xml", "/company/employees/employee", "name", COMPANY_EMP_NAMES),
                arguments("plant_catalog.xml", "/CATALOG/PLANT", "COMMON", PLANTS),
                arguments("sample-xml-files-sample-4.xml", "/root/book", "author", SAMPLE4),
                arguments("sample-xml-files-sample-5.xml", "/root/book", "author", SAMPLE5),
                arguments("sample-xml-files-sample-6.xml", "/root/book", "author", SAMPLE6),
                arguments("test_pom.xml", "/project/dependencies/dependency", "artifactId", POM_ARTIFACTIDS)
        );
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
}
