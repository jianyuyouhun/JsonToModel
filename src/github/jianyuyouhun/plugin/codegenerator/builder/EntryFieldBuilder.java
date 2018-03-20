package github.jianyuyouhun.plugin.codegenerator.builder;

import github.jianyuyouhun.plugin.codegenerator.model.EntryFieldModel;
import org.apache.http.util.TextUtils;

/**
 * Created by Darcy https://yedaxia.github.io/
 */
public class EntryFieldBuilder implements CodeBuilder {

    private String fieldTemplate;
    private EntryFieldModel entryFieldModel;

    public EntryFieldBuilder(String fieldTemplate, EntryFieldModel entryFieldModel) {
        this.fieldTemplate = fieldTemplate;
        this.entryFieldModel = entryFieldModel;
    }

    @Override
    public String builtString() {
        if (!TextUtils.isEmpty(entryFieldModel.getRemoteFieldName())) {
            fieldTemplate = fieldTemplate.replace("${REMOTE_FIELD_NAME}",entryFieldModel.getRemoteFieldName());
        }
        if (!TextUtils.isEmpty(entryFieldModel.getCaseFieldName())) {
            fieldTemplate = fieldTemplate.replace("${CASE_FIELD_NAME}",entryFieldModel.getCaseFieldName());
        }
        if (!TextUtils.isEmpty(entryFieldModel.getFieldType())) {
            fieldTemplate = fieldTemplate.replace("${FIELD_TYPE}",entryFieldModel.getFieldType());
        }
        if (!TextUtils.isEmpty(entryFieldModel.getFieldName())) {
            fieldTemplate = fieldTemplate.replace("${FIELD_NAME}",entryFieldModel.getFieldName());
        }
        return fieldTemplate + "\n";
    }

    @Override
    public String getKey() {
        return null;
    }
}
