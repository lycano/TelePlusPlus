package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TppLogger {
    public static final Logger logger = Logger.getLogger("Minecraft");

    public static void severe(String string, Exception ex) {
        logger.log(Level.SEVERE, "[Tele++] " + string, ex);
    }

    public static void severe(String string) {
        logger.log(Level.SEVERE, "[Tele++] ".concat(string));
    }

    public static void info(String string) {
        logger.log(Level.INFO, "[Tele++] ".concat(string));
    }

    public static void warning(String string) {
        logger.log(Level.WARNING, "[Tele++] ".concat(string));
    }
    
    public static void Log(String txt) {
        logger.log(Level.INFO, String.format("[Tele++] %s", txt));
    }

    public static void Log(Level loglevel, String txt) {
        Log(loglevel, txt, true);
    }

    public static void Log(Level loglevel, String txt, boolean sendReport) {
        logger.log(loglevel, String.format("[Tele++] %s", txt == null ? "" : txt));
    }

    public static void Log(Level loglevel, String txt, Exception params) {
        if (txt == null) {
            Log(loglevel, params);
        } else {
            logger.log(loglevel, String.format("[Tele++] %s", txt == null ? "" : txt), (Exception) params);
        }
    }

    public static void Log(Level loglevel, Exception err) {
        logger.log(loglevel, String.format("[Tele++] %s", err == null ? "? unknown exception ?" : err.getMessage()), err);
    }
}
