package com.example.googledriveupload;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveServiceHelper {
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private Drive mDriveService;
//    private String name;

    public DriveServiceHelper(Drive driveService) {
        this.mDriveService = driveService;
    }

    public Task<String> createFile(String filePath,String name){
        return Tasks.call(mExecutor,() ->{
            File fileMetaData = new File();
            fileMetaData.setName(name);

            java.io.File file = new java.io.File(filePath);

            FileContent mediaContent = new FileContent("text/comma-separated-values",file);

            File myFile = null;
            try{
                myFile = mDriveService.files().create(fileMetaData,mediaContent).execute();
            }catch (Exception e){
                e.printStackTrace();
            }

            if (myFile == null){
                throw new IOException("Null result when request file creation");
            }

            return myFile.getId();

        });
    }

//    public void fileName(String fileName){
//        name = fileName;
//    }
}
