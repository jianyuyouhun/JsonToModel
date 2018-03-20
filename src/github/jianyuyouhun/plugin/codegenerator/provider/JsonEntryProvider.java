package github.jianyuyouhun.plugin.codegenerator.provider;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import github.jianyuyouhun.plugin.codegenerator.model.EntryFieldModel;
import github.jianyuyouhun.plugin.utils.EntryFieldHelper;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Darcy https://yedaxia.github.io/
 */
public class JsonEntryProvider implements IEntryProvider{

    @Override
    public List<EntryFieldModel> provideEntryFields(String objectJson) {
        JsonParser jsonParser = new  JsonParser();
        JsonObject jsonObject = jsonParser.parse(objectJson).getAsJsonObject();
        if(jsonObject == null){
            return null;
        }
        List<EntryFieldModel> fieldsList = new ArrayList<>();
        for(Map.Entry<String,JsonElement> fieldEntry : jsonObject.entrySet()){
            EntryFieldModel model = buildFieldModel(fieldEntry);
            fieldsList.add(model);
        }
        return fieldsList;
    }

    private EntryFieldModel buildFieldModel(Map.Entry<String,JsonElement> fieldEntry) {
        EntryFieldModel model = new EntryFieldModel();
        String remoteFieldName = fieldEntry.getKey();
        model.setRemoteFieldName(remoteFieldName);
        String fieldName = EntryFieldHelper.getPrefFieldName(remoteFieldName);
        model.setFieldName(fieldName);
        model.setFieldType(getFieldType(fieldEntry.getValue()));
        model.setCaseFieldName(StringUtils.capitalize(fieldName));
        if (model.isNeedExtra()) {
            model.setExtraValueString(fieldEntry.getValue().toString());
        }
        return model;
    }


    private String getFieldType(JsonElement fieldElement){
        if (fieldElement.isJsonArray()) {
            return EntryFieldModel.MODEL_TYPE_JSON_ARRAY;
        } else if (fieldElement.isJsonObject()) {
            return EntryFieldModel.MODEL_TYPE_JSON_OBJECT;
        }
        return EntryFieldHelper.getPreValueType(fieldElement.toString());
    }
}
