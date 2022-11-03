import java.lang.String;

public class Paxos {
    public static void main(String[] args) {

        // proposer
        String proposerUID = "12";
        int quorumSize = 2; // change to 5
        Proposer proposer = new Proposer(proposerUID, quorumSize);
        SocketClient client = new SocketClient(proposer);
        Messenger proposerMessenger = new Messenger(client);
        proposer.setMessenger(proposerMessenger);

        // String uid = proposer.getProposerUID();
        // ProposalID id = proposer.getProposalID();

        // acceptor 1
        int port = 80;
        Acceptor acceptor = new Acceptor("30");
        SocketServer server = new SocketServer(acceptor, port);

        Messenger acceptorMessenger1 = new Messenger(server);
        acceptor.setMessenger(acceptorMessenger1);

        acceptor.start(port);
        
        // acceptor1.getMessenger().startListening(acceptor1, port);

        // System.out.println("proposer: prepare");
        String ip = "127.0.0.1";
        // make SocketClient a thread
        proposer.prepare(ip, port);
        // sending prepare
        
        // System.out.println("acceptor1: receive prepare");
        // acceptor1.receivePrepare(uid, id);









        // // sending promise

        // proposer.setProposal(1);

        // String fromUID = acceptor1.getAcceptorUID();
        // ProposalID proposalID = acceptor1.getPromisedID();
        // ProposalID prevAcceptedID = acceptor1.getAcceptedID();
        // int prevAcceptedValue = acceptor1.getAcceptedValue();

        // System.out.println("proposer: receive promise");
        // proposer.receivePromise(fromUID, proposalID, prevAcceptedID, prevAcceptedValue);



        // // acceptor 2
        // Messenger acceptorMessenger2 = new Messenger();
        // Acceptor acceptor2 = new Acceptor(acceptorMessenger2, "2");

        // System.out.println("acceptor2: receive prepare");
        // acceptor2.receivePrepare(uid, id);

        // String fromUID2 = acceptor2.getAcceptorUID();
        // ProposalID proposalID2 = acceptor2.getPromisedID();
        // ProposalID prevAcceptedID2 = acceptor2.getAcceptedID();
        // int prevAcceptedValue2 = acceptor2.getAcceptedValue();

        // System.out.println("proposer: receive promise");
        // proposer.receivePromise(fromUID2, proposalID2, prevAcceptedID2, prevAcceptedValue2);
        // // sending accept

        // Messenger learnerMessenger = new Messenger();
        // Learner learner = new Learner(learnerMessenger, quorumSize);

        // System.out.println("acceptor1: receive accept request");
        // acceptor1.receiveAcceptRequest(uid, id, proposer.getProposedValue());
        // // sending accepted

        // System.out.println("learner: receive accepted");
        // learner.receiveAccepted(acceptor1.getAcceptorUID(), acceptor1.getAcceptedID(), acceptor1.getAcceptedValue());

        // System.out.println("acceptor2: receive accept request");
        // acceptor2.receiveAcceptRequest(uid, id, proposer.getProposedValue());
        // // sending accepted
        
        // System.out.println("learner: receive accepted");
        // learner.receiveAccepted(acceptor2.getAcceptorUID(), acceptor2.getAcceptedID(), acceptor2.getAcceptedValue());
    }
}
