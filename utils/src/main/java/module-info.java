/*
 * This file is part of the BEWSoftware Utils Library.
 *
 * Copyright (C) 2020, 2021 Bradley Willcott
 *
 * BEWSoftware Utils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BEWSoftware Utils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * This library contains classes whose packages are not yet large enough,
 * or important enough, to warrant their own modules.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0.5
 */
module bewsoftware.utils {
    requires transitive java.desktop;
    requires transitive java.sql;

    exports com.bewsoftware.utils.crypto;
    exports com.bewsoftware.utils.graphics;
    exports com.bewsoftware.utils.io;
    exports com.bewsoftware.utils.reflect;
    exports com.bewsoftware.utils.regexp;
    exports com.bewsoftware.utils.sqlite;
    exports com.bewsoftware.utils.string;
    exports com.bewsoftware.utils.struct;
}
