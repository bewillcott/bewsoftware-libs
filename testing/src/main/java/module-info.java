/*
 *  File Name:    module-info.java
 *  Project Name: bewsoftware-testing
 * 
 *  Copyright (c) 2025 Bradley Willcott
 * 
 *  bewsoftware-testing is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  bewsoftware-testing is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/**
 * BEWSoftwareTestingLibrary module description.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.1.0
 * @version 3.1.0
 */
module bewsoftware.testing {
    requires org.junit.jupiter.api;

    exports com.bewsoftware.testing;
}
