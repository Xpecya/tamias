package com.xpecya.tamias.server.file.model;

public class Data {

    public static final int DATA_LENGTH = 130;

    /** 固定2位 */
    private DataType type;
    /** 固定128位 不足用*填充 */
    private String data;

    public Data(String input) {
        if (input.length() != DATA_LENGTH) {
            throw new IllegalArgumentException("data not qualified: " + input);
        }
        String type = input.substring(0, 2);
        if ("SH".equals(type)) {
            this.type = DataType.SHORT;
        } else if ("LG".equals(type)) {
            this.type = DataType.LONG;
        } else {
            throw new IllegalArgumentException("not supported data type: " + type);
        }
        this.data = input.substring(2);
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return type + data;
    }

    public enum DataType {
        SHORT("SH"),
        LONG("LG");

        private String name;

        DataType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
