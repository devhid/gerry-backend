package edu.stonybrook.cse308.gerrybackend.utils;

import java.util.Map;

public class MapUtils {

    public static void initMap(Map map, Object defaultValue){
        for (Object enumVal : map.keySet()){
            map.put(enumVal, defaultValue);
        }
    }
}
