package edu.stonybrook.cse308.gerrybackend.data.pairs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class UnorderedPair<E> implements Set<E> {

    @Getter
    protected E item1;

    @Getter
    protected E item2;

    private int size;

    @Value("${gerry.hashcode.initial}")
    private int hashCodeInitial;

    @Value("${gerry.hashcode.multiplier}")
    private int hashCodeMultiplier;

    public UnorderedPair() {
        this.item1 = null;
        this.item2 = null;
        this.size = 0;
    }

    public UnorderedPair(E item1, E item2) {
        this.add(item1);
        this.add(item2);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return (this.item1 == null && this.item2 == null);
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        boolean item1 = false;
        boolean item2 = false;
        if (this.item1 != null) {
            item1 = this.item1.equals(o);
        }
        if (this.item2 != null) {
            item2 = this.item2.equals(o);
        }
        return item1 || item2;
    }

    @Override
    public Iterator<E> iterator() {
        Iterator<E> it = new Iterator<E>() {

            private int numIterated = 0;
            private int itemNum = 1;

            @Override
            public boolean hasNext() {
                return size > numIterated;
            }

            @Override
            public E next() {
                E item = null;
                if (itemNum == 1) {
                    if (item1 != null) {
                        item = item1;
                    } else {
                        itemNum++;
                    }
                }
                if (itemNum == 2) {
                    item = item2;
                }
                numIterated++;
                itemNum++;
                return item;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[this.size];
        int idx = 0;
        if (this.item1 != null) {
            arr[idx] = this.item1;
            idx++;
        }
        if (this.item2 != null) {
            arr[idx] = this.item2;
        }
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] arr = a;
        if (a.length < this.size) {
            @SuppressWarnings("unchecked")
            T[] casted = (T[]) new Object[this.size];
            arr = casted;
        }
        int idx = 0;
        if (this.item1 != null) {
            @SuppressWarnings("unchecked")
            T casted = (T) this.item1;
            arr[idx] = casted;
            idx++;
        }
        if (this.item2 != null) {
            @SuppressWarnings("unchecked")
            T casted = (T) this.item2;
            arr[idx] = casted;
        }
        return arr;
    }

    @Override
    public boolean add(E e) {
        if (e == null || this.size == 2) {
            return false;
        }
        if (this.size == 1) {
            boolean item1 = false;
            boolean item2 = false;
            if (this.item1 != null) {
                item1 = this.item1.equals(e);
            }
            if (this.item2 != null) {
                item2 = this.item2.equals(e);
            }
            if (item1 || item2) {
                return false;
            }
        }
        if (this.item1 == null) {
            this.item1 = e;
        } else {
            this.item2 = e;
        }
        this.size += 1;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || this.size == 0) {
            return false;
        }
        if (!this.contains(o)) {
            return false;
        }
        if (this.item1.equals(o)) {
            this.item1 = null;
            this.size -= 1;
        } else {
            this.item2 = null;
            this.size -= 1;
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c.size() > this.size) {
            return false;
        }
        boolean found = true;
        for (Object o : c) {
            found = this.contains(o);
            if (!found) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.size() > this.size) {
            return false;
        }
        for (E o : c) {
            this.add(o);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        if (!c.contains(this.item1)) {
            changed = true;
            this.remove(this.item1);
        }
        if (!c.contains(this.item2)) {
            changed = true;
            this.remove(this.item2);
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        if (c.contains(this.item1)) {
            changed = true;
            this.remove(this.item1);
        }
        if (c.contains(this.item2)) {
            changed = true;
            this.remove(this.item2);
        }
        return changed;
    }

    @Override
    public void clear() {
        this.item1 = null;
        this.item2 = null;
        this.size = 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UnorderedPair)) {
            return false;
        }
        UnorderedPair otherPair = (UnorderedPair) obj;
        if (otherPair.size() != this.size()) {
            return false;
        }
        boolean item1Found = true;
        boolean item2Found = true;
        if (this.item1 != null) {
            item1Found = otherPair.contains(this.item1);
        }
        if (this.item2 != null) {
            item2Found = otherPair.contains(this.item2);
        }
        return item1Found && item2Found;
    }

    @Override
    public int hashCode() {
        int item1Hash = (this.item1 != null) ? this.item1.hashCode() : 0;
        int item2Hash = (this.item2 != null) ? this.item2.hashCode() : 0;
        return hashCodeInitial * hashCodeMultiplier + (item1Hash + item2Hash);
    }
}
