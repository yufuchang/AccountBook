package com.yufuchang.accountbook.main.mine.app.tally.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.base.utils.CommonUtils;

import java.util.Calendar;
import java.util.Date;

public class DatePickUtils {

    public static void showDatePickDialog(Activity activity, long selectedDate, final OnDatePickListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            showDatePickDialog_Lollipop(activity, selectedDate, listener);
        } else {
            showDatePickDialog_Low(activity, selectedDate, listener);
        }
    }

    private static void showDatePickDialog_Low(Activity activity, long selectedDate, final OnDatePickListener listener) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View contentView = inflater.inflate(
                R.layout.dialog_tally_date_pick, null, false);
        MaterialCalendarView materialCalendar = contentView.findViewById(R.id.calendarView);
        EditText etHour = contentView.findViewById(R.id.etHour);
        EditText etMinute = contentView.findViewById(R.id.etMinute);

        Calendar calendarInit = Calendar.getInstance();
        calendarInit.setTimeInMillis(selectedDate);
        etHour.setText(String.valueOf(calendarInit.get(Calendar.HOUR_OF_DAY)));
        etMinute.setText(String.valueOf(calendarInit.get(Calendar.MINUTE)));
        materialCalendar.setSelectedDate(new Date(selectedDate));

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder.setTitle("")
                .setView(contentView)
                .setNegativeButton(R.string.cancel, (DialogInterface d, int which) -> {
                    if (listener != null) {
                        listener.onCancelClick(d);
                    }
                })
                .setPositiveButton(R.string.confirm, (DialogInterface dialogInterface, int which) -> {
                    String hourStr = etHour.getText().toString();
                    String minuteStr = etMinute.getText().toString();
                    int hour = CommonUtils.string2int(hourStr, 0);
                    int minute = CommonUtils.string2int(minuteStr, 0);
                    hour = Math.min(hour, 23);
                    minute = Math.min(minute, 59);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(materialCalendar.getSelectedDate().getDate().getTime());
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);

                    if (listener != null) {
                        listener.onConfirmClick(dialogInterface, calendar.getTimeInMillis());
                    }
                })
                .create();

        final String titleDateFormat = activity.getString(R.string.tally_calendar_title_format);
        materialCalendar.setTitleFormatter(
                (CalendarDay day) -> String.format(titleDateFormat, day.getYear(), day.getMonth() + 1));
        materialCalendar.state().edit().setMaximumDate(Calendar.getInstance()).commit();
        materialCalendar.setOnDateChangedListener(
                (@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) -> {
                    if (listener != null) {
                        listener.onDatePick(dialog, date.getYear(), date.getMonth(), date.getDay());
                    }
                });
        dialog.show();
    }

    private static void showDatePickDialog_Lollipop(Activity activity,
                                                    long selectedDate,
                                                    final OnDatePickListener listener) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View contentView = inflater.inflate(
                R.layout.dialog_tally_date_pick, null, false);
        MaterialCalendarView calendarView = contentView.findViewById(R.id.calendarView);
        EditText etHour = contentView.findViewById(R.id.etHour);
        EditText etMinute = contentView.findViewById(R.id.etMinute);

        Calendar calendarInit = Calendar.getInstance();
        calendarInit.setTimeInMillis(selectedDate);
        etHour.setText(String.valueOf(calendarInit.get(Calendar.HOUR_OF_DAY)));
        etMinute.setText(String.valueOf(calendarInit.get(Calendar.MINUTE)));
//        calendarView.setDate(selectedDate);
        calendarView.setDateSelected(calendarInit,true);

        // 最终选择的时间
        final Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.setTimeInMillis(selectedDate);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder.setTitle("")
                .setView(contentView)
                .setNegativeButton(R.string.cancel, (DialogInterface d, int which) -> {
                    if (listener != null) {
                        listener.onCancelClick(d);
                    }
                })
                .setPositiveButton(R.string.confirm, (DialogInterface dialogInterface, int which) -> {
                    String hourStr = etHour.getText().toString();
                    String minuteStr = etMinute.getText().toString();
                    int hour = CommonUtils.string2int(hourStr, 0);
                    int minute = CommonUtils.string2int(minuteStr, 0);
                    hour = Math.min(hour, 23);
                    minute = Math.min(minute, 59);

                    selectedCalendar.set(Calendar.HOUR_OF_DAY, hour);
                    selectedCalendar.set(Calendar.MINUTE, minute);

                    if (listener != null) {
                        listener.onConfirmClick(dialogInterface, selectedCalendar.getTimeInMillis());
                    }
                })
                .create();


//        calendarView.setMaxDate(System.currentTimeMillis());
//        calendarView.setOnDateChangeListener(
//                (@NonNull CalendarView view, int year, int month, int dayOfMonth) -> {
//                    selectedCalendar.set(Calendar.YEAR, year);
//                    selectedCalendar.set(Calendar.MONTH, month);
//                    selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                    if (listener != null) {
//                        listener.onDatePick(dialog, year, month, dayOfMonth);
//                    }
//                });

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedCalendar.set(Calendar.YEAR, date.getYear());
                selectedCalendar.set(Calendar.MONTH, date.getMonth());
                selectedCalendar.set(Calendar.DAY_OF_MONTH, date.getMonth());
                if (listener != null) {
                    listener.onDatePick(dialog, date.getYear(), date.getMonth(), date.getDay());
                }
            }
        });
        dialog.show();
    }

    public static class OnDatePickListener {
        public void onDatePick(DialogInterface dialog, int year, int month, int dayOfMonth) {
        }

        public void onCancelClick(DialogInterface dialog) {
        }

        public void onConfirmClick(DialogInterface dialog, long timeInMillis) {
        }
    }
}
