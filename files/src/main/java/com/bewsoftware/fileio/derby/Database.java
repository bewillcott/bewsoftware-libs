/*
 *  File Name:    Database.java
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
package com.bewsoftware.fileio.derby;

import com.bewsoftware.common.InvalidParameterException;
import com.bewsoftware.fileio.BEWFiles;
import com.bewsoftware.utils.io.ConsoleIO;
import com.bewsoftware.utils.io.Display;
import com.bewsoftware.utils.io.DisplayDebugLevel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

import static com.bewsoftware.fileio.derby.Database.DbOpenResult.EXISTING;
import static com.bewsoftware.fileio.derby.Database.DbOpenResult.FAILED;
import static com.bewsoftware.fileio.derby.Database.DbOpenResult.NEW;
import static com.bewsoftware.fileio.derby.Database.Status.OFF;
import static com.bewsoftware.fileio.derby.Database.Status.ON;
import static com.bewsoftware.utils.io.DisplayDebugLevel.DEBUG;
import static com.bewsoftware.utils.io.DisplayDebugLevel.INFO;
import static java.nio.file.Path.of;

/**
 * This is intended to provide methods and members related to database
 * operations.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 3.0.0
 */
public class Database implements AutoCloseable
{
    private static final Display DISPLAY = ConsoleIO.consoleDisplay("");

    private static final String PROTOCOL = "derby:";

    private Connection conn = null;

    private final Path dbPath;

    private final ArrayList<Statement> stmts = new ArrayList<>();

    /**
     * Instantiates a Database object for the database: dbFilename.
     * <p>
     * <b>Note:</b><br>
     * No check is done, at this time, for the existence of the file.
     *
     * @param dbName name and location of database file.
     *
     * @throws InvalidParameterException if 'dbFilename' is either
     *                                   null or blank.
     */
    public Database(final String dbName) throws InvalidParameterException
    {
        if (dbName == null)
        {
            throw new InvalidParameterException("dbName: is null");
        } else if (dbName.isBlank())
        {
            throw new InvalidParameterException("dbName: is blank");
        }

        this.dbPath = of(dbName);
    }

    /**
     * @throws java.sql.SQLException
     */
    @Override
    public void close() throws SQLException
    {
        if (conn != null)
        {
            for (Statement stmt : stmts)
            {
                stmt.close();
            }

            stmts.clear();

            conn.close();
            conn = null;

            shutdown();
        }
    }

    /**
     * Commit any outstanding transactions, and/or, release any database
     * locks.
     * <p>
     * Required for QUERIES also to release locks held.
     *
     * @return 'true' if successful, 'false' otherwise.
     */
    public boolean commit()
    {
        boolean rtn = false;

        try
        {
            conn.commit();
            rtn = true;
        } catch (SQLException ignore)
        {
            // Ignore
        }

        return rtn;
    }

    /**
     * Executes the given SQL statement, which may return multiple results.
     * <p>
     * The primary purpose of this method is to process a batch of SQL
     * statements for which there will be no returned data. Such as
     * CREATE/INSERT statements.
     *
     * @param sql an SQL statement to execute.
     *
     * @return <b>True</b> if the statement is successfully executed.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean execute(final String sql)
    {
        boolean rtn;

        try (Statement st = conn.createStatement())
        {
            DISPLAY.println(INFO, "sql: " + sql);
            st.execute(sql);
            rtn = true;
        } catch (SQLException ex)
        {
            ex.printStackTrace();
            rtn = false;
        }

        return rtn;
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
    public ResultSet executeQuery(final String sql) throws SQLException
    {
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
     * Execute an array of SQL statements.
     * <p>
     * The primary purpose of this method is to process a batch of SQL
     * statements for which there will be no returned data. Such as
     * CREATE/INSERT statements.
     *
     * @param arraySQL An array of SQL statements.
     *
     * @return <b>True</b> if ALL statements are successfully executed.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean executeUpdate(final String[] arraySQL)
    {
        boolean rtn = false;

        try
        {
            Savepoint svpnt = conn.setSavepoint();

            try (Statement st = conn.createStatement())
            {
                for (String sql : arraySQL)
                {
                    DISPLAY.println(INFO, "sql: " + sql);
                    st.executeUpdate(sql);
                }

                conn.commit();
                rtn = true;
            } catch (SQLException ex)
            {
                ex.printStackTrace();

                try
                {
                    conn.rollback();
                } catch (SQLException ignore)
                {
                    // Ignore
                }
            }

            conn.releaseSavepoint(svpnt);
        } catch (SQLException ignore)
        {
            // Ignore
        }

        return rtn;
    }

    /**
     * Status of the Foreign Keys Constraint setting.
     *
     * @return Current status.
     *
     * @throws SQLException SQL Error, or 'foreign_keys' constraint not being
     *                      set.
     */
    public final Status getForeignKeysConstraint() throws SQLException
    {
        try (Statement st = conn.createStatement())
        {

            // Check status
            st.execute("PRAGMA foreign_keys");
            int val;

            try (ResultSet rs = st.getResultSet())
            {
                val = rs.getInt(1);
            }

            switch (val)
            {
                case 1 ->
                {
                    DISPLAY.println(DEBUG, "'foreign_keys' constraint is set.");
                    return ON;
                }

                case 0 ->
                {
                    DISPLAY.println(DEBUG, "'foreign_keys' constraint is unset.");
                    return OFF;
                }

                default ->
                    throw new SQLException("'foreign_keys' constraint not being set.");
            }
        }
    }

    /**
     * Create/Open/Replace a JavaDB database file.<p>
     * Foreign Keys Constraint will be set to ON, by default.
     *
     * @param dbOpen Type of connection process.
     *
     * @return
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public DbOpenResult open(final DbOpen dbOpen)
    {
        DbOpenResult rtn = FAILED;
        boolean fileExists;
        Properties props = new Properties();

        File file = dbPath.toFile();
        fileExists = file.exists();

        switch (dbOpen)
        {
            case CREATE ->
            {
                if (!fileExists)
                {
                    props.setProperty("create", "true");
                    rtn = NEW;
                }
            }

            case OPEN ->
            {
                if (fileExists)
                {
                    rtn = EXISTING;
                }
            }

            case REPLACE ->
            {
                try
                {
                    if (fileExists)
                    {
                        BEWFiles.deleteDirTree(dbPath);
                        props.setProperty("create", "true");
                        rtn = NEW;
                    }
                } catch (IOException ignore)
                {
                    // Ignore exception
                }
            }
        }

        if (rtn != FAILED)
        {
            try
            {
                if (props.isEmpty())
                {
                    conn = DriverManager.getConnection("jdbc:" + PROTOCOL + dbPath);

                } else
                {
                    conn = DriverManager.getConnection("jdbc:" + PROTOCOL + dbPath, props);
                }

                conn.setAutoCommit(false);
            } catch (SQLException ex)
            {
                ex.printStackTrace();
                rtn = FAILED;
            }
        }

        return rtn;
    }

    /**
     * Set the debug level for the displaying of information.
     * <p>
     * The default level is: 0.
     *
     * @param level new level to set
     */
    public void setDebugLevel(final DisplayDebugLevel level)
    {
        DISPLAY.debugLevel(level);
    }

    /**
     * Set <b>Foreign Keys</b> constraint to <u>ON</u> or <u>OFF</u>.
     *
     * @param setting Refer to {@link Status}.
     *
     * @throws SQLException SQL error, or "'foreign_keys' constraint not being
     *                      set."
     */
    public final void setForeignKeysConstraint(final Status setting) throws SQLException
    {
        try (Statement st = conn.createStatement())
        {
            switch (setting)
            {
                case ON -> // Activate foreign keys constraint checking
                    st.execute("PRAGMA foreign_keys = on");

                case OFF -> // De-activate foreign keys constraint checking
                    st.execute("PRAGMA foreign_keys = off");
            }

            // Check status
            st.execute("PRAGMA foreign_keys");
            int val;

            try (ResultSet rs = st.getResultSet())
            {
                val = rs.getInt(1);
            }

            if (val == 1 && setting == ON)
            {
                DISPLAY.println(DEBUG, "'foreign_keys' constraint is set.");
            } else if (val == 0 && setting == OFF)
            {
                DISPLAY.println(DEBUG, "'foreign_keys' constraint is unset.");
            } else
            {
                throw new SQLException("'foreign_keys' constraint not being set.");
            }
        }
    }

    @Override
    public String toString()
    {
        return "Database{" + "conn=" + conn + ", dbPath=" + dbPath + '}';
    }

    private void shutdown()
    {
        boolean gotSQLExc = false;

        try
        {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException se)
        {
            if (se.getSQLState().equals("XJ015"))
            {
                gotSQLExc = true;
            }
        }

        if (!gotSQLExc)
        {
            DISPLAY.println("Database did not shut down normally");
        } else
        {
            DISPLAY.println("Database shut down normally");
        }
    }

    /**
     * Used by the {@link #open(DbOpen)} method to specify the type of
     * process to use to establish a connection to a database.
     */
    @SuppressWarnings("PublicInnerClass")
    public static enum DbOpen
    {
        /**
         * Create a new empty database file.
         * <p>
         * If one already exists, then it will OPEN it.
         */
        CREATE,
        /**
         * Open an existing database file.
         * <p>
         * If it does not exist, then this will fail.
         */
        OPEN,
        /**
         * Replace an existing database file.
         * <p>
         * This will delete the file, if it exists, and CREATE a new empty one.
         */
        REPLACE
    }

    /**
     * Used as alternative to less informative boolean settings.<br>
     * Use: <i><b>ON</b></i> or <i><b>OFF</b></i> as a parameter setting to
     * {@link setForeignKeysConstraint(Status) setForeignKeysConstraint(setting)}.
     */
    @SuppressWarnings("PublicInnerClass")
    public static enum Status
    {
        /**
         * Set Foreign Keys Constraint = on, or is current state.
         */
        ON,
        /**
         * Set Foreign Keys Constraint = off, or is current state.
         */
        OFF
    }

    /**
     * Result of attempt to Open/Create a database.
     */
    @SuppressWarnings("PublicInnerClass")
    public static enum DbOpenResult
    {
        /**
         * Database already existed, and is now open.
         */
        EXISTING,
        /**
         * A new database has been created and is open.
         */
        NEW,
        /**
         * The process failed to open the database.
         */
        FAILED
    }
}
