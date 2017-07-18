package com.surovcevnv.tradeandwarehouse.expenses;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
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
 * Created by surovcevnv on 06.07.17.
 */

public class ExpenseActivity extends FragmentActivity {
    private Singleton ms = Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expenses);

        updateRemains();
    }

    private void updateRemains() {
        ListView listViewNomen = (ListView)findViewById(R.id.listNomExpense);
        final ArrayList<HashMap<String, String>> remains = ms.remains.getArrListRemains();

        SimpleAdapter adapterNomen = new SimpleAdapter(this, remains, R.layout.item_remain_str,
                new String[]{"name", "quant"},
                new int[] {R.id.textRemainName, R.id.textRemainQuant});
        listViewNomen.setAdapter(adapterNomen);
        listViewNomen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, final int position,
                                    long id) {
                Context context = ExpenseActivity.this;
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
                                        if (ms.remains.getRemain(Integer.parseInt(remains.get(position).get("code")))<Float.parseFloat(userInput.getText().toString())) {
                                            Toast.makeText(getApplicationContext(), "Ввели больше, чем есть на остатках",
                                                    Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        ms.remains.delRemain(Integer.parseInt(remains.get(position).get("code")), Float.parseFloat(userInput.getText().toString()));
                                        updateRemains();
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
