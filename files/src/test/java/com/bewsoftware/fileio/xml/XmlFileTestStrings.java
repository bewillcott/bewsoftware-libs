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
}
