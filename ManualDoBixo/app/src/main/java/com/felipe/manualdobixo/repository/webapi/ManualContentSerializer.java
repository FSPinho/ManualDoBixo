package com.felipe.manualdobixo.repository.webapi;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by felipe on 03/03/16.
 */
public class ManualContentSerializer implements JsonDeserializer<ManualContent> {
    @Override
    public ManualContent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ManualContent content = new Gson().fromJson(json, ManualContent.class);

        return content;
    }

}
