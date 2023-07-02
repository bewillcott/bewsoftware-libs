/*
 *  File Name:    IHolder.java
 *  Project Name: bewsoftware-jfx
 *
 *  Copyright (c) 2023 Bradley Willcott
 *
 *  bewsoftware-jfx is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  bewsoftware-jfx is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bewsoftware.jfx.util;

/**
 * The IHolder interface provides common methods for all holder classes.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.0
 * @version 3.0.0
 */
public interface IHolder
{
    /**
     * First step in the process.
     *<ul>
     * <li>performs any initialization.</li>
     * <li>sets the control in edit mode.</li>
     * <li>takes a snapshot.</li>
     * </ul>
     */
    void begin();

    /**
     * Check whether or not the control's value is valid, and set the
     * appropriate border and background styles.
     */
    void checkIsValid();

    /**
     * To end the process:
     * <ul>
     * <li>clears out the snapshot.</li>
     * <li>performs any finalization.</li>
     * <li>sets the control to non-editable mode.</li>
     * </ul>
     */
    void finish();

    /**
     * Determine if the process is active.
     * <p>
     * That is, a snapshot has been taken.
     *
     * @return current state.
     */
    boolean isActive();

    /**
     * Determine if the text in the control, has been changed from that which is
     * held in the snapshot.
     *
     * @return current state.
     */
    boolean isDirty();

    /**
     * Determine if the text in the control is valid.
     *
     * @return current state.
     */
    boolean isValid();

    /**
     * Reset the text in the control, to that which is held in the snapshot.
     */
    void reset();

    /**
     * Reset the field's style in the control, to its default style.
     */
    void resetStyle();

    /**
     * Take a snapshot of the text in the control, and store it for later use,
     * and set the control to be editable.
     */
    void snapshot();
}
