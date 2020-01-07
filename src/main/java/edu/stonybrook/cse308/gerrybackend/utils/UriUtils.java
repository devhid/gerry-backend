package edu.stonybrook.cse308.gerrybackend.utils;

import java.net.URI;

public class UriUtils {

    public static String[] getPathFragmentsFromURI(final URI uri) {
        final String path = uri.getPath();
        return path.split("/");
    }

    public static String getStatePath(final URI uri) {
        final String[] pathFragments = UriUtils.getPathFragmentsFromURI(uri);
        return pathFragments[pathFragments.length - 2];
    }

    public static String getElectionType(final URI uri) {
        final String[] pathFragments = UriUtils.getPathFragmentsFromURI(uri);
        return pathFragments[pathFragments.length - 1];
    }

}
