package github.jianyuyouhun.plugin.codegenerator.builder;

import github.jianyuyouhun.plugin.codegenerator.model.EntryFieldModel;
import org.apache.http.util.TextUtils;

/**
 * Created by Darcy https://yedaxia.github.io/
 */
public class EntryGetterBuilder implements CodeBuilder {

    private String getterTemplate;
    private EntryFieldModel entryFieldModel;

    public EntryGetterBuilder(String getterTemplate, EntryFieldModel entryFieldModel) {
        this.getterTemplate = getterTemplate;
        this.entryFieldModel = entryFieldModel;
    }

    @Override
    public String builtString() {
        if (!TextUtils.isEmpty(entryFieldModel.getFieldName())) {
            getterTemplate = getterTemplate.replace("${FIELD_NAME}",entryFieldModel.getFieldName());
        }
        if (!TextUtils.isEmpty(entryFieldModel.getRemoteFieldName())) {
            getterTemplate = getterTemplate.replace("${REMOTE_FIELD_NAME}",entryFieldModel.getRemoteFieldName());
        }
        if (!TextUtils.isEmpty(entryFieldModel.getCaseFieldName())) {
            getterTemplate = getterTemplate.replace("${CASE_FIELD_NAME}",entryFieldModel.getCaseFieldName());
        }
        if (!TextUtils.isEmpty(entryFieldModel.getFieldType())) {
            getterTemplate = getterTemplate.replace("${FIELD_TYPE}",entryFieldModel.getFieldType());
        }
        return getterTemplate + "\n";
    }

    @Override
    public String getKey() {
        return null;
    }
}
