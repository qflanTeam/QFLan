package it.imt.ui.perspective.wizards;

import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

public class NewQFLanProjectWizard extends BasicNewProjectResourceWizard{
	
	public NewQFLanProjectWizard(){
		super();
		setWindowTitle("QFLan Project");
	}
	
	@Override
	public void addPages() {
		super.addPages();
		this.getPages()[0].setDescription("Create a new QFLan project");
		this.getPages()[0].setTitle("QFLan project");
	}
}
