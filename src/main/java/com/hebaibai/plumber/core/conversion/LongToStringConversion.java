package com.hebaibai.plumber.core.conversion;

/**
 * 从 long 转换为 String
 */
public class LongToStringConversion implements Conversion<String> {

    @Override
    public boolean support(Class to, Class from) {
        boolean isLong = from == long.class || from == Long.class;
        if (to == String.class && isLong) {
            return true;
        }
        return false;
    }

    @Override
    public String conversion(Object aLong) {
        return String.valueOf(aLong);
    }
}
