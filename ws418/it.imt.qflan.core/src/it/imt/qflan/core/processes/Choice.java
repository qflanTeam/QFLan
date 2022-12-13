package it.imt.qflan.core.processes;

import java.util.ArrayList;
import java.util.Collection;

import it.imt.qflan.core.processes.interfaces.ICommitment;
import it.imt.qflan.core.processes.interfaces.IProcess;

public class Choice implements IProcess {

	private IProcess first;
	private IProcess second;
	private Collection<ICommitment> commitments;
	private int realNumberOfProcesses=-1;
	private boolean scanningFirst;
	
	public Choice(IProcess first, IProcess second) {
		super();
		this.first = first;
		this.second = second;
		//computeCommitments();
		//computeRealNumberOfProcesses();
		scanningFirst=false;
	}
	

	@Override
	public String toString() {
		return "("+first+" + "+second+")";
	}
	
	@Override
	public String getName() {
		return "("+first.getName()+" + "+second.getName()+")";
	}

	@Override
	public Collection<ICommitment> getCommitments() {
		if(commitments==null){
			computeCommitments();
		}
		return commitments;
	}
	
	private void computeCommitments(){
		commitments = new ArrayList<>(first.getCommitments().size()+second.getCommitments().size());
		commitments.addAll(first.getCommitments());
		commitments.addAll(second.getCommitments());
	}
	
	private void computeRealNumberOfProcesses() {
		realNumberOfProcesses=first.getNumberOfProcessesCommittments()+second.getNumberOfProcessesCommittments();
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
		second.startGettingCommitments();
		scanningFirst=true;
	}


	@Override
	public boolean hasCommitments() {
		if(scanningFirst && first.hasCommitments()){
			return true;
		}
		else{
			return second.hasCommitments();
		}
	}


	@Override
	public ICommitment next() {
		if(scanningFirst){
			if(first.hasCommitments()){
				return first.next();
			}
			else{
				scanningFirst=false;
				return second.next();
			}
		}
		else{
			return second.next();
		}
	}


	
	
}
