package com.surovcevnv.tawactions.typeactions;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.surovcevnv.tawactions.R;
import com.surovcevnv.tawactions.actions.ActionsActivity;
import com.surovcevnv.tawactions.actions.SprActions;
import com.surovcevnv.tawactions.comings.ComingActivity;
import com.surovcevnv.tawactions.comings.SprNom;
import com.surovcevnv.tawactions.expenses.ExpenseActivity;
import com.surovcevnv.tawactions.main.Server;
import com.surovcevnv.tawactions.main.Singleton;
import com.surovcevnv.tawactions.remains.Remains;
import com.surovcevnv.tawactions.remains.RemainsActivity;

/**
 * Created by surovcevnv on 26.07.17.
 */

public class TypeActionsActivity extends AppCompatActivity {
    private Singleton ms = Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typeactions);
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

    private void showComings() {
        Intent intent = new Intent(TypeActionsActivity.this,ComingActivity.class);
        startActivity(intent);
    }

    private void showRemains() {
        Intent intent=new Intent(TypeActionsActivity.this, RemainsActivity.class);
        startActivity(intent);
    }

    private void showExpenses() {
        Intent intent=new Intent(TypeActionsActivity.this, ExpenseActivity.class);
        startActivity(intent);
    }

    private void showActions() {
        Intent intent=new Intent(TypeActionsActivity.this, ActionsActivity.class);
        startActivity(intent);
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
