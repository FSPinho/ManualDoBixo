package com.felipe.manualdobixo.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.TextAlignment;
import com.facebook.drawee.view.SimpleDraweeView;
import com.felipe.manualdobixo.R;
import com.felipe.manualdobixo.repository.Item;
import com.orm.SugarRecord;

public class ItemActivity extends AppCompatActivity {

    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if(intent != null) {

            long itemId = intent.getLongExtra("ITEM_ID", -1);
            if(itemId != -1) {

                item = SugarRecord.findById(Item.class, itemId);
                Log.i("DEBUG", "Showing item: " + item.getTitle());

                getSupportActionBar().setTitle(item.getTitle());

                LinearLayout layoutText = (LinearLayout) findViewById(R.id.mb_item_layout_text);
                String[] paragraphs = item.getText().split("\\r?\\n");
                for(String p: paragraphs) {
                    DocumentView documentView = new DocumentView(this, DocumentView.PLAIN_TEXT);
                    documentView.getDocumentLayoutParams().setTextAlignment(TextAlignment.JUSTIFIED);
                    documentView.getDocumentLayoutParams().setAntialias(true);
                    documentView.getDocumentLayoutParams().setTextColor(getResources().getColor(R.color.md_grey_800));
                    documentView.setText("        " + p);
                    layoutText.addView(documentView);

                    Log.i("DEBUG", "Creating a new paragraph: " + p);
                }

                SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.mb_item_image);
                Uri uri = Uri.parse("asset:///" + item.getImage());
                draweeView.setImageURI(uri);

                ProgressBar pb = (ProgressBar) findViewById(R.id.mb_item_loading);
                pb.setVisibility(View.GONE);

            } else {
                Log.i("DEBUG", "Item not found!");
            }

        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

}
