package it.imt.ui.perspective.wizards;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/*import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;*/
import org.eclipse.ui.wizards.newresource.BasicNewFileResourceWizard;

import it.imt.qflan.core.multivesta.*;
import it.imt.ui.perspective.dialogs.MessageDialogShower;


public class RunMultiVeStAAnalysis  extends BasicNewFileResourceWizard {//extends Wizard implements INewWizard {

	private WizardRunMultiVeStAAnalysisPage page;
	
	private static boolean librariesPresent=false;
	private static String possibleJarPaths="";
	
	public RunMultiVeStAAnalysis() {
		super();
		setWindowTitle("Run MultiVeStA analysis");
		if(!librariesPresent){
			try {
				checkLibraries();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void checkLibraries() throws IOException {
		String osName = System.getProperty("os.name");
		String libraryFileName="multivestaQFLan.jar";
		/*boolean win=false;
		boolean lin=false;
		boolean mac=false;*/
		if(osName.contains("Windows")||osName.contains("Linux")||osName.contains("Mac")){
			/*
			if(osName.contains("Windows")){
				win=true;
			}
			else if(osName.contains("Linux")){
				lin=true;
			}
			else{
				mac=true;
			}
			*/
			
			String property = System.getProperty("java.library.path");
			//System.out.println("\n"+property+"\n");
			//CRNReducerCommandLine.println(out, "\n"+property+"\n");
			StringTokenizer st = new StringTokenizer(property);
			ArrayList<String> paths = new ArrayList<>();
			while(st.hasMoreTokens()){
				String sep =File.pathSeparator;
				String path = st.nextToken(sep);
				paths.add(path);
			}
			StringBuilder possibleJarPathsSB = new StringBuilder();
			boolean exists=false;
			for (String path : paths) {
				if(possibleJarPathsSB.length()>0){
					possibleJarPathsSB.append(":");
				}
				String fileName = path+File.separator+libraryFileName;
				possibleJarPathsSB.append(fileName);
				File f = new File(fileName);
				if(f.exists()){
					exists=true;
					break;
				}
			}
			if(!exists){
				possibleJarPaths=possibleJarPathsSB.toString();
				String link="";
				String linkShort="";
				link= "https://dl.dropboxusercontent.com/u/18840437/qflan/multivestaQFLan.jar";
				linkShort="multivestaQFLan.jar "; 
				/*String OS = "";
				if(mac){
					OS="Mac";
				}
				else if(win){
					OS="Windows";
				}
				else{
					OS = "Linux";
				}*/
				//msgVisualizer.showMessage(msg,"and add its files to one of the following locations:",paths);
				new MessageDialogShower(getShell()).openMissingQFLanCoreLibraryDialog(link, linkShort /*, paths, OS*/);
			}
			else{
				librariesPresent=true;
			}
		}

	}

	@Override
    public void addPages() {
        page = new WizardRunMultiVeStAAnalysisPage(selection);
        addPage(page);
    }

	/*@Override
    public boolean performFinish() {
        IFile file = page.createNewFile();
        if (file != null){
        	 selectAndReveal(file);
        	return true;
        }
        else{
        	return false;
        }
    }*/
	 
	 /* (non-Javadoc)
     * Method declared on IWizard.
     */
    @Override
	public boolean performFinish() {
    	EntryPointMultiVeStAAndQFLanJava.main(new String[]{
		"-c", "-sm", "false", "-sd", "it.imt.qflan.core.multivesta.QFlanJavaState", 
		"-m", "BikesIDE.java", "-l", "1", "-ir", "20", 
		"-f", "/MultiQuaTEx/FeaturesInstalledAndPriceWeightLoadAtStep.multiquatex", 
		"-jn", possibleJarPaths,
		"-bs", "20", "-a", "0.1", "-ds", "[20.0,1.0,5.0,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1]", "-osws", "ONESTEP", "-sots", "12343"
    	});
    	/*EntryPointMultiVeStAAndQFLanGUI.main(new String[]{
    			"-c", "-sm", "false", "-sd", "it.imt.qflan.core.multivesta.QFlanJavaState", 
    			"-m", "BikesIDE.java", "-l", "oneLocal", "-ir", "20", 
    			"-f", "MultiQuaTEx/FeaturesInstalledAndPriceWeightLoadAtStep.multiquatex", 
    			"-bs", "20", "-a", "0.1", "-ds", "[20.0,1.0,5.0,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1]", "-osws", "ONESTEP", "-sots", "12343"
    	});*/
    	/*EntryPointMultiVeStAAndQFLanJava.main(new String[]{"62891"});
    	try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	/*EntryPointMultiVeStAAndQFLanJava.main(new String[]{
    			"-c", "-sm", "false", "-sd", "it.imt.qflan.core.multivesta.QFlanJavaState", 
    			"-m", "BikesIDE.java", "-l", "localhost:62891", "-ir", "20", 
    			"-f", "/MultiQuaTEx/FeaturesInstalledAndPriceWeightLoadAtStep.multiquatex", 
    			"-bs", "20", "-a", "0.1", "-ds", "[20.0,1.0,5.0,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1]", "-osws", "ONESTEP", "-sots", "12343"
    	});*/
       /* IFile file = page.createNewFile();
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
        }*/

        return true;
    }



}
