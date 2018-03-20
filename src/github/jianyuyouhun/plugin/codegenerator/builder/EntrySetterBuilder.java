package github.jianyuyouhun.plugin.codegenerator.builder;


import github.jianyuyouhun.plugin.codegenerator.model.EntryFieldModel;
import org.apache.http.util.TextUtils;

/**
 * Created by Darcy https://yedaxia.github.io/
 */
public class EntrySetterBuilder implements CodeBuilder {

    private String setterTemplate;
    private EntryFieldModel entryFieldModel;

    public EntrySetterBuilder(String setterTemplate, EntryFieldModel entryFieldModel){
        this.setterTemplate = setterTemplate;
        this.entryFieldModel = entryFieldModel;
    }

    @Override
    public String builtString() {
        if (!TextUtils.isEmpty(entryFieldModel.getFieldType())) {
            setterTemplate = setterTemplate.replace("${FIELD_TYPE}",entryFieldModel.getFieldType());
        }
        if (!TextUtils.isEmpty(entryFieldModel.getFieldName())) {
            setterTemplate = setterTemplate.replace("${FIELD_NAME}",entryFieldModel.getFieldName());
        }
        if (!TextUtils.isEmpty(entryFieldModel.getRemoteFieldName())) {
            setterTemplate = setterTemplate.replace("${REMOTE_FIELD_NAME}",entryFieldModel.getRemoteFieldName());
        }
        if (!TextUtils.isEmpty(entryFieldModel.getCaseFieldName())) {
            setterTemplate = setterTemplate.replace("${CASE_FIELD_NAME}",entryFieldModel.getCaseFieldName());
        }
        return setterTemplate + "\n";
    }

    @Override
    public String getKey() {
        return null;
    }
}
