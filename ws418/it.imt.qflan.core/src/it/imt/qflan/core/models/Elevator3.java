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
			
public class Elevator3 implements IQFlanModelBuilder{
	
	public Elevator3(){
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
		QFLanVariable direction = model.addVariable("direction", new Constant(0.0));
		//QFLanVariable call = model.addVariable("call", new Constant(0.0));
		QFLanVariable button0 = model.addVariable("button0", new Constant(0.0));
		QFLanVariable button1 = model.addVariable("button1", new Constant(0.0));
		QFLanVariable button2 = model.addVariable("button2", new Constant(0.0));
		QFLanVariable button3 = model.addVariable("button3", new Constant(0.0));
		QFLanVariable button4 = model.addVariable("button4", new Constant(0.0));
			
		//////////////////
		/////Features/////
		//////////////////
		AbstractFeature Elevator = new AbstractFeature("Elevator");
		model.addAbstractFeatureDefinition(Elevator);
		
		ConcreteFeature Parking = new ConcreteFeature("Parking");
		model.addConcreteFeatureDefinition(Parking);
		
		Elevator.addSon(Parking);
		
		//model.addConstraint(new HasFeature(Parking, model));
			
		//////////////////
		////Predicates////
		//////////////////
		IPredicateDef where = new PredicateDef("where");
		model.addPredicateDef(where);
		where.setValue(Parking,0.0);
		
		
		///////////////
		////Actions////
		///////////////
		NormalAction up = new NormalAction("up");
		model.addNormalAction(up);
		NormalAction down = new NormalAction("down");
		model.addNormalAction(down);
		NormalAction reverse = new NormalAction("reverse");
		model.addNormalAction(reverse);
		NormalAction open = new NormalAction("open");
		model.addNormalAction(open);
		NormalAction close = new NormalAction("close");
		model.addNormalAction(close);
		NormalAction press = new NormalAction("press");
		model.addNormalAction(press);
		NormalAction park = new NormalAction("park");
		model.addNormalAction(park);
		NormalAction wakeup = new NormalAction("wakeup");
		model.addNormalAction(wakeup);
		
		//////////////////////////
		////Action constraints////
		//////////////////////////
		model.addActionConstraint(new ActionRequiresConstraint(open, new DisequationOfPredicateExpressions(door,new Constant(0.0),PredicateExprComparator.EQ)));
		model.addActionConstraint(new ActionRequiresConstraint(close, new DisequationOfPredicateExpressions(door,new Constant(1.0),PredicateExprComparator.EQ)));
		model.addActionConstraint(new ActionRequiresConstraint(up, new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(direction,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(floor,top,PredicateExprComparator.LE),BooleanConnector.AND),new DisequationOfPredicateExpressions(door,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND)));
		model.addActionConstraint(new ActionRequiresConstraint(down, new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(direction,new ArithmeticPredicateExpr(new Constant(0),new Constant(1.0),ArithmeticOperation.SUB),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(floor,bottom,PredicateExprComparator.GE),BooleanConnector.AND),new DisequationOfPredicateExpressions(door,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND)));
		model.addActionConstraint(new ActionRequiresConstraint(press, new TrueConstraint()));
		model.addActionConstraint(new ActionRequiresConstraint(reverse, new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(floor,top,PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(floor,bottom,PredicateExprComparator.EQ),BooleanConnector.OR),new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(button0,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(button1,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(button2,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(button3,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(button4,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),BooleanConnector.AND)));
		model.addActionConstraint(new ActionRequiresConstraint(wakeup, new BooleanConstraintExpr(new DisequationOfPredicateExpressions(direction,new Constant(0.0),PredicateExprComparator.EQ),new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new DisequationOfPredicateExpressions(button0,new Constant(1.0),PredicateExprComparator.EQ),new DisequationOfPredicateExpressions(button1,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(button2,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(button3,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),new DisequationOfPredicateExpressions(button4,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.OR),BooleanConnector.AND)));
		model.addActionConstraint(new ActionRequiresConstraint(park, new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new BooleanConstraintExpr(new HasFeature(Parking,model),new DisequationOfPredicateExpressions(new Predicate(where, Parking),floor,PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(button0,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(button1,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(button2,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(button3,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND),new DisequationOfPredicateExpressions(button4,new Constant(0.0),PredicateExprComparator.EQ),BooleanConnector.AND)));
		
		//In order to use a feature, it must be installed. These constraints are already built-in
		//( do(Parking) -> has(Parking))
		
		///////////////////
		////Constraints////
		///////////////////
		model.addConstraint(new DisequationOfPredicateExpressions(bottom,top,PredicateExprComparator.LEQ));
		model.addConstraint(new DisequationOfPredicateExpressions(floor,top,PredicateExprComparator.LEQ));
		model.addConstraint(new DisequationOfPredicateExpressions(floor,bottom,PredicateExprComparator.GEQ));
		
		
		/////////////////////////////
		////Processes definitions////
		/////////////////////////////
		ProcessDefinition Start = new ProcessDefinition("Start");
		ProcessDefinition Lift = new ProcessDefinition("Lift");
		ProcessDefinition LiftTurnButtonDown = new ProcessDefinition("LiftTurnButtonDown");
		ProcessDefinition Controller = new ProcessDefinition("Controller");
		ProcessDefinition Buttons = new ProcessDefinition("Buttons");
		
		model.addProcessDefinition(Start, new Parallel(new Parallel(Lift, Buttons), Controller));
		model.addProcessDefinition(Lift, new Choice(new Choice(new Choice(new Prefix(1.0, open, new SideEffect[]{new SideEffect(door,new Constant(1.0))}, LiftTurnButtonDown), new Prefix(1.0, close, new SideEffect[]{new SideEffect(door,new Constant(0.0))}, Lift)), new Prefix(1.0, up, new SideEffect[]{new SideEffect(floor,new ArithmeticPredicateExpr(floor,new Constant(1.0),ArithmeticOperation.SUM))}, Lift)), new Prefix(1.0, down, new SideEffect[]{new SideEffect(floor,new ArithmeticPredicateExpr(floor,new Constant(1.0),ArithmeticOperation.SUB))}, Lift)));
		model.addProcessDefinition(LiftTurnButtonDown, new Choice(new Choice(new Choice(new Choice(new Prefix(1.0, new AskAction(new DisequationOfPredicateExpressions(floor,new Constant(0.0),PredicateExprComparator.EQ)), new SideEffect[]{new SideEffect(button0,new Constant(0.0))}, Lift), new Prefix(1.0, new AskAction(new DisequationOfPredicateExpressions(floor,new Constant(1.0),PredicateExprComparator.EQ)), new SideEffect[]{new SideEffect(button1,new Constant(0.0))}, Lift)), new Prefix(1.0, new AskAction(new DisequationOfPredicateExpressions(floor,new Constant(2.0),PredicateExprComparator.EQ)), new SideEffect[]{new SideEffect(button2,new Constant(0.0))}, Lift)), new Prefix(1.0, new AskAction(new DisequationOfPredicateExpressions(floor,new Constant(3.0),PredicateExprComparator.EQ)), new SideEffect[]{new SideEffect(button3,new Constant(0.0))}, Lift)), new Prefix(1.0, new AskAction(new DisequationOfPredicateExpressions(floor,new Constant(4.0),PredicateExprComparator.EQ)), new SideEffect[]{new SideEffect(button4,new Constant(0.0))}, Lift)));
		model.addProcessDefinition(Controller, new Choice(new Choice(new Choice(new Prefix(1.0, reverse, new SideEffect[]{new SideEffect(direction,new ArithmeticPredicateExpr(direction,new ArithmeticPredicateExpr(new Constant(0),new Constant(1.0),ArithmeticOperation.SUB),ArithmeticOperation.MULT))}, Controller), new Prefix(1.0, wakeup, new SideEffect[]{new SideEffect(direction,new Constant(1.0))}, Controller)), new Prefix(1.0, wakeup, new SideEffect[]{new SideEffect(direction,new ArithmeticPredicateExpr(new Constant(0),new Constant(1.0),ArithmeticOperation.SUB))}, Controller)), new Prefix(1.0, park, new SideEffect[]{new SideEffect(direction,new Constant(0.0)),new SideEffect(door,new Constant(1.0))}, Controller)));
		model.addProcessDefinition(Buttons, new Choice(new Choice(new Choice(new Choice(new Prefix(1.0, press, new SideEffect[]{new SideEffect(button0,new Constant(1.0))}, Buttons), new Prefix(1.0, press, new SideEffect[]{new SideEffect(button1,new Constant(1.0))}, Buttons)), new Prefix(1.0, press, new SideEffect[]{new SideEffect(button2,new Constant(1.0))}, Buttons)), new Prefix(1.0, press, new SideEffect[]{new SideEffect(button3,new Constant(1.0))}, Buttons)), new Prefix(1.0, press, new SideEffect[]{new SideEffect(button4,new Constant(1.0))}, Buttons)));
		
		/////////////////////////////////////////////////
		////Initially installed features and Process ////
		/////////////////////////////////////////////////
		model.setInitialState(Arrays.asList(Parking), Start);
		model.resetToInitialState();
		return model;		
	}
}
			
			
