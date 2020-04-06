package com.xpecya.tamias.server.file.model;

public class Key {

    private static final String PATTERN = "%s-%d#";

    /** 键值对的键值 */
    private String key;

    /** 数据的起始位 */
    private Long start;

    public Key() {}

    public Key(String input) {
        String[] split = input.split("-");
        int length = split.length;
        if (length != 2) {
            throw new IllegalArgumentException("input is invalid: " + input);
        }
        this.key = split[0];
        String startStr = split[1];
        if (startStr.endsWith("#")) {
            startStr = startStr.replaceAll("#", "");
        }
        this.start = Long.getLong(startStr);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    @Override
    public String toString() {
        if (key == null || "".equals(key)) {
            throw new IllegalStateException("key is empty!");
        }
        if (start == null || start < 0) {
            throw new IllegalStateException("start pointer is missing!");
        }
        return String.format(PATTERN, key, start);
    }
}
