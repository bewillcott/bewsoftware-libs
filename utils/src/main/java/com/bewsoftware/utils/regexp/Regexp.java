/*
 * This file is part of the BEWSoftware Utils Library.
 *
 * Copyright (C) 2020, 2021 Bradley Willcott
 *
 * BEWSoftware Utils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BEWSoftware Utils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.bewsoftware.utils.regexp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bewsoftware.utils.regexp.Patterns.HtmlTagRegex;
import static java.lang.Thread.sleep;
import static java.util.regex.Pattern.MULTILINE;

/**
 * This class holds pre-tested regular expressions.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0.5
 */
public class Regexp {

    /**
     * Used for testing only.
     *
     * @param args Command-line parameters.
     *
     * @throws IOException If any file I/O errors.
     */
    public static void main(String[] args) throws IOException {
        String filename = "/Markdown Documentation - Basics.html";

        System.out.println(HtmlTagRegex);
        Pattern p = HtmlTagRegex.getPattern(MULTILINE);
        URL fileUrl = Regexp.class.getResource(filename);

        if (fileUrl != null)
        {
            String filepath = URLDecoder.decode(fileUrl.getFile(), "UTF-8");

            StringBuilder sb = new StringBuilder();

            try ( BufferedReader in = new BufferedReader(new FileReader(filepath)))
            {

                String line;

                while ((line = in.readLine()) != null)
                {
                    sb.append(line).append("\n");
                }
            }

            Matcher m = p.matcher(sb);
            StringBuilder sb2 = new StringBuilder();

            while (m.find())
            {
                System.out.println(m.group());

                String endTag = m.group("endTag");
                String tagName = m.group("tagName");
                String atts = m.group("atts");
                String att = m.group("att");
                String attName = m.group("attName");
                String attVal1 = m.group("attVal1");
                String attVal2 = m.group("attVal2");
                String completeTag = m.group("completeTag");

                sb2.append("endTag: ").append(endTag).append("\n");
                sb2.append("tagName: ").append(tagName).append("\n");
                sb2.append("atts: |").append(atts).append("|\n");
                sb2.append("att: |").append(att).append("|\n");
                sb2.append("attName: ").append(attName).append("\n");
                sb2.append("attVal1: |").append(attVal1).append("|\n");
                sb2.append("attVal2: |").append(attVal2).append("|\n");
                sb2.append("completeTag: ").append(completeTag).append("\n");
                sb2.append("\n");

                System.err.println(sb2);
                sb2.setLength(0);

                try
                {
                    sleep(1);
                } catch (InterruptedException ex)
                {
                    System.err.println(ex);
                }
            }

        } else
        {
            throw new FileNotFoundException(filename);
        }
    }

    /**
     * This class in not intended to be instantiated.
     */
    private Regexp() {
    }
}
