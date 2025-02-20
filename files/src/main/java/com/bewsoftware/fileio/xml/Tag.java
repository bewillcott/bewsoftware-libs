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

import com.bewsoftware.fileio.property.XmlProperty;
import com.bewsoftware.utils.Observable;
import com.bewsoftware.utils.ObservableArrayList;
import com.bewsoftware.utils.ObservableList;
import com.bewsoftware.utils.string.MessageBuilder;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.function.IntSupplier;

import static com.bewsoftware.fileio.xml.XPath.of;
import static com.bewsoftware.utils.string.Strings.indentLines;
import static com.bewsoftware.utils.string.Strings.notEmpty;
import static com.bewsoftware.utils.string.Strings.requireNonBlank;
import static java.util.Objects.requireNonNull;

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

    /**
     * Index of last returned Tag from the {@link searchArray}.
     *
     * @since 3.1.0
     */
    private int lastReturnedIndex = -1;

    private String name;

    private final Tag parent;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * The array of tags found during the last call to {@link #getChild(String) getChild(name)}.
     *
     * @since 3.1.0
     */
    private Tag[] searchArray = null;

    private String text;

    private final XPath xp;

    /**
     * Create named tag.
     *
     * @param parent     of this tag. Can be <i>null</i>.
     * @param idSupplier <i>id</i> number supplier.
     * @param name       of tag.
     *
     * @since 3.1.0
     */
    protected Tag(final Tag parent, final IntSupplier idSupplier, final String name)
    {
        this.parent = parent;
        this.idSupplier = requireNonNull(idSupplier);
        this.name = requireNonBlank(name);

        id = this.idSupplier.getAsInt();
        xp = of(parent, name);

        attributes = new ObservableArrayList<>(this.idSupplier.getAsInt());
        attributes.addPropertyChangeListener(pcs::firePropertyChange);

        children = new ObservableArrayList<>(this.idSupplier.getAsInt());
        children.addPropertyChangeListener(pcs::firePropertyChange);
    }

    /**
     * Create named tag.
     * <p>
     * The new Tag is added to the parent's children.
     *
     * @param parent     of this tag. Can be <i>null</i>.
     * @param idSupplier <i>id</i> number supplier.
     * @param name       of tag.
     *
     * @return a new Tag instance.
     *
     * @since 3.1.0
     */
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public static Tag get(final Tag parent, final IntSupplier idSupplier, final String name
    )
    {
        final Tag tag = new Tag(parent, idSupplier, name);

        if (parent != null)
        {
            parent.children.add(tag);
        }

        return tag;
    }

    /**
     * Add a PropertyChangeListener for all properties.
     *
     * @param listener The PropertyChangeListener to be added.
     *
     * @since 3.1.0
     */
    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener
    )
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
    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener
    )
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
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public List<XmlProperty> getAttributes()
    {
        return attributes;
    }

    /**
     * Returns the first child tag with the specified {@code name}.
     *
     * @param name to search for.
     *
     * @return either the first child tag with the specified {@code name},
     *         or <i>null</i> if none found.
     *
     * @since 3.1.0
     */
    public Tag getChild(final String name)
    {
        Tag rtn = null;

        searchArray = children.stream()
                .filter((final Tag t) -> t.getName().equals(name))
                .toArray(Tag[]::new);

        if (searchArray.length > 0)
        {
            lastReturnedIndex = 0;
            rtn = searchArray[lastReturnedIndex];
        } else
        {
            lastReturnedIndex = -1;
            searchArray = null;
        }

        return rtn;
    }

    /**
     * Get the value of children
     *
     * @return the value of children
     *
     * @since 3.1.0
     */
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public List<Tag> getChildren()
    {
        return children;
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
     * Returns the parent tag, if set, <i>null</i> otherwise.
     *
     * @return the parent tag, if set, <i>null</i> otherwise.
     *
     * @since 3.1.0
     */
    public Tag getParent()
    {
        return parent;
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

    public XPath getXPath()
    {
        return xp;
    }

    /**
     * Returns {@code true} if the search results have not all been returned
     * via {@link #nextChild()}, {@code false} otherwise.
     *
     * @return if a child tag is available for {@link #nextChild()}.
     *
     * @since 3.1.0
     */
    public boolean hasChild()
    {
        return lastReturnedIndex >= 0 && lastReturnedIndex < searchArray.length - 1;
    }

    /**
     * Has the parent been set?
     *
     * @return {@code true} if set, {@code false} otherwise.
     *
     * @since 3.1.0
     */
    public boolean hasParent()
    {
        return parent != null;
    }

    /**
     * Returns the next available child tag, from the search results
     * of the last call to {@link #getChild(String) getChild(name)}.
     *
     * @return the next available child tag, or <i>null</i> if none found.
     *
     * @since 3.1.0
     */
    public Tag nextChild()
    {
        return hasChild() ? searchArray[++lastReturnedIndex] : null;
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

    @Override
    public String toString()
    {
        final int spaces;
        boolean root = false;

        if ("/".equals(name))
        {
            root = true;
            spaces = 0;

        } else
        {
            spaces = 4;
        }

        final MessageBuilder mb = new MessageBuilder();

        if (!root)
        {
            mb.append('<').append(name);

            attributes.forEach((final XmlProperty p) ->
            {
                mb.append(' ')
                        .append(p.key())
                        .append("=\"")
                        .append(p.value())
                        .append('\"');
            });

            mb.append('>');

            if (notEmpty(text))
            {
                if (children.isEmpty())
                {
                    mb.append(text);
                } else
                {
                    mb.appendln(indentLines(text, spaces));
                }
            }
        }

        if (!children.isEmpty())
        {
            if (!root)
            {
                mb.appendln();
            }

            children.forEach((final Tag t) ->
            {
                mb.appendln(indentLines(t, spaces));
            });
        }

        if (!root)
        {
            mb.append("</").append(name).append('>');
        }

        return mb.toString();
    }
}
