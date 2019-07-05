package com.example.myapplication.Tab2;

import java.util.Comparator;
import java.util.HashMap;

// 앨범 정렬을 위한 클래스
public class TabFragment2_MapComparator implements Comparator<HashMap<String, String>> {
        private final String key;
        private final String order;

        public TabFragment2_MapComparator(String key, String order) {
                this.key = key;                                                                     // 정렬 기준
                this.order = order;                                                                 // 오름차순, 내림차순
        }

        public int compare(HashMap<String, String> first, HashMap<String, String> second) {
        // TODO: Null checking, both for maps and values
                String firstValue = first.get(key);
                String secondValue = second.get(key);

                if(this.order.toLowerCase().contentEquals("asc")) {
                        return firstValue.compareTo(secondValue);
                }
                else{
                        return secondValue.compareTo(firstValue);
                }

        }
}