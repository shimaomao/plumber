package com.hebaibai.plumber.core.conversion;

import org.junit.Test;

public class ConversionFactoryTest {

    @Test
    public void getConversionBytes2String() {
        Conversion<String> conversion = ConversionFactory.getConversion(String.class, byte[].class);
        String s = conversion.conversion("asd".getBytes());
        System.out.println(s);
    }

    @Test
    public void getConversionInt2String() {
        final Conversion<String> conversion = ConversionFactory.getConversion(String.class, int.class);
        String s = conversion.conversion(1000);
        System.out.println(s);
    }

    @Test
    public void getConversionLong2String() {
        final Conversion<String> conversion = ConversionFactory.getConversion(String.class, long.class);
        String s = conversion.conversion(1002342342342L);
        System.out.println(s);
    }
}