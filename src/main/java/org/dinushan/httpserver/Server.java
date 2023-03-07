package org.dinushan.httpserver;

import org.dinushan.httpserver.configuration.Configuration;
import org.dinushan.httpserver.configuration.ConfigurationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//User Interface Creation from the JFRAME class using the swing library and the action listener interface
//Start Button will start the server and the stop button will stop the server, and it will run on port 2728 and the server will be started on the localhost

public class Server extends JFrame {
    //Creating the variables for the UI
    public final int Width;
    private int port = 3000;
    private String WebRoot ="";
    public final int Height;
    Configuration configuration = null;

    //Creating the constructor for the UI
    public Server(int width, int height){


        setTitle("org.dinushan.httpserver.Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.Width=width;
        this.Height=height;

        //Configure the frame and add Start and Stop buttons
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial",Font.BOLD,20));
        startButton.setPreferredSize(new Dimension(100,100));

        JButton stopButton = new JButton("Stop");
        stopButton.setFont(new Font("Arial",Font.BOLD,20));
        stopButton.setPreferredSize(new Dimension(100,100));
        stopButton.setEnabled(false);
        JButton ShowHtDocs = new JButton("Explorer");
        ShowHtDocs.setFont(new Font("Arial",Font.BOLD,20));
        ShowHtDocs.setPreferredSize(new Dimension(100,100));
        ShowHtDocs.setEnabled(false);
        JButton ShowBrowser = new JButton("Admin");
        ShowBrowser.setFont(new Font("Arial",Font.BOLD,20));
        ShowBrowser.setPreferredSize(new Dimension(100,100));
        ShowBrowser.setEnabled(false);



        JLabel label = new JLabel("Simple org.dinushan.httpserver.Server running on port "+port);
        label.setFont(new Font("Serif", Font.PLAIN, 20));

//        final JPanel panel = new JPanel();
        Container contentPane = getContentPane();
        JLabel console = new JLabel("Output Console: ");
        JTextArea Output = new JTextArea();
        Output.setEditable(false);
        Output.setLineWrap(true);
        Output.setOpaque(false);
        Output.setBorder(BorderFactory.createEmptyBorder());
        console.setFont(new Font("Serif", Font.PLAIN, 20));
        console.setBounds(0,0,100,100);


        final GroupLayout layout = new GroupLayout(contentPane);
        contentPane.setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(label)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(startButton)
                                        .addComponent(stopButton)
                                        .addComponent(ShowHtDocs)
                                        .addComponent(ShowBrowser)
                                )
                                .addComponent(console)
                                .addComponent(Output)
                        )
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(label)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(startButton)
                                        .addComponent(stopButton)
                                        .addComponent(ShowHtDocs)
                                        .addComponent(ShowBrowser)
                                )
                                .addComponent(console)
                                .addComponent(Output)
                        )

        );

        setSize(Width,Height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        //Creating the action listener for the start button and call the startServer method when the button is clicked

        startButton.addActionListener(new ActionListener() {


            public void actionPerformed(ActionEvent event) {
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                ShowHtDocs.setEnabled(true);
                ShowBrowser.setEnabled(true);
                HttpServer.start();
                Output.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")) +" =>  org.dinushan.httpserver.Server Started\n");
                configuration = ConfigurationManager.getInstance().getConfiguration();
                port = configuration.getPort();
                WebRoot=configuration.getWebRoot();
                Output.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")) +" =>  org.dinushan.httpserver.Server Running on port "+port+"\n");

            }
        });
        //Open the file explorer in Htdoc when the button is clicked
        ShowHtDocs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    Desktop.getDesktop().open(new java.io.File(WebRoot));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        //Open the browser when the admin button is clicked
        ShowBrowser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    Desktop.getDesktop().browse(java.net.URI.create("http://localhost:"+port));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //Creating the action listener for the stop button and call the stopServer method when the button is clicked

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {

                try{
                    HttpServer.stop();
                    stopButton.setEnabled(false);
                    ShowHtDocs.setEnabled(false);
                    ShowBrowser.setEnabled(false);
                    startButton.setEnabled(true);
                    Output.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")) +" => org.dinushan.httpserver.Server Stopped\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        new Server(500,500);
    }

}
