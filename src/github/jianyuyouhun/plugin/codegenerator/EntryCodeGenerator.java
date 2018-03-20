package github.jianyuyouhun.plugin.codegenerator;

import com.intellij.openapi.project.Project;
import github.jianyuyouhun.plugin.Config;
import github.jianyuyouhun.plugin.codegenerator.builder.EntryClassBuilder;
import github.jianyuyouhun.plugin.codegenerator.builder.EntryFieldBuilder;
import github.jianyuyouhun.plugin.codegenerator.builder.EntryGetterBuilder;
import github.jianyuyouhun.plugin.codegenerator.builder.EntrySetterBuilder;
import github.jianyuyouhun.plugin.codegenerator.model.EntryFieldModel;
import github.jianyuyouhun.plugin.codegenerator.provider.IEntryProvider;
import github.jianyuyouhun.plugin.codegenerator.provider.ProviderFactory;
import github.jianyuyouhun.plugin.utils.EntryFieldHelper;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Darcy https://yedaxia.github.io/
 */
public class EntryCodeGenerator {

    private static final String PROJECT_DIR = "github/jianyuyouhun/";
    private static final String FILE_FIELD_TEMPLATE = "Entry_Field_template";
    private static final String FILE_GETTER_TEMPLATE = "Entry_Getter_template";
    private static final String FILE_SETTER_TEMPLATE = "Entry_Setter_template";
    private static final String FILE_CLASS_TEMPLATE = "Entry_template";
    private static final String FILE_HAS_FIELD_TEMPLATE = "Entry_Has_Field_template";
    private static final String FILE_WITHOUT_FIELD_GETTER_TEMPLATE = "Entry_Without_Field_Getter_template";
    private static final String FILE_WITHOUT_FIELD_SETTER_TEMPLATE = "Entry_Without_Field_Setter_template";
    private static final String FILE_WITHOUT_SERIALIZAL_CLASS_TEMPLATE = "Entry_template_without_serial";

    public static String generateCode(Project project, String className, String objectText){
        String[] objectArray = objectText.split("\n");
        String annotationString = "";
        for (int i = 0; i < objectArray.length; i++) {
            String anno = EntryCodeGenerator.parseAnnotation(objectArray[i]);
            objectArray[i] = objectArray[i].replace(anno, "");
            annotationString = annotationString + anno + "\n";
        }
        objectText = "";
        for (int i = 0; i < objectArray.length; i++) {
            if (i == objectArray.length - 1) {
                objectText = objectText + objectArray[i];
            } else {
                objectText = objectText + objectArray[i] + "\n";
            }
        }

        objectText = checkAndReturnJsonString(objectText);

        IEntryProvider entryProvider = ProviderFactory.createProvider();
        List<EntryFieldModel> entryFields = entryProvider.provideEntryFields(objectText);

        StringBuilder fieldStrings = new StringBuilder();
        StringBuilder methodStrings = new StringBuilder();

        ResourceTemplateProvider resourceTemplateProvider = new ResourceTemplateProvider();
        String fieldTemplate;
        String getterTemplate;
        String setterTemplate;
        String classTemplate;
        switch (Config.getTemplateMode()) {
            case Config.TEMPLATE_FIELD:
                fieldTemplate = resourceTemplateProvider.provideTemplateForName(PROJECT_DIR+FILE_HAS_FIELD_TEMPLATE);
                getterTemplate = resourceTemplateProvider.provideTemplateForName(PROJECT_DIR+FILE_WITHOUT_FIELD_GETTER_TEMPLATE);
                setterTemplate = resourceTemplateProvider.provideTemplateForName(PROJECT_DIR+FILE_WITHOUT_FIELD_SETTER_TEMPLATE);
                classTemplate = resourceTemplateProvider.provideTemplateForName(PROJECT_DIR+FILE_WITHOUT_SERIALIZAL_CLASS_TEMPLATE);
                break;
            case Config.TEMPLATE_METHOD:
                fieldTemplate = resourceTemplateProvider.provideTemplateForName(PROJECT_DIR+FILE_FIELD_TEMPLATE);
                getterTemplate = resourceTemplateProvider.provideTemplateForName(PROJECT_DIR+FILE_GETTER_TEMPLATE);
                setterTemplate = resourceTemplateProvider.provideTemplateForName(PROJECT_DIR+FILE_SETTER_TEMPLATE);
                classTemplate = resourceTemplateProvider.provideTemplateForName(PROJECT_DIR+FILE_CLASS_TEMPLATE);
                break;
            default:
                fieldTemplate = "";
                getterTemplate = "";
                setterTemplate = "";
                classTemplate = "";
                break;
        }

        List<String> innerClassList = new ArrayList<>();
        for (EntryFieldModel entryFieldModel : entryFields){
            if (entryFieldModel.isNeedExtra()) {
                String extraString = entryFieldModel.getExtraValueString();
                if (entryFieldModel.getFieldType().equals(EntryFieldModel.MODEL_TYPE_JSON_ARRAY)) {
                    try {
                        JSONArray array = new JSONArray(extraString);
                        if (array.length() == 0) {
                            extraString = "";
                        } else {
                            extraString = array.get(0).toString();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        extraString = "";
                    }
                    if (!TextUtils.isEmpty(extraString)) {
                        try {
                            new JSONObject(extraString);
                            entryFieldModel.setFieldType("List<" + entryFieldModel.getCaseFieldName() + ">");
                            innerClassList.add(generateCode(project, entryFieldModel.getCaseFieldName(), extraString));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            entryFieldModel.setFieldType(EntryFieldHelper.getPreValueType(extraString) + "[]");
                        }
                    }
                } else {
                    if (!TextUtils.isEmpty(extraString)) {
                        try {
                            new JSONObject(extraString);
                            entryFieldModel.setFieldType(entryFieldModel.getCaseFieldName());
                            innerClassList.add(generateCode(project, entryFieldModel.getCaseFieldName(), extraString));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            entryFieldModel.setFieldType(EntryFieldHelper.getPreValueType(extraString));
                        }
                    }
                }
            }
        }

        for (EntryFieldModel entryFieldModel : entryFields){
            EntryFieldBuilder fieldBuilder = new EntryFieldBuilder(fieldTemplate,entryFieldModel);
            fieldStrings.append(fieldBuilder.builtString());
            EntryGetterBuilder getterBuilder = new EntryGetterBuilder(getterTemplate,entryFieldModel);
            methodStrings.append(getterBuilder.builtString());
            EntrySetterBuilder setterBuilder = new EntrySetterBuilder(setterTemplate,entryFieldModel);
            methodStrings.append(setterBuilder.builtString());
        }

        if(methodStrings.charAt(methodStrings.length() -1 ) == '\n'){
            methodStrings.deleteCharAt(methodStrings.length() -1);
        }

        EntryClassBuilder classBuilder = new EntryClassBuilder(classTemplate, className,fieldStrings.toString(),methodStrings.toString());
        String result = classBuilder.builtString();
        for (String innerClassString : innerClassList) {
            result = appendInnerClassString(result, innerClassString);
        }
        return result;
    }

    private static String parseAnnotation(String s) {
        String[] array = s.split("//");
        StringBuilder result = new StringBuilder();
        result.append("//");
        if (array.length >= 2) {
            for (int i = 1; i < array.length; i++) {
                result.append(array[i]);
            }
        }
        if (result.toString().trim().equals("//")) {
            return "";
        } else {
            return result.toString();
        }
    }

    private static String appendInnerClassString(String superString, String innerClassString) {
        String result = superString.substring(0, superString.length() - 2);//包括一个换行符号和一个"}"
        result = result + innerClassString;
        result = result + "\n}";
        return result;
    }

    /**
     * 校验字符串是否符合json规则（去掉api注释后的string）
     * @param objectText  objectText
     * @return rightObjectText
     */
    private static String checkAndReturnJsonString(String objectText) {
        String rightObjectText;

        return objectText;
    }

}
