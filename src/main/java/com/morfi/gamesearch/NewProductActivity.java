package com.morfi.gamesearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewProductActivity extends ActionBarActivity {

    public static Context context;
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputTitle;
    EditText inputPrice;
    EditText inputPlatform;
    EditText inputGenre;
    EditText inputProducer;

    // url to create new product
    private static String url_create_product = "http://orohimaru.zebromalz.info/android/create_product.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        context = getApplicationContext();

        inputTitle = (EditText) findViewById(R.id.inputTitle);
        inputPrice = (EditText) findViewById(R.id.inputPrice);
        inputPlatform = (EditText) findViewById(R.id.inputPlatform);
        inputGenre = (EditText) findViewById(R.id.inputGenre);
        inputProducer = (EditText) findViewById(R.id.inputProducer);

        Button createBtn = (Button) findViewById(R.id.create_btn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = inputTitle.getText().toString();
                String price = inputPrice.getText().toString();
                String platform = inputPlatform.getText().toString();
                String genre = inputGenre.getText().toString();
                String producer = inputProducer.getText().toString();
                if (title.length() < 1 || price.length() < 1 || platform.length() < 1 || genre.length() < 1 || producer.length() < 1) {
                    Toast.makeText(getApplicationContext(), R.string.create_empty_fields_error, Toast.LENGTH_LONG).show();
                    return;
                }
                new CreateNewProduct().execute();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpTo(this, new Intent(this, AdminActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Background Async Task to Create new product
     */
    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewProductActivity.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {
            String title = inputTitle.getText().toString();
            String price = inputPrice.getText().toString();
            String platform = inputPlatform.getText().toString();
            String genre = inputGenre.getText().toString();
            String producer = inputProducer.getText().toString();


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("title", title));
            params.add(new BasicNameValuePair("price", price));
            params.add(new BasicNameValuePair("platform", platform));
            params.add(new BasicNameValuePair("genre", genre));
            params.add(new BasicNameValuePair("producer", producer));
            Log.d("GAMESEARCH", "params for create post are: " + params.toString());
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product, "POST", params);

            // check log cat for response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), ItemListActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    Toast.makeText(context, R.string.create_failed, Toast.LENGTH_LONG).show();
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }

}
