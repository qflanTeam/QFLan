package it.imt.qflan.core.processes.actions;


import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.features.ConcreteFeature;
import it.imt.qflan.core.model.QFlanModel;

public class InstallAction extends ActionCommonCode {

	private ConcreteFeature feature;
	//install/uninstall
	private boolean isInstall;
	
	@Override
	public boolean modifiesStore() {
		return true;
	}
	
	public boolean getIsInstall(){
		return isInstall;
	}
	
	public InstallAction(ConcreteFeature feature, boolean isInstall) {
		super((isInstall)?"install("+feature.getName()+")":"uninstall("+feature.getName()+")");		
		this.feature=feature;
		this.isInstall=isInstall;
	}
	
	@Override
	public boolean isAllowedByOtherConstraints(QFlanModel model) throws Z3Exception {
		//I can install only if I don't have already the feature
		if(isInstall && model.isInstalled(feature)){
			return false;
		}
		//I can uninstall only if I have already the feature
		else if((!isInstall) && (!model.isInstalled(feature))){
			return false;
		}
		//return true;
		//return false;
		//return model.checkSATInstalling(feature,isInstall);
		//return model.checkSATInstallingSlow(feature,isInstall);
		return model.checkInstallNoZ3(this);
	}
	
	@Override
	public boolean isAllowedByActionConstraints(QFlanModel model) throws Z3Exception {
		//I can install only if I don't have already the feature
		if(isInstall && model.isInstalled(feature)){
			return false;
		}
		//I can uninstall only if I have already the feature
		else if((!isInstall) && (!model.isInstalled(feature))){
			return false;
		}
		return model.checkExecuteNormalActionOrFeatureNoZ3(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((feature == null) ? 0 : feature.hashCode());
		result = prime * result + (isInstall ? 1231 : 1237);
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
		InstallAction other = (InstallAction) obj;
		if (feature == null) {
			if (other.feature != null)
				return false;
		} else if (!feature.equals(other.feature))
			return false;
		if (isInstall != other.isInstall)
			return false;
		return true;
	}

	public ConcreteFeature getFeature() {
		return feature;
	}
	
	/*@Override
	public Collection<IConstraint> getConstraintsToCheckWhenExecuting() {
		return feature.getConstraintsToCheckWhenInstalling();
	}*/

}
