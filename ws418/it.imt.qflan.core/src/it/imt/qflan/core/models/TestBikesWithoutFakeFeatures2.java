package it.imt.qflan.core.models;

import java.util.Arrays;

import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.features.AbstractFeature;
import it.imt.qflan.core.features.ConcreteFeature;
import it.imt.qflan.core.model.IQFlanModelBuilder;
import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.predicates.ArithmeticOperation;
import it.imt.qflan.core.predicates.ArithmeticPredicateExpr;
import it.imt.qflan.core.predicates.Constant;
import it.imt.qflan.core.predicates.Predicate;
import it.imt.qflan.core.predicates.PredicateDef;
import it.imt.qflan.core.predicates.interfaces.IPredicateDef;
import it.imt.qflan.core.processes.Choice;
import it.imt.qflan.core.processes.Prefix;
import it.imt.qflan.core.processes.ProcessDefinition;
import it.imt.qflan.core.processes.ZeroProcess;
import it.imt.qflan.core.processes.actions.InstallAction;
import it.imt.qflan.core.processes.actions.NormalAction;
import it.imt.qflan.core.processes.actions.ReplaceAction;
import it.imt.qflan.core.processes.constraints.ActionRequiresConstraint;
import it.imt.qflan.core.processes.constraints.Alternative_ORCondition;
import it.imt.qflan.core.processes.constraints.Alternative_OrConstraint;
import it.imt.qflan.core.processes.constraints.DisequationOfPredicateExpressions;
import it.imt.qflan.core.processes.constraints.FeatureRequireConstraint;
import it.imt.qflan.core.processes.constraints.FeatureSetCondition;
import it.imt.qflan.core.processes.constraints.FeatureSetConstraint;
import it.imt.qflan.core.processes.constraints.HasFeature;
import it.imt.qflan.core.processes.constraints.PredicateExprComparator;
import it.imt.qflan.core.variables.QFLanVariable;
import it.imt.qflan.core.variables.SideEffect;

public class TestBikesWithoutFakeFeatures2 implements IQFlanModelBuilder{
	
	public TestBikesWithoutFakeFeatures2(){
		System.out.println("Model builder instantiated");
	}

	public QFlanModel createModel() throws Z3Exception{
		QFlanModel model = new QFlanModel();
			
		//////////////////
		/////Variables////
		//////////////////
		QFLanVariable deploys = model.addVariable("deploys", new Constant(0.0));
		QFLanVariable trashed = model.addVariable("trashed", new Constant(0.0));
			
		//////////////////
		/////Features/////
		//////////////////
		//ABSTRACT FEATURES
		AbstractFeature Bike = new AbstractFeature("Bike");
		model.addAbstractFeatureDefinition(Bike);
		AbstractFeature Wheels = new AbstractFeature("Wheels");
		model.addAbstractFeatureDefinition(Wheels);
		AbstractFeature Energy = new AbstractFeature("Energy");
		model.addAbstractFeatureDefinition(Energy);
		AbstractFeature CompUnit = new AbstractFeature("CompUnit");
		model.addAbstractFeatureDefinition(CompUnit);
		AbstractFeature Frame = new AbstractFeature("Frame");
		model.addAbstractFeatureDefinition(Frame);
		AbstractFeature Tablet = new AbstractFeature("Tablet");
		model.addAbstractFeatureDefinition(Tablet);
		
		//CONCRETE FEATURES
		ConcreteFeature AllYear = new ConcreteFeature("AllYear");
		model.addConcreteFeatureDefinition(AllYear);
		ConcreteFeature Summer = new ConcreteFeature("Summer");
		model.addConcreteFeatureDefinition(Summer);
		ConcreteFeature Winter = new ConcreteFeature("Winter");
		model.addConcreteFeatureDefinition(Winter);
		ConcreteFeature Light = new ConcreteFeature("Light");
		model.addConcreteFeatureDefinition(Light);
		ConcreteFeature Dynamo = new ConcreteFeature("Dynamo");
		model.addConcreteFeatureDefinition(Dynamo);
		ConcreteFeature Battery = new ConcreteFeature("Battery");
		model.addConcreteFeatureDefinition(Battery);
		ConcreteFeature Engine = new ConcreteFeature("Engine");
		model.addConcreteFeatureDefinition(Engine);
		ConcreteFeature MapsApp = new ConcreteFeature("MapsApp");
		model.addConcreteFeatureDefinition(MapsApp);
		ConcreteFeature NaviApp = new ConcreteFeature("NaviApp");
		model.addConcreteFeatureDefinition(NaviApp);
		ConcreteFeature GuideApp = new ConcreteFeature("GuideApp");
		model.addConcreteFeatureDefinition(GuideApp);
		ConcreteFeature Music = new ConcreteFeature("Music");
		model.addConcreteFeatureDefinition(Music);
		ConcreteFeature GPS = new ConcreteFeature("GPS");
		model.addConcreteFeatureDefinition(GPS);
		ConcreteFeature Basket = new ConcreteFeature("Basket");
		model.addConcreteFeatureDefinition(Basket);
		ConcreteFeature Diamond = new ConcreteFeature("Diamond");
		model.addConcreteFeatureDefinition(Diamond);
		ConcreteFeature StepThru = new ConcreteFeature("StepThru");
		model.addConcreteFeatureDefinition(StepThru);
		
		//DIAGRAM
		//Normal relation
		Bike.addSon(Wheels);
		Bike.addSon(Light);
		Bike.addSon(Energy);
		Bike.addSon(Engine);
		Bike.addSon(CompUnit);
		Bike.addSon(Basket);
		Bike.addSon(Frame);
		
		//XOR relation
		Wheels.addSon(AllYear);
		Wheels.addSon(Summer);
		Wheels.addSon(Winter);
		
		//OR relation
		Energy.addSon(Dynamo);
		Energy.addSon(Battery);
		
		//Normal relation
		CompUnit.addSon(Tablet);
		CompUnit.addSon(GPS);
		
		//XOR relation
		Frame.addSon(Diamond);
		Frame.addSon(StepThru);
		
		//Normal relation
		Tablet.addSon(MapsApp);
		Tablet.addSon(NaviApp);
		Tablet.addSon(GuideApp);
		Tablet.addSon(Music);
		
		model.computeDescendantsAndAncestors();
		
		//Hierarchical constraints
		model.addConstraint(new HasFeature(Wheels,model,true));
		Wheels.setOptional(false);
		model.addConstraint(new HasFeature(Frame,model,true));
		Frame.setOptional(false);
		
		model.addConstraint(new Alternative_OrConstraint(Wheels,Arrays.asList(AllYear,Summer,Winter), Alternative_ORCondition.XOR, model));
		//model.addConstraint(new BooleanConstraintExpr(new FeatureSetConstraint(Arrays.asList(AllYear,Summer,Winter), FeatureSetCondition.ATLEASTONE,model),new FeatureSetConstraint(Arrays.asList(AllYear,Summer,Winter), FeatureSetCondition.ATMOSTONE,model),BooleanConnector.AND));
		model.addConstraint(new Alternative_OrConstraint(Energy,Arrays.asList(Dynamo,Battery), Alternative_ORCondition.OR,model));
		//model.addConstraint(new FeatureSetConstraint(Arrays.asList(Dynamo,Battery), FeatureSetCondition.ATLEASTONE,model));
		model.addConstraint(new Alternative_OrConstraint(Frame,Arrays.asList(Diamond,StepThru), Alternative_ORCondition.XOR, model));
		//model.addConstraint(new BooleanConstraintExpr(new FeatureSetConstraint(Arrays.asList(Diamond,StepThru), FeatureSetCondition.ATLEASTONE,model),new FeatureSetConstraint(Arrays.asList(Diamond,StepThru), FeatureSetCondition.ATMOSTONE,model),BooleanConnector.AND));
			
		/////////////////////////////
		////CrossTree Constraints////
		/////////////////////////////
		model.addConstraint(new FeatureRequireConstraint(Engine, Battery,model));
		model.addConstraint(new FeatureRequireConstraint(CompUnit, Battery,model));
		model.addConstraint(new FeatureRequireConstraint(Light, Energy,model));
		model.addConstraint(new FeatureRequireConstraint(NaviApp, MapsApp,model));
		model.addConstraint(new FeatureSetConstraint(Arrays.asList(GPS,Diamond), FeatureSetCondition.ATMOSTONE,model));
		
			
		//////////////////
		////Predicates////
		//////////////////
		IPredicateDef price = new PredicateDef("price");
		model.addPredicateDef(price);
		price.setValue(AllYear,100.0);
		price.setValue(Summer,70.0);
		price.setValue(Winter,80.0);
		price.setValue(Light,15.0);
		price.setValue(Dynamo,40.0);
		price.setValue(Battery,150.0);
		price.setValue(Engine,300.0);
		price.setValue(MapsApp,10.0);
		price.setValue(NaviApp,20.0);
		price.setValue(GuideApp,10.0);
		price.setValue(Music,10.0);
		price.setValue(GPS,20.0);
		price.setValue(Basket,8.0);
		price.setValue(Diamond,100.0);
		price.setValue(StepThru,90.0);
		
		IPredicateDef weight = new PredicateDef("weight");
		model.addPredicateDef(weight);
		weight.setValue(AllYear,0.3);
		weight.setValue(Summer,0.2);
		weight.setValue(Winter,0.4);
		weight.setValue(Light,0.1);
		weight.setValue(Dynamo,0.1);
		weight.setValue(Battery,3.0);
		weight.setValue(Engine,10.0);
		weight.setValue(Basket,0.5);
		weight.setValue(Diamond,5.0);
		weight.setValue(StepThru,3.5);
		
		IPredicateDef load = new PredicateDef("load");
		model.addPredicateDef(load);
		load.setValue(MapsApp,25.0);
		load.setValue(NaviApp,55.0);
		load.setValue(GuideApp,30.0);
		load.setValue(Music,5.0);
		load.setValue(GPS,10.0);
		
		
		////////////////////////////////
		////Quantitative Constraints////
		////////////////////////////////
		model.addConstraint(new DisequationOfPredicateExpressions(new Predicate(price, Bike),new Constant(800.0),PredicateExprComparator.LE));
		model.addConstraint(new DisequationOfPredicateExpressions(new Predicate(weight, Bike),new Constant(20.0),PredicateExprComparator.LE));
		model.addConstraint(new DisequationOfPredicateExpressions(new Predicate(load, Bike),new Constant(100.0),PredicateExprComparator.LEQ));
		
		
		///////////////
		////Actions////
		///////////////
		NormalAction sell = new NormalAction("sell");
		model.addNormalAction(sell);
		NormalAction irreparable = new NormalAction("irreparable");
		model.addNormalAction(irreparable);
		NormalAction maintain = new NormalAction("maintain");
		model.addNormalAction(maintain);
		NormalAction book = new NormalAction("book");
		model.addNormalAction(book);
		NormalAction stop = new NormalAction("stop");
		model.addNormalAction(stop);
		NormalAction breakAction = new NormalAction("breakAction");
		model.addNormalAction(breakAction);
		NormalAction start = new NormalAction("start");
		model.addNormalAction(start);
		NormalAction assistance = new NormalAction("assistance");
		model.addNormalAction(assistance);
		NormalAction deploy = new NormalAction("deploy");
		model.addNormalAction(deploy);
		
		//////////////////////////
		////Action constraints////
		//////////////////////////
		model.addActionConstraint(new ActionRequiresConstraint(sell, new DisequationOfPredicateExpressions(new Predicate(price, Bike),new Constant(250.0),PredicateExprComparator.GE)));
		model.addActionConstraint(new ActionRequiresConstraint(irreparable, new DisequationOfPredicateExpressions(new Predicate(price, Bike),new Constant(400.0),PredicateExprComparator.LE)));
		
		//In order to use a feature, it must be installed. These constraints are already built-in
		//( do(AllYear) -> has(AllYear))
		//( do(Summer) -> has(Summer))
		//( do(Winter) -> has(Winter))
		//( do(Light) -> has(Light))
		//( do(Dynamo) -> has(Dynamo))
		//( do(Battery) -> has(Battery))
		//( do(Engine) -> has(Engine))
		//( do(MapsApp) -> has(MapsApp))
		//( do(NaviApp) -> has(NaviApp))
		//( do(GuideApp) -> has(GuideApp))
		//( do(Music) -> has(Music))
		//( do(GPS) -> has(GPS))
		//( do(Basket) -> has(Basket))
		//( do(Diamond) -> has(Diamond))
		//( do(StepThru) -> has(StepThru))
		
		/////////////////////////////
		////Processes definitions////
		/////////////////////////////
		ProcessDefinition factory = new ProcessDefinition("factory");
		ProcessDefinition factoryInstallFeatures = new ProcessDefinition("factoryInstallFeatures");
		ProcessDefinition deposit = new ProcessDefinition("deposit");
		ProcessDefinition depositUpdateFeatures = new ProcessDefinition("depositUpdateFeatures");
		ProcessDefinition parked = new ProcessDefinition("parked");
		ProcessDefinition moving = new ProcessDefinition("moving");
		ProcessDefinition halted = new ProcessDefinition("halted");
		ProcessDefinition broken = new ProcessDefinition("broken");
		
		model.addProcessDefinition(factory, new Choice(factoryInstallFeatures, new Prefix(8.0, sell, new SideEffect[]{}, deposit)));
		model.addProcessDefinition(factoryInstallFeatures, new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Prefix(6.0, new InstallAction(GPS,true), new SideEffect[]{}, factory), new Prefix(10.0, new InstallAction(MapsApp,true), new SideEffect[]{}, factory)), new Prefix(6.0, new InstallAction(NaviApp,true), new SideEffect[]{}, factory)), new Prefix(3.0, new InstallAction(GuideApp,true), new SideEffect[]{}, factory)), new Prefix(20.0, new InstallAction(Music,true), new SideEffect[]{}, factory)), new Prefix(4.0, new InstallAction(Engine,true), new SideEffect[]{}, factory)), new Prefix(4.0, new InstallAction(Battery,true), new SideEffect[]{}, factory)), new Prefix(10.0, new InstallAction(Dynamo,true), new SideEffect[]{}, factory)), new Prefix(10.0, new InstallAction(Light,true), new SideEffect[]{}, factory)), new Prefix(8.0, new InstallAction(Basket,true), new SideEffect[]{}, factory)), new Prefix(5.0, new ReplaceAction(AllYear,Summer), new SideEffect[]{}, factory)), new Prefix(5.0, new ReplaceAction(AllYear,Winter), new SideEffect[]{}, factory)), new Prefix(10.0, new ReplaceAction(Summer,AllYear), new SideEffect[]{}, factory)), new Prefix(5.0, new ReplaceAction(Summer,Winter), new SideEffect[]{}, factory)), new Prefix(5.0, new ReplaceAction(Winter,Summer), new SideEffect[]{}, factory)), new Prefix(10.0, new ReplaceAction(Winter,AllYear), new SideEffect[]{}, factory)), new Prefix(3.0, new ReplaceAction(Diamond,StepThru), new SideEffect[]{}, factory)), new Prefix(3.0, new ReplaceAction(StepThru,Diamond), new SideEffect[]{}, factory)));
		model.addProcessDefinition(deposit, new Choice(depositUpdateFeatures, new Prefix(10.0, deploy, new SideEffect[]{new SideEffect(deploys,new ArithmeticPredicateExpr(deploys,new Constant(1.0),ArithmeticOperation.SUM))}, parked)));
		model.addProcessDefinition(depositUpdateFeatures, new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Prefix(6.0, new InstallAction(GPS,true), new SideEffect[]{}, deposit), new Prefix(10.0, new InstallAction(MapsApp,true), new SideEffect[]{}, deposit)), new Prefix(6.0, new InstallAction(NaviApp,true), new SideEffect[]{}, deposit)), new Prefix(3.0, new InstallAction(GuideApp,true), new SideEffect[]{}, deposit)), new Prefix(20.0, new InstallAction(Music,true), new SideEffect[]{}, deposit)), new Prefix(4.0, new InstallAction(Engine,true), new SideEffect[]{}, deposit)), new Prefix(4.0, new InstallAction(Battery,true), new SideEffect[]{}, deposit)), new Prefix(10.0, new InstallAction(Dynamo,true), new SideEffect[]{}, deposit)), new Prefix(10.0, new InstallAction(Light,true), new SideEffect[]{}, deposit)), new Prefix(8.0, new InstallAction(Basket,true), new SideEffect[]{}, deposit)), new Prefix(6.0, new InstallAction(GPS,false), new SideEffect[]{}, deposit)), new Prefix(10.0, new InstallAction(MapsApp,false), new SideEffect[]{}, deposit)), new Prefix(6.0, new InstallAction(NaviApp,false), new SideEffect[]{}, deposit)), new Prefix(3.0, new InstallAction(GuideApp,false), new SideEffect[]{}, deposit)), new Prefix(20.0, new InstallAction(Music,false), new SideEffect[]{}, deposit)), new Prefix(1.0, new InstallAction(Engine,false), new SideEffect[]{}, deposit)), new Prefix(2.0, new InstallAction(Battery,false), new SideEffect[]{}, deposit)), new Prefix(3.0, new InstallAction(Dynamo,false), new SideEffect[]{}, deposit)), new Prefix(10.0, new InstallAction(Light,false), new SideEffect[]{}, deposit)), new Prefix(8.0, new InstallAction(Basket,false), new SideEffect[]{}, deposit)), new Prefix(5.0, new ReplaceAction(AllYear,Summer), new SideEffect[]{}, deposit)), new Prefix(5.0, new ReplaceAction(AllYear,Winter), new SideEffect[]{}, deposit)), new Prefix(10.0, new ReplaceAction(Summer,AllYear), new SideEffect[]{}, deposit)), new Prefix(5.0, new ReplaceAction(Summer,Winter), new SideEffect[]{}, deposit)), new Prefix(5.0, new ReplaceAction(Winter,Summer), new SideEffect[]{}, deposit)), new Prefix(10.0, new ReplaceAction(Winter,AllYear), new SideEffect[]{}, deposit)), new Prefix(1.0, new ReplaceAction(Battery,Dynamo), new SideEffect[]{}, deposit)));
		model.addProcessDefinition(parked, new Choice(new Prefix(10.0, book, new SideEffect[]{}, moving), new Prefix(1.0, maintain, new SideEffect[]{}, deposit)));
		model.addProcessDefinition(moving, new Choice(new Choice(new Choice(new Prefix(5.0, stop, new SideEffect[]{}, halted), new Prefix(1.0, breakAction, new SideEffect[]{}, broken)), new Prefix(20.0, Music, new SideEffect[]{}, moving)), new Prefix(20.0, Light, new SideEffect[]{}, moving)));
		model.addProcessDefinition(halted, new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Prefix(5.0, start, new SideEffect[]{}, moving), new Prefix(1.0, breakAction, new SideEffect[]{}, broken)), new Prefix(20.0, Music, new SideEffect[]{}, halted)), new Prefix(10.0, GPS, new SideEffect[]{}, halted)), new Prefix(10.0, GuideApp, new SideEffect[]{}, halted)), new Prefix(10.0, MapsApp, new SideEffect[]{}, halted)), new Prefix(10.0, NaviApp, new SideEffect[]{}, halted)), new Prefix(10.0, Light, new SideEffect[]{}, halted)));
		model.addProcessDefinition(broken, new Choice(new Prefix(10.0, assistance, new SideEffect[]{}, deposit), new Prefix(1.0, irreparable, new SideEffect[]{new SideEffect(trashed,new Constant(1.0))}, ZeroProcess.ZERO)));
		
		/////////////////////////////////////////////////
		////Initially installed features and Process ////
		/////////////////////////////////////////////////
		model.setInitialState(Arrays.asList(Diamond,AllYear), factory);
		model.resetToInitialState();
		return model;		
	}
}
		
		
