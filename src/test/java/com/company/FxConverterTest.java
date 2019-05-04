package com.company;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.TreeMap;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "com.company.FxConverter")
public class FxConverterTest {
    FxConverter fxConverter;

    @org.junit.Before
    public void setUp() throws Exception {
        TreeMap<String, Integer> mapDecPlaces = new TreeMap<>();
        TreeMap<String, Double> pairRateMap = new TreeMap<>();
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

        fxConverter = new FxConverter(mapDecPlaces, pairRateMap);

    }

    @org.junit.Test
    public void calculateAndDisplayResult_direct_conversion() throws Exception {

        String result = fxConverter.calculateAndDisplayResult("AUD", 100.00, "USD");
        Assert.assertEquals("AUD 100.00 = USD 83.71", result);
    }

    @org.junit.Test
    public void calculateAndDisplayResult_1_isto_1() throws Exception {

        String result = fxConverter.calculateAndDisplayResult("AUD", 100.00, "AUD");
        Assert.assertEquals("AUD 100.00 = AUD 100.00", result);
    }

    @org.junit.Test
    public void calculateAndDisplayResult_chained_conversion() throws Exception {
        String result = fxConverter.calculateAndDisplayResult("AUD", 100.00, "DKK");
        Assert.assertEquals("AUD 100.00 = DKK 505.76", result);
    }

    @org.junit.Test
    public void calculateAndDisplayResult_for_jpy() throws Exception {
        String result = fxConverter.calculateAndDisplayResult("JPY", 100.00, "USD");
        Assert.assertEquals("JPY 100 = USD 0.83", result);
    }


    @org.junit.Test
    public void getResultFormattedString_double_decimal() throws Exception {
        String result = Whitebox.invokeMethod(fxConverter, "getResultFormattedString", "AUD", "USD");
        Assert.assertEquals("%s %.2f = %s %.2f", result);
    }

    @org.junit.Test
    public void getResultFormattedString_zero_decimal() throws Exception {
        String result = Whitebox.invokeMethod(fxConverter, "getResultFormattedString", "AUD", "JPY");
        Assert.assertEquals("%s %.2f = %s %.0f", result);
    }

}