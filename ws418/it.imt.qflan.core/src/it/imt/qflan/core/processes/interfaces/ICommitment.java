package it.imt.qflan.core.processes.interfaces;

import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.variables.SideEffect;

public interface ICommitment {

	//boolean isAllowed(Set<IConstraint> constraints, Set<ConcreteFeature> installedFeatures);
	boolean isAllowed(QFlanModel model) throws Z3Exception;
	//double getRealRate();
	double getActionRate();
	SideEffect[] getSideEffects();
	
	boolean modifiesStore();
	IAction getAction();
	IProcess getContinuation();
	
}
