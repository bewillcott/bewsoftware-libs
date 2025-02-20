/*
 *  File Name:    MyTest.java
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

package some.files;

import com.bewsoftware.testing.MethodListOrder;
import com.bewsoftware.testing.TestMethod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * MyTest class description.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.1.0
 * @version 3.1.0
 */
@TestMethod("testOne")
@TestMethod("testTwo")
@TestMethod("testThree")
@TestMethod("testFour")
@TestMethodOrder(MethodListOrder.class)
public class MyTest
{
    public MyTest()
    {

    }

    @Test
    public void testFour()
    {
        System.out.println("testFour.");
    }

    @Test
    public void testOne()
    {
        System.out.println("testOne.");
    }

    @Test
    public void testThree()
    {
        System.out.println("testThree.");
    }

    @Test
    public void testTwo()
    {
        System.out.println("testTwo.");
    }
}
