/*
 *  File Name:    OptionalMap.java
 *  Project Name: bewsoftware-optional
 *
 *  Copyright (c) 2021 Bradley Willcott
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.bewsoftware.optional.java.util;

import java.util.Map;
import java.util.Optional;

/**
 * Class contains static methods to return an {@linkplain Optional} result.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.1.0
 * @version 1.1.0
 */
public class OptionalMap
{

    private OptionalMap()
    {
    }

    public static <K, V> Optional<V> get(Map<K, V> map, K key)
    {
        return Optional.ofNullable(map.get(key));
    }
}
