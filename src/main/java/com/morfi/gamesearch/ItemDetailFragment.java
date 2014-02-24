package com.morfi.gamesearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.morfi.gamesearch.product.ProductContent;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {

    private Button buyBtn;
    private Button editBtn;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private ProductContent.ProductItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            mItem = ProductContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.detail_title)).setText(getString(R.string.title_label) + " " + mItem.title);
            ((TextView) rootView.findViewById(R.id.detail_price)).setText(getString(R.string.price_label) + " " + mItem.price + " " + getString(R.string.price_currency));
            ((TextView) rootView.findViewById(R.id.detail_genre)).setText(getString(R.string.genre_label) + " " + mItem.genre);
            ((TextView) rootView.findViewById(R.id.detail_producer)).setText(getString(R.string.producer_label) + " " + mItem.producer);
            ((TextView) rootView.findViewById(R.id.detail_platform)).setText(getString(R.string.platform_label) + " " + mItem.platform);

        }

        buyBtn = (Button) rootView.findViewById(R.id.buy_btn);
        editBtn = (Button) rootView.findViewById(R.id.edit_btn);

        editBtn.setEnabled(MainActivity.adminMode);
        editBtn.setVisibility(MainActivity.adminMode ? View.VISIBLE : View.INVISIBLE);

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }

}
