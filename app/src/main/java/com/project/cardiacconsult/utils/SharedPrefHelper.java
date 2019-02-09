package com.project.cardiacconsult.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.project.cardiacconsult.models.Clinic;
import com.project.cardiacconsult.models.User;
import com.google.gson.Gson;


public class SharedPrefHelper {

    public static final String PREFS_NAME = "APP_PREFS";
    public static final String PREFS_KEY_USER = "user";
    public static final String PREFS_KEY_USER_UID = "user_uid";
    public static final String PREFS_KEY_CLINIC = "clinic";
    public static final String PREFS_USER_REGISTERED = "registration";
    SharedPreferences settings;
    Editor editor;

    public SharedPrefHelper(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    public void saveUserToPref( User user){
        editor.putString(PREFS_KEY_USER, new Gson().toJson(user));
        editor.commit();
    }

    public User getUserFromPref(){
        return new Gson().fromJson(settings.getString(PREFS_KEY_USER, ""), User.class);
    }

    public void saveClinicToPref(Clinic clinic){
        editor.putString(PREFS_KEY_CLINIC, new Gson().toJson(clinic));
        editor.commit();
    }

    public Clinic getClinicFromPref(){
        return new Gson().fromJson(settings.getString(PREFS_KEY_CLINIC, ""), Clinic.class);
    }

    public void saveUidToPref(String uid){
        editor.putString(PREFS_KEY_USER_UID, new Gson().toJson(uid));
        editor.commit();
    }

    public String getUidFromPref(){
        return new Gson().fromJson(settings.getString(PREFS_KEY_USER_UID, ""), String.class);
    }

    public void setUserRegistrationDone(boolean registration){
        editor.putBoolean(PREFS_USER_REGISTERED, registration).commit();
    }

    public boolean checkUserRegistration(){
        return settings.getBoolean(PREFS_USER_REGISTERED, false);
    }

}
