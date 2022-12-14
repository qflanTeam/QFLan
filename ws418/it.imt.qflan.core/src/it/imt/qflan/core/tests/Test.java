package it.imt.qflan.core.tests;

import java.util.ArrayList;
import java.util.Collection;
//import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.features.AbstractFeature;
import it.imt.qflan.core.features.ConcreteFeature;
import it.imt.qflan.core.model.IQFlanModelBuilder;
import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.models.TestBikesWithoutFakeFeatures2;
//import it.imt.qflan.core.models.BikesWithoutFakeFeatures;
//import it.imt.qflan.core.models.BikesSPLC;
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
import it.imt.qflan.core.processes.actions.InstallAction;
import it.imt.qflan.core.processes.actions.NormalAction;
import it.imt.qflan.core.processes.actions.ReplaceAction;
import it.imt.qflan.core.processes.constraints.ActionRequiresConstraint;
import it.imt.qflan.core.processes.constraints.DisequationOfPredicateExpressions;
import it.imt.qflan.core.processes.constraints.FeatureRequireConstraint;
import it.imt.qflan.core.processes.constraints.HasFeature;
import it.imt.qflan.core.processes.constraints.PredicateExprComparator;
import it.imt.qflan.core.processes.interfaces.ICommitment;
import it.imt.qflan.core.processes.interfaces.IConstraint;
import it.imt.qflan.core.processes.interfaces.IProcess;
import it.imt.qflan.core.variables.SideEffect;
import vesta.mc.ParametersForState;

public class Test {

	public static void main(String[] args) throws Z3Exception {
		//ParametersForState pfs = new ParametersForState("bikes", "");
		//ParametersForState pfs = new ParametersForState("elevator", "");
		//ParametersForState pfs = new ParametersForState("/Users/andrea/Desktop/workspace/Elevator/Elevator.qflan", "");
		//ParametersForState pfs = new ParametersForState("/Users/andrea/Dropbox/runtime-QFLan.product/pino/BikesWithoutFakeFeatures2.qflan", "");
		
		//ParametersForState pfs = new ParametersForState("bikesdiagram", "");
		//ParametersForState pfs = new ParametersForState("/Users/andrea/Dropbox/runtime-QFLan.product/piero/BikesDiagram.qflan", "");
		//ParametersForState pfs = new ParametersForState("/Users/andrea/Documents/QFLan/QFLan/runtime-QFLan.product/FASE2018/VendingMachine.qflan", "");
		ParametersForState pfs = new ParametersForState("/Users/andrea/Documents/QFLan/QFLan/runtime-QFLan.product/FASE2018/src-gen/VendingMachine.java", "");

		//ParametersForState pfs = new ParametersForState("/Users/andrea/Dropbox/runtime-QFLan.product/Piero/Pino.qflan", "");
		//ParametersForState pfs = new ParametersForState("pino", "");
		//ParametersForState pfs = new ParametersForState("pippo", "");
		//ParametersForState pfs = new ParametersForState("/Users/andrea/Dropbox/runtime-QFLan.product/TestProject/Pippo.qflan", "");
		//ParametersForState pfs = new ParametersForState("/Users/andrea/Dropbox/runtime-QFLan.product/TestProject/SimpleModel.qflan", "");
		//ParametersForState pfs = new ParametersForState("/Users/andrea/Dropbox/runtime-QFLan.product/TestProject/SimpleModel2.qflan", "");
		
		QFlanJavaState qjs = new QFlanJavaState(pfs);
		
		double avg =0;
		
		int howManySimulations=1;
		ArrayList<Integer> seeds = new ArrayList<>(howManySimulations);
		for(int i=0; i<howManySimulations;i++){
			seeds.add(i*100);
		}
		int howManySteps=20;
		for(int i=0; i<howManySimulations;i++){
			System.out.println("#################");
			System.out.println("Simulation "+i);
			System.out.println("#################");
			qjs.setSimulatorForNewSimulation(seeds.get(i));
			for(int s=0;s<howManySteps;s++){
				qjs.performOneStepOfSimulation();
				//LinkedHashMap<String, String> logData = qjs.computeDataToLog();
				//qjs.rval("price(Bike)");
				String obs="price(Machine)";
				//String obs="Coffee";
				System.out.println(obs+" = "+qjs.rval(obs));
				/*double di = qjs.rval("Diamond");
				double ay = qjs.rval("AllYear");
				double su = qjs.rval("Summer");
				double wi = qjs.rval("Winter");
				System.out.println("Diamond="+di+" AllYear="+ay+" Summer="+su+" Winter="+wi);*/
				/*double price = qjs.rval("price");
				double weight = qjs.rval("weight");
				double load = qjs.rval("load");
				System.out.println("Step: "+s+"\n\tprice: "+price+", weight:" +weight+", load: "+load);*/
			}
			System.out.println("Total time to compute commitments:"+QFlanJavaState.TOTALCOMPUTATIONCOMMITMENTS);
			avg+=QFlanJavaState.TOTALCOMPUTATIONCOMMITMENTS;
			System.out.println("Total time to choose commitments:"+QFlanJavaState.TOTALCHOICEOFCOMMITMENT);
			System.out.println("Total time to apply commitments:"+QFlanJavaState.TOTALAPPLICATIONOFCOMMITMENT);
		}
		System.out.println("avg time to compute commitments ="+(avg/(double)howManySimulations));
	}
	
	public static void testBikes() throws Z3Exception {
		long begin=  System.currentTimeMillis();
		//IQFlanModelBuilder bikesSPLCBuilder = new BikesSPLC();
		IQFlanModelBuilder bikesBuilder = new TestBikesWithoutFakeFeatures2();
		//IQFlanModelBuilder bikesBuilder = null;
		QFlanModel bikesSPLC = bikesBuilder.createModel();
		//test2();
		//test3();
		
		System.out.println(bikesSPLC);
		//long begin=  System.currentTimeMillis();
		bikesSPLC.computeAllowedCommitments();
		long end=  System.currentTimeMillis();
		System.out.println("Time to build the model and to generate all one-step next states: "+((end-begin)/1000.0));
		/*for (DoubleAndCommitment comm : bikesSPLC.getAllCommitments()) {
			if(comm==null){
				break;
			}
			else{
				System.out.println(comm);
			}
		}*/
		ICommitment comm = bikesSPLC.getAllowedCommitment(1);
		bikesSPLC.apply(comm);
		for(int i=0;i<500;i++){
			begin=  System.currentTimeMillis();
			bikesSPLC.computeAllowedCommitments();
			end=  System.currentTimeMillis();
			if(bikesSPLC.getNumberOfComputedCommitments()>0){
				comm = bikesSPLC.getAllowedCommitment(1);
				bikesSPLC.apply(comm);
				System.out.println("Time to generate all one-step next states: "+((end-begin)/1000.0));
				System.out.println(comm);
				System.out.println();
			}
			else{
				System.out.println("\n\n");
				System.out.println("No more steps");
				System.out.println(bikesSPLC);
				System.out.println(i);
				break;
			}
		}
		//comm = bikesSPLC.getAllowedCommitment(1);
		System.out.println(bikesSPLC);
	}
	
	public static void test3() throws Z3Exception{
		QFlanModel model = new QFlanModel();
		
		/*ConcreteFeature cf = new ConcreteFeature("feat");
		ConcreteFeature cf2 = new ConcreteFeature("feat");
		if(cf.equals(cf2)){
			System.out.println("equal features");
		}*/
		
		ProcessDefinition p1Def = new ProcessDefinition("p1");
		ProcessDefinition p2Def = new ProcessDefinition("p2");
		NormalAction a = new NormalAction("a");
		NormalAction b = new NormalAction("b");
		model.addNormalAction(a);
		model.addNormalAction(b);
		IProcess p1 = new Prefix(1, a, new SideEffect[]{}, p2Def);
		IProcess p2 = new Prefix(3, b, new SideEffect[]{}, p1Def);
		
		
		model.addProcessDefinition(p1Def,p1);
		model.addProcessDefinition(p2Def,p2);

		//IProcess p12 = new Parallel(p1, p2);
		
		//model.setProcess();


		//Features
		AbstractFeature bike = new AbstractFeature("bike");
		model.addAbstractFeatureDefinition(bike);

		AbstractFeature wheels = new AbstractFeature("wheels");
		bike.addSon(wheels);
		ConcreteFeature allYear = new ConcreteFeature("allYear");
		ConcreteFeature summer = new ConcreteFeature("summer");
		ConcreteFeature winter = new ConcreteFeature("winter");
		wheels.addSon(allYear);
		wheels.addSon(summer);
		wheels.addSon(winter);
		model.addAbstractFeatureDefinition(wheels);
		model.addConcreteFeatureDefinition(allYear);
		model.addConcreteFeatureDefinition(summer);
		model.addConcreteFeatureDefinition(winter);
		
		//Constraints
		ActionRequiresConstraint aRequiresAllYear = new ActionRequiresConstraint(a, new HasFeature(allYear,model,false));
		model.addConstraint(aRequiresAllYear);
		/*NotConstraintExpr notHasAllYear = new NotConstraintExpr(new HasFeature(allYear));
		model.addConstraint(notHasAllYear);*/
		
		/*NormalAction a2 = new NormalAction("a");
		if(a.equals(a2)){
			System.out.println("equals");
		}*/
		
		//We have to avoid inifinite support models. We restrict to finite-support ones
		/*ProcessDefinition p2Def = new ProcessDefinition("p2");
		IProcess p1 = new Prefix(1, new NormalAction("a"), ZeroProcess.ZERO);
		IProcess p2 = new Parallel(p1,p2Def);
		model.addProcessDefinition(p2Def,p2);*/
		
		//Prefix p2 = new Prefix(2, new NormalAction("a"), ZeroProcess.ZERO);
		//IProcess p3 = new ProcessDefinition("p2Parp3");
		//ProcessDefinition p2Parp3 = new ProcessDefinition("p2Parp3"); //new ProcessDefinition("p2Parp3",new Parallel(p2, p3));
		//model.addProcessDefinition(p2Parp3,new Parallel(p2, p3));
		//ProcessDefinition p2Rec = new ProcessDefinition("p2Parp3Twice", new Parallel(p2Parp3, R));
		//IProcess p4 = new Prefix(2, winter, Rec);
		//ProcessDefinition pDef2 = new ProcessDefinition("pDef2", p2Parp3);
		//ProcessDefinition pDef3 = new ProcessDefinition("pDef3", pDef2);
		
		//IProcess p2Rec = new ProcessDefinition("p2Rec", p2.createPrefixWithNewContinuation(p2));
		
		//IProcess root=new Parallel(p1, p2Parp3);
		//IProcess root=new Parallel(p1, new Parallel(p2, p3));
		//IProcess root=new Parallel(p1, pDef3);
		//model.setProcess(root);
		
		//model.install(allYear);
		model.install(summer);
		
		//model.setProcess(new Parallel(p12, new Prefix(3, allYear, ZeroProcess.ZERO)));
		//model.setProcess(new Parallel(p12, new Prefix(3, new AskAction(new HasFeature(allYear)), ZeroProcess.ZERO)));
		model.setProcess(
				new Parallel(new Prefix(5, new ReplaceAction(summer,allYear), new SideEffect[]{}, ZeroProcess.ZERO), 
							 new Parallel(new Prefix(2, new InstallAction(allYear, true), new SideEffect[]{},ZeroProcess.ZERO),new Prefix(2, new InstallAction(allYear, false), new SideEffect[]{}, ZeroProcess.ZERO)))
				);
		
		//printAllCommitments(model);
		System.out.println(model);
		model.computeAllowedCommitments();
		/*for (DoubleAndCommitment comm : model.getAllCommitments()) {
			System.out.println(comm);
		}*/
		
		/*System.out.println("");
		System.out.println(model.getAllowedCommitment(0.0));
		System.out.println(model.getAllowedCommitment(1.0));
		System.out.println(model.getAllowedCommitment(2.0));*/
		
	}
	
	public static void test2() throws Z3Exception{
		QFlanModel model = new QFlanModel();
		
		//Features
		AbstractFeature bike = new AbstractFeature("bike");
		model.addAbstractFeatureDefinition(bike);
		
		AbstractFeature wheels = new AbstractFeature("wheels");
		bike.addSon(wheels);
		ConcreteFeature allYear = new ConcreteFeature("allYear");
		ConcreteFeature summer = new ConcreteFeature("summer");
		ConcreteFeature winter = new ConcreteFeature("winter");
		wheels.addSon(allYear);
		wheels.addSon(summer);
		wheels.addSon(winter);
		model.addAbstractFeatureDefinition(wheels);
		model.addConcreteFeatureDefinition(allYear);
		model.addConcreteFeatureDefinition(summer);
		model.addConcreteFeatureDefinition(winter);
		
		AbstractFeature frame = new AbstractFeature("frame");
		bike.addSon(frame);
		ConcreteFeature diamond = new ConcreteFeature("diamond");
		ConcreteFeature stepThrough = new ConcreteFeature("stepThrough");
		frame.addSon(diamond);
		frame.addSon(stepThrough);
		model.addAbstractFeatureDefinition(frame);
		model.addConcreteFeatureDefinition(diamond);
		model.addConcreteFeatureDefinition(stepThrough);
		
		AbstractFeature tablet = new AbstractFeature("tablet");
		bike.addSon(tablet);
		ConcreteFeature mapsApp = new ConcreteFeature("mapsApp"); 
		ConcreteFeature naviApp = new ConcreteFeature("naviApp"); 
		ConcreteFeature guideApp = new ConcreteFeature("guideApp");
		ConcreteFeature music = new ConcreteFeature("music");
		tablet.addSon(mapsApp);
		tablet.addSon(naviApp);
		tablet.addSon(guideApp);
		tablet.addSon(music);
		model.addAbstractFeatureDefinition(tablet);
		model.addConcreteFeatureDefinition(mapsApp);
		model.addConcreteFeatureDefinition(naviApp);
		model.addConcreteFeatureDefinition(guideApp);
		model.addConcreteFeatureDefinition(music);
		
		//Predicates
		IPredicateDef price = new PredicateDef("price");
		model.addPredicateDef(price);
		
		price.setValue(allYear, 100);
		price.setValue(summer, 70);
		price.setValue(winter, 80);
		
		price.setValue(diamond, 100);
		price.setValue(stepThrough, 90);
		
		price.setValue(mapsApp, 10);
		price.setValue(naviApp, 20);
		price.setValue(guideApp, 10);
		price.setValue(music, 10);
		
		IPredicateExpr priceOfWheels = new Predicate(price, wheels);
		IPredicateExpr priceOfFrame = new Predicate(price, frame);
		IPredicateExpr priceOfTablet = new Predicate(price, tablet);
		IPredicateExpr totalPrice = new ArithmeticPredicateExpr(priceOfWheels, new ArithmeticPredicateExpr(priceOfFrame,priceOfTablet,ArithmeticOperation.SUM), ArithmeticOperation.SUM);
		IPredicateExpr twiceTotalPrice = new ArithmeticPredicateExpr(new Constant(2), totalPrice, ArithmeticOperation.MULT);
		IPredicateExpr priceOfBike = new Predicate(price, bike);
		
		//Constraints
		IConstraint priceMinorThanTwicePrice = new DisequationOfPredicateExpressions(totalPrice, twiceTotalPrice, PredicateExprComparator.LE);
		model.addConstraint(priceMinorThanTwicePrice);
		Collection<ConcreteFeature> allWheels = new LinkedHashSet<>(3);
		allWheels.add(allYear);
		allWheels.add(summer);
		allWheels.add(winter);
		/*IConstraint atMostOneWheel = new FeatureSetConstraint(allWheels, FeatureSetCondition.ATMOSTONE,model);
		model.addConstraint(atMostOneWheel);
		IConstraint atLeastOneWheel = new FeatureSetConstraint(allWheels, FeatureSetCondition.ATLEASTONE,model);
		model.addConstraint(atLeastOneWheel);*/
		IConstraint naviNeedsMaps = new FeatureRequireConstraint(naviApp, mapsApp,model);
		model.addConstraint(naviNeedsMaps);

		//(p('price,Bike) < 800.0)
		IConstraint maxPrice = new DisequationOfPredicateExpressions(priceOfBike, new Constant(800), PredicateExprComparator.LE);
		model.addConstraint(maxPrice);
		
		NormalAction sell = new NormalAction("sell");
		model.addNormalAction(sell);
		//( do(sell) -> ((p('price,Bike) > 250.0)))
		IConstraint sellIfPriceOk = new ActionRequiresConstraint(sell, new DisequationOfPredicateExpressions(priceOfBike, new Constant(250), PredicateExprComparator.LE));
		model.addConstraint(sellIfPriceOk);
		
		//Processes
		NormalAction a = new NormalAction("a");
		model.addNormalAction(a);
		IProcess p1 = new Prefix(1, a, new SideEffect[]{},ZeroProcess.ZERO);
		Prefix p2 = new Prefix(2, summer, new SideEffect[]{},ZeroProcess.ZERO);
		IProcess p3 = new Prefix(2, winter, new SideEffect[]{},ZeroProcess.ZERO);
		ProcessDefinition p2Parp3 = new ProcessDefinition("p2Parp3"); //new ProcessDefinition("p2Parp3",new Parallel(p2, p3));
		model.addProcessDefinition(p2Parp3,new Parallel(p2, p3));
		//ProcessDefinition p2Rec = new ProcessDefinition("p2Parp3Twice", new Parallel(p2Parp3, R));
		//IProcess p4 = new Prefix(2, winter, Rec);
		//ProcessDefinition pDef2 = new ProcessDefinition("pDef2", p2Parp3);
		//ProcessDefinition pDef3 = new ProcessDefinition("pDef3", pDef2);
		
		//IProcess p2Rec = new ProcessDefinition("p2Rec", p2.createPrefixWithNewContinuation(p2));
		
		IProcess root=new Parallel(p1, p2Parp3);
		//IProcess root=new Parallel(p1, new Parallel(p2, p3));
		//IProcess root=new Parallel(p1, pDef3);
		model.setProcess(root);
		
		
		printAllCommitments(model);
		
		model.install(winter);
		//System.out.println(cost.eval(winter, model.getInstalledFeatures()));
		//System.out.println(cost.eval(wheels, model.getInstalledFeatures()));
		System.out.println(model.totalValOfInstalledFeatures(price)+"  ==  "+model.eval(priceOfBike));
		System.out.println(model.eval(twiceTotalPrice));
		System.out.println("\n");
		model.install(summer);
		System.out.println(model.totalValOfInstalledFeatures(price)+"  ==  "+model.eval(priceOfBike));
		System.out.println(model.eval(twiceTotalPrice));
		System.out.println("\n");
		
		model.install(diamond);
		System.out.println(model.totalValOfInstalledFeatures(price)+"  == "+model.eval(priceOfBike));
		System.out.println(model.eval(twiceTotalPrice));
		System.out.println("\n");
		
		System.out.println();
		
		printAllCommitments(model);
	}
	
	public static void test1() throws Z3Exception{

		IProcess p1 = new Prefix(1, new NormalAction("a"), new SideEffect[]{},ZeroProcess.ZERO);
		IProcess p2 = new Prefix(2, new NormalAction("b"), new SideEffect[]{},ZeroProcess.ZERO);
		IProcess p3 = new Prefix(3, new NormalAction("c"), new SideEffect[]{},ZeroProcess.ZERO);
		IProcess p4 = new Prefix(3, new NormalAction("d"), new SideEffect[]{},ZeroProcess.ZERO);
		IProcess p5 = new Prefix(3, new NormalAction("e"), new SideEffect[]{},ZeroProcess.ZERO);

		IProcess root;

		root = new Choice(p1, p2);
		printAllCommitments(root);

		root = new Parallel(p1, p2);
		printAllCommitments(root);

		root = new Sequential(p1, p2);
		printAllCommitments(root);

		root = new Parallel(p1, new Choice(p2,p3));
		printAllCommitments(root);

		root = new Parallel(p1, new Parallel(p2,p3));
		printAllCommitments(root);

		root = new Parallel(p1, new Parallel(p2,new Choice(p3,new Sequential(p4,p5))));
		printAllCommitments(root);
		
		root = new Parallel(p1, ZeroProcess.ZERO);
		printAllCommitments(root);
	}
	
	
	public static void printAllCommitments(QFlanModel model) throws Z3Exception{
		System.out.println("\n");
		System.out.println(model);
		System.out.println("Commitments:");
		for (ICommitment commitment : model.getProcess().getCommitments()) {
			if(commitment.isAllowed(model)){
				System.out.println("\t"+commitment);
			}
		}
	}
	
	public static void printAllCommitments(IProcess process) throws Z3Exception{
		System.out.println("\n");
		System.out.println(process);
		for (ICommitment commitment : process.getCommitments()) {
			if(commitment.isAllowed(null)){
				System.out.println("\t"+commitment);
			}
		}
	}

}
