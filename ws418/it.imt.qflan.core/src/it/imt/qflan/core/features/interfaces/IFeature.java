package it.imt.qflan.core.features.interfaces;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import it.imt.qflan.core.features.ConcreteFeature;
import it.imt.qflan.core.processes.interfaces.IAction;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public interface IFeature extends IAction{

	void setFather(IFeature father);
	IFeature getFather();
	double evalPredicate(HashMap<ConcreteFeature, Double> values, String predicateName, Set<ConcreteFeature> installedFeatures);
	//String getName();
	public void addConstraintToCheckWhenInstalling(IConstraint constraint);
	Collection<IConstraint> getConstraintsToCheckWhenInstalling();
	
	public boolean isInstallled();
	public boolean isOptional();
	public void setOptional(boolean optional);
	
}
