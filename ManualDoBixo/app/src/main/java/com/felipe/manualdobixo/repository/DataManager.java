package com.felipe.manualdobixo.repository;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.felipe.manualdobixo.repository.webapi.ManualContent;
import com.felipe.manualdobixo.repository.webapi.ManualContentService;
import com.felipe.manualdobixo.repository.webapi.OnReceiveListener;
import com.felipe.manualdobixo.repository.webapi.UpdateControl;
import com.orm.SugarRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by felipe on 01/03/16.
 */
public class DataManager {

    public static void createItems(final Context context, final OnDataLoadedListener listener) {

        new ManualContentService().getManualContent("https://raw.githubusercontent.com/FelipePinhoUFC/ManualDoBixo/master/textos/", new OnReceiveListener<ManualContent>() {
            @Override
            public void onReceived(ManualContent response) {
                Log.i("DEBUG", "Response successfully");

                List<UpdateControl> updateControls = SugarRecord.listAll(UpdateControl.class);

                if(updateControls.size() == 0) {
                    new UpdateControl(response.getLastUpdate()).save();
                    createFrom(response.getText());

                    Log.i("DEBUG", "Loading from WEB");
                } else {
                    UpdateControl updateControl = updateControls.get(0);
                    if (response.getLastUpdate() > updateControl.getTimestamp()) {
                        createFrom(response.getText());
                        updateControl.setTimestamp(response.getLastUpdate());
                        updateControl.save();
                        Log.i("DEBUG", "Loading from WEB");
                    } else {
                        createLocale(context);
                        Log.i("DEBUG", "Loading from locale");
                    }
                }

                listener.onDataLoaded();
            }

            @Override
            public void onFail() {
                Log.i("DEBUG", "Response fail");
                createLocale(context);
                Log.i("DEBUG", "Loading from locale");

                listener.onDataLoaded();
            }
        });

    }

    private static boolean createLocale(Context context) {
        if(SugarRecord.listAll(Topic.class).size() == 0) {

            long currentTimestamp = System.currentTimeMillis() / 1000;
            UpdateControl updateControl = new UpdateControl(currentTimestamp);
            updateControl.save();

            try {

                AssetManager am = context.getAssets();
                InputStream is = am.open("text/topics.txt");

                StringBuilder buf = new StringBuilder();
                BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                String str;

                while ((str = in.readLine()) != null) {
                    buf.append(str + "\n");
                }

                in.close();

                String topicsText = buf.toString();

                createFrom(topicsText);

                return true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;

    }

    private static void createFrom(String topicsText) {
        SugarRecord.deleteAll(Item.class);
        SugarRecord.deleteAll(Topic.class);

        String[] topicsRaw = topicsText.split("#TOPIC");

        for (String topicRaw : topicsRaw) {

            String[] raw = topicRaw.split("#");

            if(raw.length >= 4) {
                Topic t = new Topic(clear(raw[1]), clear(raw[2]));
                t.save();

                String[] itemsRaw = topicRaw.split("#ITEM");

                boolean flag = false;
                for (String itemRaw : itemsRaw) {

                    String[] raw2 = itemRaw.split("#");
                    if (flag && raw2.length >= 4) {

                        Item item = new Item(clear(raw2[1]), clear(raw2[2]), clear(raw2[3]), t);
                        item.save();
                        Log.i("DEBUG", "New item: " + item.getText());

                    }

                    flag = true;

                }
            }

        }
    }

    private static String clear(String s) {
        s = s.replaceAll("\\s+\\n+", "\n");
        s = s.replaceAll("\\n+\\s+", "\n");
        s = s.replaceAll("\\n+", "\n");
        s = s.replaceAll("  ", " ");

        while(s.length() > 0 && (s.charAt(0) == '\n' || s.charAt(0) == ' '))
            s = s.substring(1);

        while(s.length() > 0 && (s.charAt(s.length() - 1) == '\n' || s.charAt(s.length() - 1) == ' '))
            s = s.substring(0, s.length() - 1);

        return s;
    }

    public interface OnDataLoadedListener {

        void onDataLoaded();

    }

}
