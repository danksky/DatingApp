package com.pherodev.datingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pherodev.datingapp.R;
import com.pherodev.datingapp.activities.ProfileActivity;
import com.pherodev.datingapp.models.Person;

import java.util.ArrayList;
import java.util.Locale;

import static com.pherodev.datingapp.activities.ProfileActivity.PERSON_SELECTED_KEY;

public class SearchResultsAdapter extends BaseAdapter {

    private final static String TAG = "Search";

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Person> masterSearchResults;
    private ArrayList<Person> filtered;

    public SearchResultsAdapter(Context context, ArrayList<Person> searchResults) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.masterSearchResults = searchResults;
        this.filtered = new ArrayList<>();
        this.filtered.addAll(masterSearchResults);
    }

    public void resetSearchResults(ArrayList<Person> searchResults) {
        masterSearchResults = searchResults;
        filtered.clear();
        filtered.addAll(masterSearchResults);
        notifyDataSetChanged();
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

    // TODO: Add an OnClickListener to this that launches profile activity with the provided information.
    // TODO: Pass a Bundle to the ProfileActivity for query.
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
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("users").document(filtered.get(position).userId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Bundle personBundle = new Bundle();
                                    Person p = task.getResult().toObject(Person.class);
                                    Log.d(TAG, "getUserById:" + p.name);
                                    personBundle.putParcelable(PERSON_SELECTED_KEY, p);
                                    Intent startProfileActivity = new Intent(context, ProfileActivity.class);
                                    startProfileActivity.putExtra(PERSON_SELECTED_KEY, personBundle);
                                    context.startActivity(startProfileActivity);
                                    // TODO: Figure out finishing an activity from adapter
                                } else {
                                    Log.e(TAG, "getUserById:failure");
                                }
                            }
                        });
            }
        });
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
