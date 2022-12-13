package it.imt.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.utils.EditorUtils;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;

import com.google.common.util.concurrent.ExecutionError;
import com.google.inject.Inject;

public class RunHandlerFromPackageExplorer extends AbstractHandler implements IHandler {

    @Inject
    IResourceDescriptions resourceDescriptions;
     
    @Inject
    IResourceSetProvider resourceSetProvider;
     
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	System.out.println("execute RunHandlerFromPackageExplorer");
        ISelection selection = HandlerUtil.getCurrentSelection(event);
        if (selection instanceof IStructuredSelection) {
        	
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            Object firstElement = structuredSelection.getFirstElement();
            if (firstElement instanceof IFile) {
                IFile file = (IFile) firstElement;
                IProject project = file.getProject();
                                
                URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
                ResourceSet rs = resourceSetProvider.get(project);
                
                Resource r = rs.getResource(uri, true);
                                
                boolean canSynchEditor=false;
                
                XtextEditor xtextEditor = EditorUtils.getActiveXtextEditor();
                IEditorInput editorInput = xtextEditor.getEditorInput();
                if (editorInput instanceof IFileEditorInput)
                 {
                   IFile openFile = ((IFileEditorInput)editorInput).getFile();
                   if(openFile.equals(file)){
                	   canSynchEditor=true;
                   }
                 }
                
                ExecutorUnitOfWork execuw = new ExecutorUnitOfWork(canSynchEditor,file.getProject(),file.getParent().getFullPath());
                XtextResource xr = (XtextResource)r; 
				try {
					execuw.exec(xr);
				} catch (Exception e) {
					throw new ExecutionError(e.getMessage(), new Error(e.getCause()));
				}
            }
        }
        return null;
    }

}
