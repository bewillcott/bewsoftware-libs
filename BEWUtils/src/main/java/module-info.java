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

/**
 * BEWUtils is a library that classes whose packages are not yet large enough,
 * or important enough, to warrant their own modules.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
module BEWUtils {
    requires transitive java.desktop;
    requires transitive org.apache.logging.log4j;
    requires transitive java.sql;

    exports com.bewsoftware.utils.graphics;
    exports com.bewsoftware.utils.reflect;
    exports com.bewsoftware.utils.regexp;
    exports com.bewsoftware.utils.sqlite;
    exports com.bewsoftware.utils.string;
}
