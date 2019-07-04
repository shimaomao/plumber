package com.hebaibai.plumber.core.utils;

import com.github.shyiko.mysql.binlog.event.*;
import com.hebaibai.plumber.core.conversion.Conversion;
import com.hebaibai.plumber.core.conversion.ConversionFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author hjx
 */
@SuppressWarnings("ALL")
public class EventDataUtils {


    public static Long getTableId(EventData eventData) {
        DeleteRowsEventData deleteRowsEventData = getDeleteRowsEventData(eventData);
        if (deleteRowsEventData != null) {
            return deleteRowsEventData.getTableId();
        }
        UpdateRowsEventData updateRowsEventData = getUpdateRowsEventData(eventData);
        if (updateRowsEventData != null) {
            return updateRowsEventData.getTableId();
        }
        WriteRowsEventData writeRowsEventData = getWriteRowsEventData(eventData);
        if (writeRowsEventData != null) {
            return writeRowsEventData.getTableId();
        }
        TableMapEventData tableMapEventData = getTableMapEventData(eventData);
        if (tableMapEventData != null) {
            return tableMapEventData.getTableId();
        }
        return null;
    }

    public static WriteRowsEventData getWriteRowsEventData(EventData eventData) {
        if (eventData instanceof WriteRowsEventData) {
            return (WriteRowsEventData) eventData;
        }
        return null;
    }

    public static DeleteRowsEventData getDeleteRowsEventData(EventData eventData) {
        if (eventData instanceof DeleteRowsEventData) {
            return (DeleteRowsEventData) eventData;
        }
        return null;
    }

    public static UpdateRowsEventData getUpdateRowsEventData(EventData eventData) {
        if (eventData instanceof UpdateRowsEventData) {
            return (UpdateRowsEventData) eventData;
        }
        return null;
    }

    public static TableMapEventData getTableMapEventData(EventData eventData) {
        if (eventData instanceof TableMapEventData) {
            return (TableMapEventData) eventData;
        }
        return null;
    }

    public static String[] getBeforUpdate(EventData eventData) {
        UpdateRowsEventData updateRowsEventData = getUpdateRowsEventData(eventData);
        if (updateRowsEventData == null) {
            return null;
        }
        List<Map.Entry<Serializable[], Serializable[]>> rows = updateRowsEventData.getRows();
        if (rows.size() == 0) {
            return null;
        }
        Map.Entry<Serializable[], Serializable[]> entry = rows.get(0);
        //key befor
        return values(entry.getKey());
    }

    public static String[] getAfterUpdate(EventData eventData) {
        UpdateRowsEventData updateRowsEventData = getUpdateRowsEventData(eventData);
        if (updateRowsEventData == null) {
            return null;
        }
        List<Map.Entry<Serializable[], Serializable[]>> rows = updateRowsEventData.getRows();
        if (rows.size() == 0) {
            return null;
        }
        Map.Entry<Serializable[], Serializable[]> entry = rows.get(0);
        //value after
        return values(entry.getValue());
    }

    private static String[] values(Serializable[] values) {
        String[] val = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            Serializable v = values[i];
            if (v == null) {
                val[i] = null;
                continue;
            }
            Conversion<String> conversion = ConversionFactory.getConversion(String.class, v.getClass());
            val[i] = conversion.conversion(v);
        }
        return val;
    }
}
