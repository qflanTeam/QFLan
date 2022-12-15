package it.imt.ui.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
//import java.util.List;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
//import org.eclipse.core.resources.IWorkspace;
//import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.PlatformUI;
//import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
/*import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.utils.EditorUtils;
import org.eclipse.xtext.util.ITextRegion;*/
/*import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.utils.EditorUtils;*/


import com.microsoft.z3.Z3Exception;

import it.imt.MyParserUtil;
import it.imt.qflan.core.dialogs.IMessageDialogShower;
import it.imt.qflan.core.features.AbstractFeature;
import it.imt.qflan.core.features.ConcreteFeature;
import it.imt.qflan.core.features.interfaces.IFeature;
//import it.imt.qFLan.AbstractFeature;
//import it.imt.qFLan.AbstractFeatures;
import it.imt.qFLan.Action;
import it.imt.qFLan.ActionOrFeature;
import it.imt.qFLan.ActionRequires;
import it.imt.qFLan.Analysis;
import it.imt.qFLan.AskOrStoreModifierActionOrReferenceToActionOrToFeature;
import it.imt.qFLan.BoolExpr;
import it.imt.qFLan.CrossTreeConstraint;
import it.imt.qFLan.CrossTreeConstraints;
import it.imt.qFLan.Expression;
//import it.imt.qFLan.ConcreteFeature;
//import it.imt.qFLan.ConcreteFeatures;
import it.imt.qFLan.FatherAndSons;
import it.imt.qFLan.FatherAndSonsRelations;
import it.imt.qFLan.Feature;
import it.imt.qFLan.FeatureExcludes;
import it.imt.qFLan.FeaturePredicate;
import it.imt.qFLan.FeaturePredicateValue;
import it.imt.qFLan.FeatureRequires;
import it.imt.qFLan.ModelDefinition;
import it.imt.qFLan.NumberLiteral;
import it.imt.qFLan.ORFatherAndSons;
import it.imt.qFLan.PossiblyOptionalFeature;
import it.imt.qFLan.ProcessOfDiagram;
import it.imt.qFLan.ProcessState;
import it.imt.qFLan.ProcessTransition;
import it.imt.qFLan.QFLanVariables;
import it.imt.qFLan.QuantitativeConstraints;
import it.imt.qFLan.RealtionsAmongFeatures;
import it.imt.qFLan.RefToQFLanVariable;
import it.imt.qFLan.ReferenceToProcessDefinition;
import it.imt.qFLan.ReferenceToProcessState;

import it.imt.qFLan.SetOfConcreteFeatures;
import it.imt.qFLan.SetOfFeatures;
import it.imt.qFLan.SideEffects;
import it.imt.qFLan.Simulate;
import it.imt.qFLan.StoreModifierActionOrReferenceToActionOrToFeature;
import it.imt.qFLan.XORFatherAndSons;
//import it.imt.qFLan.SpecialActionOrReferenceToActionOrToFeature;
import it.imt.qflan.core.model.DoubleAndCommitment;
import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.multivesta.QFlanJavaState;
import it.imt.qflan.core.predicates.ArithmeticOperation;
import it.imt.qflan.core.predicates.ArithmeticPredicateExpr;
import it.imt.qflan.core.predicates.Constant;
import it.imt.qflan.core.predicates.Predicate;
import it.imt.qflan.core.predicates.PredicateDef;
import it.imt.qflan.core.predicates.interfaces.IPredicateDef;
import it.imt.qflan.core.predicates.interfaces.IPredicateExpr;
import it.imt.qflan.core.processes.Choice;
import it.imt.qflan.core.processes.Parallel;
import it.imt.qflan.core.processes.Prefix;
import it.imt.qflan.core.processes.ProcessDefinition;
import it.imt.qflan.core.processes.Sequential;
import it.imt.qflan.core.processes.ZeroProcess;
import it.imt.qflan.core.processes.actions.AskAction;
import it.imt.qflan.core.processes.actions.InstallAction;
import it.imt.qflan.core.processes.actions.NormalAction;
import it.imt.qflan.core.processes.actions.ReplaceAction;
import it.imt.qflan.core.processes.constraints.ActionRequiresConstraint;
import it.imt.qflan.core.processes.constraints.Alternative_ORCondition;
import it.imt.qflan.core.processes.constraints.Alternative_OrConstraint;
import it.imt.qflan.core.processes.constraints.BooleanConnector;
import it.imt.qflan.core.processes.constraints.BooleanConstraintExpr;
import it.imt.qflan.core.processes.constraints.DisequationOfPredicateExpressions;
import it.imt.qflan.core.processes.constraints.FalseConstraint;
import it.imt.qflan.core.processes.constraints.FeatureRequireConstraint;
import it.imt.qflan.core.processes.constraints.FeatureSetCondition;
import it.imt.qflan.core.processes.constraints.FeatureSetConstraint;
import it.imt.qflan.core.processes.constraints.HasFeature;
import it.imt.qflan.core.processes.constraints.NotConstraintExpr;
import it.imt.qflan.core.processes.constraints.PredicateExprComparator;
import it.imt.qflan.core.processes.constraints.TrueConstraint;
import it.imt.qflan.core.processes.interfaces.IAction;
import it.imt.qflan.core.processes.interfaces.ICommitment;
import it.imt.qflan.core.processes.interfaces.IConstraint;
import it.imt.qflan.core.processes.interfaces.IProcess;
//import it.imt.qflan.core.variables.MathEval;
import it.imt.qflan.core.variables.QFLanVariable;
import it.imt.qflan.core.variables.SideEffect;
import it.imt.qflan.simulation.output.IDataOutputHandler;
import it.imt.ui.perspective.dialogs.MessageDialogShower;
//import it.imt.ui.perspective.newWizards.ImportBNGWizard;
import it.imt.ui.perspective.plot.GUIDataOutputHandler;
import matheval.MathEval;
import it.imt.ui.MyConsoleUtil;
import vesta.NewVesta;
import vesta.mc.InfoMultiQuery;

/*import it.imt.erode.commandline.CRNReducerCommandLine;
import it.imt.erode.commandline.CommandsReader;
import it.imt.erode.commandline.IMessageDialogShower;
import it.imt.erode.crn.ModelDefKind;
import it.imt.erode.crn.ModelElementsCollector;
import it.imt.erode.crn.MyParserUtil;
import it.imt.erode.crn.chemicalReactionNetwork.ModelDefinition;
import it.imt.erode.crn.ui.MyConsoleUtil;
import it.imt.erode.crn.ui.perspective.dialogs.MessageDialogShower;
import it.imt.erode.crn.ui.perspective.plot.GUIDataOutputHandler;
import it.imt.erode.importing.GUICRNImporter;
import it.imt.erode.importing.ODEorNET;
import it.imt.erode.importing.UnsupportedFormatException;*/

/**
 * 
 * @author Andrea Vandin
 *
 */
public class MyMultiVeStAAnalysisExecutor {
	
	private static boolean librariesPresent=false;//private static boolean librariesPresent=false;
	protected static String jarPath="";
	private static final String libraryFileName="multivestaQFLan.jar";
	public static final String FILEWITHLIBRARYFILELOCATION="pathOfMultivestaQFLan";
	private static final int IR = 10;
	private static final int BLOCKSIZE = 80;//60;//30; //20;
	
	public void readAndExecuteMultiThreaded(ModelDefinition modelDef, boolean canSynchEditor, IProject project, IPath fullPathOfParent) {
		readAndExecute(modelDef,canSynchEditor,true,project,fullPathOfParent);
	}
	public void readAndExecute(ModelDefinition modelDef, boolean canSynchEditor, IProject project, IPath fullPathOfParent) {
		readAndExecute(modelDef,canSynchEditor,false,project,fullPathOfParent);
	}
	
	private void readAndExecute(ModelDefinition modelDef, boolean canSynchEditor,boolean multithreaded, IProject project,IPath fullPathOfParent) {
		MessageConsole console = MyConsoleUtil.generateConsole();
		//IOConsole console = MyConsoleUtil.generateConsole();
		MessageConsoleStream consoleOut = console.newMessageStream();
		consoleOut.println(MyConsoleUtil.computeWelcome(consoleOut));
		
		String workspacePath = project.getParent().getLocationURI().getPath();// .getFullPath().toOSString();
	    workspacePath = cleanWindowsPath(workspacePath);
		//String oldParentPath = fullPathOfParent.toFile().getAbsolutePath();
	     String parentPath = fullPathOfParent.toFile().getPath();
	     String absoluteParentPath = workspacePath+parentPath;
		
		while(!librariesPresent){
			try {
				checkLibraries();
			} catch (IOException e) {
				NewVesta.printStackTrace(consoleOut,e);
			}
		}
		
		
		
		/*try {
			QFlanModel model = new QFlanModel();
		} catch (Z3Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		
		
		
	     URI projectURI = project.getLocationURI();
	     String projectPath=projectURI.getPath();
	     projectPath=cleanWindowsPath(projectPath);
	     
	     //modelDef.getName();
	     
	     GUIDataOutputHandler guidog = new GUIDataOutputHandler(console);
	     
	     
	     String name = modelDef.getName();
	     String firstChar = name.substring(0, 1);
	     name = name.substring(1,name.length());
	     name = firstChar.toUpperCase()+name;
	     String fileAbsPath = projectPath+File.separator+name+".qflan";
	     //I should check if this file exists...
			/*if(fileAbsPath.endsWith(".qflan")){
				String path = fileAbsPath.substring(0,fileAbsPath.lastIndexOf(File.separator));
				String name = fileAbsPath.substring(fileAbsPath.lastIndexOf(File.separator)+1,fileAbsPath.length());
				fileAbsPath=path+"src-gen"+File.separator+name.replace(".qflan", ".java");
			}*/
			
	     Analysis analysis = null;
	     Simulate simulate = null;
	     for (EObject element : modelDef.getElements()) {
	    	 if(element instanceof Analysis){
	    		 analysis=(Analysis) element;
	    		 break;
	    	 }
	    	 if(element instanceof Simulate){
	    		 simulate=(Simulate) element;
	    		 break;
	    	 }
	     }
	     
	     QFlanModel model=null;
    	 try {
			model = populateQFlanModel(modelDef,consoleOut);
		} catch (Z3Exception e) {
			NewVesta.printStackTrace(consoleOut, e);
		}
    	 
    	 IConstraint unsat = model.checkSATCurrentState();
    	 if(unsat!=null){
    		 IMessageDialogShower msgVisualizer = new MessageDialogShower(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
 			//String msg = "The initial status does not satisfy the hierarchical, cross-tree or quantitative constraints.";
    		 String msg = "The initial state does not satisfy the constraint:\n"+unsat.toString();
 			msgVisualizer.openSimpleDialog(msg/*, DialogType.Error*/);
    		 return;
    	 }
	     
    	 String logFile=null;
	     String outputPath = projectPath+File.separator;
	     if(analysis!=null){
	    	 String queryFile = null;
	    	 String delta=null;
	    	 if(analysis.getQueryFile()!=null){
	    		 queryFile=analysis.getQueryFile();
	    		 delta=analysis.getDelta();
	    	 }
	    	 else{
	    		 MathEval math = new MathEval();
	    		 String defaultDelta=String.valueOf(math.evaluate(MyParserUtil.visitExpr(analysis.getDefaultDelta())));
	    		 queryFile="src-gen"+File.separator+"query"+modelDef.getName()+".multiquatex";
	    		 //math.eval(MyParserUtil.visitExpr(analysis.getDefaultDelta()));
	    		 List<String> deltas = MyParserUtil.parseDeltas(analysis.getQuery(), defaultDelta);
	    		 //deltas=MyParserUtil.parseDeltas(analysis.getQuery(), defaultDelta,math);
	    		 StringBuilder sb = new StringBuilder("[");
	    		 for (String d : deltas) {
					sb.append(math.evaluate(d));
					sb.append(',');
				}
	    		 sb.replace(sb.length()-1, sb.length(), "]");
	    		 delta=sb.toString();
	    	 }
	    	 
	    	 String query = MyParserUtil.computeFileName(queryFile, absoluteParentPath) ; //"/MultiQuaTEx/FeaturesInstalledAndPriceWeightLoadAtStep.quatex";

	    	 if(analysis.getLogFile()!=null) {
	    		 logFile = MyParserUtil.computeFileName(analysis.getLogFile(), absoluteParentPath);
	    	 }

		     String alpha = String.valueOf(analysis.getAlpha());//"0.1";
		    // String delta = analysis.getDelta(); //"[20.0,1.0,5.0,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1]";
		     int parallelism = analysis.getParallelism(); // 1;
		     int ir = MyMultiVeStAAnalysisExecutor.IR;
		     int blockSize= MyMultiVeStAAnalysisExecutor.BLOCKSIZE;
		     if(analysis.getBlockSize()!=0){
		    	 blockSize=analysis.getBlockSize();
		     }
		     if(analysis.getIr()!=0){
		    	 ir=analysis.getIr();
		     }
		     
		     if(ir<0){
		    	 //No intermediate results
		    	 ir=0;
		     }
		     
		     if(multithreaded){
		    	 MyMultiVeStAAnalysisExecutorWorker worker = new MyMultiVeStAAnalysisExecutorWorker(model,query,alpha,delta,parallelism,ir,guidog,consoleOut,project,fileAbsPath,getJarPath(),projectPath+File.separator,blockSize,logFile);
		    	 worker.start();
		     }
		     else{
		    	 /*InfoMultiQuery result = vesta.NewVesta.invokeClient(new String[]{
							"-sm", "false", "-sd", "it.imt.qflan.core.multivesta.QFlanJavaState", 
							"-m", fileAbsPath, "-l", "1", "-ir", "20", 
							"-op" , projectPath+File.separator,
							"-f", query,
							"-vp", "false",
							"-jn", getJarPath(), //ImportBNGWizard.getPossibleJarPaths(),
							"-bs", "20", "-a", String.valueOf(alpha), "-ds", delta, "-osws", "ONESTEP", "-sots", "12343"
					});*/

		    	 InfoMultiQuery result = invokeMultiVeStA(fileAbsPath, query, alpha, delta, parallelism,ir,getJarPath(),outputPath,blockSize,consoleOut,model,logFile);
		    	 visualizePlot(guidog,result, fileAbsPath, query, alpha, delta,consoleOut);
		    	 consoleOut.println(MyConsoleUtil.computeGoodbye(consoleOut));
		     }
		     
	     }
	     else{
	    	 if(model!=null){
	    		 String fileOut = MyParserUtil.computeFileName(simulate.getFile(), absoluteParentPath) ;
	    		 int seed = simulate.getSeed();
		    	 if(multithreaded){
		    		 MyMultiVeStAAnalysisExecutorWorker worker = new MyMultiVeStAAnalysisExecutorWorker(simulate.getSteps(),fileOut,model,seed, guidog, consoleOut, project, fileAbsPath, getJarPath(),projectPath+File.separator);
			    	 worker.start();
		    	 }
		    	 else{
		    		 try {
						performASimulation(simulate.getSteps(),fileOut,model,fileAbsPath, seed, consoleOut,getJarPath(),outputPath);
					} catch (IOException e) {
						NewVesta.printStackTrace(consoleOut, e);
					}
		    	 }
	    	 }
	     }
	     
	     
	     

	    

	}
	private String cleanWindowsPath(String path) {
		if(path.startsWith("/")&&path.contains(":")) {
	    	path=path.substring(1);
	    }
		return path;
	}
	
	private QFlanModel populateQFlanModel(ModelDefinition modelDef, MessageConsoleStream out) throws Z3Exception{
		QFlanModel model=null;
		try {
			model = new QFlanModel();
		} catch (Z3Exception e) {
			NewVesta.printStackTrace(out, e);
		}

		LinkedHashMap<String, QFLanVariable> variableNamesToVariable=new LinkedHashMap<>();
		LinkedHashMap<String, it.imt.qflan.core.features.AbstractFeature> abstractFeatureNameToAbstractFeature=new LinkedHashMap<String, AbstractFeature>();
		LinkedHashMap<String, it.imt.qflan.core.features.ConcreteFeature> concreteFeatureNameToConcreteFeature=new LinkedHashMap<String, ConcreteFeature>();
		LinkedHashMap<String, IPredicateDef> predicateNameToPredicateDef = new LinkedHashMap<String, IPredicateDef>();
		LinkedHashMap<String, NormalAction> actionNameToAction = new LinkedHashMap<String, NormalAction>();
		LinkedHashMap<String, ProcessDefinition> processDefinitionNameToProcessDefinition = new LinkedHashMap<String, ProcessDefinition>();
		LinkedHashMap<String, ProcessDefinition> processNameToFirstState = new LinkedHashMap<String, ProcessDefinition>();

		EList<it.imt.qFLan.AbstractFeature> abstractFeatures = null;
		EList<it.imt.qFLan.ConcreteFeature> concreteFeatures=null;
		EList<FatherAndSonsRelations> relationsAmongFeatures=null;

		EList<EObject> contents = modelDef.eContents();
		for (EObject element : contents) {
			if(element instanceof it.imt.qFLan.QFLanVariables){
				populateVariables(((QFLanVariables) element).getVariables(),model,out,variableNamesToVariable, abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature, predicateNameToPredicateDef);
			}
			//qflanVariables.add(new QFLanVariable(element.ge, value))
			else if(element instanceof it.imt.qFLan.AbstractFeatures){
				abstractFeatures = ((it.imt.qFLan.AbstractFeatures) element).getAbstractFeatures();
			}
			else if(element instanceof it.imt.qFLan.ConcreteFeatures){
				concreteFeatures = ((it.imt.qFLan.ConcreteFeatures) element).getConcreteFeatures();
			}
			else if(element instanceof RealtionsAmongFeatures){
				relationsAmongFeatures = ((RealtionsAmongFeatures) element).getRelations();
				populateFeatures(abstractFeatures,concreteFeatures,relationsAmongFeatures,model,out,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature);
			}
			else if(element instanceof CrossTreeConstraints){
				populateCrossTreeConstraints(((CrossTreeConstraints) element).getCrossTreeConstraints(),abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,model);
			}
			else if(element instanceof it.imt.qFLan.FeaturePredicates){
				populateFeaturePredicates(((it.imt.qFLan.FeaturePredicates) element).getFeaturePredicates(),predicateNameToPredicateDef,model,concreteFeatureNameToConcreteFeature);
			}
			else if(element instanceof QuantitativeConstraints){
				populateQuantitativeConstraints(((QuantitativeConstraints) element).getQuantitativeConstraints(),abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,model, variableNamesToVariable, predicateNameToPredicateDef);
			}
			else if(element instanceof it.imt.qFLan.Actions){
				populateActions(((it.imt.qFLan.Actions) element).getActions(),actionNameToAction,model);
			}
			else if(element instanceof it.imt.qFLan.ActionConstraints){
				populateActionConstraints(((it.imt.qFLan.ActionConstraints) element).getActionConstraints(),actionNameToAction,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,model, variableNamesToVariable, predicateNameToPredicateDef);
			}
			/*else if(element instanceof it.imt.qFLan.Constraints){
				populateConstraints(((it.imt.qFLan.Constraints) element).getConstraints(),actionNameToAction,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,model, variableNamesToVariable, predicateNameToPredicateDef);
			}*/
			else if(element instanceof it.imt.qFLan.Processes){
				visitProcesses(((it.imt.qFLan.Processes) element).getProcessDefinitions(),processDefinitionNameToProcessDefinition,model, actionNameToAction, variableNamesToVariable, predicateNameToPredicateDef, abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature);
			}
			else if(element instanceof it.imt.qFLan.ProcessDiagram){
				visitProcessDiagram(((it.imt.qFLan.ProcessDiagram) element).getProcesses(),processDefinitionNameToProcessDefinition,model, actionNameToAction, variableNamesToVariable, predicateNameToPredicateDef, abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature,processNameToFirstState);
			}
			else if(element instanceof it.imt.qFLan.InitWithProcesses){
				String name=((it.imt.qFLan.InitWithProcesses) element).getInitialProcess().getName();
				ProcessDefinition initialProcess=processDefinitionNameToProcessDefinition.get(name);
				model.setInitialState(listOfFeatures(
						((it.imt.qFLan.InitWithProcesses) element).getInstalledFeatures(), concreteFeatureNameToConcreteFeature), 
						initialProcess);
				model.resetToInitialState();
			}
			else if(element instanceof it.imt.qFLan.InitWithProcessDiagram){
				Collection<ConcreteFeature> features = listOfFeatures(((it.imt.qFLan.InitWithProcessDiagram) element).getInstalledFeatures(), concreteFeatureNameToConcreteFeature);
				EList<ProcessOfDiagram> processes = ((it.imt.qFLan.InitWithProcessDiagram) element).getProcesses();
				ProcessDefinition initial = null;
				if(processes.size()==1){
					initial=processNameToFirstState.get(processes.get(0).getName());
				}
				else{
					initial = new ProcessDefinition("init");
					Collection<IProcess> states=new ArrayList<IProcess>(processes.size());
					for (ProcessOfDiagram processOfDiagram : processes) {
						states.add(processNameToFirstState.get(processOfDiagram.getName()));
					}
					model.addProcessDefinition(initial, QFlanModel.makeMultiParallel(states));
				}
				model.setInitialState(features,initial);
				model.resetToInitialState();
			}
		}

		return model;
	}
	
	private void populateQuantitativeConstraints(EList<BoolExpr> quantitativeConstraints,
			LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature,
			LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature, QFlanModel model, LinkedHashMap<String, QFLanVariable> variableNameToVariable, LinkedHashMap<String, IPredicateDef> predicateNameToPredicateDef) {
		for (BoolExpr constraint : quantitativeConstraints) {
			try {
				model.addConstraint(visitConstraint(constraint, concreteFeatureNameToConcreteFeature, model, variableNameToVariable, abstractFeatureNameToAbstractFeature, predicateNameToPredicateDef));
			} catch (Z3Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	private void visitProcessDiagram(EList<it.imt.qFLan.ProcessOfDiagram> processes, LinkedHashMap<String, ProcessDefinition> processDefinitionNameToProcessDefinition,
			QFlanModel model, LinkedHashMap<String, NormalAction> actionNameToAction, LinkedHashMap<String, QFLanVariable> variableNameToVariable, LinkedHashMap<String, IPredicateDef> predicateNameToPredicateDef, LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature, LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature, LinkedHashMap<String, ProcessDefinition> processNameToFirstState) throws Z3Exception {
		LinkedHashMap<ProcessDefinition, List<IProcess>> processToOutgoingTransitions = new LinkedHashMap<>();
		MathEval math = new MathEval();
		for(ProcessOfDiagram element : processes){
			boolean first=true;
			for (ProcessState state :element.getStates().getStates()) {
				ProcessDefinition pdef = new ProcessDefinition(state.getName());
				processDefinitionNameToProcessDefinition.put(state.getName(), pdef);
				if(first){
					processNameToFirstState.put(element.getName(), pdef);
					first=false;
				}
			}
			for (ProcessTransition transition :element.getTransitions().getTransitions()) {
				ProcessDefinition source = processDefinitionNameToProcessDefinition.get(transition.getSource().getName());
				IProcess target = ZeroProcess.ZERO;
				if(transition.getTarget() instanceof ReferenceToProcessState){
					String targetName = ((ReferenceToProcessState)transition.getTarget()).getValue().getName();
					target=processDefinitionNameToProcessDefinition.get(targetName);
				}
				
				double rate = math.evaluate(MyParserUtil.visitExpr(transition.getRate()));
				IAction action = computeActionIncludingAskOrStoreModifierOrFeature(transition.getAction(),actionNameToAction,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,model, predicateNameToPredicateDef, variableNameToVariable);
				
				Prefix prefix = new Prefix(
						rate, 
						action,
						visitListOfSideEffects(transition.getSideEffects(),variableNameToVariable, abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature, predicateNameToPredicateDef), 
						target
						);
				
				List<IProcess> outgoingTransitions = processToOutgoingTransitions.get(source);
				if(outgoingTransitions==null){
					outgoingTransitions=new ArrayList<>();
					processToOutgoingTransitions.put(source,outgoingTransitions);
				}
				outgoingTransitions.add(prefix);
			}
		}
		
		for(ProcessDefinition pDef : processDefinitionNameToProcessDefinition.values()){
			List<IProcess> outgoingTransitions = processToOutgoingTransitions.get(pDef);
			IProcess multiChoice = QFlanModel.makeMultiChoice(outgoingTransitions);
			model.addProcessDefinition(pDef,multiChoice);
		}
	}
	
	private void visitProcesses(EList<it.imt.qFLan.ProcessDefinition> processDefinitions, LinkedHashMap<String, ProcessDefinition> processDefinitionNameToProcessDefinition,
			QFlanModel model, LinkedHashMap<String, NormalAction> actionNameToAction, LinkedHashMap<String, QFLanVariable> variableNameToVariable, LinkedHashMap<String, IPredicateDef> predicateNameToPredicateDef, LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature, LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature) throws Z3Exception {
		for(it.imt.qFLan.ProcessDefinition element : processDefinitions){
			//Process definitions
			ProcessDefinition pdef = new ProcessDefinition(element.getName());
			processDefinitionNameToProcessDefinition.put(element.getName(), pdef);
		}
		for(it.imt.qFLan.ProcessDefinition element : processDefinitions){
			ProcessDefinition pdef = processDefinitionNameToProcessDefinition.get(element.getName());
			model.addProcessDefinition(pdef, visitProcess(element.getBody(),processDefinitionNameToProcessDefinition,model,actionNameToAction,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,predicateNameToPredicateDef, variableNameToVariable));
		}
		
	}
	private IProcess visitProcess(it.imt.qFLan.ProcessExpr process,
			LinkedHashMap<String, ProcessDefinition> processDefinitionNameToProcessDefinition, QFlanModel model, /*SpecialActionOrReferenceToActionOrToFeature action,*/LinkedHashMap<String, NormalAction> actionNameToAction,LinkedHashMap<String, it.imt.qflan.core.features.AbstractFeature> abstractFeatureNameToAbstractFeature,LinkedHashMap<String, it.imt.qflan.core.features.ConcreteFeature> concreteFeatureNameToConcreteFeature, LinkedHashMap<String, IPredicateDef> predicateNameToPredicateDef, LinkedHashMap<String, QFLanVariable> variableNameToVariable) throws Z3Exception {
		MathEval math = new MathEval();
		if(process instanceof it.imt.qFLan.ZeroProcess){
			return ZeroProcess.ZERO;
		}
		else if(process instanceof it.imt.qFLan.Prefix){
			double rate=math.evaluate(MyParserUtil.visitExpr(((it.imt.qFLan.Prefix)process).getRate()));
			IAction action = computeActionIncludingAskOrStoreModifierOrFeature(((it.imt.qFLan.Prefix) process).getAction(),actionNameToAction,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,model, predicateNameToPredicateDef, variableNameToVariable);
			return new Prefix(
					rate, 
					action,
					visitListOfSideEffects(((it.imt.qFLan.Prefix) process).getSideEffects(),variableNameToVariable, abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature, predicateNameToPredicateDef), 
					visitProcess(((it.imt.qFLan.Prefix) process).getContinuation(),processDefinitionNameToProcessDefinition,model,actionNameToAction, abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature, predicateNameToPredicateDef, variableNameToVariable)
					);
		}
		else if(process instanceof it.imt.qFLan.ReferenceToProcessDefinition){
			return processDefinitionNameToProcessDefinition.get(((ReferenceToProcessDefinition)process).getValue().getName());
		}
		else if(process instanceof it.imt.qFLan.Choice){
			return new Choice(
					visitProcess(process.getFirst(), processDefinitionNameToProcessDefinition, model, actionNameToAction, abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature, predicateNameToPredicateDef, variableNameToVariable), 
					visitProcess(((it.imt.qFLan.Choice) process).getSecond(), processDefinitionNameToProcessDefinition, model, actionNameToAction, abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature, predicateNameToPredicateDef, variableNameToVariable)
					);
		}
		else if(process instanceof it.imt.qFLan.Sequential){
			return new Sequential(
					visitProcess(process.getFirst(), processDefinitionNameToProcessDefinition, model, actionNameToAction, abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature, predicateNameToPredicateDef, variableNameToVariable), 
					visitProcess(((it.imt.qFLan.Sequential) process).getSecond(), processDefinitionNameToProcessDefinition, model, actionNameToAction, abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature, predicateNameToPredicateDef, variableNameToVariable)
					);
		}
		else if(process instanceof it.imt.qFLan.Parallel){
			return new Parallel(
					visitProcess(process.getFirst(), processDefinitionNameToProcessDefinition, model, actionNameToAction, abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature, predicateNameToPredicateDef, variableNameToVariable), 
					visitProcess(((it.imt.qFLan.Parallel) process).getSecond(), processDefinitionNameToProcessDefinition, model, actionNameToAction, abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature, predicateNameToPredicateDef, variableNameToVariable)
					);
		}
		else if(process instanceof it.imt.qFLan.ProcessExpr){
			return visitProcess(process.getFirst(), processDefinitionNameToProcessDefinition, model, actionNameToAction, abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature, predicateNameToPredicateDef, variableNameToVariable);
		}
		throw new UnsupportedOperationException("Unsupported process: " + process.toString());
		//return null;
	}
	
	private void populateCrossTreeConstraints(EList<CrossTreeConstraint> crossTreeConstraints,
			LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature,
			LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature, QFlanModel model) {
		
		for (CrossTreeConstraint crossTreeConstraint : crossTreeConstraints) {
			try {
				model.addConstraint(visitCrossTreeConstraint(crossTreeConstraint,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,model));
			} catch (Z3Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private IConstraint visitCrossTreeConstraint(CrossTreeConstraint crossTreeConstraint, LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature, LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature,QFlanModel model) throws Z3Exception {
		if(crossTreeConstraint instanceof FeatureRequires){
			IFeature requirer = getFeature(((FeatureRequires) crossTreeConstraint).getRequirer(),abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature);
			IFeature required = getFeature(((FeatureRequires) crossTreeConstraint).getRequired(),abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature);
			return new FeatureRequireConstraint(requirer,required, model);
		}
		else if(crossTreeConstraint instanceof FeatureExcludes){
			IFeature first = getFeature(((FeatureExcludes) crossTreeConstraint).getFirst(),abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature);
			IFeature second = getFeature(((FeatureExcludes) crossTreeConstraint).getSecond(),abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature);
			Collection<IFeature> features=new ArrayList<>(2);
			features.add(first);
			features.add(second);
			return new FeatureSetConstraint(features, FeatureSetCondition.ATMOSTONE, model);
		}
		throw new UnsupportedOperationException("Unsupported crosstree constraint: " + crossTreeConstraint.toString());
	}
	/*private void populateConstraints(EList<BoolExpr> constraints,
			LinkedHashMap<String, NormalAction> actionNameToAction,
			LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature,
			LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature, QFlanModel model,
			LinkedHashMap<String, QFLanVariable> variableNamesToVariable,
			LinkedHashMap<String, IPredicateDef> predicateNameToPredicateDef) throws Z3Exception {
		
		for(BoolExpr element : constraints){
			model.addConstraint(visitConstraint(element, concreteFeatureNameToConcreteFeature, model, variableNamesToVariable, abstractFeatureNameToAbstractFeature, predicateNameToPredicateDef));
		}
		
	}*/
	private void populateActionConstraints(EList<ActionRequires> actionConstraints,
			LinkedHashMap<String, NormalAction> actionNameToAction, 
			LinkedHashMap<String, it.imt.qflan.core.features.AbstractFeature> abstractFeatureNameToAbstractFeature,LinkedHashMap<String, it.imt.qflan.core.features.ConcreteFeature> concreteFeatureNameToConcreteFeature,
			QFlanModel model,
			LinkedHashMap<String, QFLanVariable> variableNameToVariable, LinkedHashMap<String, IPredicateDef> predicateNameToPredicateDef
			) throws Z3Exception {
		for(ActionRequires element : actionConstraints){
			model.addActionConstraint(visitActionRequiresConstraint(element,actionNameToAction,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,model,variableNameToVariable,predicateNameToPredicateDef));
		}
		
	}
	private ActionRequiresConstraint visitActionRequiresConstraint(ActionRequires element,LinkedHashMap<String, NormalAction> actionNameToAction,LinkedHashMap<String, it.imt.qflan.core.features.AbstractFeature> abstractFeatureNameToAbstractFeature,LinkedHashMap<String, it.imt.qflan.core.features.ConcreteFeature> concreteFeatureNameToConcreteFeature,QFlanModel model, LinkedHashMap<String, QFLanVariable> variableNameToVariable, LinkedHashMap<String, IPredicateDef> predicateNameToPredicateDef) throws Z3Exception {
		IAction action=computeActionIncludingSpecialOrFeature(element.getAction(),actionNameToAction,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,model, predicateNameToPredicateDef, variableNameToVariable);
		IConstraint constraint = visitConstraint(element.getConstraint(),concreteFeatureNameToConcreteFeature,model,variableNameToVariable,abstractFeatureNameToAbstractFeature,predicateNameToPredicateDef				
				);
		return new ActionRequiresConstraint(action, constraint);
	}
	
	private IAction computeActionIncludingAskOrStoreModifierOrFeature(AskOrStoreModifierActionOrReferenceToActionOrToFeature action,LinkedHashMap<String, NormalAction> actionNameToAction,LinkedHashMap<String, it.imt.qflan.core.features.AbstractFeature> abstractFeatureNameToAbstractFeature,LinkedHashMap<String, it.imt.qflan.core.features.ConcreteFeature> concreteFeatureNameToConcreteFeature,QFlanModel model, LinkedHashMap<String, IPredicateDef> predicateNameToPredicateDef, LinkedHashMap<String, QFLanVariable> variableNameToVariable) throws Z3Exception {
		if(action instanceof StoreModifierActionOrReferenceToActionOrToFeature){
			return computeActionIncludingSpecialOrFeature((StoreModifierActionOrReferenceToActionOrToFeature)action,actionNameToAction,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,model,predicateNameToPredicateDef,variableNameToVariable);
		}
		else if(action instanceof it.imt.qFLan.AskAction){
				return new AskAction(visitConstraint(((it.imt.qFLan.AskAction) action).getQuestion(),concreteFeatureNameToConcreteFeature, model,variableNameToVariable,abstractFeatureNameToAbstractFeature,predicateNameToPredicateDef));
		}
		else{
			throw new UnsupportedOperationException("Unsupported action: " + action);
		} 
	}
	
	private IAction computeActionIncludingSpecialOrFeature(StoreModifierActionOrReferenceToActionOrToFeature action,LinkedHashMap<String, NormalAction> actionNameToAction,LinkedHashMap<String, it.imt.qflan.core.features.AbstractFeature> abstractFeatureNameToAbstractFeature,LinkedHashMap<String, it.imt.qflan.core.features.ConcreteFeature> concreteFeatureNameToConcreteFeature,QFlanModel model, LinkedHashMap<String, IPredicateDef> predicateNameToPredicateDef, LinkedHashMap<String, QFLanVariable> variableNameToVariable) throws Z3Exception {
		if(action instanceof it.imt.qFLan.Action){
			return actionNameToAction.get(((it.imt.qFLan.Action) action).getName());
		}
		else if(action instanceof it.imt.qFLan.ConcreteFeature){
			return concreteFeatureNameToConcreteFeature.get(((it.imt.qFLan.ConcreteFeature) action).getName());
		}
		else if(action instanceof it.imt.qFLan.ReferenceToActionOrFeature){
			ActionOrFeature referencedActionOrFeautre = ((it.imt.qFLan.ReferenceToActionOrFeature) action).getValue();
			if(referencedActionOrFeautre instanceof it.imt.qFLan.Action){
				return actionNameToAction.get(((it.imt.qFLan.Action) referencedActionOrFeautre).getName());
			}
			else if(referencedActionOrFeautre instanceof it.imt.qFLan.ConcreteFeature){
				ConcreteFeature f = concreteFeatureNameToConcreteFeature.get(((it.imt.qFLan.ConcreteFeature) referencedActionOrFeautre).getName());
				if(f==null){
					System.out.println("");
				}
				return f;
			} 
			else if(referencedActionOrFeautre instanceof it.imt.qFLan.AbstractFeature){
				AbstractFeature f = abstractFeatureNameToAbstractFeature.get(((it.imt.qFLan.AbstractFeature) referencedActionOrFeautre).getName());
				if(f==null){
					System.out.println("");
				}
				return f;
			}
		}
		else if(action instanceof it.imt.qFLan.StoreModifierActions){
			if(action instanceof it.imt.qFLan.InstallAction){
				return new InstallAction(concreteFeatureNameToConcreteFeature.get(((it.imt.qFLan.InstallAction) action).getFeature().getName()), true);
			}
			else if(action instanceof it.imt.qFLan.UninstallAction){
				return new InstallAction(concreteFeatureNameToConcreteFeature.get(((it.imt.qFLan.UninstallAction) action).getFeature().getName()), false);
			}
			else if(action instanceof it.imt.qFLan.ReplaceAction){
				it.imt.qFLan.ConcreteFeature toRemove = ((it.imt.qFLan.ReplaceAction) action).getToRemove();
				it.imt.qFLan.ConcreteFeature toAdd = ((it.imt.qFLan.ReplaceAction) action).getToAdd();
				return new ReplaceAction(concreteFeatureNameToConcreteFeature.get(toRemove.getName()),concreteFeatureNameToConcreteFeature.get(toAdd.getName()));
			}
			else if(action instanceof it.imt.qFLan.AskAction){
				return new AskAction(visitConstraint(((it.imt.qFLan.AskAction) action).getQuestion(),concreteFeatureNameToConcreteFeature,model,variableNameToVariable,abstractFeatureNameToAbstractFeature,predicateNameToPredicateDef));
			}
		}
		
		return null;
	}
	private IConstraint visitConstraint(BoolExpr constraint,LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature,QFlanModel model,
			LinkedHashMap<String, QFLanVariable> variableNameToVariable,LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature,LinkedHashMap<String, IPredicateDef> predicateNameToPredicateDef
			) throws Z3Exception {
		if(constraint instanceof it.imt.qFLan.FeatureRequires){
			IFeature requirer = getFeature(((it.imt.qFLan.FeatureRequires) constraint).getRequirer(), abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature);
			IFeature required = getFeature(((it.imt.qFLan.FeatureRequires) constraint).getRequired(), abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature);
			return new FeatureRequireConstraint(requirer, required, model);
		}
		else if(constraint instanceof it.imt.qFLan.HasFeature){
			Feature f = ((it.imt.qFLan.HasFeature) constraint).getFeature();
			String fName=null;
			if(f instanceof it.imt.qFLan.ConcreteFeature){
				fName=((it.imt.qFLan.ConcreteFeature) f).getName();
				return new HasFeature(concreteFeatureNameToConcreteFeature.get(fName), model);
			}
			else if(f instanceof it.imt.qFLan.AbstractFeature){
				fName=((it.imt.qFLan.AbstractFeature) f).getName();
				return new HasFeature(abstractFeatureNameToAbstractFeature.get(fName), model);
			}
			 
		} 
		else if(constraint instanceof it.imt.qFLan.FalseConstraint){
			return new FalseConstraint(); 
		} 
		else if(constraint instanceof it.imt.qFLan.TrueConstraint){
			return new TrueConstraint(); 
		} 
		else if(constraint instanceof it.imt.qFLan.NotConstraintExpr){
			return new NotConstraintExpr(visitConstraint(((it.imt.qFLan.NotConstraintExpr) constraint).getLeft(), concreteFeatureNameToConcreteFeature, model,variableNameToVariable,abstractFeatureNameToAbstractFeature,predicateNameToPredicateDef));
		} 
		else if(constraint instanceof it.imt.qFLan.AndBoolConstraintExpr){
			return new BooleanConstraintExpr(visitConstraint(((it.imt.qFLan.AndBoolConstraintExpr) constraint).getLeft(),concreteFeatureNameToConcreteFeature, model,variableNameToVariable,abstractFeatureNameToAbstractFeature,predicateNameToPredicateDef), 
											 visitConstraint(((it.imt.qFLan.AndBoolConstraintExpr) constraint).getRight(),concreteFeatureNameToConcreteFeature, model,variableNameToVariable,abstractFeatureNameToAbstractFeature,predicateNameToPredicateDef), BooleanConnector.AND);
		} 
		else if(constraint instanceof it.imt.qFLan.OrBoolConstraintExpr){
			return new BooleanConstraintExpr(visitConstraint(((it.imt.qFLan.OrBoolConstraintExpr) constraint).getLeft(),concreteFeatureNameToConcreteFeature, model,variableNameToVariable,abstractFeatureNameToAbstractFeature,predicateNameToPredicateDef), 
											 visitConstraint(((it.imt.qFLan.OrBoolConstraintExpr) constraint).getRight(),concreteFeatureNameToConcreteFeature, model,variableNameToVariable,abstractFeatureNameToAbstractFeature,predicateNameToPredicateDef), BooleanConnector.OR);
		}
		else if(constraint instanceof it.imt.qFLan.ImpliesBoolConstraintExpr){
			return new BooleanConstraintExpr(visitConstraint(((it.imt.qFLan.ImpliesBoolConstraintExpr) constraint).getLeft(),concreteFeatureNameToConcreteFeature, model,variableNameToVariable,abstractFeatureNameToAbstractFeature,predicateNameToPredicateDef), 
											 visitConstraint(((it.imt.qFLan.ImpliesBoolConstraintExpr) constraint).getRight(),concreteFeatureNameToConcreteFeature, model,variableNameToVariable,abstractFeatureNameToAbstractFeature,predicateNameToPredicateDef), 
											 BooleanConnector.IMPLIES);
		}
		/*
		else if(constraint instanceof it.imt.qFLan.AtLeastOne){
			return new FeatureSetConstraint(listOfFeatures(((it.imt.qFLan.AtLeastOne) constraint).getFeaturesSet(),concreteFeatureNameToConcreteFeature), FeatureSetCondition.ATLEASTONE, model);
		}
		else if(constraint instanceof it.imt.qFLan.AtMostOne){
			return new FeatureSetConstraint(listOfFeatures(((it.imt.qFLan.AtMostOne) constraint).getFeaturesSet(),concreteFeatureNameToConcreteFeature), FeatureSetCondition.ATMOSTONE, model);
		}
		else if(constraint instanceof it.imt.qFLan.PreciselyOne){
			Collection<ConcreteFeature> features=null;
			SetOfConcreteFeatures featureSet = ((it.imt.qFLan.PreciselyOne) constraint).getFeaturesSet();
			if(featureSet!=null){
				features = listOfFeatures(((it.imt.qFLan.PreciselyOne) constraint).getFeaturesSet(),concreteFeatureNameToConcreteFeature);
			}
			else{
				features= new ArrayList<ConcreteFeature>(1);
				features.add(concreteFeatureNameToConcreteFeature.get(((it.imt.qFLan.PreciselyOne) constraint).getFeature().getName()));
			}
			return new BooleanConstraintExpr(
					new FeatureSetConstraint(features, FeatureSetCondition.ATLEASTONE, model), 
					new FeatureSetConstraint(features, FeatureSetCondition.ATMOSTONE, model), 
					BooleanConnector.AND);
		}*/
		else if(constraint instanceof it.imt.qFLan.DisequationOfPredicateExpr){
			IPredicateExpr lhs = writeExpr(((it.imt.qFLan.DisequationOfPredicateExpr) constraint).getLhs(),variableNameToVariable,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,predicateNameToPredicateDef);
			IPredicateExpr rhs = writeExpr(((it.imt.qFLan.DisequationOfPredicateExpr) constraint).getRhs(),variableNameToVariable,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,predicateNameToPredicateDef);
			return new DisequationOfPredicateExpressions(lhs, rhs, computeComparator(((it.imt.qFLan.DisequationOfPredicateExpr) constraint).getComp()));
		}
		return null;
	}
	private IPredicateExpr writeExpr(Expression expr,LinkedHashMap<String, QFLanVariable> variableNameToVariable,LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature,LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature,LinkedHashMap<String, IPredicateDef> predicateNameToPredicateDef) {
		IPredicateExpr rightVisited=null;
		IPredicateExpr leftVisited=null;
		
		if(expr instanceof it.imt.qFLan.NumberLiteral){
			return new Constant(((NumberLiteral)expr).getValue());
		}
		else if(expr instanceof it.imt.qFLan.RefToQFLanVariable){
			return variableNameToVariable.get(((RefToQFLanVariable)expr).getVarqflan().getName());
		}
		else if(expr instanceof it.imt.qFLan.Predicate){
			it.imt.qFLan.Feature f = ((it.imt.qFLan.Predicate) expr).getFeature();
			IFeature feature=null;
			if(f instanceof it.imt.qFLan.AbstractFeature){
				feature=abstractFeatureNameToAbstractFeature.get(((it.imt.qFLan.AbstractFeature) f).getName());
			}
			else if(f instanceof it.imt.qFLan.ConcreteFeature){
				feature=concreteFeatureNameToConcreteFeature.get(((it.imt.qFLan.ConcreteFeature) f).getName());
			} 
			return new Predicate(predicateNameToPredicateDef.get(((it.imt.qFLan.Predicate) expr).getPredicate().getName()), feature);
		}
		else if(expr instanceof it.imt.qFLan.Addition || expr instanceof it.imt.qFLan.AdditionWithPredicates){
			if(expr instanceof it.imt.qFLan.Addition){
				leftVisited = writeExpr(((it.imt.qFLan.Addition) expr).getLeft(),variableNameToVariable,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,predicateNameToPredicateDef);
				rightVisited = writeExpr(((it.imt.qFLan.Addition) expr).getRight(),variableNameToVariable,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,predicateNameToPredicateDef);
			}
			else if(expr instanceof it.imt.qFLan.AdditionWithPredicates){
				leftVisited = writeExpr(((it.imt.qFLan.AdditionWithPredicates) expr).getLeft(),variableNameToVariable,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,predicateNameToPredicateDef);
				rightVisited = writeExpr(((it.imt.qFLan.AdditionWithPredicates) expr).getRight(),variableNameToVariable,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,predicateNameToPredicateDef);
			}
			return new ArithmeticPredicateExpr(leftVisited,rightVisited,ArithmeticOperation.SUM);
		}
		else if(expr instanceof it.imt.qFLan.Subtraction || expr instanceof it.imt.qFLan.SubtractionWithPredicates){
			if(expr instanceof it.imt.qFLan.Subtraction){
				leftVisited = writeExpr(((it.imt.qFLan.Subtraction) expr).getLeft(),variableNameToVariable,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,predicateNameToPredicateDef);
				rightVisited = writeExpr(((it.imt.qFLan.Subtraction) expr).getRight(),variableNameToVariable,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,predicateNameToPredicateDef);
			}
			else if(expr instanceof it.imt.qFLan.SubtractionWithPredicates){
				leftVisited = writeExpr(((it.imt.qFLan.SubtractionWithPredicates) expr).getLeft(),variableNameToVariable,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,predicateNameToPredicateDef);
				rightVisited = writeExpr(((it.imt.qFLan.SubtractionWithPredicates) expr).getRight(),variableNameToVariable,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,predicateNameToPredicateDef);
			}
			return new ArithmeticPredicateExpr(leftVisited,rightVisited,ArithmeticOperation.SUB);
		}
		else if(expr instanceof it.imt.qFLan.Multiplication || expr instanceof it.imt.qFLan.MultiplicationWithPredicates){
			if(expr instanceof it.imt.qFLan.Multiplication){
				leftVisited = writeExpr(((it.imt.qFLan.Multiplication) expr).getLeft(),variableNameToVariable,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,predicateNameToPredicateDef);
				rightVisited = writeExpr(((it.imt.qFLan.Multiplication) expr).getRight(),variableNameToVariable,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,predicateNameToPredicateDef);
			}
			else if(expr instanceof it.imt.qFLan.MultiplicationWithPredicates){
				leftVisited = writeExpr(((it.imt.qFLan.MultiplicationWithPredicates) expr).getLeft(),variableNameToVariable,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,predicateNameToPredicateDef);
				rightVisited = writeExpr(((it.imt.qFLan.MultiplicationWithPredicates) expr).getRight(),variableNameToVariable,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,predicateNameToPredicateDef);
			}
			return new ArithmeticPredicateExpr(leftVisited,rightVisited,ArithmeticOperation.MULT);
		}
		else if(expr instanceof it.imt.qFLan.MinusPrimary || expr instanceof it.imt.qFLan.MinusPrimaryWithPredicates){
			if(expr instanceof it.imt.qFLan.MinusPrimary){
				leftVisited = writeExpr(((it.imt.qFLan.MinusPrimary) expr).getLeft(),variableNameToVariable,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,predicateNameToPredicateDef);
			}
			else if(expr instanceof it.imt.qFLan.MinusPrimaryWithPredicates){
				leftVisited = writeExpr(((it.imt.qFLan.MinusPrimaryWithPredicates) expr).getLeft(),variableNameToVariable,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,predicateNameToPredicateDef);
			}
			return new ArithmeticPredicateExpr(new Constant(0),leftVisited,ArithmeticOperation.SUB);
		}
		else{
			throw new UnsupportedOperationException("Unsupported expression: " + expr.toString());
		}
	}
	private PredicateExprComparator computeComparator(String comp) {
			if(comp.equals(">")){
				return PredicateExprComparator.GE;
			}
			else if(comp.equals("<")){
				return PredicateExprComparator.LE;
			}
			else if(comp.equals(">=")){
				return PredicateExprComparator.GEQ;
			}
			else if(comp.equals("<=")){
				return PredicateExprComparator.LEQ;
			}
			else if(comp.equals("==")){
				return PredicateExprComparator.EQ;
			} 
			else if(comp.equals("!=")){
				return PredicateExprComparator.NOTEQ;
			}
			else{
				throw new UnsupportedOperationException("Unsupported comparator: " + comp);
			}
	}
	
	private SideEffect[] visitListOfSideEffects(SideEffects sideEffects,
			LinkedHashMap<String, QFLanVariable> variableNameToVariable,
			LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature,LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature,LinkedHashMap<String, IPredicateDef> predicateNameToPredicateDef
			) {
		
		if(sideEffects==null || sideEffects.getEffects()==null || sideEffects.getEffects().size()==0){
			return new SideEffect[0];
		}
		
		EList<it.imt.qFLan.SideEffect> listOfEffects = sideEffects.getEffects();
		SideEffect[] readSideEffects = new SideEffect[listOfEffects.size()];
		
		int i=0;
		for(it.imt.qFLan.SideEffect eff : listOfEffects){
			//writeExpr(Expression expr,LinkedHashMap<String, QFLanVariable> variableNameToVariable,LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature,LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature,LinkedHashMap<String, IPredicateDef> predicateNameToPredicateDef)
			IPredicateExpr expr = writeExpr(eff.getValue(), variableNameToVariable, abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature, predicateNameToPredicateDef);
			readSideEffects[i]=new SideEffect(variableNameToVariable.get(eff.getRefToQFLanVar().getVarqflan().getName()),expr);
			i++;
		}
		
		return readSideEffects;
	}
	
/*	private IPredicateExpr writeExpr(Expression expr) {
		IPredicateExpr rightVisited = null;
		IPredicateExpr leftVisited = null;
		if(expr instanceof NumberLiteral){
			return new Constant(((NumberLiteral) expr).getValue());
		}
		else if(expr instanceof RefToQFLanVariable){
			return variableNameToVariable.get(((RefToQFLanVariable) expr).getVarqflan().getName());
		}
		else if(expr instanceof Predicate){
			var f = expr.feature
			var fName="";
			if(f instanceof AbstractFeature){
				fName=f.name;
			}
			else if(f instanceof ConcreteFeature){
				fName=f.name;
			} 
			return '''new Predicate(«expr.predicate.name», «fName»)'''
		}
		else if(expr instanceof Addition || expr instanceof AdditionWithPredicates){
			if(expr instanceof Addition){
				leftVisited = writeExpr(expr.left) as String
				rightVisited = writeExpr(expr.right)  as String
			}
			else if(expr instanceof AdditionWithPredicates){
				leftVisited = writeExpr(expr.left) as String
				rightVisited = writeExpr(expr.right)  as String
			}
			return '''new ArithmeticPredicateExpr(«leftVisited»,«rightVisited»,ArithmeticOperation.SUM)'''
		}
		else if(expr instanceof Subtraction || expr instanceof SubtractionWithPredicates){
			if(expr instanceof Subtraction){
				leftVisited = writeExpr(expr.left) as String
				rightVisited = writeExpr(expr.right)  as String
			}
			else if(expr instanceof SubtractionWithPredicates){
				leftVisited = writeExpr(expr.left) as String
				rightVisited = writeExpr(expr.right)  as String
			}
			return '''new ArithmeticPredicateExpr(«leftVisited»,«rightVisited»,ArithmeticOperation.SUB)'''
		}
		else if(expr instanceof Multiplication || expr instanceof MultiplicationWithPredicates){
			if(expr instanceof Multiplication){
				leftVisited = writeExpr(expr.left) as String
				rightVisited = writeExpr(expr.right)  as String
			}
			else if(expr instanceof MultiplicationWithPredicates){
				leftVisited = writeExpr(expr.left) as String
				rightVisited = writeExpr(expr.right)  as String
			}
			return '''new ArithmeticPredicateExpr(«leftVisited»,«rightVisited»,ArithmeticOperation.MULT)'''
		}
		else if(expr instanceof MinusPrimary || expr instanceof MinusPrimaryWithPredicates){
			if(expr instanceof MinusPrimary){
				leftVisited = writeExpr(expr.left) as String
			}
			else if(expr instanceof MinusPrimaryWithPredicates){
				leftVisited = writeExpr(expr.left) as String
			}
			return '''new ArithmeticPredicateExpr(new Constant(0),«leftVisited»,ArithmeticOperation.SUB)'''
		}
		else{
			throw new UnsupportedOperationException("Unsupported expression: " + expr.toString());
		}
	}*/
	
	private Collection<IFeature> listOfFeatures(SetOfFeatures featuresSet,LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature, LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature) {
		ArrayList<IFeature> readFeatures = new ArrayList<>();
		if(featuresSet!=null){
			EList<Feature> features = featuresSet.getFeatures();
			for(it.imt.qFLan.Feature f : features){
				IFeature ifeat = getFeature(f,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature);
				readFeatures.add(ifeat);
			}
		}	
		return readFeatures;
	}
/*public IFeature getFeature(it.imt.qFLan.Feature f,LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature,LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature) {
	String name = getFeatureName(f);
	IFeature ifeat = concreteFeatureNameToConcreteFeature.get(name);
	if(ifeat==null){
		ifeat = abstractFeatureNameToAbstractFeature.get(name);
	}
	return ifeat;
}*/
	
	/*private String getFeatureName(it.imt.qFLan.Feature f) {
		if(f instanceof it.imt.qFLan.AbstractFeature){
			return ((it.imt.qFLan.AbstractFeature) f).getName();
		}
		else if(f instanceof it.imt.qFLan.ConcreteFeature){
			return ((it.imt.qFLan.ConcreteFeature) f).getName();
		}
		return null;
	}*/
	private Collection<ConcreteFeature> listOfFeatures(SetOfConcreteFeatures featuresSet,LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature) {
		ArrayList<ConcreteFeature> readFeatures = new ArrayList<>();
		if(featuresSet!=null){
			EList<it.imt.qFLan.ConcreteFeature> features = featuresSet.getFeatures();
			for(it.imt.qFLan.ConcreteFeature f : features){
				readFeatures.add(concreteFeatureNameToConcreteFeature.get(f.getName()));
			}
		}
		
		return readFeatures;
	}
	
	private void populateActions(EList<Action> actions, LinkedHashMap<String, NormalAction> actionNameToAction,
			QFlanModel model) throws Z3Exception {
		for(Action action : actions){
			NormalAction na = new NormalAction(action.getName());
			model.addNormalAction(na);
			actionNameToAction.put(action.getName(), na);
		}
		
	}
	private void populateFeaturePredicates(EList<FeaturePredicate> featurePredicates,
			LinkedHashMap<String, IPredicateDef> predicateNameToPredicateDef,QFlanModel model, LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature) {
		MathEval math = new MathEval();
		for(FeaturePredicate element : featurePredicates){
			String predicateName = element.getName();
			IPredicateDef p = new PredicateDef(predicateName);
			predicateNameToPredicateDef.put(predicateName, p);
			model.addPredicateDef(p);
			for(FeaturePredicateValue featureAndValue : element.getValues()){
				p.setValue(concreteFeatureNameToConcreteFeature.get(featureAndValue.getFeature().getName()), math.evaluate(MyParserUtil.visitExpr(featureAndValue.getValue())));
			}
		}
		
		//IPredicateDef where = new PredicateDef("where");
		//model.addPredicateDef(where);
		//where.setValue(Parking,0.0);
		
	}
	private void populateFeatures(EList<it.imt.qFLan.AbstractFeature> abstractFeatures,
			EList<it.imt.qFLan.ConcreteFeature> concreteFeatures, EList<FatherAndSonsRelations> relationsAmongFeatures,
			QFlanModel model, MessageConsoleStream out,
			LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature,
			LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature) throws Z3Exception {
		for (it.imt.qFLan.AbstractFeature af : abstractFeatures) {
			AbstractFeature a = new AbstractFeature(af.getName());
			abstractFeatureNameToAbstractFeature.put(a.getName(), a);
			model.addAbstractFeatureDefinition(a);
		}
		for (it.imt.qFLan.ConcreteFeature cf : concreteFeatures) {
			ConcreteFeature c = new ConcreteFeature(cf.getName());
			concreteFeatureNameToConcreteFeature.put(c.getName(), c);
			model.addConcreteFeatureDefinition(c);
		}
		ArrayList<IConstraint> hierarchicalConstraints = new ArrayList<IConstraint>();
		for(FatherAndSonsRelations relation : relationsAmongFeatures){
			if(relation instanceof FatherAndSons){
				visitNormalRelationAmongFeatures((FatherAndSons)relation,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,hierarchicalConstraints,model);
			}
			else if(relation instanceof ORFatherAndSons){
				//OR relation
				visitORRelationAmongFeatures((ORFatherAndSons)relation,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,hierarchicalConstraints,model);
			} else if(relation instanceof XORFatherAndSons){
				//XOR relation
				visitXORRelationAmongFeatures((XORFatherAndSons)relation,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,hierarchicalConstraints,model);
			} 
		}
		
		model.computeDescendantsAndAncestors();
		
		for (IConstraint iConstraint : hierarchicalConstraints) {
			model.addConstraint(iConstraint);
		}
	}

	private void visitNormalRelationAmongFeatures(FatherAndSons relation,
			LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature,
			LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature,
			ArrayList<IConstraint> hierarchicalConstraints, QFlanModel model) {
		String father = relation.getFather().getName();
		for(PossiblyOptionalFeature possiblyson : relation.getSonsSet().getFeatures()){
			Feature son = possiblyson.getMandatoryFeature();
			boolean mandatory=true;
			if(son==null){
				son=possiblyson.getOptionalFeature();
				mandatory=false;
			}
			IFeature sonFeature = getFeature(son,abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature);
			abstractFeatureNameToAbstractFeature.get(father).addSon(sonFeature);
			if(mandatory){
				sonFeature.setOptional(false);
				try {
					hierarchicalConstraints.add(new HasFeature(sonFeature, model,true));
				} catch (Z3Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void visitORRelationAmongFeatures(ORFatherAndSons relation,
			LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature,
			LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature,
			ArrayList<IConstraint> hierarchicalConstraints, QFlanModel model) {
		String fatherName = relation.getFather().getName();
		AbstractFeature father = abstractFeatureNameToAbstractFeature.get(fatherName);
		SetOfFeatures sonsSet = relation.getSonsSet();
		visitOR_XORRealationAmongFeatures(father,sonsSet,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,hierarchicalConstraints,model,Alternative_ORCondition.OR);
	}
	private void visitXORRelationAmongFeatures(XORFatherAndSons relation,
			LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature,
			LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature,
			ArrayList<IConstraint> hierarchicalConstraints, QFlanModel model) {
		String fatherName = relation.getFather().getName();
		AbstractFeature father = abstractFeatureNameToAbstractFeature.get(fatherName);
		SetOfFeatures sonsSet = relation.getSonsSet();
		visitOR_XORRealationAmongFeatures(father,sonsSet,abstractFeatureNameToAbstractFeature,concreteFeatureNameToConcreteFeature,hierarchicalConstraints,model,Alternative_ORCondition.XOR);
	}
	
	private void visitOR_XORRealationAmongFeatures(AbstractFeature father, SetOfFeatures sonsSet,
			LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature,
			LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature,
			ArrayList<IConstraint> hierarchicalConstraints, QFlanModel model, Alternative_ORCondition cond) {
		for(Feature son : sonsSet.getFeatures()){
			IFeature sonFeature = getFeature(son,abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature);
			father.addSon(sonFeature);
		}
		Collection<IFeature> features = listOfFeatures(sonsSet, concreteFeatureNameToConcreteFeature,abstractFeatureNameToAbstractFeature);
		try {
			hierarchicalConstraints.add(new Alternative_OrConstraint(father,features, cond,model));
		} catch (Z3Exception e) {
			e.printStackTrace();
		}
	}
	public IFeature getFeature(Feature feature,LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature,
			LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature) {
		IFeature sonFeature = null;
		if(feature instanceof it.imt.qFLan.AbstractFeature){
			String sonName = ((it.imt.qFLan.AbstractFeature) feature).getName();
			sonFeature=abstractFeatureNameToAbstractFeature.get(sonName);
		}
		else if(feature instanceof it.imt.qFLan.ConcreteFeature){
			String sonName = ((it.imt.qFLan.ConcreteFeature) feature).getName();
			sonFeature=concreteFeatureNameToConcreteFeature.get(sonName);
		}
		return sonFeature;
	}
	
	//private LinkedHashMap<String, QFLanVariable> populateVariables(EList<it.imt.qFLan.QFLanVariable> eList, QFlanModel model,MessageConsoleStream out, LinkedHashMap<String, QFLanVariable> variableNameToVariable) {
	private LinkedHashMap<String, QFLanVariable> populateVariables(EList<it.imt.qFLan.QFLanVariable> eList, QFlanModel model,MessageConsoleStream out, LinkedHashMap<String, QFLanVariable> variableNameToVariable,LinkedHashMap<String, AbstractFeature> abstractFeatureNameToAbstractFeature,LinkedHashMap<String, ConcreteFeature> concreteFeatureNameToConcreteFeature,LinkedHashMap<String, IPredicateDef> predicateNameToPredicateDef) {
		//LinkedHashMap<String, QFLanVariable> variableNameToVariable = new LinkedHashMap<>(eList.size());
		for (it.imt.qFLan.QFLanVariable qfLanVariable : eList) {
			try {
				IPredicateExpr expr = writeExpr(qfLanVariable.getValue(), variableNameToVariable, abstractFeatureNameToAbstractFeature, concreteFeatureNameToConcreteFeature, predicateNameToPredicateDef);
				//QFLanVariable v = model.addVariable(qfLanVariable.getName(), MyParserUtil.visitExpr(qfLanVariable.getValue()));
				QFLanVariable v = model.addVariable(qfLanVariable.getName(), expr);
				variableNameToVariable.put(v.getName(), v);
			} catch (Z3Exception e) {
				NewVesta.printStackTrace(out, e);
			}
		}
		return variableNameToVariable;
	}
//	public static InfoMultiQuery invokeMultiVeStA(String modelFullPath, String query, String alpha, String delta, int parallelism, int ir, String jarPath,String outputPath, int blockSize, MessageConsoleStream out, String logFile){
//		return invokeMultiVeStA(modelFullPath,  query,  alpha,  delta,  parallelism,  ir,  jarPath, outputPath, blockSize,  out,  null, logFile);
//	}
	public static InfoMultiQuery invokeMultiVeStA(String modelFullPath, String query, String alpha, String delta, int parallelism, int ir, String jarPath,String outputPath, int blockSize, MessageConsoleStream out, QFlanModel model,String logFile){
		/*String query = "/MultiQuaTEx/FeaturesInstalledAndPriceWeightLoadAtStep.quatex";
		String alpha = "0.1";
		String delta = "[20.0,1.0,5.0,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1]";*/
		
		if(logFile==null) {
			logFile="";
		}
		else {
			initCSVLog(logFile,model);
		}
		
		InfoMultiQuery result = vesta.NewVesta.invokeClient(new String[]{
				"-sm", "false", "-sd", "it.imt.qflan.core.multivesta.QFlanJavaState", "-jn", jarPath, "-vp", "false", 
				"-osws", "ONESTEP", /*"-sots", "12343",*/
				"-bs", String.valueOf(blockSize),
				"-m", modelFullPath, 
				"-l", String.valueOf(parallelism), 
				"-ir", String.valueOf(ir), 
				"-op" , outputPath,
				"-f", query,
				"-a", alpha, 
				"-ds", delta,
				"-nb", "128",
				"-ibs", "20",
				"-wm","2",
				"-o","-logFile "+logFile
		}
		, out, 
		(model==null)?null:new QFlanJavaState(model));
		
		return result;
	}
	
	private static ArrayList<String> initCSVLog(String logFile,QFlanModel model) {
		ArrayList<String> caption = QFlanJavaState.logCaption(model);
		
		
		
//		String formulaRead = NewVesta.readFormulaFile(query);
//		try {
//			IMultiQuaTExQuery mqQuery = MultiQuaTExParser.parseString(formulaRead);
//			InfoMultiQuery mq = new InfoMultiQuery();
//			mqQuery.populateInfoMultiQuery(mq);
//			for(int l=0; l<mq.getNumberOfLabels();l++) {
//				caption.add(mq.getLabel(l));
//			}
//		} catch (ParseException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		try {
		BufferedWriter writer = new BufferedWriter(new FileWriter(logFile,false));
		
		for(int i=0;i<caption.size();i++) {
			writer.write(caption.get(i));
			if(i<caption.size()-1) {
				writer.write(",");
			}
		}
		writer.write("\n");
		writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return caption;
	}
	
	public static void performASimulation(int steps,String fileOut, QFlanModel model,String modelFullPath, int seed, MessageConsoleStream out, String jarPath_param,String outputPath) throws IOException {
		/*vesta.NewVesta.invokeClient(new String[]{
				"-sm", "false", "-sd", "it.imt.qflan.core.multivesta.QFlanJavaState", "-jn", jarPath, "-vp", "false", 
				"-osws", "ONESTEP", "-sots", String.valueOf(seed),
				"-m", modelFullPath,
				"-l", "1",
				"-op" , outputPath,
				"-f", "simulation"
		}
		, out);*/
		
		
		BufferedWriter bwOut = new BufferedWriter(new FileWriter(fileOut));
		
 		QFlanJavaState qjs = new QFlanJavaState(model);
		//I have to perform the simulation
		//ParametersForState pfs = new ParametersForState(modelFullPath, /*"-cp "+*/jarPath_param);
		//ParametersForState pfs = new ParametersForState("elevator", "");
		//QFlanJavaState qjs = new QFlanJavaState(pfs);
		
		int step=1;
		qjs.setLastStateAlreadyComputed(false);
		qjs.setNumberOfSteps(0);
		qjs.setSimulatorForNewSimulation(seed);
		
		String message="Complete initial system specification:";
		NewVesta.println(out, message);
		printlnInBWOut(bwOut,message);
		message =  qjs.printModel();//qjs.currentStatus();
		NewVesta.println(out, message);
		printlnInBWOut(bwOut,message);
		
		message = qjs.computeSATMessage();
		NewVesta.println(out, message);
		printlnInBWOut(bwOut,message);
		
		NewVesta.println(out, "\n\n");
		printlnInBWOut(bwOut,"\n\n");
		
		boolean completed=false;
		while(!completed && step<=steps){
			message = "###################################\nSTEP="+step+"\n###################################\n";
			NewVesta.println(out, message);
			printlnInBWOut(bwOut,message);
			//qjs.performOneStepOfSimulation();
			ArrayList<ICommitment> forbiddenCommittments=new ArrayList<ICommitment>();
			double totalRate = qjs.computeAllowedCommittments(forbiddenCommittments);

			int allowedNextStates = qjs.getNumberOfAllowedCommitments();
			if(allowedNextStates>0){
				int last=allowedNextStates-1;
				DoubleAndCommitment[] allCommitments = qjs.getComputedCommitments();
				
				if(allowedNextStates==0){
					message = allowedNextStates+" allowed actions at step "+step+ ".";
					NewVesta.println(out, message);
					printlnInBWOut(bwOut,message);
				}
				else{
					message = allowedNextStates+" allowed actions at step "+step+ ": ";
					if(allowedNextStates==1){
						message="1 allowed action at step "+step+ ": ";
					}
					NewVesta.println(out, message);
					printlnInBWOut(bwOut,message);
					for(int i=0;i<=last;i++){
						//NewVesta.println(out, "  "+i+")");
						message="  "+(i+1)+")  "+allCommitments[i].getCommitment();
						NewVesta.println(out, message);
						printlnInBWOut(bwOut,message);
					}
				}
				NewVesta.println(out, "");
				printlnInBWOut(bwOut,"");
				if(forbiddenCommittments.isEmpty()){
					message= "No forbidden actions at step "+step;
					NewVesta.println(out, message);
					printlnInBWOut(bwOut,message);
				}
				else{
					if(forbiddenCommittments.size()==1){
						message= "1 forbidden action at step "+step;
					}
					else{
						message= forbiddenCommittments.size() +" forbidden actions at step "+step;
					}
					NewVesta.println(out, message);
					printlnInBWOut(bwOut,message);
					int i=0;
					for (ICommitment commitment : forbiddenCommittments) {
						message="  "+(i+1)+")  "+commitment;
						NewVesta.println(out, message);
						printlnInBWOut(bwOut,message);
						i++;
					}
				}

				//String line = console. .readLine();
				ICommitment chosenCommitment = qjs.chooseCommitment(totalRate);
				message="\nChosen:\n  "+chosenCommitment;
				NewVesta.println(out, message);
				printlnInBWOut(bwOut,message);

				qjs.applyChosenCommitment(chosenCommitment);
				message="\nObtained state:";
				NewVesta.println(out, message);
				printlnInBWOut(bwOut,message);
				
				message=qjs.currentStatus();
				NewVesta.println(out, message);
				printlnInBWOut(bwOut,message);
				
				message = qjs.computeSATMessage();
				NewVesta.println(out, message);
				printlnInBWOut(bwOut,message);
				
				message="\n\n";
				NewVesta.println(out, message);
				printlnInBWOut(bwOut,message);

				step++;
			}
			else{
				completed=true;
				
				message = "No action can be executed at "+step+ ".\n";
				NewVesta.println(out, message);
				printlnInBWOut(bwOut,message);
				
				message = "Simulation completed.\n";
				NewVesta.println(out, message);
				printlnInBWOut(bwOut,message);
			}
			
			/*NewVesta.println(out,"Sleeping for 4 seconds...\n\n");
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}

		message="Total ms to compute commitments:"+QFlanJavaState.TOTALCOMPUTATIONCOMMITMENTS;
		NewVesta.println(out, message);
		printlnInBWOut(bwOut,message);
		//avg+=QFlanJavaState.TOTALCOMPUTATIONCOMMITMENTS;
		message="Total ms to choose commitments:"+QFlanJavaState.TOTALCHOICEOFCOMMITMENT;
		NewVesta.println(out, message);
		printlnInBWOut(bwOut,message);
		message="Total ms to apply commitments:"+QFlanJavaState.TOTALAPPLICATIONOFCOMMITMENT;
		NewVesta.println(out, message);
		printlnInBWOut(bwOut,message);
		
		bwOut.close();
	}
	
	
	private static void printlnInBWOut(BufferedWriter bwOut, String message) {
		printInBWOut(bwOut, message+"\n");
	}
	private static void printInBWOut(BufferedWriter bwOut, String message) {
		if(bwOut!=null){
			try {
				bwOut.write(message);
				bwOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	public static void visualizePlot(IDataOutputHandler dog,InfoMultiQuery result, String fileAbsPath, String query, String alpha, String delta, MessageConsoleStream out){
		String fileAbsPathToShow=fileAbsPath;
		int lastSep = fileAbsPathToShow.lastIndexOf(File.separator);
		if(lastSep>0){
			fileAbsPathToShow=fileAbsPathToShow.substring(lastSep+1);
		}
		String command = "MultiVeStA analysis of " + fileAbsPathToShow;
		String queryToShow=query;
		lastSep = queryToShow.lastIndexOf(File.separator);
		if(lastSep>0){
			queryToShow=queryToShow.substring(lastSep+1);
		}
		String minimalDescription = "SMC of "+queryToShow;//
		boolean showLabels=true;
		boolean visualizePlot=true;

		//dog = new DataOutputHandler(minimalDescription,crn, result,alpha,delta);
		//IDataOutputHandler dog = new GUIDataOutputHandler(null);
		dog.setData(/*fileAbsPathToShow+" - "+*/minimalDescription, result,alpha,delta,command);

		dog.setShowLabels(showLabels);

		if(visualizePlot){
			if(result.getNumberOfX()<=3){
				NewVesta.println(out,"The used graphical library does not allow to draw lines basing on less than four points.");
			}                                 
			else{
				dog.showPlots(false,null);
			}
		}
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
    		//link= "https://dl.dropboxusercontent.com/u/18840437/qflan/multivestaQFLan.jar?dl=1";
    		//link = "https://www.dropbox.com/s/kodox600mqyg7pv/multivestaQFLan.jar?dl=1";
    		link = "https://www.dropbox.com/s/yj46t7c9urq8n2q/multivestaQFLan.jar?dl=1";
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
    		new MessageDialogShower(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()).openMissingQFLanCoreLibraryDialog(link, linkShort /*, paths, OS*/);
    	}
    	else{
    		librariesPresent=true;
    	}
    }

	public static String getJarPath() {
		return jarPath;
	}
	
	/*private void replaceText(ModelElementsCollector mec, ICRN crn) throws BadLocationException {
		
		XtextEditor xtextEditor = EditorUtils.getActiveXtextEditor();
		IXtextDocument xtextDocument = xtextEditor.getDocument();
		
		ICompositeNode importNode = NodeModelUtils.findActualNodeFor(mec.getImportCommand());
		ITextRegion importTextRegionWithoutComments = importNode.getTextRegion();
		
		String textToReplace = xtextDocument.get(importTextRegionWithoutComments.getOffset(), importTextRegionWithoutComments.getLength());
		
		String crnString = GUICRNImporter.buildXtextCRNString(crn,crn.getMdelDefKind());
		textToReplace = textToReplace.replace("\n", "\n//");
		xtextDocument.replace(importTextRegionWithoutComments.getOffset(), importTextRegionWithoutComments.getLength(), "//"+textToReplace+"\n"+crnString);
	}*/



	/*	private String buildXtextCRNString(ICRN crn) {
		StringBuilder sb = new StringBuilder();
		if(crn!=null){
			sb.append(" begin parameters\n");
			for (String param : crn.getParameters()) {
				int space = param.indexOf(' ');
				String parName=param.substring(0,space);
				String parExpr = param.substring(space+1);
				sb.append("  ");
				sb.append(parName.trim());
				sb.append(" = ");
				sb.append(parExpr.trim());
				sb.append(";\n");
			}
			sb.append(" end parameters\n");

			sb.append(" begin CRN\n");
			sb.append("  begin species\n");
			for (ISpecies species : crn.getSpecies()) {
				sb.append("   ");
				sb.append(species.getName());
				sb.append(" = ");
				sb.append(species.getInitialConcentrationExpr());
				sb.append("\n");
			}
			sb.append("  end species\n");
			sb.append("  begin reactions\n");
			for (ICRNReaction reaction : crn.getReactions()) {
				sb.append("   ");
				sb.append(reaction.getReagents().toMultiSet());
				sb.append(" -> ");
				sb.append(reaction.getProducts().toMultiSet());
				sb.append(" , ");
				if(reaction.hasArbitraryKinetics()){
					sb.append("arbitrary ");
				}
				sb.append(reaction.getRateExpression());
				sb.append(";\n");
			}
			sb.append("  end reactions\n");
			sb.append(" end CRN\n");
			sb.append(" begin views\n");
			for(int i=0; i<crn.getViewNames().length;i++){
				sb.append("  ");
				sb.append(crn.getViewNames()[i]);
				sb.append(" = ");
				sb.append(crn.getViewExpressions()[i]);
				sb.append(";\n");
			}
			sb.append(" end views");
		}
		else{
			sb.append("//UNDEFINED CRN");
		}
		return sb.toString();
	}*/

	/*private String getCommandText(IXtextDocument xtextDocument,FindReplaceDocumentAdapter frda, String importName) throws BadLocationException {
		IRegion start = frda.find(0,importName+"({",true,true,false,false);
		IRegion end = frda.find(start.getOffset()+start.getLength(),"})",true,true,false,false);
		String commandInEditor=xtextDocument.get(start.getOffset(), end.getOffset()+start.getLength()-start.getOffset());
		return commandInEditor;
	}*/

	/*public static String getText(FileIn par){
		return "fileIn=>"+par.getFileIn();
	}*/


	//public void read(EList<Parameter> parametersList, EList<Species> speciesList, EList<Reaction> reactionsList, EList<ODE> odes, EList<View> viewsList, EList<InitialConcentration> icList, Iterable<Command> commandsList) {
	//public void read(Iterable<ParametersList> parametersListIt, Iterable<SpeciesList> speciesListIt, Iterable<ReactionsList> reactionsListIt, Iterable<ODEsList> odesIt, Iterable<ViewsList> viewsListIt, Iterable<ICList> icListIt, Iterable<Command> commandsList) {
	/*public void read(Iterable<ParametersList> parametersListIt, Iterable<SpeciesList> speciesListIt, Iterable<ReactionsList> reactionsListIt, Iterable<ODEsList> odesIt, Iterable<ViewsList> viewsListIt, Iterable<ICList> icListIt, Iterable<Command> commandsList) {

		//ArrayList<ArrayList<String>> parameters = new ArrayList<ArrayList<String>>(parametersList.size());
		ArrayList<ArrayList<String>> parameters = new ArrayList<ArrayList<String>>(0);
		if(parametersListIt!=null && parametersListIt.iterator().hasNext()){
			EList<Parameter> parametersList = parametersListIt.iterator().next().getParameters();
			parameters = new ArrayList<ArrayList<String>>(parametersList.size());
			for (Parameter param : parametersList) {
				//name,value
				ArrayList<String> par = new ArrayList<>(2);
				parameters.add(par);
				par.add(param.getName());
				String val = MyArithmeticExpressionUtil.visitExpr(param.getParamValue().getValue());
				par.add(val);
			}
		}

		ArrayList<ArrayList<String>> reactions = new ArrayList<ArrayList<String>>(0);
		if(reactionsListIt!=null && reactionsListIt.iterator().hasNext()){
			EList<Reaction> reactionsList = reactionsListIt.iterator().next().getAllReactions();
			reactions = new ArrayList<ArrayList<String>>(reactionsList.size());
			for(Reaction reac : reactionsList){
				//reagent,product,rate
				ArrayList<String> r = new ArrayList<>(3);
				reactions.add(r);
				//reac.getReagents()
				r.add(reac.getReagents().getSpeciesOfComposite().get(0).getSpecies().getName());
				r.add(reac.getProducts().getSpeciesOfComposite().get(0).getSpecies().getName());
				//r.add(reac.getRate().getValue().toString());
				//reactions.put(reac.getReagents().getSpeciesOfComposite().get(0).getSpecies().getName(), value)
				String val = MyArithmeticExpressionUtil.visitExpr(reac.getRate().getValue().getValue());//TODO: works only for mass action reactions
				r.add(val);
			}
		}
		else {
			//either reactions or odes must be defined
			EList<ODE> odes = odesIt.iterator().next().getOdes();
			reactions = new ArrayList<ArrayList<String>>(odes.size());
			int i=0;
			for(ODE ode : odes){
				Species speciesOfOde = ode.getName().getAllSpecies().get(i);
				ArithmeticExpressionWithSpecies drift = ode.getDrift();
				String driftString = MyArithmeticExpressionUtil.visitExpr(drift.getValue());
				i++;
				//IComposite products = new Composite(speciesOfODE,speciesOfODE);
				//ICRNReaction reaction = new CRNReactionArbitrary((IComposite)speciesOfODE, products, body,varsName);
				//speciesOfTheODE,arbitrary,rate
				ArrayList<String> r = new ArrayList<>(3);
				reactions.add(r);
				r.add(speciesOfOde.getName());//r.add(2*speciesOfOde.getName());
				r.add("arbitrary");
				r.add(driftString);
			}
		}

		//ArrayList<ArrayList<String>> views = new ArrayList<ArrayList<String>>(viewsList.size());
		ArrayList<ArrayList<String>> views = new ArrayList<ArrayList<String>>(0);
		if(viewsListIt!=null && viewsListIt.iterator().hasNext()){
			EList<View> viewsList = viewsListIt.iterator().next().getAllViews();
			views = new ArrayList<ArrayList<String>>(viewsList.size());
			for (View v : viewsList) {
				//name,value
				ArrayList<String> view = new ArrayList<>(2);
				views.add(view);
				view.add(v.getName());
				String val = MyArithmeticExpressionUtil.visitExpr(v.getExpr().getValue());
				view.add(val);
			}
		}

		//ArrayList<ArrayList<String>> initialConcentrations = new ArrayList<ArrayList<String>>(icList.size());
		ArrayList<ArrayList<String>> initialConcentrations = new ArrayList<ArrayList<String>>(0);
		if(icListIt!=null && icListIt.iterator().hasNext()){
			EList<InitialConcentration> icList = icListIt.iterator().next().getAllIC();
			initialConcentrations = new ArrayList<ArrayList<String>>(icList.size());
			for (InitialConcentration ic : icList) {
				//name,value
				ArrayList<String> initialConcentration = new ArrayList<>(2);
				initialConcentrations.add(initialConcentration);
				initialConcentration.add(ic.getName().getName());
				String val = MyArithmeticExpressionUtil.visitExpr(ic.getIc().getValue());
				initialConcentration.add(val);
			}
		}

		CRNImporter crnImporter = new CRNImporter();
		try {
			crnImporter.importCRNNetwork(true, true, true, parameters, reactions,views,initialConcentrations);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(-1);
		}

		System.out.println();
		List<String> commands = new ArrayList<String>();
		for(Command command : commandsList){
			commands.add(command.toString());
			commands.add("newline");
		}
		CommandsReader commandsReader = new CommandsReader(commands);
		CRNReducerCommandLine cl = new CRNReducerCommandLine(commandsReader,crnImporter.getCRN(),crnImporter.getInitialPartition());
		try {
			cl.executeCommands(true);
		} catch (Exception e) {
			System.out.println("Unhandled errors arised while executing the commands. I terminate.");
			e.printStackTrace();
		}
	}*/



	/*public void read(Iterable<EObject> contents) {
		EList<Parameter> parametersList = contents.
				(typeOf ParametersList).get(0).parameters;
	}*/



}
