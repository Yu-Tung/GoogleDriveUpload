package com.example.net;

import android.app.Activity;
import android.util.Log;

import com.example.net.base.BaseGoogleDrive;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.drive.model.FileList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoginChecker extends BaseGoogleDrive {
    public final static String ACCOUNT_NAME = "zx0933779547@gmail.com";
    private Activity activity;
    private String account;
    private eventCallBack event;

    public LoginChecker(Activity activity,eventCallBack event) {
        super(activity);
        this.activity = activity;
        this.event = event;
    }

    @Override
    protected void beforeAccess() {
        event.showProgress();
    }

    @Override
    protected boolean onAccess() {
        FileList result = null;
        Boolean isSuccess =false;
        try {
            result = drive.files().list()
                    .setQ("name='acx1.csv'")
                    .execute();
            Log.d("name", "name = " + result.getFiles().get(0).getId());

            InputStream inputStream1 = drive.files()
                    .get(result.getFiles().get(0).getId()).executeMediaAsInputStream();
//                        .export(result.getFiles().get(0).getId(), "text/csv")
//                        .executeMediaAsInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream1));
            String line = "";
            while ((line = reader.readLine()) != null) {
                line = line.split(",")[0];
                Log.e("line", String.valueOf(line));
                if (line.equals("5456")) {
                    isSuccess = true;
                    break;
                } else {
                    Log.d("8787", "onAccess:error ");
                }
            }
        } catch (UserRecoverableAuthIOException e) {
            e.printStackTrace();
            Log.e("err",e.getMessage());
            isSuccess = false;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("err",e.getMessage());
            isSuccess = false;
        }
        Log.e("result","result = " +isSuccess);
        return isSuccess;

    }

    @Override
    protected void successAccess() {
        event.hideProgress();
        event.onSuccessful(account);
    }

    @Override
    protected void failAccess() {
        event.hideProgress();
        event.onFail();
    }

    @Override
    protected String setAccount() {
        return ACCOUNT_NAME;
    }

    public void checkFile(final String account) {
        this.account = account;
        execute();
    }

    public interface eventCallBack {
        public void onSuccessful(String account);

        public void onFail();

        public void showProgress();

        public void hideProgress();
    }
}
