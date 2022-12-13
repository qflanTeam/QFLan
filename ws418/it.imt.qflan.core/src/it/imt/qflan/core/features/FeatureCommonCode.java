package it.imt.qflan.core.features;

import it.imt.qflan.core.features.interfaces.IFeature;
import it.imt.qflan.core.processes.actions.ActionCommonCode;

public abstract class FeatureCommonCode extends ActionCommonCode implements IFeature {

	//private int id;
	private IFeature father;
	
	//By default all are optional.
	private boolean optional=true;
	
	//private static int NEXTID;
	@Override
	public void setOptional(boolean optional) {
		this.optional = optional;
	}
	@Override
	public boolean isOptional() {
		return optional;
	}
	
	public FeatureCommonCode(String name) {
		super(name);
	}
	
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
		FeatureCommonCode other = (FeatureCommonCode) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}

	/*protected int getId() {
		return id;
	}*/

	@Override
	public void setFather(IFeature father) {
		this.father = father;
	}
	
	@Override
	public IFeature getFather(){
		return father;
	}
	
}
