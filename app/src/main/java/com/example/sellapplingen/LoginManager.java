package com.example.sellapplingen;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginManager {

    // Die URL des Servers, auf dem die Benutzerdaten überprüft werden sollen
    private static final String SERVER_URL = "http://131.173.65.77:5000/auth/login";

    public static final String PREF_NAME = "LoginPrefs";
    public String username;
    public String password;

    public LoginManager(String username, String password) {

        this.username = username;
        this.password = password;

    }

    private static LoginManager instance;
    private Context context;

    // Privater Konstruktor, um die Instanz nur einmal zu erstellen
    private LoginManager(Context context) {
        this.context = context;
    }

    // Statische Methode zum Erhalten der einzigen Instanz des LoginManagers
    public static LoginManager getInstance(Context context) {
        if (instance == null) {
            instance = new LoginManager(context.getApplicationContext());
        }
        return instance;
    }



    //jsonwebtoken muss immer mit geschickt werden, z.b bei settings usw

    public void saveLoginCredentials(String enteredUsername, String enteredPassword) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(username, enteredUsername);
        editor.putString(password, enteredPassword);
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(username) && sharedPreferences.contains(password);
    }



    // Beispiel-Implementierung für die Überprüfung der Login-Daten mit dem Server


    public void sendPost() {
        Log.d("LoginManager", "sendPost() Methode aufgerufen");

        if (username == null || password == null )  {
            Log.d("LoginManager", "Name oder Passwort ist null");
            throw new NullPointerException("Name or password is null");
        } else if (username.trim().isEmpty() || password.trim().isEmpty()) {
            Log.d("LoginManager", "Name oder Passwort ist leer");
            throw new IllegalArgumentException("Name or password is empty");
        }

        Log.d("LoginManager", "Geht in Methdawdwdode");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://131.173.65.77:5000/auth/login");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    Log.d("LoginManager", "Serververbindung wird hergestellt...");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject requestData = new JSONObject();
                    requestData.put("username", username.trim());
                    requestData.put("password", password.trim());

                    Log.i("JSON", requestData.toString());

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(requestData.toString());
                    os.flush();
                    os.close();
                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    conn.disconnect();


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.start();
    }

}

