package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactDBListActivity extends AppCompatActivity {

    // 뒤로 가기 버튼 만들기(mainactivity 로 돌아가기)
    private String TAG = ContactDBListActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView listView;
    private static String url = "http://143.248.36.159:3000/getdata";
    ArrayList<HashMap<String, String>> contactList;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactdblist);

        contactList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.lv_contactlist);

        new GetContacts().execute();
    }            /// bundle help send and recieve data between activities
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
//            pDialog = new ProgressDialog(ContactDBListActivity.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
            Log.d("디버깅", "onClick:09999999999999999999999999999999999999");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            Log.d("디버깅", "onCli0000000000000000000000000000000000000000");
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);    // here problem exists
           Log.d("디버깅", "onClick: ㅠㅠ");
            Log.e("디버깅", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//                    Log.d("디버깅", "onCli12345678912345678912345678912345678679으아아아아아아아");
                    // Getting JSON Array node
                    JSONArray contacts = new JSONArray(jsonStr);
                    Log.d("디버깅", "onCli12345678912345678912345678912345678679악으으으으으으으ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        Log.d("디버깅", "onCli12345678912345678912345678912345678679으아아아아아아아악");
                        JSONObject jsonObject = contacts.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String number = jsonObject.getString("phonenumber");
                        String email = jsonObject.getString("email");
//                        Log.d("디버깅", name);
//                        Log.d("디버깅", number);
//                        Log.d("디버깅", email);
                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("name", name);
                        contact.put("phonenumber", number);
                        contact.put("email",email);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
                 return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog!=null)
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

            ListAdapter adapter = new SimpleAdapter(
                    ContactDBListActivity.this, contactList,
                    R.layout.list_view, new String[]{"name", "phonenumber","email"}, new int[]{R.id.list_name, R.id.list_num, R.id.list_email});

            listView.setAdapter(adapter);
            Log.d("디버깅", "onCli12345678912345678912345678912345678679으아아아아아아아악");
        }
    }
}
