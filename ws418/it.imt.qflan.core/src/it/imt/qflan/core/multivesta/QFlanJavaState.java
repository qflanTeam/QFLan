package it.imt.qflan.core.multivesta;

import it.imt.qflan.core.features.AbstractFeature;
import it.imt.qflan.core.features.ConcreteFeature;
import it.imt.qflan.core.features.interfaces.IFeature;
import it.imt.qflan.core.model.DoubleAndCommitment;
import it.imt.qflan.core.model.IQFlanModelBuilder;
import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.models.BikesDiagram;
import it.imt.qflan.core.models.BikesWithoutFakeFeatures2;
import it.imt.qflan.core.models.Elevator;
/*import it.imt.qflan.core.models.BikesWithoutFakeFeatures;
import it.imt.qflan.core.models.ElevatorOLD;*/
import it.imt.qflan.core.predicates.interfaces.IPredicateDef;
import it.imt.qflan.core.processes.interfaces.ICommitment;
import it.imt.qflan.core.variables.QFLanVariable;
import it.imtlucca.util.RandomEngineFacilities;
import cern.jet.random.engine.MersenneTwister;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.tools.JavaCompiler;
//import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.microsoft.z3.Z3Exception;

import vesta.mc.NewState;
import vesta.mc.ParametersForState;
import vesta.quatex.DEFAULTOBSERVATIONS;

public class QFlanJavaState extends NewState {

	private QFlanModel loadedModel;
	private String nameOfTheJarForBM;
	private String logFile="";
	private boolean toLog=false;
	private RandomEngineFacilities randomGenerator;
	public static long TOTALCOMPUTATIONCOMMITMENTS;
	public static long TOTALCHOICEOFCOMMITMENT;
	public static long TOTALAPPLICATIONOFCOMMITMENT;
	private LinkedHashMap<String, String> latestCaptionToValue;
	
	//private String jarPath;

	public String currentStatus() {
		if(loadedModel==null){
			return "model not loaded";
		}
		else{
			return loadedModel.currentStatus();
		}
	}
	
	public String printModel() {
		if(loadedModel==null){
			return "model not loaded";
		}
		else{
			return loadedModel.toString();
		}
	}
	
	public QFlanJavaState(QFlanModel model) {
		super(new ParametersForState("Provided already loaded", ""));
		this.loadedModel=model;
	}
	
	public QFlanJavaState(ParametersForState parameters) {
		super(parameters);
		
		this.nameOfTheJarForBM=parameters.getNameOfTheJarForBM();

		/*String otherParams = parameters.getOtherParameters();
		if(otherParams!=null && !otherParams.equals("")){
			jarPath=otherParams.trim();
			if(otherParams.startsWith("-cp ")){
				jarPath=otherParams.substring(4);
			}
			
		}*/
		
		//System.out.println("Before loading the model");
		IQFlanModelBuilder modelBuilder = loadModel(getModelName());
		//System.out.println("After loading the model: " + modelBuilder);
		//modelBuilder = (IQFlanModelBuilder)ClassLoader.getSystemClassLoader().loadClass("BikesIDE").newInstance();			
		//IQFlanModelBuilder bikesSPLCBuilder = new BikesSPLC();
		//IQFlanModelBuilder bikesSPLCBuilder = new BikesIDE();
		try {
			loadedModel = modelBuilder.createModel();
		} catch (Z3Exception e) {
			e.printStackTrace();
		}
		
		//"-o","-logFile "+logFile
		String other = parameters.getOtherParameters();
		other = other.replace("-logFile ", "");
		if(other.length()>0) {
			logFile=other;
			toLog=true;
		}
	}

	private IQFlanModelBuilder loadModel(String modelAbsolutePath) {
		if(modelAbsolutePath.equals("bikes")){
			//return new TestBikesWithoutFakeFeatures2();
			return new BikesWithoutFakeFeatures2();
		}
		else if(modelAbsolutePath.equals("elevator")){
			return new Elevator();
		}
		else if(modelAbsolutePath.equalsIgnoreCase("bikesdiagram")){
			return new BikesDiagram();
		}
		
		File sourceFile = null;
		JavaCompiler compiler = null;
		URLClassLoader classLoader;
		File root;
		IQFlanModelBuilder modelBuilder=null;
		
		//root = new File("/models/abc/d-e");//new File("/Users/andrea/Dropbox/runtime-EclipseApplication/qflan2/src-gen");//new File("/models/abc");//new File("/models"); // On Windows running on C:\, this is C:\java.
		//root = new File("/Users/andrea/Dropbox/runtime-EclipseApplication/qflan2/src-gen");
		String rootName = modelAbsolutePath.substring(0,modelAbsolutePath.lastIndexOf(File.separator));
		String modelName = modelAbsolutePath.substring(modelAbsolutePath.lastIndexOf(File.separator)+1);
		if(modelAbsolutePath.endsWith(".qflan")) {
			rootName = rootName + File.separator + "src-gen";
			modelName = modelName.replace(".qflan", ".java");
		}
		root = new File(rootName);
		sourceFile = new File(root, modelName);
		File compilationLog = new File(root,sourceFile.getName().replace(".java", ".log"));
		OutputStream stream=null;
		try {
			stream = new FileOutputStream(compilationLog);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		
		String javaclassPath = System.getProperty("java.class.path");
		if(nameOfTheJarForBM!=null&&nameOfTheJarForBM.length()>0) {
			//TODO: gli devo passare il nameofthejar
			//String nameOfTheJar="/Users/andrea/multivestaRisQFLan.jar";
			javaclassPath=javaclassPath+File.pathSeparator+nameOfTheJarForBM;
			//javaclassPath=javaclassPath+File.pathSeparator+"/Users/andrea/multivestaRisQFLan.jar";
		}
		
		compiler = ToolProvider.getSystemJavaCompiler();
		int res = compiler.run(null, null, stream,"-cp",javaclassPath,sourceFile.getPath());
		//int res = compiler.run(null, null, stream, sourceFile.getPath()/*,"-cp",cp*/);
		if(res==0){
			System.out.println("The compilation succeeded: "+res);
		}
		else{
			System.out.println("Compilation failed: "+res);
			return null;
		}
		
		//Load and instantiate compiled class.
		try {
			URL rootUrl = root.toURI().toURL();
			if(nameOfTheJarForBM!=null&&nameOfTheJarForBM.length()>0) {
				URL jarURL = new File(nameOfTheJarForBM).toURI().toURL();
				classLoader = URLClassLoader.newInstance(new URL[] { jarURL, rootUrl });
			}
			else {
				classLoader = URLClassLoader.newInstance(new URL[] {         rootUrl });
			}
			
			//classLoader = URLClassLoader.newInstance(new URL[] {         rootUrl });
			String className = sourceFile.getName().replace(".java", "");
			Class<?> cls = Class.forName(className, true, classLoader);
			//Object inst = cls.newInstance(); //cls.getDeclaredConstructor().newInstance(null);
			Object inst=null;
			try {
				inst = cls.getConstructors()[0].newInstance();
			} catch (IllegalArgumentException | InvocationTargetException | SecurityException e) {
			//} catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			modelBuilder = (IQFlanModelBuilder)inst;
			//System.out.println(modelBuilder);
		
//		// Load and instantiate compiled class.
//		try {
//			URL rootUrl = root.toURI().toURL();
//			//URL jarURL = new File(jarPath).toURI().toURL();
//			classLoader = URLClassLoader.newInstance(new URL[] { /*jarURL ,*/rootUrl });
//			String className = sourceFile.getName().replace(".java", "");
//			Class<?> cls = Class.forName(className, true, classLoader);
//			Object inst = cls.newInstance();
//			 modelBuilder = (IQFlanModelBuilder)inst;
//			//System.out.println(modelBuilder);
		} catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.exit(-1);
		} 
		return modelBuilder;
	}
	
	/*
	private IQFlanModelBuilder loadModelWRONG(String modelAbsolutePath) {
		if(modelAbsolutePath.equals("bikes")){
			return new BikesWithoutFakeFeatures();
		}
		else if(modelAbsolutePath.equals("elevator")){
			return new ElevatorOLD();
		} 
		
		File sourceFile = null;
		JavaCompiler compiler = null;
		URLClassLoader classLoader;
		File root;
		IQFlanModelBuilder modelBuilder=null;
		
		//root = new File("/models/abc/d-e");//new File("/Users/andrea/Dropbox/runtime-EclipseApplication/qflan2/src-gen");//new File("/models/abc");//new File("/models"); // On Windows running on C:\, this is C:\java.
		//root = new File("/Users/andrea/Dropbox/runtime-EclipseApplication/qflan2/src-gen");
		String rootName = modelAbsolutePath.substring(0,modelAbsolutePath.lastIndexOf(File.separator));
		rootName = rootName + File.separator + "src-gen";
		root = new File(rootName);
		String modelName = modelAbsolutePath.substring(modelAbsolutePath.lastIndexOf(File.separator)+1);
		modelName = modelName.replace(".qflan", ".java");
		sourceFile = new File(root, modelName);
		File compilationLog = new File(root,sourceFile.getName().replace(".java", ".log"));
		OutputStream stream=null;
		try {
			stream = new FileOutputStream(compilationLog);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		
		{
			//StandardJavaFileManager fileManager = compiler.getStandardFileManager(this , null, null);
			// get compilationunits from somewhere, for instance via fileManager.getJavaFileObjectsFromFiles(List<file> files)
			//List<String> options = new ArrayList<String>();
		///	options.add("-classpath");
			//StringBuilder sb = new StringBuilder();
			
			//ClassLoader aaa = getClass().getClassLoader();
			
			//URLClassLoader urlClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
//			ClassLoader bbb = Thread.currentThread().getContextClassLoader();
//			bbb.getSystemClassLoader().
//			for (URL url : urlClassLoader.getURLs())
//				sb.append(url.getFile()).append(File.pathSeparator);
//			options.add(sb.toString());
			//CompilationTask task = compiler.getTask(null, fileManager, this , options, null, compilationUnits);
			//task.call();
		}
		
		//String lp=System.getProperty("java.library.path");
		
		compiler = ToolProvider.getSystemJavaCompiler();
		String cp=System.getProperty("java.class.path");
		if(jarPath!=null){
			if(cp.equals("")){
				cp=jarPath;
			}
			else{
				cp=jarPath+":"+cp;
			}
		}
		//cp = "/Users/andrea/Dropbox/workspaceSDK/it.imt.qflan.xtext.ui/bin:/Users/andrea/Dropbox/workspaceSDK/it.imt.qflan.xtext/bin:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.xtext_2.9.2.v201603040440.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.emf.ecore.xmi_2.12.0.v20160315-0423.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.emf.ecore_2.12.0.v20160315-0423.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.emf.common_2.12.0.v20160315-0423.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.antlr.runtime_3.2.0.v201101311130.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/com.google.inject_3.0.0.v201312141243.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.emf.mwe.core_1.3.20.v201603221017.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.apache.commons.cli_1.2.0.v201404270220.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.emf.mwe2.runtime_2.9.0.v201603221017.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.emf.mwe.utils_1.3.20.v201603221017.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.xtext.util_2.9.2.v201603040440.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/com.google.guava_15.0.0.v201403281430.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/javax.inject_1.0.0.v20091030.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.xtext.xbase_2.9.2.v201603040440.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.xtext.common.types_2.9.2.v201603040440.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.xtend.lib_2.9.2.v201603040440.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.xtext.xbase.lib_2.9.2.v201603040440.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.xtend.lib.macro_2.9.2.v201603040440.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.equinox.common_3.8.0.v20160315-1450.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.xtext.xtext.generator_2.9.2.v201603040440.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.emf.codegen.ecore_2.12.0.v20160321-0508.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.emf.codegen_2.11.0.v20160321-0508.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.emf.mwe2.launch_2.9.0.v201603221017.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.emf.mwe2.language_2.9.0.v201603221017.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.emf.mwe2.lib_2.9.0.v201603221017.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.objectweb.asm_5.0.1.v201404251740.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.apache.commons.logging_1.1.1.v201101211721.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.apache.log4j_1.2.15.v201012070815.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/com.ibm.icu_56.1.0.v201601250100.jar:/Users/andrea/Dropbox/workspaceSDK/it.imt.qflan.xtext.ide/bin:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.xtext.ide_2.9.2.v201603040440.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.xtext.xbase.ide_2.9.2.v201603040440.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.xtext.ui_2.9.2.v201603040440.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.core.runtime_3.12.0.v20160222-1238.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.osgi_3.11.0.v20160309-1913.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.equinox.transforms.hook_1.1.0.v20131021-1933.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.equinox.weaving.hook_1.1.200.v20150730-1648.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.osgi.compatibility.state_1.0.100.v20150709-1617.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.equinox.region_1.3.0.v20150929-2134.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.core.jobs_3.8.0.v20160209-0147.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.equinox.registry_3.6.100.v20160223-2218.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.equinox.preferences_3.6.0.v20160120-1756.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.core.contenttype_3.5.100.v20160310-1346.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.equinox.app_1.3.400.v20150715-1528.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.core.resources_3.11.0.v20160310-1245.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.ui_3.107.0.v20160108-0627.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.swt_3.105.0.v20160317-0027.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.swt.cocoa.macosx.x86_64_3.105.0.v20160317-0027.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.jface_3.12.0.v20160310-1055.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.core.commands_3.8.0.v20160316-1921.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.ui.workbench_3.108.0.v20160311-1856.jar:/Users/andrea/Dropbox/workspaceSDK/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/plugins/org.eclipse.e4.ui.workbench3_0.13.0.v20150727-1801.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.ui.editors_3.10.0.v20160222-1603.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.core.filebuffers_3.6.0.v20160308-1118.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.jface.text_3.11.0.v20160314-1855.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.text_3.6.0.v20160225-0827.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.emf.edit.ui_2.12.0.v20160321-0508.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.ui.views_3.8.0.v20160224-1855.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.emf.edit_2.12.0.v20160321-0508.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.emf.ecore.change_2.11.0.v20160315-0423.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.emf.common.ui_2.11.0.v20160321-0508.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.ltk.core.refactoring_3.7.0.v20160120-1732.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.xtext.ui.shared_2.9.2.v201603040440.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.xtext.ui.codetemplates.ui_2.9.2.v201603040440.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.xtext.ui.codetemplates_2.9.2.v201603040440.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.ui.ide_3.12.0.v20160316-1326.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.compare_3.5.700.v20151225-0213.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.compare.core_3.5.500.v20150505-1058.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.xtext.builder_2.9.2.v201603040440.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.debug.ui_3.11.200.v20160307-1120.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.debug.core_3.10.100.v20160203-1134.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.nebula.visualization.xygraph_2.0.0.201604151927.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.draw2d_3.10.100.201603210204.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.ui.console_3.6.100.v20160226-2242.jar:/Users/andrea/Dropbox/workspaceSDK/multivestaPluginProject/bin:/Users/andrea/Dropbox/workspaceSDK/multivestaPluginProject/lib/argparser.jar:/Users/andrea/Dropbox/workspaceSDK/multivestaPluginProject/lib/colt.jar:/Users/andrea/Dropbox/workspaceSDK/multivestaPluginProject/lib/concurrent.jar:/Users/andrea/Dropbox/workspaceSDK/multivestaPluginProject/lib/deus-0.5.1.jar:/Users/andrea/Dropbox/workspaceSDK/multivestaPluginProject/lib/jmathplot.jar:/Users/andrea/Dropbox/workspaceSDK/multivestaPluginProject/lib/commons-math3-3.2.jar:/Users/andrea/Dropbox/workspaceSDK/multivestaPluginProject/lib/py4j0.8.2.1.jar:/Users/andrea/Dropbox/workspaceSDK/multivestaPluginProject/lib/tools_openjdk.jar:/Users/andrea/Dropbox/workspaceSDK/multivestaPluginProject/lib/ssj.jar:/Users/andrea/Dropbox/workspaceSDK/it.imt.qflan.core/bin:/Users/andrea/Dropbox/workspaceSDK/it.imt.qflan.core/lib/z3/com.microsoft.z3.jar:/Applications/Eclipse-SDK.app/Contents/Eclipse/plugins/org.eclipse.ui.workbench.texteditor_3.10.0.v20160226-1512.jar";
		//String flag = "-cp "+cp;
		int res = compiler.run(null, null, stream, sourceFile.getPath(),"-cp",cp);
		if(res==0){
			System.out.println("The compilation succeeded: "+res);
		}
		else{
			System.out.println("Compilation failed: "+res);
			return null;
		}
		// Load and instantiate compiled class.
		try {
			URL rootUrl = root.toURI().toURL();
			URL jarURL = new File(jarPath).toURI().toURL();
			classLoader = URLClassLoader.newInstance(new URL[] { jarURL ,rootUrl });
			String className = sourceFile.getName().replace(".java", "");
			Class<?> cls = Class.forName(className, true, classLoader);
			Object inst = cls.newInstance();
			 modelBuilder = (IQFlanModelBuilder)inst;
			//System.out.println(modelBuilder);
		} catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.exit(-1);
		} 
		return modelBuilder;
		
		
		
		
//		String fileAbsPath = getModelName();
//		String path = fileAbsPath.substring(0,fileAbsPath.lastIndexOf(File.separator));
//		path = path+File.separator+"src-gen";
//		String name = fileAbsPath.substring(fileAbsPath.lastIndexOf(File.separator)+1,fileAbsPath.length());
//		name=name.replace(".qflan", ".java");
//		
//		System.out.println(path);
//		System.out.println(name);
//		
//		root = new File(path); // On Windows running on C:\, this is C:\java.
//		sourceFile = new File(root, name);
//		File compilationLog = new File(root,name.replace(".java", ".log"));
//		OutputStream stream=null;
//		try {
//			stream = new FileOutputStream(compilationLog);
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//			System.exit(-1);
//		}
//		compiler = ToolProvider.getSystemJavaCompiler();
//		int res = compiler.run(null, null, stream, sourceFile.getPath());
//		System.out.println("Compilation result is: "+res);
//		// Load and instantiate compiled class.
//		try {
//			classLoader = URLClassLoader.newInstance(new URL[] { new File("src-gen").toURI().toURL() });
//			//classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
//			Class<?> cls = Class.forName(name.replace(".java", ""), true, classLoader);
//			 modelBuilder = (IQFlanModelBuilder)cls.newInstance();
//			//System.out.println(modelBuilder);
//		} catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//			e.printStackTrace();
//			System.exit(-1);
//		}
//		return modelBuilder; 
	}*/

	@Override
	public void setSimulatorForNewSimulation(int seed) {
		//System.out.println("setSimulatorForNewSimulation");
		try {
			loadedModel.resetToInitialState();
		} catch (Z3Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		randomGenerator = new RandomEngineFacilities(new MersenneTwister(this.getCurrentSeed()));
		/*DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		System.out.println("New simulation at "+dateFormat.format(new Date())); //2014/08/06 15:59:48*/
		
		TOTALCOMPUTATIONCOMMITMENTS=0;
		TOTALCHOICEOFCOMMITMENT=0;
		TOTALAPPLICATIONOFCOMMITMENT=0;
		
		if(toLog) {
			LinkedHashMap<String, String> captionToValue = computeDataToLog("reset");
			addRowToLog(captionToValue);
		}
	}

	public LinkedHashMap<String, String> computeDataToLog() {
		return computeDataToLog(loadedModel.getLastPerformedActionName());
	}
	private LinkedHashMap<String, String> computeDataToLog(String performedActivity) {
		LinkedHashMap<String, String> captionToValue = defaultLogInfo(true);
		captionToValue.put("activity", cleanAction(performedActivity));
		return captionToValue;
	}
	
	@Override
	public void noMoreStepsNecessary() {
		super.noMoreStepsNecessary();
		addSimulationTerminatedToLog();
	}
	private void addSimulationTerminatedToLog() {
		if(toLog && latestCaptionToValue!=null) {
			latestCaptionToValue.put("activity", "simulationTerminated");
			addRowToLog(latestCaptionToValue);
			latestCaptionToValue=null;
		}
	}

	
	private void addRowToLog(LinkedHashMap<String, String> captionToValue) {
		StringBuffer row = new StringBuffer();
		for(Entry<String, String> entry:captionToValue.entrySet()) {
			row.append(entry.getValue());
			row.append(",");
		}
		row.deleteCharAt(row.length()-1);
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(logFile,true));
			writer.write(row.toString());
			writer.write("\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private LinkedHashMap<String, String> defaultLogInfo(boolean stepMinusOne) {
		LinkedHashMap<String, String> captionToValue=new LinkedHashMap<>();
		
		//case id
		captionToValue.put("caseID", this.getCurrentSeed()+"");
		
		//time
		int time =this.getNumberOfSteps();
		if(stepMinusOne) {
			time--;
		}
		captionToValue.put("time", time+"");
		
		
		//state
		captionToValue.put("state", loadedModel.getCurrentState().getName());
		//concrete features
		for(ConcreteFeature feature : loadedModel.getConcreteFeaturesDefs()) {
			boolean installed = loadedModel.isInstalled(feature);
			captionToValue.put(feature.getName(), String.valueOf(installed));
		}
		//abstract features
		for(AbstractFeature feature : loadedModel.getAbstractFeaturesDefs()) {
			boolean installed = loadedModel.isInstalled(feature);
			captionToValue.put(feature.getName(), String.valueOf(installed));
		}
		
		//variables
		for(Entry<String, QFLanVariable> entry : loadedModel.getVariables().entrySet()) {
			QFLanVariable var = entry.getValue();
			double val=var.eval(null);
			captionToValue.put(var.getName(), String.valueOf(val));
		}
		
		//activity is add outside
		
		latestCaptionToValue=captionToValue;
		
		return captionToValue;
	}
	public static ArrayList<String> logCaption(QFlanModel loadedModel) {
		ArrayList<String> caption=new ArrayList<>();
		caption.add("caseID");
		caption.add("time");
		caption.add("state");
		
		//concrete features
		for(ConcreteFeature feature : loadedModel.getConcreteFeaturesDefs()) {
			caption.add(feature.getName());
		}
		//abstract features
		for(AbstractFeature feature : loadedModel.getAbstractFeaturesDefs()) {
			caption.add(feature.getName());
		}
		
		//variables
		for(Entry<String, QFLanVariable> entry : loadedModel.getVariables().entrySet()) {
			QFLanVariable var = entry.getValue();
			caption.add(var.getName());
		}
		
		caption.add("activity");
		
		return caption;
	}
	
	@Override
	public void performOneStepOfSimulation() {
		//System.out.println("performOneStepOfSimulation");
		//try {
			double totalRate = computeAllowedCommittments();
			//QFlanModel.printTime("\nCompuptation of allowedCommitments",begin,end);
			if(loadedModel.getNumberOfComputedCommitments()==0){
				//setLastStateAlreadyComputed(true);
			}
			else{
				long begin = System.currentTimeMillis();
				ICommitment comm = chooseCommitment(totalRate);
				//ICommitment comm = bikesSPLC.getAllowedCommitmentEfficient(sampledNumber);
				long end = System.currentTimeMillis();
				TOTALCHOICEOFCOMMITMENT+= (end-begin);
				//QFlanModel.printTime("Choice of commitment",begin,end);
				applyChosenCommitment(comm);
				//QFlanModel.printTime("Application of commitment",begin,end);
				
				if(toLog) {
//					LinkedHashMap<String, String> captionToValue = defaultLogInfo(false);
//					IAction executed = comm.getAction();
//					captionToValue.put("activity", cleanAction(executed.getName()));
					LinkedHashMap<String, String> captionToValue = computeDataToLog(comm.getAction().getName());
					addRowToLog(captionToValue);
				}
			}
		/*} catch (Z3Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}*/
	}

	private String cleanAction(String name) {
		return name.replace(",", ";");
	}

	public ICommitment chooseCommitment(double totalRate) {
		//nextDouble()
		//Returns a 64 bit uniformly distributed random number in the open unit interval (0.0,1.0) (excluding 0.0 and 1.0).

		double sampledDouble = randomGenerator.nextDouble();
		double sampledNumber = sampledDouble * totalRate;
		ICommitment comm = loadedModel.getAllowedCommitment(sampledNumber);
		return comm;
	}

	public void applyChosenCommitment(ICommitment comm) /*throws Z3Exception*/ {
		long begin;
		long end;
		begin = System.currentTimeMillis();
		try {
			loadedModel.apply(comm);
		} catch (Z3Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(comm.getAction()+" applied");
		end = System.currentTimeMillis();
		TOTALAPPLICATIONOFCOMMITMENT+= (end-begin);
	}

	public double computeAllowedCommittments(){
		return computeAllowedCommittments(null);
	}
	
	public double computeAllowedCommittments(ArrayList<ICommitment> forbiddenCommittments) /*throws Z3Exception*/ {
		long begin = System.currentTimeMillis();
		double totalRate=0;
		try {
			totalRate = loadedModel.computeAllowedCommitments(forbiddenCommittments);
		} catch (Z3Exception e) {
			//NewVesta.printStackTrace(out, e);
			e.printStackTrace();
		}
		//double totalRate=bikesSPLC.computeAllowedCommitmentsEfficient();
		long end = System.currentTimeMillis();
		TOTALCOMPUTATIONCOMMITMENTS+= (end-begin);
		if(loadedModel.getNumberOfComputedCommitments()==0){
			setLastStateAlreadyComputed(true);
		}
		return totalRate;
	}
	
	public DoubleAndCommitment[] getComputedCommitments(){
		return loadedModel.getAllCommitments();
	}
	
	public int getNumberOfAllowedCommitments(){
		return loadedModel.getNumberOfComputedCommitments();
	}
	
	@Override
	public void performWholeSimulation() {
		//System.out.println("performWholeSimulation");
		while(!isLastStateAlreadyComputed()){
			performOneStepOfSimulation();
		}
	}
	

	@Override
	public double rval(int observation) {
		//System.out.println("rval");
		if(observation == 1){
			//System.out.println("End rval("+r+")");
			return (double)this.getNumberOfSteps();
		}
		else{
			throw new UnsupportedOperationException("Unsupported observation "+observation);
		}
	}

	@Override
	public double rval(String whichObservation) {
		//System.out.println("rval string");
		whichObservation = whichObservation.trim();
		if(whichObservation.equalsIgnoreCase(DEFAULTOBSERVATIONS.STEPS.toString())){
			return (double)this.getNumberOfSteps();
		}
		else if(whichObservation.equalsIgnoreCase(DEFAULTOBSERVATIONS.TERMINATED.toString())||
				whichObservation.equalsIgnoreCase(DEFAULTOBSERVATIONS.DEADLOCK.toString())){
			if(isLastStateAlreadyComputed()){
				return 1.0;
			}
			else {
				return 0.0;
			}
		}
		/*else if(whichObservation.equalsIgnoreCase(DEFAULTOBSERVATIONS.DEADLOCK.toString())){
			if(!isLastStateAlreadyComputed()){
				//If with the last "next" we generated a new state, we cannot be in deadlock (in case no more states can be generated starting from this state, in the next state we will notice it ...)
				return 0.0;
			}
			else{
				String maudeCommand = "red in "+ rootModule + " : val(\"isNilProcess\", [ " + term + " ]) ." ;
				this.maude.send(maudeCommand + "\n");
				double ret = completeObservationEvaluation();
				return 1.0 - ret;
			}
		}*/
		IFeature toSearch = loadedModel.getFeature(whichObservation);
		if(toSearch!=null){
			if(loadedModel.isInstalled(toSearch)){
				return 1;
			}
			else{
				return 0;
			}
		}
		QFLanVariable qflanVariable = loadedModel.getVariable(whichObservation);
		if(qflanVariable!=null){
			return loadedModel.eval(qflanVariable);
		}
		
		whichObservation=whichObservation.replace(" ", "");
		int openPar=whichObservation.indexOf('(');
		int closedPar=whichObservation.indexOf(')');
		if(openPar>0 && closedPar>0 && closedPar>openPar && whichObservation.endsWith(")")){
			String predName=whichObservation.substring(0, openPar);
			String featureName= whichObservation.substring(openPar+1,closedPar);
			IPredicateDef predToSearch = loadedModel.getPredicateDef(predName);
			if(predToSearch!=null){
				double val = predToSearch.eval(loadedModel.getFeature(featureName), loadedModel.getInstalledFeatures());
				return val;
			}
		}
		else{
			IPredicateDef predToSearch = loadedModel.getPredicateDef(whichObservation);
			if(predToSearch!=null){
				double val = loadedModel.totalValOfInstalledFeatures(predToSearch);
				return val;
			}
		}
		throw new UnsupportedOperationException("Unsupported observation "+whichObservation);
	}

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public double getTime() {
		try {
			throw new Exception("The method getTime is not supported for QFLAN. Time does not exist in QFLAN.");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return -1.0;
	}

	public String computeSATMessage() {
		return loadedModel.computeSATMessage();
	}
	
}
