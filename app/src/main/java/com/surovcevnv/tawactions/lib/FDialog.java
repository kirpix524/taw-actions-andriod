package com.surovcevnv.tawactions.lib;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import com.surovcevnv.tawactions.R;

/**
 * Created by surovcevnv on 26.07.17.
 */

public class FDialog extends android.support.v4.app.DialogFragment implements DialogInterface.OnClickListener, DialogInterface.OnMultiChoiceClickListener, DatePickerDialog.OnDateSetListener {

    /** Типы диалогов **/
    public static final int DIALOG_INPUT   = 0;
    public static final int DIALOG_BOX     = 1;
    public static final int DIALOG_QUEST   = 2;
    public static final int DIALOG_LIST    = 3;
    public static final int DIALOG_PRG1    = 4;
    public static final int DIALOG_PRG2    = 5;
    public static final int DIALOG_CUSTOM  = 6;
    public static final int DIALOG_MULTI_SELECT_LIST = 7;
    public static final int DIALOG_DATE    = 8;

    /** Индентификаторы аргументов **/
    public static final String ARG_TITLE   = "arg_title";
    public static final String ARG_CONTENT = "arg_content";
    public static final String ARG_MESSAGE = "arg_message";
    public static final String ARG_TYPE    = "arg_type";
    public static final String ARG_RESULT  = "arg_result";

    private Object mObject;
    private DialogInterface.OnClickListener mOnClick;
    private CountDownLatch mLockDialog = new CountDownLatch(1);

    /** Устанавливает object для диалога **/
    public void setObject(Object object) {
        mObject = object;
    }

    /** Устанавливает интерфейс для обработки нажатия кнопок **/
    public void setOnClickListener(DialogInterface.OnClickListener onClick) {
        mOnClick = onClick;
    }

    /** Создает диалог **/
    public static FDialog createDialog(int type, String title, String message,
                                       String[] content, DialogInterface.OnClickListener onClick, Object object) {
        Bundle bundle = new Bundle();
        bundle.putInt(FDialog.ARG_TYPE, type);
        bundle.putString(FDialog.ARG_TITLE, title);
        bundle.putString(FDialog.ARG_MESSAGE, message);
        bundle.putStringArray(FDialog.ARG_CONTENT, content);

        FDialog dialog = new FDialog();
        dialog.setArguments(bundle);
        dialog.setOnClickListener(onClick);
        dialog.setObject(object);

        return dialog;
    }

    /** Блокирует текущий поток **/
    public void lock() {
        try {
            mLockDialog.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** Разблокирует текущий поток **/
    public void unlock() {
        mLockDialog.countDown();
    }

    @Override
    public void onCreate(Bundle icicle) {
        setRetainInstance(true);
        setCancelable(false);
        super.onCreate(icicle);
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        AlertDialog dialog;
        switch (bundle.getInt(ARG_TYPE)) {
            case DIALOG_CUSTOM:
                return new AlertDialog.Builder(getActivity())
                        .setView((View)mObject)
                        .create();
            case DIALOG_INPUT:
                EditText input = new EditText(getActivity());
                input.setId(R.id.edit_text);
                input.requestFocus();
                dialog = new AlertDialog.Builder(getActivity())
                        .setView(input)
                        .setPositiveButton("Ок", this)
                        .create();
                break;
            case DIALOG_BOX:
                dialog = new AlertDialog.Builder(getActivity())
                        .setPositiveButton("Ок", this)
                        .create();
                break;
            case DIALOG_QUEST:
                dialog = new AlertDialog.Builder(getActivity())
                        .setPositiveButton("Да", this)
                        .setNegativeButton("Нет", this)
                        .create();
                break;
            case DIALOG_LIST:
                dialog = new AlertDialog.Builder(getActivity())
                        .setNegativeButton("Отмена", this)
                        .setItems(bundle.getStringArray(ARG_CONTENT), this)
                        .create();
                break;
            case DIALOG_PRG1:
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog = progressDialog;
                break;
            case DIALOG_PRG2:
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMax(100);
                dialog = progressDialog;
                break;
            case DIALOG_MULTI_SELECT_LIST:
                dialog = new AlertDialog.Builder(getActivity())
                        .setPositiveButton("Ок", this)
                        .setNegativeButton("Отмена", this)
                        .setMultiChoiceItems(bundle.getStringArray(ARG_CONTENT), (boolean[]) mObject, this)
                        .create();
                break;
            case DIALOG_DATE:
                Calendar c = Calendar.getInstance();
                if (mObject != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
                    try {
                        Date date = sdf.parse(mObject.toString());
                        c.setTime(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                dialog = new DatePickerDialog(getActivity(), this, year, month, day);
                break;
            default:
                dialog = new AlertDialog.Builder(getActivity())
                        .create();
                break;
        }
        dialog.setTitle(bundle.getString(ARG_TITLE));
        dialog.setMessage(bundle.getString(ARG_MESSAGE));
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (!view.isShown()) return;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String dtv = sdf.format(c.getTime());
        getArguments().putString(ARG_RESULT, dtv);
        unlock();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Bundle bundle = getArguments();
        switch (bundle.getInt(ARG_TYPE)) {
            case DIALOG_INPUT:
                EditText edit = (EditText)((AlertDialog) dialogInterface).findViewById(R.id.edit_text);
                bundle.putString(ARG_RESULT, edit.getText().toString());
                break;
            default:
                bundle.putInt(ARG_RESULT, i);
                break;
        }
        unlock();
        if (mOnClick != null) mOnClick.onClick(dialogInterface, i);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        ((boolean[]) mObject)[which] = isChecked;
    }
}