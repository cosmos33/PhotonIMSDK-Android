package com.cosmos.photonim.imbase.utils;

import java.util.Collection;
import java.util.Map;

public class CollectionUtils {
    public static <E> boolean isEmpty(Collection<E> collection) {
        return collection == null || collection.size() == 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }
}
