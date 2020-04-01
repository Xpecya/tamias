package com.xpecya.tamias.core;

import static com.xpecya.tamias.core.Constant.INPUT_SEPARATOR;

/** 请求返回值 */
public class Response {

    /**
     * %d固定为一个数字
     * 请求成功返回0 失败返回1
     * %s为value值
     */
    private static final String SIMPLE_RETURN_FORMAT = "%d%s";

    /**
     * 在请求值包含请求时间的场合返回此模板数据
     * 第一个参数为SIMPLE_RETURN_FORMAT的返回值
     * 第二个参数为receiveTime
     * 第三个参数为startFileExecutionTime
     * 第四个参数为endFileExecutionTime
     * 第五个参数为endTime 处理完毕的时间，由toString方法中生成并注入
     */
    private static final String FULL_FORMAT = "%s" + INPUT_SEPARATOR + "%d" + INPUT_SEPARATOR + "%d" + INPUT_SEPARATOR + "%d" + INPUT_SEPARATOR + "%d";

    /** 请求结果 */
    public boolean status;

    /**
     * 请求返回值
     * 请求失败的场合，为失败信息
     */
    public String value;

    /**********以下数据只在请求参数包含发送时间的条件下才返回**********/

    /** 接收到请求的时间 */
    public long receiveTime;

    /**
     * 开始文件IO的时间
     * 一个请求需要多个文件IO的场合，取第一个文件的开始时间
     */
    public long startFileExecutionTime;

    /**
     * 结束文件IO的时间
     * 一个请求需要多个文件IO的场合，取最后一个文件的结束时间
     */
    public long endFileExecutionTime;

    /**
     * 结束文件IO的时间
     * 一个请求需要多个文件IO的场合，取最后一个文件的结束时间
     */
    public long endTime;

    /** 无参构造函数 */
    public Response() {}

    /** 客户端解析response使用 */
    public Response(String input) {
        if (input.contains(INPUT_SEPARATOR)) {
            String[] split = input.split(INPUT_SEPARATOR);
            int length = split.length;
            if (length != 5) {
                throw new IllegalArgumentException("input is illegal! input = " + input);
            }
            getStatusAndValue(split[0]);
            this.receiveTime = Long.parseLong(split[1]);
            this.startFileExecutionTime = Long.parseLong(split[2]);
            this.endFileExecutionTime = Long.parseLong(split[3]);
            this.endTime = Long.parseLong(split[4]);
        } else {
            getStatusAndValue(input);
        }
    }

    @Override
    public String toString() {
        String result = String.format(SIMPLE_RETURN_FORMAT, status ? 0 : 1, value);
        if (receiveTime != 0 || startFileExecutionTime != 0 || endFileExecutionTime != 0) {
            result = String.format(
                    FULL_FORMAT, result,
                    receiveTime == 0 ? "" : receiveTime,
                    startFileExecutionTime == 0 ? "" : startFileExecutionTime,
                    endFileExecutionTime == 0 ? "" : endFileExecutionTime,
                    System.currentTimeMillis());
        }
        return result;
    }

    private void getStatusAndValue(String input) {
        char statusChar = input.charAt(0);
        switch(statusChar) {
            case '0': {
                this.status = true;
                break;
            }
            case '1': {
                this.status = false;
                break;
            }
            default: {
                throw new IllegalArgumentException("status is illegal! status = " + statusChar);
            }
        }
        this.value = input.substring(1);
    }
}
