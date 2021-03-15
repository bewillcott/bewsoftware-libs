/*
 *  File Name:    Finder.java
 *  Project Name: bewsoftware-files
 *
 *  Copyright (c) 2021 Bradley Willcott
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
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.bewsoftware.fileio;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.nio.file.FileVisitResult.CONTINUE;

/**
 * This class is used by
 * {@link java.nio.file.Files#walkFileTree(java.nio.file.Path, java.nio.file.FileVisitor)
 * Files.walkFileTree}.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.7
 * @version 1.0.7
 */
public class Finder extends SimpleFileVisitor<Path> {

    private final int vlevel;
    private final PathMatcher matcher;
    private int numMatches = 0;
    private final SortedSet<Path> filenames = new TreeSet<>();

    /**
     * Creates a new instance of the {@code Finder} class.
     *
     * @param pattern The file search pattern to be used.
     * @param vlevel  The verbose level, for info/debugging.
     */
    public Finder(final String pattern, final int vlevel) {
        matcher = FileSystems.getDefault()
                .getPathMatcher("glob:" + Objects.requireNonNull(pattern));
        this.vlevel = vlevel;
    }

    /**
     * Compares the glob pattern against the file name.
     *
     * @param file File to check.
     */
    private void find(final Path file) {
        Path name = Objects.requireNonNull(file).getFileName();

        if (name != null && matcher.matches(name))
        {
            numMatches++;

            if (vlevel >= 2)
            {
                // Printout verbose info.
                System.err.println(file);
            }

            filenames.add(file);
        }
    }

    /**
     * Prints the total number of matches to standard out.
     *
     * @return the sorted list of filename
     */
    public SortedSet<Path> done() {
        if (vlevel >= 1)
        {
            // Printout verbose info.
            System.err.println("Matched: " + numMatches);
        }

        return filenames;
    }

    /**
     * {@inheritDoc }
     * <p>
     * Calls a pattern matching method on each file.
     *
     * @param file  {@inheritDoc }
     * @param attrs {@inheritDoc }
     *
     * @return {@inheritDoc }
     */
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) {
        find(file);
        return CONTINUE;
    }

    /**
     * {@inheritDoc }
     *
     * @param dir   {@inheritDoc }
     * @param attrs {@inheritDoc }
     *
     * @return {@inheritDoc }
     */
    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) {
        return CONTINUE;
    }

    /**
     * {@inheritDoc }
     *
     * @param file {@inheritDoc }
     * @param exc  {@inheritDoc }
     *
     * @return {@inheritDoc }
     */
    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exc) {
        System.err.println("visitFileFailed: " + exc);
        return CONTINUE;
    }

}
