/*
 *  File Name:    Finder.java
 *  Project Name: bewsoftware-files
 *
 *  Copyright (c) 2023 Bradley Willcott
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
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.bewsoftware.utils.io.DisplayDebugLevel.DEBUG;
import static com.bewsoftware.utils.io.DisplayDebugLevel.INFO;
import static java.nio.file.FileVisitResult.CONTINUE;

/**
 * This class is used by
 * {@link java.nio.file.Files#walkFileTree(java.nio.file.Path, java.nio.file.FileVisitor)
 * Files.walkFileTree}.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.7
 * @version 3.0.2
 */
public class Finder extends SimpleFileVisitor<Path>
{
    private final Display display;

    private final SortedSet<Path> filenames = new TreeSet<>();

    private final PathMatcher matcher;

    private int numMatches = 0;

    /**
     * Creates a new instance of the {@code Finder} class.
     *
     * @param display
     * @param pattern The file search pattern to be used.
     */
    public Finder(final Display display, final String pattern)
    {
        this.display = display;
        display.println(DEBUG,pattern);

        matcher = FileSystems.getDefault()
                .getPathMatcher("glob:" + Objects.requireNonNull(pattern));
    }

    /**
     * Prints the total number of matches to standard out.
     *
     * @return the sorted list of filename
     */
    public SortedSet<Path> done()
    {
        display.println(INFO,"Matched: " + numMatches);
        return Collections.unmodifiableSortedSet(filenames);
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
    {
        find(file);
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exc)
    {
        display.println(DEBUG,"visitFileFailed: " + exc);
        return CONTINUE;
    }

    /**
     * Compares the glob pattern against the file name.
     *
     * @param file File to check.
     */
    private void find(final Path file)
    {
        Path name = Objects.requireNonNull(file).getFileName();

        if (name != null && matcher.matches(name))
        {
            numMatches++;
            display.println(DEBUG,file);
            filenames.add(file);
        }
    }
}
