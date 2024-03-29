package com.anki.cloneword.util;

import com.anki.cloneword.constant.LanguageType;

import java.util.Collection;
import java.util.Map;

public class CollectionUtils {

    private CollectionUtils() {
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isEmpty(Object[] array) {
        return array != null && array.length == 0;
    }
}
