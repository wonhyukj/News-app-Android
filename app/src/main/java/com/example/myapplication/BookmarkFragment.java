
package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookmarkFragment extends Fragment {
    private List<News> news;
    private RecyclerView recyclerView;
    private TextView textView;

    static BookmarkFragment newInstance() {
        return new BookmarkFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        news = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.news = extractBookmarks();
        BookmarkAdapter bookmarkAdapter = new BookmarkAdapter(getApplicationContext(), this.news, textView);
        recyclerView.setAdapter(bookmarkAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_bookmark, container, false);
        recyclerView = root.findViewById(R.id.recyclerViewBookmark);
        textView = root.findViewById(R.id.bookmark_none);
        this.news = extractBookmarks();
        BookmarkAdapter bookmarkAdapter = new BookmarkAdapter(getApplicationContext(), this.news, textView);
        recyclerView.setAdapter(bookmarkAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        return root;
    }

    private Activity getMainActivity() {
        return Objects.requireNonNull(getActivity());
    }

    private Context getApplicationContext() {
        return getMainActivity().getApplicationContext();
    }

    private ArrayList<News> extractBookmarks() {
        return SharedPreference.getAllSavedObjectFromPreference(getMainActivity(), "storage");
    }
}
