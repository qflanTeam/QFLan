package it.imt.qflan.core.processes.constraints;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.processes.interfaces.IAction;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public class FalseConstraint implements IConstraint {
	
	@Override
	public String toString() {
		return "false";
	}
	
	@Override
	public BoolExpr toZ3(QFlanModel model) throws Z3Exception {
		//eq $store2smtConstraints(False, ignoreHasAndDo) = "false" .
		return model.getCTX().MkFalse();
	}

	@Override
	public boolean eval(QFlanModel model, IAction actionToExecute) {
		return false;
	}
	
	@Override
	public boolean eval(QFlanModel model) {
		return false;
	}

	/*@Override
	public void addConstraintToCheck(IConstraint constraintToAdd) {
	}*/
	
}
