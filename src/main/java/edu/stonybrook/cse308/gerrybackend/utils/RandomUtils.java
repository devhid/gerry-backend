package edu.stonybrook.cse308.gerrybackend.utils;

import java.util.Collection;
import java.util.Iterator;

public class RandomUtils {

    public static int randomIndex(Collection collection) {
        return (int) Math.floor(Math.random() * collection.size());
    }

    public static <E> E getRandomElement(Collection<E> collection) throws IllegalArgumentException {
        int randomIndex = randomIndex(collection);
        Iterator<E> iter = collection.iterator();
        for (int idx = 0; idx < randomIndex; idx++) {
            iter.next();
        }
        if (iter.hasNext()) {
            return iter.next();
        } else {
            return null;
        }
    }

}
