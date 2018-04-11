package pt.rhosystems.rhotwitter.utilities;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * This class implements a list where its elements have an expiration
 * date, based on the parameter expireTime, given by its only constructor.
 * @param <K>
 */
public class ExpiringList<K> implements List<K> {
    private long expirationTime;
    private List<K> expirationList;
    private Map<K, Long> timestamps;

    /**
     * Main constructor.
     * @param expirationTime Expiration time, in milliseconds.
     */
    public ExpiringList(long expirationTime) {
        this.expirationTime = expirationTime;
        this.expirationList = new LinkedList<>();
        this.timestamps = new HashMap<>();
    }

    /**
     * Adds an object of type K to the list.
     * @param k Object to be added.
     * @return Always true.
     */
    @Override
    public synchronized boolean add(K k) {
        add(0, k);
        return true;
    }

    /**
     * Adds a object to a given index of the list.
     * @param index Index of the list to place the object.
     * @param element The object of type K to be inserted.
     */
    @Override
    public synchronized void add(int index, K element) {
        clearExpired();

        if (!timestamps.containsKey(element)) {
            timestamps.put(element, System.currentTimeMillis());
            expirationList.add(index, element);
        }
    }

    /**
     * Clears the expired values existing in the list.
     */
    public synchronized void clearExpired() {
        Iterator<K> iterator = expirationList.iterator();
        while (iterator.hasNext()) {
            K k = iterator.next();

            if (timestamps.get(k) != null) {
                long now = System.currentTimeMillis();
                long then = timestamps.get(k);

                if ((now - then) >= expirationTime) {
                    iterator.remove();
                    timestamps.remove(k);
                }
            }
        }
    }

    @Override
    public synchronized int size() {
        return expirationList.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public synchronized boolean contains(Object o) {
        return expirationList.contains(o);
    }

    @NonNull
    @Override
    public Iterator<K> iterator() {
        return expirationList.iterator();
    }

    @NonNull
    @Override
    public synchronized Object[] toArray() {
        return expirationList.toArray();
    }

    @NonNull
    @Override
    public synchronized <T> T[] toArray(@NonNull T[] a) {
        return expirationList.toArray(a);
    }

    @Override
    public synchronized boolean remove(Object o) {
        return expirationList.remove(o);
    }

    @Override
    public synchronized boolean containsAll(@NonNull Collection<?> c) {
        return expirationList.containsAll(c);
    }

    @Override
    public synchronized boolean addAll(@NonNull Collection<? extends K> c) {
        return expirationList.addAll(c);
    }

    @Override
    public synchronized boolean addAll(int index, @NonNull Collection<? extends K> c) {
        return expirationList.addAll(index, c);
    }

    @Override
    public synchronized boolean removeAll(@NonNull Collection<?> c) {
        return expirationList.removeAll(c);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        return expirationList.retainAll(c);
    }

    @Override
    public synchronized void clear() {
        expirationList.clear();
    }

    @Override
    public synchronized K get(int index) {
        return expirationList.get(index);
    }

    @Override
    public synchronized K set(int index, K element) {
        return expirationList.set(index, element);
    }

    @Override
    public synchronized K remove(int index) {
        return expirationList.remove(index);
    }

    @Override
    public synchronized int indexOf(Object o) {
        return expirationList.indexOf(o);
    }

    @Override
    public synchronized int lastIndexOf(Object o) {
        return expirationList.lastIndexOf(o);
    }

    @NonNull
    @Override
    public ListIterator<K> listIterator() {
        return expirationList.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<K> listIterator(int index) {
        return expirationList.listIterator(index);
    }

    @NonNull
    @Override
    public synchronized List<K> subList(int fromIndex, int toIndex) {
        return expirationList.subList(fromIndex, toIndex);
    }
}
