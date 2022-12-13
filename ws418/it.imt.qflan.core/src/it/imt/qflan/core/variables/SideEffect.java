package it.imt.qflan.core.variables;

import java.util.Set;

import it.imt.qflan.core.features.ConcreteFeature;
import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.predicates.interfaces.IPredicateExpr;

public class SideEffect {

	private QFLanVariable variable;
	//private String updateExpression;
	private IPredicateExpr updateExpression;
	public SideEffect(QFLanVariable variable, IPredicateExpr updateExpression) {
		super();
		this.variable = variable;
		this.updateExpression = updateExpression;
	}
	
	public QFLanVariable getVariable() {
		return variable;
	}
	/*public String getNewValue() {
		return updateExpression;
	}*/
	public double evalUpdateExpression(Set<ConcreteFeature> installedFeatures){
		return updateExpression.eval(installedFeatures);
	}
	
	@Override
	public String toString() {
		return variable.getName()+"="+updateExpression;
	}

	public boolean isAllowed(QFlanModel model) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public static String sideEffectsToString(SideEffect[] se) {
		StringBuilder sb = new StringBuilder("{");
		for(int i=0;i<se.length;i++){
			sb.append(se[i].toString());
			if(i<se.length-1){
				sb.append(",");
			}
		}
		sb.append("}");
		return sb.toString();
	}
	
	
	
}
