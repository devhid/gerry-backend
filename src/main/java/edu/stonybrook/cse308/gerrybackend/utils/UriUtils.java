package edu.stonybrook.cse308.gerrybackend.utils;

import java.net.URI;

public class UriUtils {

    public static String getStatePath(final URI uri) {
        final String path = uri.getPath();
        final String[] pathFragments = path.split("/");
        final String value = pathFragments[pathFragments.length - 1];
        return value;
    }

}
