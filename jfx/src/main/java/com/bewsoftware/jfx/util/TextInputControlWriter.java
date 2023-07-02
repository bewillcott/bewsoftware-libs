/*
 *  File Name:    TextInputControlWriter.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2023 Bradley Willcott
 *
 *  bewsoftware-utils is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  bewsoftware-utils is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bewsoftware.jfx.util;

import com.bewsoftware.utils.io.Clearable;
import com.bewsoftware.utils.io.ConsoleIO;
import com.bewsoftware.utils.io.Display;
import java.io.IOException;
import java.io.Writer;
import javafx.scene.control.TextInputControl;

/**
 * The TextInputControlWriter class is used in conjunction with the
 * {@link Display} interface and {@link ConsoleIO} as an output destination.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.0
 * @version 3.0.0
 */
public class TextInputControlWriter extends Writer implements Clearable
{
    private TextInputControl control;

    private final StringBuilder sb = new StringBuilder();

    /**
     * Primary constructor.
     *
     * @param control to output to.
     */
    public TextInputControlWriter(final TextInputControl control)
    {
        this.control = control;
    }

    @Override
    public void clear() throws IOException
    {
        if (isOpen())
        {
            control.clear();
            sb.setLength(0);
        } else
        {
            throw new IOException("This Writer is closed.");
        }
    }

    @Override
    public void close() throws IOException
    {
        if (isOpen())
        {
            flush();
            control = null;
        } else
        {
            throw new IOException("This Writer is closed.");
        }
    }

    @Override
    public void flush() throws IOException
    {
        if (isOpen())
        {
            control.appendText(sb.toString());
            sb.setLength(0);
        } else
        {
            throw new IOException("This Writer is closed.");
        }
    }

    /**
     * Determine if this Writer is open or not.
     *
     * @return {@code true} if open, {@code false} otherwise.
     */
    public boolean isOpen()
    {
        return control != null;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException
    {
        if (isOpen())
        {
            sb.append(cbuf, off, len);
        } else
        {
            throw new IOException("This Writer is closed.");
        }
    }
}
