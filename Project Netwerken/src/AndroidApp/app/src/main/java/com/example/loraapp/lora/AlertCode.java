package com.example.loraapp.lora;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toby_ on 22-3-2018.
 */

public class AlertCode {
    private int code, color;
    private String description;

    public AlertCode(int code, int color, String description) {
        this.code = code;
        this.color = color;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public int getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get the default alarmCodes
     * @return a list with the default alarmcodes
     */
    public static List<AlertCode> getAlertCodes() {
        List<AlertCode> alertCodes = new ArrayList<>();
        alertCodes.add(new AlertCode(10, Color.RED, "SOS"));
        alertCodes.add(new AlertCode(11, Color.RED, "Help"));
        alertCodes.add(new AlertCode(12, Color.CYAN, "Intel"));
        alertCodes.add(new AlertCode(13, Color.GRAY, "Activity"));
        alertCodes.add(new AlertCode(14, Color.RED, "Incident"));
        alertCodes.add(new AlertCode(15, Color.LTGRAY, "Command"));
        alertCodes.add(new AlertCode(16, Color.GREEN, "Status"));
        alertCodes.add(new AlertCode(17, Color.MAGENTA, "Request"));
        alertCodes.add(new AlertCode(99, Color.YELLOW, "Delete alarm"));

        return alertCodes;
    }
}
