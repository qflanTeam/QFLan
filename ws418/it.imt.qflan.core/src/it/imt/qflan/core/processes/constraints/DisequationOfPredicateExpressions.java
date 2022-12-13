package it.imt.qflan.core.processes.constraints;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.RatNum;
import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.predicates.interfaces.IPredicateExpr;
import it.imt.qflan.core.processes.interfaces.IAction;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public class DisequationOfPredicateExpressions implements IConstraint {
	
	private IPredicateExpr lhs;
	private IPredicateExpr rhs;
	private PredicateExprComparator comp;
	
	public DisequationOfPredicateExpressions(IPredicateExpr lhs,
			IPredicateExpr rhs, PredicateExprComparator comp) {
		super();
		this.lhs = lhs;
		this.rhs = rhs;
		this.comp = comp;
	}

	private String getMathSymbol(PredicateExprComparator c) {
		switch (c) {
		case EQ:
			return "==";
		case GE:
			return ">";
		case GEQ:
			return ">=";
		case LE:
			return "<";
		case LEQ:
			return "<=";
		case NOTEQ:
			return "!=";
		default:
			throw new UnsupportedOperationException(c.toString());
		}
	}

	@Override
	public String toString() {
		return "(" + lhs + " "+ getMathSymbol(comp) +" " + rhs + ")";
	}
	
	@Override
	public boolean eval(QFlanModel model, IAction actionToExecute) {
		return eval(model);
	}
	
	@Override
	public boolean eval(QFlanModel model){
		double lhsVal = model.eval(lhs);
		double rhsVal = model.eval(rhs);
		switch (comp) {
		case EQ:
			return lhsVal == rhsVal;
		case GE:
			return lhsVal > rhsVal;
		case GEQ:
			return lhsVal >= rhsVal;
		case LE:
			return lhsVal < rhsVal;
		case LEQ:
			return lhsVal <= rhsVal;
		case NOTEQ:
			return lhsVal != rhsVal;
		default:
			throw new UnsupportedOperationException(comp.toString());
		}
	}
	
	@Override
	public BoolExpr toZ3(QFlanModel model) throws Z3Exception {
		RatNum lhsZ3 = model.getCTX().MkReal(String.valueOf(model.eval(lhs)));
		RatNum rhsz3 = model.getCTX().MkReal(String.valueOf(model.eval(rhs)));
		switch (comp) {
		case EQ:
			return model.getCTX().MkEq(lhsZ3, rhsz3);
		case GE:
			return model.getCTX().MkGt(lhsZ3, rhsz3);
		case GEQ:
			return model.getCTX().MkGe(lhsZ3, rhsz3);
		case LE:
			return model.getCTX().MkLt(lhsZ3, rhsz3);
		case LEQ:
			return model.getCTX().MkLe(lhsZ3, rhsz3);
		case NOTEQ:
			return model.getCTX().MkNot(model.getCTX().MkEq(lhsZ3, rhsz3));
		default:
			throw new UnsupportedOperationException(comp.toString());
		}
	}

	/*@Override
	public void addConstraintToCheck(IConstraint constraintToAdd) {
		lhs.addConstraintToInvolvedActions(constraintToAdd);
		rhs.addConstraintToInvolvedActions(constraintToAdd);
	}*/

}
