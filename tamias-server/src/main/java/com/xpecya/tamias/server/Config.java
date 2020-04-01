package com.xpecya.tamias.server;

/**
 * server运行时配置
 * 配置由配置文件及启动参数决定
 */
public class Config {

    /** 是否支持json读写 */
    public static Boolean JSON_SUPPORT = false;

    /** 客户端接入地址 */
    public static int port = 56789;

    /** 数据根目录 开发期间写死 */
    public static final String DATA_ROOT = "D:\\tamias\\data";
}
