package edu.utexas.ee360p_teamproject.ClientRequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by aria on 4/22/17.
 */

class ClientSocket {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientSocket(String ip, int port) throws IOException {
        clientSocket      = new Socket(ip, port);
        InputStream input = clientSocket.getInputStream();
        out               = new PrintWriter(clientSocket.getOutputStream(), true);
        in                = new BufferedReader(new InputStreamReader((input)));
    }

    public BufferedReader inStream() {
        return in;
    }

    public PrintWriter outStream() {
        return out;
    }
}
