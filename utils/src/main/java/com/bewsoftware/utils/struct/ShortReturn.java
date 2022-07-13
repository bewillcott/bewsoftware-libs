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
package com.bewsoftware.utils.struct;

/**
 * ShortReturn class provides a way to get a {@code short} value into
 * and out of either a Lambda expression or a method through a parameter.
 *
 * @deprecated Replaced with {@link Ref Ref&lt;T&gt;}.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.5
 * @version 1.0.5
 */
@Deprecated
public class ShortReturn {

    public short val;

    public ShortReturn() {
    }

    public ShortReturn(short val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "" + val;
    }
}
