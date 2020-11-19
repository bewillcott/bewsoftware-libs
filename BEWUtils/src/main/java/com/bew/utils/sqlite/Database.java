/*
 * This file is part of the BEW Utils Library (aka: BEWUtils).
 *
 * Copyright (C) 2020 Bradley Willcott
 *
 * BEWUtils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BEWUtils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.bew.utils.sqlite;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;

import static com.bew.utils.sqlite.Database.Status.OFF;
import static com.bew.utils.sqlite.Database.Status.ON;

/**
 * This is intended to provide methods and members related to database
 * operations.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public class Database implements AutoCloseable {

    private final Connection conn;
    private final ArrayList<Statement> stmts;


    {   // Initialize the ArrayList
        stmts = new ArrayList<>();
    }

    /**
     * Create/Open/Replace an Sqlite database file.<p>
     * Foreign Keys Constraint will be set to ON, by default.
     *
     * @param path   name and location of file to create.
     * @param dbOpen Type of connection process.
     *
     * @throws SQLException SQL error
     * @throws IOException  File access error.
     */
    public Database(Path path, DbOpen dbOpen) throws SQLException, IOException {
        boolean fileExists;

        File file = path.toFile();
        fileExists = file.exists();

        switch (dbOpen)
        {
            case CREATE:
                if (fileExists)
                {
                    throw new IOException("Database(CREATE) File exists: " + path);
                }
                break;

            case OPEN:
                if (!fileExists)
                {
                    throw new IOException("Database(OPEN) File does not exist: " + path);
                }
                break;

            case REPLACE:
                if (!fileExists)
                {
                    throw new IOException("Database(REPLACE) File does not exist: " + path);
                }

                file.delete();
                break;
        }

        conn = DriverManager.getConnection("jdbc:sqlite:" + path);
        setForeignKeysConstraint(ON);
    }

    @Override
    public void close() throws SQLException {
        for (Statement stmt : stmts)
        {
            stmt.close();
        }
        conn.close();
    }

    /**
     * Execute an array of SQL statements.
     * <p>
     * The primary purpose of this method is to process a batch of SQL
     * statements for which there will be no returned data. Such as
     * CREATE/INSERT statements.
     *
     * @param arraySQL An array of SQL statements.
     *
     * @return <b>True</b> if ALL statements are successfully executed.
     *
     * @throws SQLException SQL Error
     */
    public boolean execute(String[] arraySQL) throws SQLException {
        String failedSQL = "";

        try ( Statement st = conn.createStatement())
        {

            for (String sql : arraySQL)
            {
                failedSQL = sql;
                st.execute(sql);
            }

            return true;
        }
    }

    /**
     * Use to execute individual queries that are expected to return a
     * ResultSet. Such as: SELECT statements.
     *
     * @param sql SQL string.
     *
     * @return The ResultSet, or null if no data.
     *
     * @throws SQLException SQL Error.
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        Statement st = conn.createStatement();
        stmts.add(st);
        st.closeOnCompletion();

        if (stmts.size() % 5 == 0)
        {
            int changed = 0;

            for (int i = 0; i < stmts.size(); i++)
            {
                Statement stmt = stmts.get(i);

                if (stmt.isClosed())
                {
                    stmts.set(i, null);
                    changed++;
                }
            }

            if (changed > 0)
            {
                for (int i = 0; i < changed; i++)
                {
                    stmts.remove(null);
                }
            }
        }

        return st.executeQuery(sql);
    }

    /**
     * Status of the Foreign Keys Constraint setting.
     *
     * @return Current status.
     *
     * @throws SQLException SQL Error, or 'foreign_keys' constraint not being
     *                      set.
     */
    public final Status getForeignKeysConstraint() throws SQLException {
        try ( Statement st = conn.createStatement())
        {

            // Check status
            st.execute("PRAGMA foreign_keys");
            int val;

            try ( ResultSet rs = st.getResultSet())
            {
                val = rs.getInt(1);
            }

            switch (val)
            {
                case 1:
                    System.out.println("'foreign_keys' constraint is set.");
                    return ON;
                case 0:
                    System.out.println("'foreign_keys' constraint is unset.");
                    return OFF;
                default:
                    throw new SQLException("'foreign_keys' constraint not being set.");
            }
        }
    }

    /**
     * Set <b>Foreign Keys</b> constraint to <u>ON</u> or <u>OFF</u>.
     *
     * @param setting Refer to {@link Status}.
     *
     * @throws SQLException SQL error, or "'foreign_keys' constraint not being
     *                      set."
     */
    public final void setForeignKeysConstraint(Status setting) throws SQLException {
        try ( Statement st = conn.createStatement())
        {
            switch (setting)
            {
                case ON:
                    // Activate foreign keys constraint checking
                    st.execute("PRAGMA foreign_keys = on");
                    break;

                case OFF:
                    // De-activate foreign keys constraint checking
                    st.execute("PRAGMA foreign_keys = off");
            }

            // Check status
            st.execute("PRAGMA foreign_keys");
            int val;

            try ( ResultSet rs = st.getResultSet())
            {
                val = rs.getInt(1);
            }

            if (val == 1 && setting == ON)
            {
                System.out.println("'foreign_keys' constraint is set.");
            } else if (val == 0 && setting == OFF)
            {
                System.out.println("'foreign_keys' constraint is unset.");
            } else
            {
                throw new SQLException("'foreign_keys' constraint not being set.");

            }
        }
    }

    /**
     * Used by the class constructor to specify the type of process to establish
     * a connection to a database.
     */
    public static enum DbOpen {
        /**
         * Create a new empty database file if one does not already exist.
         * Throws an IOException if one does.
         */
        CREATE,
        /**
         * Open an existing database file. Throws an IOException if it does not
         * exist.
         */
        OPEN,
        /**
         * Replace an existing database file. This will delete the file and
         * create a new empty one. Throws an IOException if it does not exist.
         */
        REPLACE
    }

    /**
     * Used as alternative to less informative boolean settings.<br>
     * Use: <i><b>ON</b></i> or <i><b>OFF</b></i> as a parameter setting to
     * {@link setForeignKeysConstraint(Status) setForeignKeysConstraint(setting)}.
     */
    public static enum Status {
        /**
         * Set Foreign Keys Constraint = on, or is current state.
         */
        ON,
        /**
         * Set Foreign Keys Constraint = off, or is current state.
         */
        OFF
    }
}
