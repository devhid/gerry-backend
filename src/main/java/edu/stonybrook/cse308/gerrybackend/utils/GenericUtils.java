package edu.stonybrook.cse308.gerrybackend.utils;

import java.util.Set;
import java.util.stream.Collectors;

public class GenericUtils {

    public static <T> T castInstanceOfObject(Object o, Class<T> clazz) {
        try {
            return clazz.cast(o);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public static <T> Set<T> castSetOfObjects(Set<?> objects, Class<T> clazz) {
        return objects.stream().map(clazz::cast).collect(Collectors.toSet());
    }
}
