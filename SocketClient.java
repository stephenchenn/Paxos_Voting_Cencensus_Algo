import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintStream;

public class SocketClient {
    private Socket socket;
    private Proposer proposer;

    public class MyRunnable implements Runnable {
        private SocketClient client;
        private ProposalID proposalID;

        public MyRunnable(SocketClient client, ProposalID proposalID) {
            this.client = client;
            this.proposalID = proposalID;
        }

        public void run() {
            try {
                client.clientThread(proposalID);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public SocketClient(Proposer proposer) {
        this.proposer = proposer;
    }

    public void startClient(ProposalID proposalID, String ip, int port) {
        try {
            // Connect to the server
            socket = new Socket(ip, port);
            Runnable r = new MyRunnable(this, proposalID);
            new Thread(r).start();
            System.out.println("Client connected to " + ip + ":" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clientThread(ProposalID proposalID) throws InterruptedException {
        try {
            // Create input and output streams to read from and write to the server
            PrintStream out = new PrintStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Follow the HTTP protocol of GET <path> HTTP/1.0 followed by an empty line
            String number = String.valueOf(proposalID.getNumber());
            String uid = proposalID.getUID();

            out.println(number);
            out.println(uid);

            String p_acceptorUID = in.readLine();
            int p_proposal_number = Integer.parseInt(in.readLine());
            String p_proposal_uid = in.readLine();

            ProposalID p_proposalID = new ProposalID(p_proposal_number, p_proposal_uid);

            ProposalID p_prevAcceptedID = null;
            String temp_previous_num = in.readLine();
            String temp_previous_uid = in.readLine();
            if ((temp_previous_num == "null") || (temp_previous_uid == "null")) {
                int p_previous_number = Integer.parseInt(temp_previous_num);
                String p_previous_uid = temp_previous_uid;
                p_prevAcceptedID = new ProposalID(p_previous_number, p_previous_uid);
            }

            int p_acceptedValue = Integer.parseInt(in.readLine());

            System.out.println(p_acceptorUID);
            System.out.println(p_proposalID.getNumber());
            System.out.println(p_proposalID.getUID());
            System.out.println(p_prevAcceptedID);
            System.out.println(p_acceptedValue);

            // maybe proposer should inform instead of busy wait? think tmr

            // every 5 seconds check with acceptor to see if majority is reached
            // if (accReq == null) {
            // boolean promiseFromMajority = false;
            // while (!promiseFromMajority) {
            // System.out.println("not rdy");
            // TimeUnit.SECONDS.sleep(5);
            // accReq = proposer.checkPromiseCount();
            // if (accReq != null) {
            // promiseFromMajority = true;
            // }
            // }
            // }

            synchronized (this) {
                AcceptRequest accReq = proposer.receivePromise(p_acceptorUID, p_proposalID, p_prevAcceptedID,
                        p_acceptedValue);
                // count ++;

                // if (count == 2){
                // // notifies the produce thread that it
                // // can wake up.
                // System.out.println("notifying, count: " + count);
                // notifyAll();
                // System.out.println("i go first!");
                // }else{
                // // releases the lock on shared resource
                // System.out.println("waiting, count: " + count);
                // wait();
                // System.out.println("back here");
                // Thread.sleep(5000);
                // }
                if (accReq != null) {
                    // notifies the produce thread that it can wake up.
                    System.out.println("notifying");
                    notifyAll();
                    System.out.println("i still go first tho");
                } else {
                    // releases the lock on shared resource

                    System.out.println("waiting");
                    wait();

                    System.out.println("back!");
                }
            }

            // Close our streams
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}