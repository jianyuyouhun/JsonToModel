package github.jianyuyouhun.plugin.codegenerator.model;

/**
 * Created by Darcy https://yedaxia.github.io/
 */
public class EntryFieldModel {

    public static final String MODEL_TYPE_OBJECT = "Object";
    public static final String MODEL_TYPE_BOOLEAN = "boolean";
    public static final String MODEL_TYPE_FLOAT = "float";
    public static final String MODEL_TYPE_INTEGER = "int";
    public static final String MODEL_TYPE_STRING = "String";
    public static final String MODEL_TYPE_JSON_OBJECT = "JsonObject";
    public static final String MODEL_TYPE_JSON_ARRAY = "JsonArray";

    private String remoteFieldName;
    private String caseFieldName;
    private String fieldName;
    private String fieldType;
    private String extraValueString;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getRemoteFieldName() {
        return remoteFieldName;
    }

    public void setRemoteFieldName(String remoteFieldName) {
        this.remoteFieldName = remoteFieldName;
    }

    public String getCaseFieldName() {
        return caseFieldName;
    }

    public void setCaseFieldName(String caseFieldName) {
        this.caseFieldName = caseFieldName;
    }

    public String getExtraValueString() {
        return extraValueString;
    }

    public void setExtraValueString(String extraValueString) {
        this.extraValueString = extraValueString;
    }

    public boolean isNeedExtra() {
        return fieldType.equals(MODEL_TYPE_JSON_OBJECT) || fieldType.equals(MODEL_TYPE_JSON_ARRAY);
    }

}
