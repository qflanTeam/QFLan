package it.imt.qflan.core.predicates.interfaces;

import java.util.Set;

import it.imt.qflan.core.features.ConcreteFeature;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public interface IPredicateExpr {

	double eval(Set<ConcreteFeature> installedFeatures);

	void addConstraintToInvolvedActions(IConstraint constraintToAdd);
	
}
