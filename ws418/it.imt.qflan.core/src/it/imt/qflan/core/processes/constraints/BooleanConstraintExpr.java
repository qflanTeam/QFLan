package it.imt.qflan.core.processes.constraints;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.processes.interfaces.IAction;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public class BooleanConstraintExpr implements IConstraint {

	private IConstraint first;
	private IConstraint second;
	private BooleanConnector op;
	
	public BooleanConstraintExpr(IConstraint first, IConstraint second,
			BooleanConnector op) {
		super();
		this.first = first;
		this.second = second;
		this.op = op;
		//no need to add this constraint to involved actions-  
	}
	
	@Override
	public String toString() {
		return "("+first.toString()+getSymbol(op)+second.toString()+")";
	}

	private static String getSymbol(BooleanConnector op) {
		switch (op) {
		case AND:
			return " /\\ ";
		case IMPLIES:
			return " -> ";
		case OR:
			return " \\/ ";
		default:
			throw new UnsupportedOperationException(op.toString());
		}
	}
	
	@Override
	public BoolExpr toZ3(QFlanModel model) throws Z3Exception {
		switch (op) {
		case AND:
			return model.getCTX().MkAnd(new BoolExpr[] {first.toZ3(model),second.toZ3(model)});
		case IMPLIES:
			return model.getCTX().MkImplies(first.toZ3(model),second.toZ3(model));
		case OR:
			return model.getCTX().MkOr(new BoolExpr[] {first.toZ3(model),second.toZ3(model)});
		default:
			throw new UnsupportedOperationException(op.toString());
		}
	}

	@Override
	public boolean eval(QFlanModel model, IAction actionToExecute) {
		switch (op) {
		case AND:
			return   first.eval(model, actionToExecute)  && second.eval(model, actionToExecute);
		case IMPLIES:
			return (!first.eval(model, actionToExecute)) || second.eval(model, actionToExecute);
		case OR:
			return   first.eval(model, actionToExecute)  || second.eval(model, actionToExecute);
		default:
			throw new UnsupportedOperationException(op.toString());
		}
	}
	
	@Override
	public boolean eval(QFlanModel model) {
		switch (op) {
		case AND:
			return   first.eval(model)  && second.eval(model);
		case IMPLIES:
			return (!first.eval(model)) || second.eval(model);
		case OR:
			return   first.eval(model)  || second.eval(model);
		default:
			throw new UnsupportedOperationException(op.toString());
		}
	}

	/*@Override
	public void addConstraintToCheck(IConstraint constraintToAdd) {
		first.addConstraintToCheck(constraintToAdd);
		second.addConstraintToCheck(constraintToAdd);
	}*/
	
	
}
