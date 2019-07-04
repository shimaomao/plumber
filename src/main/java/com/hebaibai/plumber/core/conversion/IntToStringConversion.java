package com.hebaibai.plumber.core.conversion;

/**
 * 从 int 转换为 String
 */
public class IntToStringConversion implements Conversion<String> {

    @Override
    public boolean support(Class to, Class from) {
        boolean isInt = from == int.class || from == Integer.class;
        if (to == String.class && isInt) {
            return true;
        }
        return false;
    }

    @Override
    public String conversion(Object integer) {
        return String.valueOf(integer);
    }
}
