package com.example.quanlibenhvien.UTILS;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
public class Util {
    public String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return  year + "-" + month + "-" + day;
    } // lấy ngày hiện tại
    public static String formatDate(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    } // định dạng ngày
    public static List<String> getWeekList(int year) {
        List<String> weekList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        while (calendar.get(Calendar.YEAR) == year) {
            int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
            String startDate = dateFormat.format(calendar.getTime());

            calendar.add(Calendar.DATE, 6);
            String endDate = dateFormat.format(calendar.getTime());

            String weekData = "Tuần " + weekNumber + ":  " + startDate + " -- " + endDate;
            weekList.add(weekData);

            calendar.add(Calendar.DATE, 1);
        }
        return weekList;
    } // lấy danh sách tuần trong năm




}


