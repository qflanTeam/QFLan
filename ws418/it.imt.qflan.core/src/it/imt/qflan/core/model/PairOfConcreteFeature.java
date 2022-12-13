package it.imt.qflan.core.model;

import it.imt.qflan.core.features.ConcreteFeature;

public class PairOfConcreteFeature {

	private ConcreteFeature first;
	private ConcreteFeature second;
	
	public PairOfConcreteFeature(ConcreteFeature first, ConcreteFeature second) {
		super();
		this.first = first;
		this.second = second;
	}
	public ConcreteFeature getFirst() {
		return first;
	}
	public ConcreteFeature getSecond() {
		return second;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
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
		PairOfConcreteFeature other = (PairOfConcreteFeature) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}
	
	
}
