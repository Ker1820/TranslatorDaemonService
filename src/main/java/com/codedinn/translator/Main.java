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

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Map;

public class Main {

    private static String HOST = "https://gateway-lon.watsonplatform.net/language-translator/api";
    private static String SUBSCRIPTION_KEY = "kNu28Au0LPwEo_KmOPMYoaes2rwyPKK00FUf7h4aPJIn";
    private static Gson gson = new Gson();

    private static boolean run;

    private static void changeBool(){
        run = false;
    }

    public static void main(String[] args) throws IOException, UnsupportedFlavorException {
        run = true;
        // Might throw a UnsatisfiedLinkError if the native library fails to load or a RuntimeException if hooking fails
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(false); // Use false here to switch to hook instead of raw input

        System.out.println("Global keyboard hook successfully started, press [escape] key to shutdown. Connected keyboards:");

        for (Map.Entry<Long, String> keyboard : GlobalKeyboardHook.listKeyboards().entrySet()) {
            System.out.format("%d: %s\n", keyboard.getKey(), keyboard.getValue());
        }

        keyboardHook.addKeyListener(new GlobalKeyAdapter() {

            @Override
            public void keyPressed(GlobalKeyEvent event) {
                if (event.isControlPressed() && event.isShiftPressed() && event.getVirtualKeyCode() == GlobalKeyEvent.VK_F7) {
                    translate();
                }
            }
        });

        try {
            while(true) {
                Thread.sleep(128);
            }
        } catch(InterruptedException e) {
            //Do nothing
        } finally {
            keyboardHook.shutdownHook();
        }


    }

    private static void translate(){
        HttpClient httpClient = createHttpClient();

        HttpPost translate = new HttpPost(HOST + "/v3/translate?version=2018-05-01");

        translate.addHeader("Content-Type", "application/json");
        String json = "";
        try {
            TranslatorBody body = new TranslatorBody(getBuffer(), "en-ru");
            translate.setEntity(new StringEntity(gson.toJson(body), "UTF-8"));

            HttpResponse execute = httpClient.execute(translate);
            json = EntityUtils.toString(execute.getEntity(), "UTF-8");
        } catch (Exception e){
            e.printStackTrace();
        }
        TranslatorResponse response = gson.fromJson(json, TranslatorResponse.class);

        response.getTranslations().forEach(translation -> System.out.println(translation.getTranslation()));
//        JOptionPane.showMessageDialog(null, response.getTranslations().get(0).getTranslation(), "Translation", JOptionPane.INFORMATION_MESSAGE);
        try {
            displayTray(response.getTranslations().get(0).getTranslation());
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void displayTray(String message) throws AWTException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);

        trayIcon.displayMessage("Translator", message.replace(message.substring(0, 1), message.substring(0, 1).toUpperCase()), MessageType.INFO);
    }

    private static String getBuffer() throws IOException, UnsupportedFlavorException {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Clipboard systemClipboard = defaultToolkit.getSystemClipboard();

        DataFlavor dataFlavor = DataFlavor.stringFlavor;
        String text = "";
        if (systemClipboard.isDataFlavorAvailable(dataFlavor)){
            text = (String) systemClipboard.getData(dataFlavor);
        }

        return text;
    }

    private static HttpClient createHttpClient(){
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials("apikey", SUBSCRIPTION_KEY);
        provider.setCredentials(AuthScope.ANY, credentials);
        return HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
    }
}
