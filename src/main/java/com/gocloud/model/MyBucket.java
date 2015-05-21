package com.gocloud.model;

import java.util.*;

/**
 * Created by marcofunaro on 18/05/15.
 */
public enum MyBucket {

    INSTANCE;

    private final Map<String, String> state;


    MyBucket() {
        state = new HashMap<>();
    }


    public Optional<String> getById(String id) {
        return Optional.ofNullable(state.get(id));
    }


    public Set<String> getAll(){
        Set<String> result = new HashSet<>();
        state.forEach((k, v) -> result.add(k));
        return result;
    }

    public String createEl(String value) {
        String id = UUID.randomUUID().toString();
        state.put(id, value);
        return id;
    }

    public void updateEl(String id, String value) {
        if (state.containsKey(id)){
            state.put(id, value);
        } else {
            throw new MyBucketException("Tried to update a non-existing resource");
        }
    }

    public void removeEl(String id) {
        state.remove(id);
    }


    public static final class MyMapEntry {
        private final String id;
        private final Object value;

        public MyMapEntry(String id, Object value) {
            this.id = id;
            this.value = value;
        }

        public String getId() {
            return id;
        }

        public Object getValue() {
            return value;
        }
    }
}
