package com.util;

/*
@copy right Chetan RM
created date 31/01/2021
 */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;

import android.util.Log;

public class SendRequest {

	//String url1="http://192.168.2.18:7075/Vspire";//live sangeetha given
	String url1="http://45.127.101.236:8080/pgrs/";//live sangeetha given


	    
		static int conTimeOut = 15000;
		static int readTimeOut =25000;
	    
		public String uploadToServer(String methodName, JSONArray obj) {
			BufferedReader reader = null;
			try {
				URL url = new URL(url1 + methodName);
				System.out.println("login Url------>"+url);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setConnectTimeout(conTimeOut);
				con.setReadTimeout(readTimeOut);
				con.setDoOutput(true);
				con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
				byte[] outputBytes = obj.toString().getBytes("UTF-8");
				OutputStream os = con.getOutputStream();
				os.write(outputBytes);
				os.flush();
				os.close();
				StringBuilder sb = new StringBuilder();
				reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {  
					sb.append(line + "\n");  
				}
				line = sb.toString();  

				Log.i("RESPONSE", line);
				return line.trim();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close(); // Closing the
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}
		
		
		public String getRegisterationDetails(String methodName,String accountID) {
			StringBuilder result = new StringBuilder();
			 HttpURLConnection urlConnection = null;
		    try {

                String urlCESC = url1+methodName+"/"+accountID ;
                System.out.println(urlCESC);
                URL url = new URL(urlCESC);
                urlConnection = (HttpURLConnection) url.openConnection(/*proxy*/);
                urlConnection.setDoInput(true);
                urlConnection.setConnectTimeout(conTimeOut);
                urlConnection.setReadTimeout(readTimeOut);

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
            	try {
					if(urlConnection!=null)
					urlConnection.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
            }


            return result.toString();
		}
		
		

}
