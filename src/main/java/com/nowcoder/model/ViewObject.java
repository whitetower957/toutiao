package com.nowcoder.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by rainday on 16/6/30.
 */
//专门用来给试图展示的对象
public class ViewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();
    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
