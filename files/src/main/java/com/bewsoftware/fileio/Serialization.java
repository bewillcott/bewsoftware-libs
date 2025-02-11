/*
 *  File Name:    Serialization.java
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
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.bewsoftware.fileio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This interface contains helper methods for serializing and deserializing
 * objects to an external binary file.
 *
 * @apiNote
 * Converted from a utility class to an interface. Simplifies understanding (that
 * it is not meant to be instantiated).
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.6
 * @version 3.0.2
 */
public interface Serialization
{
    /**
     * Add the Object {@code obj}, to the external binary file:
     * {@code filename}.
     *
     * @implNote
     * This will only append the object to the end of an existing file.
     * <p>
     * The file must have been created using either of the methods:
     * {@link #store(java.lang.Object, java.lang.String) store}
     * or {@link #storeAll(java.util.List, java.lang.String) storeAll}.
     *
     * @param <T>      type of Object
     * @param obj      to store
     * @param filename file to store into
     *
     * @throws IOException if any
     */
    public static <T> void add(final T obj, final String filename) throws IOException
    {
        // Write out the objects
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename, true))
        {
            @Override
            protected void writeStreamHeader() throws IOException
            {
                reset();
            }
        })
        {
            out.writeObject(obj);
            out.flush();
        }
    }

    /**
     * Add all of the Objects in {@code objs}, to the external binary file:
     * {@code filename}.
     *
     * @implNote
     * This will only append the objects to the end of an existing file.
     * <p>
     * The file must have been created using either of the methods:
     * {@link #store(java.lang.Object, java.lang.String) store}
     * or {@link #storeAll(java.util.List, java.lang.String) storeAll}.
     *
     * @param <T>      type of Object
     * @param objs     List to store
     * @param filename file to store into
     *
     * @throws IOException if any
     */
    public static <T> void addAll(final List<T> objs, final String filename) throws IOException
    {
        // Write out the objects
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename, true))
        {
            @Override
            protected void writeStreamHeader() throws IOException
            {
                reset();
            }
        })
        {
            for (T obj : objs)
            {
                out.writeObject(obj);
            }

            out.flush();
        }
    }

    /**
     * Read all the Objects from the external binary file: {@code filename}.
     *
     * @implSpec
     * All of the objects must be of type: T.
     *
     * @param <T>      type of Object
     * @param filename file to read from
     *
     * @return new list of Objects of type: T
     *
     * @throws ClassNotFoundException if any
     * @throws IOException            if any
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> readObjects(final String filename)
            throws ClassNotFoundException, IOException
    {

        final List<T> objs = new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));)
        {
            T obj = null;

            do
            {
                obj = (T) in.readObject();
                objs.add(obj);
            } while (true);

        } catch (EOFException ex)
        {
            // Job done!
        }

        return objs;
    }

    /**
     * Store the Object {@code obj}, into the external binary file:
     * {@code filename}.
     *
     * @implNote
     * This will either overwrite an existing file, or create a new one if none
     * exists.
     *
     * @param <T>      type of Object
     * @param obj      to store
     * @param filename file to store into
     *
     * @throws IOException if any
     */
    public static <T> void store(final T obj, final String filename) throws IOException
    {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename)))
        {
            out.writeObject(obj);
            out.flush();
        }
    }

    /**
     * Store all the Objects in {@code objs}, into the external binary file:
     * {@code filename}.
     *
     * @implNote
     * This will either overwrite an existing file, or create a new one if none
     * exists.
     *
     * @param <T>      type of Object
     * @param objs     List to store
     * @param filename file to store into
     *
     * @throws IOException if any
     */
    public static <T> void storeAll(final List<T> objs, final String filename) throws IOException
    {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename)))
        {
            for (T obj : objs)
            {
                out.writeObject(obj);
            }

            out.flush();
        }
    }
}
