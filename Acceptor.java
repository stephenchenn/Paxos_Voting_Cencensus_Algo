public class Acceptor {

	protected Messenger messenger;
	protected String acceptorUID;
	protected ProposalID promisedID;
	protected ProposalID acceptedID = null;
	protected int acceptedValue = -1;

	public Acceptor(String acceptorUID) {
		this.acceptorUID = acceptorUID;
	}

	public void setMessenger (Messenger messenger){
		this.messenger = messenger;
	}

	public void start (){
		this.messenger.startListening();
	}

	// acceptor’s prepare(n) handler:
	public Promise receivePrepare(String fromUID, ProposalID proposalID) {

		System.out.println("receive prepare");

		if (this.promisedID != null && proposalID.equals(promisedID)) { // duplicate message
			messenger.sendPromise(acceptorUID, proposalID, acceptedID, acceptedValue);
		} else if (this.promisedID == null || proposalID.isGreaterThan(promisedID)) { // if n > n_p
			// n_p = n
			promisedID = proposalID;
			// reply prepare_ok(n, n_a, v_a)
			Promise promise = new Promise(acceptorUID, proposalID, acceptedID, acceptedValue);
			return promise;
			// messenger.sendPromise(acceptorUID, proposalID, acceptedID, acceptedValue);
		}
		return null;
	}

	// acceptor’s accept(n, v) handler:
	public AcceptRequest receiveAcceptRequest(String fromUID, ProposalID proposalID, int value) { // if n >= n_p
		if (promisedID == null || proposalID.isGreaterThan(promisedID) || proposalID.equals(promisedID)) {
			promisedID = proposalID; // n_p = n
			acceptedID = proposalID; // n_a = n
			acceptedValue = value; // v_a = v

			AcceptRequest accepted = new AcceptRequest(acceptedID, acceptedValue);
			return accepted; // reply accept_ok(n)

			// messenger.sendAccepted(acceptedID, acceptedValue); 
		} else {
			return null;
		}
	}

	public Messenger getMessenger() {
		return messenger;
	}

	public ProposalID getPromisedID() {
		return promisedID;
	}

	public ProposalID getAcceptedID() {
		return acceptedID;
	}

	public int getAcceptedValue() {
		return acceptedValue;
	}

	public String getAcceptorUID() {
		return acceptorUID;
	}

}
