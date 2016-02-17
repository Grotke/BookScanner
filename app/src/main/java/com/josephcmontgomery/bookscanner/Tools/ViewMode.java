package com.josephcmontgomery.bookscanner.Tools;

import java.util.ArrayList;

public class ViewMode {
    public static final int EDIT_MODE = 1;
    public static final int DETAIL_MODE = 2;
    public static final int LIST_MODE = 4;
    public static final int ADD_MODE = 8;
    public static final int DUAL_MODE = 16;


    private ViewMode(){}

    public static String convertToString(int currentMode){
        ArrayList<String> outputTokens = new ArrayList<>();
        if(currentModeIs(currentMode, EDIT_MODE)){
            outputTokens.add("EDIT");
        }
        if(currentModeIs(currentMode, DETAIL_MODE)){
            outputTokens.add("DETAIL");
        }
        if(currentModeIs(currentMode, LIST_MODE)){
            outputTokens.add("LIST");
        }
        if(currentModeIs(currentMode, ADD_MODE)){
            outputTokens.add("ADD");
        }
        if(currentModeIs(currentMode, DUAL_MODE)){
            outputTokens.add("DUAL");
        }
        String output = "";
        for(String token: outputTokens){
            if(!output.isEmpty()){
                output += " ";
            }
            output += token;
        }
        return output;
    }

    public static boolean currentModeIs(int currentMode, int modeToCheck) {
        return (currentMode & modeToCheck) != 0;
    }

    public static int removeMode(int currentMode, int modeToRemove){
        modeToRemove ^= Integer.MAX_VALUE;
        currentMode &= modeToRemove;
        return currentMode;
    }

    public static int addMode(int currentMode, int modeToAdd){
        currentMode |= modeToAdd;
        return currentMode;
    }
}
