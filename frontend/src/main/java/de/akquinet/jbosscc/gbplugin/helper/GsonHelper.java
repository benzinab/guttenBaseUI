package de.akquinet.jbosscc.gbplugin.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import de.akquinet.jbosscc.gbplugin.data.Action;

import java.io.*;
import java.util.List;

public class GsonHelper {
    public static String exportJSON(List<Action> actions, String fileName) throws IOException {
        Gson gson = new GsonBuilder().create();
        String actionsString = gson.toJson(actions, new TypeToken<List<Action>>(){}.getType());
        FileWriter file = new FileWriter(fileName, false);
        file.write(actionsString);
        file.flush();
        String path = new File(fileName).getAbsolutePath();
        return path;
    }

    public static List<Action> importJSON(String fileName) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(fileName));
        return gson.fromJson(reader, new TypeToken<List<Action>>(){}.getType());

    }
}
