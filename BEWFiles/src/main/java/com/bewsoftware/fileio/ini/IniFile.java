/*
 * This file is part of the BEW Files Library (aka: BEWFiles).
 *
 * Copyright (C) 2020 Bradley Willcott
 *
 * BEWFiles is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BEWFiles is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.bewsoftware.fileio.ini;

import com.bewsoftware.common.InvalidParameterValueException;
import com.bewsoftware.common.InvalidProgramStateException;
import com.bewsoftware.property.IniProperty;
import com.bewsoftware.property.MutableIniProperty;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.exit;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * <p>
 * This class provides access to properties in a file that uses the format of a
 * standard Windows <u>.ini</u> file.
 * </p><p>
 * Like this:
 * </p>
 * <pre>
 * ; Windows type ini file format
 *
 * [section]
 * key1 = value
 * ; Value is of greatest importance
 * key2 = value
 * key3 = value
 * </pre>
 * <p>
 * <b>Note:</b> The file extension can be anything that the user deems
 * appropriate. There are many files with the <u>.conf</u> extension that use
 * this same file format.
 * </p><p>
 * In addition, it is compatible with the {@code java.util.Properties} type of
 * file format:
 * </p>
 * <pre><code>
 *
 * # Properties file format
 *
 * key1=value
 * key2=value ; This is not treated as a comment, but as part of the value.
 * key3=value
 * </code></pre>
 * <p>
 * Furthermore, you can combine both types in the one file.
 * </p>
 * <pre><code>
 *
 * # Properties file format
 *
 * key1=value
 * key2=value
 * key3=value
 * Something Extra=Look Ma, spaces in the Key!
 *
 * ; Windows type ini file format
 *
 * [section]
 * key1 = value
 * key2 = value
 * key3 = value
 * That's right=You can include spaces in your Keys.
 * This one will cause an Exception to be thrown, though.
 * Neither of these are either comments or Keys.
 * </code></pre>
 * <p>
 * <b>Note:</b> The special characters:
 * </p>
 * <ul>
 * <li>"<b>#</b>" number symbol</li>
 * <li>"<b>;</b>" semi-colon</li>
 * </ul>
 * <p>
 * They are both accepted as comment tags, provided they are the first character
 * on a line, and are followed by at least <b>one</b> blank space or tab
 * ({@code "\t"}), as shown above. Check out the {@link IniDocument} class. It
 * has methods that provide easy access to, and management of, the associated
 * comments as shown above. See
 * {@link IniDocument#getComment(java.lang.String, java.lang.String) IniDocument.getComment},
 * and null {@link IniDocument#setComment(java.lang.String, java.lang.String, java.lang.String)
 * IniDocument.setComment}. Also there are versions of the various other
 * {@code set*()} methods, such as null {@link IniDocument#setBoolean(java.lang.String, java.lang.String, boolean, java.lang.String)
 * IniDocument.setBoolean}, that allow you to set the {@code comment} at the
 * same time as setting the {@code value}.
 * </p><p>
 * Finally, a bit of clarification might be in order, as to the relationship
 * between these two classes: {@code IniFile} and {@link IniDocument}. As its
 * name implies, {@code IniDocument} holds all the data, maintaining the
 * original loading sequence, whether from file or program. {@code IniFile}
 * provides the front-end interface with the file system. Further, it does the
 * parsing of the
 * <u>ini</u> file, handling the decoding of the file data based on the
 * standards coded into it. It also handles the saving of the data out to a
 * file, writing it out in the correct format.
 * </p><p>
 * Though it is possible for you to use {@code IniDocument} directly within your
 * own program, it is not recommended, unless you wish to write your own parsing
 * front-end to replace {@code IniFile}. By instantiating {@code IniFile}, you
 * have access to all that you need to read and write your ini files. Through
 * its public member {@link #iniDoc}, you can access the internal instance of
 * {@link IniDocument}, and then all of its members and methods.
 * </p>
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0.20
 */
public class IniFile {

    /**
     * To be used for testing purposes <i>only</i>.
     *
     * @param args Not used.
     *
     * @throws FileNotFoundException If test files not found.
     * @since 1.0
     */
    public static void main(String[] args) throws FileNotFoundException {
        final URL fileUrl = IniFile.class.getResource("/Test.ini");
        final URL fileUrlOrig = IniFile.class.getResource("/Test-orig.ini");

        Path path = null;
        Path pathOrig = null;

        if (fileUrl != null)
        {
            path = FileSystems.getDefault().getPath(fileUrl.getPath());
        } else
        {
            throw new FileNotFoundException("/Test.ini");
        }

        if (fileUrlOrig != null)
        {
            pathOrig = FileSystems.getDefault().getPath(fileUrlOrig.getPath());
        } else
        {
            throw new FileNotFoundException("/Test-orig.ini");
        }

        final int SECTION = 0;
        final int KEY = 1;
        final int VALUE = 2;

        final String[][] INPUT =
        {
            {
                "others", "Home", "Newtown"
            },
            {
                "Employees", "001", "Fred Smith"
            },
            {
                "Fred Smith", "Address", "10 Anders Ave"
            },
            {
                "Fred Smith", "Suburb", "Jinville"
            },
            {
                "Fred Smith", "State", "SA"
            },
            {
                "Fred Smith", "Phone", "0412-345-395"
            },
            {
                "Fred Smith", "Comments", "Good worker"
            },
            {
                "Employees", "002", "Peter Davis"
            },
            {
                "Peter Davis", "Address", "12A Anders Way"
            },
            {
                "Peter Davis", "Suburb", "Shineytown"
            },
            {
                "Peter Davis", "State", "Vic"
            },
            {
                "Peter Davis", "Phone", "0428-859-271"
            },
            {
                "Peter Davis", "Comments", "Avg. worker"
            }
        };

        @SuppressWarnings("UnusedAssignment")
        IniFile ini = null;

        try
        {

            Files.copy(pathOrig, path, StandardCopyOption.REPLACE_EXISTING);
            (ini = new IniFile(path)).loadFile();

            for (String[] inparr : INPUT)
            {
                ini.iniDoc.setString(inparr[SECTION], inparr[KEY], inparr[VALUE]);
            }

            System.out.println("com.bew.commons.io.IniFile.main()");
//            System.out.println(ini);

            ini.saveFile();

            ini = new IniFile(path);

            // Display all employees details
            for (IniProperty<Object> employee : ini.iniDoc.getSection("Employees"))
            {
                System.out.println(
                        "Id = " + employee.key()
                        + " | Name = " + employee.value()
                        + " | Comment=" + employee.comment()
                );

                for (IniProperty<Object> empDetails : ini.iniDoc.getSection((String) employee.value()))
                {
                    System.out.println((empDetails.comment() != null ? "\t" + empDetails.comment() + "\n" : "")
                                       + "\t" + empDetails.key() + ": "
                                       + empDetails.value()
                    );
                }
                System.out.println();
            }

        } catch (NullPointerException | IOException
                 | IniFileFormatException | InvalidParameterValueException ex)
        {
            System.err.println(IniFile.class.getName() + ".main():\n" + ex);
            exit(1);
        }
    }

    /**
     * The document contains all the properties:<br>
     * <ul>
     * <li>loaded from the file</li>
     * <li>added in memory</li>
     * <li>modified in memory</li>
     * </ul>
     *
     * @since 1.0
     */
    public final IniDocument iniDoc;

    /**
     * This setting is referred to at the time of saving the data to file.<p>
     * If {@code true}, then " = " will be used to separate the key/value data:
     * "key = value".<br>
     * If {@code false}, then "=" will be used to separate the key/value data:
     * "key=value"
     * .<p>
     * The default is {@code false}.
     *
     * @since 1.0
     */
    public boolean paddedEquals = false;

    /**
     * The path to the <u>ini</u> file.
     *
     * @since 1.0
     */
    public final Path path;

    private boolean fileIsLoaded;

    /**
     * This constructor uses the <b>Path</b> object passed in the <i>path</i>
     * parameter.
     *
     * @param path The <b>Path</b> object to use.
     *
     * @throws NullPointerException           If the path parameter is {@code null}.
     * @throws InvalidParameterValueException If path.toString().isBlank().
     * @since 1.0
     */
    public IniFile(Path path)
            throws NullPointerException, InvalidParameterValueException {
        if (path == null)
        {
            throw new NullPointerException("path is null");
        } else if (path.toString().isBlank())
        {
            throw new InvalidParameterValueException("path is blank");
        } else
        {
            this.path = path;
            iniDoc = new IniDocument();
        }
    }

    /**
     * Default constructor.
     *
     * @since 1.0
     */
    public IniFile() {
        iniDoc = new IniDocument();
        path = null;
    }

    /**
     * @return <i>true</i> if the file has been loaded, <i>false</i> otherwise.
     *
     * @since 1.0
     */
    public boolean isLoaded() {
        return fileIsLoaded;
    }

    /**
     * Opens and parses the file, based on the <b>Path</b> set-up by the class
     * constructor.
     *
     * @return this instance for chaining.
     *
     * @throws FileAlreadyLoadedException     The file cannot be reloaded.
     * @throws IniFileFormatException         The format of the <u>ini</u> file
     *                                        is non-conforming.
     * @throws InvalidParameterValueException If the {@code comment} is not a
     *                                        valid <u>ini</u> file format
     *                                        comment.
     * @throws IOException                    An I/O error occurs opening the
     *                                        file
     * @since 1.0
     */
    public IniFile loadFile()
            throws FileAlreadyLoadedException, IniFileFormatException,
                   InvalidParameterValueException, IOException {

        if (fileIsLoaded)
        {
            throw new FileAlreadyLoadedException();

        }
        try ( BufferedReader in = Files.newBufferedReader(path))
        {
            fileIsLoaded = parseINI(in);
        }

        return this;
    }

    /**
     *
     * @param file File to merge with this ini file.
     *
     * @return this instance for chaining.
     *
     * @throws IOException            if an I/O error occurs opening the file.
     * @throws IniFileFormatException If the format of the supplied ini file does
     *                                not conform to the general standard.
     */
    public IniFile mergeFile(Path file)
            throws IOException, IniFileFormatException {

        if (fileIsLoaded)
        {
            try ( BufferedReader in = Files.newBufferedReader(file))
            {
                parseINI(in);
            }
        } else
        {
            throw new InvalidProgramStateException("File not loaded:\n" + path);
        }

        return this;
    }

    /**
     * Saves the list of properties to the previously designated file.
     * <p>
     * This can be either the file referenced in the <b>Path</b> object passed to the
     * class constructor {@link #IniFile(Path)}.
     * </p>
     *
     * @return this instance for chaining.
     *
     * @throws IOException File i/o problem.
     * @since 1.0
     */
    public IniFile saveFile() throws IOException {
        saveFileAs(path);

        return this;
    }

    /**
     * Saves the list of properties to the file.
     *
     * @param newFile Path of new file to save to.
     *
     * @return this instance for chaining.
     *
     * @throws IOException File i/o problem.
     */
    public IniFile saveFileAs(Path newFile) throws IOException {
        try ( BufferedWriter out = Files.newBufferedWriter(newFile, CREATE, TRUNCATE_EXISTING, WRITE))
        {
            storeINI(out);
        }

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IniFile{\n_path=").append(path);
        sb.append(",\n\nentries=");
        sb.append(iniDoc.toString().replace("{", "\n{\n    ")
                .replace("}", "\n}").replace("=\n", "\n    ==========\n")
                .replace("}, ", "}\n    ").replace(", ", "\n    "));
        sb.append("\n}");
        return sb.toString();
    }

    /**
     *
     * @param key
     * @param value
     *
     * @return
     */
    private String join(String key, String value) {
        String separator = paddedEquals ? " = " : "=";
        return value.isEmpty()
               ? (key + separator).strip()
               : key + separator + value;
    }

    /**
     * Parses the contents of the <i>ini</i> file.
     *
     * @param reader The ini file to be parsed.
     *
     * @throws IOException            If an I/O error occurs.
     * @throws IniFileFormatException If the format of the supplied ini file does
     *                                not conform to the general standard.
     */
    private boolean parseINI(BufferedReader reader)
            throws IOException, IniFileFormatException {
        Pattern p = Pattern.compile(IniDocument.INI_PATTERN);
        String line = "";
        String currentSection = null;
        String lastComment = null;
        int lineNumber = 0;

        while ((line = reader.readLine()) != null)
        {
            lineNumber++;
            Matcher m = p.matcher(line);
            m.find();
            String section = m.group("Section");
            String key = m.group("Key");
            String value = m.group("Value");
            String comment = m.group("Comment");
            String tail = m.group("Tail");

            if (section != null)
            {
                iniDoc.setSection(section, lastComment);
                lastComment = null;
                currentSection = section.strip();
            } else if (key != null)
            {
                iniDoc.setString(currentSection, key.strip(), value.strip(), lastComment);
                lastComment = null;
            } else if (comment != null)
            {
                if (lastComment != null)
                {
                    iniDoc.setComment(currentSection,
                                      lastComment.substring(0, 1) + (lineNumber - 1),
                                      lastComment);
                }

                lastComment = comment;
            } else if (!tail.isEmpty())
            {
                throw new IniFileFormatException(path.toString(),
                                                 "Unknown entry (line# " + lineNumber + "): "
                                                 + tail);
            } else
            {
                if (lastComment != null)
                {
                    iniDoc.setComment(currentSection,
                                      lastComment.substring(0, 1) + (lineNumber - 1),
                                      lastComment);
                    lastComment = null;
                }
            }
        }

        return true;
    }

    private void storeINI(BufferedWriter bw) throws IOException {

        for (MutableIniProperty<ArrayList<MutableIniProperty<Object>>> section : iniDoc.entries)
        {
            if (section.key() != null)
            {
                if (section.comment() != null)
                {
                    bw.write("\n" + section.comment() + "\n[" + section.key() + "]\n");
                } else
                {
                    bw.write("\n[" + section.key() + "]\n");
                }
            }

            for (MutableIniProperty<Object> key : section.value())
            {
                // Do we have a comment?
                if (key.key().startsWith("#") || key.key().startsWith(";"))
                {
                    bw.write(key.comment() + "\n");
                } else if (key.comment() != null)
                {
                    bw.write(key.comment() + "\n" + join(key.key(), (String) key.value()) + "\n");
                } else
                {
                    bw.write(join(key.key(), (String) key.value()) + "\n");
                }
            }
        }
    }

}
