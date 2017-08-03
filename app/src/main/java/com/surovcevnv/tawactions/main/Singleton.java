package com.surovcevnv.tawactions.main;

/**
 * Created by surovcevnv on 06.07.17.
 */

import com.surovcevnv.tawactions.actions.Actions;
import com.surovcevnv.tawactions.actions.SprActions;


public class Singleton {
    private static Singleton instance;
    public SprActions sprActions = new SprActions();
    public Actions actions = new Actions();
    public int curActionCode = -1;
    public String serverPath = "";
    public String routeAddMove = "";
    public String routeAddOst = "";
    public String routeGetSotr = "";
    public String routeGetSprAct = "";
    public String routeGetAct = "";
    public Sotr curSotr = null;
    private Singleton (){
    }

    public static Singleton getInstance(){
        if (null == instance){
            instance = new Singleton();
        }
        return instance;
    }
}
