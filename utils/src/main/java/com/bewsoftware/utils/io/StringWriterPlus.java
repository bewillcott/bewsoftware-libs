/*
 *  File Name:    StringWriterPlus.java
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

package com.bewsoftware.utils.io;

import java.io.StringWriter;
/**
 * This StringWriterPlus class extends the {@code java.io.StringWriterPlus} by adding
 * a convenience method: {@linkplain #clear()}.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.1
 * @version 3.0.1
 */
public class StringWriterPlus extends StringWriter
{
    public StringWriterPlus()
    {
        super();
    }

    public StringWriterPlus(int initialSize)
    {
        super(initialSize);
    }

    /**
     * Empty out the internal buffer. When this returns, the buffer will be
     * empty.
     */
    public void clear()
    {
        getBuffer().setLength(0);
    }
}
