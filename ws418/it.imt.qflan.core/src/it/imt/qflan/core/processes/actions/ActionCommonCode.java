package it.imt.qflan.core.processes.actions;

import it.imt.qflan.core.processes.interfaces.IAction;

public abstract class ActionCommonCode implements IAction {

	//private Set<IConstraint> constraintsToCheckWhenExecuting;
	private String name;

	public ActionCommonCode(String name) {
		this.name=name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean modifiesStore() {
		return false;
	}

	/*@Override
	public void addConstraintToCheckWhenExecuting(IConstraint constraint) {
		if(constraintsToCheckWhenExecuting==null){
			constraintsToCheckWhenExecuting=new HashSet<>();
		}
		this.constraintsToCheckWhenExecuting.add(constraint);
	}
	
	@Override
	public Collection<IConstraint> getConstraintsToCheckWhenExecuting() {
		return constraintsToCheckWhenExecuting;
	}
	
	*/
	
	@Override
	public String toString() {
		return name;
	}

}
