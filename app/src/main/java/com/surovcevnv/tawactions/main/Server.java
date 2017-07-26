package com.surovcevnv.tawactions.main;

import com.surovcevnv.tawactions.logger.Logger;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by surovcevnv on 10.07.17.
 */

public class Server {
    public static void sendInit() {
        final Singleton ms = Singleton.getInstance();
        Logger.log(Logger.Level.System, "Server", "Start sending init to server");
        final JSONObject config = new JSONObject();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendData(ms.serverPath+ms.routeInit, config);
            }
        }).start();


    }

    public static void sendRemain(int code, double quant) {
        final Singleton ms = Singleton.getInstance();
        Logger.log(Logger.Level.System, "Server", "Start sending remains to server");
        final JSONObject config = new JSONObject();
        try {
            config.put("codeNom",code);
            config.put("curOst",quant);
        } catch (Exception e) {
            Logger.log(Logger.Level.System, "Server", "Error: "+e.toString());
            return;
            //            Toast.makeText(Singleton.this, "Ввели больше, чем есть на остатках",
//                    Toast.LENGTH_LONG).show();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
//                sendData("http://92.53.120.131:8888/addOst/", config);
                sendData(ms.serverPath+ms.routeAddOst, config);
            }
        }).start();


    }

    public static void sendMove(String typeMove, int codeNom, Double quant, int codeAct) {
        final Singleton ms = Singleton.getInstance();
        Logger.log(Logger.Level.System, "Server", "Start sending move: "+typeMove+" "+codeNom+" "+quant+" "+codeAct);
        final JSONObject config = new JSONObject();
        try {
            config.put("typeMove",typeMove);
            if (typeMove.equals("com")||(typeMove.equals("exp"))) {
                config.put("quant",quant);
                config.put("codeNom",codeNom);
            } else {
                config.put("codeAct",codeAct);
            }
        } catch (Exception e) {
            Logger.log(Logger.Level.System, "Server", "Error: "+e.toString());
            return;
            //            Toast.makeText(Singleton.this, "Ввели больше, чем есть на остатках",
//                    Toast.LENGTH_LONG).show();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendData(ms.serverPath+ms.routeAddMove, config);
            }
        }).start();
    }

    public static JSONObject getResponse(String path, JSONObject config) {
        return sendData(path, config);
    }

    private static JSONObject sendData(String path, JSONObject config) {
        Logger.log(Logger.Level.System, "Server", "Start sending data, path="+path);
        URL url = null;
        BufferedReader reader=null;
        String resultJson="";
        JSONObject dataJsonObj = null;
        try {
            url = new URL(path);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("POST");
            c.setReadTimeout(10000);
            c.setDoOutput(true);
            c.setDoInput(true);

            DataOutputStream wr = new DataOutputStream(c.getOutputStream());
            wr.writeBytes("req="+config.toString());
            wr.flush();
            wr.close();

            reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder buf=new StringBuilder();
            String line=null;
            while ((line=reader.readLine()) != null) {
                buf.append(line + "\n");
            }
            resultJson = buf.toString();
            dataJsonObj = new JSONObject(resultJson);
        } catch (Exception e) {
            Logger.log(Logger.Level.System, "Server", "Error: "+e.toString());
        }
        return dataJsonObj;
    }
}
