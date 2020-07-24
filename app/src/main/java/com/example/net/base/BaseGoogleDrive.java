package com.example.net.base;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

public abstract class BaseGoogleDrive extends AsyncTask<String,Void,Boolean> {
    private Activity activity;
    protected GoogleAccountCredential credential;
    protected com.google.api.services.drive.Drive drive;
    public static final int ASK_ACCOUNT = 1;

    public BaseGoogleDrive(Activity activity){
        this.activity =activity;
        if (credential == null){
            credential = GoogleAccountCredential.usingOAuth2(
                    activity,
                    DriveScopes.all()
            );
        }
        drive = new com.google.api.services.drive.Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                new GsonFactory(),
                credential)
                .build();
        credential.setSelectedAccountName(setAccount());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        beforeAccess();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean isSuccess = false;
        if (credential.getSelectedAccountName() == null){
            activity.startActivityForResult(credential.newChooseAccountIntent(),ASK_ACCOUNT);
        }else{
            isSuccess = onAccess();
        }
        return isSuccess;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean){
            successAccess();
        }else{
            failAccess();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public GoogleAccountCredential getCredential(){
        return credential;
    }

    protected abstract void beforeAccess();

    protected abstract boolean onAccess();

    protected abstract void successAccess();

    protected abstract void failAccess();

    protected abstract String setAccount();
}
