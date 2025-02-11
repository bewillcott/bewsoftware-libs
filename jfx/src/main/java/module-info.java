/*
 *  File Name:    module-info.java
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

/**
 * BEWSoftwareJFXLibrary module description.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.0
 * @version 3.0.2
 */
module bewsoftware.jfx {
    requires transitive bewsoftware.utils;
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires javafx.graphics;
    requires bewsoftware.annotations;

    exports com.bewsoftware.jfx;
    exports com.bewsoftware.jfx.beans.property;
    exports com.bewsoftware.jfx.util;
}
