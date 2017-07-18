package com.surovcevnv.tawactions.remains;

/**
 * Created by surovcevnv on 04.07.17.
 */

import com.surovcevnv.tawactions.main.Server;
import com.surovcevnv.tawactions.main.Singleton;

import java.util.ArrayList;
import java.util.HashMap;


public class Remains {
    private ArrayList<Remain> remains;
    public Remains() {
        remains = new ArrayList<Remain>();
    }

    public void addRemain(int code, double quantity) {
        Singleton ms = Singleton.getInstance();
        for (int i=0; i<remains.size(); i++) {
            if (remains.get(i).getCode()==code) {
                remains.get(i).addQuant(quantity);
                Server.sendRemain(code, remains.get(i).getQuant());
                Server.sendMove("com", code, quantity, 0);
                return;
            }
        }
        remains.add(new Remain(code, quantity));
        Server.sendRemain(code, quantity);
        Server.sendMove("com", code, quantity, 0);
        return;
    }

    public void delRemain(int code, double quantity) {
        Singleton ms = Singleton.getInstance();
        for (int i=0; i<remains.size(); i++) {
            if (remains.get(i).getCode()==code) {
                remains.get(i).delQuant(quantity);
                Server.sendRemain(code, remains.get(i).getQuant());
                Server.sendMove("exp", code, quantity, 0);
                if ( remains.get(i).getQuant()==0) {
                    remains.remove(i);
                }
                return;
            }
        }
        return;
    }

    public double getRemain(int code) {
        for (int i=0; i<remains.size(); i++) {
            if (remains.get(i).getCode()==code) {
                return remains.get(i).getQuant();
            }
        }
        return 0;
    }

    public ArrayList<HashMap<String, String>> getArrListRemains() {
        Singleton ms = Singleton.getInstance();
        ArrayList<HashMap<String,String>> remainsArrList = new ArrayList<HashMap<String,String>>();
        if (remains.size()>0) {
            for (int i=0; i<remains.size(); i++) {
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("name",ms.sprNom.getName(remains.get(i).getCode()));
                map.put("code",""+remains.get(i).getCode());
                map.put("quant",""+remains.get(i).getQuant());
                remainsArrList.add(map);
            }
        }
        return remainsArrList;
    }

    @Override
    public String toString() {
        String outStr="";
        for (int i=0; i<remains.size(); i++) {
            if (outStr.equals("")) {
                outStr = remains.get(i).getCode()+":"+remains.get(i).getQuant();
            } else {
                outStr = outStr+", "+remains.get(i).getCode()+":"+remains.get(i).getQuant();
            }
        }
        return "["+outStr+"]";
    }

    private class Remain {
        private int code;
        private double quantity;

        Remain(int initCode) {
            code = initCode;
            quantity = 0;
        }

        Remain(int initCode, double initQuantity) {
            code = initCode;
            quantity = initQuantity;
        }

        public int getCode() {
            return this.code;
        }

        public double getQuant() {
            return this.quantity;
        }

        public void setQuant(double newQuantity) {
            this.quantity = newQuantity;
        }

        public void addQuant(double quantToAdd) {
            this.quantity = this.quantity+quantToAdd;
        }
        public void delQuant(double quantToDel) {
            this.quantity = this.quantity-quantToDel;
            if (this.quantity<0) {
                this.quantity=0;
            }
        }
    }

}
