package it.imt.qflan.core.processes;

import java.util.ArrayList;
import java.util.Collection;

import it.imt.qflan.core.processes.interfaces.ICommitment;
import it.imt.qflan.core.processes.interfaces.IProcess;

public class ZeroProcess implements IProcess {

	public static final Collection<ICommitment> EMPTYCOMMITMENTS = new ArrayList<>(0);
	public static final IProcess ZERO = new ZeroProcess();
	
	private ZeroProcess() {
	}
	
	@Override
	public String toString() {
		//return "0";
		return "nil";
	}
	@Override
	public String getName() {
		return toString();
	}

	@Override
	public Collection<ICommitment> getCommitments() {
		return EMPTYCOMMITMENTS;
	}

	@Override
	public int getNumberOfProcessesCommittments() {
		return 1;
	}

	@Override
	public void startGettingCommitments() {
	}

	@Override
	public boolean hasCommitments() {
		return false;
	}

	@Override
	public ICommitment next() {
		return null;
	}
	
}
