package webbserver_connections;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

public class WebbViewer extends JDialog {
    private JPanel contentPane;
    private JButton searchButton;
    private JTextField searchField;
    private JTextArea textArea;
    private JPanel searchPanel;
    private JScrollPane scrollPane;

    public WebbViewer() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        searchButton.addActionListener(e -> {
            try {
                textArea.setText(WebbConnection.getText(searchField.getText()));
            } catch (URISyntaxException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        setContentPane(contentPane);
        setModal(true);
    }

    public WebbViewer(String initText) {
        this();
        textArea.setText(initText);
    }
}
