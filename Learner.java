import java.util.HashMap;

public class Learner{

	class Proposal {
		int acceptCount;
		int retentionCount;
		int value;

		Proposal(int acceptCount, int retentionCount, int value) {
			this.acceptCount = acceptCount;
			this.retentionCount = retentionCount;
			this.value = value;
		}
	}

	private final Messenger messenger;
	private final int quorumSize;
	private HashMap<ProposalID, Proposal> proposals = new HashMap<ProposalID, Proposal>();
	private HashMap<String, ProposalID> acceptors = new HashMap<String, ProposalID>();
	private int finalValue = -1;
	private ProposalID finalProposalID = null;

	public Learner(Messenger messenger, int quorumSize) {
		this.messenger = messenger;
		this.quorumSize = quorumSize;
	}

	public boolean isComplete() {
		return finalValue != -1;
	}

	public void receiveAccepted(String fromUID, ProposalID proposalID, int acceptedValue) {

		if (isComplete())
			return;

		// confusing start
		ProposalID oldPID = acceptors.get(fromUID);
		// make sure the received proposal is not duplicate or out of date
		if (oldPID != null && !proposalID.isGreaterThan(oldPID))
			return;

		acceptors.put(fromUID, proposalID);
		if (oldPID != null) {
			Proposal oldProposal = proposals.get(oldPID);
			oldProposal.retentionCount -= 1;
			if (oldProposal.retentionCount == 0)
				proposals.remove(oldPID);
		}
		//confusing end


		if (!proposals.containsKey(proposalID))
			proposals.put(proposalID, new Proposal(0, 0, acceptedValue));

		Proposal thisProposal = proposals.get(proposalID);

		thisProposal.acceptCount += 1;
		thisProposal.retentionCount += 1;

		// if accept_ok(n) from majority:
		if (thisProposal.acceptCount == quorumSize) {
			finalProposalID = proposalID;
			finalValue = acceptedValue;
			proposals.clear();
			acceptors.clear();

			// send decided(v’) to all
			messenger.onResolution(proposalID, acceptedValue);
		}
	}

	public int getQuorumSize() {
		return quorumSize;
	}

	public int getFinalValue() {
		return finalValue;
	}

	public ProposalID getFinalProposalID() {
		return finalProposalID;
	}
}
