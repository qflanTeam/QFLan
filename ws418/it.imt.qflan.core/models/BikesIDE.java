import java.util.Arrays;

import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.features.*;
import it.imt.qflan.core.model.*;
import it.imt.qflan.core.predicates.*;
import it.imt.qflan.core.predicates.interfaces.*;
import it.imt.qflan.core.processes.*;
import it.imt.qflan.core.processes.constraints.*;
import it.imt.qflan.core.processes.actions.*;

public class BikesIDE implements IQFlanModelBuilder{

	public BikesIDE(){
		System.out.println("Model builder instantiated");
	}


	public void print(){
		System.out.println("print!");
	}


	public QFlanModel createModel() throws Z3Exception{
		QFlanModel model = new QFlanModel();

		//////////////////
		/////Features/////
		//////////////////
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
		ConcreteFeature Trashed = new ConcreteFeature("Trashed");
		model.addConcreteFeatureDefinition(Trashed);

		Bike.addSon(Wheels);
		Bike.addSon(Light);
		Bike.addSon(Energy);
		Bike.addSon(Engine);
		Bike.addSon(CompUnit);
		Bike.addSon(Basket);
		Bike.addSon(Frame);

		Wheels.addSon(AllYear);
		Wheels.addSon(Summer);
		Wheels.addSon(Winter);

		Energy.addSon(Dynamo);
		Energy.addSon(Battery);

		CompUnit.addSon(Tablet);
		CompUnit.addSon(GPS);

		Frame.addSon(Diamond);
		Frame.addSon(StepThru);

		Tablet.addSon(MapsApp);
		Tablet.addSon(NaviApp);
		Tablet.addSon(GuideApp);
		Tablet.addSon(Music);


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


		///////////////
		////Actions////
		///////////////
		NormalAction sell = new NormalAction("sell");
		model.addNormalAction(sell);NormalAction irreparable = new NormalAction("irreparable");
		model.addNormalAction(irreparable);NormalAction maintain = new NormalAction("maintain");
		model.addNormalAction(maintain);NormalAction book = new NormalAction("book");
		model.addNormalAction(book);NormalAction stop = new NormalAction("stop");
		model.addNormalAction(stop);NormalAction breakAction = new NormalAction("breakAction");
		model.addNormalAction(breakAction);NormalAction start = new NormalAction("start");
		model.addNormalAction(start);NormalAction assistance = new NormalAction("assistance");
		model.addNormalAction(assistance);NormalAction deploy = new NormalAction("deploy");
		model.addNormalAction(deploy);

		///////////////////
		////Constraints////
		///////////////////
		model.addConstraint(new BooleanConstraintExpr(new FeatureSetConstraint(Arrays.asList(AllYear,Summer,Winter), FeatureSetCondition.ATLEASTONE,model),new FeatureSetConstraint(Arrays.asList(AllYear,Summer,Winter), FeatureSetCondition.ATMOSTONE,model),BooleanConnector.AND));
		model.addConstraint(new BooleanConstraintExpr(new FeatureSetConstraint(Arrays.asList(Diamond,StepThru), FeatureSetCondition.ATLEASTONE,model),new FeatureSetConstraint(Arrays.asList(Diamond,StepThru), FeatureSetCondition.ATMOSTONE,model),BooleanConnector.AND));
		model.addConstraint(new FeatureRequireConstraint(Engine, Battery,model));
		model.addConstraint(new FeatureRequireConstraint(MapsApp, Battery,model));
		model.addConstraint(new FeatureRequireConstraint(NaviApp, Battery,model));
		model.addConstraint(new FeatureRequireConstraint(GuideApp, Battery,model));
		model.addConstraint(new FeatureRequireConstraint(Music, Battery,model));
		model.addConstraint(new FeatureRequireConstraint(GPS, Battery,model));
		model.addConstraint(new BooleanConstraintExpr(new FeatureRequireConstraint(Light, Battery,model),new FeatureRequireConstraint(Light, Dynamo,model),BooleanConnector.OR));
		model.addConstraint(new FeatureRequireConstraint(NaviApp, MapsApp,model));
		model.addConstraint(new FeatureSetConstraint(Arrays.asList(GPS,Diamond), FeatureSetCondition.ATMOSTONE,model));
		model.addConstraint(new ActionRequiresConstraint(sell, new DisequationOfPredicateExpressions(new Predicate(price, Bike),new Constant(250.0),PredicateExprComparator.GE)));
		model.addConstraint(new ActionRequiresConstraint(irreparable, new DisequationOfPredicateExpressions(new Predicate(price, Bike),new Constant(400.0),PredicateExprComparator.LE)));
		model.addConstraint(new DisequationOfPredicateExpressions(new Predicate(price, Bike),new Constant(800.0),PredicateExprComparator.LE));
		model.addConstraint(new DisequationOfPredicateExpressions(new Predicate(weight, Bike),new Constant(20.0),PredicateExprComparator.LE));
		model.addConstraint(new DisequationOfPredicateExpressions(new Predicate(load, Bike),new Constant(100.0),PredicateExprComparator.LE));

		//In order to use a feature, it must be installed. These constraints are already built-in

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
		ProcessDefinition trash = new ProcessDefinition("trash");

		model.addProcessDefinition(factory, new Choice(factoryInstallFeatures, new Prefix(8.0, sell, deposit)));
		model.addProcessDefinition(factoryInstallFeatures, new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Prefix(6.0, new InstallAction(GPS,true), factory), new Prefix(10.0, new InstallAction(MapsApp,true), factory)), new Prefix(6.0, new InstallAction(NaviApp,true), factory)), new Prefix(3.0, new InstallAction(GuideApp,true), factory)), new Prefix(20.0, new InstallAction(Music,true), factory)), new Prefix(4.0, new InstallAction(Engine,true), factory)), new Prefix(4.0, new InstallAction(Battery,true), factory)), new Prefix(10.0, new InstallAction(Dynamo,true), factory)), new Prefix(10.0, new InstallAction(Light,true), factory)), new Prefix(8.0, new InstallAction(Basket,true), factory)), new Prefix(5.0, new ReplaceAction(AllYear,Summer), factory)), new Prefix(5.0, new ReplaceAction(AllYear,Winter), factory)), new Prefix(10.0, new ReplaceAction(Summer,AllYear), factory)), new Prefix(5.0, new ReplaceAction(Summer,Winter), factory)), new Prefix(5.0, new ReplaceAction(Winter,Summer), factory)), new Prefix(10.0, new ReplaceAction(Winter,AllYear), factory)), new Prefix(3.0, new ReplaceAction(Diamond,StepThru), factory)), new Prefix(3.0, new ReplaceAction(StepThru,Diamond), factory)));
		model.addProcessDefinition(deposit, new Choice(depositUpdateFeatures, new Prefix(10.0, deploy, parked)));
		model.addProcessDefinition(depositUpdateFeatures, new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Prefix(6.0, new InstallAction(GPS,true), deposit), new Prefix(10.0, new InstallAction(MapsApp,true), deposit)), new Prefix(6.0, new InstallAction(NaviApp,true), deposit)), new Prefix(3.0, new InstallAction(GuideApp,true), deposit)), new Prefix(20.0, new InstallAction(Music,true), deposit)), new Prefix(4.0, new InstallAction(Engine,true), deposit)), new Prefix(4.0, new InstallAction(Battery,true), deposit)), new Prefix(10.0, new InstallAction(Dynamo,true), deposit)), new Prefix(10.0, new InstallAction(Light,true), deposit)), new Prefix(8.0, new InstallAction(Basket,true), deposit)), new Prefix(6.0, new InstallAction(GPS,false), deposit)), new Prefix(10.0, new InstallAction(MapsApp,false), deposit)), new Prefix(6.0, new InstallAction(NaviApp,false), deposit)), new Prefix(3.0, new InstallAction(GuideApp,false), deposit)), new Prefix(20.0, new InstallAction(Music,false), deposit)), new Prefix(1.0, new InstallAction(Engine,false), deposit)), new Prefix(2.0, new InstallAction(Battery,false), deposit)), new Prefix(3.0, new InstallAction(Dynamo,false), deposit)), new Prefix(10.0, new InstallAction(Light,false), deposit)), new Prefix(8.0, new InstallAction(Basket,false), deposit)), new Prefix(5.0, new ReplaceAction(AllYear,Summer), deposit)), new Prefix(5.0, new ReplaceAction(AllYear,Winter), deposit)), new Prefix(10.0, new ReplaceAction(Summer,AllYear), deposit)), new Prefix(5.0, new ReplaceAction(Summer,Winter), deposit)), new Prefix(5.0, new ReplaceAction(Winter,Summer), deposit)), new Prefix(10.0, new ReplaceAction(Winter,AllYear), deposit)), new Prefix(1.0, new ReplaceAction(Battery,Dynamo), deposit)));
		model.addProcessDefinition(parked, new Choice(new Prefix(10.0, book, moving), new Prefix(1.0, maintain, deposit)));
		model.addProcessDefinition(moving, new Choice(new Choice(new Choice(new Prefix(5.0, stop, halted), new Prefix(1.0, breakAction, broken)), new Prefix(20.0, Music, moving)), new Prefix(20.0, Light, moving)));
		model.addProcessDefinition(halted, new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Prefix(5.0, start, moving), new Prefix(1.0, breakAction, broken)), new Prefix(20.0, Music, halted)), new Prefix(10.0, GPS, halted)), new Prefix(10.0, GuideApp, halted)), new Prefix(10.0, MapsApp, halted)), new Prefix(10.0, NaviApp, halted)), new Prefix(10.0, Light, halted)));
		model.addProcessDefinition(broken, new Choice(new Prefix(10.0, assistance, deposit), new Prefix(1.0, irreparable, trash)));
		model.addProcessDefinition(trash, new Prefix(1.0, new InstallAction(Trashed,true), ZeroProcess.ZERO));

		/////////////////////////////////////////////////
		////Initially installed features and Process ////
		/////////////////////////////////////////////////
		model.setInitialState(Arrays.asList(Diamond,AllYear), factory);
		model.resetToInitialState();
		return model;
	}
}
