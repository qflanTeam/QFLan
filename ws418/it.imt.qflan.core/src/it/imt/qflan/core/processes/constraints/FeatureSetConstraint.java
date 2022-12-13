package it.imt.qflan.core.processes.constraints;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.features.interfaces.IFeature;
import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.processes.interfaces.IAction;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public class FeatureSetConstraint implements IConstraint {

	private Set<IFeature> features;
	private FeatureSetCondition condition;
	private BoolExpr z3Encoding;
	
	public FeatureSetConstraint(Collection<IFeature> f,
			FeatureSetCondition condition, QFlanModel model) throws Z3Exception {
		super();
		features=new HashSet<>(f.size());
		for (IFeature iFeature : f) {
			features.add(iFeature);
		}
		this.condition = condition;
		/*if(condition.equals(FeatureSetCondition.ATMOSTONE) && features.size()!=2){
			throw new UnsupportedOperationException("Currently, this constraint can regard only 2 features. Use more constraints if needed. "+this.toString());
		}*/
		if(!QFlanModel.IGNOREZ3){
			this.z3Encoding = computeToZ3(model);
		}
	}
	
	public FeatureSetConstraint(IFeature firstFeature, IFeature secondFeature,
			FeatureSetCondition condition, QFlanModel model) throws Z3Exception {
		super();
		this.features = new LinkedHashSet<>(2);
		features.add(firstFeature);
		features.add(secondFeature);
		this.condition = condition;
		/*if(condition.equals(FeatureSetCondition.ATMOSTONE) && features.size()!=2){
			throw new UnsupportedOperationException("Currently, this constraint can regard only 2 features. Use more constraints if needed. "+this.toString());
		}*/
		if(!QFlanModel.IGNOREZ3){
			this.z3Encoding = computeToZ3(model);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		sb.append(getSymbol(condition));
		for (IFeature concreteFeature : features) {
			sb.append(concreteFeature.getName());
			sb.append(" ");
		}
		sb.append(")");
		return sb.toString();
	}

	private static String getSymbol(FeatureSetCondition c) {
		switch (c) {
		case ATMOSTONE:
			//return " * ";
			return "At most one: ";
		case ATLEASTONE:
			//return " \\/ ";
			return "At least one: ";
		default:
			throw new UnsupportedOperationException(c.toString());
		}
	}
	
	/*@Override
	public Collection<ConcreteFeature> involvedFeatures(){
		return features;
	}*/

	@Override
	public BoolExpr toZ3(QFlanModel model) {
		return this.z3Encoding;
	}
	
	
	private BoolExpr computeToZ3(QFlanModel model) throws Z3Exception {
		switch (condition) {
		case ATMOSTONE:
			ArithExpr one = model.getCTX().MkInt(1);
			ArithExpr zero = model.getCTX().MkInt(0);
			ArithExpr[] sum = new ArithExpr[features.size()];
			int j=0;
			for (IFeature feature : features) {
				sum[j] = (ArithExpr)model.getCTX().MkITE(HasFeature.toZ3(model, feature), one, zero);
				j++;
			}
			return model.getCTX().MkLe(model.getCTX().MkAdd(sum),one);
			//eq $store2smtConstraints( f * g , ignoreHasAndDo) = "(not (and (has " #+ toz3String(f) #+ ") (has " #+ toz3String(g) #+ ") ) )" .
			//return model.getCTX().MkNot(model.getCTX().MkAnd(all));
		case ATLEASTONE:
			BoolExpr[] all = new BoolExpr[features.size()];
			int i=0;
			for (IFeature feature : features) {
				all[i]=HasFeature.toZ3(model, feature);
				i++;
			}
			return model.getCTX().MkOr(all);
		default:
			throw new UnsupportedOperationException(condition.toString());
		}
	}

	@Override
	public boolean eval(QFlanModel model, IAction actionToExecute) {
		return eval(model);
	}
	
	@Override
	public boolean eval(QFlanModel model) {
		int count=0;
		for (IFeature feature : features) {
			if(model.isInstalled(feature)){
				count++;
				if(condition.equals(FeatureSetCondition.ATMOSTONE)&&count>1){
					return false;
				}
				else if(condition.equals(FeatureSetCondition.ATLEASTONE)&&count>0){
					return true;
				} 
			}
		}
		if(condition.equals(FeatureSetCondition.ATMOSTONE)){
			return count<=1;
		}
		else if(condition.equals(FeatureSetCondition.ATLEASTONE)){
			return count>0;
		}
		else {
			throw new UnsupportedOperationException(condition.toString());
		}
		
	}

	/*@Override
	public void addConstraintToCheck(IConstraint constraintToAdd) {
		for (ConcreteFeature concreteFeature : features) {
			//concreteFeature.addConstraintToCheckWhenExecuting(constraintToAdd);
			concreteFeature.addConstraintToCheckWhenInstalling(constraintToAdd);
		}
	}*/
	
	
}
