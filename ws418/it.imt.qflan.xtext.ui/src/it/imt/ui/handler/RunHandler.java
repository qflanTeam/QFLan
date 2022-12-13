package it.imt.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.ui.editor.XtextEditor;

public class RunHandler extends AbstractHandler implements IHandler {
	
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	IEditorPart activeEditor = HandlerUtil.getActiveEditor(event);
    	if (activeEditor instanceof XtextEditor) {
    		activeEditor.doSave(new NullProgressMonitor());
    		IFileEditorInput input = (IFileEditorInput) (activeEditor.getEditorInput());
    		IPath fullPathOfParent = input.getFile().getParent().getFullPath();
    		((XtextEditor)activeEditor).getDocument().readOnly(new ExecutorUnitOfWork(true,input.getFile().getProject(),fullPathOfParent));
    	}
    	return null;
    }
    
}


