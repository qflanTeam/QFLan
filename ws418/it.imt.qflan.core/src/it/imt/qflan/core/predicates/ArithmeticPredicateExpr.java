package it.imt.qflan.core.predicates;

import java.util.Set;

import it.imt.qflan.core.features.ConcreteFeature;
import it.imt.qflan.core.predicates.interfaces.IPredicateExpr;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public class ArithmeticPredicateExpr implements IPredicateExpr{

	private IPredicateExpr firstOperand;
	private IPredicateExpr secondOperand;
	private ArithmeticOperation op;
	
	public ArithmeticPredicateExpr(IPredicateExpr firstOperand,
			IPredicateExpr secondOperand, ArithmeticOperation op) {
		super();
		this.firstOperand = firstOperand;
		this.secondOperand = secondOperand;
		this.op = op;
	}

	@Override
	public double eval(Set<ConcreteFeature> installedFeatures) {
		double first = firstOperand.eval(installedFeatures);
		double second = secondOperand.eval(installedFeatures);
		switch (op) {
		case SUM:
			return first+second;
		case SUB:
			return first-second;
		case MULT:
			return first*second;
		default:
			throw new UnsupportedOperationException(op.toString());
		} 
	}

	@Override
	public String toString() {
		return "(" + firstOperand + getMathSymbol(op) + secondOperand+")";
	}
	
	
	public static char getMathSymbol(ArithmeticOperation o){
		switch (o) {
		case SUM:
			return '+';
		case SUB:
			return '-';
		case MULT:
			return '*';
		default:
			throw new UnsupportedOperationException(o.toString());
		} 
	}

	@Override
	public void addConstraintToInvolvedActions(IConstraint constraintToAdd) {
		firstOperand.addConstraintToInvolvedActions(constraintToAdd);
		secondOperand.addConstraintToInvolvedActions(constraintToAdd);
	}
	

}
