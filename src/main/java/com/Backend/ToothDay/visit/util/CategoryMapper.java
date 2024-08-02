package com.Backend.ToothDay.visit.util;

import java.util.HashMap;
import java.util.Map;

public class CategoryMapper {
    private static final Map<Integer, String> categoryMap = new HashMap<>();

    static {
        //categoryMap.put(1, "전체");
        categoryMap.put(2, "잇몸");
        categoryMap.put(3, "스케일링");
        categoryMap.put(4, "레진");
        categoryMap.put(5, "인레이");
        categoryMap.put(6, "크라운");
        categoryMap.put(7, "신경");
        categoryMap.put(8, "임플란트");
    }

    public static String getCategoryName(int categoryId) {
        return categoryMap.getOrDefault(categoryId, "전체");
    }
}
