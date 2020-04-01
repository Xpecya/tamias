package com.xpecya.tamias.server;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;

public class Main {

    private static final String VERSION = "Tamias Server v0.1";

    private static final String SHORT_PORT = "-p";
    private static final String FULL_PORT = "--port";
    private static final String SHORT_JSON = "-j";
    private static final String FULL_JSON = "--json";
    private static final String SHORT_VERSION = "-v";
    private static final String FULL_VERSION = "--version";

    public static void main(String[] args) {
        boolean check =
                args == null ||
                args.length == 0 ||
                getConfig(args);
        if (check) {
            // transaction data fix
            Transaction.fix();
            // start server
            Vertx vertx = Vertx.vertx();
            NetServer netServer = vertx.createNetServer();
            netServer.connectHandler(socket -> socket.handler(buffer -> {
                byte[] bytes = buffer.getBytes();
            })).listen(Config.port);
        }
    }

    /**
     * 处理入参
     * @param args 入参
     * @return 是否启动服务（包含version和help的场合，不启动）
     */
    private static boolean getConfig(String[] args) {
        boolean check = true;
        int length = args.length;
        for (int i = 0; i < length; i++) {
            switch(args[i]) {
                case SHORT_VERSION:
                case FULL_VERSION: {
                    check = false;
                    System.out.println(VERSION);
                    break;
                }
                case SHORT_JSON:
                case FULL_JSON: {
                    Config.JSON_SUPPORT = true;
                    break;
                }
                case SHORT_PORT:
                case FULL_PORT: {
                    Config.port = Integer.parseInt(args[++i]);
                    break;
                }
                default: {
                    check = false;
                    System.out.println("-p --port    set binding port");
                    System.out.println("-j --json    enable json read&write");
                    System.out.println("-v --version show version then quit");
                    System.out.println("-h --help    show this help");
                    break;
                }
            }
            if (!check) {
                break;
            }
        }
        return check;
    }
}
