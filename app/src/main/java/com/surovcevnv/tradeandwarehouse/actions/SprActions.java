package com.surovcevnv.tradeandwarehouse.actions;

import android.app.Notification;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by surovcevnv on 07.07.17.
 */

public class SprActions {
    private ArrayList<Action> sprActions;

    public SprActions() {
        sprActions = new ArrayList<>();
    }

    public SprActions(String[] arrActions) {
        sprActions = new ArrayList<>();
        for (int i=0; i<arrActions.length; i++) {
            sprActions.add(new Action(i, arrActions[i]));
        }

    }

    public ArrayList<HashMap<String, String>> getArrListSprActions() {
        ArrayList<HashMap<String,String>> sprActArrList = new ArrayList<>();
        if (sprActions.size()>0) {
            for (int i=0; i<sprActions.size(); i++) {
                HashMap<String,String> map = new HashMap<>();
                map.put("name",sprActions.get(i).getName());
                map.put("code",""+sprActions.get(i).getCode());
                map.put("btn","Начать");
                sprActArrList.add(map);
            }
        }
        return sprActArrList;
    }

    public String getName(int searchCode) {
        if (sprActions.size()>0) {
            for (int i=0; i<sprActions.size(); i++) {
                if (sprActions.get(i).getCode()==searchCode) {
                    return sprActions.get(i).getName();
                }
            }
        }
        return "";
    }


    private class Action {
        private int code;
        private String name;

        Action(int initCode, String initName) {
            code = initCode;
            name = initName;
        }

        public String getName() {
            return name;
        }

        public int getCode() {
            return code;
        }

    }
}
