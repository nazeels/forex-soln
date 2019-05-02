package com.company;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        //TreeMap - will save items sorted and faster retrieval
        TreeMap<String, Integer> mapDecPlaces = new TreeMap<>();
        mapDecPlaces.put("AUD", 2);
        mapDecPlaces.put("CAD", 2);
        mapDecPlaces.put("CNY", 2);
        mapDecPlaces.put("CZK", 2);
        mapDecPlaces.put("DKK", 2);
        mapDecPlaces.put("EUR", 2);
        mapDecPlaces.put("GBP", 2);
        mapDecPlaces.put("JPY", 0);
        mapDecPlaces.put("NOK", 2);
        mapDecPlaces.put("NZD", 2);
        mapDecPlaces.put("USD", 2);

        TreeMap<Integer, String> mapColRowName = new TreeMap<>();
        Integer idx = 0;
        for (Map.Entry<String, Integer> it : mapDecPlaces.entrySet()) {
//        mapDecPlaces.keySet().forEach(it->{
            mapColRowName.put(idx, it.getKey());
            idx = idx + 1;
        }

        TreeMap<String, Double> pairRateMap = new TreeMap<>();
        pairRateMap.put("AUDUSD", 0.8371);
        pairRateMap.put("CADUSD", 0.8711);
        pairRateMap.put("CNYUSD", 6.1715);
        pairRateMap.put("EURUSD", 1.2315);
        pairRateMap.put("GBPUSD", 1.5683);
        pairRateMap.put("NZDUSD", 0.7750);
        pairRateMap.put("USDJPY", 119.95);
        pairRateMap.put("EURCZK", 27.6028);
        pairRateMap.put("EURDKK", 7.4405);
        pairRateMap.put("EURNOK", 8.6651);

        TreeMap<String, String> mapCurToCur = new TreeMap<>();
        pairRateMap.keySet().forEach(it -> {
            mapCurToCur.put(it.substring(0, 2), it.substring(3, 5));
        });


        // 2D- Array - cross - via matrix
        int countCurrency = mapDecPlaces.size();
        String[][] crossMat = new String[countCurrency][countCurrency];
        for (int row = 0; row < countCurrency; row++) {
            for (int col = 0; col < countCurrency; col++) {
                String rowName = mapColRowName.get(row);
                String colName = mapColRowName.get(col);
                String combined = rowName + colName;
                String combinedInv = colName + rowName;
                if (row == col) {
                    crossMat[row][col] = "1:1";
                } else if (pairRateMap.get(combined) != null) {
                    crossMat[row][col] = "D";
                } else if (pairRateMap.get(combinedInv) != null) {
                    crossMat[row][col] = "Inv";
                } else {

                    long countOfRowOccurence = pairRateMap.keySet().stream().filter(it -> it.contains(rowName)).count();
                    if (countOfRowOccurence == 1) {
                        Optional<String> first = pairRateMap.keySet().stream().filter(it -> it.contains(rowName)).findFirst();
                        crossMat[row][col] = first.get().replace(rowName, "");
                    } else {
                        crossMat[row][col] = crossMat[col][row];
                    }
                }
            }
//            System.out.println(mapColRowName.get(row));
//            System.out.println(Arrays.deepToString(crossMat[row]));
        }
        for (int row = 0; row < countCurrency; row++) {
            for (int col = 0; col < countCurrency; col++) {
                if(crossMat[row][col] == null){
                    crossMat[row][col] = crossMat[col][row];
                }
            }
            System.out.println(mapColRowName.get(row));
            System.out.println(Arrays.deepToString(crossMat[row]));
        }
    }
}
