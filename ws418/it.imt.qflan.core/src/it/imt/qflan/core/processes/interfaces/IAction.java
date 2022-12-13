package it.imt.qflan.core.processes.interfaces;

import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.model.QFlanModel;

public interface IAction {
	
	//boolean isAllowed(Set<IConstraint> constraints, Set<ConcreteFeature> installedFeatures);
	boolean isAllowedByOtherConstraints(QFlanModel model) throws Z3Exception;
	boolean isAllowedByActionConstraints(QFlanModel model) throws Z3Exception;

	String getName();

	boolean modifiesStore();

	/*void addConstraintToCheckWhenExecuting(IConstraint constraint);
	Collection<IConstraint> getConstraintsToCheckWhenExecuting();*/

}
