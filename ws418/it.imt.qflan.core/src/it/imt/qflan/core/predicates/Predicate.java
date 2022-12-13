package it.imt.qflan.core.predicates;

import java.util.Set;

import it.imt.qflan.core.features.ConcreteFeature;
import it.imt.qflan.core.features.interfaces.IFeature;
import it.imt.qflan.core.predicates.interfaces.IPredicateDef;
import it.imt.qflan.core.predicates.interfaces.IPredicateExpr;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public class Predicate implements IPredicateExpr {

	private IPredicateDef predicateDef;
	private IFeature feature;
	
	public Predicate(IPredicateDef predicateDef, IFeature feature) {
		super();
		this.predicateDef = predicateDef;
		this.feature = feature;
	}

	@Override
	public double eval(Set<ConcreteFeature> installedFeatures) {
		return predicateDef.eval(feature, installedFeatures);
	}

	@Override
	public String toString() {
		return predicateDef.getName()+"("+feature+")";
	}

	@Override
	public void addConstraintToInvolvedActions(IConstraint constraintToAdd) {
		//feature.addConstraintToCheckWhenExecuting(constraintToAdd);
		feature.addConstraintToCheckWhenInstalling(constraintToAdd);
	}

	
	
}
