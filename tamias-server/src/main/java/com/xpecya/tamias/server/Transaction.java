package com.xpecya.tamias.server;

import com.xpecya.tamias.core.Logger;

public class Transaction {

    /**
     * sometimes the transaction would be interrupted
     * by some reasons, and this method is to check and
     * finish all to-do submitted or rollbacked transactions
     */
    public static void fix() {
        if (check()) {
            Logger.info("start unfinished transaction data fix.");
            Logger.info("transaction data fix over.");
        }
    }

    /**
     * check if there is unfinished transaction
     * @return check result
     */
    public static boolean check() {
        boolean result = false;
        Logger.info("start unfinished transaction check.");
        Logger.info("transaction check finish.");
        return result;
    }
}
