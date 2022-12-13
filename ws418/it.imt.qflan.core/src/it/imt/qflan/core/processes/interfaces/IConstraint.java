package it.imt.qflan.core.processes.interfaces;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.model.QFlanModel;


public interface IConstraint {

	BoolExpr toZ3(QFlanModel model) throws Z3Exception;

	boolean eval(QFlanModel model, IAction actionToExecute);
	
	boolean eval(QFlanModel model);

	//Collection<ConcreteFeature> involvedFeatures();
	//void addConstraintToCheck(IConstraint constraintToAdd);

}
