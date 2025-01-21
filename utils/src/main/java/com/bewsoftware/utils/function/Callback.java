/*
 *  File Name:    Callback.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2025 Bradley Willcott
 *
 *  JCodes is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  JCodes is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bewsoftware.utils.function;

/**
 * Callback class description.<br>
 * This functional interface is intended to be used to provide a simple
 * alternative to {@link java.beans.PropertyChangeSupport PropertyChangeSupport},
 * when you only need an aggregate class to link to it.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.2
 * @version 3.0.2
 */
@FunctionalInterface
public interface Callback extends TriConsumer<String, Object, Object>
{
    @Override
    void accept(String name, Object oldValue, Object newValue);
}
