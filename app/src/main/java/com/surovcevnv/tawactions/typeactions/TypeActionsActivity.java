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
import com.surovcevnv.tawactions.lib.MDialog;
import com.surovcevnv.tawactions.logger.Logger;
import com.surovcevnv.tawactions.main.Server;
import com.surovcevnv.tawactions.main.Singleton;
import com.surovcevnv.tawactions.main.Sotr;

import org.json.JSONObject;

/**
 * Created by surovcevnv on 26.07.17.
 */

public class TypeActionsActivity extends AppCompatActivity {
    private Singleton ms = Singleton.getInstance();
    private MDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typeactions);
        mDialog  = MDialog.getInstance(getSupportFragmentManager());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Создание обращения в техподдержку", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button btnActions = (Button) findViewById(R.id.btnActions);
        btnActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActions();
            }
        });
        loadActions(ms.curSotr.id_sotr);

        Button btnControl = (Button) findViewById(R.id.btnControl);
        btnControl.setVisibility(View.INVISIBLE);

        if (ms.curSotr.id_role.equals("3")) { //Интерфейс контролера
            btnControl.setVisibility(View.VISIBLE);
            btnControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showControl();
                }
            });
        }
    }

    private void showActions() {
        Intent intent=new Intent(TypeActionsActivity.this, ActionsActivity.class);
        startActivity(intent);
    }



    private void loadActions(String id_sotr) {

    }

    private void showControl() {

    }
}
