/*
 *  File Name:    DatabaseTest.java
 *  Project Name: bewsoftware-files
 *
 *  Copyright (c) 2022 Bradley Willcott
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

package com.bewsoftware.fileio.derby;

import com.bewsoftware.common.InvalidParameterException;
import com.bewsoftware.fileio.derby.Database.DbOpenResult;
import com.bewsoftware.utils.io.ConsoleIO;
import com.bewsoftware.utils.io.Display;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.bewsoftware.fileio.derby.Database.DbOpen.CREATE;
import static com.bewsoftware.fileio.derby.Database.DbOpen.OPEN;
import static com.bewsoftware.fileio.derby.Database.DbOpenResult.EXISTING;
import static com.bewsoftware.fileio.derby.Database.DbOpenResult.NEW;
import static com.bewsoftware.utils.io.DisplayDebugLevel.TRACE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test the Database class.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 */
public class DatabaseTest
{

    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    private static final String[] CREATE_SQL =
    {
        "CREATE TABLE USERS (\n"
        + "FIRST_NAME VARCHAR(30) NOT NULL,\n"
        + "LAST_NAME VARCHAR(30) NOT NULL,\n"
        + "EMP_NO INTEGER NOT NULL CONSTRAINT EMP_NO_PK PRIMARY KEY\n"
        + ")",
        "CREATE TABLE PC (\n"
        + "TYPE VARCHAR(10) NOT NULL,\n"
        + "SERIAL VARCHAR(50),\n"
        + "OS VARCHAR(20),\n"
        + "EMP_NO INTEGER,\n"
        + "CODE_NO INTEGER NOT NULL CONSTRAINT CODE_NO_PK PRIMARY KEY\n"
        + ")",
        "INSERT INTO USERS VALUES('Bill','Gates',1)",
        "INSERT INTO USERS VALUES('Joe','Bloggs',2)",
        "INSERT INTO USERS VALUES('Peter','Kropotkin',3)",
        "INSERT INTO PC VALUES('Desktop','01010','Linux',1,1)",
        "INSERT INTO PC VALUES('Laptop','101010','BSD',2,2)",
        "INSERT INTO PC VALUES('Desktop','101010','XP',3,12)"
    };

    private final static String DBNAME = "testdb";

    private static final Display DISPLAY = ConsoleIO.consoleDisplay("");

    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    private static final String[] QUERY_SQL =
    {
        "SELECT * FROM users",
        "SELECT * FROM pc"
    };

    private static Database db;

    public DatabaseTest()
    {
    }

    @BeforeAll
    public static void setUpClass() throws SQLException, InvalidParameterException
    {
        DISPLAY.println("setUp()");

        db = new Database(DBNAME);
        db.setDebugLevel(TRACE);

        DISPLAY.println("Init db: " + db);
    }

    @AfterAll
    public static void tearDownClass() throws SQLException
    {
        DISPLAY.println("tearDown()");

        if (db != null)
        {
            db.close();
            db = null;
        }
    }

    @Test
    public void testOpen()
    {
        DISPLAY.println("open - OPEN");
        DbOpenResult result = db.open(OPEN);
        DISPLAY.println("OPEN db: " + db);

        if (result != EXISTING)
        {
            DISPLAY.println("open - CREATE");
            result = db.open(CREATE);
            DISPLAY.println("CREATE db: " + db);
            assertEquals(NEW, result);
        }

        if (result == NEW && !db.executeUpdate(CREATE_SQL))
        {
            fail("Statement execution failed: CREATE_SQL!");
        }

        if (executeQueries())
        {
            db.commit();
        } else
        {
            fail("Statement execution failed: QUERY_SQL!");
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private boolean executeQueries()
    {
        boolean rtn = true;
        DISPLAY.println();

        for (String sql : QUERY_SQL)
        {
            DISPLAY.println(sql);

            try (ResultSet result = db.executeQuery(sql))
            {
                ResultSetPrinter.print(result);
                DISPLAY.println();
            } catch (SQLException ex)
            {
                ex.printStackTrace();
                rtn = false;
                break;
            }
        }

        return rtn;
    }
}
