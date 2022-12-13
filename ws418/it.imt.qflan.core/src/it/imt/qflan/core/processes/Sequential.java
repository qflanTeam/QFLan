package it.imt.qflan.core.processes;

import java.util.ArrayList;
import java.util.Collection;

import it.imt.qflan.core.processes.commitments.SeqCommitment;
import it.imt.qflan.core.processes.interfaces.ICommitment;
import it.imt.qflan.core.processes.interfaces.IProcess;

public class Sequential implements IProcess{
	
	private IProcess first;
	private IProcess second;
	private Collection<ICommitment> commitments;
	private int realNumberOfProcesses=-1;
	
	public Sequential(IProcess first, IProcess second) {
		super();
		this.first = first;
		this.second = second;
		//computeCommitments();
		//computeRealNumberOfProcesses();
	}
	
	private void computeRealNumberOfProcesses() {
		realNumberOfProcesses = first.getNumberOfProcessesCommittments();
	}

	@Override
	public String toString() {
		return "("+first+" ; "+second+")";
	}
	@Override
	public String getName() {
		return "("+first.getName()+" ; "+second.getName()+")";
	}

	@Override
	public Collection<ICommitment> getCommitments() {
		if(commitments==null){
			computeCommitments();
		}
		return commitments;
	}
	
	private void computeCommitments(){
		commitments = new ArrayList<>(first.getCommitments().size());
		for (ICommitment commitment : first.getCommitments()) {
			commitments.add(new SeqCommitment(commitment,second));
		}
	}

	@Override
	public int getNumberOfProcessesCommittments() {
		if(realNumberOfProcesses==-1){
			computeRealNumberOfProcesses();
		}
		return realNumberOfProcesses;
	}

	@Override
	public void startGettingCommitments() {
		first.startGettingCommitments();
	}

	@Override
	public boolean hasCommitments() {
		return first.hasCommitments();
	}

	@Override
	public ICommitment next() {
		return first.next();
	}

}
