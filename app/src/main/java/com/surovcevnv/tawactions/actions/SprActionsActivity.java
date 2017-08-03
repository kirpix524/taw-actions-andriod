package com.surovcevnv.tawactions.actions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.surovcevnv.tawactions.R;
import com.surovcevnv.tawactions.main.Server;
import com.surovcevnv.tawactions.main.Singleton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by surovcevnv on 07.07.17.
 */

public class SprActionsActivity extends FragmentActivity {
    private Singleton ms = Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spr_actions);

        ListView listViewActions = (ListView)findViewById(R.id.listSprActions);
        final ArrayList<HashMap<String, String>> sprActions = ms.sprActions.getArrListSprActions();
        SimpleAdapter adapterActions = new SimpleAdapter(this, sprActions, R.layout.item_actions,
                new String[]{"name_oper"},
                new int[] {R.id.textActionName});
        listViewActions.setAdapter(adapterActions);
        listViewActions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, final int position,
                                    long id) {
                ms.curActionCode=Integer.parseInt(sprActions.get(position).get("id_oper"));
//                Server.sendMove("start", 0, 0.0, ms.curActionCode);
                Intent intent=new Intent(SprActionsActivity.this, ActionsActivity.class);
                startActivity(intent);
                SprActionsActivity.this.finish();

            }
        });
    }
}
