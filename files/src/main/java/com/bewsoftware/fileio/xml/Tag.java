/*
 *  File Name:    Tag.java
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

import com.bewsoftware.fileio.property.IniProperty;
import com.bewsoftware.fileio.property.XmlProperty;
import com.bewsoftware.utils.Observable;
import com.bewsoftware.utils.ObservableArrayList;
import com.bewsoftware.utils.ObservableList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.List;
import java.util.function.IntSupplier;

/**
 * Tag class description.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.1.0
 * @version 3.1.0
 */
public class Tag implements Observable
{
    public static final String PROP_ATTRIBUTES = "attributes";

    public static final String PROP_CHILDREN = "children";

    public static final String PROP_NAME = "name";

    public static final String PROP_TEXT = "text";

    private final ObservableList<XmlProperty> attributes;

    private final ObservableList<Tag> children;

    private final int id;

    private transient final IntSupplier idSupplier;

    private String name;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private String text;

    /**
     * Create named tag.
     *
     * @param idSupplier <i>id</i> number supplier.
     * @param name       of tag.
     *
     * @since 3.1.0
     */
    public Tag(final IntSupplier idSupplier, final String name)
    {
        this.idSupplier = idSupplier;
        this.name = name;

        id = this.idSupplier.getAsInt();
        attributes = new ObservableArrayList<>(this.idSupplier.getAsInt());
        attributes.addPropertyChangeListener(pcs::firePropertyChange);
        children = new ObservableArrayList<>(this.idSupplier.getAsInt());
        children.addPropertyChangeListener(pcs::firePropertyChange);
    }

    /**
     * Add a PropertyChangeListener for all properties.
     *
     * @param listener The PropertyChangeListener to be added.
     *
     * @since 3.1.0
     */
    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener)
    {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Add a PropertyChangeListener for the specific property.
     *
     * @param propertyName The name of the property to listen on.
     * @param listener     The PropertyChangeListener to be added.
     *
     * @since 3.1.0
     */
    @Override
    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener)
    {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Get the value of attributes
     *
     * @return the value of attributes
     *
     * @since 3.1.0
     */
    public List<IniProperty<String>> getAttributes()
    {
        return Collections.unmodifiableList(attributes);
    }

    /**
     * Get the value of children
     *
     * @return the value of children
     *
     * @since 3.1.0
     */
    public List<Tag> getChildren()
    {
        return Collections.unmodifiableList(children);
    }

    @Override
    public int getId()
    {
        return id;
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     *
     * @since 3.1.0
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the value of text
     *
     * @return the value of text
     *
     * @since 3.1.0
     */
    public String getText()
    {
        return text;
    }

    /**
     * Remove a PropertyChangeListener from the listener list.
     *
     * @param listener The PropertyChangeListener to be removed.
     *
     * @since 3.1.0
     */
    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener)
    {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Remove a PropertyChangeListener from the listener list.
     *
     * @param propertyName The name of the property that was listened on.
     * @param listener     The PropertyChangeListener to be removed.
     *
     * @since 3.1.0
     */
    @Override
    public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener)
    {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     *
     * @since 3.1.0
     */
    public void setName(final String name)
    {
        String oldName = this.name;
        this.name = name;
        pcs.firePropertyChange(PROP_NAME, oldName, name);
    }

    /**
     * Set the value of text
     *
     * @param text new value of text
     *
     * @since 3.1.0
     */
    public void setText(final String text)
    {
        String oldText = this.text;
        this.text = text;
        pcs.firePropertyChange(PROP_TEXT, oldText, text);
    }

}
