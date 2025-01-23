/*
 *  File Name:    SerializableList.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2025 Bradley Willcott
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

package com.bewsoftware.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SerializableList interface description.<br>
 * This interface is to be used when needing to Serialize a List like {@link ArrayList}.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 * @param <E> the type of elements in this list.
 *
 * @since 3.0.2
 * @version 3.0.2
 */
public interface SerializableList<E> extends List<E>, Serializable, Cloneable
{

}
