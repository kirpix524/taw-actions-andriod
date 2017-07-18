package com.surovcevnv.tradeandwarehouse.actions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.surovcevnv.tradeandwarehouse.R;
import com.surovcevnv.tradeandwarehouse.main.MainActivity;
import com.surovcevnv.tradeandwarehouse.main.Server;
import com.surovcevnv.tradeandwarehouse.main.Singleton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by surovcevnv on 07.07.17.
 */

public class ActionsActivity extends FragmentActivity {
    private Singleton ms = Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_actions);
        refresh();
    }

    private void refresh() {
        TextView header = (TextView) findViewById(R.id.headerActions);
        Button btnStart = (Button) findViewById(R.id.btnStartAction);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAction();
            }
        });
        Button btnEnd = (Button) findViewById(R.id.btnEndAction);
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endAction();
            }
        });
        if (ms.curActionCode==-1) {
            header.setText("В данный момент не выполняется действий");
            btnStart.setVisibility(View.VISIBLE);
            btnEnd.setVisibility(View.INVISIBLE);
        } else {
            header.setText("В данный момент выполняется "+ms.sprActions.getName(ms.curActionCode));
            btnStart.setVisibility(View.INVISIBLE);
            btnEnd.setVisibility(View.VISIBLE);
        }
    }

    private void startAction() {
        Intent intent=new Intent(ActionsActivity.this, SprActionsActivity.class);
        startActivity(intent);
        ActionsActivity.this.finish();
    }

    private void endAction() {
        Server.sendMove("end", 0, 0.0, ms.curActionCode);
        ms.curActionCode=-1;
        refresh();
    }
}
