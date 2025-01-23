/*
 *  File Name:    CallbackUser.java
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

import com.bewsoftware.utils.function.Callback;
/**
 * CallbackUser interface provides declarations for required methods to use the <i>Callback</i> functionality.
 * <p>
 * The
 * {@link #fireCallback(java.lang.String, java.lang.Object, java.lang.Object) fireCallback}
 * method would be of most use to the implementing class. Calling it when a property has changed.
 * <p>
 * The {@link #setCallback(com.bewsoftware.utils.Callback) setCallback} method is of primary use by any
 * aggregating class containing the <i>CallbackUser</i> implementing class.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.2
 * @version 3.0.2
 */
public interface CallbackUser
{
    /**
     * Fire the callback function.
     *
     * @param name     of property that has changed.
     * @param oldValue of property.
     * @param newValue of property.
     */
    void fireCallback(String name, Object oldValue, Object newValue);

    /**
     * Returns {@code true} if the callback method has been set.
     *
     * @return {@code true} if set, {@code false} otherwise.
     */
    boolean callbackIsSet();

    /**
     * Set the callback that will be used when a property has changed.
     *
     * @param callback to set.
     */
    void setCallback(Callback callback);
}
