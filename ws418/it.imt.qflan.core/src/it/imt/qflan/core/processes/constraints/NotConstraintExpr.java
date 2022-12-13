package it.imt.qflan.core.processes.constraints;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.processes.interfaces.IAction;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public class NotConstraintExpr implements IConstraint {

	private IConstraint innerConstraint;
	
	public NotConstraintExpr(IConstraint first) {
		super();
		this.innerConstraint = first;
	}
	
	@Override
	public String toString() {
		return "(!"+innerConstraint.toString()+")";
	}

	@Override
	public BoolExpr toZ3(QFlanModel model) throws Z3Exception {
		return model.getCTX().MkNot(innerConstraint.toZ3(model));
	}

	@Override
	public boolean eval(QFlanModel model, IAction actionToExecute) {
		return !innerConstraint.eval(model, actionToExecute);
	}
	
	@Override
	public boolean eval(QFlanModel model) {
		return !innerConstraint.eval(model);
	}

	/*@Override
	public void addConstraintToCheck(IConstraint constraintToAdd) {
		innerConstraint.addConstraintToCheck(constraintToAdd);
	}*/
		
}
