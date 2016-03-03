package com.felipe.manualdobixo.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.felipe.manualdobixo.R;
import com.felipe.manualdobixo.repository.DataManager;
import com.felipe.manualdobixo.repository.Item;
import com.felipe.manualdobixo.repository.Topic;
import com.felipe.manualdobixo.view.listview.Card;
import com.felipe.manualdobixo.view.listview.CardListViewAdapter;
import com.felipe.manualdobixo.view.searchbox.MySearchBox;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    public static final int SCREEN_TOPICS = 0;
    public static final int SCREEN_ITEMS = 1;
    public static final int SCREEN_SEARCH = 2;

    Stack<ScreenState> screenStateStack;

    private RecyclerView recyclerView;
    private CardListViewAdapter adapter;
    private MySearchBox searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Fresco.initialize(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchBox = (MySearchBox) findViewById(R.id.mb_main_searchbox);
        searchBox.setActivity(this);
        searchBox.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                String term = searchBox.getSearchText();
                List<Item> items = Select.from(Item.class).where(Condition.prop("title").like("%" + term + "%")).list();
                setScreen(new ScreenState(SCREEN_SEARCH, term, items));

                Log.i("DEBUG", "Search opened!");

            }

            @Override
            public void onSearchClosed() {
                searchBox.hideCircularly(MainActivity.this);
                if (searchBox.getSearchText().isEmpty() && screenStateStack.peek().screenType == SCREEN_SEARCH) {
                    backScreen();
                }

                Log.i("DEBUG", "Search closed!");
            }

            @Override
            public void onSearchTermChanged(String term) {

                if (searchBox.getSearchOpen()) {
                    List<Item> items = Select.from(Item.class).where(Condition.prop("title").like("%" + term + "%")).list();
                    setScreen(new ScreenState(SCREEN_SEARCH, term, items));
                }

                Log.i("DEBUG", "Search term changed!");

            }

            @Override
            public void onSearch(String term) {

                List<Item> items = Select.from(Item.class).where(Condition.prop("title").like("%" + term + "%")).list();
                setScreen(new ScreenState(SCREEN_SEARCH, term, items));

                Log.i("DEBUG", "Search search!");

            }

            @Override
            public void onResultClick(SearchResult result) {

                String term = result.title;
                List<Item> items = Select.from(Item.class).where(Condition.prop("title").like("%" + term + "%")).list();
                setScreen(new ScreenState(SCREEN_SEARCH, term, items));

                Log.i("DEBUG", "Search result click!");

            }

            @Override
            public void onSearchCleared() {

                if (searchBox.getSearchText().isEmpty() && screenStateStack.peek().screenType == SCREEN_SEARCH) {
                    backScreen();
                }

                Log.i("DEBUG", "Search text cleared!");

            }

        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                searchBox.revealFromMenuItem(R.id.action_search, MainActivity.this);
                return true;
            }
        });

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

        DataManager.createItems(this, new DataManager.OnDataLoadedListener() {
            @Override
            public void onDataLoaded() {
                View pb = findViewById(R.id.mb_main_layout_progress);
                pb.setVisibility(View.GONE);
                setScreen(new ScreenState(SCREEN_TOPICS));
            }
        });

    }



    // Selection

    public void onItemSelected(long itemId) {

        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("ITEM_ID", itemId);

        startActivity(intent);

    }

    public void onTopicSelected(long topicId) {
        setScreen(
                new ScreenState(SCREEN_ITEMS, Topic.findById(Topic.class, topicId))
        );
    }



    // Showing

    public void setScreen(ScreenState state) {

        if(screenStateStack == null)
            screenStateStack = new Stack<>();

        if(screenStateStack.size() > 0
                && screenStateStack.peek().screenType == SCREEN_SEARCH
                && state.screenType == SCREEN_SEARCH)
            screenStateStack.pop();

        screenStateStack.push(state);

        switch (state.screenType) {
            case SCREEN_ITEMS:
                Log.i("DEBUG", "New state: SCREEN_ITEMS, " + screenStateStack.size());
                doShowScreenItems(state);
                break;
            case SCREEN_TOPICS:
                Log.i("DEBUG", "New state: SCREEN_TOPICS, " + screenStateStack.size());
                doShowScreenTopics(state);
                break;
            case SCREEN_SEARCH:
                Log.i("DEBUG", "New state: SCREEN_SEARCH, " + screenStateStack.size());
                doShowScreenSearch(state);
                break;
        }

    }

    public boolean backScreen() {

        screenStateStack.pop();

        if (screenStateStack.size() == 0) {
            Log.i("DEBUG", "Can't screen back! Size = 0!");
            return false;
        }

        setScreen(screenStateStack.pop());

        return true;

    }

    public void doShowScreenItems(ScreenState state) {

        Topic topic = (Topic) state.data[0];

        getSupportActionBar().setTitle(topic.getTitle());

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter.removeAllItems();

        List<Item> items = topic.getItems();
        for(Item i: items) {
            adapter.addCard(new Card(i.getTitle(), i.getText(), i.getImage(), i.getId()));
        }

        adapter.setOnCardClickListener(new CardListViewAdapter.OnCardClickListener<Long>() {
            @Override
            public void onCardClicked(View view, Card<Long> card) {
                onItemSelected(card.getContent());
            }
        });

        recyclerView.getLayoutManager().scrollToPosition(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((StaggeredGridLayoutManager)recyclerView.getLayoutManager()).invalidateSpanAssignments();
            }
        }, 100);


    }

    public void doShowScreenSearch(ScreenState state) {

        String search = (String) state.data[0];
        List<Item> items = (List) state.data[1];

        getSupportActionBar().setTitle(getString(R.string.mb_search_results_for) + " " + search);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter.removeAllItems();

        for(Item i: items) {
            adapter.addCard(new Card(i.getTitle(), i.getText(), i.getImage(), i.getId()));
        }

        adapter.setOnCardClickListener(new CardListViewAdapter.OnCardClickListener<Long>() {
            @Override
            public void onCardClicked(View view, Card<Long> card) {
                onItemSelected(card.getContent());
            }
        });

        recyclerView.getLayoutManager().scrollToPosition(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).invalidateSpanAssignments();
            }
        }, 100);

    }

    public void doShowScreenTopics(ScreenState state) {

        getSupportActionBar().setTitle(getString(R.string.mb_app_name));

        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        adapter.removeAllItems();

        List<Topic> topics = Topic.listAll(Topic.class, "title");
        for(Topic t: topics) {
            String subtitle = "";
            List<Item> items = t.getItems();
            for(Item i: items)
                subtitle += i.getTitle() + ", ";
            subtitle = subtitle.substring(0, subtitle.length() - 2);
            adapter.addCard(new Card(t.getTitle(), subtitle, t.getImage(), t.getId()));
        }

        adapter.setOnCardClickListener(new CardListViewAdapter.OnCardClickListener<Long>() {
            @Override
            public void onCardClicked(View view, Card<Long> card) {
                onTopicSelected(card.getContent());
            }
        });

        recyclerView.getLayoutManager().scrollToPosition(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).invalidateSpanAssignments();
            }
        }, 100);

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

        if(!backScreen())
            finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public class ScreenState {

        public int screenType;
        public Object[] data;

        public ScreenState(int screenType, Object... data) {
            this.data = data;
            this.screenType = screenType;
        }
    }

}
