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
package com.bewsoftware.utils;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

/**
 * This class stores a copy an initial Date instance. This Date once created, is
 * never modified.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public final class ImmutableDate implements Serializable, Cloneable, Comparable<ImmutableDate>
{

    /**
     * @serial serial
     */
    private static final long serialVersionUID = -5343636338811996888L;

    /**
     * The internal date field;
     */
    private Date date;

    /**
     * Allocates a {@code Date} object and initializes it so that
     * it represents the time at which it was allocated, measured to the
     * nearest millisecond.
     *
     * @see java.lang.System#currentTimeMillis()
     */
    public ImmutableDate()
    {
        this.date = new Date();
    }

    /**
     * Creates a new {@code ImmutableDate} object, and stores a copy of the
     * Date passed in.
     *
     * @param date Date object to copy from.
     */
    public ImmutableDate(Date date)
    {
        this.date = new Date(date.getTime());
    }

    /**
     * Allocates a {@code Date} object and initializes it to
     * represent the specified number of milliseconds since the
     * standard base time known as "the epoch", namely January 1,
     * 1970, 00:00:00 GMT.
     *
     * @param date the milliseconds since January 1, 1970, 00:00:00 GMT.
     *
     * @see java.lang.System#currentTimeMillis()
     */
    public ImmutableDate(long date)
    {
        this.date = new Date(date);
    }

    /**
     * Obtains an instance of {@code Date} from an {@code Instant} object.
     * <p>
     * {@code Instant} uses a precision of nanoseconds, whereas {@code Date}
     * uses a precision of milliseconds. The conversion will truncate any
     * excess precision information as though the amount in nanoseconds was
     * subject to integer division by one million.
     * <p>
     * {@code Instant} can store points on the time-line further in the future
     * and further in the past than {@code Date}. In this scenario, this method
     * will throw an exception.
     *
     * @param instant the instant to convert
     *
     * @return a {@code Date} representing the same point on the time-line as
     *         the provided instant
     *
     * @exception NullPointerException     if {@code instant} is null.
     * @exception IllegalArgumentException if the instant is too large to
     *                                     represent as a {@code Date}
     * @since 1.8
     */
    public static Date from(Instant instant)
    {
        try
        {
            return new Date(instant.toEpochMilli());
        } catch (ArithmeticException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Tests if this date is after the specified date.
     *
     * @param when a date.
     *
     * @return {@code true} if and only if the instant represented
     *         by this {@code Date} object is strictly later than the
     *         instant represented by {@code when};
     *         {@code false} otherwise.
     *
     * @exception NullPointerException if {@code when} is null.
     */
    public boolean after(Date when)
    {
        return date.after(when);
    }

    /**
     * Tests if this date is before the specified date.
     *
     * @param when a date.
     *
     * @return {@code true} if and only if the instant of time
     *         represented by this {@code Date} object is strictly
     *         earlier than the instant represented by {@code when};
     *         {@code false} otherwise.
     *
     * @exception NullPointerException if {@code when} is null.
     */
    public boolean before(Date when)
    {
        return date.before(when);
    }

    /**
     * @return a copy of this object.
     */
    @Override
    @SuppressWarnings(
            {
                "CloneDeclaresCloneNotSupported",
                "AccessingNonPublicFieldOfAnotherObject"
            })
    public Object clone()
    {
        ImmutableDate id = null;

        try
        {
            id = (ImmutableDate) super.clone();

            if (date != null)
            {
                id.date = (Date) date.clone();
            }

        } catch (CloneNotSupportedException ex)
        {
            // Never happen
        }

        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public int compareTo(ImmutableDate o)
    {
        return date.compareTo(o.date);
    }

    /**
     * Compares two dates for equality.
     * You can compare this object to either another {@code ImmutableDate},
     * or a {@link java.util.Date}.
     * <p>
     * The result is {@code true} if and only if the argument is
     * not {@code null} and is a {@code Date} object that
     * represents the same point in time, to the millisecond, as this object.
     * <p>
     * Thus, two {@code Date} objects are equal if and only if the
     * {@code getTime} method returns the same {@code long}
     * value for both.
     *
     * @param obj the object to compare with.
     *
     * @return {@code true} if the objects are the same;
     *         {@code false} otherwise.
     *
     * @see java.util.Date#getTime()
     */
    @Override
    @SuppressWarnings(
            {
                "AccessingNonPublicFieldOfAnotherObject",
                "EqualsWhichDoesntCheckParameterClass"
            })
    public boolean equals(Object obj)
    {
        switch (obj)
        {
            case Date lDate ->
            {
                return date.getTime() == lDate.getTime();
            }

            case ImmutableDate iDate ->
            {
                return date.getTime() == iDate.date.getTime();
            }

            case default ->
            {
                return false;
            }
        }
    }

    /**
     * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT
     * represented by this {@code Date} object.
     *
     * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT
     *         represented by this date.
     */
    public long getTime()
    {
        return date.getTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.date);
        return hash;
    }

    /**
     * Converts this {@code Date} object to an {@code Instant}.
     * <p>
     * The conversion creates an {@code Instant} that represents the same
     * point on the time-line as this {@code Date}.
     *
     * @return an instant representing the same point on the time-line as
     *         this {@code Date} object
     *
     * @since 1.8
     */
    public Instant toInstant()
    {
        return date.toInstant();
    }

    /**
     * Converts this {@code Date} object to a {@code String}
     * of the form:
     * <blockquote><pre>
     * dow mon dd hh:mm:ss zzz yyyy</pre></blockquote>
     * where:<ul>
     * <li>{@code dow} is the day of the week ({@code Sun, Mon, Tue, Wed,
     *     Thu, Fri, Sat}).
     * <li>{@code mon} is the month ({@code Jan, Feb, Mar, Apr, May, Jun,
     *     Jul, Aug, Sep, Oct, Nov, Dec}).
     * <li>{@code dd} is the day of the month ({@code 01} through
     * {@code 31}), as two decimal digits.
     * <li>{@code hh} is the hour of the day ({@code 00} through
     * {@code 23}), as two decimal digits.
     * <li>{@code mm} is the minute within the hour ({@code 00} through
     * {@code 59}), as two decimal digits.
     * <li>{@code ss} is the second within the minute ({@code 00} through
     * {@code 61}, as two decimal digits.
     * <li>{@code zzz} is the time zone (and may reflect daylight saving
     * time). Standard time zone abbreviations include those
     * recognized by the method {@code parse}. If time zone
     * information is not available, then {@code zzz} is empty -
     * that is, it consists of no characters at all.
     * <li>{@code yyyy} is the year, as four decimal digits.
     * </ul>
     *
     * @return a string representation of this date.
     *
     * @see java.util.Date#toLocaleString()
     * @see java.util.Date#toGMTString()
     */
    @Override
    public String toString()
    {
        return date.toString();
    }
}
