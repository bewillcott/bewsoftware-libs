/*
 *  File Name:    ObservableArrayList.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2025 Bradley Willcott
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static com.bewsoftware.utils.ObservableArrayList.ChangeType.*;

/**
 * ObservableArrayList class description.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 * @param <E> the type of elements in this list
 *
 * @since 3.1.0
 * @version 3.1.0
 */
public class ObservableArrayList<E extends Observable> extends SerializableArrayList<E> implements ObservableList<E>
{
    private static final long serialVersionUID = 1L;

    private final int id;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Create a new instance.
     *
     * @param id The unique id for this observable list..
     *
     * @since 3.1.0
     */
    public ObservableArrayList(final int id)
    {
        super();
        this.id = id;
    }

    @Override
    public void add(final int index, final E element)
    {
        final int expectedModCount = modCount;
        element.addPropertyChangeListener(pcs::firePropertyChange);
        super.add(index, element);

        if (modCount != expectedModCount)
        {
            pcs.firePropertyChange(ADDED.toString(), null, element);
        }
    }

    @Override
    public boolean add(final E e)
    {
        final boolean rtn;
        e.addPropertyChangeListener(pcs::firePropertyChange);

        if (rtn = super.add(e))
        {
            pcs.firePropertyChange(ADDED.toString(), null, e);
        }

        return rtn;
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> c)
    {
        final boolean rtn;
        c.forEach((final E e) -> e.addPropertyChangeListener(pcs::firePropertyChange));

        if (rtn = super.addAll(index, c))
        {
            pcs.firePropertyChange(ADDED_ALL.toString(), null, c);
        }

        return rtn;
    }

    @Override
    public boolean addAll(final Collection<? extends E> c)
    {
        final boolean rtn;
        c.forEach((final E e) -> e.addPropertyChangeListener(pcs::firePropertyChange));

        if (rtn = super.addAll(c))
        {
            pcs.firePropertyChange(ADDED_ALL.toString(), null, c);
        }

        return rtn;
    }

    @Override
    public void addFirst(final E element)
    {
        final int expectedModCount = modCount;
        element.addPropertyChangeListener(pcs::firePropertyChange);
        super.addFirst(element);

        if (modCount != expectedModCount)
        {
            pcs.firePropertyChange(ADDED.toString(), null, element);
        }
    }

    @Override
    public void addLast(final E element)
    {
        final int expectedModCount = modCount;
        element.addPropertyChangeListener(pcs::firePropertyChange);
        super.addLast(element);

        if (modCount != expectedModCount)
        {
            pcs.firePropertyChange(ADDED.toString(), null, element);
        }
    }

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener)
    {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener)
    {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    public void clear()
    {
        final int expectedModCount = modCount;
        super.clear();

        if (modCount != expectedModCount)
        {
            pcs.firePropertyChange(CLEARED.toString(), null, null);
        }
    }

    @Override
    public Object clone()
    {
        return super.clone();
    }

    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public boolean remove(final Object o)
    {
        final boolean rtn;

        if (rtn = super.remove(o))
        {
            pcs.firePropertyChange(REMOVED.toString(), o, null);
        }

        return rtn;
    }

    @Override
    public E remove(final int index)
    {
        final E rtn;

        if ((rtn = super.remove(index)) != null)
        {
            pcs.firePropertyChange(REMOVED.toString(), rtn, null);
        }

        return rtn;
    }

    @Override
    public boolean removeAll(final Collection<?> c)
    {
        final int expectedModCount = modCount;
        super.removeAll(c);

        if (modCount != expectedModCount)
        {
            pcs.firePropertyChange(REMOVED_ALL.toString(), c, null);
        }

        return true;
    }

    @Override
    public E removeFirst()
    {
        final E rtn;

        if ((rtn = super.removeFirst()) != null)
        {
            pcs.firePropertyChange(REMOVED.toString(), rtn, null);
        }

        return rtn;
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter)
    {
        final boolean rtn;

        if (rtn = super.removeIf(filter))
        {
            pcs.firePropertyChange(REMOVED_IF.toString(), null, null);
        }

        return rtn;
    }

    @Override
    public E removeLast()
    {
        final E rtn;

        if ((rtn = super.removeLast()) != null)
        {
            pcs.firePropertyChange(REMOVED.toString(), rtn, null);
        }

        return rtn;
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener)
    {
        pcs.removePropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener)
    {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    @Override
    public void replaceAll(final UnaryOperator<E> operator)
    {
        super.replaceAll(operator);
    }

    @Override
    public boolean retainAll(final Collection<?> c)
    {
        final int expectedModCount = modCount;
        super.retainAll(c);

        if (modCount != expectedModCount)
        {
            pcs.firePropertyChange(RETAINED_ALL.toString(), c, null);
        }

        return true;
    }

    @Override
    public E set(final int index, final E element)
    {
        final E oldElement = super.set(index, element);
        pcs.firePropertyChange(REPLACED.toString(), oldElement, element);

        return oldElement;
    }

    @Override
    public void sort(final Comparator<? super E> c)
    {
        final int expectedModCount = modCount;
        super.sort(c);

        if (modCount != expectedModCount)
        {
            pcs.firePropertyChange(SORTED.toString(), null, null);
        }
    }

    /**
     * The list of change types used with the
     * {@link PropertyChangeEvent} sent whenever
     * a bound property changes.
     */
    @SuppressWarnings("PublicInnerClass")
    public static enum ChangeType
    {
        /**
         * An element has been added.
         */
        ADDED("Added"),
        /**
         * A list of elements have been added.
         */
        ADDED_ALL("Added All"),
        /**
         * All elements have been removed.
         */
        CLEARED("Cleared"),
        /**
         * An element has been removed.
         */
        REMOVED("Removed"),
        /**
         * Removed from this list all of its elements that
         * are contained in the specified collection.
         */
        REMOVED_ALL("Removed All"),
        /**
         * Removes all of the elements of this collection
         * that satisfy the given predicate.
         */
        REMOVED_IF("Removed If"),
        /**
         * Replaced the element at the specified position
         * in this list with the specified element.
         */
        REPLACED("Replaced"),
        /**
         * Retained only the elements in this list that
         * are contained in the specified collection.
         */
        RETAINED_ALL("Retained All"),
        /**
         * The list has been sorted.
         */
        SORTED("Sorted");

        private final String text;

        private ChangeType(final String text)
        {
            this.text = text;
        }

        @Override
        public String toString()
        {
            return text;
        }
    }
}
