package com.company;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class FxConverter {
    public static final String CONVERSION_DIRECT = "D";
    public static final String CONVERSION_INV = "Inv";
    public static final String CONVERSION_ONE_TO_ONE = "1:1";
    TreeMap<String, Integer> mapDecPlaces;
    TreeMap<String, Double> pairRateMap;
    TreeMap<Integer, String> mapIdxToCtry = new TreeMap<>();
    TreeMap<String, Integer> mapCtryToIdx = new TreeMap<>();
    String[][] crossMat;
    private double result = 0;
    private String destCur = "";

    public FxConverter(TreeMap<String, Integer> mapDecPlaces, TreeMap<String, Double> pairRateMap) {
        this.mapDecPlaces = mapDecPlaces;
        this.pairRateMap = pairRateMap;
        createCrossMatrix(mapDecPlaces, pairRateMap);
    }

    public void calculateAndDisplayResult(String inputCurrency, Double inputValue, String termCur) {
        result = inputValue;
        try {
            getResult(inputCurrency, termCur);
            String displayMsg = String.format(getResultFormattedString(inputCurrency, termCur), inputCurrency, inputValue, termCur, result);
            System.out.println(displayMsg);
        } catch (Exception e) {
            throw new ForExException(String.format("Unable to find rate for %s/%s", inputCurrency, termCur), e);
        }
    }

    private String getResultFormattedString(String inputCurrency, String termCur) {
        Integer input = this.mapDecPlaces.get(inputCurrency);
        Integer output = this.mapDecPlaces.get(termCur);
        return "%s %." + input + "f = %s %." + output + "f";
    }

    private void createCrossMatrix(TreeMap<String, Integer> mapDecPlaces, TreeMap<String, Double> pairRateMap) {
        Integer idx = 0;

        for (Map.Entry<String, Integer> it : mapDecPlaces.entrySet()) {
            mapIdxToCtry.put(idx, it.getKey());
            mapCtryToIdx.put(it.getKey(), idx);
            idx = idx + 1;
        }

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
                    crossMat[row][col] = CONVERSION_ONE_TO_ONE;
                } else if (pairRateMap.get(combined) != null) {
                    crossMat[row][col] = CONVERSION_DIRECT;
                } else if (pairRateMap.get(combinedInv) != null) {
                    crossMat[row][col] = CONVERSION_INV;
                } else {

                    long countOfRowOccurence = pairRateMap.keySet().stream().filter(it -> it.contains(rowName)).count();
                    if (countOfRowOccurence == 1) {
                        Optional<String> first = pairRateMap.keySet().stream().filter(it -> it.contains(rowName)).findFirst();
                        crossMat[row][col] = first.get().replace(rowName, "");
                    } else {
                        String tmpColName = mapIdxToCtry.get(col);
                        long tmpCount = pairRateMap.keySet().stream().filter(it -> it.contains(tmpColName)).count();
                        assert 1 < tmpCount : "Not Possible";
                        Optional<String> first = pairRateMap.keySet().stream().filter(it -> it.contains(tmpColName)).findFirst();
                        crossMat[row][col] = first.get().replace(tmpColName, "");
                    }
                }
            }
            System.out.print(mapIdxToCtry.get(row) + " - ");
            System.out.print(Arrays.deepToString(crossMat[row]));
            System.out.println("\n");
        }
    }


    private void getResult(String inputCurrency, String termCur) throws ForExException {

        String convFlag = crossMat[mapCtryToIdx.get(inputCurrency)][mapCtryToIdx.get(termCur)];
        if (convFlag.equals(CONVERSION_DIRECT)) {
            Double termConversion = pairRateMap.get(inputCurrency + termCur);
            result = result * termConversion;
        } else if (convFlag.equals(CONVERSION_INV)) {
            Double termConversion = pairRateMap.get(termCur + inputCurrency);
            result = result * (1 / termConversion);
        } else if (convFlag.equals(CONVERSION_ONE_TO_ONE)) {
            //noop
        } else {
            while (!destCur.equals(termCur)) {
                destCur = crossMat[mapCtryToIdx.get(inputCurrency)][mapCtryToIdx.get(termCur)];
                if (destCur.length() == 1)
                    destCur = termCur;
                getResult(inputCurrency, destCur);
                inputCurrency = destCur;
            }
        }

    }
}
