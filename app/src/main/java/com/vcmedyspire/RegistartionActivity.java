package com.vcmedyspire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.util.AmrMethods;
import com.util.SendRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class RegistartionActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextName,editFather,editTextAddress,editTextState,editTextCity,editTextArea,editTextPin,editTextEmployment;
    Spinner spinnerCity,spinnerState,spinnerPin,spinnerArea;
    TextView TextViewDOB;
    CheckBox checkBox1;
    Button submitRegister;
    String dobValue, name,fatherSpouse,address,state,city,area,pin,employment;
    String	category,divName;
    ArrayList<String> idCategoryArray= new ArrayList<String>();
    ArrayList<String> subCategoryidArray= new ArrayList<String>();
    ArrayList<String> idDivisionArray= new ArrayList<String>();
    ArrayList<String> subDivisionsidArray= new ArrayList<String>();
    String categoryId="",sitecode="",subcategoryId="",subDivisionId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registartion);
        editTextName=findViewById(R.id.editTextName);
        editFather=findViewById(R.id.editFather);
        editTextAddress=findViewById(R.id.editTextAddress);
        spinnerArea =findViewById(R.id.spinnerArea);
        spinnerPin=findViewById(R.id.spinnerPin);
        editTextEmployment=findViewById(R.id.editTextEmployment);
        submitRegister=findViewById(R.id.submitRegister);
        spinnerState=findViewById(R.id.spinnerState);
        spinnerCity=findViewById(R.id.spinnerCity);

        TextViewDOB=findViewById(R.id.TextViewDOB);
        TextViewDOB.setOnClickListener(this);


        if (AmrMethods.getDefaults("category", RegistartionActivity.this) == null || AmrMethods.getDefaults("division", RegistartionActivity.this) == null) {
            new DownloadCategory().execute();
        }
        else{
       // new DownloadCategory().execute();
            spinnerData();
       }

    }






    @Override
    protected void onStart() {
        super.onStart();

        submitRegister.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                name = editTextName.getText().toString().trim();
                fatherSpouse=editFather.getText().toString().trim();
                address=editTextAddress.getText().toString().trim();
             //   state=editTextState.getText().toString().trim();
               // city=editTextCity.getText().toString().trim();
                state = String.valueOf(spinnerState.getSelectedItem());
                city = String.valueOf(spinnerCity.getSelectedItem());
                area=String.valueOf(spinnerArea.getSelectedItem());
                pin=String.valueOf(spinnerPin.getSelectedItem());
                employment=editTextEmployment.toString().trim();

                startActivity(new Intent(RegistartionActivity.this,OTPActivity.class));

            }
        });



    }

    public void onRadioBtnClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radioBtnGender:
                if (checked) {
                    String gender ="male";
                    Toast.makeText(this, "gender==>"+gender, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.radioBtnFemale:
                if (checked) {
                    String gender ="Female";
                    Toast.makeText(this, "gender==>"+gender, Toast.LENGTH_SHORT).show();


                }
                break;
            case R.id.radioBtnmaried:
                if (checked) {
                    String merital ="maried";
                    Toast.makeText(this, "Merital==>"+merital, Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.radioBtnSingle:
                if (checked) {
                    String merital ="Single";
                    Toast.makeText(this, "Merital==>"+merital, Toast.LENGTH_SHORT).show();

                }
                break;

        }

    }


    private class DownloadCategory extends AsyncTask<Void, Integer, Void> {
        String responsefromserver = null;
        ProgressDialog mProgressDialog =null;

        //TODO
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(RegistartionActivity.this);
            mProgressDialog.setMessage("Please wait...");
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
                JSONObject jsonarray = new JSONObject();
                try {
                    jsonarray.put("Sub-Category", "");
                    ja1.put(jsonarray);

                    SendRequest req = new SendRequest();
                    String url1 = "getListOfCategory";
                    responsefromserver = req.uploadToServer(url1, ja1);

                    System.out.println("++++++++++++++++>>>>>>>>>>>>>++++++++++++++++++"+ja1);
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
            mProgressDialog.dismiss();
            if (responsefromserver == null)
            {
                new AlertDialog.Builder(RegistartionActivity.this)
                        .setTitle("Info")
                        .setMessage("Sorry,connection cannot made at this time. Server is down. Please try again later")
                        .setPositiveButton("OK", null).show();
            }

            else	if (responsefromserver .contains("html"))
            {
                new AlertDialog.Builder(RegistartionActivity.this)
                        .setTitle("Info")
                        .setMessage("Sorry,connection cannot made at this time. Server is down. Please try again later")
                        .setPositiveButton("OK", null).show();
            }

            else
            {
                System.out.println(">>>>>>>>>>>>>>InputStraem>>>>>>>>>>>"+responsefromserver);

                AmrMethods.setDefaults("category", responsefromserver, RegistartionActivity.this);
                new DownloadSubCategory().execute();

            }
        }
    }

    private class DownloadSubCategory extends AsyncTask<Void, Integer, Void> {
        String responsefromserver = null;
        ProgressDialog mProgressDialog =null;

        //TODO
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(RegistartionActivity.this);
            mProgressDialog.setMessage("Please wait...");
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
                JSONObject jsonarray = new JSONObject();
                try {
                    jsonarray.put("Sub-Category", "");
                    ja1.put(jsonarray);

                    SendRequest req = new SendRequest();
                    String url1 = "getListOfSubcategory";
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
            mProgressDialog.dismiss();
            if (responsefromserver == null)
            {
                new AlertDialog.Builder(RegistartionActivity.this)
                        .setTitle("Info")
                        .setMessage("Sorry,connection cannot made at this time. Server is down. Please try again later")
                        .setPositiveButton("OK", null).show();
            }

            else	if (responsefromserver .contains("html"))
            {
                new AlertDialog.Builder(RegistartionActivity.this)
                        .setTitle("Info")
                        .setMessage("Sorry,connection cannot made at this time. Server is down. Please try again later")
                        .setPositiveButton("OK", null).show();
            }

            else
            {
                System.out.println(">>>>>>>>>>>>>>InputStraem>>>>>>>>>>>"+responsefromserver);

                AmrMethods.setDefaults("subCategory", responsefromserver, RegistartionActivity.this);
                new DownloadDivision().execute();

            }
        }
    }


    private class DownloadDivision extends AsyncTask<Void, Integer, Void> {
        String responsefromserver = null;
        ProgressDialog mProgressDialog =null;

        //TODO
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(RegistartionActivity.this);
            mProgressDialog.setMessage("Please wait...");
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
                JSONObject jsonarray = new JSONObject();
                try {
                    ja1.put(jsonarray);

                    SendRequest req = new SendRequest();
                    String url1 = "getListOfDivistionCode";
                    responsefromserver = req.uploadToServer(url1, ja1);

                    System.out.println("++++++++++++++++divisions++++++++++++++++++"+responsefromserver);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;


        }

        protected void onPostExecute(Void unused) {
            mProgressDialog.dismiss();
            if (responsefromserver == null)
            {
                new AlertDialog.Builder(RegistartionActivity.this)
                        .setTitle("Info")
                        .setMessage("Sorry,connection cannot made at this time. Server is down. Please try again later")
                        .setPositiveButton("OK", null).show();
            }

            else	if (responsefromserver .contains("html"))
            {
                new AlertDialog.Builder(RegistartionActivity.this)
                        .setTitle("Info")
                        .setMessage("Sorry,connection cannot made at this time. Server is down. Please try again later")
                        .setPositiveButton("OK", null).show();
            }

            else
            {
                System.out.println(">>>>>>>>>>>>>>InputStraem>>>>>>>>>>>"+responsefromserver);

                AmrMethods.setDefaults("division", responsefromserver, RegistartionActivity.this);
                new DownloadSubDivision().execute();

            }
        }
    }

    private class DownloadSubDivision extends AsyncTask<Void, Integer, Void> {
        String responsefromserver = null;
        ProgressDialog mProgressDialog =null;

        //TODO
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(RegistartionActivity.this);
            mProgressDialog.setMessage("Please wait...");
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
                JSONObject jsonarray = new JSONObject();
                try {
                    jsonarray.put("Sub-Category", "");
                    ja1.put(jsonarray);

                    SendRequest req = new SendRequest();
                    String url1 = "getListOfSiteCode";
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
            mProgressDialog.dismiss();
            if (responsefromserver == null)
            {
                new AlertDialog.Builder(RegistartionActivity.this)
                        .setTitle("Info")
                        .setMessage("Sorry,connection cannot made at this time. Server is down. Please try again later")
                        .setPositiveButton("OK", null).show();
            }

            else	if (responsefromserver .contains("<html>"))
            {
                new AlertDialog.Builder(RegistartionActivity.this)
                        .setTitle("Info")
                        .setMessage("Sorry,connection cannot made at this time. Server is down. Please try again later")
                        .setPositiveButton("OK", null).show();
            }

            else
            {
                System.out.println(">>>>>>>>>>>>>>InputStraem>>>>>>>>>>>"+responsefromserver);

                AmrMethods.setDefaults("subDivisions", responsefromserver, RegistartionActivity.this);
                spinnerData();

            }
        }
    }


    public  ArrayList<String> getCategory(Context context, String responsefromserver) {
        ArrayList<String> categoryArray=new ArrayList<String>();

        if (responsefromserver == null) {

            Toast.makeText(context, "Please Refresh", Toast.LENGTH_SHORT).show();

        } else if (responsefromserver.contains("<html>")) {

            Toast.makeText(context, "Please Refresh", Toast.LENGTH_SHORT).show();


        } else if (responsefromserver.equals("no_data")) {

            //	Toast.makeText(context, "No Data Found",Toast.LENGTH_SHORT).show();
        } else {
            // Log.e("Response", responsefromserver);
            try {
                JSONArray jsonarray = new JSONArray(responsefromserver);
                JSONObject jsondata = null;
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsondata = jsonarray.getJSONObject(i);
                    categoryArray.add(jsondata.getString("categoryName").trim());
                    idCategoryArray.add(jsondata.getString("categoryId").trim());

                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        return categoryArray;
    }

    public  ArrayList<String> getSubCategory(Context context,String responsefromserver) {
        ArrayList<String> categoryArray=new ArrayList<String>();

        if (responsefromserver == null) {

            Toast.makeText(context, "Please Refresh", Toast.LENGTH_SHORT).show();

        } else if (responsefromserver.contains("<html>")) {

            Toast.makeText(context, "Please Refresh", Toast.LENGTH_SHORT).show();


        } else if (responsefromserver.equals("no_data")) {

            //	Toast.makeText(context, "No Data Found",Toast.LENGTH_SHORT).show();
        } else {
            // Log.e("Response", responsefromserver);
            try {
                JSONArray jsonarray = new JSONArray(responsefromserver);
                JSONObject jsondata = null;
                subCategoryidArray= new ArrayList<String>();
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsondata = jsonarray.getJSONObject(i);
                    if(categoryId.equals(jsondata.getString("Categoryid").trim())){
                        subCategoryidArray.add(jsondata.getString("subCategoryid").trim());
                        categoryArray.add(jsondata.getString("subCategoryName").trim());
                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        return categoryArray;
    }


    public  ArrayList<String> getDivisions(Context context,String responsefromserver) {
        ArrayList<String> divisionArray=new ArrayList<String>();

        if (responsefromserver == null) {

            Toast.makeText(context, "Please Refresh", Toast.LENGTH_SHORT).show();

        } else if (responsefromserver.contains("<html>")) {

            Toast.makeText(context, "Please Refresh", Toast.LENGTH_SHORT).show();


        } else if (responsefromserver.equals("no_data")) {

            //	Toast.makeText(context, "No Data Found",Toast.LENGTH_SHORT).show();
        } else {
            // Log.e("Response", responsefromserver);
            try {
                JSONArray jsonarray = new JSONArray(responsefromserver);
                JSONObject jsondata = null;
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsondata = jsonarray.getJSONObject(i);
                    divisionArray.add(jsondata.getString("divisionName").trim());
                    idDivisionArray.add(jsondata.getString("divisionSiteCode").trim());

                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        return divisionArray;
    }

    public  ArrayList<String> getSubDivision(Context context,String responsefromserver) {
        ArrayList<String> subDivArray=new ArrayList<String>();

        if (responsefromserver == null) {

            Toast.makeText(context, "Please Refresh", Toast.LENGTH_SHORT).show();

        } else if (responsefromserver.contains("<html>")) {

            Toast.makeText(context, "Please Refresh", Toast.LENGTH_SHORT).show();


        } else if (responsefromserver.equals("no_data")) {

            //	Toast.makeText(context, "No Data Found",Toast.LENGTH_SHORT).show();
        } else {
            // Log.e("Response", responsefromserver);
            try {
                JSONArray jsonarray = new JSONArray(responsefromserver);
                JSONObject jsondata = null;
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsondata = jsonarray.getJSONObject(i);
                    if(sitecode.equals(jsondata.getString("divistionCode").trim())){
                        subDivisionsidArray.add(jsondata.getString("siteCode").trim());
                        subDivArray.add(jsondata.getString("subDivisionName").trim());
                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        return subDivArray;
    }


    private void spinnerData(){
        if (AmrMethods.getDefaults("category", RegistartionActivity.this) != null) {
            String	category =AmrMethods.getDefaults("category",	RegistartionActivity.this);
            System.out.println();
            ArrayList<String> categorryArray=	getCategory(RegistartionActivity.this, category);
            ArrayAdapter<String> adapter= new ArrayAdapter<String>(RegistartionActivity.this, android.R.layout.simple_dropdown_item_1line, categorryArray);
            spinnerState.setAdapter(adapter);
        }

        if (AmrMethods.getDefaults("subCategory", RegistartionActivity.this) != null) {
            category =AmrMethods.getDefaults("subCategory",	RegistartionActivity.this);
            System.out.println();
            ArrayList<String> categorryArray=	getSubCategory(RegistartionActivity.this, category);
            ArrayAdapter<String> adapter= new ArrayAdapter<String>(RegistartionActivity.this, android.R.layout.simple_dropdown_item_1line, categorryArray);
            spinnerCity.setAdapter(adapter);
        }

        if (AmrMethods.getDefaults("division", RegistartionActivity.this) != null) {
            String divisionName =AmrMethods.getDefaults("division",	RegistartionActivity.this);
            ArrayList<String> categorryArray=	getDivisions(RegistartionActivity.this, divisionName);
            ArrayAdapter<String> adapter= new ArrayAdapter<String>(RegistartionActivity.this, android.R.layout.simple_dropdown_item_1line, categorryArray);
            spinnerArea.setAdapter(adapter);
        }
        if (AmrMethods.getDefaults("subDivisions", RegistartionActivity.this) != null) {
            divName =AmrMethods.getDefaults("subDivisions",	RegistartionActivity.this);
            System.out.println();
            ArrayList<String> categorryArray=	getSubDivision(RegistartionActivity.this, divName);
            ArrayAdapter<String> adapter= new ArrayAdapter<String>(RegistartionActivity.this, android.R.layout.simple_dropdown_item_1line, categorryArray);
            spinnerPin.setAdapter(adapter);
        }


        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,	int arg2, long arg3) {
                categoryId=idCategoryArray.get(arg2);
                //Toast.makeText(getApplicationContext(), categoryId, Toast.LENGTH_SHORT).show();
                ArrayList<String> categorryArray=	getSubCategory(RegistartionActivity.this, category);
                ArrayAdapter<String> adapter= new ArrayAdapter<String>(RegistartionActivity.this, android.R.layout.simple_dropdown_item_1line, categorryArray);
                spinnerCity.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                subcategoryId=subCategoryidArray.get(position);
                //Toast.makeText(getApplicationContext(), subcategoryId, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,	int arg2, long arg3) {
                sitecode=idDivisionArray.get(arg2);
                //Toast.makeText(getApplicationContext(), sitecode, Toast.LENGTH_SHORT).show();
                ArrayList<String> DivisionsArray=	getSubDivision(RegistartionActivity.this, divName);
                ArrayAdapter<String> adapter= new ArrayAdapter<String>(RegistartionActivity.this, android.R.layout.simple_dropdown_item_1line, DivisionsArray);
                spinnerPin.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        spinnerPin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                subDivisionId=subDivisionsidArray.get(position);
                //Toast.makeText(getApplicationContext(), subDivisionId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }







    @Override
    public void onClick(View v) {

        if (v == TextViewDOB) {

            Calendar c1 = Calendar.getInstance();
//			c1.add(Calendar.MONTH, 1);
            int mYear = c1.get(Calendar.YEAR);
            int mMonth = c1.get(Calendar.MONTH);
            int mDay = c1.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            String monthString;
                            if ((monthOfYear + 1) > 9) {
                                monthString = (monthOfYear + 1) + "";
                            } else {
                                monthString = "0" + (monthOfYear + 1);
                            }
                            TextViewDOB.setText(dayOfMonth+"/"+ (monthString)+"/"+year );
                            dobValue=TextViewDOB.getText().toString().trim();

                        }
                    }, mYear, mMonth, mDay);
           // datePickerDialog.getDatePicker().findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
           datePickerDialog.getDatePicker().findViewById(Resources.getSystem().getIdentifier("day", "id", "android"));
            datePickerDialog.show();
            c1.add(Calendar.MONTH, 1);
            datePickerDialog.getDatePicker().setMaxDate(c1.getTimeInMillis());
        }

    }





}
