package com.pherodev.datingapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pherodev.datingapp.R;
import com.pherodev.datingapp.models.Person;

import java.util.ArrayList;
import java.util.Locale;

public class SearchResultsAdapter extends BaseAdapter {

    private final static String TAG = "Search";

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Person> masterSearchResults;
    private ArrayList<Person> filtered;

    public SearchResultsAdapter(Context context, ArrayList<Person> searchResults) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
        this.masterSearchResults = searchResults;
        this.filtered = new ArrayList<>();
        this.filtered.addAll(masterSearchResults);
    }

    public class ViewHolder {
        TextView nameTextView;
    }

    @Override
    public int getCount() {
        return filtered.size();
    }

    @Override
    public Person getItem(int position) {
        return filtered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // TODO: Add an OnClickListener to this.
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_result_item, null);
            // Locate the TextViews in listview_item.xml
            holder.nameTextView = (TextView) view.findViewById(R.id.text_view_search_result_item_header);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.nameTextView.setText(filtered.get(position).getName());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filtered.clear();
        if (charText.length() == 0) {
            filtered.addAll(masterSearchResults);
        } else {
            for (Person p : masterSearchResults) {
                if (p.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filtered.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

}
