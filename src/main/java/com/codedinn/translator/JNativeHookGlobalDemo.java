package com.codedinn.translator;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JNativeHookGlobalDemo implements NativeKeyListener {
    private static boolean isCtrl;
    private static boolean isShift;
    private static int i;

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent k) {
//        System.out.println(NativeKeyEvent.getModifiersText(k.getKeyCode()));
//        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(k.getKeyCode()) + " BOOL: " + k.isActionKey());
//        int[] holder = new int[3];
//        holder[i++] = k.getKeyCode();
//        if (i == 3){

        if (k.getKeyCode() == NativeKeyEvent.VC_F7) {
            Main.translate();
        }

//
//
//
//
//            // clear state
//            holder[0] = 0;
//            holder[1] = 0;
//            holder[2] = 0;
//            i = 0;
//        }
//
//
//        System.out.println(k.getKeyCode() + " " + NativeKeyEvent.CTRL_L_MASK);
//        System.out.println(isCtrl);
//        if ()
//            isCtrl = false;
//        else {
//            if (k.getKeyCode() == NativeKeyEvent.VC_CONTROL || isCtrl) {
//                isCtrl = true;
//                if (k.getKeyCode() == NativeKeyEvent.VC_SHIFT || isShift) {
//                    isShift = true;
//                    if (k.getKeyCode() == NativeKeyEvent.VC_F7) {
//                        System.out.println("Good!");
//                    } else {
//                        isCtrl = false;
//                        isShift = false;
//                    }
//
//                }
//            }
//        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }

    public static void main(String[] args) {
        // Get the logger for "org.jnativehook" and set the level to off.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new JNativeHookGlobalDemo());
    }

}
