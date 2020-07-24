package com.example.function;


import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CreateFolder {
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    //    private Drive mDriveService;
    protected com.google.api.services.drive.Drive mDriveService;
//    private String name;

    FileList result = null;

    public CreateFolder(Drive driveService) {
        this.mDriveService = driveService;
    }

    public Task<String> createFolder(){
        return Tasks.call(mExecutor,() ->{
            File fileMetaData = new File();
            fileMetaData.setName("UserA");
            fileMetaData.setMimeType("application/vnd.google-apps.folder");

            File file = mDriveService.files().create(fileMetaData)
                    .setFields("id")
                    .execute();

////            fileMetaData.setParents((Collections.singletonList("appDataFolder")));
//
//            java.io.File file = new java.io.File(filePath);
//
//            FileContent mediaContent = new FileContent("text/comma-separated-values",file);
//
//            File myFile = null;
//            try{
//                myFile = mDriveService.files().create(fileMetaData,mediaContent)
////                        .setFields("id,parent")
//                        .execute();
//
//                Log.e("File" ,"Flie ID = " +myFile.getId());
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//            if (myFile == null){
//                throw new IOException("Null result when request file creation");
//            }
//
//            return myFile.getId();
            Log.e("fileid", "createFolder: " + file.getId() );
            return file.getId();
        });
    }
}
