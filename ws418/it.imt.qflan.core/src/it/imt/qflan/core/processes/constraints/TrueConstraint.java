package it.imt.qflan.core.processes.constraints;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.processes.interfaces.IAction;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public class TrueConstraint implements IConstraint {

	@Override
	public String toString() {
		return "true";
	}

	@Override
	public BoolExpr toZ3(QFlanModel model) throws Z3Exception {
		//eq $store2smtConstraints(True, ignoreHasAndDo) = "true" .
		return model.getCTX().MkTrue();
	}

	@Override
	public boolean eval(QFlanModel model, IAction actionToExecute) {
		return eval(model);
	}
	
	@Override
	public boolean eval(QFlanModel model) {
		return true;
	}

	/*@Override
	public void addConstraintToCheck(IConstraint constraintToAdd) {
	}*/
	
}
