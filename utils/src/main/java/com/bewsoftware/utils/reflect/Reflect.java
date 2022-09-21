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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.bewsoftware.utils.reflect;

import java.lang.reflect.*;

import static com.bewsoftware.utils.string.Strings.requireNonBlank;
import static java.util.Objects.requireNonNull;

/**
 * The primary purpose of this class, is to assist in Unit Testing classes
 * with hidden members.
 * <p>
 * By using reflection, testing can be done without
 * requiring the changing of the public interface of the class.
 * <br>
 * In addition, reflection allows the testing of the internal code to help
 * maintain required end results.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.5
 * @version 1.0.7
 */
public final class Reflect
{
    /**
     * This class is not meant to be instantiated.
     */
    private Reflect()
    {
    }

    /**
     * Get access to a private attribute/field of a class instance.
     * <p>
     * Example:<br>
     * The follow classes would be in separate files: {@code Person.java} and
     * {@code Main.java}.<br>
     * <br><hr>
     * {@snippet internal:
     * public class Person {
     *     private String name;
     *
     *     private Person() {
     *         name = "not set";
     *     }
     *
     *     public String toString() {
     *         return "Person { name = " + name + " }";
     *     }
     * }
     *
     * public class Main {
     *     public static void main() {
     *         Person person = Reflect.instantiatePrivateClass(Person.class);
     *         Field name = Reflect.getPrivateAttribute(person, "name");
     *         name.set(person, "Fred Smith");
     *         System.out.println("name = " + name.get(person));
     *         System.out.println(person);
     * }
     * }
     * }
     * <hr>
     * {@snippet internal:
     * name = Fred Smith
     * Person { name = Fred Smith }
     * }
     *
     * @param instance The instantiated Class object.
     * @param name     The name of the class attribute to get.
     *
     * @return The Field object representing the required class attribute,
     *         or <i>null</i> if not found.
     *
     * @throws IllegalArgumentException     if {@code name} is <i>blank</i>.
     * @throws NullPointerException         if either {@code instance} or
     *                                      {@code name} are <i>null</i>.
     * @throws ReflectiveOperationException if a reflective operation failed.
     */
    public static Field getPrivateAttribute(Object instance, String name) throws ReflectiveOperationException
    {
        Field field = null;

        // instance and name must NOT be null.
        requireNonNull(instance, "instance");
        requireNonBlank(name, "name");

        try
        {
            field = instance.getClass().getDeclaredField(name);
            requireNonNull(field, "field").setAccessible(true);

        } catch (NullPointerException ex)
        {
            // Do nothing - return null.

        } catch (NoSuchFieldException | SecurityException ex)
        {
            throw new ReflectiveOperationException(ex);
        }

        return field;
    }

    /**
     * Get access to a private method of a class instance.
     * <p>
     * Example:<br>
     * The follow classes would be in separate files: {@code Person.java} and
     * {@code Main.java}.<br>
     * <br><hr>
     * {@snippet internal:
     * public class Person {
     *     private String name;
     *
     *     private Person() {
     *         name = "not set";
     *     }
     *
     *     private String getName() {
     *         return name;
     *     }
     *
     *     private void setName(String name) {
     *         this.name = name;
     *     }
     *
     *     public String toString() {
     *         return "Person { name = " + name + " }";
     *     }
     * }
     *
     * public class Main {
     *     public static void main() {
     *         Person person = Reflect.instantiatePrivateClass(Person.class);
     *         Method setName = Reflect.getPrivateMethod(person, "setName", "text");
     *         Method getName = Reflect.getPrivateMethod(person, "getName");
     *         setName.invoke(person, "Peter Adams");
     *         System.out.println("name = " + getName.invoke(person));
     *         System.out.println(person);
     * }
     * }
     * }
     * <hr>
     * {@snippet internal:
     * name = Peter Adams
     * Person { name = Peter Adams }
     * }
     *
     * @param instance   The instantiated Class object.
     * @param name       The name of the class attribute to get.
     * @param sampleArgs Sample arguments/parameters. Used only to obtain their
     *                   Class&lt;?&gt; types.
     *
     * @return The Method object representing the required class method, or
     * <i>null</i> if not found.
     *
     * @throws IllegalArgumentException     if {@code name} is <i>blank</i>.
     * @throws NullPointerException         if either {@code instance} or
     *                                      {@code name} are <i>null</i>.
     * @throws ReflectiveOperationException if a reflective operation failed.
     */
    public static Method getPrivateMethod(Object instance, String name, Object... sampleArgs) throws ReflectiveOperationException
    {
        Class<?>[] types = null;
        Method method = null;
        int length = sampleArgs.length;

        // instance and name must NOT be null.
        requireNonNull(instance, "instance");
        requireNonBlank(name, "name");

        if (length > 0)
        {
            types = new Class<?>[length];

            for (int i = 0; i < length; i++)
            {
                types[i] = sampleArgs[i].getClass();
            }
        }

        try
        {
            method = instance.getClass().getDeclaredMethod(name, types);
            requireNonNull(method, "method").setAccessible(true);

        } catch (NullPointerException ex)
        {
            // Do nothing - return null.

        } catch (NoSuchMethodException | SecurityException ex)
        {
            throw new ReflectiveOperationException(ex);
        }

        return method;
    }

    /**
     * Instantiate a class with a private parameterized constructor. To access
     * the default constructor, only provide the {@code clazz} parameter.<p>
     * Example:<br>
     * The follow classes would be in separate files: {@code Person.java} and
     * {@code Main.java}.<br>
     * <br>
     * {@snippet internal:
     * public class Person {
     *     private String name;
     *
     *     private Person() {
     *         name = "not set";
     *     }
     *
     *     private Person(String name) {
     *         this.name = name;
     *     }
     *
     *     public String toString() {
     *         return "Person { name = " + name + " }";
     *     }
     * }
     *
     * public class Main {
     *     public static void main() {
     *         Person person = instantiatePrivateClass(Person.class);
     *         System.out.println(person);
     *
     *         person = instantiatePrivateClass(Person.class, "Jane Doe");
     *         System.out.println(person);
     * }
     * }
     * }
     * <hr>
     * {@snippet internal:
     * Person { name = not set }
     * Person { name = Jane Doe }
     * }
     *
     * @param <T>   The type of the reference.
     * @param clazz The class to instantiated.
     * @param args  The parameters to be passed to the constructor.
     *
     *
     * @return The newly instantiated object, or <i>null</i> if the required
     *         constructor is not found.
     *
     * @throws NullPointerException         if {@code clazz} is <i>null</i>.
     * @throws ReflectiveOperationException if a reflective operation failed.
     */
    public static <T> T instantiatePrivateClass(Class<T> clazz, Object... args)
            throws ReflectiveOperationException
    {
        Class<?>[] types = null;
        T rtn = null;
        int numParams = args.length;

        // clazz must NOT be null.
        requireNonNull(clazz, "clazz");

        // Setup the types array.
        if (numParams > 0)
        {
            types = new Class<?>[numParams];

            for (int i = 0; i < numParams; i++)
            {
                types[i] = args[i].getClass();
            }
        }

        try
        {
            // Get the array of Constructors.
            Constructor<?>[] cons = clazz.getDeclaredConstructors();

            // Check through all constructors to find what we need.
            for (Constructor<?> con : cons)
            {
                // Only looking for 'private' constructors with
                // the same number of parameters as 'args'.
                if (Modifier.isPrivate(con.getModifiers()) && con.getParameterCount() == numParams)
                {
                    rtn = checkConstructorSignature(numParams, con, types, rtn, args);
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException
                | InstantiationException | SecurityException | InvocationTargetException ex)
        {
            throw new ReflectiveOperationException(ex);
        }

        return rtn;
    }

    /**
     * Check the signature of the constructor.
     *
     * @param <T>       The type of the reference.
     * @param numParams The number of required parameters.
     * @param con       The constructor to check.
     * @param types     Class types of the required parameters.
     * @param rtn       Provides the return type reference.
     * @param args      List of arguments to pass to the constructor on
     *                  instantiation.
     *
     * @return The new instantiated object, or the value of the parameter:
     *         {@code rtn}.
     *
     * @throws IllegalAccessException    if this {@code Constructor} object
     *                                   is enforcing Java language access
     *                                   control and the underlying
     *                                   constructor is inaccessible.
     * @throws InstantiationException    if the class that declares the
     *                                   underlying constructor represents an
     *                                   abstract class.
     * @throws InvocationTargetException if the underlying constructor
     *                                   throws an exception.
     */
    @SuppressWarnings(
            {
                "unchecked", "AssignmentToMethodParameter"
            })
    private static <T> T checkConstructorSignature(
            int numParams, Constructor<?> con, Class<?>[] types, T rtn, Object[] args
    )
            throws IllegalAccessException, InvocationTargetException, InstantiationException
    {

        int found = 0;

        // Make sure it has the right signature.
        if (numParams > 0)
        {
            Class<?>[] params = con.getParameterTypes();

            // Cycle through the parameters.
            for (int i = 0; i < numParams; i++)
            {
                // If match on class type...
                if (params[i] == types[i]
                        || params[i].isPrimitive()
                        && Primitives.isWrapperFor(params[i].getName(), types[i].getName()))
                {
                    found++;
                }
            }
        }

        if (found == numParams)
        {
            con.setAccessible(true);
            rtn = (T) con.newInstance(args);
        }

        return rtn;
    }

    /**
     * Used for matching primitives and their wrapper classes.
     *
     * @since 1.0.5
     * @version 1.0.6
     */
    private static class Primitives
    {

        /**
         * The list used in the comparison.
         */
        private static final String[][] THE_LIST =
        {
            {
                "boolean", "java.lang.Boolean"
            },
            {
                "byte", "java.lang.Byte"
            },
            {
                "char", "java.lang.Character"
            },
            {
                "double", "java.lang.Double"
            },
            {
                "float", "java.lang.Float"
            },
            {
                "long", "java.lang.Long"
            },
            {
                "int", "java.lang.Integer"
            },
            {
                "short", "java.lang.Short"
            }
        };

        /**
         * Not to be instantiated. Class contains only static methods.
         */
        private Primitives()
        {
        }

        /**
         * Determines whether or not the named class is the wrapper class
         * for the named primitive type.
         *
         * @param primitive Name of primitive.
         * @param className Name of class.
         *
         * @return {@code true} if it is, {@code false} otherwise.
         *
         * @throws NullPointerException     if either {@code primitive} or
         *                                  {@code className} are <i>null</i>.
         * @throws IllegalArgumentException if either {@code primitive} or
         *                                  {@code className} are <i>blank</i>.
         */
        public static boolean isWrapperFor(String primitive, String className)
        {
            requireNonBlank(primitive, "primitive");
            requireNonBlank(className, "className");

            // Linear search for primitive in the list.
            for (String[] entry : THE_LIST)
            {
                // Have we a found the primitive?
                // Is 'className' the wrapper class?
                if (entry[0].equals(primitive) && entry[1].equals(className))
                {
                    return true;
                }
            }

            return false;
        }
    }
}
