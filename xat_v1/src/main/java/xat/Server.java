/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alcampo
 */
public class Server {

    private static final int PORT = 4000;
    //Mapa donde guardaremos los nombres y los Handlers de cada cliente siendo el Handler el socket ,input y output stream de cada cliente.
    public static ConcurrentHashMap<String, Handler> users = new ConcurrentHashMap<String, Handler>();

    public static void main(String[] args) throws Exception {
        System.out.println("Server is currently running");
        //Crea una cola de threads siendo 500 el maximo, si se une algun thread estando los 500 ya activos este tendra que espearse hasta que haya uno espacio libre 
        ExecutorService pool = Executors.newFixedThreadPool(500);
        //Crea un server en el puerto 4000
        try (MyServerSocket listener = new MyServerSocket(PORT)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }
    }

    public static class Handler implements Runnable {

        final int time = 50;
        private String lastMsg;
        public BufferedReader in = null;
        public PrintWriter out = null;
        private String name;
        private MySocket socket;

        public Handler(MySocket socket) {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(this.socket.MyGetInputStream()));
            this.out = new PrintWriter(socket.MyGetOutputStream(), true);
        }

        public void run() {

            while (true) {
                this.out.print("\nIntroduce your username:\n ");
                this.out.flush();
                try {
                    this.name = this.in.readLine();
                } catch (IOException e) {
                    System.out.println(e);
                }
                if (!users.containsKey(this.name)) {
                    users.put(this.name, this);
                    //envia un mensaje a todos los clientes conectados al server con el mensaje de que un nuevo cliente se ha conectado
                    for (Handler ms : Server.users.values()) {
                        ms.out.print("(" + this.name + ") joined the chat\n");
                        ms.out.flush();
                    }
                    //Se imprime por la pantalla del server el nuevo cliente que se acava de conectar
                    System.out.println("New user joined : (" + this.name + ")");
                    break;
                } else {
                    this.out.println("Name already exists");
                    this.out.flush();
                }
            }
            while (true) {
                try {
                    if (this.in.ready()) {
                        this.lastMsg = this.in.readLine();
                        System.out.println("received message: '" + this.lastMsg + "'");
                        //Si el mensaje escrito es logout  se desconecta el cliente borrando su entrada en el mapa
                        if (this.lastMsg.equals("logout") || this.lastMsg.equals("Logout")) {
                            users.remove(this.name);
                            //envia un mensaje a todos los clientes conectados al server con el mensaje de que un nuevo cliente se ha conectado
                            for (Handler ms : Server.users.values()) {
                                ms.out.print("(" + this.name + ") has left the chat\n");
                                ms.out.flush();
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                if (!"reseted".equals(this.lastMsg) || this.lastMsg == null) {
                    for (Handler ms : Server.users.values()) {
                        ms.out.print("(" + this.name + "): " + this.lastMsg + "\n");
                        ms.out.flush();
                    }
                }
                this.lastMsg = "reseted";
            }
            this.socket.close();
            try {
                this.in.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.out.close();

        }
    }
}
