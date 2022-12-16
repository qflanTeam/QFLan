package it.imt.ui.perspective.wizards;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;

import it.imt.ui.perspective.dialogs.MessageDialogShower;

//import org.eclipse.ui.wizards.datatransfer.FileSystemImportWizard;
/**
 * This class has been created starting from org.eclipse.ui.wizards.datatransfer.FileSystemImportWizard;
 * @author Andrea Vandin
 *
 */
@SuppressWarnings({ "restriction", "rawtypes" })
public class ImportBNGWizard  extends Wizard implements IImportWizard  {

	private IWorkbench workbench;

    private IStructuredSelection selection;

    private WizardImportBNGCreationPage mainPage;

	//private MessageConsoleStream out;
    
    private static boolean librariesPresent=false;
	protected static String jarPath="";
	private static final String libraryFileName="multivestaQFLan.jar";
	public static final String FILEWITHLIBRARYFILELOCATION="pathOfMultivestaQFLan";

    /**
     * Creates a wizard for importing resources into the workspace from
     * the file system.
     */
    public ImportBNGWizard() {
    	while(!librariesPresent){
			try {
				checkLibraries();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    	setWindowTitle("Run MultiVeStA analysis");
        IDialogSettings workbenchSettings = WorkbenchPlugin.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings
                .getSection("FileSystemImportWizard");//$NON-NLS-1$
        if (section == null) {
			section = workbenchSettings.addNewSection("FileSystemImportWizard");//$NON-NLS-1$
		}
        setDialogSettings(section);
    }

    /* (non-Javadoc)
     * Method declared on IWizard.
     */
    @Override
	public void addPages() {
        super.addPages();
        mainPage = new WizardImportBNGCreationPage(workbench, selection);
        addPage(mainPage);
    }


    /* (non-Javadoc)
     * Method declared on IWorkbenchWizard.
     */
    @Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        this.workbench = workbench;
        this.selection = currentSelection;

        List selectedResources = IDE.computeSelectedResources(currentSelection);
        if (!selectedResources.isEmpty()) {
            this.selection = new StructuredSelection(selectedResources);
        }

        setWindowTitle(DataTransferMessages.DataTransfer_importTitle);
        setDefaultPageImageDescriptor(IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/importdir_wiz.png"));//$NON-NLS-1$
        setNeedsProgressMonitor(true);
    }

    /* (non-Javadoc)
     * Method declared on IWizard.
     */
    @Override
	public boolean performFinish() {
    	return mainPage.finish();
    }

    private void checkLibraries() throws IOException {
    	/*
    	String osName = System.getProperty("os.name");
    	boolean win=false;
    	boolean lin=false;
    	boolean mac=false;*/
    	/*if(osName.contains("Windows")||osName.contains("Linux")||osName.contains("Mac")){
    		if(osName.contains("Windows")){
    			win=true;
    		}
    		else if(osName.contains("Linux")){
    			lin=true;
    		}
    		else{
    			mac=true;
    		}
    	}*/
    	boolean weHaveJar = false;
    	File f = new File(FILEWITHLIBRARYFILELOCATION);
    	if(f.exists()){
    		BufferedReader br = new BufferedReader(new FileReader(f));
    		String path = br.readLine();
    		br.close();
    		File jar = new File(path);
    		if(jar.exists()){
    			weHaveJar=true;
    			jarPath=path;
    			librariesPresent=true;
    		}
    	}
    	/*String property = System.getProperty("java.library.path");
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
			}*/
    	if(!weHaveJar){
    		//possibleJarPaths=possibleJarPathsSB.toString();
    		String link="";
    		String linkShort="";
    		link= "https://dl.dropboxusercontent.com/u/18840437/qflan/multivestaQFLan.jar";
    		linkShort=libraryFileName; 
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

	public static String getJarPath() {
		return jarPath;
	}


			

    /*public static String getPossibleJarPaths() {
    	if(possibleJarPaths.equals("")){
    		String property = System.getProperty("java.library.path");
    		StringTokenizer st = new StringTokenizer(property);
    		StringBuilder possibleJarPathsSB = new StringBuilder();
    		while(st.hasMoreTokens()){
    			if(possibleJarPathsSB.length()>0){
    				possibleJarPathsSB.append(":");
    			}
    			String sep =File.pathSeparator;
    			String path = st.nextToken(sep);
    			String fileName = path+File.separator+libraryFileName;
    			possibleJarPathsSB.append(fileName);
    		}
    		possibleJarPaths = possibleJarPathsSB.toString(); 
    	}
    	return possibleJarPaths;
    }*/
}
