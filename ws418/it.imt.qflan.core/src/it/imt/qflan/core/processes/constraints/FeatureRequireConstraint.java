package it.imt.qflan.core.processes.constraints;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.features.interfaces.IFeature;
import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.processes.interfaces.IAction;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public class FeatureRequireConstraint implements IConstraint {

	private IFeature requirer;
	private IFeature required;
	private BoolExpr z3Encoding;
	
	public FeatureRequireConstraint(IFeature requirer, IFeature required, QFlanModel model) throws Z3Exception {
		super();
		this.requirer = requirer;
		this.required = required;
		if(!QFlanModel.IGNOREZ3){
			this.z3Encoding = computeToZ3(model);
		}
	}
	
	@Override
	public String toString() {
		return "(" + requirer.getName() + " requires " + required.getName() + ")"; 
	}
	
	@Override
	public BoolExpr toZ3(QFlanModel model) {
		return this.z3Encoding;
	}

	public BoolExpr computeToZ3(QFlanModel model) throws Z3Exception {
		// eq $store2smtConstraints( f |> g , ignoreHasAndDo) =  "(implies (has " #+ toz3String(f) #+ ") (has " #+ toz3String(g) #+ ")) " .
		/*Expr hasRequirer = model.getHasFunc().Apply(model.getActionAndFeatureToZ3(requirer));
		Expr hasRequired = model.getHasFunc().Apply(model.getActionAndFeatureToZ3(required));
		return model.getCTX().MkImplies(model.getCTX().MkEq(hasRequirer, model.getCTX().MkTrue()), 
				model.getCTX().MkEq(hasRequired, model.getCTX().MkTrue()));*/
		
		return model.getCTX().MkImplies(HasFeature.toZ3(model, requirer),HasFeature.toZ3(model, required));
		
	}

	@Override
	public boolean eval(QFlanModel model) {
		return (!model.isInstalled(requirer)) || model.isInstalled(required) ;
	}
	
	@Override
	public boolean eval(QFlanModel model, IAction actionToExecute) {
		return eval(model);
	}

	/*@Override
	public void addConstraintToCheck(IConstraint constraintToAdd) {
		requirer.addConstraintToCheckWhenInstalling(constraintToAdd);
		required.addConstraintToCheckWhenInstalling(constraintToAdd);
	}*/
	
	
}
