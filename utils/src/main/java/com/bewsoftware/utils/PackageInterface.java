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
package com.bewsoftware.utils;

import java.net.URL;

/**
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public interface PackageInterface {

    /**
     * Return the title of this package.
     *
     * @return the title of the implementation, {@code null} is returned if it is not known.
     */
    String getImplementationTitle();

    /**
     * @return the vendor that implemented this package, {@code null}
     *         is returned if it is not known.
     */
    String getImplementationVendor();

    /**
     * Return the version of this implementation.
     * <p>
     * It consists of any string
     * assigned by the vendor of this implementation and does
     * not have any particular syntax specified or expected by the Java
     * runtime. It may be compared for equality with other
     * package version strings used for this implementation
     * by this vendor for this package.
     *
     * @return the version of the implementation, {@code null} is returned if it is not known.
     */
    String getImplementationVersion();

    /**
     * Return the name of this package.
     *
     * @return The fully-qualified name of this package as defined in section 6.5.3 of
     * <cite>The Java&trade; Language Specification</cite>,
     * for example, {@code java.lang}
     */
    String getName();

    /**
     * Return the title of the specification that this package implements.
     *
     * @return the specification title, {@code null} is returned if it is not known.
     */
    String getSpecificationTitle();

    /**
     * Return the name of the organization, vendor,
     * or company that owns and maintains the specification
     * of the classes that implement this package.
     *
     * @return the specification vendor, {@code null} is returned if it is not known.
     */
    String getSpecificationVendor();

    /**
     * Returns the version number of the specification
     * that this package implements.
     * <p>
     * This version string must be a sequence of non-negative decimal
     * integers separated by "."'s and may have leading zeros.
     * When version strings are compared the most significant
     * numbers are compared.
     * <p>
     * Specification version numbers use a syntax that consists of non-negative
     * decimal integers separated by periods ".", for example "2.0" or
     * "1.2.3.4.5.6.7". This allows an extensible number to be used to represent
     * major, minor, micro, etc. versions. The version specification is described
     * by the following formal grammar:
     * <blockquote>
     * <dl>
     * <dt><i>SpecificationVersion:</i>
     * <dd><i>Digits RefinedVersion<sub>opt</sub></i>
     * <p>
     * <dt><i>RefinedVersion:</i>
     * <dd>{@code .}<i>Digits</i>
     * <dd>{@code .}<i>Digits RefinedVersion</i>
     * <p>
     * <dt><i>Digits:</i>
     * <dd><i>Digit</i>
     * <dd><i>Digits</i>
     * <p>
     * <dt><i>Digit:</i>
     * <dd>any character for which {@link Character#isDigit} returns {@code true},
     * e.g. 0, 1, 2, ...
     * </dl>
     * </blockquote>
     *
     * @return the specification version, {@code null} is returned if it is not known.
     */
    String getSpecificationVersion();

    /**
     * @return the hash code computed from the package name.
     */
    @Override
    int hashCode();

    /**
     * Compare this package's specification version with a
     * desired version. It returns true if
     * this packages specification version number is greater than or equal
     * to the desired version number.
     * <p>
     * Version numbers are compared by sequentially comparing corresponding
     * components of the desired and specification strings.
     * Each component is converted as a decimal integer and the values
     * compared.
     * <p>
     * If the specification value is greater than the desired
     * value true is returned. If the value is less false is returned.
     * If the values are equal the period is skipped and the next pair of
     * components is compared.
     *
     * @param desired the version string of the desired version.
     *
     * @return true if this package's version number is greater
     *         than or equal to the desired version number
     *
     * @exception NumberFormatException if the current version is not known or
     *                                  the desired or current version is not of the correct dotted form.
     */
    boolean isCompatibleWith(String desired) throws NumberFormatException;

    /**
     * @return true if the package is sealed, false otherwise
     */
    boolean isSealed();

    /**
     * @param url the code source URL
     *
     * @return true if this package is sealed with respect to the given {@code url}
     */
    boolean isSealed(URL url);

    /**
     * Returns the string representation of this Package.
     * <p>
     * Its value is the string "package " and the package name.
     * If the package title is defined it is appended.
     * If the package version is defined it is appended.
     *
     * @return the string representation of the package.
     */
    @Override
    String toString();

}
