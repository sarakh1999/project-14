package view;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.account.Account;
import view.enums.StateType;
import view.sample.StageLauncher;


import java.util.ArrayList;

import static view.GeneralGraphicMethods.*;

public class ShopScene {
    private static Scene shopScene = StageLauncher.getScene(StateType.SHOP);
    private static Group root = (Group) shopScene.getRoot();
    private static ArrayList<HBox> hBoxes = new ArrayList<>();

    private static Group makeShopIconBar(String path, String input, int i) {
        Group group = new Group();
        group.relocate(20, (i + 1) * 100 - 50);

        addImage(group, path, 0, 0, 100, 100);

        Text text = addText(group, input, 120, 50,
                Color.rgb(225, 225, 225, 0.5), 30);

        text.setOnMouseEntered(event -> {
            Glow glow = new Glow();
            glow.setLevel(10);
            text.setEffect(glow);
        });
        text.setOnMouseExited(event -> text.setEffect(null));

        return group;


    }

    public static void makeRectIcon(int width, int height, int numberOfRows,
                                    String iconName, int numberOfCulonm,
                                    int numberOfIcons, String typeOfFile, int upperNumber) {
        HBox hBox;
        int j = 0;
        ImageView imageIcon;
        for (int i = 0; i < numberOfRows; i++) {
            hBox = new HBox();
            hBoxes.add(hBox);
            hBox.setSpacing(10);
            hBox.relocate(350, height * (i + 1) + 10 * i - upperNumber);
            root.getChildren().add(hBox);
            for (int k = 0; k < numberOfCulonm; k++) {
                j++;
                if (j > numberOfIcons)
                    return;
                imageIcon = addImage(hBox,
                        "pics/shop/" + iconName + "-" + j + typeOfFile, 0, 0, width, height);
            }
        }
    }

    public static void makeShopScene(Account account) {
        setBackground(root, "pics/shop/shop_background.jpg", false, 15, 15);
        VBox sideVBox = new VBox();
        sideVBox.relocate(0, 0);

        sideVBox.setBackground(new Background(new BackgroundFill(
                Color.rgb(10, 10, 10, 0.1),
                CornerRadii.EMPTY, Insets.EMPTY)));

        root.getChildren().add(sideVBox);

        root.getChildren().add(makeShopIconBar(
                "pics/shop/buy_card.png", "Buy Card", 0));

        root.getChildren().add(makeShopIconBar(
                "pics/shop/sellCard.png", "Sell Card", 1));

        Group search = makeShopIconBar(
                "pics/shop/research.png", "Search", 2
        );
        root.getChildren().add(search);

        Group emote = makeShopIconBar(
                "pics/shop/emotes.png", "Emotes", 3);
        root.getChildren().add(emote);

        Group profile = makeShopIconBar(
                "pics/shop/profile_icon.png", "Profile Icon", 4);
        root.getChildren().add(profile);

        Group battle_maps = makeShopIconBar(
                "pics/shop/battle_map.png", "Battle Maps", 5);
        root.getChildren().add(battle_maps);

        Group bundles = makeShopIconBar(
                "pics/shop/friends.png", "Bundles", 6);
        root.getChildren().add(bundles);

        Group orbs = makeShopIconBar(
                "pics/shop/spirit_orb.png", "Spirit Orbs", 7);
        root.getChildren().add(orbs);

        Rectangle rectangle = new Rectangle(20, 840, 220, 60);
        rectangle.setFill(Color.rgb(10, 10, 10));
        rectangle.setArcHeight(100);
        rectangle.setArcWidth(100);
        root.getChildren().add(rectangle);

        addImage(root, "pics/shop/diamond.png", 25, 840, 50, 50);
        addText(root, "Daric: " + account.getDaric(),
                80, 865, Color.rgb(225, 225, 225, 0.5), 20);


        emote.setOnMouseClicked(event -> {
            root.getChildren().removeAll(hBoxes);
            hBoxes.clear();
            makeRectIcon(200, 200, 4,
                    "emotes", 5, 20, ".png", 90);

        });
        orbs.setOnMouseClicked(event -> {
            root.getChildren().removeAll(hBoxes);
            hBoxes.clear();
            makeRectIcon(200, 200, 2,
                    "orbs", 4, 7, ".png", 90);
        });


        battle_maps.setOnMouseClicked(event -> {
            root.getChildren().removeAll(hBoxes);
            hBoxes.clear();
            makeRectIcon(200, 400, 2,
                    "battle_map", 4,
                    8, ".jpg", 300);
        });

        bundles.setOnMouseClicked(event -> {
            root.getChildren().removeAll(hBoxes);
            hBoxes.clear();
            makeRectIcon(210, 200, 4,
                    "bundles", 5,
                    17, ".png", 90);
        });

        profile.setOnMouseClicked(event -> {
            root.getChildren().removeAll(hBoxes);
            hBoxes.clear();
            makeRectIcon(200, 200, 4,
                    "profile", 5,
                    20, ".jpg", 90);
        });

        search.setOnMouseClicked(event -> {
            root.getChildren().removeAll(hBoxes);
            hBoxes.clear();
            HBox hBox = new HBox();

            addRectangle(hBox, 0, 0, 400, 60, 40, 40
                    , Color.rgb(0, 0, 0, 0.7));

            hBox.relocate(600, 200);
            Group group = new Group();
            addRectangle(group, 0, 0, 80, 60, 40, 40
                    , Color.rgb(0, 0, 0, 0.7));
            ImageView magnifier = addImage(group,
                    "pics/shop/research.png", 10, 3, 50, 50);
            hBox.getChildren().addAll(group);


            root.getChildren().addAll(hBox);
            hBoxes.add(hBox);
        });


    }


}
