package it.imt.ui.handler;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.Issue;

//import it.imt.qFLan.Analysis;
import it.imt.qFLan.ModelDefinition;
import it.imt.qflan.core.dialogs.IMessageDialogShower;
import it.imt.ui.perspective.dialogs.MessageDialogShower;
//import it.imt.erode.crn.chemicalReactionNetwork.ModelDefinition;

public class ExecutorUnitOfWork implements IUnitOfWork<Boolean, XtextResource> {
	private final boolean canSynchEditor;
	private final IProject project;
	private IPath fullPathOfParent;

	public ExecutorUnitOfWork(boolean canSynchEditor, IProject project, IPath fullPathOfParent) {
		super();
		this.fullPathOfParent=fullPathOfParent;
		this.canSynchEditor=canSynchEditor;
		this.project=project;
	}

	@Override
	public Boolean exec(XtextResource state)
			throws Exception {
		try {
            IWorkbench workbench = PlatformUI.getWorkbench();
            workbench.showPerspective("QFLANPerspective.perspective1",workbench.getActiveWorkbenchWindow());
        } catch (WorkbenchException e) {
            e.printStackTrace();
        }
		
		List<Issue> issues = state.getResourceServiceProvider().getResourceValidator().validate(state, CheckMode.ALL, CancelIndicator.NullImpl);
		if(issues!=null && issues.size()>0){
			IMessageDialogShower msgVisualizer = new MessageDialogShower(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
			String msg = "Please, fix the "+issues.size()+" errors.";
			if(issues.size()==1){
				msg = "Please, fix the error.";
			}
			msgVisualizer.openSimpleDialog(msg/*, DialogType.Error*/);
			return Boolean.FALSE;
		}
		else if(state.getErrors().size()>0){
			IMessageDialogShower msgVisualizer = new MessageDialogShower(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
			String msg = "Please, fix the "+state.getErrors().size()+" errors.";
			if(state.getErrors().size()==1){
				msg = "Please, fix the error.";
			}
			msgVisualizer.openSimpleDialog(msg/*, DialogType.Error*/);
			return Boolean.FALSE;
		}
		else{
			//Discard run invocations if already running
			ModelDefinition modelDef = null;
			TreeIterator<EObject> contents = state.getAllContents();
			while(contents.hasNext()&&modelDef==null){
				EObject content = contents.next();
				if(content instanceof ModelDefinition){
					modelDef=(ModelDefinition)content;
				}
			}
			if(modelDef!=null){
				MyMultiVeStAAnalysisExecutor myExecutor = new MyMultiVeStAAnalysisExecutor();
				//myExecutor.readAndExecute(modelDef,canSynchEditor);
				myExecutor.readAndExecuteMultiThreaded(modelDef,canSynchEditor,project,fullPathOfParent);
			}
		}
		
		
		
		return Boolean.TRUE;
	}
}
