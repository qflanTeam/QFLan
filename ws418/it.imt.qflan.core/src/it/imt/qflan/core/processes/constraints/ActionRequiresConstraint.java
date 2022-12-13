package it.imt.qflan.core.processes.constraints;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.processes.interfaces.IAction;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public class ActionRequiresConstraint implements IConstraint {

	private IAction action;
	private IConstraint constraint;
	
	public ActionRequiresConstraint(IAction action, IConstraint constraint) {
		super();
		this.action = action;
		this.constraint = constraint;
	}
	
	@Override
	public String toString() {
		return "(do("+action.getName()+") -> "+constraint+")";
	}

	@Override
	public BoolExpr toZ3(QFlanModel model) throws Z3Exception {
		Expr doAction = model.getDoFunc().Apply(model.getActionAndFeatureToZ3(action));
		BoolExpr doActionTrue = model.getCTX().MkEq(doAction, model.getCTX().MkTrue());
		return model.getCTX().MkImplies(doActionTrue,constraint.toZ3(model));
	}

	@Override
	public boolean eval(QFlanModel model, IAction actionToExecute) {
		if(actionToExecute.equals(action)){
			return constraint.eval(model, actionToExecute);
		}
		else{
			return true;
		}
	}
	
	@Override
	public boolean eval(QFlanModel model) {
		throw new UnsupportedOperationException("An action constraint must be evaluated wrt an action to be executed.");
	}

	/*@Override
	public void addConstraintToCheck(IConstraint constraintToAdd) {
		action.addConstraintToCheckWhenExecuting(constraintToAdd);
		//DO I NEED THIS?
		//constraint.addConstraintToInvolvedActions(constraintToAdd);	
	}*/
	
	public IAction getAction(){
		return action;
	}

	public IConstraint getConstraint() {
		return constraint;
	}
	
	
}
