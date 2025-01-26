/*
 *  File Name:    ImmutableDate.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2021-2023 Bradley Willcott
 *
 *  bewsoftware-utils is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  bewsoftware-utils is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.bewsoftware.utils;

import com.bewsoftware.annotations.jcip.Immutable;
import java.io.Serializable;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * The class ImmutableDate represents a specific instant in time, with
 * millisecond precision.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 3.0.2
 */
@Immutable
public class ImmutableDate implements Serializable, Cloneable, Comparable<ImmutableDate>
{
    private static final long serialVersionUID = 5239153880709366361L;

    /**
     * The day of the month between 1-31.
     */
    public final int dom;

    /**
     * The month between 0-11. January = 0.
     */
    public final int month;

    /**
     * The year.
     */
    public final int year;

    /**
     * The milliseconds since January 1, 1970, 00:00:00 GMT (Gregorian).
     */
    private final long millis;

    /**
     * Constructs a default {@code ImmutableDate} using the current time in the
     * default time zone with the default
     * {@linkplain Locale.Category#FORMAT FORMAT} locale.
     *
     * @deprecated Replacing all public constructors with static factory methods.
     *
     * @see ImmutableDate#of() : as the replacement
     * @see GregorianCalendar : used internally.
     */
    @Deprecated(forRemoval = true, since = "3.0.2")
    public ImmutableDate()
    {
        this(new GregorianCalendar());
    }

    /**
     * Initializes a new instance of {@code ImmutableDate} to represent the
     * specified number of milliseconds since the standard base time known as
     * "the epoch", namely January 1, 1970, 00:00:00 GMT.
     *
     * @param millis the milliseconds since January 1, 1970, 00:00:00 GMT
     *               (Gregorian).
     *
     * @deprecated Replacing all public constructors with static factory methods.
     *
     * @see ImmutableDate#of(long) : as the replacement
     * @see System#currentTimeMillis()
     */
    @Deprecated(forRemoval = true, since = "3.0.2")
    public ImmutableDate(final long millis)
    {
        final Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(millis);
        this(cal);
    }

    /**
     * Initializes a new instance of {@code ImmutableDate} setting its values to
     * those in the {@code date} object.
     *
     * @param date Date object to copy from.
     *
     * @deprecated Replacing all public constructors with static factory methods.
     *
     * @see ImmutableDate#from(Date) : as the replacement
     */
    @Deprecated(forRemoval = true, since = "3.0.2")
    public ImmutableDate(final Date date)
    {
        final Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(date.getTime());
        this(cal);
    }

    /**
     * Allocates an ImmutableDate object and initializes it so that it
     * represents midnight, local time, at the beginning of the day specified by
     * the year, month, and dom arguments.
     *
     * @param year  the year beginning at 1900.
     * @param month the month between 0-11. January = 0.
     * @param dom   the day of the month between 1-31.
     *
     * @deprecated Replacing all public constructors with static factory methods.
     *
     * @see ImmutableDate#of(int, int, int) : as the replacement
     */
    @Deprecated(forRemoval = true, since = "3.0.2")
    public ImmutableDate(final int year, final int month, final int dom)
    {
        this(new GregorianCalendar(year, month, dom));
    }

    /**
     * This constructor is called by all the {@code public} constructors, to
     * finish the initialization process.
     *
     * @param cal The calendar with required information.
     */
    private ImmutableDate(final Calendar cal)
    {
        year = cal.isSet(Calendar.YEAR) ? cal.get(Calendar.YEAR) : 1970;
        month = cal.isSet(Calendar.MONTH) ? cal.get(Calendar.MONTH) : 0;
        dom = cal.isSet(Calendar.DAY_OF_MONTH) ? cal.get(Calendar.DAY_OF_MONTH) : 1;
        millis = cal.getTimeInMillis();
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
     * @since 1.8,
     * @since 3.0.2 (made final)
     */
    public static final ImmutableDate from(final Instant instant)
    {
        try
        {
            return new ImmutableDate(instant.toEpochMilli());
        } catch (ArithmeticException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Initializes a new instance of {@code ImmutableDate} setting its values to
     * those in the {@code date} object.
     *
     * @return a new fully configured Immutable instance.
     *
     * @param date Date object to copy from.
     *
     * @since 3.0.2
     */
    public static final ImmutableDate from(final Date date)
    {
        final Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(date.getTime());
        return new ImmutableDate(cal);
    }

    /**
     * Initializes a new instance of {@code ImmutableDate} to represent the
     * specified number of milliseconds since the standard base time known as
     * "the epoch", namely January 1, 1970, 00:00:00 GMT.
     *
     * @param millis the milliseconds since January 1, 1970, 00:00:00 GMT
     *               (Gregorian).
     *
     * @return a new fully configured Immutable instance.
     *
     * @see java.lang.System#currentTimeMillis()
     *
     * @since 3.0.2
     */
    public static final ImmutableDate of(final long millis)
    {
        final Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(millis);
        return new ImmutableDate(cal);
    }

    /**
     * Constructs a default {@code ImmutableDate} using the current time in the
     * default time zone with the default
     * {@linkplain Locale.Category#FORMAT FORMAT} locale.
     *
     * @return a new fully configured Immutable instance.
     *
     * @see GregorianCalendar
     *
     * @since 3.0.2
     */
    public static final ImmutableDate of()
    {
        return new ImmutableDate(new GregorianCalendar());
    }

    /**
     * Allocates an ImmutableDate object and initializes it so that it
     * represents midnight, local time, at the beginning of the day specified by
     * the year, month, and dom arguments.
     *
     * @param year  the year beginning at 1900.
     * @param month the month between 0-11. January = 0.
     * @param dom   the day of the month between 1-31.
     *
     * @return a new fully configured Immutable instance.
     *
     * @since 3.0.2
     */
    public static final ImmutableDate of(final int year, final int month, final int dom)
    {
        return new ImmutableDate(new GregorianCalendar(year, month, dom));
    }

    /**
     * Tests if this date is after the specified date.
     *
     * @param when the object to be compared.
     *
     * @return {@code true} if and only if the instant represented
     *         by this {@code ImmutableDate} object is strictly later than the
     *         instant represented by {@code when};
     *         {@code false} otherwise.
     *
     * @exception NullPointerException if {@code when} is null.
     */
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public boolean after(final ImmutableDate when)
    {
        return this.millis > when.millis;
    }

    /**
     * Tests if this date is before the specified date.
     *
     * @param when the object to be compared.
     *
     * @return {@code true} if and only if the instant of time
     *         represented by this {@code ImmutableDate} object is strictly
     *         earlier than the instant represented by {@code when};
     *         {@code false} otherwise.
     *
     * @exception NullPointerException if {@code when} is null.
     */
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public boolean before(final ImmutableDate when)
    {
        return this.millis < when.millis;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public int compareTo(final ImmutableDate other)
    {
        return Long.compare(this.millis, other.millis);
    }

    @Override
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public boolean equals(final Object obj)
    {
        return obj instanceof ImmutableDate other && this.millis == other.millis;
    }

    /**
     * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT
     * represented by this {@code ImmutableDate} object.
     *
     * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT
     *         represented by this date.
     */
    public long getTime()
    {
        return this.millis;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 89 * hash + (int) (this.millis ^ (this.millis >>> 32));
        return hash;
    }

    /**
     * Converts this {@code ImmutableDate} object to a
     * {@code GregorianCalendar}.
     * <p>
     * The conversion creates a {@code GregorianCalendar} that represents the
     * same point on the time-line as this {@code ImmutableDate}.
     *
     * @return a GregorianCalendar representing the same point on the time-line
     *         as this {@code ImmutableDate} object.
     *
     * @since 3.0.2
     */
    public GregorianCalendar toCalendar()
    {
        final GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(millis);
        return cal;
    }

    /**
     * Converts this {@code ImmutableDate} object to a {@code Date}.
     * <p>
     * The conversion creates a {@code Date} that represents the same
     * point on the time-line as this {@code ImmutableDate}.
     *
     * @return a date representing the same point on the time-line as
     *         this {@code ImmutableDate} object.
     *
     * @since 3.0.2
     */
    public Date toDate()
    {
        return new Date(millis);
    }

    /**
     * Converts this {@code ImmutableDate} object to an {@code Instant}.
     * <p>
     * The conversion creates an {@code Instant} that represents the same
     * point on the time-line as this {@code ImmutableDate}.
     *
     * @return an instant representing the same point on the time-line as
     *         this {@code ImmutableDate} object
     *
     * @since 1.8
     */
    public Instant toInstant()
    {
        return Instant.ofEpochMilli(getTime());
    }

    /**
     * Converts this {@code ImmutableDate} object to a {@code String}
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
        return new Date(millis).toString();
    }
}
