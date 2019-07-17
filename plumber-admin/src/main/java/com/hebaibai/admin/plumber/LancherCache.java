package com.hebaibai.admin.plumber;

import com.hebaibai.plumber.PlumberLancher;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LancherCache {

    /**
     * key  :b_plumber表中的id
     * value:PlumberLancher 实例
     */
    public static ConcurrentMap<String, PlumberLancher> plumberLancher = new ConcurrentHashMap<>();


}
