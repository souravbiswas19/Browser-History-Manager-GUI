
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Class representing a single page in the browsing history
class Page {

    String url; // URL of the page
    Page prev;  // Link to the previous page in history
    Page next;  // Link to the next page in history

    // Constructor to initialize the Page with a URL
    public Page(String url) {
        this.url = url;
        this.prev = null;
        this.next = null;
    }
}

// Class that manages the browsing history functionality
class History {

    private Page currentPage; // Tracks the current page in the history

    // Method to visit a new page and add it to the browsing history
    public void visitPage(String url) {
        Page newPage = new Page(url);

        // Clear forward pages if any exist
        if (currentPage != null) {
            currentPage.next = null; // Disconnect forward links
            newPage.prev = currentPage; // Link new page back to current page
            currentPage.next = newPage; // Link current page forward to new page
        }

        // Update the current page to the newly visited page
        currentPage = newPage;
    }

    // Method to go back to the previous page
    public String goBack() {
        if (currentPage == null || currentPage.prev == null) {
            return "No previous page to go back to.";
        } else {
            currentPage = currentPage.prev; // Move current page pointer back
            return "Went back to: " + currentPage.url;
        }
    }

    // Method to go forward to the next page
    public String goForward() {
        if (currentPage == null || currentPage.next == null) {
            return "No forward page to go to.";
        } else {
            currentPage = currentPage.next; // Move current page pointer forward
            return "Went forward to: " + currentPage.url;
        }
    }

    // Method to view the complete browsing history
    public String viewHistory() {
        if (currentPage == null) {
            return "No browsing history available.";
        }

        StringBuilder history = new StringBuilder("Browsing History:\n");
        Page firstPage = currentPage;

        // Navigate to the first page in the history
        while (firstPage.prev != null) {
            firstPage = firstPage.prev;
        }

        // Build history list starting from the first page
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

    // Method to clear all browsing history
    public void clearHistory() {
        currentPage = null; // Set current page to null to clear history
    }
}

// GUI class for managing browser history with a Swing interface
public class BrowserHistoryManagerGUI extends JFrame {

    private final History browserHistory; // Instance of History to manage browsing
    private final JTextArea historyTextArea; // Display area for history
    private final JTextField urlField; // Input field for new URLs

    // Constructor to set up the GUI layout and components
    public BrowserHistoryManagerGUI() {
        browserHistory = new History();
        setTitle("Browser History Manager");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen

        urlField = new JTextField(20); // Field to enter URL
        JButton visitButton = new JButton("Visit Page");
        JButton backButton = new JButton("Go Back");
        JButton forwardButton = new JButton("Go Forward");
        JButton viewButton = new JButton("View History");
        JButton clearButton = new JButton("Clear History");

        historyTextArea = new JTextArea(); // Area to display browsing history
        historyTextArea.setEditable(false);
        historyTextArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(historyTextArea);

        // Panel for URL input and visit button
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("URL:"));
        inputPanel.add(urlField);
        inputPanel.add(visitButton);

        // Panel for navigation buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        buttonPanel.add(forwardButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(clearButton);

        // Add panels to main frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners for Button Actions
        visitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText().trim();
                if (!url.isEmpty()) {
                    browserHistory.visitPage(url); // Add page to history
                    urlField.setText(""); // Clear URL input
                    updateHistoryDisplay(); // Refresh history display
                    showMessage("Visited: " + url);
                } else {
                    showMessage("Please enter a valid URL.");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = browserHistory.goBack(); // Go back in history
                updateHistoryDisplay(); // Refresh history display
                showMessage(message);
            }
        });

        forwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = browserHistory.goForward(); // Go forward in history
                updateHistoryDisplay(); // Refresh history display
                showMessage(message);
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateHistoryDisplay(); // Display the full history
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browserHistory.clearHistory(); // Clear the entire history
                updateHistoryDisplay(); // Refresh history display
                showMessage("Browsing history cleared.");
            }
        });
    }

    // Method to update the text area with the current browsing history
    private void updateHistoryDisplay() {
        historyTextArea.setText(browserHistory.viewHistory());
    }

    // Method to display message dialogs for user feedback
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    // Main method to launch the GUI application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BrowserHistoryManagerGUI().setVisible(true);
            }
        });
    }
}
