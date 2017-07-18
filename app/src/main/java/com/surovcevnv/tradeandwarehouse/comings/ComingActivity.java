package com.surovcevnv.tradeandwarehouse.comings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.surovcevnv.tradeandwarehouse.R;
import com.surovcevnv.tradeandwarehouse.main.Singleton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by surovcevnv on 30.06.17.
 */

public class ComingActivity extends FragmentActivity {
    private Singleton ms = Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_coming);

//        Toast.makeText(getApplicationContext(), ms.remains.toString(),
//                Toast.LENGTH_LONG).show();
        ListView listViewNomen = (ListView)findViewById(R.id.listNom);
        final ArrayList<HashMap<String, String>> sprNom = ms.sprNom.getArrListSprNom();
        SimpleAdapter adapterNomen = new SimpleAdapter(this, sprNom, R.layout.item_spr_nom,
                new String[]{"name"},
                new int[] {R.id.textNom});
        listViewNomen.setAdapter(adapterNomen);
        listViewNomen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, final int position,
                                    long id) {
                Context context = ComingActivity.this;
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.input_prompts, parent, false);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        if (userInput.getText().toString().equals("")) {
                                            return;
                                        }
                                        ms.remains.addRemain(Integer.parseInt(sprNom.get(position).get("code")), Float.parseFloat(userInput.getText().toString()));
                                        Toast.makeText(getApplicationContext(), "Печать ШК номенклатуры",
                                                Toast.LENGTH_LONG).show();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
    }
}
