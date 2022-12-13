package it.imt.qflan.core.model;

import it.imt.qflan.core.processes.interfaces.ICommitment;

public class DoubleAndCommitment implements Comparable<DoubleAndCommitment>{
	
	private double rate;
	private ICommitment commitment;
	
	public DoubleAndCommitment(double rate, ICommitment commitment) {
		super();
		this.rate = rate;
		this.commitment = commitment;
	}

	public DoubleAndCommitment(double rate) {
		this.rate = rate;
	}

	protected double getRate() {
		return rate;
	}

	public ICommitment getCommitment() {
		return commitment;
	}
	
	@Override
	public String toString() {
		if(commitment==null){
			return String.valueOf(rate);
		}
		else{
			return rate+" "+commitment;
		}
	}
	
	@Override
	public int compareTo(DoubleAndCommitment o) {
		return Double.compare(rate, o.getRate());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(rate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		DoubleAndCommitment other = (DoubleAndCommitment) obj;
		if (Double.doubleToLongBits(rate) != Double
				.doubleToLongBits(other.rate))
			return false;
		return true;
	}
	
	

}
