package com.example.googledriveupload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
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
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DriveServiceHelper driveServiceHelper;
    private final static String BR=System.getProperty("line.separator");
    private Button btnChooseFile;
    private static final int PICK_CSV_FROM_GALLERY_REQUEST_CODE = 100;
    private String filePath;
    private List<String> filepa = new ArrayList<>();
    private List<String> fileName = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ItemData> itemcardData = new ArrayList<>();

    boolean isCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChooseFile = findViewById(R.id.btnChooseFile);
        btnChooseFile.setOnClickListener(this);

        itemcardData.add(new ItemData("asdasd"));
        BuildRecyclyerView();
        requestSignIn();


    }



    public void uploadFile(View view){
//        String filePath = "/storage/emulated/0/acx1.csv";
//        driveServiceHelper.createFile(filePath)
        int j = filepa.size();
        if (j == 0){
            Toast.makeText(this, "請選取資料", Toast.LENGTH_SHORT).show();
        }else{
            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Uploading to Google Drive");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

        for (int i =0;i<j;i++){
//        driveServiceHelper.fileName(fileName.get(i));
        driveServiceHelper.createFile(filepa.get(i),fileName.get(i))
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
        }
        filepa.clear();
        fileName.clear();
//        Log.d("aaa",""+fileName.size());
    }

    public void requestSignIn(){
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE),
                        new Scope(DriveScopes.DRIVE_APPDATA))
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(this,signInOptions);

        startActivityForResult(client.getSignInIntent(),400);
    }

    private void handleSignInIntent(Intent data) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener(googleSignInAccount -> {
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

                })
                .addOnFailureListener(e -> e.printStackTrace());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnChooseFile:
                chooseFile();
                break;

//            case R.id.rbtn:
//                isCheck = !isCheck;
////                radioButton.setChecked(isCheck);
//////                if (isCheck)
//                break;

             default:
                 break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode)
        {
            case 400:
                if(resultCode == RESULT_OK ) {
                    handleSignInIntent(data);
                }
                break;

            case PICK_CSV_FROM_GALLERY_REQUEST_CODE:
                Uri selectedCsv =null;
                ClipData clipData = null;
                if (resultCode == Activity.RESULT_OK && data != null){
                    selectedCsv = data.getData();
                    if (clipData != null){
                        catchFileInApp(selectedCsv,clipData);
                    }
                    else if (Build.VERSION.SDK_INT>=16 && clipData== null){
                        clipData = data.getClipData();
                        catchFileInApp(selectedCsv,clipData);
                    }
                }
                    break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void catchFileInApp(Uri selectedCsv,ClipData clipData){
        if (selectedCsv != null){
            String path = FileUtil.getFileAbsolutePath(this,selectedCsv);
//            Log.d("path1",""+path);
//            filePath = path;
            filepa.add(path);
            String name = FileUtil.fileName(path);
            fileName.add(name);
            Log.d("asd",fileName.get(0));

            itemcardData.add(new ItemData(name));
            mAdapter.addItem(itemcardData);


            //設定檔案名稱
        }else  if (clipData != null){
            int count = clipData.getItemCount();
            if (count > 0){
                Uri[] uris = new Uri[count];
                String[] paths = new String[count];
                String[] names = new String[count];
                for (int i=0;i<count;i++){
                    uris[i] = clipData.getItemAt(i).getUri();
                    paths[i] = FileUtil.getFileAbsolutePath(this,uris[i]);
                    names[i] = FileUtil.fileName(paths[i]);
                    Log.d("paee",paths[i]);
                    filepa.add(paths[i]);
                    fileName.add(names[i]);
                }
                Log.d("paee",fileName.get(1));
                Log.d("paee",fileName.get(0));
            }
        }
    }

    private void chooseFile() {
        //https://www.jianshu.com/p/c1656748849f mimeType類型
        String mimeType = "text/comma-separated-values";
        PackageManager packageManager = MainActivity.this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0){
            Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
            picker.setType(mimeType);
            picker.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//            picker.addCategory(Intent.CATEGORY_OPENABLE);
            picker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            Intent destIntent = Intent.createChooser(picker, "選取csv檔案");
            startActivityForResult(destIntent,PICK_CSV_FROM_GALLERY_REQUEST_CODE);

        }else{
            Log.d("error","無可用的Activity");
        }
    }

    public void BuildRecyclyerView(){
        mRecyclerView = findViewById(R.id.recyclerviewFile);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ItemAdapter(MainActivity.this,itemcardData);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


}
