package it.imt.qflan.core.variables;

import java.util.Set;

import it.imt.qflan.core.features.ConcreteFeature;
import it.imt.qflan.core.predicates.interfaces.IPredicateExpr;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public class QFLanVariable implements IPredicateExpr{

	private String name;
	private double value;
	private final double initialValue;
	//private String expr;
	
	public QFLanVariable(String name, double value) {
		super();
		this.name = name;
		this.value=value;
		this.initialValue=value;
	}
	
	public double getInitialValue(){
		return initialValue;
	}
	
	public String getName() {
		return name;
	}
	/*public double getValue() {
		return value;
	}*/
	public void setValue(double value) {
		this.value=value;
	}
	
	@Override
	public String toString() {
		//return name+"[="+value+"]";
		return name;
	}
	
	public String toStringWithValue() {
		//return name+"[="+value+"]";
		return name+"="+value;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof QFLanVariable){
			return name.equals(((QFLanVariable) obj).getName());
		}
		else{
			return false;
		}
	}

	@Override
	public double eval(Set<ConcreteFeature> installedFeatures) {
		return value;
	}

	@Override
	public void addConstraintToInvolvedActions(IConstraint constraintToAdd) {
		//TODO check if we need to do something here
		
	}
	
}
