package it.imt.qflan.core.models;

import java.util.Arrays;

import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.features.*;
import it.imt.qflan.core.model.*;
import it.imt.qflan.core.predicates.*;
import it.imt.qflan.core.predicates.interfaces.*;
import it.imt.qflan.core.processes.*;
import it.imt.qflan.core.processes.constraints.*;
import it.imt.qflan.core.processes.actions.*;
import it.imt.qflan.core.variables.QFLanVariable;
import it.imt.qflan.core.variables.SideEffect;
			
public class Elevator2 implements IQFlanModelBuilder{
	
	public Elevator2(){
			System.out.println("Model builder instantiated");
		}
	
	public QFlanModel createModel() throws Z3Exception{
		QFlanModel model = new QFlanModel();
			
		//////////////////
		/////Variables////
		//////////////////
		QFLanVariable top = model.addVariable("top", new Constant(4.0));
		QFLanVariable bottom = model.addVariable("bottom", new Constant(0.0));
		QFLanVariable floor = model.addVariable("floor", new Constant(0.0));
		QFLanVariable door = model.addVariable("door", new Constant(0.0));
		QFLanVariable capacity = model.addVariable("capacity", new Constant(8.0));
		QFLanVariable maxLoad = model.addVariable("maxLoad", new Constant(4.0));
		QFLanVariable load = model.addVariable("load", new Constant(0.0));
		QFLanVariable direction = model.addVariable("direction", new Constant(0.0));
		QFLanVariable goal = model.addVariable("goal", new Constant(0.0));
		QFLanVariable buttonL0 = model.addVariable("buttonL0", new Constant(0.0));
		QFLanVariable buttonL1 = model.addVariable("buttonL1", new Constant(0.0));
		QFLanVariable buttonL2 = model.addVariable("buttonL2", new Constant(0.0));
		QFLanVariable buttonL3 = model.addVariable("buttonL3", new Constant(0.0));
		QFLanVariable buttonL4 = model.addVariable("buttonL4", new Constant(0.0));
		QFLanVariable buttonF0 = model.addVariable("buttonF0", new Constant(0.0));
		QFLanVariable buttonF1 = model.addVariable("buttonF1", new Constant(0.0));
		QFLanVariable buttonF2 = model.addVariable("buttonF2", new Constant(0.0));
		QFLanVariable buttonF3 = model.addVariable("buttonF3", new Constant(0.0));
		QFLanVariable buttonF4 = model.addVariable("buttonF4", new Constant(0.0));
			
		//////////////////
		/////Features/////
		//////////////////
		AbstractFeature Elevator = new AbstractFeature("Elevator");
		model.addAbstractFeatureDefinition(Elevator);
		
		ConcreteFeature AntiPrank = new ConcreteFeature("AntiPrank");
		model.addConcreteFeatureDefinition(AntiPrank);
		ConcreteFeature Empty = new ConcreteFeature("Empty");
		model.addConcreteFeatureDefinition(Empty);
		ConcreteFeature Executive = new ConcreteFeature("Executive");
		model.addConcreteFeatureDefinition(Executive);
		ConcreteFeature OpenIfIdle = new ConcreteFeature("OpenIfIdle");
		model.addConcreteFeatureDefinition(OpenIfIdle);
		ConcreteFeature Overload = new ConcreteFeature("Overload");
		model.addConcreteFeatureDefinition(Overload);
		ConcreteFeature Parking = new ConcreteFeature("Parking");
		model.addConcreteFeatureDefinition(Parking);
		ConcreteFeature QuickClose = new ConcreteFeature("QuickClose");
		model.addConcreteFeatureDefinition(QuickClose);
		ConcreteFeature Shuttle = new ConcreteFeature("Shuttle");
		model.addConcreteFeatureDefinition(Shuttle);
		ConcreteFeature AlmostFull = new ConcreteFeature("AlmostFull");
		model.addConcreteFeatureDefinition(AlmostFull);
		
		Elevator.addSon(AntiPrank);
		Elevator.addSon(Empty);
		Elevator.addSon(Executive);
		Elevator.addSon(OpenIfIdle);
		Elevator.addSon(Overload);
		Elevator.addSon(Parking);
		Elevator.addSon(QuickClose);
		Elevator.addSon(Shuttle);
		Elevator.addSon(AlmostFull);
		
			
		//////////////////
		////Predicates////
		//////////////////
		IPredicateDef where = new PredicateDef("where");
		model.addPredicateDef(where);
		where.setValue(Parking,0.0);
		where.setValue(Executive,4.0);
		
		
		///////////////
		////Actions////
		///////////////
		NormalAction up = new NormalAction("up");
		model.addNormalAction(up);
		NormalAction down = new NormalAction("down");
		model.addNormalAction(down);
		NormalAction startUp = new NormalAction("startUp");
		model.addNormalAction(startUp);
		NormalAction startDown = new NormalAction("startDown");
		model.addNormalAction(startDown);
		NormalAction continueACT = new NormalAction("continueACT");
		model.addNormalAction(continueACT);
		NormalAction stop = new NormalAction("stop");
		model.addNormalAction(stop);
		NormalAction reverse = new NormalAction("reverse");
		model.addNormalAction(reverse);
		NormalAction open = new NormalAction("open");
		model.addNormalAction(open);
		NormalAction close = new NormalAction("close");
		model.addNormalAction(close);
		NormalAction pressL = new NormalAction("pressL");
		model.addNormalAction(pressL);
		NormalAction pressF = new NormalAction("pressF");
		model.addNormalAction(pressF);
		NormalAction unpressL = new NormalAction("unpressL");
		model.addNormalAction(unpressL);
		NormalAction unpressF = new NormalAction("unpressF");
		model.addNormalAction(unpressF);
		NormalAction park = new NormalAction("park");
		model.addNormalAction(park);
		NormalAction wakeup = new NormalAction("wakeup");
		model.addNormalAction(wakeup);
		NormalAction clean = new NormalAction("clean");
		model.addNormalAction(clean);
		NormalAction enter = new NormalAction("enter");
		model.addNormalAction(enter);
		NormalAction leave = new NormalAction("leave");
		model.addNormalAction(leave);
		
		//////////////////////////
		////Action constraints////
		//////////////////////////
		model.addActionConstraint(new ActionRequiresConstraint(open, new BooleanConstraintExpr(new DisequationOfPredicateExpressions(door,new Constant(0.0),PredicateExprComparator.EQ),new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(4.0),PredicateExprComparator.EQ),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL4,new Constant(1.0),PredicateExprComparator.EQ),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonF4,new Constant(1.0),PredicateExprComparator.EQ),new BooleanConstraintExpr(new NotConstraintExpr(new HasFeature(AlmostFull,model)),new HasFeature(Executive,model),BooleanConnector.OR),BooleanConnector.AND),BooleanConnector.OR),BooleanConnector.AND),new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(3.0),PredicateExprComparator.EQ),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL4,new Constant(0.0),PredicateExprComparator.EQ),new NotConstraintExpr(new HasFeature(Executive,model)),BooleanConnector.OR),BooleanConnector.AND),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL3,new Constant(1.0),PredicateExprComparator.EQ),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonF3,new Constant(1.0),PredicateExprComparator.EQ),new NotConstraintExpr(new HasFeature(AlmostFull,model)),BooleanConnector.AND),BooleanConnector.OR),BooleanConnector.AND),BooleanConnector.OR),new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(2.0),PredicateExprComparator.EQ),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL4,new Constant(0.0),PredicateExprComparator.EQ),new NotConstraintExpr(new HasFeature(Executive,model)),BooleanConnector.OR),BooleanConnector.AND),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL2,new Constant(1.0),PredicateExprComparator.EQ),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonF2,new Constant(1.0),PredicateExprComparator.EQ),new NotConstraintExpr(new HasFeature(AlmostFull,model)),BooleanConnector.AND),BooleanConnector.OR),BooleanConnector.AND),BooleanConnector.OR),new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(1.0),PredicateExprComparator.EQ),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL4,new Constant(0.0),PredicateExprComparator.EQ),new NotConstraintExpr(new HasFeature(Executive,model)),BooleanConnector.OR),BooleanConnector.AND),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL1,new Constant(1.0),PredicateExprComparator.EQ),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonF1,new Constant(1.0),PredicateExprComparator.EQ),new NotConstraintExpr(new HasFeature(AlmostFull,model)),BooleanConnector.AND),BooleanConnector.OR),BooleanConnector.AND),BooleanConnector.OR),new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(0.0),PredicateExprComparator.EQ),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL4,new Constant(0.0),PredicateExprComparator.EQ),new NotConstraintExpr(new HasFeature(Executive,model)),BooleanConnector.OR),BooleanConnector.AND),new BooleanConstraintExpr(new BooleanConstraintExpr(new HasFeature(Parking,model),new DisequationOfPredicateExpressions(buttonL0,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonF0,new Constant(1.0),PredicateExprComparator.EQ),new NotConstraintExpr(new HasFeature(AlmostFull,model)),BooleanConnector.AND),BooleanConnector.OR),BooleanConnector.AND),BooleanConnector.OR),BooleanConnector.AND)));
		model.addActionConstraint(new ActionRequiresConstraint(close, new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(door,new Constant(1.0),PredicateExprComparator.EQ),new BooleanConstraintExpr(new HasFeature(Parking,model),new NotConstraintExpr(new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.EQ)),BooleanConnector.IMPLIES),BooleanConnector.AND),new BooleanConstraintExpr(new HasFeature(Overload,model),new DisequationOfPredicateExpressions(load,maxLoad,PredicateExprComparator.LEQ),BooleanConnector.IMPLIES),BooleanConnector.AND)));
		model.addActionConstraint(new ActionRequiresConstraint(up, new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(direction,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(floor,top,PredicateExprComparator.LE),BooleanConnector.AND),new DisequationOfPredicateExpressions(door,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND)));
		model.addActionConstraint(new ActionRequiresConstraint(down, new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(direction,new ArithmeticPredicateExpr(new Constant(0),new Constant(1.0),ArithmeticOperation.SUB),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(floor,bottom,PredicateExprComparator.GE),BooleanConnector.AND),new DisequationOfPredicateExpressions(door,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND)));
		model.addActionConstraint(new ActionRequiresConstraint(startUp, new DisequationOfPredicateExpressions(floor,goal,PredicateExprComparator.LE)));
		model.addActionConstraint(new ActionRequiresConstraint(startDown, new DisequationOfPredicateExpressions(floor,goal,PredicateExprComparator.GE)));
		model.addActionConstraint(new ActionRequiresConstraint(continueACT, new BooleanConstraintExpr(new BooleanConstraintExpr(new HasFeature(Shuttle,model),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,goal,PredicateExprComparator.LE),new DisequationOfPredicateExpressions(direction,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.AND),BooleanConnector.OR),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,goal,PredicateExprComparator.GE),new DisequationOfPredicateExpressions(direction,new ArithmeticPredicateExpr(new Constant(0),new Constant(1.0),ArithmeticOperation.SUB),PredicateExprComparator.EQ),BooleanConnector.AND),BooleanConnector.OR)));
		model.addActionConstraint(new ActionRequiresConstraint(stop, new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new NotConstraintExpr(new HasFeature(Shuttle,model)),new DisequationOfPredicateExpressions(buttonL0,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL1,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL2,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL3,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL4,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF0,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF1,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF2,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF3,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF4,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND)));
		model.addActionConstraint(new ActionRequiresConstraint(reverse, new BooleanConstraintExpr(new HasFeature(Shuttle,model),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,top,PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(floor,bottom,PredicateExprComparator.EQ),BooleanConnector.OR),BooleanConnector.AND)));
		model.addActionConstraint(new ActionRequiresConstraint(wakeup, new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL0,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(buttonL1,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(buttonL2,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(buttonL3,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(buttonL4,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(buttonF0,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(buttonF1,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(buttonF2,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(buttonF3,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(buttonF4,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR)));
		model.addActionConstraint(new ActionRequiresConstraint(park, new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new HasFeature(Parking,model),new DisequationOfPredicateExpressions(new Predicate(where, Parking),floor,PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL0,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL1,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL2,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL3,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL4,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF0,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF1,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF2,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF3,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF4,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND)));
		model.addActionConstraint(new ActionRequiresConstraint(clean, new BooleanConstraintExpr(new HasFeature(Empty,model),new DisequationOfPredicateExpressions(load,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND)));
		model.addActionConstraint(new ActionRequiresConstraint(pressL, new DisequationOfPredicateExpressions(load,new Constant(0.0),PredicateExprComparator.GE)));
		model.addActionConstraint(new ActionRequiresConstraint(pressF, new TrueConstraint()));
		model.addActionConstraint(new ActionRequiresConstraint(unpressL, new HasFeature(AntiPrank,model)));
		model.addActionConstraint(new ActionRequiresConstraint(unpressF, new HasFeature(AntiPrank,model)));
		model.addActionConstraint(new ActionRequiresConstraint(enter, new BooleanConstraintExpr(new DisequationOfPredicateExpressions(door,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(load,capacity,PredicateExprComparator.LE),BooleanConnector.AND)));
		model.addActionConstraint(new ActionRequiresConstraint(leave, new BooleanConstraintExpr(new DisequationOfPredicateExpressions(door,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(capacity,new Constant(0.0),PredicateExprComparator.GE),BooleanConnector.AND)));
		
		//In order to use a feature, it must be installed. These constraints are already built-in
		//( do(AntiPrank) -> has(AntiPrank))
		//( do(Empty) -> has(Empty))
		//( do(Executive) -> has(Executive))
		//( do(OpenIfIdle) -> has(OpenIfIdle))
		//( do(Overload) -> has(Overload))
		//( do(Parking) -> has(Parking))
		//( do(QuickClose) -> has(QuickClose))
		//( do(Shuttle) -> has(Shuttle))
		//( do(AlmostFull) -> has(AlmostFull))
		
		///////////////////
		////Constraints////
		///////////////////
		model.addConstraint(new DisequationOfPredicateExpressions(bottom,top,PredicateExprComparator.LEQ));
		model.addConstraint(new DisequationOfPredicateExpressions(floor,top,PredicateExprComparator.LEQ));
		model.addConstraint(new DisequationOfPredicateExpressions(floor,bottom,PredicateExprComparator.GEQ));
		model.addConstraint(new DisequationOfPredicateExpressions(maxLoad,capacity,PredicateExprComparator.LE));
		model.addConstraint(new DisequationOfPredicateExpressions(maxLoad,new Constant(0.0),PredicateExprComparator.GE));
		model.addConstraint(new DisequationOfPredicateExpressions(load,new Constant(0.0),PredicateExprComparator.GEQ));
		model.addConstraint(new DisequationOfPredicateExpressions(load,capacity,PredicateExprComparator.LEQ));
		
		
		/////////////////////////////
		////Processes definitions////
		/////////////////////////////
		ProcessDefinition Test = new ProcessDefinition("Test");
		ProcessDefinition Start = new ProcessDefinition("Start");
		ProcessDefinition Lift = new ProcessDefinition("Lift");
		ProcessDefinition LiftTurnButtonDown = new ProcessDefinition("LiftTurnButtonDown");
		ProcessDefinition Controller = new ProcessDefinition("Controller");
		ProcessDefinition ChooseGoal = new ProcessDefinition("ChooseGoal");
		ProcessDefinition ChooseGoalExec = new ProcessDefinition("ChooseGoalExec");
		ProcessDefinition ChooseGoalNonExec = new ProcessDefinition("ChooseGoalNonExec");
		ProcessDefinition ChooseGoalInside = new ProcessDefinition("ChooseGoalInside");
		ProcessDefinition ChooseGoalInAndOut = new ProcessDefinition("ChooseGoalInAndOut");
		ProcessDefinition ChooseDirection = new ProcessDefinition("ChooseDirection");
		ProcessDefinition Buttons = new ProcessDefinition("Buttons");
		ProcessDefinition People = new ProcessDefinition("People");
		
		model.addProcessDefinition(Test, new Prefix(1.0, pressF, new SideEffect[]{new SideEffect(direction,new ArithmeticPredicateExpr(new Predicate(where, Parking),floor,ArithmeticOperation.SUB))}, LiftTurnButtonDown));
		model.addProcessDefinition(Start, new Parallel(new Parallel(new Parallel(Lift, Controller), Buttons), People));
		model.addProcessDefinition(Lift, new Choice(new Choice(new Choice(new Choice(new Prefix(1.0, open, new SideEffect[]{new SideEffect(door,new Constant(1.0))}, LiftTurnButtonDown), new Prefix(1.0, close, new SideEffect[]{new SideEffect(door,new Constant(0.0))}, Lift)), new Prefix(1.0, up, new SideEffect[]{new SideEffect(floor,new ArithmeticPredicateExpr(floor,new Constant(1.0),ArithmeticOperation.SUM))}, Lift)), new Prefix(1.0, down, new SideEffect[]{new SideEffect(floor,new ArithmeticPredicateExpr(floor,new Constant(1.0),ArithmeticOperation.SUB))}, Lift)), new Prefix(100.0, clean, new SideEffect[]{new SideEffect(buttonL0,new Constant(0.0)),new SideEffect(buttonL1,new Constant(0.0)),new SideEffect(buttonL2,new Constant(0.0)),new SideEffect(buttonL3,new Constant(0.0)),new SideEffect(buttonL4,new Constant(0.0))}, Lift)));
		model.addProcessDefinition(LiftTurnButtonDown, new Choice(new Choice(new Choice(new Choice(new Prefix(100.0, new AskAction(new DisequationOfPredicateExpressions(floor,new Constant(0.0),PredicateExprComparator.EQ)), new SideEffect[]{new SideEffect(buttonL0,new Constant(0.0)),new SideEffect(buttonF0,new Constant(0.0))}, Lift), new Prefix(100.0, new AskAction(new DisequationOfPredicateExpressions(floor,new Constant(1.0),PredicateExprComparator.EQ)), new SideEffect[]{new SideEffect(buttonL1,new Constant(0.0)),new SideEffect(buttonF1,new Constant(0.0))}, Lift)), new Prefix(100.0, new AskAction(new DisequationOfPredicateExpressions(floor,new Constant(2.0),PredicateExprComparator.EQ)), new SideEffect[]{new SideEffect(buttonL2,new Constant(0.0)),new SideEffect(buttonF2,new Constant(0.0))}, Lift)), new Prefix(100.0, new AskAction(new DisequationOfPredicateExpressions(floor,new Constant(3.0),PredicateExprComparator.EQ)), new SideEffect[]{new SideEffect(buttonL3,new Constant(0.0)),new SideEffect(buttonF3,new Constant(0.0))}, Lift)), new Prefix(100.0, new AskAction(new DisequationOfPredicateExpressions(floor,new Constant(4.0),PredicateExprComparator.EQ)), new SideEffect[]{new SideEffect(buttonL4,new Constant(0.0)),new SideEffect(buttonF4,new Constant(0.0))}, Lift)));
		model.addProcessDefinition(Controller, new Prefix(100.0, new AskAction(new TrueConstraint()), new SideEffect[]{}, ChooseGoal));
		model.addProcessDefinition(ChooseGoal, new Choice(new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new HasFeature(Parking,model),new DisequationOfPredicateExpressions(buttonL0,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF0,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL1,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF0,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL2,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF0,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL3,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF0,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL4,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF0,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(0.0))}, ChooseDirection), new Prefix(100.0, new AskAction(new NotConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new HasFeature(Parking,model),new DisequationOfPredicateExpressions(buttonL0,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF0,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL1,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF0,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL2,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF0,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL3,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF0,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL4,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonF0,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND))), new SideEffect[]{}, ChooseGoalExec)));
		model.addProcessDefinition(ChooseGoalExec, new Choice(new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new HasFeature(Executive,model),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL4,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(buttonF4,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(4.0))}, ChooseDirection), new Prefix(100.0, new AskAction(new NotConstraintExpr(new BooleanConstraintExpr(new HasFeature(Executive,model),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL4,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(buttonF4,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),BooleanConnector.AND))), new SideEffect[]{}, ChooseGoalNonExec)));
		model.addProcessDefinition(ChooseGoalNonExec, new Choice(new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new HasFeature(AlmostFull,model),new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL0,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(buttonL1,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(buttonL2,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(buttonL3,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(buttonL4,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),BooleanConnector.AND)), new SideEffect[]{}, ChooseGoalInside), new Prefix(100.0, new AskAction(new NotConstraintExpr(new BooleanConstraintExpr(new HasFeature(AlmostFull,model),new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL0,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(buttonL1,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(buttonL2,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(buttonL3,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(buttonL4,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),BooleanConnector.AND))), new SideEffect[]{}, ChooseGoalInAndOut)));
		model.addProcessDefinition(ChooseGoalInside, new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(4.0),PredicateExprComparator.LE),new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.GEQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL4,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(4.0))}, ChooseDirection), new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(3.0),PredicateExprComparator.LE),new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.GEQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL3,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(3.0))}, ChooseDirection)), new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(2.0),PredicateExprComparator.LE),new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.GEQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL2,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(2.0))}, ChooseDirection)), new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(1.0),PredicateExprComparator.LE),new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.GEQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL1,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(1.0))}, ChooseDirection)), new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(3.0),PredicateExprComparator.GE),new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.LEQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL3,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(3.0))}, ChooseDirection)), new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(2.0),PredicateExprComparator.GE),new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.LEQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL2,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(2.0))}, ChooseDirection)), new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(1.0),PredicateExprComparator.GE),new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.LEQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL1,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(1.0))}, ChooseDirection)), new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(0.0),PredicateExprComparator.GE),new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.LEQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(buttonL0,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(0.0))}, ChooseDirection)));
		model.addProcessDefinition(ChooseGoalInAndOut, new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(4.0),PredicateExprComparator.LE),new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.GEQ),BooleanConnector.AND),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL4,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(buttonF4,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(4.0))}, ChooseDirection), new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(3.0),PredicateExprComparator.LE),new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.GEQ),BooleanConnector.AND),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL3,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(buttonF3,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(3.0))}, ChooseDirection)), new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(2.0),PredicateExprComparator.LE),new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.GEQ),BooleanConnector.AND),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL2,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(buttonF2,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(2.0))}, ChooseDirection)), new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(1.0),PredicateExprComparator.LE),new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.GEQ),BooleanConnector.AND),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL1,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(buttonF1,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(1.0))}, ChooseDirection)), new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(3.0),PredicateExprComparator.GE),new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.LEQ),BooleanConnector.AND),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL3,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(buttonF3,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(3.0))}, ChooseDirection)), new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(2.0),PredicateExprComparator.GE),new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.LEQ),BooleanConnector.AND),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL2,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(buttonF3,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(2.0))}, ChooseDirection)), new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(1.0),PredicateExprComparator.GE),new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.LEQ),BooleanConnector.AND),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL1,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(buttonF3,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(1.0))}, ChooseDirection)), new Prefix(100.0, new AskAction(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,new Constant(0.0),PredicateExprComparator.GE),new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.LEQ),BooleanConnector.AND),new BooleanConstraintExpr(new DisequationOfPredicateExpressions(buttonL0,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(buttonF3,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),BooleanConnector.AND)), new SideEffect[]{new SideEffect(goal,new Constant(0.0))}, ChooseDirection)));
		model.addProcessDefinition(ChooseDirection, new Choice(new Choice(new Choice(new Choice(new Choice(new Prefix(100.0, stop, new SideEffect[]{new SideEffect(direction,new Constant(0.0))}, Controller), new Prefix(100.0, park, new SideEffect[]{new SideEffect(direction,new Constant(0.0)),new SideEffect(door,new Constant(1.0))}, Controller)), new Prefix(100.0, startUp, new SideEffect[]{new SideEffect(direction,new Constant(1.0))}, Controller)), new Prefix(100.0, startDown, new SideEffect[]{new SideEffect(direction,new ArithmeticPredicateExpr(new Constant(0),new Constant(1.0),ArithmeticOperation.SUB))}, Controller)), new Prefix(100.0, continueACT, new SideEffect[]{}, Controller)), new Prefix(100.0, reverse, new SideEffect[]{new SideEffect(direction,new ArithmeticPredicateExpr(new Constant(0),direction,ArithmeticOperation.SUB))}, Controller)));
		model.addProcessDefinition(Buttons, new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Choice(new Prefix(1.0, pressL, new SideEffect[]{new SideEffect(buttonL0,new Constant(1.0))}, Buttons), new Prefix(1.0, pressL, new SideEffect[]{new SideEffect(buttonL1,new Constant(1.0))}, Buttons)), new Prefix(1.0, pressL, new SideEffect[]{new SideEffect(buttonL2,new Constant(1.0))}, Buttons)), new Prefix(1.0, pressL, new SideEffect[]{new SideEffect(buttonL3,new Constant(1.0))}, Buttons)), new Prefix(1.0, pressL, new SideEffect[]{new SideEffect(buttonL4,new Constant(1.0))}, Buttons)), new Prefix(1.0, pressF, new SideEffect[]{new SideEffect(buttonF0,new Constant(1.0))}, Buttons)), new Prefix(1.0, pressF, new SideEffect[]{new SideEffect(buttonF1,new Constant(1.0))}, Buttons)), new Prefix(1.0, pressF, new SideEffect[]{new SideEffect(buttonF2,new Constant(1.0))}, Buttons)), new Prefix(1.0, pressF, new SideEffect[]{new SideEffect(buttonF3,new Constant(1.0))}, Buttons)), new Prefix(1.0, pressF, new SideEffect[]{new SideEffect(buttonF4,new Constant(1.0))}, Buttons)), new Prefix(1.0, unpressL, new SideEffect[]{new SideEffect(buttonL0,new Constant(0.0))}, Buttons)), new Prefix(1.0, unpressL, new SideEffect[]{new SideEffect(buttonL1,new Constant(0.0))}, Buttons)), new Prefix(1.0, unpressL, new SideEffect[]{new SideEffect(buttonL2,new Constant(0.0))}, Buttons)), new Prefix(1.0, unpressL, new SideEffect[]{new SideEffect(buttonL3,new Constant(0.0))}, Buttons)), new Prefix(1.0, unpressL, new SideEffect[]{new SideEffect(buttonL4,new Constant(0.0))}, Buttons)), new Prefix(1.0, unpressF, new SideEffect[]{new SideEffect(buttonF0,new Constant(0.0))}, Buttons)), new Prefix(1.0, unpressF, new SideEffect[]{new SideEffect(buttonF1,new Constant(0.0))}, Buttons)), new Prefix(1.0, unpressF, new SideEffect[]{new SideEffect(buttonF2,new Constant(0.0))}, Buttons)), new Prefix(1.0, unpressF, new SideEffect[]{new SideEffect(buttonF3,new Constant(0.0))}, Buttons)), new Prefix(1.0, unpressF, new SideEffect[]{new SideEffect(buttonF4,new Constant(0.0))}, Buttons)));
		model.addProcessDefinition(People, new Choice(new Prefix(10.0, enter, new SideEffect[]{new SideEffect(load,new ArithmeticPredicateExpr(load,new Constant(1.0),ArithmeticOperation.SUM))}, People), new Prefix(10.0, leave, new SideEffect[]{new SideEffect(load,new ArithmeticPredicateExpr(load,new Constant(1.0),ArithmeticOperation.SUB))}, People)));
		
		/////////////////////////////////////////////////
		////Initially installed features and Process ////
		/////////////////////////////////////////////////
		model.setInitialState(Arrays.asList(Parking,AntiPrank,Empty,Executive,OpenIfIdle,Overload,Parking,QuickClose,Shuttle,AlmostFull), Test);
		model.resetToInitialState();
		return model;		
	}
}
			
			
