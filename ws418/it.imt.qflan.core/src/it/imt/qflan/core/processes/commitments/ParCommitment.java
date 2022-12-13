package it.imt.qflan.core.processes.commitments;

import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.processes.Parallel;
import it.imt.qflan.core.processes.ZeroProcess;
import it.imt.qflan.core.processes.interfaces.IAction;
import it.imt.qflan.core.processes.interfaces.ICommitment;
import it.imt.qflan.core.processes.interfaces.IProcess;
import it.imt.qflan.core.variables.SideEffect;

public class ParCommitment implements ICommitment {

	private ICommitment commitment;
	private IProcess rest;
	
	public ParCommitment(ICommitment commitment,IProcess rest) {
		this.commitment=commitment;
		this.rest=rest;
	}

	/*@Override
	public boolean isAllowed(Set<IConstraint> constraints, Set<ConcreteFeature> installedFeatures) {
		return commitment.isAllowed(constraints,installedFeatures);
	}*/
	
	public boolean isAllowed(QFlanModel model) throws Z3Exception {
		return commitment.isAllowed(model);
	}
	
	@Override
	public String toString() {
		return "(" + commitment + " | " + rest + ")";
	}

	/*@Override
	public double getRealRate() {
		return commitment.getActionRate() * rest.getRealNumberOfProcesses();
	}*/

	@Override
	public double getActionRate() {
		return commitment.getActionRate();
	}
	
	@Override
	public IAction getAction() {
		return this.commitment.getAction();
	}
	
	@Override
	public SideEffect[] getSideEffects() {
		return commitment.getSideEffects();
	}
	
	@Override
	public IProcess getContinuation() {
		if(commitment.getContinuation().equals(ZeroProcess.ZERO)){
			return rest;
		}
		else{
			return new Parallel(this.commitment.getContinuation(), rest);
		}
	}

	@Override
	public boolean modifiesStore() {
		return commitment.modifiesStore();
	}

}
