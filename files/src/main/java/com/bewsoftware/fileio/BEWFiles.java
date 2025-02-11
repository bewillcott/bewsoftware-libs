/*
 *  File Name:    BEWFiles.java
 *  Project Name: bewsoftware-files
 *
 *  Copyright (c) 2020-2023 Bradley Willcott
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.bewsoftware.fileio;

import com.bewsoftware.utils.io.Display;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.spi.FileSystemProvider;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bewsoftware.utils.io.DisplayDebugLevel.DEBUG;
import static java.nio.file.Files.*;
import static java.nio.file.Path.of;

/**
 * This class contains various helper methods for file and directory operations.
 *
 * @apiNote
 * Converted from a utility class to an interface. Simplifies understanding (that
 * it is not meant to be instantiated).
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 3.0.2
 */
public interface BEWFiles
{
    /**
     * Recursively copies the directories and files of the {@code sourceDir} to
     * the {@code destDir}.
     * <p>
     * Example:
     * <pre><code>
     * public static void main(String[] args) throws IOException {
     *     BEWFiles.copyDirTree("src", "target/src", "*.{html,css}", 2, COPY_ATTRIBUTES, REPLACE_EXISTING);
     * }</code></pre>
     *
     * @param display   Used to display output.
     * @param sourceDir Source Directory.
     * @param destDir   Destination Directory.
     * @param pattern   Regex pattern to find files.
     * @param options   List of objects that configure how to copy or move a
     *                  file.
     *
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If thread is interrupted.
     *
     * @since 3.0.1
     */
    public static void copyDirTree(
            final Display display,
            final String sourceDir,
            final String destDir,
            final String pattern,
            final CopyOption... options
    ) throws IOException, InterruptedException
    {
        final Path srcPath = (sourceDir != null ? of(sourceDir) : of(""));
        final Path destPath = (destDir != null ? of(destDir) : of(""));

        copyDirTree(display, srcPath, destPath, pattern, options);
    }

    /**
     * Recursively copies the directories and files of the {@code srcPath} to
     * the {@code destPath}.
     * <p>
     * This method would most likely only be called by the
     * {@linkplain #copyDirTree(com.bewsoftware.utils.io.Display, java.lang.String,
     * java.lang.String, java.lang.String, java.nio.file.CopyOption...) }
     * method.
     *
     * @param display  Used to display output.
     * @param srcPath  Source Path.
     * @param destPath Destination Path.
     * @param pattern  File search pattern.
     * @param options  List of objects that configure how to copy or move a
     *                 file.
     *
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If thread is interrupted.
     *
     * @since 3.0.1
     */
    public static void copyDirTree(
            final Display display,
            final Path srcPath,
            final Path destPath,
            final String pattern,
            final CopyOption... options
    ) throws IOException, InterruptedException
    {
        // Can't be copying source onto itself.
        // No point.
        if (Objects.requireNonNull(srcPath).equals(Objects.requireNonNull(destPath)))
        {
            return;
        }

        // Initialise and prepare finder...
        final Finder finder = new Finder(display, pattern != null ? pattern : "*");

        // This is to let 'finder' collect relevant information.
        walkFileTree(srcPath, finder);

        // Process list of files/directories found...
        final SortedSet<Path> inList = finder.done();
        final List<FileData> outList = new ArrayList<>(inList.size());
        final Set<Path> dirList = new TreeSet<>();

        processInList(srcPath, inList, destPath, outList, dirList, display);

        display.appendln(DEBUG, "Creating directories ...");

        for (Path dir : dirList)
        {
            display.append(DEBUG, "    ").appendln(DEBUG, dir);
            createDirectories(dir);
        }

        display.flush();
        display.appendln(DEBUG, "Copying files ...");

        for (FileData fileData : outList)
        {
            display.appendln(DEBUG, fileData.sourcePath)
                    .append(DEBUG, "    ")
                    .appendln(DEBUG, fileData.destinationPath);

            copy(fileData.sourcePath, fileData.destinationPath, options);
        }

        display.flush();
    }

    /**
     * Delete the 'targetDir' directory and it's contents, recursively.
     * <p>
     * <b>WARNING:</b> <i>There is no recovery from this.</i> Once this method
     * returns, the entire {@code targetDir} will be GONE!
     *
     * @param targetDir Directory to delete.
     *
     * @throws IOException if any.
     */
    public static void deleteDirTree(final Path targetDir) throws IOException
    {
        walkFileTree(targetDir, new SimpleFileVisitor<Path>()
        {
            @Override
            public FileVisitResult postVisitDirectory(final Path dir, final IOException e)
                    throws IOException
            {
                if (e == null)
                {
                    delete(dir);
                    return FileVisitResult.CONTINUE;
                } else
                {
                    // directory iteration failed
                    throw e;
                }
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
                    throws IOException
            {
                delete(file);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Get the resource Path from a 'jar' file.
     *
     * @param clazz Calling class.
     * @param name  Resource name.
     *
     * @return Path to the resource.
     *
     * @throws IOException        General IO failure.
     * @throws URISyntaxException Name produces an invalid path data.
     */
    public static Path getResource(
            final Class<?> clazz,
            final String name
    ) throws IOException, URISyntaxException
    {
        // clazz and name must NOT be null.
        if (clazz == null || name == null || name.isBlank())
        {
            return null;
        }

        final URI uri = clazz.getResource(name).toURI();

        if ("jar".equals(uri.getScheme()))
        {
            for (FileSystemProvider provider : FileSystemProvider.installedProviders())
            {
                if (provider.getScheme().equalsIgnoreCase("jar"))
                {
                    try
                    {
                        provider.getFileSystem(uri);
                    } catch (FileSystemNotFoundException e)
                    {
                        // in this case we need to initialize it first:
                        provider.newFileSystem(uri, Collections.emptyMap());
                    }
                }
            }
        }

        return Paths.get(uri);
    }

    private static void processInList(
            final Path srcPath,
            final SortedSet<Path> inList,
            final Path destPath,
            final List<FileData> outList,
            final Set<Path> dirList,
            final Display display
    ) throws IOException
    {
        final Pattern pattern1 = Pattern.compile("^(?<filename>.*)$");
        final Pattern pattern2 = Pattern.compile("^(?:" + srcPath + "/)(?<filename>.*)$");

        for (Path inPath : inList)
        {
            Matcher matcher;

            if (srcPath.toString().isEmpty())
            {
                matcher = pattern1.matcher(inPath.toString());
            } else
            {
                matcher = pattern2.matcher(inPath.toString());
            }

            if (matcher.find())
            {
                final Path outPath = of(destPath.toString(), matcher.group("filename"));

                if (notExists(outPath) || getLastModifiedTime(inPath).compareTo(getLastModifiedTime(outPath)) > 0)
                {
                    outList.add(new FileData(inPath, outPath));
                    final Path parent = outPath.getParent();

                    if (notExists(parent))
                    {
                        dirList.add(parent);
                    }

                    display.println(DEBUG, outPath);
                }
            }
        }
    }

    @SuppressWarnings("PublicInnerClass")
    static class FileData
    {
        public final Path destinationPath;

        public final Path sourcePath;

        private FileData(Path sourcePath, Path destinationPath)
        {
            this.sourcePath = sourcePath;
            this.destinationPath = destinationPath;
        }
    }
}
