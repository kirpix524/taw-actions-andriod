package com.surovcevnv.tawactions.actions;

import com.surovcevnv.tawactions.R;
import com.surovcevnv.tawactions.logger.Logger;
import com.surovcevnv.tawactions.main.Server;
import com.surovcevnv.tawactions.main.Sotr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public SprActions(JSONArray arrActions) throws Error{
        sprActions = new ArrayList<>();
        try {
            for (int i=0; i<arrActions.length(); i++) {
                JSONObject act = (JSONObject) arrActions.get(i);
                sprActions.add(new Action(act.getInt("id_oper"), act.getString("name_oper")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            throw new Error(e.getMessage());
        }
    }

    public ArrayList<HashMap<String, String>> getArrListSprActions() {
        ArrayList<HashMap<String,String>> sprActArrList = new ArrayList<>();
        if (sprActions.size()>0) {
            for (int i=0; i<sprActions.size(); i++) {
                HashMap<String,String> map = new HashMap<>();
                map.put("name_oper",sprActions.get(i).getName());
                map.put("id_oper",""+sprActions.get(i).getCode());
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
        private int id_oper;
        private String name_oper;


        Action(int initCode, String initName) {
            id_oper = initCode;
            name_oper = initName;
        }

        public String getName() {
            return name_oper;
        }

        public int getCode() {
            return id_oper;
        }

    }
}
