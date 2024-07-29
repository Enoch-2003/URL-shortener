import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class URLShortener {
    private JFrame frame;
    private JTextField longUrlField;
    private JTextField shortUrlField;
    private JTextArea resultArea;
    private URLShortenerService service;

    public URLShortener() {
        service = new URLShortenerService();
        initialize();
    }

    // functions to shorten and expand url
    
    public void sUrl(){
        String longUrl = longUrlField.getText();
        String shortUrl = service.shortenURL(longUrl);
        resultArea.setText("Short URL: " + shortUrl);
    }

    public void lUrl(){
        String shortUrl = shortUrlField.getText();
        String longUrl = service.expandURL(shortUrl);
        resultArea.setText("Long URL: " + longUrl);
    }

    private void initialize() {
        frame = new JFrame("URL Shortener");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLayout(new GridBagLayout());

        // Create separate GridBagConstraints for the image and other components
        GridBagConstraints gbcImage = new GridBagConstraints();
        gbcImage.insets = new Insets(10, 10, 20, 10);
        gbcImage.fill = GridBagConstraints.NONE;
        gbcImage.gridx = 0;
        gbcImage.gridy = 0;
        gbcImage.gridwidth = 3;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Load and add the image
        try {
            ImageIcon logoIcon = new ImageIcon(ImageIO.read(new File("URL_icon.PNG")));
            JLabel logoLabel = new JLabel(logoIcon);
            frame.add(logoLabel, gbcImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel longUrlLabel = new JLabel("Enter Long URL:");
        longUrlLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 10, 10, 10);
        frame.add(longUrlLabel, gbc);

        //longUrlField attributes

        longUrlField = new JTextField("Paste long URL here...");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        frame.add(longUrlField, gbc);

        longUrlField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (longUrlField.getText().equals("Paste long URL here...")) {
                    longUrlField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (longUrlField.getText().isEmpty()) {
                    longUrlField.setText("Paste long URL here...");
                }
            }
        });

        longUrlField.addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == longUrlField){
                    sUrl();
                }
            } 
        });

        JButton shortenButton = new JButton("Shorten URL");
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        frame.add(shortenButton, gbc);

        shortenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sUrl();
            }
        });

        // shortUrlField attributes

        JLabel shortUrlLabel = new JLabel("Enter Short URL:");
        shortUrlLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(shortUrlLabel, gbc);

        shortUrlField = new JTextField("Paste short URL here...");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        frame.add(shortUrlField, gbc);

        shortUrlField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (shortUrlField.getText().equals("Paste short URL here...")) {
                    shortUrlField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (shortUrlField.getText().isEmpty()) {
                    shortUrlField.setText("Paste short URL here...");
                }
            }
        });

        shortUrlField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == shortUrlField){
                    lUrl();
                }
            }
        });

        JButton expandButton = new JButton("Expand URL");
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        frame.add(expandButton,gbc);

        expandButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lUrl();
            }
        });

        // Copy button setup
        JButton copyButton = new JButton("Copy URL");
        copyButton.setPreferredSize(new Dimension(100, 30)); // Set button size
        GridBagConstraints gbcCopyButton = new GridBagConstraints();
        gbcCopyButton.insets = new Insets(10, 10, 10, 10);
        gbcCopyButton.gridx = 2;
        gbcCopyButton.gridy = 5;
        gbcCopyButton.anchor = GridBagConstraints.NORTHEAST;
        gbcCopyButton.fill = GridBagConstraints.NONE;
        frame.add(copyButton, gbcCopyButton);

        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = resultArea.getText();
                if (text.startsWith("Short URL: ")) {
                    text = text.substring(11);  // Remove "Short URL: " prefix
                } else if (text.startsWith("Long URL: ")) {
                    text = text.substring(10);  // Remove "Long URL: " prefix
                }
                StringSelection stringSelection = new StringSelection(text);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        });

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        frame.add(new JScrollPane(resultArea), gbc);

        frame.setVisible(true);

        // Start the server
        URLShortenerServer.startServer(service);
    }

    public static void main(String[] args) {
        new URLShortener();
    }
}
