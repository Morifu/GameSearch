package com.morfi.gamesearch;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.morfi.gamesearch.product.ProductContent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details
 * (if present) is a {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ItemListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ItemListActivity extends FragmentActivity
        implements ItemListFragment.Callbacks {

    private ProgressDialog pDialog;

    private JSONParser jParser = new JSONParser();

    // url for http get all products
    // private static String url_all_products_MYSQL = "http://31.172.184.17/android_connect/get_all_products.php";
    private String url_single_product_MYSQL = "http://31.172.184.17/android_connect/get_product_details.php";
    //private static String url_all_products_POSTGRESQL = "http://31.172.184.17:90/android_connect/get_all_products.php";
    private String url_single_product_POSTGRESQL = "http://31.172.184.17:90/android_connect/get_product_details.php";

    private String query;
    private String requestURL;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "pid";
    private static final String TAG_TITLE = "Title";
    private static final String TAG_GENRE = "Genre";
    private static final String TAG_PRICE = "Price";
    private static final String TAG_PRODUCER = "Producer";
    private static final String TAG_PLATFORM = "Platform";

    private static final String TAG_LIST_FRAGMENT = "list_tag";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    JSONArray products = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        // Show the Up button in the action bar.
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ItemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list_container))
                    .setActivateOnItemClick(true);


        }

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("DBMANAGER", "query is:" + query);


            ProductContent.ITEMS.clear();

            doSearch();


        } else {
            ItemListFragment fragment = ((ItemListFragment) getSupportFragmentManager()
                    .findFragmentByTag(TAG_LIST_FRAGMENT));
            if (fragment == null) {
                Log.d("DBMANAGER", "Fragment is null, creating new one");
                fragment = new ItemListFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.search_result_container, fragment)
                    .commit();


        }


    }

    private void doSearch() {
        Log.d("DBMANAGER", "SEARCHING QUERY");

        // Loading products in Background Thread
        new LoadAllProducts().execute();
    }

    /**
     * Callback method from {@link ItemListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID, id);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ItemDetailActivity.class);
            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // This ID represents the Home or Up button. In the case of this
//                // activity, the Up button is shown. Use NavUtils to allow users
//                // to navigate up one level in the application structure. For
//                // more details, see the Navigation pattern on Android Design:
//                //
//                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
//                //
//                NavUtils.navigateUpTo(this, new Intent(this, SearchActivity.class));
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    /**
     * Background Async Task to Load all product by making HTTP Request
     */
    class LoadAllProducts extends AsyncTask<String, String, String> {

        // list of php params
        List<NameValuePair> params = new ArrayList<NameValuePair>();


        private int search_id_count = 0;

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ItemListActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            // params have always title!
            params.add(new BasicNameValuePair("title", query));

            // get shared preferences instance
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            if (preferences.getBoolean("advanced_search_preference", false)) {
                Log.d("DBMANAGER", "PREFERENCES ADVANCED ARE ON!");

                // CHECK GENRES! -------------------------------------------
                Set<String> genres = null;
                genres = preferences.getStringSet("genres_list", genres);

                if (genres != null) {
                    String genres_str = "";
                    for (int i = 0; i < genres.size(); i++) {
                        if (i > 0)
                            genres_str += ",";
                        genres_str += genres.toArray()[i].toString();
                    }

                    params.add(new BasicNameValuePair("genre", genres_str));
                }

                // CHECK PRICES!! ------------------------------------------
                String prices;
                prices = preferences.getString("price_preference", "");

                if (!prices.isEmpty()) {
                    Log.d("DBMANAGER", "prices are : " + prices);
                    String[] minMax = prices.split("-");

                    params.add(new BasicNameValuePair("priceMin", minMax[0]));
                    params.add(new BasicNameValuePair("priceMax", minMax[1]));
                }

                // CHECK PRODUCERS -----------------------------------------
                Set<String> producers = null;
                producers = preferences.getStringSet("developers_list", producers);

                if (producers != null) {
                    String producers_str = "";
                    for (int i = 0; i < producers.size(); i++) {
                        if (i > 0)
                            producers_str += ",";
                        producers_str += producers.toArray()[i].toString();
                    }

                    params.add(new BasicNameValuePair("producer", producers_str));
                }

                // CHECK PLATFORM ------------------------------------------
                Set<String> platforms = null;
                platforms = preferences.getStringSet("platform_list", platforms);

                if (platforms != null) {
                    String producers_str = "";
                    for (int i = 0; i < platforms.size(); i++) {
                        if (i > 0)
                            producers_str += ",";
                        producers_str += platforms.toArray()[i].toString();
                    }

                    params.add(new BasicNameValuePair("producer", producers_str));
                }

                Log.d("DBMANAGER", "PARAMS ARE: " + params.toString());
            }

            search_id_count = 0;
        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {

            // TODO: setting up params based on preferences


            makeRequest(url_single_product_MYSQL);
            makeRequest(url_single_product_POSTGRESQL);

            return null;
        }

        private void makeRequest(String url) {
            JSONObject json = jParser.makeHttpRequest(url, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("DBMANAGER", "All products: " + json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String title = c.getString(TAG_TITLE);
                        String genre = c.getString(TAG_GENRE);
                        int price = c.getInt(TAG_PRICE);
                        String producer = c.getString(TAG_PRODUCER);
                        String platform = c.getString(TAG_PLATFORM);
                        Log.d("DBMANAGER", "ADDING TITLE: " + title);
                        ProductContent.addItem(new ProductContent.ProductItem(id + search_id_count, title, genre, price, producer, platform));
                        search_id_count++;
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
//                    Intent i = new Intent(getApplicationContext(),
//                            NewProductActivity.class);
//                    // Closing all previous activities
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
                    Log.d("DBMANAGER", "CANNOT SUCCESSFULLY DOWNLOAD ITEMS");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();


            ItemListFragment fragment = new ItemListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.search_result_container, fragment, TAG_LIST_FRAGMENT)
                    .commit();

            for (int i = 0; i < ProductContent.ITEMS.size(); i++)
                Log.d("DBPRODUCTS", "Product is: " + ProductContent.ITEMS.get(i).title);
            Log.d("DBMANAGER", "POST EXECUTE");


        }

    }

}
