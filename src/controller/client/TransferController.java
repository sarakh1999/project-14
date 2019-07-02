package controller.client;

import controller.OrderEnum;
import controller.RequestEnum;
import controller.Transmitter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TransferController {
    static Transmitter transmitter;
    static ObjectOutputStream objectOutputStream = Client.getObjectOutputStream();
    static ObjectInputStream objectInputStream = Client.getObjectInputStream();

    public static Transmitter main(OrderEnum order, Transmitter transmitter) {
        TransferController.transmitter = transmitter;
        switch (order) {
            case ENTER_DECK:
                transmitter.requestEnum = RequestEnum.COLLECTION_DECKS;
                transfer(true);
                return transmitter;
            case ENTER_COLLECTION:
                transmitter.requestEnum = RequestEnum.COLLECTION_SHOW;
                transfer(true);
                return transmitter;
            case EXPORT_DECK:
                transmitter.requestEnum = RequestEnum.COLLECTION_EXPORT;
                transfer(false);
                return transmitter;
            case IMPORT_DECK:
                transmitter.requestEnum = RequestEnum.COLLECTION_IMPORT;
                transfer(true);
                return transmitter;
            case NEW_DECK:
                transmitter.requestEnum = RequestEnum.COLLECTION_NEW_DECK;
                transfer(false);
                return transmitter;
            case MAIN_DECK:
                transmitter.requestEnum = RequestEnum.COLLECTION_SELECT_MAIN_DECK;
                transfer(false);
                return transmitter;
            case ENTER_CHAT:
                transmitter.requestEnum = RequestEnum.ENTER_CHAT;
                transfer(false);
                return transmitter;
            case CHAT:
                transmitter.requestEnum = RequestEnum.CHAT;
                transfer(false);
                return transmitter;
            case CHEK_NEW_MEESSAGE:
                try {
                    Object object =  objectInputStream.readObject();
                    if(object!=null){
                        transmitter.group=((Transmitter )object).group;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return transmitter;
        }
        return transmitter;

    }

    public static void transfer(boolean waitForAnswer) {
        try {
            objectOutputStream.writeObject(transmitter);
            objectOutputStream.flush();
            if (waitForAnswer) {
                while (true) {
                    transmitter = (Transmitter) objectInputStream.readObject();
                    if (transmitter != null) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
