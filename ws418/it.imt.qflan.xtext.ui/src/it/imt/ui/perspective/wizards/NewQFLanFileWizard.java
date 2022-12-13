package it.imt.ui.perspective.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewFileResourceWizard;

public class NewQFLanFileWizard  extends BasicNewFileResourceWizard {//extends Wizard implements INewWizard {

	private WizardNewQFLanFileCreationPage page;
	
	public NewQFLanFileWizard() {
		super();
		setWindowTitle("QFLan File");
	}

	@Override
    public void addPages() {
        page = new WizardNewQFLanFileCreationPage(selection);
        addPage(page);
    }
	 
	 /* (non-Javadoc)
     * Method declared on IWizard.
     */
    @Override
	public boolean performFinish() {
        IFile file = page.createNewFile();
        if (file == null) {
			return false;
		}

        selectAndReveal(file);

        // Open editor on new file.
        IWorkbenchWindow dw = getWorkbench().getActiveWorkbenchWindow();
        try {
            if (dw != null) {
                IWorkbenchPage page = dw.getActivePage();
                if (page != null) {
                    IDE.openEditor(page, file, true);
                }
            }
        } catch (PartInitException e) {
        	
            //DialogUtil.openError(dw.getShell(), ResourceMessages.FileResource_errorMessage,e.getMessage(), e);
        	e.printStackTrace();
        }

        return true;
    }



}
