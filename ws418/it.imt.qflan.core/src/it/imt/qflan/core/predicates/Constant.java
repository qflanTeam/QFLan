package it.imt.qflan.core.predicates;

import java.util.Set;

import it.imt.qflan.core.features.ConcreteFeature;
import it.imt.qflan.core.predicates.interfaces.IPredicateExpr;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public class Constant implements IPredicateExpr {

	private double val;
	
	public Constant(double val) {
		this.val=val;
	}
	
	@Override
	public double eval(Set<ConcreteFeature> installedFeatures) {
		return val;
	}
	
	@Override
	public String toString() {
		return String.valueOf(val);
	}

	@Override
	public void addConstraintToInvolvedActions(IConstraint constraintToAdd) {	
	}

}
