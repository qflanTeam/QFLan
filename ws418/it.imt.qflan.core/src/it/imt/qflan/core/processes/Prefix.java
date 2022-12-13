package it.imt.qflan.core.processes;

import java.util.ArrayList;
import java.util.Collection;

import it.imt.qflan.core.processes.commitments.PrefixCommitment;
import it.imt.qflan.core.processes.interfaces.IAction;
import it.imt.qflan.core.processes.interfaces.ICommitment;
import it.imt.qflan.core.processes.interfaces.IProcess;
import it.imt.qflan.core.variables.SideEffect;

public class Prefix implements IProcess {

	private double rate;
	private IAction action;
	private IProcess continuation;
	private Collection<ICommitment> commitments;
	private boolean commitmentReturned;
	private PrefixCommitment commitment;
	
	private SideEffect[] sideEffects;
	
	/*public Prefix(double rate, IAction action,  IProcess continuation) {
		super();
		this.rate = rate;
		this.action = action;
		this.continuation = continuation;
		sideEffects=new SideEffect[0];
		//computeCommitments();
		commitmentReturned=false;
		commitment = new PrefixCommitment(action,rate,continuation);
	}*/
	
	public Prefix(double rate, IAction action, SideEffect[] sideEffects, IProcess continuation) {
		super();
		this.rate = rate;
		this.action = action;
		this.continuation = continuation;
		this.sideEffects=sideEffects;
		//computeCommitments();
		commitmentReturned=false;
		commitment = new PrefixCommitment(action,rate,sideEffects,continuation);
	}
	
	@Override
	public String toString() {
		return "("+action+","+rate+","+SideEffect.sideEffectsToString(sideEffects)+")."+continuation;
	}
	@Override
	public String getName() {
		return "("+action+","+rate+","+SideEffect.sideEffectsToString(sideEffects)+")."+continuation.getName();
	}

	@Override
	public Collection<ICommitment> getCommitments() {
		if(commitments==null){
			computeCommitments();
		}
		return commitments;
	}
	
	private void computeCommitments(){
		commitments = new ArrayList<>(1);
		commitments.add(commitment);
	}

	@Override
	public int getNumberOfProcessesCommittments() {
		return 1;
	}

	@Override
	public void startGettingCommitments() {
		commitmentReturned=false;
	}

	@Override
	public boolean hasCommitments() {
		return !commitmentReturned;
	}

	@Override
	public ICommitment next() {
		if(!commitmentReturned){
			commitmentReturned=true;
			return commitment;
		}
		else{
			return null;
		}
	}

	/*public IProcess createPrefixWithNewContinuation(IProcess newContinuation) {
		Prefix newPrefix = new Prefix(rate, action, newContinuation);
		return newPrefix;
	}*/
	
	
	
}
