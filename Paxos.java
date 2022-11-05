import java.lang.String;

public class Paxos {
    public static void main(String[] args) {

        // implemeting proposers as clients, acceptors and learners as servers

        // proposer
        String proposerUID = "100";
        int quorumSize = 2; // change to 5
        Proposer proposer = new Proposer(proposerUID, quorumSize);
        SocketClient client = new SocketClient(proposer);
        Messenger proposerMessenger = new Messenger(client);
        proposer.setMessenger(proposerMessenger);
        proposer.setProposal(7);

        // learner
        int l_port = 5555;
        String l_ip = "127.0.0.1";
        Learner learner = new Learner(quorumSize);
        SocketServer l_server = new SocketServer(learner, l_port);
        Messenger messenger = new Messenger(l_server);
        learner.setMessenger(messenger);
        learner.start();

        // need to pass learner to acceptors

        // String uid = proposer.getProposerUID();
        // ProposalID id = proposer.getProposalID();
 
        // acceptor 1
        int port = 80;
        Acceptor acceptor = new Acceptor("200");
        SocketServer server = new SocketServer(acceptor, port, l_ip, l_port);

        Messenger acceptorMessenger1 = new Messenger(server);
        acceptor.setMessenger(acceptorMessenger1);

        acceptor.start();

        // acceptor 2
        int port2 = 81;
        Acceptor acceptor2 = new Acceptor("201");
        SocketServer server2 = new SocketServer(acceptor2, port2, l_ip, l_port);

        Messenger acceptorMessenger2 = new Messenger(server2);
        acceptor2.setMessenger(acceptorMessenger2);

        acceptor2.start();
        
        // acceptor1.getMessenger().startListening(acceptor1, port);

        // System.out.println("proposer: prepare");
        String ip1 = "127.0.0.1";
        String ip2 = "127.0.0.1";
        
        String[] ips = {ip1, ip2};
        int[] ports = {port, port2};
        // make SocketClient a thread
        proposer.prepare(ips, ports);
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
