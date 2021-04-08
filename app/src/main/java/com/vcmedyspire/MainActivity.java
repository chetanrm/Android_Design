package com.vcmedyspire;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.util.SendRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity-Create an Account";
    AlertDialog show;
    EditText editTextPhoneNo,editTextNewPassword,editTextConfirmPassword;
    Button btnLogin, btnSubmit;
    String phoneNo,newPwd,confPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextPhoneNo= findViewById(R.id.etPhoneNo);
        editTextNewPassword= findViewById(R.id.etNewPassword);
        editTextConfirmPassword= findViewById(R.id.etConfirmPassword);
        btnSubmit= findViewById(R.id.btnSubmit);
        btnLogin= findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);


    }



    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                Log.e(TAG, "onClick: " + "Login clicked" );
                phoneNo = editTextPhoneNo.getText().toString().trim();
                newPwd = editTextNewPassword.getText().toString().trim();
                confPwd = editTextConfirmPassword.getText().toString().trim();

                if(phoneNo.equals("")){
                    editTextPhoneNo.requestFocus();
                    show = new AlertDialog.Builder(MainActivity.this)
                            .setTitle(getResources().getString(R.string.information)).setMessage(getResources().getString(R.string.enterPhone))
                            .setPositiveButton(getResources().getString(R.string.ok), null).show();

                }else if(phoneNo.length()<10){
                    editTextPhoneNo.requestFocus();
                    editTextPhoneNo.requestFocus();
                    show = new AlertDialog.Builder(MainActivity.this)
                            .setTitle(getResources().getString(R.string.information))
                            .setMessage(
                                    getResources().getString(R.string.phoneLength))
                            .setPositiveButton("OK", null).show();
                }
              else if(newPwd.equals("")){

                    editTextNewPassword.requestFocus();
                    show = new AlertDialog.Builder(MainActivity.this)
                            .setTitle(getResources().getString(R.string.information)).setMessage(getResources().getString(R.string.enterNewPassword))
                            .setPositiveButton(getResources().getString(R.string.ok), null).show();

                }else if(confPwd.equals("")){
                    editTextConfirmPassword.requestFocus();
                            show = new AlertDialog.Builder(MainActivity.this)
                            .setTitle(getResources().getString(R.string.information)).setMessage(getResources().getString(R.string.enterConfiPassword))
                            .setPositiveButton(getResources().getString(R.string.ok), null).show();

                }else if(newPwd.length()<4){
                    editTextNewPassword.requestFocus();
                    show = new AlertDialog.Builder(MainActivity.this)
                            .setTitle(getResources().getString(R.string.information))
                            .setMessage(
                                    getResources().getString(R.string.passwordLength))
                            .setPositiveButton("OK", null).show();

                }else if(confPwd.length()<4){
                    editTextConfirmPassword.requestFocus();
                    show = new AlertDialog.Builder(MainActivity.this)
                            .setTitle(getResources().getString(R.string.information))
                            .setMessage(
                                    getResources().getString(R.string.passwordLength))
                            .setPositiveButton("OK", null).show();
                }


                else if(!confPwd.equals(newPwd)){
                    editTextNewPassword.setText("");
                    editTextConfirmPassword.requestFocus();
                    editTextConfirmPassword.setText("");
                    show = new AlertDialog.Builder(MainActivity.this)
                            .setTitle(getResources().getString(R.string.information))
                            .setMessage(getResources().getString(R.string.passwordNotMatching)
                            )
                            .setPositiveButton(getResources().getString(R.string.ok), null).show();

                }else{
                   startActivity(new Intent(this,RegistartionActivity.class));
                    // new RegisterComplaint().execute();

                }

                break;

            case R.id.btnLogin:
                 break;

           /* case R.id.btnChangeLanguage:
                NavDirections action2 = LoginFragmentDirections.actionLoginFragmentToLanguageFragment();
                Navigation.findNavController(view).navigate(action2);
                break;*/
        }
    }






    private class RegisterComplaint extends AsyncTask<Void, Integer, Void> {
        String responsefromserver = null;
        ProgressDialog mProgressDialog =null;
        //TODO
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Please Wait...");
            mProgressDialog.setMessage("This will take several minutes. Please wait...");
            //mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressDialog.setProgress(values[0]);
        }
        protected Void doInBackground(Void... params) {
            try {
                JSONArray ja1 = new JSONArray();
                JSONObject jsonobj = new JSONObject();
                try {
                    jsonobj.put("PhoneNo", phoneNo);
                    jsonobj.put("NewPassword", newPwd);
                    jsonobj.put("ConfirmPAssword", confPwd);
                    ja1.put(jsonobj);

                    SendRequest req = new SendRequest();
                    String url1 = "changePasswordFromMobileCustomers";
                    responsefromserver = req.uploadToServer(url1, ja1);

                    System.out.println("++++++++++++++++>>>>>>>>>>>>>++++++++++++++++++"+responsefromserver);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void unused) {

            if (responsefromserver == null)
            {
                show = new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getResources().getString(R.string.information))
                        .setMessage(getResources().getString(R.string.serverDown))
                        .setPositiveButton(getResources().getString(R.string.ok), null).show();
            }

            else if(responsefromserver.contains("Successfully Updated"))
            {
                mProgressDialog.dismiss();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Info")
                        .setMessage(responsefromserver)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MainActivity.this, RegistartionActivity.class));
                                finish();
                            }
                        }).show();
            }
            else
            {
                System.out.println(">>>>>>>>>>>>>>InputStraem>>>>>>>>>>>"+responsefromserver);
                mProgressDialog.dismiss();
            }
        }
    }


}
