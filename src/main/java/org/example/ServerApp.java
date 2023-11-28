package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    private JTextField addressField;
    private JTextField portField;


    public ServerApp() {
        JFrame frame = new JFrame("TCP Server Setup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel addressLabel = new JLabel("Server Address:");
        addressField = new JTextField();
        JLabel portLabel = new JLabel("Server Port:");
        portField = new JTextField();

        JButton startButton = new JButton("Start Server");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });

        panel.add(addressLabel);
        panel.add(addressField);
        panel.add(portLabel);
        panel.add(portField);
        panel.add(startButton);

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setVisible(true);
    }


        private void startServer() {
            try {
                ServerSocket serverSocket = new ServerSocket(9876);
                System.out.println("Server is running and waiting for connections...");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
