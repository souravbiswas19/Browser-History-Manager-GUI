
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Page {

    String url;
    Page prev;
    Page next;

    public Page(String url) {
        this.url = url;
        this.prev = null;
        this.next = null;
    }
}

class History {

    private Page currentPage;

    public void visitPage(String url) {
        Page newPage = new Page(url);

        // If there are forward pages, clear them before visiting a new page
        if (currentPage != null) {
            currentPage.next = null; // Clear forward links
            newPage.prev = currentPage;
            currentPage.next = newPage;
        }

        // Set the new page as the current page
        currentPage = newPage;
    }

    public String goBack() {
        if (currentPage == null || currentPage.prev == null) {
            return "No previous page to go back to.";
        } else {
            currentPage = currentPage.prev;
            return "Went back to: " + currentPage.url;
        }
    }

    public String goForward() {
        if (currentPage == null || currentPage.next == null) {
            return "No forward page to go to.";
        } else {
            currentPage = currentPage.next;
            return "Went forward to: " + currentPage.url;
        }
    }

    public String viewHistory() {
        if (currentPage == null) {
            return "No browsing history available.";
        }

        StringBuilder history = new StringBuilder("Browsing History:\n");
        Page firstPage = currentPage;

        while (firstPage.prev != null) {
            firstPage = firstPage.prev;
        }

        while (firstPage != null) {
            if (firstPage == currentPage) {
                history.append("-> ").append(firstPage.url).append(" (current)\n");
            } else {
                history.append("   ").append(firstPage.url).append("\n");
            }
            firstPage = firstPage.next;
        }
        return history.toString();
    }

    public void clearHistory() {
        currentPage = null;
    }
}

public class BrowserHistoryManagerGUI extends JFrame {

    private final History browserHistory;
    private final JTextArea historyTextArea;
    private final JTextField urlField;

    public BrowserHistoryManagerGUI() {
        browserHistory = new History();
        setTitle("Browser History Manager");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        urlField = new JTextField(20);
        JButton visitButton = new JButton("Visit Page");
        JButton backButton = new JButton("Go Back");
        JButton forwardButton = new JButton("Go Forward");
        JButton viewButton = new JButton("View History");
        JButton clearButton = new JButton("Clear History");

        historyTextArea = new JTextArea();
        historyTextArea.setEditable(false);
        historyTextArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(historyTextArea);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("URL:"));
        inputPanel.add(urlField);
        inputPanel.add(visitButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        buttonPanel.add(forwardButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(clearButton);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners for Buttons
        visitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText().trim();
                if (!url.isEmpty()) {
                    browserHistory.visitPage(url);
                    urlField.setText("");
                    updateHistoryDisplay();
                    showMessage("Visited: " + url);
                } else {
                    showMessage("Please enter a valid URL.");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = browserHistory.goBack();
                updateHistoryDisplay();
                showMessage(message);
            }
        });

        forwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = browserHistory.goForward();
                updateHistoryDisplay();
                showMessage(message);
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateHistoryDisplay();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browserHistory.clearHistory();
                updateHistoryDisplay();
                showMessage("Browsing history cleared.");
            }
        });
    }

    private void updateHistoryDisplay() {
        historyTextArea.setText(browserHistory.viewHistory());
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BrowserHistoryManagerGUI().setVisible(true);
            }
        });
    }
}
