package github.jianyuyouhun.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import github.jianyuyouhun.plugin.codegenerator.EntryCodeGenerator;
import github.jianyuyouhun.plugin.ui.CodeDialogBuilder;
import github.jianyuyouhun.plugin.ui.StringResources;
import com.intellij.openapi.ui.Messages;
import github.jianyuyouhun.plugin.utils.ClipboardHelper;
import org.apache.commons.lang.StringUtils;

/**
 * Json转java实体类
 * Created by wangyu on 2017/4/17.
 */
public class JsonToModelAction extends AnAction {

    private Project project;
    private String entryCode;

    @Override
    public void actionPerformed(AnActionEvent e) {
        project = getEventProject(e);
        showEntryCodeGeneratorDialog();
    }

    private void showEntryCodeGeneratorDialog() {
        final CodeDialogBuilder codeDialogBuilder = new CodeDialogBuilder(project, StringResources.TITLE_GENERATE_ENTRY_CODE, "\n\n\n\n\n\n\n");
        codeDialogBuilder.addEntryClassName(StringResources.DEFAULT_ENTRY_NAME);
        codeDialogBuilder.addAction(StringResources.ACTION_CREAETE_ENTRY, new Runnable() {
            @Override
            public void run() {
                String entryClassName = codeDialogBuilder.getEntryClassName();
                String entryObjectText = codeDialogBuilder.getModifiedCode();
                if(StringUtils.isEmpty(entryClassName)){
                    Messages.showErrorDialog(project,"error","Entry Class Cannot Be Empty");
                    return ;
                }
                if(StringUtils.isEmpty(entryObjectText)){
                    Messages.showErrorDialog(project,"error","Class Json Text Cannot Be Empty");
                    return ;
                }

                String newCode = generateEntryCode(project,entryClassName,entryObjectText);
                if(entryCode == null || !entryCode.equals(newCode)){
                    entryCode = newCode;
                    codeDialogBuilder.setCodeText(newCode);
                }
            }
        });
        codeDialogBuilder.addAction(StringResources.COPY_ACTION_LABEL, new Runnable() {
            @Override
            public void run() {
                ClipboardHelper.copy(entryCode);
                codeDialogBuilder.closeDialog();
            }
        });
        codeDialogBuilder.showDialog();
    }

    private String generateEntryCode(Project project, String entryClassName, String objectText){
        return EntryCodeGenerator.generateCode(project, entryClassName,objectText);
    }
}
