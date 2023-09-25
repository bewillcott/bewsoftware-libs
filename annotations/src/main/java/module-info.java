/*
 *  File Name:    module-info.java
 *  Project Name: bewsoftware.annotations
 * 
 *  Copyright (c) 2023 Bradley Willcott
 * 
 *  bewsoftware.annotations is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  bewsoftware.annotations is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/**
 * BEWSoftware Annotations Library was created by copying the annotation code
 * from the project file: jcip-annotations-1.0.jar.
 * <p>
 * The reason for this, is to provide this module-info.java file, which makes
 * it safe to include in other projects intended to be published to the Maven
 * Repository.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.0
 * @version 3.0.0
 */
module bewsoftware.annotations {
    exports com.bewsoftware.annotations;
}
