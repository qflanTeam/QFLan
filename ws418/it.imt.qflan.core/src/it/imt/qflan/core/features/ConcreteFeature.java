package it.imt.qflan.core.features;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public class ConcreteFeature extends FeatureCommonCode {

	private Set<IConstraint> constraintsToCheckWhenInstalling;
	private Set<AbstractFeature> ancestors;
	private boolean installed=false;
	
	public ConcreteFeature(String name) {
		super(name);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	/*@Override
	public boolean isAllowed(Set<IConstraint> constraints, Set<ConcreteFeature> installedFeatures) {
		if(!installedFeatures.contains(this)){
			//If this feature is not installed, I cannot use it
			return false;
		}
		else{
			//TODO
			return true;
		}
	}*/
	
	@Override
	public boolean isAllowedByOtherConstraints(QFlanModel model) throws Z3Exception{
		if(! model.isInstalled(this)){
			//If this feature is not installed, I cannot use it
			return false;
		}
		else{
			/*Expr doAction = model.getDoFunc().Apply(model.getActionAndFeatureToZ3(this));
			return model.checkSAT(model.getCTX().MkEq(doAction, model.getCTX().MkTrue()));*/
			//return model.checkSATSlow(this);
			//return model.checkSATExecuteNormalActionOrFeature(this);
			//return true;
			
			//I commented this out. Because only action constraints can affect it
			//return model.checkExecuteNormalActionOrFeatureNoZ3(this);
			//return true;
			
			//I need to check if the application of the side effects made the store false
			return model.checkOtherConstraints(this);
		}
	}
	
	@Override
	public boolean isAllowedByActionConstraints(QFlanModel model) throws Z3Exception{
		if(! model.isInstalled(this)){
			//If this feature is not installed, I cannot use it
			return false;
		}
		else{
			/*Expr doAction = model.getDoFunc().Apply(model.getActionAndFeatureToZ3(this));
			return model.checkSAT(model.getCTX().MkEq(doAction, model.getCTX().MkTrue()));*/
			//return model.checkSATSlow(this);
			//return model.checkSATExecuteNormalActionOrFeature(this);
			//return true;
			return model.checkExecuteNormalActionOrFeatureNoZ3(this);
		}
	}

	@Override
	public double evalPredicate(HashMap<ConcreteFeature, Double> values, String predicateName, Set<ConcreteFeature> installedFeatures) {
		if(!installedFeatures.contains(this)){
			return 0;
		}
		else{
			Double ret = values.get(this);
			if(ret == null){
				//By default, we have value 0
				return 0;
			}
			return ret;
		}
	}
	
	public void addConstraintToCheckWhenInstalling(IConstraint constraint) {
		//super.addConstraintToCheckWhenExecuting(constraint);
		if(constraintsToCheckWhenInstalling==null){
			constraintsToCheckWhenInstalling=new HashSet<>();
		}
		constraintsToCheckWhenInstalling.add(constraint);
	}

	@Override
	public Collection<IConstraint> getConstraintsToCheckWhenInstalling() {
		/*Qui devo distinguere da:
			- Vincoli quando eseguo:
				- vincoli quando eseguo la Feature. (do(feature) -> c) 
				- vincoli quando eseguo la azione install(F) (do(install(feature)) -> c)
				- vincoli quando eseguo la azione uninstall(F) (do(uninstall(feature)) -> c)
				- vincoli quando eseguo la azione replace(F) (do(replace(feature)) -> c)
			- Vincoli dello store quando install/uninstall/replace la feature
			
			-Poi a parte creo dei hierarchical vincoli appositi: or, alternative, mandatory */
		return constraintsToCheckWhenInstalling;
	}

	public void addAncestor(AbstractFeature abstractFeature) {
		if(ancestors==null){
			ancestors=new LinkedHashSet<>();
		}
		ancestors.add(abstractFeature);		
	}

	public void hasBeenInstalled() {
		installed=true;
		if(ancestors!=null){
			for (AbstractFeature abstractFeature : ancestors) {
				abstractFeature.concreteSonInstalled();
			}
		}
	}

	public void hasBeenUninstalled() {
		installed=false;
		if(ancestors!=null){
			for (AbstractFeature abstractFeature : ancestors) {
				abstractFeature.concreteSonUninstalled();
			}
		}
	}

	@Override
	public boolean isInstallled() {
		return installed;
	}

}
