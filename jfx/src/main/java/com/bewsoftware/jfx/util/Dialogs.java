/*
 *  File Name:    Dialogs.java
 *  Project Name: bewsoftware-jfx
 *
 *  Copyright (c) 2023 Bradley Willcott
 *
 *  bewsoftware-jfx is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  bewsoftware-jfx is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bewsoftware.jfx.util;

import com.bewsoftware.jfx.JfxGlobals;
import com.bewsoftware.utils.io.ConsoleIO;
import com.bewsoftware.utils.io.Display;
import javafx.scene.control.*;

import static com.bewsoftware.utils.io.DisplayDebugLevel.DEBUG;
import static com.bewsoftware.utils.io.DisplayDebugLevel.INFO;
import static com.bewsoftware.utils.io.DisplayDebugLevel.TRACE;
import static com.bewsoftware.utils.string.Strings.indentLines;
import static java.lang.String.format;

/**
 * This class is a utility class providing helper methods for the JavaFx
 * {@link Dialog} class and its sub-classes.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.0
 * @version 3.0.0
 */
public class Dialogs
{
    private static final Display DISPLAY = ConsoleIO.consoleDisplay("Dialogs: ");

    static
    {
        DISPLAY.debugLevel(JfxGlobals.getDebugLevel());
//        DISPLAY.debugLevel(TRACE);
    }

    /**
     * Not meant to be instantiated.
     */
    private Dialogs()
    {
    }

    public static Alert setDefaultButton(final Alert alert, final ButtonType defBtn)
    {
        DISPLAY.println(INFO, "setDefaultButton(alert, defBtn)");
        DISPLAY.println(TRACE, () -> format(
                "  alert:\n%s\n"
                + "  defBtn:\n%s",
                indentLines(alert, 4),
                indentLines(defBtn, 4)
        ));

        DialogPane pane = alert.getDialogPane();

        for (ButtonType t : alert.getButtonTypes())
        {
            ((Button) pane.lookupButton(t)).setDefaultButton(t == defBtn);
        }

        DISPLAY.println(DEBUG, "setDefaultButton(...): done");
        return alert;
    }
}
