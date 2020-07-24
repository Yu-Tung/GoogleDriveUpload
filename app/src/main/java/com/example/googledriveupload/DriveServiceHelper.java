package com.example.googledriveupload;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveServiceHelper {
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
//    private Drive mDriveService;
    protected com.google.api.services.drive.Drive mDriveService;
//    private String name;

    String folderId = "1frxuLY0zAu_XFrpVf8ULLQpMZtM5j9Ua";
    FileList result = null;

    public DriveServiceHelper(Drive driveService) {
        this.mDriveService = driveService;
    }

    public Task<String> createFile(String filePath,String name){
        return Tasks.call(mExecutor,() ->{
            File fileMetaData = new File();
            fileMetaData.setName(name);

//            fileMetaData.setParents((Collections.singletonList(folderId)));

            java.io.File file = new java.io.File(filePath);

            FileContent mediaContent = new FileContent("application/octet-stream",file);

            File myFile = null;
            try{
                myFile = mDriveService.files().create(fileMetaData,mediaContent)
                        .setFields("id")
                        .execute();

                Log.e("File" ,"Flie ID = " +myFile.getId());
            }catch (Exception e){
                e.printStackTrace();
            }

            if (myFile == null){
                throw new IOException("Null result when request file creation");
            }

            return myFile.getId();

        });
    }
}
