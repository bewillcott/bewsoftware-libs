/*
 *  File Name:    XmlFileFormatException.java
 *  Project Name: bewsoftware-files
 *
 *  Copyright (c) 2025 Bradley Willcott
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

package com.bewsoftware.fileio.xml;

import com.bewsoftware.utils.string.MessageBuilder;
/**
 * When the format of the <u>xml</u> file does not conform to what this
 * class expects, this exception will be thrown.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.1.0
 * @version 3.1.0
 */
public class XmlFileFormatException extends Exception
{
    private static final long serialVersionUID = 8195272949303766800L;

    /**
     * Path to the <u>xml</u> file.
     */
    public final String filepath;

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param filepath Path to the <u>xml</u> file.
     * @param msg      the detail message.
     */
    public XmlFileFormatException(final String filepath, final String msg)
    {
        super(msg);
        this.filepath = filepath;
    }

    @Override
    public String toString()
    {
        StackTraceElement[] stElements = getStackTrace();
        MessageBuilder mb = new MessageBuilder();

        for (StackTraceElement stElement : stElements)
        {
            mb.append("\t").appendln(stElement.toString());
        }

        return XmlFileFormatException.class.getName() + "\n\nMessage:  " + getMessage()
                + "\n\nXml File: " + filepath + "\n\nStack Trace:\n" + mb.toString();
    }
}
