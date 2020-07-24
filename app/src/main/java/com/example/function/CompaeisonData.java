package com.example.function;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CompaeisonData {
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    protected com.google.api.services.drive.Drive mDriveService;
    FileList result = null;

    public CompaeisonData(Drive mDriveService) {
        this.mDriveService = mDriveService;
    }

    public Task createFile() {

        return Tasks.call(mExecutor, () -> {
            Boolean isSuccess = null;

            try {
                result = mDriveService.files().list()
                        .setQ("name='acx.xlsx'")
                        .execute();
                Log.d("name", "name = " + result.getFiles().get(0).getId());

                InputStream inputStream1 = mDriveService.files()
                        .get(result.getFiles().get(0).getId()).executeMediaAsInputStream();
//                        .export(result.getFiles().get(0).getId(), "text/csv")
//                        .executeMediaAsInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream1));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    line = line.split(",")[0];
                    Log.e("line", String.valueOf(line));
                    if (line.equals("asdasd")) {
                        isSuccess = true;
                        break;
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

        });
    }
}
