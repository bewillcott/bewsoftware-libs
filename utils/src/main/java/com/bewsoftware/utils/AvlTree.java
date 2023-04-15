/*
 *  File Name:    AvlTree.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2021 Bradley Willcott
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.bewsoftware.utils;

import java.util.*;

/**
 * This is a Binary Search Tree with the default capability of being a Balanced
 * Binary Search Tree.
 * <p>
 * This class does not support storage of either {@code null}s or duplicates.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 * @param <E> type of item stored in this tree.
 *
 * @since 1.0.9
 * @version 3.0.0
 */
public final class AvlTree<E extends Comparable<E>> implements Set<E>
{

    /**
     * No nulls allowed string.
     */
    private static final String NO_NULLS = "This class does not support storage of 'null's";

    /**
     * A value indicating whether this {@linkplain  AvlTree}{@literal <T>} is
     * balanced.
     */
    private boolean balanced;

    /**
     * The number of elements in this tree.
     */
    private int count;

    /**
     * The version of data last indexed.
     */
    private int lastIndexVersion;

    /**
     * The root node.
     */
    private Node<E> root;

    /**
     * The version of the data.
     */
    private int version;

    /**
     * Initializes a new instance of the {@linkplain  AvlTree}{@literal <E>}
     * class as a
     * Balanced Binary Search Tree.
     */
    public AvlTree()
    {
        balanced = true;
    }

    /**
     * Initializes a new instance of the {@linkplain  AvlTree}{@literal <E>}
     * class as a
     * Binary Search Tree that will/will not be balanced based on the value of
     * the
     * parameter: {@code balanced}.
     *
     * @param balanced if {@code true} tree will be balanced
     */
    public AvlTree(boolean balanced)
    {
        this.balanced = balanced;
    }

    /**
     * Initializes a new instance of the {@linkplain  AvlTree}{@literal <E>}
     * class with the contents
     * of the {@code list} as a Balanced Binary Search Tree.
     *
     * @param list the list containing the items to add to this tree
     */
    public AvlTree(List<E> list)
    {
        this(list, true);
    }

    /**
     * Initializes a new instance of the {@linkplain  AvlTree}{@literal <E>}
     * class, as a
     * Binary Search Tree that will/will not be balanced based on the value of
     * the
     * parameter: {@code balanced}, with the contents of the {@code list}.
     *
     * @param list     the list containing the items to add to this tree
     * @param balanced if {@code true} tree will be balanced
     */
    public AvlTree(List<E> list, boolean balanced)
    {
        if (list == null)
        {
            throw new IllegalArgumentException("list: must not be 'null'");
        }

        if (!list.isEmpty())
        {
            list.forEach(this::internalAdd);
        }

        this.balanced = balanced;
    }

    @Override
    public boolean add(E e)
    {
        return internalAdd(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        boolean rtn = false;

        if (!Objects.requireNonNull(c).isEmpty())
        {
            c.forEach(this::internalAdd);
            rtn = true;
        }

        return rtn;
    }

    @Override
    public void clear()
    {
        root = null;
        count = 0;
        version++;
    }

    @Override
    public boolean contains(Object o)
    {
        @SuppressWarnings("unchecked")
        E item = (E) Objects.requireNonNull(o, NO_NULLS);
        return find(item) != null;
    }

    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean containsAll(Collection<?> c)
    {
        boolean rtn = false;

        for (Object object : Objects.requireNonNull(c))
        {
            rtn = contains(object);
        }

        return rtn;
    }

    /**
     * Delete the {@code target} from the tree.
     *
     * @param target the element to delete
     *
     * @return {@code true} unless {@code target} is {@code null}, or
     *         {@code target} is not found.
     */
    public boolean delete(E target)
    {
        Ref<Boolean> rtn = Ref.val(false);

        if (target != null)
        {
            root = deleteNode(root, target, rtn, balanced);

            if (rtn.val)
            {
                count--;
                version++;
            }
        }

        return rtn.val;
    }

    /**
     * Returns an iterator over the elements in this set, in descending order.
     * Equivalent in effect to {@code descendingSet().iterator()}.
     *
     * @return an iterator over the elements in this set, in descending order
     */
    public Iterator<E> descendingIterator()
    {
        reIndex();
        return new ATDescItor<>();
    }

    /**
     * Display the data items in order.
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void display()
    {
        if (root == null)
        {
            System.out.println("Tree is empty");
            return;
        }

        System.out.println(displayInOrder(root));
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     *
     * @return the element at the specified position in this list
     *
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   (index {@literal < 0 || index >= } size())
     */
    //    @Override
    public E get(int index)
    {
        Node<E> rtn = getNodeAt(index);
        return rtn != null ? rtn.Value : null;
    }

    /**
     * Returns the index of the specified element in this list, or -1 if this
     * list does
     * not contain the element.
     *
     * @param o element to search for
     *
     * @return the index of the specified element in this list, or -1 if this
     *         list does
     *         not contain the element
     *
     * @throws ClassCastException   if the type of the specified element is
     *                              incompatible with this list
     * @throws NullPointerException if the specified element is null as this
     *                              list does <b>not</b> permit null
     *                              elements
     */
    //    @Override
    public int indexOf(Object o)
    {
        @SuppressWarnings("unchecked")
        E item = (E) o;
        reIndex();
        Node<E> node = find(item);
        return node != null ? node.Index : -1;
    }

    /**
     * Gets a value indicating whether this {@linkplain  AvlTree}{@literal <T>}
     * is balanced.
     *
     * @return {@code true } if balanced, {@code false } otherwise
     */
    public boolean isBalanced()
    {
        return balanced;
    }

    @Override
    public boolean isEmpty()
    {
        return count == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<E> iterator()
    {
        reIndex();
        return new ATItor<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o)
    {
        if (o == null)
        {
            throw new NullPointerException(NO_NULLS);
        }

        return delete((E) o);
    }

    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean removeAll(Collection<?> c)
    {
        //
        // Original code copied from: java.util.AbstractCollection
        //
        Objects.requireNonNull(c);
        boolean modified = false;

        if (size() > c.size())
        {
            modified = c.stream().map(this::remove).reduce(modified, (accumulator, item) -> accumulator | item);
        } else
        {
            for (Iterator<?> i = iterator(); i.hasNext();)
            {
                if (c.contains(i.next()))
                {
                    i.remove();
                    modified = true;
                }
            }
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        //
        // Code copied from: java.util.AbstractCollection
        //
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<E> it = descendingIterator();

        while (it.hasNext())
        {
            if (!c.contains(it.next()))
            {
                it.remove();
                modified = true;
            }
        }

        return modified;
    }

    @Override
    public int size()
    {
        return count > Integer.MAX_VALUE ? Integer.MAX_VALUE : count;
    }

    @Override
    public Object[] toArray()
    {
        Object[] rtn = new Object[count];
        Ref<Integer> index = Ref.val(0);

        fillArray(root, rtn, index);

        return rtn;
    }

    @Override
    @SuppressWarnings(
            {
                "unchecked", "AssignmentToMethodParameter"
            })
    public <T> T[] toArray(T[] a)
    {
        // Code from LinkedList class, with some mods
        if (a.length < count)
        {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), count);
        }

        Ref<Integer> index = Ref.val(0);
        fillArray(root, a, index);

        for (int i = count; i < a.length; i++)
        {
            a[i] = null;
        }

        return a;
    }

    @Override
    public String toString()
    {
        return "AvlTree{\n"
                + "  balanced = " + balanced + ",\n"
                + "  count = " + count + ",\n"
                + "  lastIndexVersion = " + lastIndexVersion + ",\n"
                + "  version = " + version + "\n"
                + "\n"
                + displayInOrder(root)
                + "\n}";
    }

    /**
     * Recursively search for the correct node to add the new node to.
     *
     * @param current the current Node
     * @param node    the node to add
     * @param added   {@code true } if successful, {@code false } if already
     *                exists
     *
     * @return replacement parent node
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    private Node<E> addRecursive(Node<E> current, Node<E> node, Ref<Boolean> added)
    {
        if (current == null)
        {
            current = node;
            added.val = true;

        } else if (node.Value.compareTo(current.Value) < 0)
        {
            current.Left = addRecursive(current.Left, node, added);

        } else if (node.Value.compareTo(current.Value) > 0)
        {
            current.Right = addRecursive(current.Right, node, added);
        }

        if (balanced && added.val)
        {
            current = balanceTree(current);
        }

        return current;
    }

    /**
     * The balance factor of this part of the tree.
     *
     * @param current the current Node.
     *
     * @return the balance factor
     */
    private int balanceFactor(Node<E> current)
    {
        int l = getHeight(current.Left);
        int r = getHeight(current.Right);

        return l - r;
    }

    /**
     * Method to balance tree after insert or delete.
     *
     * @param current the Node whose sub-tree is to be balanced
     *
     * @return the replacement parent Node
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    private Node<E> balanceTree(Node<E> current)
    {
        int bFactor = balanceFactor(current);

        if (bFactor > 1)
        {
            current = balanceFactor(current.Left) > 0
                    ? rotateLeftLeft(current) : rotateLeftRight(current);
        } else if (bFactor < -1)
        {
            current = balanceFactor(current.Right) > 0
                    ? rotateRightLeft(current) : rotateRightRight(current);
        }

        return current;
    }

    /**
     * Delete the Node containing the {@code target}.
     *
     * @param current  the current Node
     * @param target   the element to be deleted
     * @param found    {@code true} if found
     * @param balanced {@code true} if tree is to be kept balanced
     *
     * @return replacement parent Node
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    private Node<E> deleteNode(Node<E> current, E target, Ref<Boolean> found, boolean balanced)
    {
        Node<E> parent;

        if (current != null)
        {
            //left subtree
            if (target.compareTo(current.Value) < 0)
            {
                current.Left = deleteNode(current.Left, target, found, balanced);

                if (balanced && balanceFactor(current) == -2)//here
                {
                    current = balanceFactor(current.Right) <= 0
                            ? rotateRightRight(current) : rotateRightLeft(current);
                }
            } //right subtree
            else if (target.compareTo(current.Value) > 0)
            {
                current.Right = deleteNode(current.Right, target, found, balanced);

                if (balanced && balanceFactor(current) == 2)
                {
                    current = balanceFactor(current.Left) >= 0
                            ? rotateLeftLeft(current) : rotateLeftRight(current);
                }
            } //if target is found
            else
            {
                if (current.Right != null)
                {
                    //delete its in-order successor
                    parent = current.Right;

                    while (parent.Left != null)
                    {
                        parent = parent.Left;
                    }

                    current.Value = parent.Value;
                    current.Right = deleteNode(current.Right, parent.Value, found, balanced);

                    if (balanced && balanceFactor(current) == 2)//re-balancing
                    {
                        current = balanceFactor(current.Left) >= 0
                                ? rotateLeftLeft(current) : rotateLeftRight(current);
                    }
                } else
                {   //if current.left != null
                    current = current.Left;
                }

                found.val = true;
            }
        }

        return current;
    }

    /**
     * Prepare string output of all items held, in order of sequencing.
     *
     * @param current current Node to work down from
     *
     * @return built string
     */
    private String displayInOrder(Node<E> current)
    {
        StringBuilder rtn = new StringBuilder();

        if (current != null)
        {
            rtn.append(displayInOrder(current.Left));
            rtn.append(current.Value).append(", ");
            rtn.append(displayInOrder(current.Right));
        }

        return rtn.length() > 0 ? rtn.toString() : "";
    }

    /**
     * Fill the array in proper sequence.
     *
     * @param current the current Node
     * @param array   the array to file
     * @param index   the current index into the array
     */
    @SuppressWarnings("ValueOfIncrementOrDecrementUsed")
    private void fillArray(Node<E> current, Object[] array, Ref<Integer> index)
    {
        if (current != null)
        {
            fillArray(current.Left, array, index);
            array[index.val++] = current.Value;
            fillArray(current.Right, array, index);
        }
    }

    /**
     * Finds the specified key.
     *
     * @param key the key to search for
     *
     * @return the {@linkplain Node}{@literal <T>} if found, or {@code null}
     *         otherwise
     */
    private Node<E> find(E key)
    {
        Node<E> rtn = null;

        if (key != null)
        {
            var node = findRecursive(key, root);
            rtn = node != null && node.Value.equals(key) ? node : null;
        } else
        {
            throw new NullPointerException(NO_NULLS);
        }

        return rtn;
    }

    /**
     * Recursively search for the Node at the {@code index}.
     *
     * @param index   the index we are searching for
     * @param current the current Node to check
     *
     * @return the Node if found, otherwise {@code null}
     */
    private Node<E> findIndexRecursive(int index, Node<E> current)
    {
        Node<E> rtn = null;

        if (current != null)
        {
            if (index == current.Index)
            {
                rtn = current;
            } else
            {
                rtn = findIndexRecursive(index, index < current.Index
                        ? current.Left : current.Right);
            }
        }

        return rtn;
    }

    /**
     * Recursively find the {@code target} within the
     * current Node's sub-tree.
     *
     * @param target  the target object being sought
     * @param current the current Node
     *
     * @return the Node if found, otherwise {@code null}
     */
    private Node<E> findRecursive(E target, Node<E> current)
    {
        Node<E> rtn = null;

        if (current != null)
        {
            if (target.equals(current.Value))
            {
                rtn = current;
            } else
            {
                rtn = findRecursive(target, target.compareTo(current.Value) < 0
                        ? current.Left : current.Right);
            }
        }

        return rtn;
    }

    /**
     * Get the height of the current Node.
     *
     * @param current the current Node
     *
     * @return the height
     */
    private int getHeight(Node<E> current)
    {
        // (BW) Changed to 'var'
        int height = 0;

        if (current != null)
        {
            int leftHeight = getHeight(current.Left);
            int rightHeight = getHeight(current.Right);
            int maxHeight = Math.max(leftHeight, rightHeight);
            height = maxHeight + 1;
        }

        return height;
    }

    /**
     * Gets the Node at {@code index}.
     *
     * @param index the index to search for
     *
     * @return the Node if found, {@code null} otherwise
     */
    private Node<E> getNodeAt(int index)
    {
        if (index < 0 || index >= count)
        {
            throw new IndexOutOfBoundsException("index: " + index);
        }

        reIndex();
        return findIndexRecursive(index, root);
    }

    /**
     * Get the root Node.
     *
     * @return the root Node
     */
    private Node<E> getRoot()
    {
        return root;
    }

    /**
     * Set the root Node.
     *
     * @param root the replacement Node
     */
    private void setRoot(Node<E> root)
    {
        this.root = root;
    }

    /**
     * Gets a value indicating whether [index is dirty].
     *
     * @return {@code true} if the internal index is dirty.
     */
    private boolean indexIsDirty()
    {
        return lastIndexVersion != version;
    }

    /**
     * Recursively index the nodes.
     *
     * @param current the current Node
     * @param index   the current index value
     */
    @SuppressWarnings("ValueOfIncrementOrDecrementUsed")
    private void indexRecursively(Node<E> current, Ref<Integer> index)
    {
        if (current != null)
        {
            indexRecursively(current.Left, index);
            current.Index = ++index.val;
            indexRecursively(current.Right, index);
        }
    }

    /**
     * This is used to add an {@code item } to the tree.
     * <p>
     * The reason it has been pulled out of the public method, is to allow
     * constructor access
     * to it, as the public method is a virtual one.
     * </p>
     *
     * @param item the item to add
     *
     * @return {@code true } if successful, {@code false } otherwise
     */
    private boolean internalAdd(E item)
    {
        Ref<Boolean> rtn = Ref.val(false);

        Node<E> newItem = new Node<>(Objects.requireNonNull(item, NO_NULLS));

        if (root == null)
        {
            root = newItem;
            rtn.val = true;
        } else
        {
            root = addRecursive(root, newItem, rtn);
        }

        if (rtn.val)
        {
            count++;
            version++;
        }

        return rtn.val;
    }

    /**
     * Re-index the tree.
     */
    private void reIndex()
    {
        Ref<Integer> index = Ref.val(-1);

        if (count > 0 && indexIsDirty())
        {
            indexRecursively(root, index);
            lastIndexVersion = version;

            if (index.val != count - 1)
            {
                throw new ReindexFailedException(
                        "Re-indexing has failed: count(" + count + "), index("
                        + index + ")");
            }
        }
    }

    /**
     * Rotate sub-tree Left-Left
     *
     * @param parent the parent Node.
     *
     * @return the pivot Node.
     */
    private Node<E> rotateLeftLeft(Node<E> parent)
    {
        Node<E> pivot = parent.Left;
        parent.Left = pivot.Right;
        pivot.Right = parent;

        return pivot;
    }

    /**
     * Rotate sub-tree Left-Right
     *
     * @param parent the parent Node.
     *
     * @return the pivot Node.
     */
    private Node<E> rotateLeftRight(Node<E> parent)
    {
        Node<E> pivot = parent.Left;
        parent.Left = rotateRightRight(pivot);

        return rotateLeftLeft(parent);
    }

    /**
     * Rotate sub-tree Right-Left
     *
     * @param parent the parent Node.
     *
     * @return the pivot Node.
     */
    private Node<E> rotateRightLeft(Node<E> parent)
    {
        Node<E> pivot = parent.Right;
        parent.Right = rotateLeftLeft(pivot);

        return rotateRightRight(parent);
    }

    /**
     * Rotate sub-tree Right-Right
     *
     * @param parent the parent Node.
     *
     * @return the pivot Node.
     */
    private Node<E> rotateRightRight(Node<E> parent)
    {
        Node<E> pivot = parent.Right;
        parent.Right = pivot.Left;
        pivot.Left = parent;

        return pivot;
    }

    /**
     * This is a descending iterator.
     *
     * @param <T> type of the elements
     */
    private class ATDescItor<T> extends ATItor<T>
    {

        /**
         * Instantiates a new ATDescItor object.
         */
        private ATDescItor()
        {
            super();
            position = size();
        }

        @Override
        public boolean hasNext()
        {
            return isExpectedVersion() && position > 0;
        }

        @Override
        @SuppressWarnings(
                {
                    "unchecked", "ValueOfIncrementOrDecrementUsed"
                })
        public T next()
        {
            if (!isExpectedVersion())
            {
                throw new ConcurrentModificationException(CONCURRENT_MODIFICATION_EXCEPTION);
            }

            if (--position < 0)
            {
                throw new NoSuchElementException();
            }

            lastReturned = (T) get(position);
            return lastReturned;
        }
    }

    /**
     * This is an ascending iterator.
     *
     * @param <T> type of the elements
     */
    private class ATItor<T> implements Iterator<T>
    {

        /**
         * String to display when there is a ConcurrentModificationException.
         */
        protected static final String CONCURRENT_MODIFICATION_EXCEPTION
                = "Another thread has modified the data structure";

        /**
         * The expected version number.
         */
        protected int expectedVersion;

        /**
         * The last entry returned.
         */
        protected T lastReturned;

        /**
         * The current position within the list.
         */
        protected int position;

        /**
         * Instantiates a new ATItor object.
         */
        private ATItor()
        {
            position = -1;
            expectedVersion = version;
            lastReturned = null;
        }

        @Override
        public boolean hasNext()
        {
            return isExpectedVersion() && position < count - 1;
        }

        @Override
        @SuppressWarnings(
                {
                    "unchecked", "ValueOfIncrementOrDecrementUsed"
                })
        public T next()
        {
            if (!isExpectedVersion())
            {
                throw new ConcurrentModificationException(CONCURRENT_MODIFICATION_EXCEPTION);
            }

            if (++position == count)
            {
                throw new NoSuchElementException();
            }

            lastReturned = (T) get(position);
            return lastReturned;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void remove()
        {
            if (!isExpectedVersion())
            {
                throw new ConcurrentModificationException(CONCURRENT_MODIFICATION_EXCEPTION);
            }

            if (lastReturned == null)
            {
                throw new IllegalStateException();
            }

            delete((E) lastReturned);
            lastReturned = null;
            expectedVersion = version;
        }

        /**
         * Is the current version what we expect?
         *
         * @return result
         */
        protected boolean isExpectedVersion()
        {
            return expectedVersion == version;
        }
    }

    /**
     * Stores a value/item in the BST.
     *
     * @param <T> element type
     */
    @SuppressWarnings("ProtectedField")
    private final class Node<T>
    {

        /**
         * List index position of this Node.
         */
        public int Index;

        /**
         * The attached Left child Node.
         */
        public Node<T> Left;

        /**
         * The attached Right child Node.
         */
        public Node<T> Right;

        /**
         * The value/item being stored in this Node.
         */
        public T Value;

        /**
         * Initialize a new instance of the {@link Node Node&lt;E&gt;} class.
         *
         * @param value to store in this Node.
         */
        private Node(T value)
        {
            this.Value = value;
        }
    }
}
