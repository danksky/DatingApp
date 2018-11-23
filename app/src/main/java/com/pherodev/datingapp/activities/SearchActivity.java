package com.pherodev.datingapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pherodev.datingapp.R;
import com.pherodev.datingapp.adapters.SearchResultsAdapter;
import com.pherodev.datingapp.models.Person;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public class SearchActivity extends AppCompatActivity {

    private final static String TAG = "Search";

    // Data
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<Person> personList;

    // View control
    private SearchView searchSearchView;
    private SearchResultsAdapter searchResultsAdapter;
    private ListView searchResultsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // data
        firebaseFirestore = FirebaseFirestore.getInstance();
        personList = new ArrayList<>();
        getPersonList();

        // initialize views
        searchSearchView = (SearchView) findViewById(R.id.search_view_search);
        searchResultsAdapter = new SearchResultsAdapter(this, personList);
        searchResultsListView = (ListView) findViewById(R.id.list_view_search_results);

        // control views
        searchResultsListView.setAdapter(searchResultsAdapter);
        searchSearchView.setQueryHint(getText(R.string.search_activities_search_hint));
        searchSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                searchResultsAdapter.filter(text);
                return false;
            }
        });
    }

    // TODO: Replace this with a Firestore seeder
    private ArrayList<Person> getFakeList() {
        ArrayList<Person> fakeList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            StringBuilder randomName = new StringBuilder();
            for (int j = 0; j < 10; j++)
                randomName.append((char) ('a' + Math.random() * 26));
            randomName.insert(0, ' ');
            String name = i + randomName.toString();
            String email = i + "@gmail.com";
            fakeList.add(new Person(UUID.randomUUID().toString(), name, email));
        }
        return fakeList;
    }

    private void getPersonList() {
        personList.clear();
        firebaseFirestore.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "getPersonList:success");
                            for (QueryDocumentSnapshot qds : task.getResult()) {
                                Log.d(TAG, qds.getId() + " => " + qds.toObject(Person.class).name);
                                personList.add(qds.toObject(Person.class));
                            }
                            searchResultsAdapter.resetSearchResults(personList);
                            searchResultsAdapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, "getPersonList:" + task.getException());
                        }
                    }
                });
    }
}
