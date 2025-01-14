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
package com.bewsoftware.utils.concurrent;

import com.bewsoftware.utils.AvlTree;
import com.bewsoftware.utils.Ref;
import com.bewsoftware.utils.ReindexFailedException;
import com.bewsoftware.utils.io.ConsoleIO;
import com.bewsoftware.utils.io.Display;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.lang.Integer.MAX_VALUE;

/**
 * This is a Binary Search Tree with the default capability of being a Balanced
 * Binary Search Tree.
 * <p>
 * This class does not support storage of either {@code null}s or duplicates.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 * @param <E> type of item stored in this tree.
 *
 * @since 3.0.2
 * @version 3.0.2
 */
public final class ConcurrentAvlTree<E extends Comparable<E>> implements Set<E>
{
    /**
     * No nulls allowed string.
     */
    private static final String NO_NULLS = "This class does not support storage of 'null's";

    /**
     * A value indicating whether this {@linkplain  AvlTree}{@literal <T>} is
     * balanced.
     */
    private final boolean balanced;

    /**
     * The number of elements in this tree.
     */
    private final AtomicInteger count = new AtomicInteger();

    private final Display display = ConsoleIO.consoleDisplay("");

    /**
     * The version of data last indexed.
     */
    private final AtomicInteger lastIndexVersion = new AtomicInteger();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Lock rl = lock.readLock();

    /**
     * The root node.
     */
    private final AtomicReference<Node<E>> root = new AtomicReference<>();

    /**
     * The version of the data.
     */
    private final AtomicInteger version = new AtomicInteger();

    private final Lock wl = lock.writeLock();

    /**
     * Initializes a new instance of the {@linkplain  AvlTree}{@literal <E>}
     * class as a Balanced Binary Search Tree.
     */
    public ConcurrentAvlTree()
    {
        balanced = true;
    }

    /**
     * Initializes a new instance of the {@linkplain  AvlTree}{@literal <E>}
     * class as a Binary Search Tree that will/will not be balanced based on the
     * value of the parameter: {@code balanced}.
     *
     * @param balanced if {@code true} tree will be balanced
     */
    public ConcurrentAvlTree(final boolean balanced)
    {
        this.balanced = balanced;
    }

    /**
     * Initializes a new instance of the {@linkplain  AvlTree}{@literal <E>}
     * class with the contents of the {@code list} as a Balanced Binary Search
     * Tree.
     *
     * @param list the list containing the items to add to this tree
     */
    public ConcurrentAvlTree(final List<E> list)
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
    public ConcurrentAvlTree(final List<E> list, final boolean balanced)
    {
        if (list == null)
        {
            throw new IllegalArgumentException("list: must not be 'null'");
        }

        this.balanced = balanced;

        if (!list.isEmpty())
        {
            list.forEach(this::internalAdd);
        }
    }

    @Override
    public boolean add(final E e)
    {
        try
        {
            wl.lockInterruptibly();
        } catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
            return false;
        }

        try
        {
            return internalAdd(e);
        } finally
        {
            wl.unlock();
        }
    }

    @Override
    public boolean addAll(final Collection<? extends E> c)
    {
        try
        {
            wl.lockInterruptibly();
        } catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
            return false;
        }

        try
        {
            final Ref<Boolean> rtn = Ref.val(false);

            if (!Objects.requireNonNull(c).isEmpty())
            {
                rtn.val = true;

                c.stream().takeWhile((t) -> rtn.val).forEach(item ->
                {
                    rtn.val = this.internalAdd(item);
                });
            }

            return rtn.val;
        } finally
        {
            wl.unlock();
        }
    }

    @Override
    public void clear()
    {
        try
        {
            wl.lockInterruptibly();
        } catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
            return;
        }

        try
        {
            root.set(null);
            count.set(0);
            version.getAndIncrement();
        } finally
        {
            wl.unlock();
        }
    }

    @Override
    public boolean contains(final Object o)
    {
        try
        {
            rl.lockInterruptibly();
        } catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
            return false;
        }

        try
        {
            @SuppressWarnings("unchecked")
            final E item = (E) Objects.requireNonNull(o, NO_NULLS);
            return find(item) != null;
        } finally
        {
            rl.unlock();
        }
    }

    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean containsAll(final Collection<?> c)
    {
        try
        {
            rl.lockInterruptibly();
        } catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
            return false;
        }

        try
        {
            boolean rtn = false;

            for (Object object : Objects.requireNonNull(c))
            {
                rtn = contains(object);

                if (!rtn)
                {
                    break;
                }
            }

            return rtn;
        } finally
        {
            rl.unlock();
        }
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
    public void display()
    {
        if (root.get() == null)
        {
            display.println("Tree is empty");
            return;
        }

        display.println(displayInOrder(root.get()));
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return.
     *
     * @return the element at the specified position in this list, if one
     *         exists. {@code null} otherwise.
     *
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   (index {@literal < 0 || index >= } size()).
     */
    public E get(final int index)
    {
        final Node<E> rtn = getNodeAt(index);
        return rtn != null ? rtn.value.get() : null;
    }

    /**
     * Returns the index of the specified element in this list, or -1 if this
     * list does
     * not contain the element.
     *
     * @param o element to search for.
     *
     * @return the index of the specified element in this list, or -1 if this
     *         list does not contain the element.
     *
     * @throws ClassCastException   if the type of the specified element is
     *                              incompatible with this list.
     * @throws NullPointerException if the specified element is null as this
     *                              list does <b>not</b> permit null
     *                              elements.
     */
    public int indexOf(final Object o)
    {
        @SuppressWarnings("unchecked")
        final E item = (E) Objects.requireNonNull(o, NO_NULLS);

        reIndex();
        final Node<E> node = find(item);

        return node != null ? node.index.get() : -1;
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
        return count.get() == 0;
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
    public boolean remove(final Object o)
    {
        return delete((E) Objects.requireNonNull(o, NO_NULLS));
    }

    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean removeAll(final Collection<?> c)
    {
        try
        {
            wl.lockInterruptibly();
        } catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
            return false;
        }

        try
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
        } finally
        {
            wl.unlock();
        }

    }

    @Override
    public boolean retainAll(final Collection<?> c)
    {
        try
        {
            wl.lockInterruptibly();
        } catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
            return false;
        }

        try
        {
            Objects.requireNonNull(c);
            boolean modified = false;

            for (Iterator<?> i = descendingIterator(); i.hasNext();)
            {
                if (!c.contains(i.next()))
                {
                    i.remove();
                    modified = true;
                }
            }

            return modified;
        } finally
        {
            wl.unlock();
        }
    }

    @Override
    public int size()
    {
        return count.get();
    }

    @Override
    public Object[] toArray()
    {
        try
        {
            rl.lockInterruptibly();
        } catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
            return new Object[0];
        }

        try
        {
            final Object[] rtn = new Object[count.get()];
            final Ref<Integer> index = Ref.val(0);

            fillArray(root.get(), rtn, index);

            return rtn;
        } finally
        {
            rl.unlock();
        }
    }

    @Override
    @SuppressWarnings(
            {
                "unchecked", "AssignmentToMethodParameter"
            })
    public <T> T[] toArray(T[] a)
    {
        try
        {
            rl.lockInterruptibly();
        } catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
            return (T[]) new Object[0];
        }

        try
        {
            // Code from LinkedList class, with some mods
            if (Objects.requireNonNull(a).length < count.get())
            {
                a = (T[]) java.lang.reflect.Array.newInstance(
                        a.getClass().getComponentType(), count.get());
            }

            final Ref<Integer> index = Ref.val(0);
            fillArray(root.get(), a, index);

            for (int i = count.get(); i < a.length; i++)
            {
                a[i] = null;
            }

            return a;
        } finally
        {
            rl.unlock();
        }
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
                + displayInOrder(root.get())
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
    private Node<E> addRecursive(Node<E> current, final Node<E> node, final Ref<Boolean> added)
    {
        if (current == null)
        {
            current = node;
            added.val = true;

        } else if (node.value.get().compareTo(current.value.get()) < 0)
        {
            current.left.set(addRecursive(current.left.get(), node, added));

        } else if (node.value.get().compareTo(current.value.get()) > 0)
        {
            current.right.set(addRecursive(current.right.get(), node, added));
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
    private int balanceFactor(final Node<E> current)
    {
        int l = getHeight(current.left.get());
        int r = getHeight(current.right.get());

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
            current = balanceFactor(current.left.get()) > 0
                    ? rotateLeftLeft(current) : rotateLeftRight(current);
        } else if (bFactor < -1)
        {
            current = balanceFactor(current.right.get()) > 0
                    ? rotateRightLeft(current) : rotateRightRight(current);
        }

        return current;
    }

    /**
     * Delete the {@code target} from the tree.
     *
     * @param target the element to delete
     *
     * @return {@code true} unless {@code target} is {@code null}, or
     *         {@code target} is not found.
     */
    private boolean delete(final E target)
    {
        try
        {
            wl.lockInterruptibly();
        } catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
            return false;
        }

        try
        {
            final Ref<Boolean> rtn = Ref.val(false);

            if (target != null)
            {
                root.set(deleteNode(root.get(), target, rtn, balanced));

                if (rtn.val)
                {
                    count.getAndDecrement();
                    version.getAndIncrement();
                }
            }

            return rtn.val;
        } finally
        {
            wl.unlock();
        }
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
    private Node<E> deleteNode(Node<E> current, final E target, final Ref<Boolean> found, final boolean balanced)
    {
        Node<E> parent;

        if (current != null)
        {
            //left subtree
            if (target.compareTo(current.value.get()) < 0)
            {
                current.left.set(deleteNode(current.left.get(), target, found, balanced));

                if (balanced && balanceFactor(current) == -2)//here
                {
                    current = balanceFactor(current.right.get()) <= 0
                            ? rotateRightRight(current) : rotateRightLeft(current);
                }
            } //right subtree
            else if (target.compareTo(current.value.get()) > 0)
            {
                current.right.set(deleteNode(current.right.get(), target, found, balanced));

                if (balanced && balanceFactor(current) == 2)
                {
                    current = balanceFactor(current.left.get()) >= 0
                            ? rotateLeftLeft(current) : rotateLeftRight(current);
                }
            } //if target is found
            else
            {
                if (current.right != null)
                {
                    //delete its in-order successor
                    parent = current.right.get();

                    while (parent.left.get() != null)
                    {
                        parent = parent.left.get();
                    }

                    current.value.set(parent.value.get());
                    current.right.set(deleteNode(current.right.get(), parent.value.get(), found, balanced));

                    if (balanced && balanceFactor(current) == 2)//re-balancing
                    {
                        current = balanceFactor(current.left.get()) >= 0
                                ? rotateLeftLeft(current) : rotateLeftRight(current);
                    }
                } else
                {   //if current.left != null
                    current = current.left.get();
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
    private String displayInOrder(final Node<E> current)
    {
        try
        {
            rl.lockInterruptibly();
        } catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
            return "Thread interrupted!";
        }

        try
        {

            final StringBuilder rtn = new StringBuilder();

            if (current != null)
            {
                rtn.append(displayInOrder(current.left.get()));
                rtn.append(current.value.get()).append(", ");
                rtn.append(displayInOrder(current.right.get()));
            }

            return rtn.length() > 0 ? rtn.toString() : "";
        } finally
        {
            rl.unlock();
        }
    }

    /**
     * Fill the array in proper sequence.
     *
     * @param current the current Node
     * @param array   the array to file
     * @param index   the current index into the array
     */
    @SuppressWarnings("ValueOfIncrementOrDecrementUsed")
    private void fillArray(final Node<E> current, final Object[] array, final Ref<Integer> index)
    {
        if (current != null)
        {
            fillArray(current.left.get(), array, index);
            array[index.val++] = current.value.get();
            fillArray(current.right.get(), array, index);
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
    private Node<E> find(final E key)
    {
        try
        {
            rl.lockInterruptibly();
        } catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
            return null;
        }

        try
        {
            Node<E> rtn = null;

            if (key != null)
            {
                var node = findRecursive(key, root.get());
                rtn = node != null && node.value.equals(key) ? node : null;
            } else
            {
                throw new NullPointerException(NO_NULLS);
            }

            return rtn;
        } finally
        {
            rl.unlock();
        }
    }

    /**
     * Recursively search for the Node at the {@code index}.
     *
     * @param index   the index we are searching for
     * @param current the current Node to check
     *
     * @return the Node if found, otherwise {@code null}
     */
    private Node<E> findIndexRecursive(final int index, final Node<E> current)
    {
        try
        {
            rl.lockInterruptibly();
        } catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
            return null;
        }

        try
        {
            Node<E> rtn = null;

            if (current != null)
            {
                if (index == current.index.get())
                {
                    rtn = current;
                } else
                {
                    rtn = findIndexRecursive(index, index < current.index.get()
                            ? current.left.get() : current.right.get());
                }
            }

            return rtn;
        } finally
        {
            rl.unlock();
        }
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
    private Node<E> findRecursive(final E target, final Node<E> current)
    {
        Node<E> rtn = null;

        if (current != null)
        {
            if (target.equals(current.value))
            {
                rtn = current;
            } else
            {
                rtn = findRecursive(target, target.compareTo(current.value.get()) < 0
                        ? current.left.get() : current.right.get());
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
    private int getHeight(final Node<E> current)
    {
        int height = 0;

        if (current != null)
        {
            final int leftHeight = getHeight(current.left.get());
            final int rightHeight = getHeight(current.right.get());
            final int maxHeight = Math.max(leftHeight, rightHeight);
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
    private Node<E> getNodeAt(final int index)
    {
        if (index < 0 || index >= count.get())
        {
            throw new IndexOutOfBoundsException("index: " + index);
        }

        reIndex();
        return findIndexRecursive(index, root.get());
    }

    /**
     * Gets a value indicating whether [index is dirty].
     *
     * @return {@code true} if the internal index is dirty.
     */
    private boolean indexIsDirty()
    {
        return lastIndexVersion.get() != version.get();
    }

    /**
     * Recursively index the nodes.
     *
     * @param current the current Node
     * @param index   the current index value
     */
    @SuppressWarnings("ValueOfIncrementOrDecrementUsed")
    private void indexRecursively(final Node<E> current, final Ref<Integer> index)
    {
        if (current != null)
        {
            indexRecursively(current.left.get(), index);
            current.index.set(++index.val);
            indexRecursively(current.right.get(), index);
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
    private boolean internalAdd(final E item)
    {
        final Ref<Boolean> rtn = Ref.val(false);

        if (count.get() < MAX_VALUE)
        {
            final Node<E> newItem = new Node<>(Objects.requireNonNull(item, NO_NULLS));

            if (root.get() == null)
            {
                root.set(newItem);
                rtn.val = true;
            } else
            {
                root.set(addRecursive(root.get(), newItem, rtn));
            }

            if (rtn.val)
            {
                count.getAndIncrement();
                version.getAndIncrement();
            }
        }

        return rtn.val;
    }

    /**
     * Re-index the tree.
     */
    private void reIndex()
    {

        if (count.get() == 0 || !indexIsDirty())
        {
            return;
        }

        try
        {
            wl.lockInterruptibly();
        } catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
            return;
        }

        try
        {
            final Ref<Integer> index = Ref.val(-1);

            indexRecursively(root.get(), index);
            lastIndexVersion.set(version.get());

            if (index.val != count.get() - 1)
            {
                throw new ReindexFailedException(
                        "Re-indexing has failed: count(" + count + "), index("
                        + index + ")");
            }
        } finally
        {
            wl.unlock();
        }
    }

    /**
     * Rotate sub-tree Left-Left
     *
     * @param parent the parent Node.
     *
     * @return the pivot Node.
     */
    private Node<E> rotateLeftLeft(final Node<E> parent)
    {
        final Node<E> pivot = parent.left.get();
        parent.left.set(pivot.right.get());
        pivot.right.set(parent);

        return pivot;
    }

    /**
     * Rotate sub-tree Left-Right
     *
     * @param parent the parent Node.
     *
     * @return the pivot Node.
     */
    private Node<E> rotateLeftRight(final Node<E> parent)
    {
        final Node<E> pivot = parent.left.get();
        parent.left.set(rotateRightRight(pivot));

        return rotateLeftLeft(parent);
    }

    /**
     * Rotate sub-tree Right-Left
     *
     * @param parent the parent Node.
     *
     * @return the pivot Node.
     */
    private Node<E> rotateRightLeft(final Node<E> parent)
    {
        final Node<E> pivot = parent.right.get();
        parent.right.set(rotateLeftLeft(pivot));

        return rotateRightRight(parent);
    }

    /**
     * Rotate sub-tree Right-Right
     *
     * @param parent the parent Node.
     *
     * @return the pivot Node.
     */
    private Node<E> rotateRightRight(final Node<E> parent)
    {
        final Node<E> pivot = parent.right.get();
        parent.right.set(pivot.left.get());
        pivot.left.set(parent);

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
            position.set(size());
        }

        @Override
        public boolean hasNext()
        {
            return isExpectedVersion() && position.get() > 0;
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

            if (position.decrementAndGet() < 0)
            {
                throw new NoSuchElementException();
            }

            lastReturned.set((T) get(position.get()));
            return lastReturned.get();
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
        protected final AtomicInteger expectedVersion = new AtomicInteger();

        /**
         * The last entry returned.
         */
        protected final AtomicReference<T> lastReturned = new AtomicReference<>();

        /**
         * The current position within the list.
         */
        protected final AtomicInteger position = new AtomicInteger();

        /**
         * Instantiates a new ATItor object.
         */
        private ATItor()
        {
            position.set(-1);
            expectedVersion.set(version.get());
        }

        @Override
        public boolean hasNext()
        {
            return isExpectedVersion() && position.get() < count.get() - 1;
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

            if (position.incrementAndGet() == count.get())
            {
                throw new NoSuchElementException();
            }

            lastReturned.set((T) get(position.get()));
            return lastReturned.get();
        }

        @Override
        @SuppressWarnings("unchecked")
        public void remove()
        {
            try
            {
                wl.lockInterruptibly();
            } catch (InterruptedException ex)
            {
                Thread.currentThread().interrupt();
                return;
            }

            try
            {
                if (!isExpectedVersion())
                {
                    throw new ConcurrentModificationException(CONCURRENT_MODIFICATION_EXCEPTION);
                }

                if (lastReturned.get() == null)
                {
                    throw new IllegalStateException();
                }

                delete((E) lastReturned.get());
                lastReturned.set(null);
                expectedVersion.set(version.get());
            } finally
            {
                wl.unlock();
            }
        }

        /**
         * Is the current version what we expect?
         *
         * @return result
         */
        protected boolean isExpectedVersion()
        {
            return expectedVersion.get() == version.get();
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
        public final AtomicInteger index = new AtomicInteger();

        /**
         * The attached Left child Node.
         */
        public final AtomicReference<Node<T>> left = new AtomicReference<>();

        /**
         * The attached Right child Node.
         */
        public final AtomicReference<Node<T>> right = new AtomicReference<>();

        /**
         * The value/item being stored in this Node.
         */
        public final AtomicReference<T> value = new AtomicReference<>();

        /**
         * Initialize a new instance of the {@link Node Node&lt;E&gt;} class.
         *
         * @param value to store in this Node.
         */
        private Node(final T value)
        {
            this.value.set(value);
        }
    }
}
