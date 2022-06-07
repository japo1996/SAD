package xat;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;

public class Client extends JFrame {

    public static final int SERVER_PORT = 4000;
    public static final String SERVER_HOST = "localhost";
    public static String name, lastLine = "initial";
    public static MySocket socket = new MySocket(SERVER_HOST, SERVER_PORT);
    public static PrintWriter out = new PrintWriter(socket.MyGetOutputStream(), true);
    public JFrame window = this;
    public static JTextArea TextChatArea, TextUserArea;
    public static JLabel label;

    public Client() {

        Client.ReceiverThread reader = new ReceiverThread();
        Client.CloseAction updater = new CloseAction();
        GUIComponents();
        reader.execute();
        updater.execute();
    }

    class CloseAction extends SwingWorker<String, Object> {

        @Override
        protected String doInBackground() throws Exception {
            window.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    out.print("CloseConnection\n");
                    out.flush();
                    socket.close();
                    System.out.println("Closing connection");
                    System.exit(0);
                }
            });
            return null;
        }
    }

    class ReceiverThread extends SwingWorker<String, Object> {

        @Override
        protected String doInBackground() throws Exception {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.MyGetInputStream()));
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(name);
                System.out.println(lastLine);
                System.out.println(message);
                if (message.contains(lastLine) && message.contains("has joined")) {
                    name = lastLine;
                    TextChatArea.append(message + "\n");
                } else if (message.contains("updateUser")) {
                    String[] text;
                    text = message.split("-");
                    TextUserArea.setText("");
                    for (int j = 1; j < text.length; j++) {
                        TextUserArea.append("-" + text[j] + "\n");
                    }
                } else {
                    TextChatArea.append(message + "\n");
                }
            }
            return message;
        }
    }

    private void GUIComponents() {
        ButtonSend = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TextUserArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        TextChatArea = new javax.swing.JTextArea();
        TitleLabelChat = new javax.swing.JLabel();
        TitleLabelUser = new javax.swing.JLabel();
        TextBar = new javax.swing.JTextField();

        TextUserArea.setColumns(20);
        TextUserArea.setRows(5);
        jScrollPane1.setViewportView(TextUserArea);

        TextChatArea.setColumns(20);
        TextChatArea.setRows(5);
        jScrollPane2.setViewportView(TextChatArea);

        TitleLabelChat.setForeground(new java.awt.Color(51, 0, 255));
        TitleLabelChat.setText("Chat:");

        TitleLabelUser.setForeground(new java.awt.Color(51, 0, 255));
        TitleLabelUser.setText("Users in Chat");

        ButtonSend.setText("Send");
        ButtonSend.addActionListener((java.awt.event.ActionEvent evt) -> {
            sendButtonActionPerfomed(evt, out);
        });

        jScrollPane2.setViewportView(TextChatArea);
        //Codigo generado por GUI_xat version grafica para nuestro chat ubicada en GUI package
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(TextBar)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ButtonSend))
                                        .addComponent(TitleLabelChat, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addComponent(TitleLabelUser)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(TitleLabelChat)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ButtonSend)
                                        .addComponent(TextBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(56, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(TitleLabelUser)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        pack();
        setLocationRelativeTo(null);
    }

    private void sendButtonActionPerfomed(java.awt.event.ActionEvent evt, PrintWriter out) {
        String TxText = TextBar.getText();
        if (TextBar.getText().isEmpty()) {
            TextChatArea.append("Introduce something on TextBox" + "\n");
        } else {
            lastLine = TxText;
            TextBar.setText("");
            out.print(TxText + "\n");
            out.flush();
            if (name != null) {
                TextChatArea.append("**" + name + ": " + TxText + "\n");
            } else {
                TextChatArea.append(TxText + "\n");
            }
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
    }
    private javax.swing.JButton ButtonSend;
    private javax.swing.JLabel TitleLabelChat;
    private javax.swing.JLabel TitleLabelUser;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField TextBar;
}
