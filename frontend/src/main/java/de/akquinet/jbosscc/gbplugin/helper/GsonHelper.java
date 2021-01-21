package de.akquinet.jbosscc.gbplugin.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import de.akquinet.jbosscc.gbplugin.data.gbactions.*;
import de.akquinet.jbosscc.gbplugin.data.GBActionsJSON;

import java.io.*;
import java.util.List;

/**
 * @author siraj
 */
public class GsonHelper {
    public static String exportJSON(List<GBAction> gbActions, String fileName) throws IOException {
        gbActions.forEach(gbAction -> System.out.println(gbAction.getName() + gbAction.getGBActionType()));
        Gson gson = GsonHelper.initGson();
        GBActionsJSON gbActionsJSON = new GBActionsJSON(gbActions);
        String actionsString = gson.toJson(gbActionsJSON, GBActionsJSON.class);
        FileWriter file = new FileWriter(fileName, false); //replace file
        file.write(actionsString);
        file.flush();
        return new File(fileName).getAbsolutePath();
    }

    public static List<GBAction> importJSON(String fileName) throws FileNotFoundException {
        Gson gson = GsonHelper.initGson();
        JsonReader reader = new JsonReader(new FileReader(fileName));
        GBActionsJSON gbActionsJSON =  gson.fromJson(reader, GBActionsJSON.class);
        gbActionsJSON.getGbActions().forEach(gbAction -> System.out.println(gbAction.getName() + gbAction.getGBActionType()));
        return gbActionsJSON.getGbActions();

    }

    public static Gson initGson() {
        RuntimeTypeAdapterFactory<GBAction> actionFactory = RuntimeTypeAdapterFactory.of(GBAction.class, "type")
                .registerSubtype(RenameGBAction.class, GBActionType.RENAME.name())
                .registerSubtype(ChangeTypeGBAction.class, GBActionType.CHANGE_COLUMN_TYPE.name())
                .registerSubtype(ExcludeColumnGBAction.class, GBActionType.EXCLUDE_COLUMN.name())
                .registerSubtype(ExcludeTableGBAction.class, GBActionType.EXCLUDE_TABLE.name());
        return new GsonBuilder()
                .registerTypeAdapterFactory(actionFactory)
                .create();

    }
}
