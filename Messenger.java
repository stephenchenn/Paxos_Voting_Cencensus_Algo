public class Messenger{

    protected SocketServer server;
    protected SocketClient client;

    public Messenger () {
        // default constructor
    }

    public Messenger (SocketServer server) {
        this.server = server;
    }

    public Messenger (SocketClient client) {
        this.client = client;
    }

    public void startListening(Acceptor acceptor, int port){
        server.startServer();
    }

    public void sendPrepare(Proposer proposer, ProposalID proposalID, String ip, int port){
        client.startClient(proposalID, ip, port);
        // System.out.println("messenger: sending prepare: seq: " + proposalID.getNumber() + " uid: " + proposalID.getUID());
    }

	public void sendPromise(String acceptorUID, ProposalID proposalID, ProposalID previousID, int acceptedValue){
        System.out.println("messenger: sending promise: uid: " + acceptorUID + " seq: " + proposalID.getNumber() + " accepted val: " + acceptedValue);
    }

	public void sendAccept(ProposalID proposalID, int proposalValue){
        System.out.println("messenger: sending accept: uid: " + proposalID.getUID() + " seq: " + proposalID.getNumber() + " proposal value: " + proposalValue);
    }

	public void sendAccepted(ProposalID proposalID, int acceptedValue){
        System.out.println("messenger: sending accepted");
    }
	
	public void onResolution(ProposalID proposalID, int value){
        System.out.println("messenger: on resolution");
    }
}
