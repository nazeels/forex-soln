package com.company;

import java.util.*;

public class Main {
    public static TreeMap<String, Integer> mapDecPlaces = new TreeMap<>();
    public static TreeMap<Integer, String> mapIdxToCtry = new TreeMap<>();
    public static TreeMap<String,Integer> mapCtryToIdx = new TreeMap<>();
    public static TreeMap<String, Double> pairRateMap = new TreeMap<>();
    public static String[][] crossMat;


    public static void main(String[] args) {

        //TreeMap - will save items sorted and faster retrieval
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


        Integer idx = 0;
        for (Map.Entry<String, Integer> it : mapDecPlaces.entrySet()) {
//        mapDecPlaces.keySet().forEach(it->{
            mapIdxToCtry.put(idx, it.getKey());
            mapCtryToIdx.put(it.getKey(),idx);
            idx = idx + 1;
        }

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
        crossMat = new String[countCurrency][countCurrency];
        for (int row = 0; row < countCurrency; row++) {
            for (int col = 0; col < countCurrency; col++) {
                String rowName = mapIdxToCtry.get(row);
                String colName = mapIdxToCtry.get(col);
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
        }
        for (int row = 0; row < countCurrency; row++) {
            for (int col = 0; col < countCurrency; col++) {
                if(crossMat[row][col] == null){
                    crossMat[row][col] = crossMat[col][row];
                }
            }
            System.out.print(mapIdxToCtry.get(row)+ " - ");
            System.out.print(Arrays.deepToString(crossMat[row]));
            System.out.println("\n");
        }

        calculateAndDisplayResult("AUD", 100.00, "USD");
        calculateAndDisplayResult("AUD", 100.00, "AUD");
        calculateAndDisplayResult("AUD", 100.00, "DKK");
        calculateAndDisplayResult("JPY", 100.00, "USD");


    }

    private static void calculateAndDisplayResult(String inputCurrency, double inputValue, String termCur) {
        result = inputValue;
        getResult(inputCurrency, termCur);
        String displayMsg = String.format("%s %f = %s %f",inputCurrency, inputValue, termCur, result);
        System.out.println(displayMsg);


    }

    private static double result = 0;
    private static String destCur ="";
    private static double getResult(String inputCurrency, String termCur) {
        String convFlag = crossMat[mapCtryToIdx.get(inputCurrency)][mapCtryToIdx.get(termCur)];
//        System.out.println("Conversion Type: " + convFlag);
//        double result = inputValue;
        if(convFlag.equals("D")) {
            Double termConversion = pairRateMap.get(inputCurrency + termCur);
            result = result * termConversion;
        }
        else if(convFlag.equals("Inv")){
            Double termConversion = pairRateMap.get(termCur + inputCurrency);
            result = result * (1/termConversion);
        }
        else if(convFlag.equals("1:1")){
//            re = result;
        }
        else {
            while (!destCur.equals(termCur)){
                destCur = crossMat[mapCtryToIdx.get(inputCurrency)][mapCtryToIdx.get(termCur)];
                if(destCur.length() == 1)
                    destCur = termCur;
                getResult(inputCurrency,destCur);
                inputCurrency = destCur;
            }
        }
        return result;
    }

}
