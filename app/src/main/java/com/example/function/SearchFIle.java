package com.example.function;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SearchFIle {
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    //    private Drive mDriveService;
    protected com.google.api.services.drive.Drive mDriveService;
    //    private String name;
    String folderId = "1frxuLY0zAu_XFrpVf8ULLQpMZtM5j9Ua";
    FileList result = null;

    public SearchFIle(Drive driveService) {
        this.mDriveService = driveService;
    }

    public Task<String> searFile(){
        return Tasks.call(mExecutor,() ->{
            String pageToken = null;

            try{
                do {
                    FileList reset =mDriveService.files().list()
//                            .setQ("'root' in parents and mimeType != 'application/vnd.google-apps.folder' and trashed = false")
//                            .setQ("mimeType='application/vnd.google-apps.folder' ")
//                            .setQ("Id = '1frxuLY0zAu_XFrpVf8ULLQpMZtM5j9Ua'")
//                            .setQ("mimeType='text/comma-separated-values'")
                            .setQ("'zx0933779547@gmail.com' in writers")
                            .setSpaces("drive")
//                            .setFields("nextPageToken, files(id,name)")
                            .setFields("nextPageToken, files(id,name,mimeType)")
//                            .setFields("nextPageToken, files(id,name)")
                            .setPageToken(pageToken)
                            .execute();
                    for (File file : reset.getFiles()){
                        Log.e("File", "File: "+file.getName()+","+file.getId()+","+file.getMimeType());
//                        Log.e("File", "File: "+file.getName()+","+file.getId()+","+file.getMimeType());
                    }

                    pageToken = reset.getNextPageToken();
                }while (pageToken !=null);


            }catch (UserRecoverableAuthIOException e) {
                e.printStackTrace();
                Log.e("err",e.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("err",e.getMessage());

            }

            return folderId;
        });
    }
}
