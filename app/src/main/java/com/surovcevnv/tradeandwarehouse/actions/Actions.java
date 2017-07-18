package com.surovcevnv.tradeandwarehouse.actions;

import com.surovcevnv.tradeandwarehouse.main.Singleton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by surovcevnv on 07.07.17.
 */

public class Actions {
        private ArrayList<Action> actions;

        public Actions() {
            actions = new ArrayList<>();
        }

        public void addAction(int code) {
            actions.add(new Action(actions.size(), code));
        }

        public void addAction(int code, String dateSt) {
            actions.add(new Action(actions.size(), code, dateSt));
        }

        public void addAction(int code, String dateSt, String dateEn) {
            actions.add(new Action(actions.size(), code, dateSt, dateEn));
        }

        public String getLastUnfinishedAction() {
            Singleton ms = Singleton.getInstance();
            String nameLast = "";
            if (actions.size()>0) {
                for (int i=0; i<actions.size(); i++) {
                    if (actions.get(i).getDtEn().equals("")) {
                        nameLast=ms.sprActions.getName(actions.get(i).getCode());
                    }
                }
            }
            return nameLast;
        }

        public ArrayList<HashMap<String, String>> getUnfinishedActionList() {
            Singleton ms = Singleton.getInstance();
            ArrayList<HashMap<String,String>> actArrList = new ArrayList<>();
            if (actions.size()>0) {
                for (int i=0; i<actions.size(); i++) {
                    if (actions.get(i).getDtEn().equals("")) {
                        HashMap<String,String> map = new HashMap<>();
                        map.put("name",ms.sprActions.getName(actions.get(i).getCode()));
                        map.put("btn","Завершить");
                        actArrList.add(map);
                    }
                }
            }
            return actArrList;
        }

        private class Action {
            private int number;
            private int code;
            private String dateSt;
            private String dateEn;

            Action(int newNumber, int initCode) {
                number = newNumber;
                code = initCode;
                dateSt = "";
                dateEn = "";
            }

            Action(int newNumber, int initCode, String initDtSt) {
                number = newNumber;
                code = initCode;
                dateSt = initDtSt;
                dateEn = "";
            }

            Action(int newNumber, int initCode, String initDtSt, String initDtEn) {
                number = newNumber;
                code = initCode;
                dateSt = initDtSt;
                dateEn = initDtEn;
            }

            public void setDtSt(String newDtSt) {
                dateSt = newDtSt;
            }

            public String getDtSt() {
                return dateSt;
            }

            public void setDtEn(String newDtEn) {
                dateEn = newDtEn;
            }

            public String getDtEn() {
                return dateEn;
            }

            public int getCode() {
                return code;
            }

            public int getNumber() {
                return number;
            }



        }
}
