package com.surovcevnv.tawactions.logger;

/**
 * Created by surovcevnv on 08.07.17.
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Это клас описывает логгер
 */
public class Logger {

    /** Разделитель данных в LogCat */
    public static String mDelimiter = " - ";

    /** Тег для LogCat */
    public static String mTag = "DEBUG";

    /** УИД */
    public static String mUid = null;

    /** Контекст */
    private static Context mContext;

    /** Флаг созранения логов в базу */
    public static boolean mSaveToSql = false;

    /** Флаг сохранерия логов в файл */
    public static boolean mSaveToFile = false;

    /** Сервис для записи логов */
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    /** Шаблон для вывода логов */
    public static Pattern[] mPattern = new Pattern[]{ Pattern.File, Pattern.Method };

    /** Уровни сообщений */
    public enum Level { Info, Error, User, System, Tag }

    /** Шаблоны */
    public enum Pattern { Thread, Method, Object, File, Class, Level }

    public static void init(Context context) {
        init(context, null, null, null, null);
    }

    public static void init(Context context, String tag) {
        init(context, tag, null, null, null);
    }

    public static void init(Context context, String tag, Pattern[] pattern) {
        init(context, tag, null, null, pattern);
    }

    public static void init(Context context, Boolean saveToSql, Boolean saveToFile) {
        init(context, null, saveToSql, saveToFile, null);
    }

    public static void init(Context context, String tag, Boolean saveToSql, Boolean saveToFile) {
        init(context, tag, saveToSql, saveToFile, null);
    }

    public static void init(Context context, String tag, Boolean saveToSql, Boolean saveToFile, Pattern[] pattern) {
        mContext = context;
        if (tag != null) mTag = tag;
        if (saveToSql != null) mSaveToSql = saveToSql;
        if (saveToFile != null) mSaveToFile = saveToFile;
        if (pattern != null && pattern.length > 0) mPattern = pattern;
    }

    public static void log(Level level, Object object, String msg, Object... args) {
        log(level, null, object, String.format(msg, args), 0);
    }

    public static void log(Level level, Object object, String msg) {
        log(level, null, object, msg, 0);
    }

    public static void log(Level level, Object object) {
        log(level, null, object, null, 0);
    }

    public static void log(Level level, String tag, Object object, String msg, Object... args) {
        log(level, tag, object, String.format(msg, args), 0);
    }

    public static void log(Level level, String tag, Object object, String msg) {
        log(level, tag, object, msg, 0);
    }

    public static void log(String tag, Object object, String msg) {
        log(Level.Info, tag, object, msg, 0);
    }

    public static void log(String tag, Object object) {
        log(Level.Info, tag, object, null, 0);
    }

    public static void log(Object object, String msg) {
        log(Level.Info, null, object, msg, 0);
    }

    public static void log(Object object, String msg, Object... args) {
        log(Level.Info, null, object, String.format(msg, args), 0);
    }

    public static void log(Object object) {
        log(Level.Info, null, object, null, 0);
    }

    /** Записывает лог в базу и/или файл и выводит в LogCat **/
    private static void logInThread(Level level, String tag, Object object, String msg, StackTraceElement stackTrace) {
        String methodName = stackTrace.getMethodName();
        String fileName   = stackTrace.getFileName();
        String className  = stackTrace.getClassName();
        String objectName = "";
        if (object instanceof android.support.v4.app.Fragment) {
            Fragment fragment = (Fragment) object;
            Activity activity = fragment.getActivity();
            if (activity != null) objectName = activity.getClass().getSimpleName() + " / ";
        }
        objectName = objectName + object.getClass().getSimpleName();

        // Формируем сообщение по шаблону для LogCat
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mPattern.length; i++) {
            Pattern pattern = mPattern[i];
            switch (pattern) {
                case Thread:
                    sb.append(Thread.currentThread().toString());
                    sb.append(mDelimiter);
                    break;
                case Method:
                    sb.append(stackTrace.getMethodName());
                    sb.append(mDelimiter);
                    break;
                case Class:
                    sb.append(className);
                    sb.append(mDelimiter);
                    break;
                case File:
                    sb.append(fileName);
                    sb.append(mDelimiter);
                    break;
                case Object:
                    sb.append(objectName);
                    sb.append(mDelimiter);
                    break;
                case Level:
                    sb.append(level.toString());
                    sb.append(mDelimiter);
                    break;
            }
        }

        // Добавляем сообщение
        if (msg != null) sb.append(msg);

        // Выводим в LogCat
        if (tag == null) tag = mTag;
        switch (level) {
            case Error:
                Log.e(tag, sb.toString());
                break;
            case System:
            case Info:
            case User:
            case Tag:
                Log.d(tag, sb.toString());
                break;
        }

        // Определяем файл лога
        File logFile;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            logFile = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                    new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()) + ".log");
        } else {
            logFile = new File(Environment.getExternalStorageDirectory(),
                    new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()) + ".log");
        }

        String dateTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS", Locale.getDefault()).format(new Date());
        String fileString = dateTime + mDelimiter + sb.toString() + "\r\n";

        if (mSaveToSql) {
            ContentValues cv = new ContentValues();
            cv.put("dttm", dateTime);
            cv.put("typeEvent", level.toString());
            cv.put("file", fileName);
            cv.put("function", methodName);
            cv.put("msg", msg);
            cv.put("isLoaded", "0");
            cv.put("uid", mUid);
            try {
                mContext.getContentResolver().insert(LoggerProvider.CONTENT_URI, cv);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mSaveToFile) {
            try {
                FileWriter fw = new FileWriter(logFile, true);
                fw.write(fileString);
                fw.flush();
                fw.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private static void log(final Level level, final String tag, final Object object, final String msg, final int inject) {
        final StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[4];
        executor.execute(new Runnable() {
            @Override
            public void run() {
                logInThread(level, tag, object, msg, stackTrace);
            }
        });
    }
}