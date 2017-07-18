package com.surovcevnv.tradeandwarehouse.main;

/**
 * Created by surovcevnv on 06.07.17.
 */

import android.widget.Toast;

import com.surovcevnv.tradeandwarehouse.actions.Actions;
import com.surovcevnv.tradeandwarehouse.actions.SprActions;
import com.surovcevnv.tradeandwarehouse.remains.Remains;
import com.surovcevnv.tradeandwarehouse.comings.SprNom;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.surovcevnv.tradeandwarehouse.logger.Logger;


public class Singleton {
    private static Singleton instance;
    public Remains remains = new Remains();
    public SprNom sprNom = new SprNom();
    public SprActions sprActions = new SprActions();
    public Actions actions = new Actions();
    public int curActionCode = -1;
    public String serverPath = "";
    public String routeAddMove = "";
    public String routeAddOst = "";
    public String routeInit = "";
    private Singleton (){
    }

    public static Singleton getInstance(){
        if (null == instance){
            instance = new Singleton();
        }
        return instance;
    }
}
