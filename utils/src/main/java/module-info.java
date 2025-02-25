/*
 *  File Name:    package-info.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2020, 2021, 2023 Bradley Willcott
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

/**
 * This library contains classes whose packages are not yet large enough,
 * or important enough, to warrant their own modules.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 3.1.0
 */
module bewsoftware.utils {
    requires transitive java.desktop;
    requires transitive java.sql;
    requires transitive bewsoftware.annotations;
    requires transitive bewsoftware.common;

    exports com.bewsoftware.utils;
    exports com.bewsoftware.utils.concurrent;
    exports com.bewsoftware.utils.crypto;
    exports com.bewsoftware.utils.function;
    exports com.bewsoftware.utils.graphics;
    exports com.bewsoftware.utils.io;
    exports com.bewsoftware.utils.reflect;
    exports com.bewsoftware.utils.regexp;
    exports com.bewsoftware.utils.string;
    exports com.bewsoftware.utils.struct;
    exports com.github.dweymouth;
}
