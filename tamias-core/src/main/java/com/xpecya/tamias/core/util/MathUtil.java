package com.xpecya.tamias.core.util;

public class MathUtil {

    private static final byte MINUS = -128;

    private MathUtil() {}

    /** 将任意int值用四个byte表示，这样写入文件的时候可以用4个字节来唯一标识一个int */
    private static byte[] parseBytes(int number) {
        long unsignedInt = (long) number + Integer.MAX_VALUE;
        long firstUnsignedByte = ((unsignedInt & 0xff000000) >> 24) + MINUS;
        long secondUnsignedByte = ((unsignedInt & 0xff0000) >> 16) + MINUS;
        long thirdUnsignedByte = ((unsignedInt & 0xff00) >> 8) + MINUS;
        long fourthUnsignedByte = (unsignedInt & 0xff) + MINUS;
        byte[] result = new byte[4];
        result[0] = (byte) firstUnsignedByte;
        result[1] = (byte) secondUnsignedByte;
        result[2] = (byte) thirdUnsignedByte;
        result[3] = (byte) fourthUnsignedByte;
        return result;
    }

    /** 将读取到的4个字节转化成一个int */
    private static int parseInt(byte[] array) {
        long first = ((long) array[0] - MINUS) << 24;
        long second = (array[1] - MINUS) << 16;
        long third = (array[2] - MINUS) << 8;
        long fourth = array[3] - MINUS;
        long longResult = first + second + third + fourth;
        longResult -= Integer.MAX_VALUE;
        return  (int) longResult;
    }
}
