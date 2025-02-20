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

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.bewsoftware.utils.string.Strings.println;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 */
public class XmlFileTest
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

    static Stream<Arguments> testPV()
    {
        return Stream.of(
                arguments("Hello", "World!"),
                arguments("Hello", "there!")
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
     * @param filename
     *
     * @throws com.bewsoftware.fileio.xml.XmlFileFormatException
     * @throws java.io.IOException
     */
    @ParameterizedTest
    @ValueSource(strings =
    {
        "cd_catalog.xml",
        "company.xml",
        "plant_catalog.xml",
        "sample-xml-files-sample-4.xml",
        "sample-xml-files-sample-5.xml",
        "sample-xml-files-sample-6.xml",
        "test_pom.xml"
    })
    public void testLoadFile(final String filename) throws XmlFileFormatException, IOException
    {
        println("testLoadFile");

        println(filename);

        final Path xmlFilePath = Path.of(getClass().getResource(filename).getPath());

        println(xmlFilePath);

        final XmlFile xmlFile = new XmlFile(xmlFilePath);
        XmlFile result = xmlFile.loadFile();
        assertTrue(xmlFile.isLoaded());

        println(xmlFile);

        println("testLoadFile: completed");
    }

    @ParameterizedTest
    @MethodSource
    public void testPV(final String arg1, final String arg2)
    {
        println("%s, %s", arg1, arg2);
    }

}
