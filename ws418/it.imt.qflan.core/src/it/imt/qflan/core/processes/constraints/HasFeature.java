package it.imt.qflan.core.processes.constraints;

import java.util.HashMap;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.features.interfaces.IFeature;
//import it.imt.qflan.core.features.ConcreteFeature;
import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.processes.interfaces.IAction;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public class HasFeature implements IConstraint {

	private static HashMap<IFeature,BoolExpr> allHasToZ3=new HashMap<>();
	
	private IFeature feature;
	private BoolExpr z3Encoding;
	private boolean representsMandatory;
	
	public boolean representsMandatory(){
		return representsMandatory;
	}
	
	public HasFeature(IFeature feature, QFlanModel model,boolean representsMandatory) throws Z3Exception{
		this.feature=feature;
		if(feature==null){
			System.out.println("null");
		}
		this.representsMandatory=representsMandatory;
		if(!QFlanModel.IGNOREZ3){
			this.z3Encoding=computeZ3(model);
		}
	}
	
	public HasFeature(IFeature feature, QFlanModel model) throws Z3Exception{
		this.feature=feature;
		if(feature==null){
			System.out.println("null");
		}
		this.representsMandatory=false;
		if(!QFlanModel.IGNOREZ3){
			this.z3Encoding=computeZ3(model);
		}
	}
	
	@Override
	public String toString() {
		if(representsMandatory){
			return "mandatory("+feature.getName()+")";
		}
		else{
			return "has("+feature.getName()+")";
		}
		 
	}

	private BoolExpr computeZ3(QFlanModel model) throws Z3Exception {
		Expr hasFeature = model.getHasFunc().Apply(model.getActionAndFeatureToZ3(feature));
		return model.getCTX().MkEq(hasFeature, model.getCTX().MkTrue());
	}
	
	@Override
	public BoolExpr toZ3(QFlanModel model) {
		return this.z3Encoding;
	}
	
	public static BoolExpr toZ3(QFlanModel model, IFeature aFeature) throws Z3Exception{
		//Build a vector, and create only once each has.
		BoolExpr hasFeatureIsTrue = allHasToZ3.get(aFeature);
		if(hasFeatureIsTrue==null){
			Expr hasFeature = model.getHasFunc().Apply(model.getActionAndFeatureToZ3(aFeature));
			hasFeatureIsTrue= model.getCTX().MkEq(hasFeature, model.getCTX().MkTrue());
			allHasToZ3.put(aFeature, hasFeatureIsTrue);
		}
		return hasFeatureIsTrue;
	}

	@Override
	public boolean eval(QFlanModel model, IAction actionToExecute) {
		//Pay attention: we already have the effecto of actionToExecute in the store. 
		return eval(model);
	}
	
	@Override
	public boolean eval(QFlanModel model) {
		//Pay attention: we already have the effecto of actionToExecute in the store. 
		return model.isInstalled(feature);
	}

	/*@Override
	public void addConstraintToCheck(IConstraint constraintToAdd) {
		if(representsMandatory){
			feature.addConstraintToCheckWhenInstalling(constraintToAdd);
		}
		else{
			feature.addConstraintToCheckWhenExecuting(constraintToAdd);
		}
	}*/
	

}
