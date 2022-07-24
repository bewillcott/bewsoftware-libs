/*
 *  File Name:    ResultSetPrinter.java
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

import com.bewsoftware.utils.io.ConsoleIO;
import com.bewsoftware.utils.io.Display;
import com.bewsoftware.utils.string.Strings;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static java.lang.String.valueOf;

/**
 * Provides static methods for generic printing of a ResultSets rows.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 2.1.0
 * @version 2.1.0
 */
public class ResultSetPrinter
{
    private ResultSetPrinter()
    {
    }

    public static void print(final ResultSet resultSet)
    {
        _print(ConsoleIO.consoleDisplay("> "), resultSet);
    }

    public static void print(final Display display, final ResultSet resultSet)
    {
        _print(display, resultSet);
    }

    private static void _print(final Display display, final ResultSet resultSet)
    {
        display.println(resultSet);

        try
        {
            ResultSetMetaData rsmd = resultSet.getMetaData();

            int columnCount = rsmd.getColumnCount();

            if (columnCount > 0)
            {
                String bar = buildHeader(display, rsmd);
                processRows(display, resultSet, rsmd);
                display.println(bar);
            }
        } catch (SQLException ex)
        {
            display.println(ex);
        }
    }

    private static String buildHeader(final Display display, final ResultSetMetaData rsmd)
    {
        StringBuilder sbTop = new StringBuilder("┏");
        StringBuilder sbHeader = new StringBuilder("┃");
        StringBuilder sbSeparator = new StringBuilder("┠");
        StringBuilder sbBottom = new StringBuilder("┗");

        try
        {
            int colcnt = rsmd.getColumnCount();

            for (int i = 1; i <= colcnt; i++)
            {
                int columnWidth = rsmd.getColumnDisplaySize(i);
                String label = rsmd.getColumnLabel(i);
                String title = Strings.leftJustify(label, columnWidth);

                sbTop.append(Strings.fill("━", columnWidth + 2)).append(i < colcnt ? "┯" : "┓");
                sbHeader.append(" ").append(title).append(" ").append(i < colcnt ? "│" : "┃");
                sbSeparator.append(Strings.fill("─", columnWidth + 2)).append(i < colcnt ? "┼" : "┨");
                sbBottom.append(Strings.fill("━", columnWidth + 2)).append(i < colcnt ? "┷" : "┛");
            }
        } catch (SQLException ex)
        {
            display.println(ex);
        }

        display.appendln(sbTop)
                .appendln(sbHeader)
                .appendln(sbSeparator);

        return sbBottom.toString();
    }

    private static void processRow(
            final Display display,
            final ResultSet resultSet,
            final ResultSetMetaData rsmd
    )
    {
        StringBuilder sbRow = new StringBuilder("┃");

        try
        {
            int colcnt = rsmd.getColumnCount();

            for (int i = 1; i <= colcnt; i++)
            {
                int columnWidth = rsmd.getColumnDisplaySize(i);
                Object value = resultSet.getObject(i);

                String title = Strings.leftJustify(valueOf(value), columnWidth);

                sbRow.append(" ").append(title).append(" ").append(i < colcnt ? "│" : "┃");
            }
        } catch (SQLException ex)
        {
            display.println(ex);
        }

        display.appendln(sbRow);
    }

    private static void processRows(
            final Display display,
            final ResultSet resultSet,
            final ResultSetMetaData rsmd
    )
    {
        try
        {
            while (resultSet.next())
            {
                processRow(display, resultSet, rsmd);
            }
        } catch (SQLException ex)
        {
            display.println(ex);
        }

    }
}
