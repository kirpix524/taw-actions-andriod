package com.surovcevnv.tradeandwarehouse.main;

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
import android.widget.Toast;

import com.surovcevnv.tradeandwarehouse.R;
import com.surovcevnv.tradeandwarehouse.actions.ActionsActivity;
import com.surovcevnv.tradeandwarehouse.actions.SprActions;
import com.surovcevnv.tradeandwarehouse.comings.ComingActivity;
import com.surovcevnv.tradeandwarehouse.expenses.ExpenseActivity;
import com.surovcevnv.tradeandwarehouse.logger.Logger;
import com.surovcevnv.tradeandwarehouse.remains.RemainsActivity;
import com.surovcevnv.tradeandwarehouse.remains.Remains;
import com.surovcevnv.tradeandwarehouse.comings.SprNom;

public class MainActivity extends AppCompatActivity {
    private Singleton ms = Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init(getApplicationContext());
        Logger.mSaveToFile=true;
        Logger.log(Logger.Level.System, this, "Main activity created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Создание обращения в техподдержку", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        loadSprNom();
        loadRemains();
        loadSprActions();
        init();

        Button btnComings = (Button) findViewById(R.id.btnComings);
        btnComings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComings();
            }
        });

        Button btnRemains = (Button) findViewById(R.id.btnRemaining);
        btnRemains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRemains();
            }
        });

        Button btnExpenses = (Button) findViewById(R.id.btnExpenses);
        btnExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExpenses();
            }
        });

        Button btnActions = (Button) findViewById(R.id.btnActions);
        btnActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActions();
            }
        });

        Button btnInit = (Button) findViewById(R.id.btnInit);
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Server.sendInit();
            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Настройка параметров работы!", Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }

        if (id == R.id.action_uselocalip) {
            ms.serverPath =getResources().getString(R.string.serverPathLoc);
            Server.sendInit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showComings() {
        Intent intent = new Intent(MainActivity.this,ComingActivity.class);
        startActivity(intent);
    }

    private void showRemains() {
        Intent intent=new Intent(MainActivity.this, RemainsActivity.class);
        startActivity(intent);
    }

    private void showExpenses() {
        Intent intent=new Intent(MainActivity.this, ExpenseActivity.class);
        startActivity(intent);
    }

    private void showActions() {
        Intent intent=new Intent(MainActivity.this, ActionsActivity.class);
        startActivity(intent);
    }

    protected  void init() {
        ms.serverPath =getResources().getString(R.string.serverPath);
        ms.routeAddMove=getResources().getString(R.string.routeAddMove);
        ms.routeAddOst=getResources().getString(R.string.routeAddOst);
        ms.routeInit=getResources().getString(R.string.routeInit);
        Server.sendInit();
    }
    protected void loadSprNom() { //Получение справочника номенклатуры
        ms.sprNom = new SprNom(getResources().getStringArray(R.array.spr_nom));
    }

    private void loadRemains() {
        ms.remains = new Remains();
    }

    private void loadSprActions() {
        ms.sprActions = new SprActions(getResources().getStringArray(R.array.spr_act));
    }

    private void loadActions() {

    }
}
