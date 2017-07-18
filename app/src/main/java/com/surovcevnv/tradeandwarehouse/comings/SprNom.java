package com.surovcevnv.tradeandwarehouse.comings;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by surovcevnv on 06.07.17.
 */

public class SprNom {
    private ArrayList<Nomen> sprNom;

    public SprNom() {
        sprNom = new ArrayList<>();
    }

    public SprNom(String[] arrNom) {
        sprNom = new ArrayList<>();
        for (int i=0; i<arrNom.length; i++) {
            sprNom.add(new Nomen(i, arrNom[i]));
        }

    }

    public void addNomen(String name) {
        int newcode=sprNom.size();
        sprNom.add(new Nomen(newcode, name));
        return;
    }

    public void addNomen(int code, String name) {
        sprNom.add(new Nomen(code, name));
        return;
    }

    public String getName(int code) {
        for (int i=0; i<sprNom.size(); i++) {
            if (sprNom.get(i).getCode()==code) {
                return sprNom.get(i).getName();
            }
        }
        return "<элемент "+code+" не найден>";
    }

    public ArrayList<HashMap<String, String>> getArrListSprNom() {
        ArrayList<HashMap<String,String>> sprNomArrList = new ArrayList<HashMap<String,String>>();
        if (sprNom.size()>0) {
            for (int i=0; i<sprNom.size(); i++) {
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("name",sprNom.get(i).getName());
                map.put("code",""+sprNom.get(i).getCode());
                sprNomArrList.add(map);
            }
        }
        return sprNomArrList;
    }

    @Override
    public String toString() {
        String outStr="";
        for (int i=0; i<sprNom.size(); i++) {
            if (outStr.equals("")) {
                outStr = sprNom.get(i).getCode()+":"+sprNom.get(i).getName();
            } else {
                outStr = outStr+", "+sprNom.get(i).getCode()+":"+sprNom.get(i).getName();
            }
        }
        return "["+outStr+"]";
    }

    private class Nomen {
        private int code;
        private String name;

        Nomen(int initCode, String initName) {
            code = initCode;
            name = initName;
        }

        public int getCode() {
            return this.code;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String newName) {
            this.name = newName;
        }
    }

}
