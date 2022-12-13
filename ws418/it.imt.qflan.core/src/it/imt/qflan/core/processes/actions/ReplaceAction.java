package it.imt.qflan.core.processes.actions;

import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.features.ConcreteFeature;
import it.imt.qflan.core.model.QFlanModel;

public class ReplaceAction extends ActionCommonCode {

	private ConcreteFeature toRemove;
	private ConcreteFeature toAdd;
	//private HashSet<IConstraint> constraintsToCheckOfArguments;
	
	@Override
	public boolean modifiesStore() {
		return true;
	}
	
	public ReplaceAction(ConcreteFeature toRemove, ConcreteFeature toAdd) {
		super("replace("+toRemove+","+toAdd+")");
		this.toRemove=toRemove;
		this.toAdd=toAdd;
		if(toRemove.equals(toAdd)){
			throw new UnsupportedOperationException("Replace actions must regard different features");
		}
	}
	
	@Override
	public boolean isAllowedByOtherConstraints(QFlanModel model) throws Z3Exception {
		if(model.isInstalled(toAdd)){
			return false;
		}
		if(!model.isInstalled(toRemove)){
			return false;
		}
		//return true;
		//return false;
		//return model.checkSATReplacingSlow(toAdd, toRemove);
		//return model.checkSATReplacing(toAdd,toRemove);
		return model.checkReplaceNoZ3(this);
	}
	
	@Override
	public boolean isAllowedByActionConstraints(QFlanModel model) throws Z3Exception {
		if(model.isInstalled(toAdd)){
			return false;
		}
		if(!model.isInstalled(toRemove)){
			return false;
		}
		return model.checkExecuteNormalActionOrFeatureNoZ3(this);
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((toAdd == null) ? 0 : toAdd.hashCode());
		result = prime * result
				+ ((toRemove == null) ? 0 : toRemove.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReplaceAction other = (ReplaceAction) obj;
		if (toAdd == null) {
			if (other.toAdd != null)
				return false;
		} else if (!toAdd.equals(other.toAdd))
			return false;
		if (toRemove == null) {
			if (other.toRemove != null)
				return false;
		} else if (!toRemove.equals(other.toRemove))
			return false;
		return true;
	}

	public ConcreteFeature getToRemove() {
		return toRemove;
	}
	
	public ConcreteFeature getToAdd() {
		return toAdd;
	}

	/*@Override
	public Collection<IConstraint> getConstraintsToCheckWhenExecuting() {
		if(constraintsToCheckOfArguments==null){
			if(toAdd.getConstraintsToCheckWhenInstalling()!=null || toRemove.getConstraintsToCheckWhenInstalling()!=null){
				constraintsToCheckOfArguments=new HashSet<>();
			}
			if(toAdd.getConstraintsToCheckWhenInstalling()!=null){
				constraintsToCheckOfArguments.addAll(toAdd.getConstraintsToCheckWhenInstalling());
			}
			if(toRemove.getConstraintsToCheckWhenInstalling()!=null){
				constraintsToCheckOfArguments.addAll(toRemove.getConstraintsToCheckWhenInstalling());
			}
		}
		return constraintsToCheckOfArguments;
	}*/
	
	
}
