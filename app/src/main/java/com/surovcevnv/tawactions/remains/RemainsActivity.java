package com.surovcevnv.tawactions.remains;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.surovcevnv.tawactions.R;
import com.surovcevnv.tawactions.main.Singleton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by surovcevnv on 06.07.17.
 */

public class RemainsActivity extends FragmentActivity {
    private Singleton ms = Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_remains);

        ListView listViewNomen = (ListView)findViewById(R.id.remainsNom);
        final ArrayList<HashMap<String, String>> remains = ms.remains.getArrListRemains();
        SimpleAdapter adapterNomen = new SimpleAdapter(this, remains, R.layout.item_remain_str,
                new String[]{"name", "quant"},
                new int[] {R.id.textRemainName, R.id.textRemainQuant});
        listViewNomen.setAdapter(adapterNomen);
    }
}
