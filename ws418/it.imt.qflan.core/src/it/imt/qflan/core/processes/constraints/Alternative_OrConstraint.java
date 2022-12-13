package it.imt.qflan.core.processes.constraints;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.features.AbstractFeature;
import it.imt.qflan.core.features.interfaces.IFeature;
import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.processes.interfaces.IAction;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public class Alternative_OrConstraint implements IConstraint {

	private AbstractFeature root;
	private Set<IFeature> features;
	private Alternative_ORCondition condition;
	private BoolExpr z3Encoding;
	
	public Alternative_OrConstraint(AbstractFeature root, Collection<IFeature> f,
			Alternative_ORCondition condition, QFlanModel model) throws Z3Exception {
		super();
		this.root=root;
		this.features=new HashSet<>(f.size());
		for (IFeature iFeature : f) {
			features.add(iFeature);
		}
		this.condition = condition;
		if(!QFlanModel.IGNOREZ3){
			this.z3Encoding = computeToZ3(model);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		sb.append(getSymbol(condition));
		for (IFeature feature : features) {
			sb.append(feature.getName());
			sb.append(" ");
		}
		sb.append(")");
		return sb.toString();
	}

	private static String getSymbol(Alternative_ORCondition c) {
		switch (c) {
		case XOR:
			return " XOR ";
		case OR:
			return " OR ";
		default:
			throw new UnsupportedOperationException(c.toString());
		}
	}

	@Override
	public boolean eval(QFlanModel model) {
		//In order for this constraint to be considered: the optional root must be currently installed. 
		//Otherwise the constraint is vacuously satisfied
		//If the root is mandatory I have to consider it.
		if(root.isOptional() && !root.isInstallled()){
			return true;
		}
		//In order for this constraint to be considered: all optional ancestors including root must be currently installed. 
		//Otherwise the constraint is vacuously satisfied
		/*IFeature ancestor =root;
		while(ancestor!=null){
			if(ancestor.isOptional() && !ancestor.isInstallled()){
				return true;
			}
			ancestor=ancestor.getFather();
		}*/
		
		//XOR: precisely one
		//OR: at least one
		int count=0;
		for (IFeature feature : features) {
			if(model.isInstalled(feature)){
				count++;
				if(condition.equals(Alternative_ORCondition.XOR)&&count>1){
					return false;
				}
				else if(condition.equals(Alternative_ORCondition.OR)&&count>0){
					return true;
				} 
			}
		}
		if(condition.equals(Alternative_ORCondition.XOR)){
			return count==1;
		}
		else if(condition.equals(Alternative_ORCondition.OR)){
			return count>0;
		}
		else {
			throw new UnsupportedOperationException(condition.toString());
		}
	}
	
	@Override
	public boolean eval(QFlanModel model, IAction actionToExecute) {
		return eval(model);
	}
	
	@Override
	public BoolExpr toZ3(QFlanModel model) {
		return this.z3Encoding;
	}
	
	
	private BoolExpr computeToZ3(QFlanModel model) throws Z3Exception {
		/*switch (condition) {
		case ATMOSTONE:
			ArithExpr one = model.getCTX().MkInt(1);
			ArithExpr zero = model.getCTX().MkInt(0);
			ArithExpr[] sum = new ArithExpr[features.size()];
			int j=0;
			for (ConcreteFeature feature : features) {
				sum[j] = (ArithExpr)model.getCTX().MkITE(HasFeature.toZ3(model, feature), one, zero);
				j++;
			}
			return model.getCTX().MkLe(model.getCTX().MkAdd(sum),one);
			//eq $store2smtConstraints( f * g , ignoreHasAndDo) = "(not (and (has " #+ toz3String(f) #+ ") (has " #+ toz3String(g) #+ ") ) )" .
			//return model.getCTX().MkNot(model.getCTX().MkAnd(all));
		case ATLEASTONE:
			BoolExpr[] all = new BoolExpr[features.size()];
			int i=0;
			for (ConcreteFeature feature : features) {
				all[i]=HasFeature.toZ3(model, feature);
				i++;
			}
			return model.getCTX().MkOr(all);
		default:
			throw new UnsupportedOperationException(condition.toString());
		}*/
		throw new UnsupportedOperationException("NOT IMPLEMENTED");
	}

}
