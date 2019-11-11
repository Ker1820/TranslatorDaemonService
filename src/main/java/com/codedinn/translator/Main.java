package com.codedinn.translator;

import com.codedinn.translator.Data.TranslatorBody;
import com.codedinn.translator.Data.TranslatorResponse;
import com.google.gson.Gson;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Map;

public class Main {

    private static final String HOST = "https://gateway-lon.watsonplatform.net/language-translator/api";
    private static final String SUBSCRIPTION_KEY = "kNu28Au0LPwEo_KmOPMYoaes2rwyPKK00FUf7h4aPJIn";
    private static final Gson gson = new Gson();
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("IN MAIN");


//        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(false);
//        keyboardHook.addKeyListener(new GlobalKeyAdapter() {
//            @Override
//            public void keyPressed(GlobalKeyEvent event) {
//                logger.info("IN GLOBAL LISTENER " + event.getKeyChar());
//                if (event.isControlPressed() && event.isShiftPressed() && event.getVirtualKeyCode() == GlobalKeyEvent.VK_F7) {
//                    logger.info("IN LISTENER");
//                    translate();
//                }
//            }
//        });



//        try {
//            boolean l = false;
//            while (l) {
//                Thread.sleep(128);
//            }
//        } catch (InterruptedException e) {
//            logger.error(e.getMessage(), e);
//        } finally {
//            keyboardHook.shutdownHook();
//        }


    }

    public static void translate() {
        logger.info("IN TRANSLATE");
        HttpClient httpClient = createHttpClient();

        HttpPost translate = new HttpPost(HOST + "/v3/translate?version=2018-05-01");
        translate.addHeader("Content-Type", "application/json");

        String json = "";
        try {
            TranslatorBody body = new TranslatorBody(getBuffer(), "en-ru");
            translate.setEntity(new StringEntity(gson.toJson(body), "UTF-8"));

            logger.info("BEFORE EXECUTING REQUEST");
            HttpResponse execute = httpClient.execute(translate);
            json = EntityUtils.toString(execute.getEntity(), "UTF-8");

            TranslatorResponse response = gson.fromJson(json, TranslatorResponse.class);
            logger.info("RESPONSE IS MAPPED");

//            response.getTranslations().forEach(translation -> System.out.println(translation.getTranslation()));
            displayTray(response.getTranslations().get(0).getTranslation());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void displayTray(String message) throws AWTException {
        logger.info("IN DISPLAYTRAY");
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
//        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):

        Image image = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("Translator-icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "Translator");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("Translator");
        tray.add(trayIcon);
        String finalMessage = message.substring(0, 1).toUpperCase() + message.substring(1);
        trayIcon.displayMessage("Translator", finalMessage, MessageType.INFO);
        tray.remove(trayIcon);
        logger.info("END OF DISPLAYTRAY");
    }

    private static String getBuffer() throws IOException, UnsupportedFlavorException {
        logger.info("IN GETBUFFER");
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Clipboard systemClipboard = defaultToolkit.getSystemClipboard();
        logger.info("STRING FLAVOR");
        DataFlavor dataFlavor = DataFlavor.stringFlavor;
        String text = "";
        if (systemClipboard.isDataFlavorAvailable(dataFlavor)) {
            text = (String) systemClipboard.getData(dataFlavor);
        }
        logger.info("ENT OF GETBUFFER");
        return text;
    }

    private static HttpClient createHttpClient() {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials("apikey", SUBSCRIPTION_KEY);
        provider.setCredentials(AuthScope.ANY, credentials);
        return HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
    }
}
