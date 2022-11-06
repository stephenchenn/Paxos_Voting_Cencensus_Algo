import java.lang.String;
import java.net.ServerSocket;
import java.io.IOException;

public class Paxos {

    static Proposer createProposer (String proposerUID, int quorumSize) {
        Proposer proposer = new Proposer(proposerUID, quorumSize);
        SocketClient client = new SocketClient(proposer);
        Messenger proposerMessenger = new Messenger(client);
        proposer.setMessenger(proposerMessenger);
        proposer.setProposal(Integer.parseInt(proposerUID));
        return proposer;
    }

    static Learner createLearner (String l_ip, int l_port, int quorumSize) {
        Learner learner = new Learner(quorumSize);
        SocketServer l_server = new SocketServer(learner, l_port);
        Messenger messenger = new Messenger(l_server);
        learner.setMessenger(messenger);
        return learner;
    }

    static Acceptor createAcceptor (String acceptorUID, int a_port, String l_ip, int l_port) {
        Acceptor acceptor = new Acceptor(acceptorUID);
        SocketServer server = new SocketServer(acceptor, a_port, l_ip, l_port);
        Messenger acceptorMessenger1 = new Messenger(server);
        acceptor.setMessenger(acceptorMessenger1);
        return acceptor;
    }

    public static void main(String[] args) throws IOException {

        // implemeting proposers as clients, acceptors and learners as servers

        // 9 members in the council

        int quorumSize = 5;

        // proposers
        Proposer proposer1 = createProposer("1", quorumSize);
        Proposer proposer2 = createProposer("2", quorumSize);
        Proposer proposer3 = createProposer("3", quorumSize);
        
        // learner
        int l_port = 5555;
        String l_ip = "127.0.0.1";
        Learner learner = createLearner(l_ip, l_port, quorumSize);
        learner.start();
 
        // acceptors
        int _ports[] = new int[9];
        try {
            for (int i=0; i<9 ; i++){
                ServerSocket s = new ServerSocket(0);
                _ports[i] = s.getLocalPort();
                System.out.println("port: " + _ports[i]);
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Acceptor acceptor1 = createAcceptor("1", _ports[0], l_ip, l_port);
        Acceptor acceptor2 = createAcceptor("2", _ports[1], l_ip, l_port);
        Acceptor acceptor3 = createAcceptor("3", _ports[2], l_ip, l_port);
        Acceptor acceptor4 = createAcceptor("4", _ports[3], l_ip, l_port);
        Acceptor acceptor5 = createAcceptor("5", _ports[4], l_ip, l_port);
        Acceptor acceptor6 = createAcceptor("6", _ports[5], l_ip, l_port);
        Acceptor acceptor7 = createAcceptor("7", _ports[6], l_ip, l_port);
        Acceptor acceptor8 = createAcceptor("8", _ports[7], l_ip, l_port);
        Acceptor acceptor9 = createAcceptor("9", _ports[8], l_ip, l_port);

        acceptor1.start();
        acceptor2.start();
        acceptor3.start();
        acceptor4.start();
        acceptor5.start();
        acceptor6.start();
        acceptor7.start();
        acceptor8.start();
        acceptor9.start();
        
        // acceptor1.getMessenger().startListening(acceptor1, port);

        // System.out.println("proposer: prepare");
        String ip1 = "127.0.0.1";
        String ip2 = "127.0.0.1";
        String ip3 = "127.0.0.1";
        String ip4 = "127.0.0.1";
        String ip5 = "127.0.0.1";
        String ip6 = "127.0.0.1";
        String ip7 = "127.0.0.1";
        String ip8 = "127.0.0.1";
        String ip9 = "127.0.0.1";
        
        //
        String[] ips = {ip1, ip2, ip3, ip4, ip5, ip6, ip7, ip8, ip9};
        // make SocketClient a thread
        proposer1.prepare(ips, _ports);
        proposer2.prepare(ips, _ports);
        proposer3.prepare(ips, _ports);
    }
}