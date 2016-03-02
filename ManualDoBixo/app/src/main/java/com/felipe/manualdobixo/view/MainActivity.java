package com.felipe.manualdobixo.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.felipe.manualdobixo.R;
import com.felipe.manualdobixo.repository.DataManager;
import com.felipe.manualdobixo.repository.Item;
import com.felipe.manualdobixo.repository.Topic;
import com.felipe.manualdobixo.view.listview.Card;
import com.felipe.manualdobixo.view.listview.CardListViewAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int SCREEN_TOPICS = 0;
    public static final int SCREEN_ITEMS = 1;

    int screen = SCREEN_TOPICS;
    long currentTopicId = -1;

    private RecyclerView recyclerView;
    private CardListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Fresco.initialize(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DataManager.createItems(this);

        recyclerView = (RecyclerView) findViewById(R.id.mb_main_recycler_view);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            StaggeredGridLayoutManager glm = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            glm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
            recyclerView.setLayoutManager(glm);

        } else {

            StaggeredGridLayoutManager glm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            glm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
            recyclerView.setLayoutManager(glm);

        }

        adapter = new CardListViewAdapter(this);
        recyclerView.setAdapter(adapter);

        doShowScreenTopics();

    }

    public void onItemSelected(Item item) {
        Log.i("DEBUG", "Item selected: " + item.getTitle());

        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("ITEM_ID", item.getId());

        startActivity(intent);
    }

    public void onTopicSelected(Topic topic) {
        doShowScreenItems(topic);
    }

    public void doShowScreenItems(Topic topic) {

        screen = SCREEN_ITEMS;
        currentTopicId = topic.getId();

        getSupportActionBar().setTitle(topic.getTitle());

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter.removeAllItems();

        List<Item> items = topic.getItems();
        for(Item i: items) {
            adapter.addCard(new Card(i.getTitle(), i.getText(), i.getImage(), i));
            Log.i("Debug", "Creating item: " + i.getTitle() + ", " + i.getImage());
        }

        adapter.setOnCardClickListener(new CardListViewAdapter.OnCardClickListener<Item>() {
            @Override
            public void onCardClicked(View view, Card<Item> card) {
                onItemSelected(card.getContent());
            }
        });

    }

    public void doShowScreenTopics() {

        screen = SCREEN_TOPICS;

        getSupportActionBar().setTitle(getString(R.string.app_name));

        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        adapter.removeAllItems();

        List<Topic> topics = Topic.listAll(Topic.class, "title");
        for(Topic t: topics) {
            adapter.addCard(new Card(t.getTitle(), "", t.getImage(), t));
            Log.i("Debug", "Creating topic: " + t.getTitle() + ", " + t.getImage());
        }

        adapter.setOnCardClickListener(new CardListViewAdapter.OnCardClickListener<Topic>() {
            @Override
            public void onCardClicked(View view, Card<Topic> card) {
                doShowScreenItems(card.getContent());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (screen == SCREEN_ITEMS)
            doShowScreenTopics();
        else
            super.onBackPressed();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putInt("SCREEN", screen);
        outState.putLong("CURRENT_TOPIC_ID", currentTopicId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        screen = savedInstanceState.getInt("SCREEN");
        currentTopicId = savedInstanceState.getLong("CURRENT_TOPIC_ID");
        if(screen == SCREEN_ITEMS)
            doShowScreenItems( Topic.findById(Topic.class, currentTopicId) );
        else
            doShowScreenTopics();
    }
}
