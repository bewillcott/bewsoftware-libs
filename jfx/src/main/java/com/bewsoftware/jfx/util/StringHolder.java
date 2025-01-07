/*
 *  File Name:    StringHolder.java
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
import com.bewsoftware.utils.string.MessageBuilder;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import static com.bewsoftware.utils.io.DisplayDebugLevel.DEBUG;
import static com.bewsoftware.utils.io.DisplayDebugLevel.INFO;
import static com.bewsoftware.utils.io.DisplayDebugLevel.TRACE;

/**
 * The StringHolder class holds a snapshot of the value of a {@link TextField}
 * or {@link TextArea} whilst it is being edited.
 * <p>
 * To use this class, there is a procedure you need to follow:
 * <ol>
 * <li>Instantiate a StringHolder object.</li>
 * <li>{@link #begin() begin} the process.</li>
 * <li>Whilst you are editing, you can:
 * <ul>
 * <li>take update {@link #snapshot() snapshot}s, or</li>
 * <li>check to see if the process {@link #isActive() isActive}, or</li>
 * <li>check to see if the text has been changed ({@link #isDirty() isDirty}),
 * or</li>
 * <li>check to see if the text {@link #isValid() isValid}, or</li>
 * <li>{@link #reset() reset} the text in the control to the version held in the
 * snapshot.</li>
 * </ul>
 * </li>
 * <li>When you are finished editing and no longer need the current snapshot,
 * then you can {@link #finish() finish} up, which will clear out the
 * snapshot.</li>
 * </ol>
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.0
 * @version 3.0.0
 */
public final class StringHolder implements IHolder
{
    private static final Display DISPLAY = ConsoleIO.consoleDisplay("StringHolder: ");

    private static final String INVALID_FIELD_STYLE = "-fx-border-color: red";

    /**
     * Name of the TextField.
     */
    public final String name;

    private final TextInputControl control;

    private final ChangeListener<Boolean> focusListener;

    /**
     * Holds a copy of the original text in the TextField, if any.
     */
    private String snapshot = null;

    private final Function<String, Boolean> validator;

    static
    {
        DISPLAY.debugLevel(JfxGlobals.getDebugLevel());
//        DISPLAY.debugLevel(TRACE);
    }

    /**
     * Instantiate a StringHolder object.
     *
     * @param name         This is for reference purposes.
     * @param control      to link to.
     * @param validator    is a function that takes a String parameter and
     *                     checks its validity, returning either {@code true} or
     *                     {@code false} as appropriate.
     * @param focusChanged is a method that takes two parameters, a String and a
     *                     Boolean, then acts on them. This method will be used
     *                     this control's focusedProperty.changeEvent.
     */
    public StringHolder(
            final String name,
            final TextInputControl control,
            final Function<String, Boolean> validator,
            final BiConsumer<StringHolder, Boolean> focusChanged
    )
    {
        DISPLAY.println(INFO,"StringHolder(name, control, validator, focusChanged)");
        DISPLAY.println(TRACE,
                "name:\n"
                + "  %s\n"
                + "control:\n"
                + "  %s\n"
                + "validator:\n"
                + "  %s\n"
                + "focusChanged:\n"
                + "  %s",
                name, control, validator, focusChanged);

        this.name = Objects.requireNonNull(name);
        this.control = Objects.requireNonNull(control);
        this.validator = Objects.requireNonNull(validator);
        Objects.requireNonNull(focusChanged);

        focusListener = (observable, oldValue, newValue) ->
        {
            focusChanged.accept(this, newValue);
        };

        DISPLAY.println(DEBUG,"StringHolder(): done");
    }

    @Override
    public void begin()
    {
        DISPLAY.println(TRACE,"%s->begin()", name);
        control.focusedProperty().addListener(focusListener);
        snapshot();
        control.setEditable(true);
    }

    @Override
    public void checkIsValid()
    {
        DISPLAY.println(TRACE,"%s->checkIsValid()", name);

        if (isValid())
        {
            control.setStyle("");
        } else
        {
            control.setStyle(INVALID_FIELD_STYLE);
        }
    }

    @Override
    public void finish()
    {
        DISPLAY.println(TRACE,"%s->finish()", name);

        snapshot = null;
        control.focusedProperty().removeListener(focusListener);
        control.setEditable(false);
    }

    @Override
    public boolean isActive()
    {
        boolean rtn = snapshot != null;
        DISPLAY.println(TRACE,"%s->isActive(): %b", name, rtn);
        return rtn;
    }

    @Override
    public boolean isDirty()
    {
        boolean rtn = isActive() && !snapshot.equals(control.getText());
        DISPLAY.println(TRACE,"%s->isDirty(): %b", name, rtn);
        return rtn;
    }

    @Override
    public boolean isValid()
    {
        boolean rtn = isActive() && validator.apply(control.getText());
        DISPLAY.println(TRACE,"%s->isValid(): %b", name, rtn);
        return rtn;
    }

    @Override
    public void reset()
    {
        control.setText(snapshot);
    }

    @Override
    public void resetStyle()
    {
        control.setStyle("");
    }

    @Override
    public void snapshot()
    {
        DISPLAY.println(TRACE,"%s->snapshot()", name);
        snapshot = control.getText();
    }

    @Override
    public String toString()
    {
        MessageBuilder mb = new MessageBuilder();
        mb.appendln("StringHolder{");
        mb.appendln("  name: %s", name);
        mb.appendln("  control:\n    |%s|", control.getText());
        mb.appendln("  snapshot:\n    |%s|", snapshot);
        mb.appendln('}');
        return mb.toString();
    }
}
