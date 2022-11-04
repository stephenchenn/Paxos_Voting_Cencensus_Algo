import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {
    private ServerSocket serverSocket;
    private int port;
    private boolean running = false;
    private Acceptor acceptor;

    public SocketServer(Acceptor acceptor, int port) {
        this.port = port;
        this.acceptor = acceptor;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            this.start();
            System.out.println("Server started on port: " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                System.out.println("Listening for a connection");

                // Call accept() to receive the next connection
                Socket socket = serverSocket.accept();

                // Pass the socket to the RequestHandler thread for processing
                RequestHandler requestHandler = new RequestHandler(socket, acceptor);
                requestHandler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class RequestHandler extends Thread {
    private Socket socket;
    private Acceptor acceptor;

    RequestHandler(Socket socket, Acceptor acceptor) {
        this.socket = socket;
        this.acceptor = acceptor;
    }

    @Override
    public void run() {
        try {
            System.out.println("Server received a connection");

            // Get input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            int id = Integer.parseInt(in.readLine());
            String uid = in.readLine();

            ProposalID proposalID = new ProposalID(id, uid);
            Promise promise;

            synchronized(this){
                promise = this.acceptor.receivePrepare(uid, proposalID);
            }
            
            if (promise != null) {
                String p_acceptorUID = promise.acceptorUID;
                String p_proposal_number = String.valueOf(promise.proposalID.getNumber());
                String p_proposal_uid = promise.proposalID.getUID();
                String p_previous_number = null;
                String p_previous_uid = null;
                if (promise.previousID != null){
                    p_previous_number = String.valueOf(promise.previousID.getNumber());
                    p_previous_uid = promise.previousID.getUID();
                }
                String p_acceptedValue = String.valueOf(promise.acceptedValue);

                out.println(p_acceptorUID);
                out.println(p_proposal_number);
                out.println(p_proposal_uid);
                out.println(p_previous_number);
                out.println(p_previous_uid);
                out.println(p_acceptedValue);
                out.flush();
            } else {
                // NACK
            }

            int a_proposal_number = Integer.parseInt(in.readLine());
            String a_proposal_uid = in.readLine();
            ProposalID a_proposalID = new ProposalID(a_proposal_number, a_proposal_uid);
            int a_value = Integer.parseInt(in.readLine());
            
            AcceptRequest accepted;
            synchronized(this){
                accepted = acceptor.receiveAcceptRequest(a_proposal_uid, a_proposalID, a_value);
            }

            if (accepted!=null){
                System.out.println(accepted.proposalID.getNumber());
                System.out.println(accepted.proposalID.getUID());
                System.out.println(accepted.proposedValue);
            } else {
                System.out.println("not accepted");
            }

            // Close our connection
            in.close();
            out.close();
            socket.close();

            System.out.println("Connection closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}