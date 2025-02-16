/*
 *  File Name:    Observable.java
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

import java.beans.PropertyChangeListener;

/**
 * This class represents an observable object, or "data" in the model-view paradigm.
 * It can be implemented to represent an object that the application wants to have observed.
 * <p>
 * The method of 'observation' is through the registration of one or more
 * {@linkplain PropertyChangeListener} objects. When a bound property changes, all
 * listeners registered for that property, will be notified.
 *
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.1.0
 * @version 3.1.0
 */
public interface Observable
{
    /**
     * Add a PropertyChangeListener for all properties, to the listener list.
     *
     * @param listener The PropertyChangeListener to be added.
     *
     * @since 3.1.0
     */
    public void addPropertyChangeListener(final PropertyChangeListener listener);

    /**
     * Add a PropertyChangeListener for the specific property, to the listener list.
     *
     * @param propertyName The name of the property to listen on.
     * @param listener     The PropertyChangeListener to be added.
     *
     * @since 3.1.0
     */
    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener);

    /**
     * Returns the unique id for this Observable.
     *
     * @return the unique id for this Observable.
     *
     * @since 3.1.0
     */
    public int getId();

    /**
     * Remove a PropertyChangeListener for all properties, from the listener list.
     *
     * @param listener The PropertyChangeListener to be removed.
     *
     * @since 3.1.0
     */
    public void removePropertyChangeListener(final PropertyChangeListener listener);

    /**
     * Remove a PropertyChangeListener for the specific property, from the listener list.
     *
     * @param propertyName The name of the property that was listened on.
     * @param listener     The PropertyChangeListener to be removed.
     *
     * @since 3.1.0
     */
    public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener);

}
