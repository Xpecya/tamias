package com.xpecya.tamias.core;

import java.util.regex.Pattern;

import static com.xpecya.tamias.core.Constant.INPUT_SEPARATOR;
import static com.xpecya.tamias.core.Constant.VALUE_SEPARATOR;

/** 请求参数 */
public class Request {

    /** 只允许大写字符，小写字符，数字，英文句号和下划线 */
    private static final Pattern TEXT_PATTERN = Pattern.compile("[0-9a-zA-Z\\._]");

    /** 事务id */
    public String transactionId;

    /** 指令 */
    public String command;

    /** 指令参数数组 */
    public String[] params;

    /** 请求发送时间 */
    public long timeMills;

    /**
     * 文本基本格式
     * 第一个%s表示事务Id值
     * 可以省略，没有事务Id的场合如何处理取决于具体指令，但是INPUT_SEPARATOR不可省略
     * 第二个%s表示请求指令名，不可省略
     * 第三个%s表示请求参数列表，根据具体请求指令，可以省略或传多个值
     * 传多个值的场合用VALUE_SEPARATOR分隔开。
     * 省略参数列表的场合不可省略INPUT_SEPARATOR
     * 第四个%d表示请求发送时的currentTimeMills
     * 第四个参数在需要做请求性能分析时使用
     * 如果发送请求不带这个参数，返回值将不带这个参数，此时最后一个INPUT_SEPARATOR可以省略
     * 反之，如果发送请求带这个数，则返回值会包含若干性能分析用指标
     */
    private static final String FORMAT = "%s" + INPUT_SEPARATOR + "%s" + INPUT_SEPARATOR + "%s" + INPUT_SEPARATOR + "%d";

    /**
     * 将输入值解析为Request对象
     * @param input 输入字符串
     */
    public Request(String input) {
        String[] split = input.split(INPUT_SEPARATOR);
        int length = split.length;
        switch(length) {
            case 3: {
                // 不进行性能测试
                requestWithoutTimeMills(split);
                break;
            }
            case 4: {
                // 进行性能测试
                requestWithTimeMills(split);
                break;
            }
            default: {
                throw new IllegalArgumentException("input is illegal! input = " + input);
            }
        }
    }

    @Override
    public String toString() {
        if (command == null || "".equals(command)) {
            throw new IllegalStateException("command is null!");
        }
        String param = "";
        int length;
        if (params != null && (length = params.length) > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                stringBuilder.append(params[i]).append(VALUE_SEPARATOR);
            }
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(VALUE_SEPARATOR));
            param = stringBuilder.toString();
        }
        return String.format(FORMAT, transactionId == null ? "" : transactionId, command, param,
                timeMills == 0 ? "" : timeMills);
    }

    /**
     * 不进行性能测试的请求
     * @param split 输入值分割后的数组
     */
    private void requestWithoutTimeMills(String[] split) {
        if (split[1] == null || "".equals(split[0])) {
            throw new IllegalArgumentException("command is illegal! command = " + split[1]);
        }
        this.command = split[1];
        split = split[2].split(VALUE_SEPARATOR);
        int length = split.length;
        for (int i = 0; i < length; i++) {
            String singleValue = split[i];
            if (!check(singleValue)) {
                throw new IllegalArgumentException("current param is illegal! illegal param = " + singleValue);
            }
        }
        this.params = split;
        if (split[0] != null && !"".equals(split[0])) {
            this.transactionId = split[0];
        }
    }

    /**
     * 进行性能测试的请求
     * @param split 输入值分割后的数组
     */
    private void requestWithTimeMills(String[] split) {
        requestWithoutTimeMills(split);
        if (split[3] != null && !"".equals(split[3])) {
            this.timeMills = Long.parseLong(split[3]);
        }
    }

    /**
     * 对文本进行校验
     * @param text 被校验的文本
     * @return 校验结果
     */
    private boolean check(String text) {
        return text != null && TEXT_PATTERN.matcher(text).matches();
    }
}
