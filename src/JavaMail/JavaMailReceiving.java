/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaMail;

import MainProgram.MainProgram;
import MainProgram.StringUtils;
import com.sun.mail.pop3.POP3Store;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

/**
 *
 * @author Daniyar
 */
public class JavaMailReceiving {

    private static final String fileName = "Last Message.txt";
    static boolean messageExist = MainProgram.messageExist;

    public static void receiveEmail(String pop3Host, String storeType,
            String user, String password) throws MessagingException, IOException, InterruptedException {
            //1) get the session object  
            Properties properties = new Properties();
            properties.put("mail.pop3.host", pop3Host);
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.socketFactory", 995);
            properties.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.starttls.enable", "true");

            //Session emailSession = Session.getDefaultInstance(properties);
            Session emailSession = Session.getInstance(properties, new GMailAuthenticator(user, password));

            //2) create the POP3 store object and connect with the pop server  
            POP3Store emailStore = (POP3Store) emailSession.getStore(storeType);
            emailStore.connect(user, password);

            //3) create the folder object and open it  
            Folder emailFolder = emailStore.getFolder("Inbox");
            emailFolder.open(Folder.READ_ONLY);

            //4) retrieve the messages from the folder in an array and print it  
            Message[] messages = emailFolder.getMessages();

            messageParsing(messages);

            //5) close the store and folder objects  
            emailFolder.close(false);
            emailStore.close();

    }

    public static void messageParsing(Message[] messages) throws MessagingException, IOException, InterruptedException {

        String text = null;
        BodyPart mBody = null;
        boolean firstMessage = MainProgram.firstMessage;
        boolean tempBool = false;
        boolean messageFound = false;
        boolean messageGotten = false;
        Long lastMessageTime = readFile("Last Message.txt");
        try {
            if (messages.length != 0) {
                for (int i = messages.length - 1; i >= 0; i--) {
                    Message message = messages[i];
                    if (messageExist) {
                        if (message.getSentDate().getTime() > lastMessageTime) {
                            if (message.getSubject().equals("Watch List Notice")) {
                                System.out.println("Watch List notice is found");
                                try {
                                    try (BufferedWriter out = new BufferedWriter(new FileWriter("Last Message.txt"))) {
                                        String writeDate = String.valueOf(message.getSentDate().getTime());
                                        out.write(writeDate);
                                        tempBool = true;
                                    }
                                } catch (IOException e) {
                                    messageExist = false;
                                    e.printStackTrace(MainProgram.errorLog);
                                }
                                GetBodyMessage bodyMessageGetter = new GetBodyMessage();
                                if (message.getContent() instanceof String) {
                                    text = (String) message.getContent();
                                } else if (message.getContent() instanceof Multipart) {
                                    Multipart mText = (Multipart) message.getContent();
                                    mBody = mText.getBodyPart(0);
                                    text = mBody.getContent().toString();
                                }
                                String[] tokenedString = text.split("\\s");

                                for (int x = 0; x < tokenedString.length; x++) {
                                    String token = tokenedString[x];

                                    if (token.length() == 8 && token.charAt(7) == ')') {
                                        StringBuilder sb = new StringBuilder(token);
                                        sb.deleteCharAt(0);
                                        sb.deleteCharAt(sb.length() - 1);
                                        token = sb.toString();
                                        messageGotten = StringUtils.isAllNumber(token);
                                        if (messageGotten) {
                                            System.out.println("Opened seat for " + token + " found");
                                            MainProgram.registrationMain(token);
                                        }
                                        break;
                                    }

                                    if (messageGotten) {
                                        break;
                                    }
                                }

                                messageFound = true;
                            }
                        } else {
                            tempBool = true;
                            break;
                        }
                        tempBool = true;
                    } else if (message.getSentDate().getTime() >= MainProgram.dataStart.getTime()) {

                        if (firstMessage) {
                            try {
                                try (BufferedWriter out = new BufferedWriter(new FileWriter("Last Message.txt"))) {
                                    String writeDate = String.valueOf(message.getSentDate().getTime());
                                    out.write(writeDate);
                                    tempBool = true;
                                }
                            } catch (IOException e) {
                                messageExist = false;
                                e.printStackTrace(MainProgram.errorLog);
                            }
                            firstMessage = false;
                        }

                        if (message.getSubject().equals("Watch List Notice")) {
                            System.out.println("Watch List notice is found");
                            GetBodyMessage bodyMessageGetter = new GetBodyMessage();
                            if (message.getContent() instanceof String) {
                                text = (String) message.getContent();
                            } else if (message.getContent() instanceof Multipart) {
                                Multipart mText = (Multipart) message.getContent();
                                mBody = mText.getBodyPart(0);
                                text = mBody.getContent().toString();
                            }

                            String[] tokenedString = text.split("\\s");

                            for (int x = 0; x < tokenedString.length; x++) {
                                String token = tokenedString[x];

                                if (token.length() == 8 && token.charAt(7) == ')') {
                                    StringBuilder sb = new StringBuilder(token);
                                    sb.deleteCharAt(0);
                                    sb.deleteCharAt(sb.length() - 1);
                                    token = sb.toString();
                                    messageGotten = StringUtils.isAllNumber(token);
                                    if (messageGotten) {
                                        System.out.println("Opened seat for " + token + " found");
                                        messageExist = true;
                                        MainProgram.registrationMain(token);
                                    }
                                    break;
                                }

                                if (messageGotten) {
                                    break;
                                }
                            }

                            messageFound = true;

                        }
                    } else {
                        break;
                    }
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace(MainProgram.errorLog);
        }
    }

    private static Long readFile(String fileName) {
        String text = "";
        try {
            File file = new File(fileName);
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    text += scanner.nextLine();
                }
            }
        } catch (FileNotFoundException e) {
            messageExist = false;
            return null;
        }
        Long lastMessageTime = new Long(text);
        return lastMessageTime;
    }

    public static boolean testMail(String pop3Host, String storeType,
            String user, String password) throws IOException, InterruptedException {

        try {
            //1) get the session object  
            Properties properties = new Properties();
            properties.put("mail.pop3.host", pop3Host);
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.socketFactory", 995);
            properties.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.starttls.enable", "true");

            Session emailSession = Session.getDefaultInstance(properties);

            //2) create the POP3 store object and connect with the pop server  
            POP3Store emailStore = (POP3Store) emailSession.getStore(storeType);
            emailStore.connect(user, password);

        } catch (NoSuchProviderException e) {
            e.printStackTrace(MainProgram.errorLog);
        } catch (MessagingException e) {
            e.printStackTrace(MainProgram.errorLog);
            System.out.println("Wrong Username or Password");
            return false;
        }
        return true;
    }

}

class GMailAuthenticator extends Authenticator {

    String user;
    String pw;

    public GMailAuthenticator(String username, String password) {
        super();
        this.user = username;
        this.pw = password;
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, pw);
    }
}

class GetBodyMessage {

    private boolean textIsHtml = false;

    /**
     * Return the primary text content of the message.
     */
    public String getText(Part p) throws
            MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String) p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null) {
                        text = getText(bp);
                    }
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null) {
                        return s;
                    }
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null) {
                    return s;
                }
            }
        }

        return null;
    }

}
