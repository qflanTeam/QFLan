package it.imt.qflan.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Symbol;
import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.features.AbstractFeature;
import it.imt.qflan.core.features.ConcreteFeature;
import it.imt.qflan.core.features.interfaces.IFeature;
import it.imt.qflan.core.predicates.interfaces.IPredicateDef;
import it.imt.qflan.core.predicates.interfaces.IPredicateExpr;
import it.imt.qflan.core.processes.Choice;
import it.imt.qflan.core.processes.Parallel;
import it.imt.qflan.core.processes.ProcessDefinition;
import it.imt.qflan.core.processes.ZeroProcess;
import it.imt.qflan.core.processes.actions.AskAction;
import it.imt.qflan.core.processes.actions.InstallAction;
import it.imt.qflan.core.processes.actions.NormalAction;
import it.imt.qflan.core.processes.actions.ReplaceAction;
import it.imt.qflan.core.processes.constraints.ActionRequiresConstraint;
import it.imt.qflan.core.processes.constraints.HasFeature;
import it.imt.qflan.core.processes.interfaces.IAction;
import it.imt.qflan.core.processes.interfaces.ICommitment;
import it.imt.qflan.core.processes.interfaces.IConstraint;
import it.imt.qflan.core.processes.interfaces.IProcess;
/*import it.imt.qflan.core.variables.MathEval;*/
import it.imt.qflan.core.variables.QFLanVariable;
import it.imt.qflan.core.variables.SideEffect;

public class QFlanModel {
	
	public static final boolean IGNOREZ3=true;
	private static final boolean USEJUSTCOSTRAINTSOFTHEACTION = false;//Don't change this. It must be  false
	//This is to check if the use of local constraints only is correct. 
	private static final boolean CHECKSATWITHLOCALANDGLOBALCONSTRAINTS = false;
	
	private IProcess currentProcess;
	//1private HashMap<ProcessDefinition,IProcess> processDefinitions;
	private Collection<AbstractFeature> abstractFeaturesDefs;
	private Collection<ConcreteFeature> concreteFeaturesDefs;
	private HashMap<String,IFeature> featuresDefs;
	private Collection<IPredicateDef> predicatesDefs;
	private HashMap<String,IPredicateDef> nameToPredicatesDefs;
	//private Collection<IPredicateExpr> predicates;
	private LinkedHashSet<ConcreteFeature> installedFeatures;
	private Set<NormalAction> actions;
	
	private LinkedHashMap<String,QFLanVariable> variables;
	//private MathEval math;
	
	//All constraints
	private Set<IConstraint> constraints;
	//private Set<ActionRequiresConstraint> actionConstraints;
	//constraints of do(install(f)) -> c
	private HashMap<ConcreteFeature, Set<IConstraint>> installActionConstraints;
	//constraints of do(uninstall(f)) -> c
	private HashMap<ConcreteFeature, Set<IConstraint>> uninstallActionConstraints;
	//constraints of do(replace(f1,f2)) -> c
	private HashMap<PairOfConcreteFeature, Set<IConstraint>> replaceActionConstraints;
	//constraints of do(feature) -> c, and do(action) -> c
	private HashMap<ConcreteFeature, Set<IConstraint>> concreteFeatureActionConstraints;
	private HashMap<NormalAction, Set<IConstraint>> normalActionConstraints;
	
	//Quantitative constraints (e.g., price of bike < 500)
	//private Set<IConstraint> quantitativeConstraints;
	//Action constraints (e.g., ( do(sell) -> ((p('price,Bike) > 250.0))) )
	//private Set<IConstraint> actionConstraints;
	//Feature costraints from the feature diagram
	//private Set<IConstraint> featuresConstraints;
	
	private DoubleAndCommitment[] commitments;
	private double cumulativeRate;
	private int actualNumberOfCommitments;
	private HashMap<IAction, ArithExpr> actionAndFeatureToZ3;
	private HashMap<IAction, ArithExpr> actionAndFeatureToZ3Val;
	/*private HashMap<QFLanVariable, ArithExpr> varToZ3;
	private HashMap<QFLanVariable, ArithExpr> varToZ3Val;*/
	private String lastPerformedActionName=null;
	
	public QFlanModel() throws Z3Exception {
		super();
		//System.out.println("constructor");
		//this.processDefinitions = new HashMap<>();
		this.abstractFeaturesDefs = new LinkedHashSet<>();
		this.concreteFeaturesDefs = new LinkedHashSet<>();
		//this.featuresDefs = new LinkedHashSet<>();
		this.featuresDefs = new LinkedHashMap<>();
		//this.actionConstraints=new HashSet<>();
		this.installActionConstraints=new HashMap<>();
		this.uninstallActionConstraints=new HashMap<>();
		this.replaceActionConstraints=new HashMap<>();
		this.concreteFeatureActionConstraints=new HashMap<>();
		this.normalActionConstraints=new HashMap<>();
		this.constraints=new HashSet<>();
		//this.installedFeatures=new LinkedHashSet<>();
		this.predicatesDefs = new LinkedHashSet<>();
		this.nameToPredicatesDefs=new HashMap<>();
		//this.predicates = new ArrayList<>();
		this.actions=new HashSet<>();
		
		this.variables=new LinkedHashMap<>();
		//this.math=new MathEval();
		
		if(!IGNOREZ3){
			initZ3DataStructures();
		}
	}
	
	public Collection<ConcreteFeature> getConcreteFeaturesDefs() {
		return concreteFeaturesDefs;
	}
	public Collection<AbstractFeature> getAbstractFeaturesDefs() {
		return abstractFeaturesDefs;
	}
	public LinkedHashMap<String, QFLanVariable> getVariables() {
		return variables;
	}

	public void setInitialState(Collection<ConcreteFeature> installedFeatures, ProcessDefinition process ){
		this.initialProcess = process;
		if(intialInstalledFeatures==null){
			intialInstalledFeatures=new HashSet<>(installedFeatures.size());
		}
		for (ConcreteFeature concreteFeature : installedFeatures) {
			intialInstalledFeatures.add(concreteFeature);
		}
	}
	
	public void resetToInitialState() throws Z3Exception{
		setProcess(initialProcess);
		if(!IGNOREZ3){
			initInstalledFeaturesForZ3();
		}
		
		if(installedFeatures!=null){
			for (ConcreteFeature installedFeature : installedFeatures) {
				uninstall(installedFeature,false);
			}
		}
		
		installedFeatures=new LinkedHashSet<>();
		for (ConcreteFeature feature : intialInstalledFeatures) {
			install(feature);
		}
		commitments=null;
		//allowedCommitments=null;
		cumulativeRate=-1;
		actualNumberOfCommitments=-1;
		
		for (Entry<String, QFLanVariable> v : variables.entrySet()) {
			updateVariableValue(v.getValue(), v.getValue().getInitialValue());
		}
		
		lastPerformedActionName="reset";
	}
	
	public boolean isInstalled(IFeature feature){
		return feature.isInstallled();
		/*if(feature instanceof ConcreteFeature){
			return installedFeatures.contains(feature);
		}
		else{
			AbstractFeature af = (AbstractFeature)feature;
			for (ConcreteFeature f : af.computeOrGetConcreteDescendants()) {
				if(isInstalled(f)){
					return true;
				}
			}
			//None of its descendants is installled 
			return false;
		}*/
		
	}
	
	public QFLanVariable addVariable(String name, IPredicateExpr expr)  throws Z3Exception{
		double varValue = expr.eval(null);//computeExpressionValue(expr);
		QFLanVariable var = new QFLanVariable(name, varValue);
		variables.put(name,var);
		if(!IGNOREZ3){
			addVariableToZ3(var);
		}
		updateVariableValue(var, varValue);
		return var;
		/*
		if(variables.contains(var)){
			return;
		}
		else{
			double varValue = computeVariableValue(var);
			variables.add(var);
			var.setValue(varValue);
			//math.setVariable(var.getName(), varValue);
			
			if(!IGNOREZ3){
				addVariableToZ3(var,varValue);
			}
		}*/
		
	}
	
	public QFLanVariable getVariable(String name){
		return variables.get(name);
	}

	/*private double computeExpressionValue(String valueExpr) {
		double value;
		try  
		{  
			value = Double.valueOf(valueExpr);  
		}  
		catch(NumberFormatException nfe)  
		{  
			value = math.evaluate(valueExpr);  
		}  		
		return value;
	}*/
	
	private void updateVariableValue(QFLanVariable var, double value){
		var.setValue(value);//TODO: update in z3...
		//math.setVariable(var.getName(), value);
		
	}

	public void addAbstractFeatureDefinition(AbstractFeature feature) throws Z3Exception{
		featuresDefs.put(feature.getName(), feature);
		abstractFeaturesDefs.add(feature);
		if(!IGNOREZ3){
			addActionOrFeatureToZ3(feature);
		}
	}
	
	public void addConcreteFeatureDefinition(ConcreteFeature feature) throws Z3Exception{
		featuresDefs.put(feature.getName(), feature);
		concreteFeaturesDefs.add(feature);
		if(!IGNOREZ3){
			addActionOrFeatureToZ3(feature);
		}
	}
	
	public void addConstraint(IConstraint constraint){
		constraints.add(constraint);
		//constraint.addConstraintToCheck(constraint);
		
		
//		if(constraint instanceof DisequationOfPredicateExpressions){
//			qs.add(constraint);AAAA
//		}
	}
	
	/*public void addHierarchicalConstraint(){
		
	}
	
	public void addCrossTreeConstraint(){
		
	}
	
	public void addQuunatitativeConstraint(){
		
	}*/
	
	public void addActionConstraint(ActionRequiresConstraint actionRequiresConstraint) {
		Set<IConstraint> constraints=null;
		IAction a = actionRequiresConstraint.getAction();
		if(a instanceof InstallAction){
			if(((InstallAction) a).getIsInstall()){
				constraints = installActionConstraints.get(((InstallAction) a).getFeature());
				if(constraints==null){
					constraints=new LinkedHashSet<IConstraint>();
					installActionConstraints.put(((InstallAction) a).getFeature(), constraints);
				}
			}
			else{
				constraints=uninstallActionConstraints.get(((InstallAction) a).getFeature());
				if(constraints==null){
					constraints=new LinkedHashSet<IConstraint>();
					uninstallActionConstraints.put(((InstallAction) a).getFeature(), constraints);
				}
			}
		}
		else if(a instanceof ReplaceAction){
			constraints=replaceActionConstraints.get(new PairOfConcreteFeature(((ReplaceAction) a).getToRemove(), ((ReplaceAction) a).getToAdd()));
			if(constraints==null){
				constraints=new LinkedHashSet<IConstraint>();
				replaceActionConstraints.put(new PairOfConcreteFeature(((ReplaceAction) a).getToRemove(), ((ReplaceAction) a).getToAdd()),constraints);
			}
		} 
		else if(a instanceof ConcreteFeature){
			constraints=concreteFeatureActionConstraints.get((ConcreteFeature)a);
			if(constraints==null){
				constraints=new LinkedHashSet<IConstraint>();
				concreteFeatureActionConstraints.put((ConcreteFeature)a, constraints);
			}
		}
		else if(a instanceof NormalAction){
			constraints=normalActionConstraints.get((NormalAction)a);
			if(constraints==null){
				constraints=new LinkedHashSet<IConstraint>();
				normalActionConstraints.put((NormalAction)a, constraints);
			}
		}
		
		if(constraints!=null){
			constraints.add(actionRequiresConstraint.getConstraint());
		}
		else{
			System.out.println("It is not possible to assign action constraints to "+a.getName());
		}
		/*actionConstraints.add(actionRequiresConstraint);
		actionRequiresConstraint.addConstraintToCheck(actionRequiresConstraint);*/
	}
	
	public Set<IConstraint> getActionConstraints(IAction a){
		Set<IConstraint> constraints=null;
		
		if(a instanceof InstallAction){
			if(((InstallAction) a).getIsInstall()){
				constraints = installActionConstraints.get(((InstallAction) a).getFeature());
			}
			else{
				constraints=uninstallActionConstraints.get(((InstallAction) a).getFeature());
			}
		}
		else if(a instanceof ReplaceAction){
			constraints=replaceActionConstraints.get(new PairOfConcreteFeature(((ReplaceAction) a).getToRemove(), ((ReplaceAction) a).getToAdd()));
		} 
		else if(a instanceof ConcreteFeature){
			constraints=concreteFeatureActionConstraints.get((ConcreteFeature)a);
		}
		else if(a instanceof NormalAction){
			constraints=normalActionConstraints.get((NormalAction)a);
		}
		
		return constraints;
	}

	public IProcess getProcess() {
		return currentProcess;
	}
	public IProcess getCurrentState() {
		return getProcess();
	}
	
	public String currentStatus(){
		return currentStatus(new StringBuilder()).toString();
	}
	
	public String computeSATMessage(){
		StringBuilder sb=new StringBuilder();
		IConstraint unsat =checkSATCurrentState(); 
		if(unsat==null){
			sb.append("The current state satisfies all constraints.");
		}
		else{
			sb.append("The current state DOES NOT SATISFY all constraints (e.g.:\n");
			sb.append(unsat.toString());
		}
		return sb.toString();
	}
	
	private StringBuilder currentStatus(StringBuilder sb) {
		sb.append("\nBehavior:\n ");
		sb.append(currentProcess.toString());
		sb.append("\n\nInstalled concrete features:\n");
		for (ConcreteFeature concreteFeature : installedFeatures) {
			sb.append(" "+concreteFeature.getName());
			sb.append("\n");
		}
		//sb.append(installedFeatures);
		sb.append("\nVariables:\n");
		for (Entry<String, QFLanVariable> v : variables.entrySet()) {
			sb.append(" "+v.getValue().toStringWithValue()+"\n");
		}
		return sb;
	}

	@Override
	public String toString() {
		StringBuilder sb = currentStatus(new StringBuilder());
		if(predicatesDefs!=null){
			sb.append("\nPredicates:\n");
			for (IPredicateDef predicate : predicatesDefs) {
				sb.append("  "+predicate+"\n");
			}
		}
		if(constraints!=null){
			sb.append("\nConstraints:\n");
			for (IConstraint constraint : constraints) {
				sb.append("  "+constraint+"\n");
			}
		}
		sb.append("\nAction constraints:\n");
		for (Entry<ConcreteFeature, Set<IConstraint>> entry : installActionConstraints.entrySet()) {
			for (IConstraint constraint : entry.getValue()) {
				sb.append("  do(install("+entry.getKey()+")) -> ("+constraint+")\n");
			}
		}
		for (Entry<ConcreteFeature, Set<IConstraint>> entry : uninstallActionConstraints.entrySet()) {
			for (IConstraint constraint : entry.getValue()) {
				sb.append("  do(uninstall("+entry.getKey()+")) -> ("+constraint+")\n");
			}
		}
		for (Entry<PairOfConcreteFeature, Set<IConstraint>> entry : replaceActionConstraints.entrySet()) {
			for (IConstraint constraint : entry.getValue()) {
				sb.append("  do(replace("+entry.getKey().getFirst()+","+entry.getKey().getSecond()+")) -> ("+constraint+")\n");
			}
		}
		for (Entry<ConcreteFeature, Set<IConstraint>> entry : concreteFeatureActionConstraints.entrySet()) {
			for (IConstraint constraint : entry.getValue()) {
				sb.append("  do("+entry.getKey()+") -> ("+constraint+")\n");
			}
		}
		for (Entry<NormalAction,Set<IConstraint>> entry : normalActionConstraints.entrySet()) {
			for (IConstraint constraint : entry.getValue()) {
				sb.append("  do("+entry.getKey()+") -> ("+constraint+")\n");
			}
		}
		return sb.toString();
	}

	public Set<ConcreteFeature> getInstalledFeatures() {
		return installedFeatures;
	}

	public Set<IConstraint> getConstraints() {
		return constraints;
	}
	
	/*public Set<ActionRequiresConstraint> getActionConstraints() {
		return actionConstraints;
	}*/

	public boolean install(ConcreteFeature feature) throws Z3Exception {
		boolean ret = installedFeatures.add(feature);
		if(ret){
			feature.hasBeenInstalled();
		}
		if(!IGNOREZ3){
			if(!ret){
				return ret;
			}
			else{
				int pos = concreteFeatureToPosition.get(feature);
				installedFeaturesForZ3Array[pos] = HasFeature.toZ3(this, feature);
				/*Expr hasFeature = hasFunc.Apply(actionAndFeatureToZ3.get(feature));
			installedFeaturesForZ3Array[pos]=ctx.MkEq(hasFeature,ctx.MkTrue());*/
			}
			return true;
		}
		else {
			return ret;
		}
	}
	
	public boolean uninstall(ConcreteFeature feature) throws Z3Exception {
		return uninstall(feature, true);
	}
	
	public boolean uninstall(ConcreteFeature feature, boolean updateList) throws Z3Exception {
		boolean ret=false;
		if(updateList){
			ret = installedFeatures.remove(feature);
		}
		else{
			ret=installedFeatures.contains(feature);
		}
		if(feature.isInstallled()){
			feature.hasBeenUninstalled();
		}
		if(!IGNOREZ3){
			if(!ret){
				return ret;
			}
			else{
				int pos = concreteFeatureToPosition.get(feature);
				Expr hasFeature = hasFunc.Apply(actionAndFeatureToZ3.get(feature));
				installedFeaturesForZ3Array[pos]=ctx.MkEq(hasFeature,ctx.MkFalse());
			}
			return true;
		}
		else {
			return ret;
		}
	}
	
	public boolean replace(ConcreteFeature toRemove, ConcreteFeature toAdd) throws Z3Exception {
		if((!installedFeatures.contains(toRemove)) || installedFeatures.contains(toAdd)){
			return false;
		}
		
		uninstall(toRemove);
		install(toAdd);
		
		//installedFeatures.remove(toRemove);
		//installedFeatures.add(toAdd);
		
		/*if(!IGNOREZ3){
			int pos = concreteFeatureToPosition.get(toRemove);
			Expr hasFeature = hasFunc.Apply(actionAndFeatureToZ3.get(toRemove));
			installedFeaturesForZ3Array[pos]=ctx.MkEq(hasFeature,ctx.MkFalse());
			pos = concreteFeatureToPosition.get(toAdd);
			hasFeature = hasFunc.Apply(actionAndFeatureToZ3.get(toAdd));
			installedFeaturesForZ3Array[pos]=ctx.MkEq(hasFeature,ctx.MkTrue());
		}*/
				
		return true;
	}

	public double totalValOfInstalledFeatures(IPredicateDef predDef) {
		double ret=0;
		for (ConcreteFeature feature : installedFeatures) {
			ret+=predDef.eval(feature, installedFeatures);
		}
		return ret;
	}

	public double eval(IPredicateExpr predExpr) {
		return predExpr.eval(installedFeatures);
	}

	public void setProcess(IProcess rootProcess) {
		this.currentProcess=rootProcess;
	}
	

	public void addPredicateDef(IPredicateDef predicateDef) {
		predicatesDefs.add(predicateDef);
		nameToPredicatesDefs.put(predicateDef.getName(),predicateDef);
		//predicateDef.setMath(math,this.featuresDefs);
		/*FunctionHandler hdl= new FunctionHandler() {
			
			@Override
			public double evaluateFunction(String fncnam, ArgParser fncargs) throws ArithmeticException {
				// TODO Auto-generated method stub
				return nameToPredicatesDefs.get(fncnam).eval(featureNameToFeature.get(fncargs.next()), installedFeatures);
			}
		};*/
		/*math.setVariable(predicateDef.getName()+"[piero]", 1);
		double d = math.evaluate(predicateDef.getName()+"[piero]");
		System.out.println("d="+d);*/
	}

	public void addProcessDefinition(ProcessDefinition pDefName, IProcess pDefBody) {
		//this.processDefinitions.put(pDefName, pDefBody);
		pDefName.setBody(pDefBody);
	}
	
	public double computeAllowedCommitments() throws Z3Exception{
		return computeAllowedCommitments(null);
	}
	
	
	
	@SuppressWarnings("unused")
	public double computeAllowedCommitments(ArrayList<ICommitment> forbiddenCommitments) throws Z3Exception{
		
		if((!IGNOREZ3) && (!z3Initialized)){
			initZ3();
		}
	
		//BoolExpr[] installedFeaturesInz3 = computeInstalledFeatures();//Make it more efficient 
		
		Collection<ICommitment> allCommitments = getProcess().getCommitments();
		cumulativeRate=0;
		commitments = new DoubleAndCommitment[allCommitments.size()];
		actualNumberOfCommitments=0;
		for (ICommitment commitment : allCommitments) {
			//System.out.println(commitment);
			if(commitment.isAllowed(this)){
				//System.out.println("\t"+commitment);
				cumulativeRate+=commitment.getActionRate();
				commitments[actualNumberOfCommitments]= new DoubleAndCommitment(cumulativeRate, commitment);
				actualNumberOfCommitments++;
				//System.out.println("\t allowed");
			}
			else{
				if(forbiddenCommitments!=null){
					forbiddenCommitments.add(commitment);
				}
			}
		}
		
		//System.out.println(actualNumberOfCommitments+" "+cumulativeRate);
		return cumulativeRate;
	}
	
	/*@SuppressWarnings("unused")
	public double computeAllowedCommitmentsEfficient() throws Z3Exception{
		
		if((!IGNOREZ3) && (!z3Initialized)){
			initZ3();
		}
	
		//BoolExpr[] installedFeaturesInz3 = computeInstalledFeatures();//Make it more efficient 
		cumulativeRate=0;
		actualNumberOfCommitments=0;
		getProcess().startGettingCommitments();
		allowedCommitments = new TreeSet<>();
		while(getProcess().hasCommitments()){
			ICommitment commitment = getProcess().next();
			if(commitment.isAllowed(this)){
				cumulativeRate+=commitment.getActionRate();
				actualNumberOfCommitments++;
				allowedCommitments.add(new DoubleAndCommitment(cumulativeRate, commitment));
			}
		}
		//System.out.println(actualNumberOfCommitments+" "+cumulativeRate);
		return cumulativeRate;
	}*/
	
	public static void printTime(String what, long begin, long end) {
		System.out.println(what+" performed in "+((end-begin)/1000.0)+" seconds.");
	}

	private void commonToSATChecks() throws Z3Exception{
		if(!z3Initialized){
			initZ3();
		}
		solver.Reset();
		solver.Assert(staticConstraints);
		
		/*if(installedFeaturesForZ3Array==null){
			computeInstalledFeaturesForZ3();
		}*/
		
		/*for (AbstractFeature abstractFeature : abstractFeaturesDefs) {
			ArithExpr featureInz3 = actionAndFeatureToZ3.get(abstractFeature);
			Expr hasFeature = hasFunc.Apply(featureInz3);
			solver.Assert(ctx.MkEq(ctx.MkTrue(), hasFeature));
		}*/
	}
	
	public boolean checkSATAsk(IConstraint question) throws Z3Exception {
		commonToSATChecks();
		
		solver.Assert(installedFeaturesForZ3Array);
		solver.Assert(question.toZ3(this));
		
		Status status = solver.Check();
		return status == Status.SATISFIABLE;
	}	
	
	public boolean checkSATExecuteNormalActionOrFeature(IAction actionToExecute) throws Z3Exception {
		commonToSATChecks();
		//solver.Assert(computeInstalledFeatures());
		
		solver.Assert(installedFeaturesForZ3Array);
		Expr doAction = doFunc.Apply(getActionAndFeatureToZ3(actionToExecute));
		solver.Assert(ctx.MkEq(doAction, ctx.MkTrue()));
		
		/*for(ConcreteFeature concreteFeature : concreteFeaturesDefs){
			ArithExpr featureInz3 = actionAndFeatureToZ3.get(concreteFeature);
			Expr hasFeature = hasFunc.Apply(featureInz3);
			Expr doFeature = doFunc.Apply(getActionAndFeatureToZ3(concreteFeature));
			
			if(isInstalled(concreteFeature)){
				solver.Assert(ctx.MkEq(hasFeature,ctx.MkTrue()));
			}
			else{
				solver.Assert(ctx.MkEq(hasFeature,ctx.MkFalse()));
			}
			if(concreteFeature.equals(actionToExecute)){
				solver.Assert(ctx.MkEq(doFeature, ctx.MkTrue()));
			}
			else{
				solver.Assert(ctx.MkEq(doFeature, ctx.MkFalse()));
			}
		}*/
		
		/*for (IAction action : actions) {
			Expr doAction = doFunc.Apply(getActionAndFeatureToZ3(action));
			if(action.equals(actionToExecute)){
				solver.Assert(ctx.MkEq(doAction, ctx.MkTrue()));
			}
			else{
				solver.Assert(ctx.MkEq(doAction, ctx.MkFalse()));
			}
		}*/
				
		Status status = solver.Check();
		return status == Status.SATISFIABLE;
	}
	
	private void initInstalledFeaturesForZ3() throws Z3Exception {
		boolean updatePos=false;
		if(concreteFeatureToPosition==null){
			concreteFeatureToPosition = new HashMap<>(concreteFeaturesDefs.size());
			updatePos=true;
		}
		
		installedFeaturesForZ3Array = new BoolExpr[concreteFeaturesDefs.size()];
		int pos=0;
		for(ConcreteFeature concreteFeature : concreteFeaturesDefs){
			if(updatePos){
				concreteFeatureToPosition.put(concreteFeature, pos);
			}
			ArithExpr featureInz3 = actionAndFeatureToZ3.get(concreteFeature);
			Expr hasFeature = hasFunc.Apply(featureInz3);
			//Expr hasFeature = HasFeature.toZ3(this, concreteFeature);
			/*if(isInstalled(concreteFeature)){
				installedFeaturesForZ3Array[pos]=ctx.MkEq(hasFeature,ctx.MkTrue());
			}
			else{
				installedFeaturesForZ3Array[pos]=ctx.MkEq(hasFeature,ctx.MkFalse());
			}*/
			installedFeaturesForZ3Array[pos]=ctx.MkEq(hasFeature,ctx.MkFalse());
			pos++;
		}
	}

	public boolean checkSATInstalling(ConcreteFeature feature, boolean isInstall) throws Z3Exception {
		commonToSATChecks();
		
		//Store previous installation status of the feature
		int pos = concreteFeatureToPosition.get(feature);
		BoolExpr prev = installedFeaturesForZ3Array[pos];
		
		if(isInstall){
			//Set to true the installation status of the feature, and check sat
			installedFeaturesForZ3Array[pos] = HasFeature.toZ3(this, feature);
			/*ArithExpr featureInz3 = actionAndFeatureToZ3.get(feature);
			Expr hasFeature = hasFunc.Apply(featureInz3);
			installedFeaturesForZ3Array[pos]=ctx.MkEq(hasFeature,ctx.MkTrue());*/
		}
		else{
			//Set to false the installation status of the feature, and check sat
			ArithExpr featureInz3 = actionAndFeatureToZ3.get(feature);
			Expr hasFeature = hasFunc.Apply(featureInz3);
			installedFeaturesForZ3Array[pos]=ctx.MkEq(hasFeature,ctx.MkFalse());
		}
		
		solver.Assert(installedFeaturesForZ3Array);
		Status status = solver.Check();
		
		//Restore the installation status of the feature
		installedFeaturesForZ3Array[pos]=prev;
		return status == Status.SATISFIABLE;
	}
	
	public boolean checkAskNoZ3(AskAction action) throws Z3Exception {
		boolean ret = action.getQuestion().eval(this, action);
		//Do I need to check also all the other constraints?
		/*if(ret){
			ret = check(action);
		}*/
		return ret;
	}
	
	public boolean checkExecuteNormalActionOrFeatureNoZ3(IAction actionToExecute) throws Z3Exception {
		boolean sat = checkConstraintsWhenExecuting(actionToExecute);
		/*if(CHECKSATWITHLOCALANDGLOBALCONSTRAINTS){
			if(sat != checkConstraints(actionToExecute,!QFlanModel.USEJUSTCOSTRAINTSOFTHEACTION)){
				System.out.println("problem! "+actionToExecute);
			}
		}*/
		return sat;
	}
	
	public boolean checkInstallNoZ3(InstallAction action) throws Z3Exception {
		if(action.getIsInstall()){
			this.install(action.getFeature());
		}
		else{
			this.uninstall(action.getFeature());
		}
		
		boolean sat = checkConstraints(action);
		if(CHECKSATWITHLOCALANDGLOBALCONSTRAINTS){
			if(sat != checkConstraints(action,!QFlanModel.USEJUSTCOSTRAINTSOFTHEACTION)){
				System.out.println("problem! "+action);
			}
		}
		
		if(action.getIsInstall()){
			this.uninstall(action.getFeature());
		}
		else{
			this.install(action.getFeature());
		}
	
		return sat;
	}
	
	public boolean checkReplaceNoZ3(ReplaceAction action) throws Z3Exception {
		
		this.uninstall(action.getToRemove());
		this.install(action.getToAdd());
		
		boolean sat = checkConstraints(action);
		if(CHECKSATWITHLOCALANDGLOBALCONSTRAINTS){
			if(sat != checkConstraints(action,!QFlanModel.USEJUSTCOSTRAINTSOFTHEACTION)){
				System.out.println("problem! "+action);
			}
		}
		
		this.install(action.getToRemove());
		this.uninstall(action.getToAdd());
		
		return sat;

	}

	private boolean checkConstraintsWhenExecuting(IAction action) {
		return checkConstraints(action,true);
	}
	
	public boolean checkOtherConstraints(IAction action) {
		return checkConstraints(action,false);
	}
	
	private boolean checkConstraints(IAction action) {
		return checkConstraints(action,QFlanModel.USEJUSTCOSTRAINTSOFTHEACTION);
	}
	
	private boolean checkConstraints(IAction action, boolean checkConstraintsWhenExecuting) {
		boolean sat=true;
		
		Collection<IConstraint> constraintsToConsider = (checkConstraintsWhenExecuting)?getActionConstraints(action):constraints;
		//Collection<IConstraint> constraintsToConsider = (checkConstraintsWhenExecuting)?action.getConstraintsToCheckWhenExecuting():constraints;
		
		if(constraintsToConsider!=null){
			for (IConstraint constraint : constraintsToConsider) {
				//System.out.println("\t"+constraint);
				//if((!(constraint instanceof HasFeature)) ){
				//&& (!(constraint instanceof DoAction))
				if(!constraint.eval(this,action)){
					sat=false;
					break;
				}
				//}
			}
		}
		return sat;
	}
	
	public IConstraint checkSATCurrentState() {
		for (IConstraint constraint : constraints) {
			if(!constraint.eval(this)){
				return constraint;
			}
		}
		return null;
	}
	
	
	public boolean checkSATReplacing(ConcreteFeature toAdd,
			ConcreteFeature toRemove) throws Z3Exception {
		commonToSATChecks();

		//Store previous installation status of the two features
		int posToAdd = concreteFeatureToPosition.get(toAdd);
		BoolExpr prevToAdd = installedFeaturesForZ3Array[posToAdd];
		int posToRemove = concreteFeatureToPosition.get(toRemove);
		BoolExpr prevToRemove = installedFeaturesForZ3Array[posToRemove];
		
		//Set to true the installation status of the feature, and check sat
		/*ArithExpr featureInz3 = actionAndFeatureToZ3.get(toAdd);
		Expr hasFeature = hasFunc.Apply(featureInz3);
		installedFeaturesForZ3Array[posToAdd]=ctx.MkEq(hasFeature,ctx.MkTrue());*/
		installedFeaturesForZ3Array[posToAdd] = HasFeature.toZ3(this, toAdd);
		
		ArithExpr featureInz3 = actionAndFeatureToZ3.get(toRemove);
		Expr hasFeature = hasFunc.Apply(featureInz3);
		//hasFeature = HasFeature.toZ3(this, toRemove);
		installedFeaturesForZ3Array[posToRemove]=ctx.MkEq(hasFeature,ctx.MkFalse());
		solver.Assert(installedFeaturesForZ3Array);
		Status status = solver.Check();

		//Restore the installation status of the feature
		installedFeaturesForZ3Array[posToAdd]=prevToAdd;
		installedFeaturesForZ3Array[posToRemove]=prevToRemove;
		return status == Status.SATISFIABLE;
		
	}
	
	public boolean checkSATSlow(IConstraint question) throws Z3Exception {
		commonToSATChecks();
		
		for(ConcreteFeature concreteFeature : concreteFeaturesDefs){
			ArithExpr featureInz3 = actionAndFeatureToZ3.get(concreteFeature);
			Expr hasFeature = hasFunc.Apply(featureInz3);
			Expr doFeature = doFunc.Apply(getActionAndFeatureToZ3(concreteFeature));
			
			if(isInstalled(concreteFeature)){
				solver.Assert(ctx.MkEq(hasFeature,ctx.MkTrue()));
			}
			else{
				solver.Assert(ctx.MkEq(hasFeature,ctx.MkFalse()));
			}
			solver.Assert(ctx.MkEq(doFeature, ctx.MkFalse()));
		}
		
		dontDoActions();
		
		solver.Assert(question.toZ3(this));
		
		Status status = solver.Check();
		return status == Status.SATISFIABLE;
	}

	public boolean checkSATSlow(IAction actionToExecute) throws Z3Exception {
		commonToSATChecks();
		//solver.Assert(computeInstalledFeatures());
		
		for(ConcreteFeature concreteFeature : concreteFeaturesDefs){
			ArithExpr featureInz3 = actionAndFeatureToZ3.get(concreteFeature);
			Expr hasFeature = hasFunc.Apply(featureInz3);
			Expr doFeature = doFunc.Apply(getActionAndFeatureToZ3(concreteFeature));
			
			if(isInstalled(concreteFeature)){
				solver.Assert(ctx.MkEq(hasFeature,ctx.MkTrue()));
			}
			else{
				solver.Assert(ctx.MkEq(hasFeature,ctx.MkFalse()));
			}
			if(concreteFeature.equals(actionToExecute)){
				solver.Assert(ctx.MkEq(doFeature, ctx.MkTrue()));
			}
			else{
				solver.Assert(ctx.MkEq(doFeature, ctx.MkFalse()));
			}
		}
		
		for (IAction action : actions) {
			Expr doAction = doFunc.Apply(getActionAndFeatureToZ3(action));
			if(action.equals(actionToExecute)){
				solver.Assert(ctx.MkEq(doAction, ctx.MkTrue()));
			}
			else{
				solver.Assert(ctx.MkEq(doAction, ctx.MkFalse()));
			}
		}
				
		Status status = solver.Check();
		return status == Status.SATISFIABLE;
	}
	
	public boolean checkSATInstallingSlow(ConcreteFeature feature, boolean isInstall) throws Z3Exception {
		commonToSATChecks();
		
		for(ConcreteFeature concreteFeature : concreteFeaturesDefs){
			ArithExpr featureInz3 = actionAndFeatureToZ3.get(concreteFeature);
			Expr hasFeature = hasFunc.Apply(featureInz3);
			Expr doFeature = doFunc.Apply(getActionAndFeatureToZ3(concreteFeature));
			
			if(concreteFeature.equals(feature)){
				if(isInstall){
					solver.Assert(ctx.MkEq(hasFeature,ctx.MkTrue()));
				}
				else{
					solver.Assert(ctx.MkEq(hasFeature,ctx.MkFalse()));
				}
			}
			else if(isInstalled(concreteFeature)){
				solver.Assert(ctx.MkEq(hasFeature,ctx.MkTrue()));
			}
			else{
				solver.Assert(ctx.MkEq(hasFeature,ctx.MkFalse()));
			}
			solver.Assert(ctx.MkEq(doFeature, ctx.MkFalse()));
		}
		
		dontDoActions();
				
		Status status = solver.Check();
		return status == Status.SATISFIABLE;
	}
	
	public boolean checkSATReplacingSlow(ConcreteFeature toAdd,
			ConcreteFeature toRemove) throws Z3Exception {
		commonToSATChecks();
		
		for(ConcreteFeature concreteFeature : concreteFeaturesDefs){
			ArithExpr featureInz3 = actionAndFeatureToZ3.get(concreteFeature);
			Expr hasFeature = hasFunc.Apply(featureInz3);
			Expr doFeature = doFunc.Apply(getActionAndFeatureToZ3(concreteFeature));
			
			if(concreteFeature.equals(toAdd)){
				solver.Assert(ctx.MkEq(hasFeature,ctx.MkTrue()));
			}
			else if(concreteFeature.equals(toRemove)){
				solver.Assert(ctx.MkEq(hasFeature,ctx.MkFalse()));
			}
			else if(isInstalled(concreteFeature)){
				solver.Assert(ctx.MkEq(hasFeature,ctx.MkTrue()));
			}
			else{
				solver.Assert(ctx.MkEq(hasFeature,ctx.MkFalse()));
			}
			solver.Assert(ctx.MkEq(doFeature, ctx.MkFalse()));
		}
		
		dontDoActions();
		
		Status status = solver.Check();
		return status == Status.SATISFIABLE;
		
	}

	
	private void dontDoActions() throws Z3Exception {
		for (IAction action : actions) {
			Expr doAction = doFunc.Apply(getActionAndFeatureToZ3(action));
			solver.Assert(ctx.MkEq(doAction, ctx.MkFalse()));			
		}
	}


	
	public Context getCTX(){
		return ctx;
	}
	
	public FuncDecl getHasFunc(){
		return hasFunc;
	}
	
	public FuncDecl getDoFunc(){
		return doFunc;
	}
	
	public ArithExpr getActionAndFeatureToZ3(IAction actionOrFeature){
		return actionAndFeatureToZ3.get(actionOrFeature);
	}
	
	private void initZ3() throws Z3Exception {
		z3Initialized=true;
		staticConstraints = ctx.MkTrue();
		//Now we also have actionConstraints. UPDATE TO CONSIDER THEM!
		for (IConstraint constraint : constraints) {
			/*
			eq $store2smtConstraints(True, ignoreHasAndDo) = "true" .
			eq $store2smtConstraints(False, ignoreHasAndDo) = "false" .
			eq $store2smtConstraints( f * g , ignoreHasAndDo) = "(not (and (has " #+ toz3String(f) #+ ") (has " #+ toz3String(g) #+ ") ) )" .
			eq $store2smtConstraints( f |> g , ignoreHasAndDo) =  "(implies (has " #+ toz3String(f) #+ ") (has " #+ toz3String(g) #+ ")) " .
			*/
			/*if((!(constraint instanceof HasFeature)) //&& (!(constraint instanceof DoAction))
														){
				//solver.Assert(constraint.toZ3(this));
				//System.out.println(constraint);
				staticConstraints = ctx.MkAnd(new BoolExpr[]{constraint.toZ3(this),staticConstraints});
			}*/
			staticConstraints = ctx.MkAnd(new BoolExpr[]{constraint.toZ3(this),staticConstraints});
		}
		
		//staticConstraints=(BoolExpr)staticConstraints.Simplify();
		
		/*for (ConcreteFeature installedFeature : installedFeatures) {
			ArithExpr featureInz3 = actionAndFeatureToZ3.get(installedFeature);
			Expr hasFeature = hasFunc.Apply(featureInz3);
			solver.Assert(ctx.MkEq(ctx.MkTrue(), hasFeature));
		}*/
		
		/*NormalAction sell = new NormalAction("sell");
		Symbol actionName = ctx.MkSymbol(sell.getName());
		actionAndFeatureToZ3.put(sell, (ArithExpr)ctx.MkConst(actionName,ctx.MkIntSort()));
		actionAndFeatureToZ3Val.put(sell,ctx.MkInt(actionAndFeatureToZ3Val.size()+1));
		Expr aaa = doFunc.Apply(actionAndFeatureToZ3.get(sell));
		solver.Assert(ctx.MkEq(ctx.MkTrue(), aaa));*/
		
	}
	
	/*private BoolExpr[] computeInstalledFeatures() throws Z3Exception{
		BoolExpr[] installedFeatureInz3= new BoolExpr[installedFeatures.size()];
		int i=0;
		for (ConcreteFeature installedFeature : installedFeatures) {
			ArithExpr featureInz3 = actionAndFeatureToZ3.get(installedFeature);
			Expr hasFeature = hasFunc.Apply(featureInz3);
			//solver.Assert(ctx.MkEq(ctx.MkTrue(), hasFeature));
			installedFeatureInz3[i]=ctx.MkEq(ctx.MkTrue(), hasFeature);
			i++;
		}
		
		return installedFeatureInz3;
	}*/

	public double getCumulativeRate(){
		return cumulativeRate;
	}
	
	public DoubleAndCommitment[] getAllCommitments() {
		return commitments;
	}
	
	
	public int getNumberOfComputedCommitments(){
		return actualNumberOfCommitments;
	}
	
	
	/*public ICommitment getAllowedCommitmentEfficient(double sampledNumber){
		if(allowedCommitments==null||allowedCommitments.size()==0){
			return null;
		}
		if(sampledNumber>cumulativeRate){
			return null;
		}
		
		//DoubleAndCommitment comm = allowedCommitments.floor(new DoubleAndCommitment(sampledNumber));
		DoubleAndCommitment comm = allowedCommitments.ceiling(new DoubleAndCommitment(sampledNumber));
		return comm.getCommitment();
	}*/
	
	public ICommitment getAllowedCommitment(double sampledNumber){
		ICommitment com=null;
		for(int i=0;i<actualNumberOfCommitments;i++){
			if(sampledNumber < commitments[i].getRate()){
				return commitments[i].getCommitment();
			}
		}
		return com;
	}
	
	
	/*
	 DOES NOT WORK!
	 public ICommitment getAllowedCommitment(double sampledNumber){
		int first=0;
		int last=actualNumberOfCommitments-1;
		int current = updateIndex(first, last);
		
		if(sampledNumber>cumulativeRate){
			throw new UnsupportedOperationException("sampled number "+sampledNumber+" greater than the cumulative rate");
		}
		if(commitments.length==0){
			return null;
		}
		
		while(first!=last){
			if(sampledNumber==commitments[current].getRate()){
				first=current;
				last=current;
			}
			else if(sampledNumber<commitments[current].getRate()){
				last=Math.max(first, current-1);
			}
			else{
				first=Math.min(last, current+1);
			}
			current = updateIndex(first, last);
		}
		return commitments[current].getCommitment();
	}
	
	private int updateIndex(int first,int last){
		if(first==last){
			return first;
		}
		else{
			return first+ ((last-first)/2);
		}
	}
	
	*/
	
	public double[] applySideEffects(SideEffect[] sideEffects) {
		double[] oldValues=null;
		if(sideEffects!=null&&sideEffects.length>0){
			oldValues = new double[sideEffects.length];
			double[] newValues = new double[sideEffects.length];
			for (int i = 0; i < sideEffects.length; i++) {
				QFLanVariable var = sideEffects[i].getVariable();
				oldValues[i]=var.eval(null);
				newValues[i] = sideEffects[i].evalUpdateExpression(installedFeatures);
				/*String expr = sideEffects[i].getNewValue();
				newValues[i] = computeExpressionValue(expr);*/
			}
			for (int i = 0; i < sideEffects.length; i++) {
				QFLanVariable var = sideEffects[i].getVariable();
				updateVariableValue(var, newValues[i]);
			}
		}
		return oldValues;
	}
	public void revertSideEffects(SideEffect[] sideEffects,double[] oldValues){
		if(sideEffects!=null){
			for (int i = 0; i < sideEffects.length; i++) {
				QFLanVariable var = sideEffects[i].getVariable();
				updateVariableValue(var, oldValues[i]);
			}
		}
	}
	
	public void apply(ICommitment commitment) throws Z3Exception{
		setProcess(commitment.getContinuation());
		if(commitment.modifiesStore()){
			//installedFeaturesForZ3Array = null;
			if(commitment.getAction() instanceof ReplaceAction){
				ReplaceAction action = (ReplaceAction)commitment.getAction();
				replace(action.getToRemove(), action.getToAdd());
			}
			else if(commitment.getAction() instanceof InstallAction){
				InstallAction action = (InstallAction)commitment.getAction();
				if(action.getIsInstall()){
					install(action.getFeature());
				}
				else{
					uninstall(action.getFeature());
				}
				
			} 
		}
		
		applySideEffects(commitment.getSideEffects());
		/*SideEffect[] sideEffects = commitment.getSideEffects();
		if(sideEffects!=null&&sideEffects.length>0){
			double[] newValues = new double[sideEffects.length];
			for (int i = 0; i < sideEffects.length; i++) {
				//QFLanVariable var = sideEffects[i].getVariable();
				String expr = sideEffects[i].getNewValue();
				newValues[i] = math.evaluate(expr);
			}
			for (int i = 0; i < sideEffects.length; i++) {
				QFLanVariable var = sideEffects[i].getVariable();
				updateVariableValue(var, newValues[i]);
			}
		}*/
		lastPerformedActionName=commitment.getAction().getName();
		//z3Initialized=false;
	}
	
	protected void setLastPerformedActionName(String lastPerformedActionName) {
		this.lastPerformedActionName = lastPerformedActionName;
	}
	public String getLastPerformedActionName() {
		return lastPerformedActionName;
	}
	
	

	public void addNormalAction(NormalAction action) throws Z3Exception {
		if(actions.contains(action)){
			return;
		}
		else{
			actions.add(action);
			if(!IGNOREZ3){
				addActionOrFeatureToZ3(action);
			}
		}
	}

	private void addActionOrFeatureToZ3(IAction actionOrFeature) throws Z3Exception {
		Symbol actionName = ctx.MkSymbol(actionOrFeature.getName());
		/*if(actionOrFeature.getName().equals("a")){
			aSymbol= actionName;
			Expr aExpr = ctx.MkConst(actionName,ctx.MkIntSort());
			aFuncDecl = aExpr.FuncDecl(); 
		}*/
		actionAndFeatureToZ3.put(actionOrFeature, (ArithExpr)ctx.MkConst(actionName,ctx.MkIntSort()));
		actionAndFeatureToZ3Val.put(actionOrFeature,ctx.MkInt(actionAndFeatureToZ3Val.size()+1));
	}
	
	private void addVariableToZ3(QFLanVariable var) throws Z3Exception {
		//TODO double check, because I am not sure that it works
		/*Symbol varName = ctx.MkSymbol(var.getName());
		varToZ3.put(var, (ArithExpr)ctx.MkConst(varName,ctx.MkRealSort()));
		varToZ3Val.put(var,ctx.MkReal(String.valueOf(var.getValue())));*/
	}



	public static IProcess makeMultiChoice(Collection<IProcess> processes) {
		if(processes==null || processes.isEmpty()){
			return ZeroProcess.ZERO;
		}
		else{
			boolean first=true;
			IProcess p = null; 
			for (IProcess process : processes) {
				if(first){
					first=false;
					p=process;
				}
				else{
					p=new Choice(p, process);
				}
			}
			return p;
		}
	}
	
	public static IProcess makeMultiParallel(Collection<IProcess> processes) {
		if(processes==null || processes.isEmpty()){
			return ZeroProcess.ZERO;
		}
		else{
			boolean first=true;
			IProcess p = null; 
			for (IProcess process : processes) {
				if(first){
					first=false;
					p=process;
				}
				else{
					p=new Parallel(p, process);
				}
			}
			return p;
		}
	}

	public boolean isAConcreteFeature(ConcreteFeature toSearch) {
		return concreteFeaturesDefs.contains(toSearch);
	}
	
	public boolean isAbstractFeature(AbstractFeature toSearch) {
		return abstractFeaturesDefs.contains(toSearch);
	}


	public IPredicateDef getPredicateDef(String predName) {
		return nameToPredicatesDefs.get(predName);
	}
	
	/*public void addPredicate(IPredicateExpr predicate) {
		predicates.add(predicate);
	}*/
	
	public void computeDescendantsAndAncestors() {
		for (AbstractFeature abstractFeature : abstractFeaturesDefs) {
			abstractFeature.computeOrGetConcreteDescendants();
		}
		
	}

	public IFeature getFeature(String name) {
		return featuresDefs.get(name);
	}
	
	
	
	
	
	
	
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
//z3
		private Context ctx;
		private static Solver solver;
		private HashMap<String, String> cfg;
		private FuncDecl doFunc;
		private FuncDecl hasFunc;
		private boolean z3Initialized=false;
		private BoolExpr staticConstraints;
		private ProcessDefinition initialProcess;
		private Set<ConcreteFeature> intialInstalledFeatures;
		//private BoolExpr installedFeaturesz3;
		private BoolExpr[] installedFeaturesForZ3Array;
		private HashMap<ConcreteFeature, Integer> concreteFeatureToPosition;
		//private TreeSet<DoubleAndCommitment> allowedCommitments;
		
		/*private Symbol aSymbol;
		private Symbol doFuncSymbol;
		private FuncDecl aFuncDecl;*/
	
	private void initZ3DataStructures() throws Z3Exception {
		cfg = new HashMap<String, String>();
		cfg.put("model", "false");
		//cfg.put("macro-finder", "true");
		ctx = new Context(cfg);
		solver = ctx.MkSolver();
		actionAndFeatureToZ3=new HashMap<>();
		actionAndFeatureToZ3Val=new HashMap<>();
		/*varToZ3=new HashMap<>();
		varToZ3Val=new HashMap<>();*/

		//DO
		//(define-fun do ((x Int)) Bool (if (or false (= x sell) ) true false))
		doFunc = ctx.MkFuncDecl("do", ctx.MkIntSort(), ctx.MkBoolSort());
		//I don't  need to scan the constraints to search for "do(sell)". I always add it explicitly when checking if an action is allowed.  
		//solver.Assert(arg0);
		/*NormalAction sell = new NormalAction("sell");
	Symbol actionName = ctx.MkSymbol(sell.getName());
	actionAndFeatureToZ3.put(sell, (ArithExpr)ctx.MkConst(actionName,ctx.MkIntSort()));
	actionAndFeatureToZ3Val.put(sell,ctx.MkInt(actionAndFeatureToZ3Val.size()+1));
	Expr aaa = doFunc.Apply(actionAndFeatureToZ3.get(sell));
	solver.Assert(ctx.MkEq(ctx.MkTrue(), aaa));*/

		//INSTALLED ACTIONS
		//eq installedFeaturesForz3(Cons) =  " (define-fun has ((x Int)) Bool (if (or false " #+ $store2smtStatus("has",Cons) #+ ") true false))" .
		//(define-fun has ((x Int)) Bool (if (or false (= x AllYear) (= x Diamond) ) true false))
		hasFunc = ctx.MkFuncDecl("has", ctx.MkIntSort(), ctx.MkBoolSort());

		//Static constraints: this I have to do only once, as I assume them to be unmutable. I have to pass to z3 all the constraints
		//staticConstraints(initFragment)

	}

}
