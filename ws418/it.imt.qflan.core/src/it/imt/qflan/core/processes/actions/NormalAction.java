package it.imt.qflan.core.processes.actions;

import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.model.QFlanModel;

public class NormalAction extends ActionCommonCode {

	public NormalAction(String name) {
		super(name);
	}
	
	/*@Override
	public boolean isAllowed(Set<IConstraint> constraints, Set<ConcreteFeature> installedFeatures) {
		//TODO
		return true;
	}*/
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NormalAction other = (NormalAction) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}

	@Override
	public boolean isAllowedByOtherConstraints(QFlanModel model) throws Z3Exception {
		//(#smtCall constraints2smtPrecompFeaturesAndContextInfoAndStaticConstraints(Cons  do(oa1),instFeat) == "sat" ) .
		/*Expr doAction = model.getDoFunc().Apply(model.getActionAndFeatureToZ3(this));
		return model.checkSAT(model.getCTX().MkEq(doAction, model.getCTX().MkTrue()));*/
		//return model.checkSATExecuteNormalActionOrFeature(this);
		//return model.checkSATSlow(this);
		//return true;
		//return false;
		
		//I need to check if the application of the side effects made the store false
		return model.checkOtherConstraints(this);
		//return model.checkExecuteNormalActionOrFeatureNoZ3(this);
		//return true;
	}
	
	@Override
	public boolean isAllowedByActionConstraints(QFlanModel model) throws Z3Exception {
		return model.checkExecuteNormalActionOrFeatureNoZ3(this);
	}
}
