package it.imt.qflan.core.predicates;

import java.util.HashMap;
//import java.util.Map.Entry;
import java.util.Set;

import it.imt.qflan.core.features.ConcreteFeature;
import it.imt.qflan.core.features.interfaces.IFeature;
import it.imt.qflan.core.predicates.interfaces.IPredicateDef;
//import it.imt.qflan.core.variables.MathEval;

public class PredicateDef implements IPredicateDef {
	
	private HashMap<ConcreteFeature, Double> values;
	private String name;
	//private MathEval math;
	
	public PredicateDef(String name) {
		//values=new HashMap<>();
		this.name=name;//.toLowerCase();
	}
	
	@Override
	public double eval(IFeature feature, Set<ConcreteFeature> installedFeatures) {
		if(values==null){
			return 0;
		}
		return feature.evalPredicate(values,name,installedFeatures);
	}

	@Override
	public void setValue(ConcreteFeature feature, double value) {
		if(values==null){
			values = new HashMap<>();
		}
		values.put(feature, value);
		//setMathVariable(feature, value);
	}

	/*public void setMathVariable(ConcreteFeature feature, double value) {
		if(math!=null){
			math.setVariable(getName()+"["+feature.getName()+"]", value);
		}
	}*/
	
	/*@Override
	public void setMath(MathEval math) {
		this.math=math;
		if(values!=null){
			for(Entry<ConcreteFeature, Double> v : values.entrySet()){
				setMathVariable(v.getKey(), v.getValue());
			}
		}
	}*/

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name+": "+values;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		PredicateDef other = (PredicateDef) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
