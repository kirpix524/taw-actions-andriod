package com.surovcevnv.tawactions.actions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.surovcevnv.tawactions.R;
import com.surovcevnv.tawactions.main.Server;
import com.surovcevnv.tawactions.main.Singleton;

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
