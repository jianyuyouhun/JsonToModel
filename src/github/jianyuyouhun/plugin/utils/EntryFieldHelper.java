package github.jianyuyouhun.plugin.utils;

import github.jianyuyouhun.plugin.codegenerator.model.EntryFieldModel;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;

/**
 * Created by user on 2016/12/25.
 */
public class EntryFieldHelper {

    private static final String integerReg = "^-?[1-9]\\d*$";
    private static final String floatReg = "^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$";

    public static String getPrefFieldName(String originFieldName){
        String[] names = originFieldName.split("_");
        if(names.length == 1){
            return StringUtils.uncapitalize(names[0]);
        }
        StringBuilder fieldNameBuilder = new StringBuilder();
        fieldNameBuilder.append(StringUtils.uncapitalize(names[0]));
        for (int i = 1; i < names.length; i++) {
            fieldNameBuilder.append(StringUtils.capitalize(names[i]));
        }
        return fieldNameBuilder.toString();
    }

    public static String getPrefFieldType(String fieldType) {
        if(fieldType.equalsIgnoreCase("int") || fieldType.equalsIgnoreCase("integer")){
            return "int";
        }else if(fieldType.equalsIgnoreCase("short")){
            return "short";
        }else if(fieldType.equalsIgnoreCase("byte")){
            return "byte";
        }else if(fieldType.equalsIgnoreCase("long")){
            return "long";
        }else if(fieldType.equalsIgnoreCase("boolean") || fieldType.equalsIgnoreCase("bool")){
            return "boolean";
        }else if(fieldType.equalsIgnoreCase("float")){
            return "float";
        }else if(fieldType.equalsIgnoreCase("double")){
            return "double";
        }else{
            return "String";
        }
    }

    public static String getPreValueType(String fieldValue) {
        if (TextUtils.isEmpty(fieldValue) || fieldValue.equals("null")) {
            return EntryFieldModel.MODEL_TYPE_OBJECT;
        } else if (fieldValue.equals("true") || fieldValue.equals("false")) {
            return EntryFieldModel.MODEL_TYPE_BOOLEAN;
        } else if (fieldValue.matches(floatReg)) {
            return EntryFieldModel.MODEL_TYPE_FLOAT;
        } else if (fieldValue.matches(integerReg)) {
            return EntryFieldModel.MODEL_TYPE_INTEGER;
        } else {
            return EntryFieldModel.MODEL_TYPE_STRING;
        }
    }

}
