package com.vcmedyspire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.util.SendRequest;

import org.json.JSONArray;

public class AdminSplash extends AppCompatActivity {


    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 185;
    private static int SPLASH_TIME_OUT = 1000;

    private static final int REQUEST_CODE = 185;
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private boolean showedPermission = false;
    String versionName = null;

    ImageView splashLogo;
    TextView txtVersionName;
    ProgressBar progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_splash);

        splashLogo = (ImageView) findViewById(R.id.splashLogo);
        txtVersionName = (TextView) findViewById(R.id.txtVersionName);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);


        /*    if(!(AmrMethods.getDefaults("language", AdminSplash.this)==null)){
                AmrMethods.setLocale(AmrMethods.getDefaults("language", AdminSplash.this), AdminSplash.this);
            }else{
                AmrMethods.setLocale("en",AdminSplash.this);
                AmrMethods.setDefaults("language", "en", AdminSplash.this);
                AmrMethods.setLocale(AmrMethods.getDefaults("language", AdminSplash.this), AdminSplash.this);
            }*/

        //   txtVersionName.setText(getResources().getString(R.string.app_name));


        try {
            txtVersionName = (TextView) findViewById(R.id.txtVersionName);
            versionName = AdminSplash.this.getPackageManager().getPackageInfo(AdminSplash.this.getPackageName(), 0).versionName;
            txtVersionName.setText("Version " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        splashLogo.setImageResource(R.drawable.ic_launcher_background);


    }


    @Override
    protected void onStart() {
        super.onStart();


    }


    boolean checkIfAlreadyHavePermission() {
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (!(grantResult == PackageManager.PERMISSION_GRANTED)) { // IF ANY PERMISSION NOT GRANTED WILL ASK AGAIN
                            System.out.println("###############NO PERMISSION  #######################" + grantResult);
                            Toast.makeText(getApplicationContext(), "Please Grant Permissions", Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                    System.out.println("######################################");
                    openMainScreen();
                } else {
                    System.out.println("###############NO PERMISSION SIZE 0 #######################");
                    ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
                }
                break;
            default:
//		            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void openMainScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(AdminSplash.this, MainActivity.class);
                startActivity(i);
               // new GetAndroidVersion().execute();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!showedPermission && !checkIfAlreadyHavePermission()) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
                showedPermission = true;
                return;
            }
        }
        openMainScreen();

    }


    private class GetAndroidVersion extends AsyncTask<Void, Void, Void> {
        String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                JSONArray ja1 = new JSONArray();
                SendRequest req = new SendRequest();
                response = req.uploadToServer("getAndroidVersion", ja1);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if (response == null) {
				/*Toast.makeText(getApplicationContext(), getResources().getString(R.string.serverDown), Toast.LENGTH_SHORT).show();
				finish();*/
                Intent i = new Intent(AdminSplash.this, MainActivity.class);
                startActivity(i);
                finish();

            } else if (response.contains("html") || response.length() > 10) {
                System.out.println(response);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.serverDown), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                try {
                    System.out.println(response);
                    String currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

                    double current = Double.parseDouble(currentVersion);
                    double dbVersion = Double.parseDouble(response);
                    System.err.println(current + " CURRENT    DB " + dbVersion);
                    if (current >= dbVersion) {
                        Intent i = new Intent(AdminSplash.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        updateApplication();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.serverDown), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }


    private void updateApplication() {

        final Dialog dialogImage = new Dialog(AdminSplash.this);
        dialogImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogImage.setContentView(R.layout.custom_update_application);
        dialogImage.setCancelable(false);
        Button btnCancelAlert = (Button) dialogImage.findViewById(R.id.btnCancelAlert);
        Button btnUpdate = (Button) dialogImage.findViewById(R.id.btnUpdate);

        dialogImage.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                finish();
            }
        });

        btnCancelAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogImage.dismiss();
                finish();
            }
        });
    }
}

