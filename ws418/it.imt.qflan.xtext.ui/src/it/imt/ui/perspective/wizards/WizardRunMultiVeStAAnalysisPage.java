package it.imt.ui.perspective.wizards;

import java.io.InputStream;

//import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
//import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;


public class WizardRunMultiVeStAAnalysisPage extends WizardNewFileCreationPage {
	
	public WizardRunMultiVeStAAnalysisPage(IStructuredSelection selection) {
		super("Run MultiVeStA analysis", selection);
		setTitle("Run MultiVeStA analysis");
		setDescription("Run MultiVeStA analysis");
        setFileExtension("ode");
	}
	
	@Override
	protected void createAdvancedControls(Composite parent) {
		Label label1 = new Label(parent, SWT.NONE);
		label1.setText("Would you like an initial template file?");
	}	
	
		
	@Override
	protected InputStream getInitialContents() {
		/*String modelName = getFileName();
		//if(modelName.endsWith(".crn")||modelName.endsWith(".ode")){
		if(modelName.endsWith(".ode")){
				modelName=modelName.substring(0, modelName.length()-4);
		}
		modelName= GUICRNImporter.getOnlyAlphaNumeric(modelName);//.replace('-', '_').replace("(", "").replace(")", "");
		if(!getType().equals(WizardNewERODEFileCreationPage.NOTEMPLATETYPE)){
			String preDefModel = getPredefModel(getType(), modelName);
			return new ByteArrayInputStream(preDefModel.getBytes());
		}
		else{
			return null;
		}*/
		return null;
	}
}
