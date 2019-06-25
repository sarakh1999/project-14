package view.Graphic;

import javafx.scene.Group;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.battle.Player;
import view.enums.StateType;

import java.io.File;
import java.util.Random;

public class BattleHeaderGraphic {
    private Group root;
    private Group rightHeader = new Group();
    private Group leftHeader = new Group();
    private BattleScene battleScene;
    private String[] specialPowersPath = new String[2];
    private Circle circleForSpecialPower = null;

    //TODO Mana ke avas mishe avas kone -> listener
    //TODO TODO TODO TODO SABAAAAA

    BattleHeaderGraphic(BattleScene battleScene, Group root) {
        this.battleScene = battleScene;
        this.root = root;
        root.getChildren().addAll(rightHeader, leftHeader);

        initHeader();

    }

    private void addPortraitBorder(double x, double y, Group group, boolean turnOfThisPlayer, String avatarPath, boolean leftSide) {
        ImageView imageView = GeneralGraphicMethods.addImage(group, avatarPath, x - 10, y - 10, 130, 130);
        /*
        ImageView imageView =new ImageView();
        if (avatarPath != null) {
            imageView = GeneralGraphicMethods.addImage(group, avatarPath, x-10 , y-10 , 150, 150);
            if (!leftSide) {
                imageView.setRotationAxis(Rotate.Y_AXIS);
                imageView.setRotate(180);
            }
        }
        if(!turnOfThisPlayer) {
            ColorAdjust blackout = new ColorAdjust();
            blackout.setBrightness(-0.5);

            imageView.setEffect(blackout);
            imageView.setCache(true);
            imageView.setCacheHint(CacheHint.SPEED);
        }
       /* if (turnOfThisPlayer) {
            GeneralGraphicMethods.addImage(group, "pics/battle_categorized/general_portrait_border_highlight@2x.png", x-10, y-20, 130, 130);
        } else {
            GeneralGraphicMethods.addImage(group, "pics/battle_categorized/general_portrait_border@2x.png", x, y, 130, 130);
        }

        /*
        ImageView imageView1 = GeneralGraphicMethods.addImage(group, "pics/battle_categorized/general_portrait_border@2x.png", x, y, 130, 130);
        ImageView imageView2 = GeneralGraphicMethods.addImage(group, "pics/battle_categorized/general_portrait_border_highlight@2x.png", x, y, 130, 130);
        group.getChildren().remove(imageView2);
        imageView2.setOnMouseExited(event -> {
            group.getChildren().add(imageView1);
            group.getChildren().remove(imageView2);
        });
        imageView1.setOnMouseEntered(event -> {
            group.getChildren().add(imageView2);
            group.getChildren().remove(imageView1);
        });
        */
    }

    private void addNumberToMana(Group group, int numberOfMana, double x, double y) {
        Text text;
        if (x > 500) {
            x -= 60;
        } else
            x = x + 9 * 28 + 8;
        text = GeneralGraphicMethods.addText(group, x, y + 5, numberOfMana + " / 9",
                Color.rgb(225, 225, 225), 25);
        text.setStroke(Color.rgb(0, 0, 0, 0.5));
        text.setStrokeWidth(1);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        text.setOnMouseEntered(event -> {
            Glow glow = new Glow();
            glow.setLevel(10);
            text.setEffect(glow);
            text.setOnMouseExited(event1 -> text.setEffect(null));
        });
    }

    private void addMana(double x, double y, int numberOfMana, Group group) {
        for (int i = 0; i < 9; i++) {
            if (i < numberOfMana)
                GeneralGraphicMethods.addImage(group,
                        "pics/other/icon_mana@2x.png", x + i * 28, y, 25, 25);
            else
                GeneralGraphicMethods.addImage(group,
                        "pics/battle_categorized/icon_mana_inactive@2x.png",
                        x + i * 28, y, 25, 25);
        }
        addNumberToMana(group, numberOfMana, x, y + 2);
    }

    private void addCoolDown(double x, double y, Group group, int turnNotUsedSpecialPower, int coolDown) {
        Group coolDownGroup = new Group();
        group.getChildren().add(coolDownGroup);
        int currentX = 0, currentY = 0, tillNow = 0;
        for (int i = 0; i < coolDown; i++) {
            Color color = Color.rgb(200, 200, 200, 0.8);
            if (i < turnNotUsedSpecialPower)
                color = Color.rgb(0, 200, 200, 0.8);
            Rectangle rectangle = GeneralGraphicMethods.addRectangle(coolDownGroup, currentX, currentY, 10,
                    10, 3, 3, color);
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(1.5);
            currentX += 12;
            tillNow++;
            if (tillNow > 3) {
                tillNow = 0;
                currentX = 0;
                currentY += 12;
            }
        }
        coolDownGroup.relocate(x, y);
    }

    private void chooseHeroSpecialPowerPath(int ind) {
        File file = new File("pics/battle_categorized/heroSpecialPower/type1");
        Random random = new Random();
        if (file.listFiles() == null)
            return;
        int index = random.nextInt(file.listFiles().length);
        specialPowersPath[ind] = file.listFiles()[index].getPath();
    }

    private void addHeroSpecialPower(double x, double y, Group group, int ind, int turnNotUsedSpecialPower, int coolDown, boolean itsTurn) {
        ImageView imageView = SpriteMaker.getInstance().makeSpritePic(specialPowersPath[ind],
                x, y, group, 19, 3, 2500, 34.5, 34.5);


        imageView.setFitWidth(35);
        imageView.setFitHeight(35);

        if (itsTurn) {
            if (circleForSpecialPower == null)
                circleForSpecialPower = new Circle(x + imageView.getFitWidth() / 2,
                        y + imageView.getFitHeight() / 2, imageView.getFitWidth() + 5);
            circleForSpecialPower.setStroke(Color.rgb(0, 204, 255));
            circleForSpecialPower.setStrokeWidth(0);
            circleForSpecialPower.setFill(Color.gray(1, 0.01));
            group.getChildren().add(circleForSpecialPower);

            GeneralGraphicMethods.setOnMouseEntered(circleForSpecialPower, StageLauncher.getScene(StateType.BATTLE), true);
            GeneralGraphicMethods.setOnMouseEntered(imageView, StageLauncher.getScene(StateType.BATTLE), true);

            circleForSpecialPower.setOnMouseClicked(event -> {
                if (!DragAndDrop.getWait()) {
                    if (!battleScene.isHeroSpecialPowerClicked()) {
                        activeSpecialPower();
                    } else {
                        deactiveSpecialPower();
                    }
                }
            });
        }

        addCoolDown(x, y + 50, group, turnNotUsedSpecialPower, coolDown);
    }

    public void activeSpecialPower() {
        battleScene.setHeroSpecialPowerClicked(true);
        circleForSpecialPower.setStrokeWidth(5);
    }

    public void deactiveSpecialPower() {
        battleScene.setHeroSpecialPowerClicked(false);
        circleForSpecialPower.setStrokeWidth(0);
    }

    private void initHeader() {
        chooseHeroSpecialPowerPath(0);
        chooseHeroSpecialPowerPath(1);
        makeLeftHeader(battleScene.getMatch().getPlayers()[0]);
        makeRightHeader(battleScene.getMatch().getPlayers()[1]);

        /*
        PerspectiveTransform perspectiveTransform = new PerspectiveTransform();
        perspectiveTransform.setUlx(115);
        perspectiveTransform.setUly(30);
        perspectiveTransform.setUrx(546);
        perspectiveTransform.setUry(21);
        perspectiveTransform.setLlx(115);
        perspectiveTransform.setLly(200);
        perspectiveTransform.setLrx(546);
        perspectiveTransform.setLry(177);

        leftHeader.setEffect(perspectiveTransform);
*/
        /*
        addManaLeft(248, 96, 9);
        addManaRight(920, 96, 9);
        */
    }

    private void makeLeftHeader(Player player) {
        leftHeader.getChildren().clear();
        GeneralGraphicMethods.addTextWithShadow(leftHeader, 248, 78, player.getUserName(), "Arial", 27);
        addMana(245, 100, player.getMana(), leftHeader);
        addHeroSpecialPower(110, 195, leftHeader, 0, player.getHero().getTurnNotUsedSpecialPower(), player.getHero().getCoolDown(), true);
        addPortraitBorder(120, 25, leftHeader, true, player.getAvatarPath(), true);
        //      addPortraitBorder(1165, 25, rightHeader, false, COMPUTER_PROFILE, false);

        showOwnedFlag(leftHeader, 245, 140, player.getNumberOfFlagsSaved(), player.getMatch().getNumberOfFlags(), player.getTurnForSavingFlag());
    }

    private void makeRightHeader(Player player) {
        rightHeader.getChildren().clear();
        GeneralGraphicMethods.addTextWithShadow(rightHeader, 1010, 78, player.getUserName(), "Arial", 27);
        addMana(911, 100, player.getMana(), rightHeader);
        addHeroSpecialPower(1275, 195, rightHeader, 1, player.getHero().getTurnNotUsedSpecialPower(), player.getHero().getCoolDown(), false
        );
        addPortraitBorder(1165, 25, rightHeader, true, player.getAvatarPath(), false);
        //     addPortraitBorder(120, 25, leftHeader, false, COMPUTER_PROFILE, true);
        showOwnedFlag(rightHeader, 911, 140, player.getNumberOfFlagsSaved(), player.getMatch().getNumberOfFlags(), player.getTurnForSavingFlag());
    }

    public void makeHeaderEachTurn(int numberOfPlayer, Player player) {
        if (numberOfPlayer == 0) {
            makeLeftHeader(player);
        } else {
            makeRightHeader(player);
        }
    }

    private void showOwnedFlag(Group group, double x, double y, int numberOfFlag, int totalNumberOfFlag, int howManyTurn) {
        if (totalNumberOfFlag == 0)
            return;

        GeneralGraphicMethods.addImage(group, "pics/battle_categorized/flag.gif", x, y, 25, 25);
        String string = numberOfFlag + " / " + totalNumberOfFlag + " flags";
        if (totalNumberOfFlag == 1)
            string = string + " for " + howManyTurn + " turn";
        Text text = GeneralGraphicMethods.addText(group, x, y + 30, string,
                Color.rgb(225, 225, 225), 25);
        text.setStroke(Color.rgb(0, 0, 0, 0.5));
        text.setStrokeWidth(1);
    }
}
