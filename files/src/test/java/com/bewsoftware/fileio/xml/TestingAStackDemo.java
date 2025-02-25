/*
 *  File Name:    TestingAStackDemo.java
 *  Project Name: bewsoftware-files
 *
 *  Copyright (c) 2025 Bradley Willcott
 *
 *  bewsoftware-files is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  bewsoftware-files is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bewsoftware.fileio.xml;

import java.util.EmptyStackException;
import java.util.Stack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.bewsoftware.utils.string.Strings.indentLine;
import static com.bewsoftware.utils.string.Strings.println;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("A stack")
@SuppressWarnings("StaticNonFinalUsedInInitialization")
class TestingAStackDemo
{

    @SuppressWarnings("PackageVisibleField")
    Stack<Object> stack;

    @SuppressWarnings("PackageVisibleField")
    static int indent = 0;

    static
    {
        println("A stack");
        indent += 4;
    }

    @Test
    @DisplayName("is instantiated with new Stack()")
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    void isInstantiatedWithNew()
    {
        new Stack<>();
    }

    @Nested
    @DisplayName("when new")
    @SuppressWarnings(
            {
                "PackageVisibleInnerClass"
            })
    class WhenNew
    {
        static
        {
            println(indentLine("when new", indent));
            indent += 4;
        }

        @BeforeEach
        void createNewStack()
        {
            stack = new Stack<>();
        }

        @Test
        @DisplayName("is empty")
        void isEmpty()
        {
            assertTrue(stack.isEmpty());
            println(indentLine("is empty", indent));
        }

        @Test
        @DisplayName("throws EmptyStackException when peeked")
        @SuppressWarnings("ThrowableResultIgnored")
        void throwsExceptionWhenPeeked()
        {
            assertThrows(EmptyStackException.class, stack::peek);
            println(indentLine("throws EmptyStackException when peeked", indent));
        }

        @Test
        @DisplayName("throws EmptyStackException when popped")
        @SuppressWarnings("ThrowableResultIgnored")
        void throwsExceptionWhenPopped()
        {
            assertThrows(EmptyStackException.class, stack::pop);
            println(indentLine("throws EmptyStackException when popped", indent));
        }

        @Nested
        @DisplayName("after pushing an element")
        class AfterPushing
        {

            static
            {
                println(indentLine("after pushing an element", indent));
                indent += 4;

            }

            @SuppressWarnings("PackageVisibleField")
            String anElement = "an element";

            @BeforeEach
            void pushAnElement()
            {
                stack.push(anElement);
            }

            @Test
            @DisplayName("it is no longer empty")
            void isNotEmpty()
            {
                assertFalse(stack.isEmpty());
                println(indentLine("it is no longer empty", indent));
            }

            @Test
            @DisplayName("returns the element when popped and is empty")
            void returnElementWhenPopped()
            {
                assertEquals(anElement, stack.pop());
                assertTrue(stack.isEmpty());
                println(indentLine("returns the element when popped and is empty", indent));
            }

            @Test
            @DisplayName("returns the element when peeked but remains not empty")
            void returnElementWhenPeeked()
            {
                assertEquals(anElement, stack.peek());
                assertFalse(stack.isEmpty());
                println(indentLine("returns the element when peeked but remains not empty", indent));
            }
        }
    }
}
