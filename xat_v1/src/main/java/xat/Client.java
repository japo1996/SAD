/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alcampo
 */
public class Client {

    public static final int SERVER_PORT = 4000;
    public static final String SERVER_HOST = "localhost";
    public static String name, lastLine = "initial";

    public static void main(String[] args) throws IOException {

        //Iniciamos socket con puerto 4000 y nombre MyHost
        MySocket socket = new MySocket(SERVER_HOST, SERVER_PORT);
        //Iniciamos la salida de texto del socket creado
        PrintWriter out = new PrintWriter(socket.MyGetOutputStream(), true);
        //Iniciamos la entrada de texto del socket creado
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.MyGetInputStream()));
        //Iniciamos scanner (lector del teclado)
        Scanner Keyboard = new Scanner(System.in);

        //Creamos un thread que se dedique a leer las lineas del teclado y las envie al servidor
        Thread inputFlow = new Thread(new Runnable() {
            public void run() {
                String line;
                while (( line = Keyboard.nextLine()) != null) {
                    lastLine = line;
                    out.print(line + "\n");
                    out.flush();
                }
            }
        });
        //Creamos un thread que se dedique a leer las lineas del servidor enviadas desde otro cliente 
        //y las imprime por pantalla.
        Thread outputFlow = new Thread(new Runnable() {
            public void run() {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        if (message.contains(lastLine) && message.contains("joined")) {
                            name = lastLine;
                        } else if (name == null  || !message.contains(name)) {
                            System.out.println(message);
                        } 
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        inputFlow.start();
        outputFlow.start();
        System.out.println("Client Started correctly");
        System.out.println("insert:\nlogout: to close the connection with the server");
        System.out.println("CTRL+C: to close the chat");
    }
}
