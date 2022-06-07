/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xat;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alcampo
 */
public class MyServerSocket extends ServerSocket {

    ServerSocket serverSocket;
    MySocket socket;

    public MyServerSocket(int port) throws IOException {

        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public MySocket accept() {
        try {
            return this.socket = new MySocket(serverSocket.accept());
        } catch (IOException ex) {
            Logger.getLogger(MyServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void close() {
        try {
            this.serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(MyServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
