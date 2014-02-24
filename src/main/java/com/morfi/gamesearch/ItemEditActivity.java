package com.morfi.gamesearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.morfi.gamesearch.product.ProductContent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemEditActivity extends ActionBarActivity {

    public static Context context;
    public static ItemEditActivity instance;

    // url to create new product
    private static String url_update_product = "http://orohimaru.zebromalz.info/android/update_product.php";
    private static String url_delete_product = "http://orohimaru.zebromalz.info/android/delete_product.php";

    private ProgressDialog pDialog;

    private JSONParser jParser = new JSONParser();

    EditText inputTitle;
    EditText inputPrice;
    EditText inputPlatform;
    EditText inputGenre;
    EditText inputProducer;


    /**
     * The dummy content this fragment is presenting.
     */
    private ProductContent.ProductItem mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);
        context = getApplicationContext();
        instance = this;
        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID));
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item_edit, menu);
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
                NavUtils.navigateUpTo(this, new Intent(this, ItemListActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.----------------------
     */
    public static class PlaceholderFragment extends Fragment {

        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        public static final String ARG_ITEM_ID = "item_id";

        Button saveEditBtn;
        Button deleteEditBtn;

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getArguments().containsKey(ItemDetailFragment.ARG_ITEM_ID)) {

                instance.mItem = ProductContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_item_edit, container, false);

            if (instance.mItem != null) {
                instance.inputTitle = (EditText) rootView.findViewById(R.id.inputTitle);
                instance.inputPrice = (EditText) rootView.findViewById(R.id.inputPrice);
                instance.inputPlatform = (EditText) rootView.findViewById(R.id.inputPlatform);
                instance.inputGenre = (EditText) rootView.findViewById(R.id.inputGenre);
                instance.inputProducer = (EditText) rootView.findViewById(R.id.inputProducer);

                instance.inputTitle.setText(instance.mItem.title);
                instance.inputPrice.setText(Integer.toString(instance.mItem.price));
                instance.inputPlatform.setText(instance.mItem.platform);
                instance.inputGenre.setText(instance.mItem.genre);
                instance.inputProducer.setText(instance.mItem.producer);
            }

            saveEditBtn = (Button) rootView.findViewById(R.id.save_edit_btn);
            deleteEditBtn = (Button) rootView.findViewById(R.id.delete_btn);

            saveEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    instance.doSave();
                }
            });

            deleteEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    instance.doDelete();
                }
            });

            return rootView;
        }

    }

    // ---------------- FRAGMENT END


    public void doSave() {
        new SaveProductDetails().execute();
    }

    public void doDelete() {
        new DeleteProduct().execute();
    }

    /**
     * Background Async Task to  Save product Details
     */
    class SaveProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ItemEditActivity.this);
            pDialog.setMessage(getString(R.string.saving_label));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving product
         */
        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
            mItem.title = inputTitle.getText().toString();
            mItem.price = Integer.parseInt(inputPrice.getText().toString());
            mItem.platform = inputPlatform.getText().toString();
            mItem.genre = inputGenre.getText().toString();
            mItem.producer = inputProducer.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(ProductContent.TAG_PID, mItem.pid));
            params.add(new BasicNameValuePair(ProductContent.TAG_BASEID, mItem.baseID));
            params.add(new BasicNameValuePair(ProductContent.TAG_TITLE, mItem.title));
            params.add(new BasicNameValuePair(ProductContent.TAG_PRICE, Integer.toString(mItem.price)));
            params.add(new BasicNameValuePair(ProductContent.TAG_PLATFORM, mItem.platform));
            params.add(new BasicNameValuePair(ProductContent.TAG_GENRE, mItem.genre));
            params.add(new BasicNameValuePair(ProductContent.TAG_PRODUCER, mItem.producer));

            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jParser.makeHttpRequest(url_update_product, "POST", params);

            // check json success tag
            try {
                int success = json.getInt(ProductContent.TAG_SUCCESS);

                if (success == 1) {
                    Intent i = new Intent(getApplicationContext(), ItemListActivity.class);
                    i.putExtra("ALL_PRODUCTS", true);
                    startActivity(i);
                    // closing this screen
                    finish();
                } else {
                    Log.d("GAMESEARCH", "Failed to update product");
                    // failed to update product
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
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }

    /**
     * **************************************************************
     * Background Async Task to Delete Product
     */
    class DeleteProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ItemEditActivity.this);
            pDialog.setMessage(getString(R.string.deleting_label));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Deleting product
         */
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(ProductContent.TAG_PID, mItem.pid));
                params.add(new BasicNameValuePair(ProductContent.TAG_BASEID, mItem.baseID));

                // getting product details by making HTTP request
                JSONObject json = jParser.makeHttpRequest(url_delete_product, "POST", params);

                // check your log for json response
                Log.d("Delete Product", json.toString());

                // json success tag
                success = json.getInt(ProductContent.TAG_SUCCESS);
                if (success == 1) {
                    Intent i = new Intent(getApplicationContext(), ItemListActivity.class);
                    i.putExtra("ALL_PRODUCTS", true);
                    startActivity(i);
                    // closing this screen
                    finish();
                } else {
                    Log.d("GAMESEARCH", "Failed to delete product");
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
            // dismiss the dialog once product deleted
            pDialog.dismiss();

        }

    }

}
