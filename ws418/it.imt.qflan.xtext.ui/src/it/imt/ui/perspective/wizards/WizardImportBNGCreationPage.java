package it.imt.ui.perspective.wizards;

import java.io.File;
//import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;

//import org.eclipse.core.resources.IProject;
//import org.eclipse.core.resources.IWorkspace;
//import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.FileSystemElement;
import org.eclipse.ui.internal.wizards.datatransfer.WizardFileSystemResourceImportPage1;

import it.imt.qflan.simulation.output.IDataOutputHandler;
import it.imt.ui.perspective.plot.GUIDataOutputHandler;
import vesta.mc.InfoMultiQuery;

@SuppressWarnings({ "restriction", "unchecked" })
public class WizardImportBNGCreationPage extends WizardFileSystemResourceImportPage1 {

	protected WizardImportBNGCreationPage(IWorkbench aWorkbench, IStructuredSelection selection) {
		super(aWorkbench, selection);
		this.selectedTypes=new ArrayList<String>();
		selectedTypes.add(getExtension());
	}
	
	protected String getExtension(){
		return ".java";
	}
	
	@Override
	public boolean finish() {
		if (!ensureSourceIsValid()) {
			return false;
		}

		saveWidgetValues();

		IPath resourcePath = getResourcePath();
		//IWorkspace workspace= ResourcesPlugin.getWorkspace();
		//IPath location= Path.fromOSString(myfile.getAbsolutePath());
		String proj = resourcePath.toString();
		//String remainingInternalPath = "";
		int posOfSep = proj.indexOf(File.separator, 1);
		//int posOfSep = proj.indexOf(File.separator);
		if(posOfSep!=-1){
			//remainingInternalPath=proj.substring(posOfSep);
			proj=proj.substring(0,posOfSep);
		}
		/*IProject project = workspace.getRoot().getProject(proj);
		URI projectURI = project.getLocationURI();
		String projectPath=projectURI.getPath();*/
	
		/*IFile file= workspace.getRoot().getFileForLocation(resourcePath);
  	    IProject project = file.getProject();*/

	
		Iterator<FileSystemElement> resourcesEnum = getSelectedResources().iterator();
		//List<File> fileSystemObjects = new ArrayList<File>();
		
		/*while (resourcesEnum.hasNext()) {
			. . .
		}*/
		if(resourcesEnum.hasNext()){
			File f = (File)(resourcesEnum.next().getFileSystemObject());
			String fileAbsPath = f.getAbsolutePath();
			/*if(fileAbsPath.endsWith(".qflan")){
				String path = fileAbsPath.substring(0,fileAbsPath.lastIndexOf(File.separator));
				String name = fileAbsPath.substring(fileAbsPath.lastIndexOf(File.separator)+1,fileAbsPath.length());
				fileAbsPath=path+"src-gen"+File.separator+name.replace(".qflan", ".java");
			}*/
			String query = "/MultiQuaTEx/FeaturesInstalledAndPriceWeightLoadAtStep.multiquatex";
			String alpha = "0.1";
			String delta = "[20.0,1.0,5.0,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1]";
			InfoMultiQuery result = vesta.NewVesta.invokeClient(new String[]{
					"-sm", "false", "-sd", "it.imt.qflan.core.multivesta.QFlanJavaState", 
					"-m", fileAbsPath, "-l", "1", "-ir", "20", 
					"-f", query,
					"-vp", "false",
					"-jn", ImportBNGWizard.getJarPath(), //ImportBNGWizard.getPossibleJarPaths(),
					"-bs", "20", "-a", String.valueOf(alpha), "-ds", delta, "-osws", "ONESTEP", "-sots", "12343"
			});
			
			/*EntryPointMultiVeStAAndQFLanJava.main(new String[]{
					"-c", "-sm", "false", "-sd", "it.imt.qflan.core.multivesta.QFlanJavaState", 
					"-m", fileAbsPath, "-l", "1", "-ir", "20", 
					"-f", "/MultiQuaTEx/FeaturesInstalledAndPriceWeightLoadAtStep.multiquatex", 
					"-jn", ImportBNGWizard.getPossibleJarPaths(),
					"-bs", "20", "-a", "0.1", "-ds", "[20.0,1.0,5.0,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1]", "-osws", "ONESTEP", "-sots", "12343"
			});*/
			
			String command = "MultiVeStA analysis of " + fileAbsPath;
			String minimalDescription = "SMC of "+query;//
			boolean showLabels=true;
			boolean visualizePlot=true;
			
			//dog = new DataOutputHandler(minimalDescription,crn, result,alpha,delta);
			IDataOutputHandler dog = new GUIDataOutputHandler(null);
			dog.setData(/*fileAbsPath+" - "+*/minimalDescription, result,alpha,delta,command);
			
			dog.setShowLabels(showLabels);

			if(visualizePlot){
				if(result.getNumberOfX()<=3){
					System.out.println("The used graphical library does not allow to draw lines basing on less than four points.");
				}
				else{
					dog.showPlots(false,null);
				}
			}

			/*String csvFile = fileAbsPath.replace(".java", "");
			csvFile = fileAbsPath.replace(".qflan", "");
			csvFile=csvFile+".csv";
			boolean writeCSV = csvFile!=null;
			if(writeCSV){
				dog.writeCSV(csvFile,null);
			}*/

			//return super.finish();
			return true;
		}
		return false;
	}

	@Override
	protected void createOptionsGroupButtons(Group optionsGroup) {

	}
	@Override
	protected void createOptionsGroup(Composite parent) {
		
	}

	protected String getStoreSourceNamesID(){
		return "WizardFileSystemResourceImportPage1.STORE_SOURCE_NAMES_ID";
	}
	
	//private final static String STORE_SOURCE_NAMES_ID = "WizardFileSystemResourceImportPage1.STORE_SOURCE_NAMES_ID";
	@Override
	protected void restoreWidgetValues() {
		IDialogSettings settings = getDialogSettings();
		if (settings != null) {
			String[] sourceNames = settings.getArray(getStoreSourceNamesID());
			if (sourceNames == null) {
				return; // ie.- no values stored, so stop
			}

			// set filenames history
			for (int i = 0; i < sourceNames.length; i++) {
				sourceNameField.add(sourceNames[i]);
			}
			updateWidgetEnablements();
		}
	}

	/**
	 *	Answer the directory name specified as being the import source.
	 *	Note that if it ends with a separator then the separator is first
	 *	removed so that java treats it as a proper directory
	 */
	private String getSourceDirectoryName() {
		return getSourceDirectoryName(this.sourceNameField.getText());
	}
	/**
	 *	Answer the directory name specified as being the import source.
	 *	Note that if it ends with a separator then the separator is first
	 *	removed so that java treats it as a proper directory
	 */
	private String getSourceDirectoryName(String sourceName) {
		IPath result = new Path(sourceName.trim());

		if (result.getDevice() != null && result.segmentCount() == 0) {
			result = result.addTrailingSeparator();
		} else {
			result = result.removeTrailingSeparator();
		}

		return result.toOSString();
	}

	@Override
	protected void saveWidgetValues() {
		IDialogSettings settings = getDialogSettings();
		if (settings != null) {
			// update source names history
			String[] sourceNames = settings.getArray(getStoreSourceNamesID());
			if (sourceNames == null) {
				sourceNames = new String[0];
			}

			sourceNames = addToHistory(sourceNames, getSourceDirectoryName());
			settings.put(getStoreSourceNamesID(), sourceNames);
		}
	}
	
	
}
