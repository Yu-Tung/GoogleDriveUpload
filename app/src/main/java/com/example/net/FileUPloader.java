package com.example.net;

import android.app.Activity;

import com.example.net.base.BaseGoogleDrive;
import com.google.api.services.drive.model.FileList;

import java.io.File;

//public class FileUPloader extends BaseGoogleDrive {
//    private Activity activity;
//
//    public FileUPloader(Activity activity) {
//        super(activity);
//        this.activity =activity;
//    }
//
//    @Override
//    protected void beforeAccess() {
//
//    }
//
//    @Override
//    protected boolean onAccess() {
//        FileList result = null;
//        String filderId = null;
//        boolean isSuccess = false;
//
//
//        try {
//            if (credential.getSelectedAccountName() == null){
//                activity.startActivityForResult(credential.newChooseAccountIntent(),SET_ACCOUNT);
//                return false;
//            }
//            result = drive.files().list()
//                    .setQ("name = ''")
//                    .execute();
//        }
//
//        return false;
//    }
//
//    @Override
//    protected void successAccess() {
//
//    }
//
//    @Override
//    protected void failAccess() {
//
//    }
//
//    @Override
//    protected String setAccount() {
//        return null;
//    }
//}
