/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainProgram;

import JavaMail.JavaMailReceiving;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.WebDriver_Registration;

/**
 *
 * @author Daniyar
 */
public class MainProgram {

    public static Date dataStart;
    public static boolean firstMessage = true;
    public static boolean messageExist = false;
    public static File file = new File("error.log");
    public static PrintStream errorLog;

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        MainFrame mainFrame = new MainFrame();
        errorLog = new PrintStream(file);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace(errorLog);
            Logger.getLogger(MainProgram.class.getName()).log(Level.SEVERE, null, ex);
        }
        SwingUtilities.updateComponentTreeUI(mainFrame);
        mainFrame.setVisible(true);
        mainFrame.setSize(445, 415);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

    }

    public static void registrationMain(String courseNumber) throws InterruptedException, IOException {
        System.out.println("Web driver starts...");
        String passwordString = String.copyValueOf(MainFrameProgram.pennPassField.getPassword());
        int semesterNum = Integer.parseInt(MainFrameProgram.semesterComboBox.getSelectedItem().toString());
        WebDriver_Registration.courseRegistration(MainFrameProgram.pennIDTextField.getText(), passwordString, courseNumber, semesterNum, MainFrameProgram.dropCheckBox.isSelected());
        Scanner sc = new Scanner(System.in);
        int i = sc.nextInt();
        
    }

    public static boolean testPennStateAccount(boolean pennStateCorrect) throws InterruptedException {
        String passwordString = String.copyValueOf(MainFrameProgram.pennPassField.getPassword());
        pennStateCorrect = WebDriver_Registration.testConnection(MainFrameProgram.pennIDTextField.getText(), passwordString);
        return pennStateCorrect;
    }
}

class MainFrame extends JFrame {

    public static LogOutput log = new LogOutput();

    public MainFrame() throws InterruptedException {
        super("Registration automator");
        setLayout(new FlowLayout());
        MainFrameProgram MainFrame = new MainFrameProgram();
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("power-icon.png")));
        MainFrame.setPreferredSize(new Dimension(420, 215));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width - this.getSize().width - 1250, dim.height / 5 - this.getSize().height);
        add(MainFrame);
        logFrameInitializer();
    }

    private void logFrameInitializer() {
        log.setPreferredSize(new Dimension(440, 160));
        log.setBackground(Color.white);
        add(log);
    }
}

class MainFrameProgram extends JPanel {

    private JLabel welcomeMessage;
    private JLabel welcomeMessage2;
    private JLabel pennIDLabel;
    protected static JTextField pennIDTextField;
    private JLabel pennPassLabel;
    protected static JPasswordField pennPassField;
    private JLabel emailLabel;
    private JTextField emailTextField;
    private JLabel emailDomainLabel;
    private JLabel emailPassLabel;
    private JPasswordField emailPassField;
    private JLabel courseLabel;
    protected static JTextField courseNumberTextField;
    private JLabel semesterLabel;
    protected static JComboBox semesterComboBox;
    private final String[] semestersString = {"1", "2", "3", "4"};
    private JLabel dropLabel;
    protected static JCheckBox dropCheckBox;
    private JButton button;
    private JLabel stopWatchLabel;
    private JLabel credentials;
    private Timer timer;
    private Timer checkMailTimer;
    private StopWatch stopWatch;
    private String[] stopWatchSplitting;
    private boolean checkMail;
    private boolean testConnection;

    public MainFrameProgram() throws InterruptedException {
        initComponents();
    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    private void initComponents() throws InterruptedException {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        welcomeMessage = new JLabel();
        welcomeMessage2 = new JLabel();
        pennIDLabel = new JLabel();
        pennIDTextField = new JTextField();
        pennPassLabel = new JLabel();
        pennPassField = new JPasswordField();
        emailLabel = new JLabel();
        emailTextField = new JTextField();
        emailDomainLabel = new JLabel();
        emailPassLabel = new JLabel();
        emailPassField = new JPasswordField();
        semesterLabel = new JLabel();
        semesterComboBox = new JComboBox(semestersString);
        credentials = new JLabel();
        dropLabel = new JLabel();
        dropCheckBox = new JCheckBox();
        button = new JButton();
        stopWatchLabel = new JLabel();
        checkMail = false;
        testConnection = false;

        mailCheckTimeInitializer();
        StopWatchInitializer();
        try {
            setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.ROMAN_BASELINE, 12));
        } catch (Exception e) {
            e.printStackTrace(MainProgram.errorLog);
        }

        String lcOSName = System.getProperty("os.name").toLowerCase();
        boolean IS_MAC = lcOSName.startsWith("mac os x");
        //======== this ========
        setLayout(new GridBagLayout());
        ((GridBagLayout) getLayout()).columnWidths = new int[]{81, 5, 119, 5, 0, 0};
        if (!IS_MAC) {
            ((GridBagLayout) getLayout()).rowHeights = new int[]{17, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 0, 0, 0};
        }
        ((GridBagLayout) getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        //---- welcomeMessage ----
        welcomeMessage.setText("Course Registration");
        welcomeMessage.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeMessage, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        //---- welcomeMessage2 ----
        welcomeMessage2.setText("for Penn State (BETA)");
        welcomeMessage2.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeMessage2, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        //---- pennIDLabel ----
        pennIDLabel.setText("Penn State ID: ");
        pennIDLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(pennIDLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        //---- pennIDTextField ----
        pennIDTextField.setText("xxx123");
        add(pennIDTextField, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        //---- pennPassLabel ----
        pennPassLabel.setText("Penn State Password: ");
        pennPassLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(pennPassLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        add(pennPassField, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        //---- emailLabel ----
        emailLabel.setText("Email: ");
        emailLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(emailLabel, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        //---- emailTextField ----
        emailTextField.setText("myEmail");
        add(emailTextField, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        //---- emailDomainLabel ----
        emailDomainLabel.setText("@mail.com");
        add(emailDomainLabel, new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        //---- emailPassLabel ----
        emailPassLabel.setText("Email Password: ");
        emailPassLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(emailPassLabel, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        add(emailPassField, new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        //---- Semester label ----
        semesterLabel.setText("Semester/Drop Option: ");
        semesterLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(semesterLabel, new GridBagConstraints(0, 13, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        //---- Semester ComboBox ----
        add(semesterComboBox, new GridBagConstraints(1, 13, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        //---- Drop CheckBox ----
        add(dropCheckBox, new GridBagConstraints(2, 13, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        //---- button ----
        button.setText("Start");
        add(button, new GridBagConstraints(1, 16, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        //---- stopWatchLabel ----
        stopWatchLabel.setText("  " + stopWatchSplitting[0]);
        stopWatchLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(stopWatchLabel, new GridBagConstraints(2, 16, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        //---- label11 ----
        credentials.setText(" Created by Daniyar Yeralin");
        credentials.setForeground(Color.gray);
        credentials.setHorizontalAlignment(SwingConstants.RIGHT);
        credentials.setFont(new Font("Cambria", Font.ITALIC, 12));
        add(credentials, new GridBagConstraints(2, 17, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        /*button.registerKeyboardAction(button.getActionForKeyStroke(
         KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false)),
         KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
         JComponent.WHEN_FOCUSED);

         button.registerKeyboardAction(button.getActionForKeyStroke(
         KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true)),
         KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
         JComponent.WHEN_FOCUSED);*/
        StartButtonHandler buttonListener = new StartButtonHandler();
        button.addActionListener(buttonListener);

    }

    protected class StartButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            if (button.getText().equals("Start")) {
                File file = new File("Last Message.txt");
                file.delete();
                stopWatch.start();
                MainProgram.dataStart = new Date();
                checkMailTimer.start();
                checkMailTimer.setRepeats(true);
                button.setText("Stop");
            } else {
                checkMail = false;
                button.setText("Start");
                stopWatch.reset();
                checkMailTimer.stop();
                try {
                    mailCheckTimeInitializer();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainFrameProgram.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace(MainProgram.errorLog);
                }
            }

        }
    }

    ActionListener updateLabelListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt) {
            String[] stopWatchSplitting = stopWatch.toString().split("\\.");
            stopWatchLabel.setText("  " + stopWatchSplitting[0]);
        }
    };

    public void programTerminator() {
        checkMail = false;
        button.setText("Start");
        stopWatch.reset();
        checkMailTimer.stop();
        try {
            mailCheckTimeInitializer();
        } catch (InterruptedException ex) {
            Logger.getLogger(MainFrameProgram.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace(MainProgram.errorLog);
        }
    }

    ActionListener mailCheckListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (testConnection) {
                if (!checkMail) {
                    checkMailTimer.stop();
                    checkMailTimer = new Timer(5000, mailCheckListener);
                    checkMail = true;
                    checkMailTimer.start();
                }
                mailHandler receiveMail = new mailHandler();
                try {
                    receiveMail.mailHandler(emailTextField, emailPassField);
                } catch (IOException | InterruptedException ex) {
                    Logger.getLogger(MainFrameProgram.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace(MainProgram.errorLog);
                } catch (MessagingException ex) {
                    Logger.getLogger(MainFrameProgram.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {

                boolean mailCorrect = false;
                boolean pennStateCorrect = false;
                System.out.println("Checking the correctness of the provided information");
                System.out.println("----------------------------------------------");

                mailHandler testMailConnection = new mailHandler();
                try {
                    mailCorrect = testMailConnection.mailTest(emailTextField, emailPassField, mailCorrect);
                } catch (IOException | InterruptedException ex) {
                    Logger.getLogger(MainFrameProgram.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace(MainProgram.errorLog);
                }
                if (!mailCorrect) {
                    System.out.println("There is something wrong with your email info");
                    System.out.println("Email address or email password is incorrect");
                    System.out.println("If it is not an issue, then something wrong with Mail.com server");
                    System.out.println("----------------------------------------------");
                }

                try {
                    pennStateCorrect = MainProgram.testPennStateAccount(pennStateCorrect);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainFrameProgram.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace(MainProgram.errorLog);
                }
                if (!pennStateCorrect) {
                    System.out.println("There is something wrong with provided Penn State information");
                    System.out.println("Login or password is incorrect");
                    System.out.println("If it is not an issue, then something wrong with Penn State web-site");

                }

                if (mailCorrect && pennStateCorrect) {
                    System.out.println("Test ended successfuly!");
                    System.out.println("Everything is just fine");
                    System.out.println("----------------------------------------------");
                    testConnection = true;
                } else {
                    programTerminator();
                }

            }
        }
    };

    public void StopWatchInitializer() throws InterruptedException {
        stopWatch = new StopWatch();
        stopWatchSplitting = stopWatch.toString().split("\\.");
        timer = new Timer(1000, updateLabelListener);
        timer.start();
        timer.setRepeats(true);
    }

    public void mailCheckTimeInitializer() throws InterruptedException {
        checkMailTimer = new Timer(1000, mailCheckListener);
    }

}

class LogOutput extends JPanel {

    private final JTextArea textArea = new JTextArea(15, 30);
    private final TextAreaOutputStream taOutputStream = new TextAreaOutputStream(
            textArea, "System");

    public LogOutput() {
        setLayout(new BorderLayout());
        add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        System.setOut(new PrintStream(taOutputStream));
    }
}

class NumericTextField extends JTextField {

    @Override
    protected Document createDefaultModel() {
        return new NumericDocument();
    }

    private class NumericDocument extends PlainDocument {

        // The regular expression to match input against (zero or more digits)
        private final Pattern DIGITS = Pattern.compile("\\d*");

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            // Only insert the text if it matches the regular expression
            if (str != null && DIGITS.matcher(str).matches()) {
                super.insertString(offs, str, a);
            }
        }
    }
}

class JTextFieldLimit extends PlainDocument {

    private final int limit;

    JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}

class TextAreaOutputStream extends OutputStream {

    private final JTextArea textArea;
    private final StringBuilder sb = new StringBuilder();
    private final String title;

    public TextAreaOutputStream(final JTextArea textArea, String title) {
        this.textArea = textArea;
        this.title = title;
        sb.append(title).append("> ");
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }

    @Override
    public void write(int b) throws IOException {

        if (b == '\r') {
            return;
        }

        if (b == '\n') {
            final String text = sb.toString() + "\n";
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    textArea.append(text);
                }
            });
            sb.setLength(0);
            sb.append("Program" + ": ");
            return;
        }

        sb.append((char) b);
    }
}

class mailHandler {

    public void mailHandler(JTextField emailTextField, JPasswordField emailPasswordField) throws IOException, InterruptedException, MessagingException {
        String passwordString = String.copyValueOf(emailPasswordField.getPassword());
        JavaMailReceiving.receiveEmail("pop.mail.com", "pop3", emailTextField.getText() + "@mail.com", passwordString);
    }

    public boolean mailTest(JTextField emailTextField, JPasswordField emailPasswordField, boolean mailCorrect) throws IOException, InterruptedException {
        String passwordString = String.copyValueOf(emailPasswordField.getPassword());
        mailCorrect = JavaMailReceiving.testMail("pop.mail.com", "pop3", emailTextField.getText() + "@mail.com", passwordString);
        return mailCorrect;
    }
}
