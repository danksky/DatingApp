package com.pherodev.datingapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.pherodev.datingapp.R;
import com.pherodev.datingapp.adapters.SearchResultsAdapter;
import com.pherodev.datingapp.models.Person;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchSearchView;
    private SearchResultsAdapter searchResultsAdapter;
    private ListView searchResultsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchSearchView = (SearchView) findViewById(R.id.search_view_search);
        searchResultsAdapter = new SearchResultsAdapter(this, getFakeList());
        searchResultsListView = (ListView) findViewById(R.id.list_view_search_results);

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

    // TODO: Replace this with a query to Firebase
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
}
