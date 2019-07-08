package controller.client;

import controller.RequestEnum;
import controller.Transmitter;
import view.Graphic.GlobalChatScene;
import view.Graphic.SelectGameScene;
import view.Graphic.StageLauncher;

import java.util.ArrayList;

public class TransferController {
    private static Transmitter fromServerTransmitter;
    private static ClientIOHandler clientIOhandler = Client.getClientIOHandler();
    private static ArrayList<Transmitter> messages = new ArrayList<>();

    public static Transmitter main(RequestEnum requestEnum, Transmitter transmitter) {
        fromServerTransmitter = transmitter;
        transmitter.requestEnum = requestEnum;
        switch (requestEnum) {
                /*
            case COLLECTION_DECKS:
            case COLLECTION_CARDS:
            case COLLECTION_ITEMS:
            case COLLECTION_HELP:
            case COLLECTION_SEARCH_CARD:
            case COLLECTION_SEARCH_ITEM:
            */
            case LOGOUT:
            case COLLECTION_UPDATE:
            case EXPORT_DECK:
            case SEND_MESSAGE:
            case NEW_CARD_ID:
            case ENTER_COLLECTION:
            case EXIT_FROM_CHAT:
            case END_OF_CLIENT:
            case ACCEPT_PLAY:
            case DECLINE_PLAY:
            case CANCEL_START_MATCH:
            case START_MATCH:
                fromServerTransmitter = clientIOhandler.transfer(false, transmitter);
                return fromServerTransmitter;
            case SIGN_UP:
            case LOGIN:
            case IMPORT_DECK:
            case SHOP_BUY:
            case SHOP_HELP:
            case SHOP_SEARCH:
            case SHOP_CARDS:
            case SHOP_ITEMS:
            case SHOP_DARIC:
            case PROFILE:
            case DECKS:
            case START_STORY_GAME:
            case ENTER_CHAT:
            case START_CUSTOM_GAME:
            case SHOP_SELL:
            case MAKE_NEW_CARD:
            case NEW_CARD_ARRAY_LISTS:
            case MAIN_DECK:
            case GET_COLLECTION:
            case ALL_ACCOUNT:
                fromServerTransmitter = clientIOhandler.transfer(true, transmitter);
                return fromServerTransmitter;
            case CHECK_NEW_MESSAGE:
                if (messages.size() != 0) {
                    fromServerTransmitter = messages.get(0);
                    messages.remove(0);
                }
                return fromServerTransmitter;
               /*
               // try {
                    Thread one=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                object =(Transmitter) objectInputStream.readObject();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    one.start();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (object != null) {
                        fromServerTransmitter = (Transmitter) object;
                    }
                one.stop();
//                } catch (IOException | ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
                return fromServerTransmitter;*/
        }
        return fromServerTransmitter;

    }


    static void fromServerTransmitter(Transmitter transmitter) {
        switch (transmitter.requestEnum) {
            case NEW_MESSAGE:
                System.out.println("new message");
                //messages.add(transmitter);
                GlobalChatScene.getNewMessage(transmitter);
                break;
            case PLAY_REQUEST:
                System.out.println("play request - transfer controller");
                StageLauncher.getNewRequest(transmitter);
                break;
            case DECLINE_PLAY:
                SelectGameScene.decline();
                break;
            case BATTLE:
                SelectGameScene.startGame(transmitter.game, transmitter.match,
                        transmitter.numberOfMap, transmitter.battleMessage.imPlayer0);
                //todo make battle Scene
                break;
            case CANCEL_START_MATCH:
                //todo
        }
    }

}
