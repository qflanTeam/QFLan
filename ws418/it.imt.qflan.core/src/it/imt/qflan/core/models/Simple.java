package it.imt.qflan.core.models;

import java.util.Arrays;

import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.features.*;
import it.imt.qflan.core.features.interfaces.*;
import it.imt.qflan.core.model.*;
import it.imt.qflan.core.predicates.*;
import it.imt.qflan.core.predicates.interfaces.*;
import it.imt.qflan.core.processes.*;
import it.imt.qflan.core.processes.constraints.*;
import it.imt.qflan.core.processes.actions.*;
import it.imt.qflan.core.variables.QFLanVariable;
import it.imt.qflan.core.variables.SideEffect;
			
public class Simple implements IQFlanModelBuilder{
	
	public Simple(){
			System.out.println("Model builder instantiated");
		}
	
	public QFlanModel createModel() throws Z3Exception{
		QFlanModel model = new QFlanModel();
			
		//////////////////
		/////Variables////
		//////////////////
		QFLanVariable x1 = model.addVariable("x1", new Constant(0.0));
			
		//////////////////
		/////Features/////
		//////////////////
		//ABSTRACT FEATURES
		AbstractFeature af1 = new AbstractFeature("af1");
		model.addAbstractFeatureDefinition(af1);
		AbstractFeature af2 = new AbstractFeature("af2");
		model.addAbstractFeatureDefinition(af2);
		
		//CONCRETE FEATURES
		ConcreteFeature cf1 = new ConcreteFeature("cf1");
		model.addConcreteFeatureDefinition(cf1);
		ConcreteFeature cf2 = new ConcreteFeature("cf2");
		model.addConcreteFeatureDefinition(cf2);
		ConcreteFeature cf3 = new ConcreteFeature("cf3");
		model.addConcreteFeatureDefinition(cf3);
		ConcreteFeature cf4 = new ConcreteFeature("cf4");
		model.addConcreteFeatureDefinition(cf4);
		ConcreteFeature cf5 = new ConcreteFeature("cf5");
		model.addConcreteFeatureDefinition(cf5);
		
		//DIAGRAM
		//Normal relation
		af1.addSon(cf1);
		af1.addSon(cf2);
		af1.addSon(cf3);
		cf3.setOptional(false);
		af1.addSon(af2);
		
		//OR relation
		af2.addSon(cf4);
		af2.addSon(cf5);
		
		model.computeDescendantsAndAncestors();
		
		////////////////////////////
		//Hierarchical constraints//
		////////////////////////////
		model.addConstraint(new HasFeature(cf3,model,true));
		model.addConstraint(new Alternative_OrConstraint(af2,Arrays.asList((IFeature)cf4,(IFeature)cf5), Alternative_ORCondition.OR,model));
			
		/////////////////////////////
		////CrossTree Constraints////
		/////////////////////////////
		model.addConstraint(new FeatureRequireConstraint(cf1, cf2,model));
		model.addConstraint(new FeatureSetConstraint(Arrays.asList((IFeature)cf2,(IFeature)cf5), FeatureSetCondition.ATMOSTONE,model));
		
			
		//////////////////
		////Predicates////
		//////////////////
		IPredicateDef price = new PredicateDef("price");
		model.addPredicateDef(price);
		price.setValue(cf1,1.0);
		price.setValue(cf2,2.0);
		price.setValue(cf4,4.0);
		
		
		////////////////////////////////
		////Quantitative Constraints////
		////////////////////////////////
		model.addConstraint(new DisequationOfPredicateExpressions(new Predicate(price, af1),new Constant(6.0),PredicateExprComparator.LEQ));
		model.addConstraint(new DisequationOfPredicateExpressions(x1,new Constant(1.0),PredicateExprComparator.LEQ));
		
		
		///////////////
		////Actions////
		///////////////
		NormalAction act1 = new NormalAction("act1");
		model.addNormalAction(act1);
		NormalAction act2 = new NormalAction("act2");
		model.addNormalAction(act2);
		
		//////////////////////////
		////Action constraints////
		//////////////////////////
		model.addActionConstraint(new ActionRequiresConstraint(act1, new BooleanConstraintExpr(new HasFeature(cf2,model),new DisequationOfPredicateExpressions(x1,new Constant(1.0),PredicateExprComparator.EQ),BooleanConnector.AND)));
		
		//In order to use a feature, it must be installed. These constraints are already built-in
		//( do(cf1) -> has(cf1))
		//( do(cf2) -> has(cf2))
		//( do(cf3) -> has(cf3))
		//( do(cf4) -> has(cf4))
		//( do(cf5) -> has(cf5))
		
		/////////////////////////////
		////Processes definitions////
		/////////////////////////////
		
		ProcessDefinition proc1 = new ProcessDefinition("proc1");
		ProcessDefinition proc2 = new ProcessDefinition("proc2");
		
		model.addProcessDefinition(proc1, QFlanModel.makeMultiChoice(Arrays.asList(new Prefix(2.0,act2,new SideEffect[]{new SideEffect(x1,new Constant(1.0))},proc2), new Prefix(3.0,cf2,new SideEffect[]{},proc2))));
		model.addProcessDefinition(proc2, ZeroProcess.ZERO);
		
		/////////////////////////////////////////////////
		////Initially installed features and Process ////
		/////////////////////////////////////////////////
		ProcessDefinition initial= new ProcessDefinition("init");
		model.addProcessDefinition(initial, QFlanModel.makeMultiParallel(Arrays.asList(proc1,proc1,proc1)));
		model.setInitialState(Arrays.asList(cf3,cf4), initial);
		model.resetToInitialState();
		return model;		
	}
}
			
			
