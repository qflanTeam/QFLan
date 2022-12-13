package it.imt.qflan.core.predicates.interfaces;

import java.util.Set;

import it.imt.qflan.core.features.ConcreteFeature;
import it.imt.qflan.core.features.interfaces.IFeature;

public interface IPredicateDef {

	void setValue(ConcreteFeature feacture, double value);
	double eval(IFeature feature, Set<ConcreteFeature> installedFeatures);
	String getName();
	//void setMath(MathEval math, Collection<IFeature> features);
	
}
