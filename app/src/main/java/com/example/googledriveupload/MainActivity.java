package com.example.googledriveupload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    DriveServiceHelper driveServiceHelper;
    private final static String BR=System.getProperty("line.separator");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        tvPath = findViewById(R.id.tvPath);
//        btnChooseFile = findViewById(R.id.btnChooseFile);
//        btnChooseFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               aaa();
//            }
//        });

        requestSignIn();
    }


//    public void aaa(){
//        FilenameFilter namefilter =new FilenameFilter(){
//            private String[] filter={
//                    "bmp","gif","jpg","png"
//            };
//            @Override
//            public boolean accept(File dir, String filename){
//                for(int i=0;i<filter.length;i++){
//                    if(filename.indexOf(filter[i])!=-1)
//                        return true;
//                }
//                return false;
//            }
//        };
//        try{
//            File filePath=new File(Environment.getExternalStorageDirectory()+"/");
//            //String str=filePath.getName();取得檔案夾名稱
//            //textFileName.setText(str);
//            File[] fileList=filePath.listFiles(namefilter);
//            CharSequence[] list =new CharSequence[fileList.length];
//            for(int i=0;i<list.length;i++){
//                list[i]=fileList[i].getName();
//                tvPath.setText(tvPath.getText().toString()+list[i]+BR);
//            }
//
//
//        }catch(Exception e){
//
//        }
//
//    }@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_choose_file, menu);
//        return true;
//    }

    public void uploadFile(View view){
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Uploading to Google Drive");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        String filePath = "/storage/emulated/0/aaaa.pdf";
        driveServiceHelper.createFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Uploaded successfully",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Check your google api key",Toast.LENGTH_LONG).show();
                Log.d("aaa",""+e);
            }
        });
    }

    public void requestSignIn(){
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE),
                        new Scope(DriveScopes.DRIVE_APPDATA))
                .requestIdToken("68625113567-abf38rjn2qdku3a5pirkoh9mjb6ks870.apps.googleusercontent.com")
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(this,signInOptions);

        startActivityForResult(client.getSignInIntent(),400);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case 400:
                if(resultCode == RESULT_OK) {
                    handleSignInIntent(data);
                }
                break;
            default:
                break;
        }
    }

    private void handleSignInIntent(Intent data) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        GoogleAccountCredential credential = GoogleAccountCredential
                                .usingOAuth2(MainActivity.this, Collections.singleton(DriveScopes.DRIVE_FILE));

                        credential.setSelectedAccount(googleSignInAccount.getAccount());

                        Drive googleDriveService =
                                new Drive.Builder(
                                        AndroidHttp.newCompatibleTransport(),
                                        new GsonFactory(),
                                        credential)
                                        .setApplicationName("AppName")
                                        .build();

                        driveServiceHelper = new DriveServiceHelper(googleDriveService);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
