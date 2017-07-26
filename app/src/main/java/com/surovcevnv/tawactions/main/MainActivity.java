package com.surovcevnv.tawactions.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.surovcevnv.tawactions.R;
import com.surovcevnv.tawactions.actions.ActionsActivity;
import com.surovcevnv.tawactions.actions.SprActions;
import com.surovcevnv.tawactions.comings.ComingActivity;
import com.surovcevnv.tawactions.expenses.ExpenseActivity;
import com.surovcevnv.tawactions.lib.MDialog;
import com.surovcevnv.tawactions.logger.Logger;
import com.surovcevnv.tawactions.main.interf.Const;
import com.surovcevnv.tawactions.remains.RemainsActivity;
import com.surovcevnv.tawactions.remains.Remains;
import com.surovcevnv.tawactions.comings.SprNom;
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
        Sotr sotr;
        mDialog.showProgress(getString(R.string.dialog_wait), getString(R.string.dialog_load_data));
        JSONObject config = new JSONObject();
        try {
            config.put("pk",edit.getText().toString());
        } catch (Exception e) {
            Logger.log(Logger.Level.System, "Server", "Error: "+e.toString());
            mDialog.showBox(getString(R.string.dialog_error),e.getMessage());
            return;
        }

        try {
            JSONObject response = Server.getResponse(ms.serverPath+ms.routeLogin, config);
            sotr = new Sotr(response.getString("name_sotr"), response.getString("id_sotr"), response.getString("id_role"),response.getString("name_dolgn"));
        } catch (Exception e) {
            mDialog.showBox(getString(R.string.dialog_error), e.getMessage());
            return;
        }
        mDialog.hide();
        Intent intent = new Intent(MainActivity.this, TypeActionsActivity.class);
        intent.putExtra(Const.EXTRA_SOTR, sotr);
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
            Server.sendInit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected  void init() {
        ms.serverPath =getResources().getString(R.string.serverPath);
        ms.routeAddMove=getResources().getString(R.string.routeAddMove);
        ms.routeAddOst=getResources().getString(R.string.routeAddOst);
        ms.routeInit=getResources().getString(R.string.routeInit);
        Server.sendInit();
    }
}
