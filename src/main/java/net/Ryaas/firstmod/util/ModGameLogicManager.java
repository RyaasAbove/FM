package net.Ryaas.firstmod.util;

public class ModGameLogicManager {
    private static TelekinesisHandler telekinesisHandler = new TelekinesisHandler();


    public static TelekinesisHandler getTelekinesisHandler() {
        return telekinesisHandler;
    }
    public static void setTelekinesisHandler(TelekinesisHandler handler) {
        telekinesisHandler = handler;
    }

}
