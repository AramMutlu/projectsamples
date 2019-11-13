package com.example.loraapp.lora;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class LoraException extends Exception {

    public LoraException() {
    }

    public LoraException(String message) {
        super(message);
    }

    public LoraException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoraException(Throwable cause) {
        super(cause);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    public LoraException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
