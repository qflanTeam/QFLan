package it.imt.qflan.core.features;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import it.imt.qflan.core.features.interfaces.IFeature;
import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public class AbstractFeature extends FeatureCommonCode {

	private Set<IFeature> sons;
	private Set<ConcreteFeature> concreteDescendants;
	int installedConcreteDescendants=0;
	
	public AbstractFeature(String name) {
		super(name);
		sons=new LinkedHashSet<>();
	}
	
	/*@Override
	public boolean isAllowed(Set<IConstraint> constraints, Set<ConcreteFeature> installedFeatures) {
		//We can execute only concrete features
		return false;
	}*/
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public boolean isAllowedByOtherConstraints(QFlanModel model) {
		//We can execute only concrete features
		return false;
	}
	
	@Override
	public boolean isAllowedByActionConstraints(QFlanModel model) {
		//We can execute only concrete features
		return false;
	}

	public void addSon(IFeature son) {
		sons.add(son);
		son.setFather(this);
	}

	@Override
	public double evalPredicate(HashMap<ConcreteFeature, Double> values,String predicateName, Set<ConcreteFeature> installedFeatures) {
		double ret=0;
		for (IFeature feature : sons) {
			ret+=feature.evalPredicate(values, predicateName,installedFeatures);
		}
		return ret;
	}
	
	/*@Override
	public void addConstraintToCheckWhenExecuting(IConstraint constraint) {
		throw new UnsupportedOperationException();
		//We can execute only concrete features
		//for (IFeature son : sons) {
		//	son.addConstraintToCheckWhenExecuting(constraint);
		//}
	}
	
	@Override
	public Collection<IConstraint> getConstraintsToCheckWhenExecuting() {
		throw new UnsupportedOperationException();
	}*/
	
	@Override
	public void addConstraintToCheckWhenInstalling(IConstraint constraint) {
		for (IFeature son : sons) {
			son.addConstraintToCheckWhenInstalling(constraint);
		}
	}

	@Override
	public Collection<IConstraint> getConstraintsToCheckWhenInstalling() {
		throw new UnsupportedOperationException();
	}

	public Set<ConcreteFeature> computeOrGetConcreteDescendants() {
		if(concreteDescendants==null){
			concreteDescendants = new LinkedHashSet<>();
			for (IFeature son : sons) {
				if(son instanceof ConcreteFeature){
					ConcreteFeature concreteSon = (ConcreteFeature)son;
					concreteDescendants.add(concreteSon);
				}
				else{
					AbstractFeature abstractSon = (AbstractFeature)son;
					Set<ConcreteFeature> concreteDescendantsOfSon = abstractSon.computeOrGetConcreteDescendants();
					concreteDescendants.addAll(concreteDescendantsOfSon);
				}
			}
			for (ConcreteFeature concreteFeature : concreteDescendants) {
				concreteFeature.addAncestor(this);
			}
		}
		return concreteDescendants;
		
	}

	public void concreteSonInstalled() {
		installedConcreteDescendants++;
		if(installedConcreteDescendants>concreteDescendants.size()){
			throw new UnsupportedOperationException(getName()+" has "+installedConcreteDescendants+" installed concrete descendants but only "+concreteDescendants.size()+" concrete descendants.");
		}
	}
	public void concreteSonUninstalled() {
		installedConcreteDescendants--;
		if(installedConcreteDescendants<0){
			throw new UnsupportedOperationException(getName()+" has "+installedConcreteDescendants+" installed concrete descendants.");
		}
	}
	@Override
	public boolean isInstallled() {
		return installedConcreteDescendants>0;
	}
}
