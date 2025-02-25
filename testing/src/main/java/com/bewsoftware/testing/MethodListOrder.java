/*
 *  File Name:    MethodListOrder.java
 *  Project Name: bewsoftware-testing
 *
 *  Copyright (c) 2025 Bradley Willcott
 *
 *  JCodes is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  JCodes is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bewsoftware.testing;

import java.util.Comparator;
import org.junit.jupiter.api.MethodDescriptor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.MethodOrdererContext;

/**
 * {@code MethodOrderer} that sorts methods based on the {@code @TestMethod} annotation.
 * <p>
 * {@snippet :
 * import com.bewsoftware.testing.MethodListOrder;
 * import com.bewsoftware.testing.TestMethod;
 * import org.junit.jupiter.api.Test;
 * import org.junit.jupiter.api.TestMethodOrder;
 *
 * @TestMethod("testOne")
 * @TestMethod("testTwo")
 * @TestMethod("testThree")
 * @TestMethod("testFour")
 * @TestMethodOrder(MethodListOrder.class)
 * public class MyTest
 * {
 *     public MyTest(){}
 *
 *     @Test
 *     public void testFour()
 *     {
 *         System.out.println("testFour.");
 *     }
 *
 *     @Test
 *     public void testOne()
 *     {
 *         System.out.println("testFour.");
 *     }
 *
 *     @Test
 *     public void testThree()
 *     {
 *         System.out.println("testFour.");
 *     }
 *
 *     @Test
 *     public void testTwo()
 *     {
 *         System.out.println("testFour.");
 * }
 * }
 * }
 * You might ask, why not just sort the methods manually into the order
 * you desire, then use the supplied class: {@link MethodName}?
 * This would be fine, as long as you don't reformat your source file,
 * ending up with your methods all nicely repositioned in alphanumeric order.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @see MethodList
 * @see TestMethod
 *
 * @since 3.1.0
 * @version 3.1.0
 */
public class MethodListOrder implements MethodOrderer
{
    private static final int DEFAULT = Integer.MAX_VALUE / 2;

    private String[] methodNames;

    public MethodListOrder()
    {
    }

    @Override
    public void orderMethods(final MethodOrdererContext context)
    {
        TestMethod[] methodList = context.getTestClass().getDeclaredAnnotation(MethodList.class).value();

        methodNames = new String[methodList.length];

        for (int i = 0; i < methodList.length; i++)
        {
            methodNames[i] = methodList[i].value();
        }

        context.getMethodDescriptors().sort(Comparator.comparingInt(this::getOrder));
    }

    private int getOrder(final MethodDescriptor descriptor)
    {
        final String methodName = descriptor.getMethod().getName();

        for (int i = 0; i < methodNames.length; i++)
        {
            if (methodNames[i].equals(methodName))
            {
                return i;
            }
        }

        return DEFAULT;
    }
}
