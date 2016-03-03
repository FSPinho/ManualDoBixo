package com.felipe.manualdobixo.view;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
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

                // Carregando texto...
                String[] paragraphs = item.getText().split("\\r?\\n");
                String text = "<html> <body>";;
                for(String p: paragraphs) {

                    Spannable sp = new SpannableString(p);
                    Linkify.addLinks(sp, Linkify.ALL);
                    p = Html.toHtml(sp) ;
                    if(p.startsWith("<p")) {
                        p = p.replace("<p", "<p style=\"text-align:justify; text-indent: 32px;\"");
                    }

                    text += p;

                }
                text += "</body></Html>";

                WebView webView = (WebView) findViewById(R.id.mb_item_webview);
                webView.loadData(text, "text/html; charset=utf-8", "utf-8");

                // Carregando imagem
                SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.mb_item_image);

                Uri uri = Uri.parse(item.getImage());
                Log.i("DEBUG", "Loading image: " + item.getImage());

                ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet( String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                        ProgressBar pb = (ProgressBar) findViewById(R.id.mb_item_loading);
                        pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                        ProgressBar pb = (ProgressBar) findViewById(R.id.mb_item_loading);
                        pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        ProgressBar pb = (ProgressBar) findViewById(R.id.mb_item_loading);
                        pb.setVisibility(View.GONE);
                    }
                };

                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setControllerListener(controllerListener)
                        .setUri(uri)
                        .build();

                draweeView.setController(controller);


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
