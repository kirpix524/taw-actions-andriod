package com.surovcevnv.tradeandwarehouse.logger;

/**
 * Created by surovcevnv on 10.07.17.
 */
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoggerService extends Service {

    private static ScheduledExecutorService mServiceLogs;

    public Runnable task = new Runnable() {
        @Override
        public void run() {
            Integer lastId = 0;
            ArrayList<String> inArray = new ArrayList<>();
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(LoggerProvider.CONTENT_URI, null,
                        "isLoaded = 0 AND typeEvent != 'System' LIMIT 500", null, null);
                if (cursor.moveToFirst()) {
                    String[] columns = cursor.getColumnNames();
                    do {
                        StringBuilder sb = new StringBuilder();
                        for (String column : columns) {
                            sb.append(cursor.getString(cursor.getColumnIndex(column)));
                            sb.append("|");
                        }
                        inArray.add(sb.toString());
                        lastId = cursor.getInt(cursor.getColumnIndex("id"));
                    } while (cursor.moveToNext());
                }

                Logger.log(Logger.Level.System, this, "Логов для выгрузки: " + inArray.size());

                if (inArray.size() == 0) return;

                Logger.log(Logger.Level.System, this, "id: " + lastId);

                ContentValues cv = new ContentValues();
                cv.put("isLoaded", "1");
                getContentResolver().update(LoggerProvider.CONTENT_URI, cv, "id < ? OR id = ?",
                        new String[] {lastId.toString(), lastId.toString()});
            } catch (Exception e) {
                e.printStackTrace();
                Logger.log(Logger.Level.Info, this, "Ошибка выгрузки логов: " + e.getMessage());
            } finally {
                if (cursor != null) cursor.close();
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.log(Logger.Level.System, this);

        if (mServiceLogs == null) {
            mServiceLogs = Executors.newSingleThreadScheduledExecutor();
            mServiceLogs.scheduleAtFixedRate(task, 0, 5, TimeUnit.MINUTES);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.log(Logger.Level.System, this);

        if (mServiceLogs != null) {
            mServiceLogs.shutdownNow();
            mServiceLogs = null;
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.log(Logger.Level.System, this);
        return START_STICKY;
    }

}
