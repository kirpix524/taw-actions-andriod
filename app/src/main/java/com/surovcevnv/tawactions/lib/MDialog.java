package com.surovcevnv.tawactions.lib;

/**
 * Created by surovcevnv on 26.07.17.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import com.surovcevnv.tawactions.logger.Logger;
import com.surovcevnv.tawactions.R;

/** Менеджер диалогов **/
public class MDialog extends android.support.v4.app.Fragment {

    /** Тег для сохранения фрагмента **/
    private static final String TAG = "MD";

    /** Флаг определяющий находится ли фрагмент в состоянии паузы **/
    private boolean mFlagPause = false;

    /** Очередь из диалогов, которые нужно показать или скрыть **/
    private final Vector<FDialog> messageQueueBuffer = new Vector<>();

    /** Возвращает текущий фрагмент менеджера диалогов **/
    public static MDialog getInstance(FragmentManager manager) {
        MDialog dialog = (MDialog) manager.findFragmentByTag(TAG);
        if (dialog != null) return dialog;
        dialog = new MDialog();
        manager.beginTransaction().add(dialog, TAG).commit();
        manager.executePendingTransactions();
        return dialog;
    }

    /** При создании фрагмента **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /** При событии pause **/
    @Override
    public void onPause() {
        super.onPause();
        mFlagPause = true;
    }

    /** При событии Resume **/
    @Override
    public void onResume() {
        super.onResume();
        mFlagPause = false;
        while (messageQueueBuffer.size() > 0) {
            FDialog dialog = messageQueueBuffer.elementAt(0);
            messageQueueBuffer.removeElementAt(0);
            if (dialog == null) hide();
            else show(dialog);
        }
    }

    /** Показывеет диалог для выбора даты **/
    public String showDate(String title, String message, String defDate) {
        FDialog dialog = FDialog.createDialog(FDialog.DIALOG_DATE, title, message, null, null, defDate);
        show(dialog);
        return dialog.getArguments().getString(FDialog.ARG_RESULT);
    }

    /** Показывает диалог для ввода текста с собственным обработчиком нажатий **/
    public String showInput(String title, String message, OnClickListener onClick) {
        FDialog dialog = FDialog.createDialog(FDialog.DIALOG_INPUT, title, message, null, onClick, null);
        show(dialog);
        return dialog.getArguments().getString(FDialog.ARG_RESULT);
    }

    /** Показывает диалог для ввода текста со стандартным обработчиком нажатий **/
    public String showInput(String title, String message) {
        return showInput(title, message, null);
    }

    /** Показывает диалог с кнопкой ОК с собственным обработчиком нажатий **/
    public void showBox(String title, String message, OnClickListener onClick) {
        FDialog dialog = FDialog.createDialog(FDialog.DIALOG_BOX, title, message, null, onClick, null);
        show(dialog);
    }

    /** Показывает диалог с кнопкой ОК со стандартным обработчиком нажатий **/
    public void showBox(String title, String message) {
        showBox(title, message, null);
    }

    /** Показывает диалог с выбором 'да' и 'нет' с собственным обработчиком нажатий **/
    public boolean showQuest(String title, String message, OnClickListener onClick) {
        FDialog dialog = FDialog.createDialog(FDialog.DIALOG_QUEST, title, message, null, onClick, null);
        show(dialog);
        return dialog.getArguments().getInt(FDialog.ARG_RESULT) == Dialog.BUTTON_POSITIVE;
    }

    /** Показывает диалог с выбором 'да' и 'нет' со стандартным обработчиком нажатий **/
    public boolean showQuest(String title, String message) {
        return showQuest(title, message, null);
    }

    /** Показывает диалог с выбором из списка с собственным обработчиком нажатий **/
    public String showList(String title, String[] content, OnClickListener onClick) {
        FDialog dialog = FDialog.createDialog(FDialog.DIALOG_LIST, title, null, content, onClick, null);
        show(dialog);
        int result = dialog.getArguments().getInt(FDialog.ARG_RESULT);
        return result < 0 ? null : content[result];
    }

    /** Показывает диалог с выбором из списка со стандартным обработчиком нажатий **/
    public String showList(String title, String[] content) {
        return showList(title, content, null);
    }

    /** Показывает диалог с выбором из списка **/
    public <T> T showList(String title, Map<String, T> map, OnClickListener onClick) {
        String[] content = map.keySet().toArray(new String[map.size()]);
        FDialog dialog = FDialog.createDialog(FDialog.DIALOG_LIST, title, null, content, onClick, null);
        show(dialog);
        int result = dialog.getArguments().getInt(FDialog.ARG_RESULT);
        return result < 0 ? null : map.get(content[result]);
    }

    /** Показывает диалог с выбором из списка **/
    public <T> T showList(String title, Map<String, T> map) {
        return showList(title, map, null);
    }

    //-----------------------------------------------------------------------------------------------------------------------------------
    /** Показывает диалог с множественным выбором из списка с собственным обработчиком нажатий**/
    public boolean[] showMultiSelectList(String title, String[] content, boolean[] checkedItems, OnClickListener onClick) {
        FDialog dialog = FDialog.createDialog(FDialog.DIALOG_MULTI_SELECT_LIST, title, null, content, onClick, checkedItems);
        show(dialog);
        int result = dialog.getArguments().getInt(FDialog.ARG_RESULT);
        return result != Dialog.BUTTON_POSITIVE ? null : checkedItems;
    }

    /** Показывает диалог с множественным выбором из списка со стандартным обработчиком нажатий**/
    public boolean[] showMultiSelectList(String title, String[] content, boolean[] checkedItems) {
        return showMultiSelectList(title, content, checkedItems, null);
    }

    /** Показывает заданый пользователем по форме подиалог **/
    public void showCustom(View view) {
        FDialog dialog = FDialog.createDialog(FDialog.DIALOG_CUSTOM, null, null, null, null, view);
        show(dialog);
    }

    /**  Показывает диалог типа ожидание (крутилка) **/
    public void showProgress(String title, String message) {
        FDialog dialog = FDialog.createDialog(FDialog.DIALOG_PRG1, title, message, null, null, null);
        show(dialog);
    }

    /** Показывает диалог типа прогрессбар **/
    public void showProgressH(String title, String message) {
        FDialog dialog = FDialog.createDialog(FDialog.DIALOG_PRG2, title, message, null, null, null);
        show(dialog);
    }

    /** Устанавливает знаечние прогресс бара **/
    public void setProgress(int value) {
        FragmentActivity activity = getActivity();
        if (activity == null) return;
        android.support.v4.app.Fragment prev = activity.getSupportFragmentManager().findFragmentByTag("progress");
        if (prev == null) return;
        ProgressDialog pd = (ProgressDialog)((DialogFragment) prev).getDialog();
        if (pd == null) return;
        pd.setProgress(value);
    }

    /** Внутренняя процедура непосредственно отображающая диалог на экране **/
    private void show(final FDialog dialog) {

        // Этот участок кода, должен выполняться в UI потоке
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Bundle bundle = dialog.getArguments();
                int type = bundle.getInt(FDialog.ARG_TYPE);

                String tag = (type == FDialog.DIALOG_PRG1 || type == FDialog.DIALOG_PRG2 ||
                        type == FDialog.DIALOG_CUSTOM)
                        ? "progress" : "dialog";

                if (mFlagPause) {
                    messageQueueBuffer.add(dialog);
                } else {
                    FragmentActivity activity = getActivity();
                    android.support.v4.app.Fragment prev = activity.getSupportFragmentManager().findFragmentByTag("progress");
                    if (prev != null) ((FDialog) prev).dismiss();

                    activity.getSupportFragmentManager().beginTransaction().add(dialog, tag).commit();
                    activity.getSupportFragmentManager().executePendingTransactions();
                }

                // Для ProgressDialog снимаем лок
                if (tag.equals("progress")) dialog.unlock();
            }
        };

        // Определяем поток
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            task.run();
        } else {
            Handler h = new Handler(Looper.getMainLooper());
            h.post(task);
            dialog.lock();
        }
    }

    /** Скрывает диалог **/
    public void hide() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                if (mFlagPause) {
                    messageQueueBuffer.add(null);
                } else {
                    FragmentActivity activity = getActivity();
                    android.support.v4.app.Fragment prev = activity.getSupportFragmentManager().findFragmentByTag("progress");
                    if (prev != null) ((FDialog) prev).dismiss();
                }
            }
        };

        // Определяем поток
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            task.run();
        } else {
            Handler h = new Handler(Looper.getMainLooper());
            h.post(task);
        }
    }

    /** Вывод всплывающего сообщения **/
    public void showToast(Context activityContext, String message) {
        Logger.log(Logger.Level.Info, this, message);
        View view = ((Activity) activityContext).getLayoutInflater().inflate(R.layout.toast_error, null);
        ((TextView) view.findViewById(R.id.toast_msg)).setText(message);
        showCustom(view);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        hide();
    }

    /** Вывод всплывающего сообщения на указанное кол-во секунд**/
    public void showToast(Context activityContext,String message, int time) {
        Logger.log(Logger.Level.Info, this, message);
        View view = ((Activity) activityContext).getLayoutInflater().inflate(R.layout.toast_error, null);
        ((TextView) view.findViewById(R.id.toast_msg)).setText(message);
        showCustom(view);
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        hide();
    }
}
