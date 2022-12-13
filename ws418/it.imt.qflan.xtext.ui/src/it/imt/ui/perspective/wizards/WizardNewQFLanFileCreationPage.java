package it.imt.ui.perspective.wizards;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

@SuppressWarnings("restriction")
public class WizardNewQFLanFileCreationPage extends WizardNewFileCreationPage {

	private Combo combo;
	public static final String YESSimple = "Simple model (explicit processes)";
	public static final String YESBikes = "Bikes model (explicit processes)";
	public static final String YESSimpleDiagram = "Simple model";
	public static final String YESBikesDiagram = "Bikes model";
	public static final String YESElevator = "Elevator model";
	public static final String NO = "No";
	public static final List<String> QUERIESSIMPLEMODEL=Arrays.asList("SimpleModel_Price_FeaturesInstalled_x1_AtStep"); 
	public static final List<String> QUERIESBIKES=Arrays.asList("Bikes_FeaturesInstalledAndPriceWeightLoadAtFirstDeploy",
			"Bikes_FeaturesInstalledAndPriceWeightLoadAtStep","Bikes_FeaturesUninstalledAtStep");
	public static final List<String> QUERIESELEVATOR=Arrays.asList("Elevator_FloorAtStep");
	
	public WizardNewQFLanFileCreationPage(IStructuredSelection selection) {
		super("New QFLan file", selection);
		setTitle("QFLan file");
		setDescription("Create a new QFLan file");
        setFileExtension("qflan");
	}
	
	@Override
	protected void createAdvancedControls(Composite parent) {
		Label label1 = new Label(parent, SWT.NONE);
		label1.setText("Would you like an initial template file?");
		
		combo = new Combo(parent, SWT.READ_ONLY);
		//combo.add("System of Ordinary Differential Equations");
		//combo.add("Reaction Network");
		combo.add(NO);
		combo.add(YESSimpleDiagram);
		combo.add(YESSimple);
		combo.add(YESBikesDiagram);
		combo.add(YESBikes);
		combo.add(YESElevator);
		combo.select(0);
		combo.setSize(2, combo.getSize().y);
		super.createAdvancedControls(new Composite(parent, 1));
	}
	
	public String getType(){
		return combo.getText();
	}
		
	@Override
	protected InputStream getInitialContents() {
		String modelName = getFileName();
		//if(modelName.endsWith(".crn")||modelName.endsWith(".ode")){
		String ext=".qflan";
		if(modelName.endsWith(ext)){
				modelName=modelName.substring(0, modelName.length()-ext.length());
		}
		modelName= getOnlyAlphaNumeric(modelName);//.replace('-', '_').replace("(", "").replace(")", "");
		if(!getType().equals(WizardNewQFLanFileCreationPage.NO)){
			String preDefModel=null;
			try {
				preDefModel = getPredefModel(getType(), modelName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(preDefModel==null){
				preDefModel="";
			}
			return new ByteArrayInputStream(preDefModel.getBytes());
		}
		else{
			return null;
		}
	}

	public static String getOnlyAlphaNumeric(String s) {
	    Pattern pattern = Pattern.compile("[^0-9 a-z A-Z _]");
	    Matcher matcher = pattern.matcher(s.replace('-', '_'));
	    String number = matcher.replaceAll("");
	    return number;
	 }
	
	private String getPredefModel(String type, String modelName) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		System.out.println(getContainerFullPath());
		
		String file = null;

		if(type.equals(YESBikes)){
			file="Bikes.txt";
		}
		else if(type.equals(YESBikesDiagram)){
			file="BikesDiagram.txt";
		}
		else if(type.equals(YESElevator)){
			file="Elevator.txt";
		}
		else if(type.equals(YESSimple)){
			file="SimpleModel.txt";
		}
		else if(type.equals(YESSimpleDiagram)){
			file="SimpleDiagram.txt";
		}
		
		if(file!=null){
			InputStream is = getClass().getResourceAsStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line=br.readLine();
			sb.append("begin model "+modelName+"\n");
			while(line!=null){
				sb.append(line);
				sb.append("\n");
				line=br.readLine();
			}
			br.close();
		}


		//writePredefinedQueries(type);
		
		
		/*BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(fileName));*/
		
		/*IPath newFilePath = containerPath.append(resourceGroup.getResource());
		final IFile newFileHandle = createFileHandle(newFilePath);
		final InputStream initialContents = getInitialContents();*/
		
		
		return sb.toString();
	}

	public void writePredefinedQueries(String type) throws IOException {
		List<String> queries=QUERIESSIMPLEMODEL;

		if(type.equals(YESBikes)){
			queries=QUERIESBIKES;
		}
		else if(type.equals(YESElevator)){
			queries=QUERIESELEVATOR;
		}
		
		IPath containerPath = getContainerFullPath();
		IPath mq = containerPath.append(new Path("MultiQuaTEx"));
		String mqPath = createFileHandle(mq).getLocationURI().getPath();
		File mqFolder = new File(mqPath);
		if(!mqFolder.exists()){
			mqFolder.mkdir();
		}
		for(String query : queries){
			IPath p=mq.append(query+".multiquatex");
			String path = createFileHandle(p).getLocationURI().getPath();
			File q = new File(path);
			if(!q.exists()){
				writeQuery(q,query+".txt");
			}
		}

		try {
			IDEWorkbenchPlugin.getPluginWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void writeQuery(File targetquery, String localQuery) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(targetquery));
		InputStream is = getClass().getResourceAsStream(localQuery);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line=br.readLine();
		while(line!=null){
			bw.write(line);
			bw.write("\n");
			line=br.readLine();
		}
		br.close();
		bw.flush();
		bw.close();
	}
}
