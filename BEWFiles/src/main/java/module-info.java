/*
 * This file is part of the BEW Files Library (aka: BEWFiles).
 *
 * Copyright (C) 2020 Bradley Willcott
 *
 * BEWFiles is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BEWFiles is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * This library contains classes related to working with files.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
module bewsoftware.files {
    requires transitive org.apache.logging.log4j;
    requires transitive bewsoftware.property;
    requires transitive bewsoftware.common;

    exports com.bewsoftware.fileio;
    exports com.bewsoftware.fileio.ini;
}
