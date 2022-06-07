/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xat;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;//icluye todas las librerias del campo java.net
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Alcampo
 */
public class MySocket extends Socket {

    Socket socket;
//Crea un stream socket y lo conecta con el puerto especificado por el numero en el host name 

    public MySocket(String host, int port) {
        try {
            this.socket = new Socket(host, port);
        } catch (IOException ex) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public MySocket(Socket socket) {
        this.socket = socket;
    }

    public void MyConnect(SocketAddress endpoint) {
        try {
            this.socket.connect(endpoint);
        } catch (IOException ex) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public InputStream MyGetInputStream() {
        try {
            return socket.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public OutputStream MyGetOutputStream() {
        try {
            return socket.getOutputStream();
        } catch (IOException ex) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
