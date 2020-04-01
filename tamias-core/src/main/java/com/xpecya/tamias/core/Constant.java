package com.xpecya.tamias.core;

/** 项目常量 */
public class Constant {

    /** 协议字符串分隔符 */
    public static final String INPUT_SEPARATOR = "$";

    /** 值列表分隔符 */
    public static final String VALUE_SEPARATOR = ",";

    /**
     * get指令名
     * get指令：获取数据
     * 参数：数据键值
     * 返回值：数据值
     * 备注：
     *      1. 只能读取到用户已提交事务的数据
     *      2. 一次需要读取多个值的场合，入参可以有多个键值，用VALUE_SEPARATOR隔开
     */
    public static final String COMMAND_GET = "get";

    /**
     * set指令名
     * set指令：存储/修改数据
     * 参数：数据键值对
     * 返回值：插入/修改数据条数
     * 备注：
     *      1. 如果请求参数不带事务id，则默认新建一个事务，进行set操作，然后提交事务
     *      2. 需要一次性插入/修改多个键值对的场合，可以将入参封装为键1,值1,键2,值2...的形式 中间用VALUE_SEPARATOR分隔开
     */
    public static final String COMMAND_SET = "set";

    /**
     * del指令名
     * del指令：删除一条数据
     * 参数：数据键值
     * 返回值：删除数据条数
     * 备注：
     *      1. 如果请求参数不带事务id，则默认新建一个事务，进行del操作，然后提交事务
     *      2. 需要一次性删除多个键值对的场合，可以将入参封装为键1,值1,键2,值2...的形式 中间用VALUE_SEPARATOR分隔开
     */
    public static final String COMMAND_DEL = "del";

    /**
     * start指令名
     * start指令：开启一项事务
     * 参数：无
     * 返回值：事务id(UUID格式)
     */
    public static final String COMMAND_START = "start";

    /**
     * submit指令名
     * submit指令：提交一项事务
     * 参数：事务id
     * 返回值：成功提交的事务条数
     * 备注：
     *      1. 需要一次性提交多个事务的场合，可以传入多个事务id，用VALUE_SEPARATOR分隔开
     *      2. 事务提交失败的场合，自动回滚
     */
    public static final String COMMAND_SUBMIT = "submit";

    /**
     * rollback指令名
     * rollback指令：回滚一项事务
     * 参数：事务id
     * 返回值：成功回滚的事务条数
     * 备注：
     *      需要一次性回滚多个事务的场合，可以传入多个事务id，用VALUE_SEPARATOR分隔开
     */
    public static final String COMMAND_ROLLBACK = "rollback";

    /**
     * connect指令名
     * connect指令：获取服务器可用端口，进行连接
     */
    public static final String COMMAND_CONNECT = "connect";
}
