/*
 * This file is part of the BEW Utils Library (aka: BEWUtils).
 *
 * Copyright (C) 2020 Bradley Willcott
 *
 * BEWUtils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BEWUtils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.bewsoftware.utils.struct;

/**
 * FloatReturn class provides a way to get a {code float} value into
 * and out of either a Lambda expression or a method through a parameter.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.5
 * @version 1.0.5
 */
public class FloatReturn {

    public float val;

    public FloatReturn() {
    }

    public FloatReturn(float val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "" + val;
    }
}
