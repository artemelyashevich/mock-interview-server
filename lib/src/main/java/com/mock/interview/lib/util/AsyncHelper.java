package com.mock.interview.lib.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AsyncHelper {

    public static void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }
}
