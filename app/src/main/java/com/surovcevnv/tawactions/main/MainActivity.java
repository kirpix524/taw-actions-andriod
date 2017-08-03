package com.surovcevnv.tawactions.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.surovcevnv.tawactions.R;
import com.surovcevnv.tawactions.lib.MDialog;
import com.surovcevnv.tawactions.logger.Logger;
import com.surovcevnv.tawactions.main.interf.Const;
import com.surovcevnv.tawactions.typeactions.TypeActionsActivity;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Singleton ms = Singleton.getInstance();
    private MDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init(getApplicationContext());
        Logger.mSaveToFile=true;
        Logger.log(Logger.Level.System, this, "Main activity created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDialog  = MDialog.getInstance(getSupportFragmentManager());

        init();
    }

    /** Обработчик кнопок **/
    public void onClick(View v) {
        EditText edit = (EditText) findViewById(R.id.pin_code);
        switch (v.getId()) {
            case R.id.btn_clear:
                edit.setText("");
                break;
            case R.id.btn_enter:
                if (edit.getText().toString().equals("")) return;
                Logger.log(Logger.Level.System, this, "Pressed enter with PK "+edit.getText().toString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        login();
                    }
                }).start();
                break;
            default:
                edit.append(v.getTag().toString());
                break;
        }
    }

    /** Проверяет данные сотрудника по пин-коду **/
    private void login() {
        // Авторизация
        EditText edit = (EditText) findViewById(R.id.pin_code);
        mDialog.showProgress(getString(R.string.dialog_wait), getString(R.string.dialog_load_data));
        String pk=edit.getText().toString();
        if ((pk.length()<4) || (pk.length()>4)) {
            mDialog.showBox("Ошибка!", "ПК должен быть длиной 4 символа!");
            return;
        }
        JSONObject config = new JSONObject();
        try {
            config.put("pk",pk);
        } catch (Exception e) {
            Logger.log(Logger.Level.System, "Server", "Error: "+e.toString());
            mDialog.showBox(getString(R.string.dialog_error),e.getMessage());
            return;
        }

        try {
            JSONObject response = Server.getResponse(ms.serverPath+ms.routeGetSotr, config);
            mDialog.showBox("response", response.toString());
            ms.curSotr = new Sotr(response.getString("name_sotr"), response.getString("id_sotr"), response.getString("id_role"), response.getString("name_dolgn"), response.getString("id_dolgn"));
        } catch (Exception e) {
            mDialog.showBox(getString(R.string.dialog_error), e.getMessage());
            return;
        }
        mDialog.hide();
        Intent intent = new Intent(MainActivity.this, TypeActionsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_uselocalip) {
            ms.serverPath =getResources().getString(R.string.serverPathLoc);
            return true;
        }

        if (id == R.id.action_quit) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected  void init() {
        //Адреса веб-сервиса
        ms.serverPath =getResources().getString(R.string.serverPath);
        ms.routeAddMove=getResources().getString(R.string.routeAddMove);
        ms.routeAddOst=getResources().getString(R.string.routeAddOst);
        ms.routeGetSotr =getResources().getString(R.string.routeGetSotr);
        ms.routeGetSprAct =getResources().getString(R.string.routeGetSprAct);
        ms.routeGetAct =getResources().getString(R.string.routeGetAct);
        //Справочники
        ms.sprActions = null;
    }
}
