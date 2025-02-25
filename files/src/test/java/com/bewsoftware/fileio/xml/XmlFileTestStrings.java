/*
 *  File Name:    XmlFileTestStrings.java
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

import static com.bewsoftware.utils.Ternary.False;
import static com.bewsoftware.utils.Ternary.Null;
import static com.bewsoftware.utils.Ternary.True;
import static com.bewsoftware.utils.string.Strings.notEmpty;
import static com.bewsoftware.utils.string.Strings.println;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Contains the test expected result strings for {@link XmlFileTest}.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.1.0
 * @version 3.1.0
 */
public interface XmlFileTestStrings
{

    static final String CATALOG_TITLES
            = "[cd_catalog.xml]\n"
            + "CD\n"
            + "    TITLE: 'dill diya galla'\n"
            + "CD\n"
            + "    TITLE: 'Saiyara'\n"
            + "CD\n"
            + "    TITLE: 'Khairiyat'\n"
            + "CD\n"
            + "    TITLE: 'all is well'\n"
            + "CD\n"
            + "    TITLE: 'rockstar'\n";

    static final String COMPANY_EMP_NAMES
            = "[company.xml]\n"
            + "employee\n"
            + "    name: 'John Doe'\n"
            + "employee\n"
            + "    name: 'Steven Broad'\n"
            + "employee\n"
            + "    name: 'Jane Warner'\n"
            + "employee\n"
            + "    name: 'Ben Doe'\n"
            + "employee\n"
            + "    name: 'John Smith'\n"
            + "employee\n"
            + "    name: 'Ben Marsh'\n"
            + "employee\n"
            + "    name: 'Jonahan'\n"
            + "employee\n"
            + "    name: 'John Doe'\n";

    static final String PLANTS
            = "[plant_catalog.xml]\n"
            + "PLANT\n"
            + "    COMMON: 'Bloodroot'\n"
            + "PLANT\n"
            + "    COMMON: 'Columbine'\n"
            + "PLANT\n"
            + "    COMMON: 'Marsh Marigold'\n"
            + "PLANT\n"
            + "    COMMON: 'Cowslip'\n"
            + "PLANT\n"
            + "    COMMON: 'Dutchman's-Breeches'\n"
            + "PLANT\n"
            + "    COMMON: 'Ginger, Wild'\n"
            + "PLANT\n"
            + "    COMMON: 'Hepatica'\n"
            + "PLANT\n"
            + "    COMMON: 'Liverleaf'\n"
            + "PLANT\n"
            + "    COMMON: 'Jack-In-The-Pulpit'\n"
            + "PLANT\n"
            + "    COMMON: 'Mayapple'\n"
            + "PLANT\n"
            + "    COMMON: 'Phlox, Woodland'\n"
            + "PLANT\n"
            + "    COMMON: 'Phlox, Blue'\n"
            + "PLANT\n"
            + "    COMMON: 'Spring-Beauty'\n"
            + "PLANT\n"
            + "    COMMON: 'Trillium'\n"
            + "PLANT\n"
            + "    COMMON: 'Wake Robin'\n"
            + "PLANT\n"
            + "    COMMON: 'Violet, Dog-Tooth'\n"
            + "PLANT\n"
            + "    COMMON: 'Trout Lily'\n"
            + "PLANT\n"
            + "    COMMON: 'Adder's-Tongue'\n"
            + "PLANT\n"
            + "    COMMON: 'Anemone'\n"
            + "PLANT\n"
            + "    COMMON: 'Grecian Windflower'\n"
            + "PLANT\n"
            + "    COMMON: 'Bee Balm'\n"
            + "PLANT\n"
            + "    COMMON: 'Bergamot'\n"
            + "PLANT\n"
            + "    COMMON: 'Black-Eyed Susan'\n"
            + "PLANT\n"
            + "    COMMON: 'Buttercup'\n"
            + "PLANT\n"
            + "    COMMON: 'Crowfoot'\n"
            + "PLANT\n"
            + "    COMMON: 'Butterfly Weed'\n"
            + "PLANT\n"
            + "    COMMON: 'Cinquefoil'\n"
            + "PLANT\n"
            + "    COMMON: 'Primrose'\n"
            + "PLANT\n"
            + "    COMMON: 'Gentian'\n"
            + "PLANT\n"
            + "    COMMON: 'Blue Gentian'\n"
            + "PLANT\n"
            + "    COMMON: 'Jacob's Ladder'\n"
            + "PLANT\n"
            + "    COMMON: 'Greek Valerian'\n"
            + "PLANT\n"
            + "    COMMON: 'California Poppy'\n"
            + "PLANT\n"
            + "    COMMON: 'Shooting Star'\n"
            + "PLANT\n"
            + "    COMMON: 'Snakeroot'\n"
            + "PLANT\n"
            + "    COMMON: 'Cardinal Flower'\n";

    static final String POM_ARTIFACTIDS
            = "[test_pom.xml]\n"
            + "dependency\n"
            + "    artifactId: 'bewsoftware-common'\n"
            + "dependency\n"
            + "    artifactId: 'bewsoftware-utils'\n"
            + "dependency\n"
            + "    artifactId: 'derby'\n"
            + "dependency\n"
            + "    artifactId: 'junit-jupiter-api'\n"
            + "dependency\n"
            + "    artifactId: 'junit-jupiter-params'\n"
            + "dependency\n"
            + "    artifactId: 'junit-jupiter-engine'\n";

    static final String SAMPLE4
            = "[sample-xml-files-sample-4.xml]\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n";

    static final String SAMPLE5
            = "[sample-xml-files-sample-5.xml]\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n";

    static final String SAMPLE6
            = "[sample-xml-files-sample-6.xml]\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n"
            + "book\n"
            + "    author: 'Robert Johnson'\n";

    static final String TEST_POM
            //            = "[test_pom.xml]\n"
            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
            + "    <modelVersion>4.0.0</modelVersion>\n"
            + "    <parent>\n"
            + "        <groupId>com.bewsoftware</groupId>\n"
            + "        <artifactId>bewsoftware-pom</artifactId>\n"
            + "        <version>3.1.0-SNAPSHOT</version>\n"
            + "        <relativePath>../pom/pom.xml</relativePath>\n"
            + "    </parent>\n"
            + "    <artifactId>bewsoftware-files</artifactId>\n"
            + "    <packaging>jar</packaging>\n"
            + "    <name>BEWSoftware Files Library</name>\n"
            + "    <description>This library contains classes related to working with files,\n"
            + "    including data storage.</description>\n"
            + "    <dependencies>\n"
            + "        <dependency>\n"
            + "            <groupId>com.bewsoftware</groupId>\n"
            + "            <artifactId>bewsoftware-common</artifactId>\n"
            + "        </dependency>\n"
            + "        <dependency>\n"
            + "            <groupId>com.bewsoftware</groupId>\n"
            + "            <artifactId>bewsoftware-utils</artifactId>\n"
            + "        </dependency>\n"
            + "        <!-- Unit Test dependencies -->\n"
            + "        <dependency>\n"
            + "            <groupId>org.apache.derby</groupId>\n"
            + "            <artifactId>derby</artifactId>\n"
            + "            <scope>test</scope>\n"
            + "        </dependency>\n"
            + "        <dependency>\n"
            + "            <groupId>org.junit.jupiter</groupId>\n"
            + "            <artifactId>junit-jupiter-api</artifactId>\n"
            + "            <scope>test</scope>\n"
            + "        </dependency>\n"
            + "        <dependency>\n"
            + "            <groupId>org.junit.jupiter</groupId>\n"
            + "            <artifactId>junit-jupiter-params</artifactId>\n"
            + "            <scope>test</scope>\n"
            + "        </dependency>\n"
            + "        <dependency>\n"
            + "            <groupId>org.junit.jupiter</groupId>\n"
            + "            <artifactId>junit-jupiter-engine</artifactId>\n"
            + "            <scope>test</scope>\n"
            + "        </dependency>\n"
            + "    </dependencies>\n"
            + "</project>\n";

    /**
     * This function is used to find out if the {@code group} tag is the tag being searched for.
     * <p>
     * Internally this method accesses the {@code compareTag} , which should be a child tag of the {@code group} tag,
     * and then compares its {@code text} value with the {@code compareText} string.
     * <br>
     * <dl class="notes">
     * <dt><b>Parameters:</b></dt>
     * <dd><code>group</code> - The tag being checked.</dd>
     * <dd><code>compareTag</code> - The name of the child tag to do the test on.</dd>
     * <dd><code>compareText</code> - The <i>text</i> to test the {@code compareTag} with.</dd>
     * <dt><b>Returns:</b></dt>
     * <dd><i>True</i> if the comparison is successful,</dd>
     * <dd><i>False</i> if not, and</dd>
     * <dd><i>Null</i> if {@code compareTag} was not found.</dd>
     * </dl>
     *
     * @since 3.1.0
     */
    static final TriFunction<Tag, String, String, Ternary> isTheTag
            = (final Tag group, final String compareTag, final String compareText) ->
    {
        Ternary rtn = Null;
        final Tag tag = Tags.getTag(group, compareTag);

        if (tag != null)
        {
            rtn = tag.getText().equals(compareText) ? True : False;
        }

        return rtn;
    };

    /**
     * This function is used with the <i>test_pom.xml</i> file.
     * <p>
     * It supplies the previously processed text string to compare with.
     * The reason for this, is that I wanted to keep is simple.
     *
     * @since 3.1.0
     */
    static final Function<Path, String> readString = (final Path xmlPath) -> TEST_POM;

    /**
     * This function is used with the files other than the <i>test_pom.xml</i> file.
     * <p>
     * The source file is read-in and supplied as a text string to compare
     * with the test results.
     *
     * @since 3.1.0
     */
    static final Function<Path, String> readXmlFile = (final Path xmlPath) ->
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

    /**
     * Saves the supplied text to the designated file.
     *
     * @param text        to save
     * @param xmlFilePath to write to.
     *
     * @throws java.io.IOException
     *
     * @since 3.1.0
     */
    static void saveXml(final String text, final String xmlFilePath) throws IOException
    {
        println("Saving to: %s",xmlFilePath);

        try (final BufferedWriter bw = new BufferedWriter(new FileWriter(xmlFilePath)))
        {
            bw.write(text);
            bw.flush();
        }
    }

    /**
     * Provides the testing arguments for:
     * {@link XmlFileTest#testLoadFile(String, Function) testLoadFile(filename, expResult)}
     *
     * @return a stream of arguments.
     *
     * @since 3.1.0
     */
    static Stream<Arguments> testLoadFile()
    {
        return Stream.of(
                //                arguments("cd_catalog.xml", readXmlFile),
                //                arguments("company.xml", readXmlFile),
                //                arguments("plant_catalog.xml", readXmlFile),
                //                arguments("sample-xml-files-sample-4.xml", readXmlFile),
                //                arguments("sample-xml-files-sample-5.xml", readXmlFile),
                //                arguments("sample-xml-files-sample-6.xml", readXmlFile),
                arguments("test_pom.xml", readXmlFile),
                arguments("test_pom2.xml", readXmlFile)
        );
    }

    /**
     * Provides the testing arguments for:
     * {@link XmlFileTest#testXPath(String, String, String, String)
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

    /**
     * Provides the testing arguments for:
     * {@link XmlFileTest#testXPathFind(String, String, BiPredicate, String, String, String)
     * testXPathFind(filename, xpath, comparator, searchText, valueTag, expResult)}.
     *
     * @return a stream of arguments.
     *
     * @since 3.1.0
     */
    static Stream<Arguments> testXPathFind()
    {
        return Stream.of(
                arguments("cd_catalog.xml", "/CATALOG/CD", isTheTag, "TITLE", "Saiyara", "ARTIST", "Atif Aslam"),
                arguments("company.xml", "/company/employees/employee", isTheTag, "name", "Jane Warner", "position", "Software Engineer"),
                arguments("plant_catalog.xml", "/CATALOG/PLANT", isTheTag, "COMMON", "Dutchman's-Breeches", "PRICE", "$6.44"),
                arguments("sample-xml-files-sample-4.xml", "/root/person", isTheTag, "name", "Jane Smith", "email", "jane.smith@example.com"),
                arguments("sample-xml-files-sample-5.xml", "/root/person", isTheTag, "email", "john.doe@example.com", "name", "John Doe"),
                arguments("sample-xml-files-sample-6.xml", "/root/book", isTheTag, "author", "Robert Johnson", "title", "The Adventure Begins"),
                arguments("test_pom.xml", "/project/dependencies/dependency", isTheTag, "groupId", "org.junit.jupiter", "artifactId", "junit-jupiter-api")
        );
    }
}
