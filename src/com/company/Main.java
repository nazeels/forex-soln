package com.company;

import java.util.*;

public class Main {



    public static void main(String[] args) {
        System.out.println(Double.valueOf("100.00001"));

        TreeMap<String, Integer> mapDecPlaces = new TreeMap<>();
        TreeMap<String, Double> pairRateMap = new TreeMap<>();
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

        FxConverter fxConverter = new FxConverter(mapDecPlaces,pairRateMap);

        fxConverter.calculateAndDisplayResult("AUD", 100.00, "USD");
        fxConverter.calculateAndDisplayResult("AUD", 100.00, "AUD");
        fxConverter.calculateAndDisplayResult("AUD", 100.00, "DKK");
        fxConverter.calculateAndDisplayResult("JPY", 100.00, "USD");


        System.out.println("use this AUD 100.00 in USD");

        Scanner in = new Scanner(System.in);

        while (in.hasNext()) {
            String s = in.nextLine();
            StringTokenizer tokenizer = new StringTokenizer(s);
            String[] words = new String[4];
            int idx = 0;
            while (tokenizer.hasMoreTokens()){
                words[idx] = tokenizer.nextToken();
                idx++;
            }
            fxConverter.calculateAndDisplayResult(words[0],Double.valueOf(words[1]),words[3]);
        }

    }

    }


