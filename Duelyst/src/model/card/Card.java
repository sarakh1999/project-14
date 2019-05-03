package model.card;


import model.battle.Player;
import model.item.Collectible;
import model.item.Flag;
import model.land.LandOfGame;
import model.land.Square;
import model.requirment.Coordinate;
import view.enums.ErrorType;
import view.enums.RequestSuccessionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public abstract class Card {
    protected Change change = new Change();//HAS-A
    protected Target target = new Target();
    protected int mp;
    protected int hp;
    protected int ap;
    protected int turnOfCanNotMove = 0;
    protected int turnOfCanNotAttack = 0;
    protected int turnOfCanNotCounterAttack = 0;
    private String name;
    private CardId cardId;
    private ArrayList<Integer> turnsOfPickingUp = new ArrayList<>();
    private String counterAttack;
    protected int attackRange;
    private int cost;
    private HashMap<Buff, ArrayList<Integer>> buffsOnThisCard = new HashMap<>(); //todo to init perturn as addada kam kone har ki sefr shod disaffect seda kone
    private Square position;
    private LandOfGame landOfGame;
    private int CardNumber;
    private Player player;
    private boolean canMove = false;
    private boolean canAttack = false;
    private boolean canCounterAttack = true;
    private int hpChangeAfterAttack = 0; //todo mogheE ke be yeki hamle mishe va az hpsh kam mishe bayad ba in jam konin hpSh ro
    private String description;

    public Change getChange() {
        return change;
    }

    public void addBuff(Buff buff, int forHowManyTurn) {
        if (!buffsOnThisCard.containsKey(buff))
            buffsOnThisCard.put(buff, new ArrayList<>());
        buffsOnThisCard.get(buff).add(forHowManyTurn);
    }

    public void removeBuffs(boolean goodBuff) {
        ArrayList<Buff> buffsWhichAreGoingToDeleted = new ArrayList<>();
        for (Buff buff : buffsOnThisCard.keySet()) {
            if (buff.isGoodBuff() == goodBuff)
                buffsWhichAreGoingToDeleted.add(buff);
        }
        for (Buff buff : buffsWhichAreGoingToDeleted) {
            buffsOnThisCard.remove(buff);
        }
    }

    public void removeBuff(String buffName) {
        for (Buff buff : buffsOnThisCard.keySet()) {
            if (buff.getName().equals(buffName)) {
                buffsOnThisCard.remove(buff);
                return;
            }
        }
    }


    public void move(Coordinate newCoordination) {
        Square newPosition = landOfGame.passSquareInThisCoordinate(newCoordination);
        if (!canMove) {
            ErrorType.CAN_NOT_MOVE_BECAUSE_OF_EXHAUSTION.printMessage();
            return;
        }
        if (canMoveToCoordination(this, newCoordination) && withinRange(newCoordination, 2)) {
            if (this instanceof Minion) {
                if (((Minion) this).getActivationTimeOfSpecialPower() == ActivationTimeOfSpecialPower.ON_RESPAWN) {
                    setTarget(this, newPosition);

                    //todo AffectSpecialPower
                }
            }
            if (newPosition.getObject() instanceof Flag) {
                ((Flag) newPosition.getObject()).setOwnerCard(this);
                player.addToOwnFlags((Flag) newPosition.getObject());
                player.setFlagSaver(this);
                player.addToTurnForSavingFlag();
            }
            if (newPosition.getObject() instanceof Collectible) {
                player.getHand().addToCollectibleItem((Collectible) newPosition.getObject());
            }
            // if(newPosition.getObject() instanceof ) todo asare khane

            setPosition(newPosition);
            newPosition.setObject(this);
            position.setObject(null);
            RequestSuccessionType.MOVE_TO.setMessage(getCardId().getCardIdAsString() +
                    "moved to" + newCoordination.getX() + newCoordination.getY());
            RequestSuccessionType.MOVE_TO.printMessage();
            canMove = false;
        } else
            ErrorType.INVALID_TARGET.printMessage();

    }

    public boolean canMoveToCoordination(Card card, Coordinate destination) {
        if (card.getManhatanDistance(destination) == 2) {
            int x = card.position.getXCoordinate();
            int y = card.position.getYCoordinate();
            int distanceOfX = destination.getX() - card.position.getXCoordinate();
            int distanceOfY = destination.getY() - card.position.getYCoordinate();
            if (Math.abs(distanceOfX) == 2 || Math.abs(distanceOfY) == 2) {
                x -= distanceOfX / 2;
                y -= distanceOfY / 2;
                Square square = landOfGame.getSquares()[x][y];
                if (square.getObject() != null)
                    return false;
            } else {
                x += distanceOfX;
                Square square = landOfGame.getSquares()[x][y];
                if (square.getObject() != null) {
                    x -= distanceOfX;
                    y += distanceOfY;
                    square = landOfGame.getSquares()[x][y];
                    if (square.getObject() != null)
                        return false;
                }
            }
        }
        return Objects.requireNonNull(landOfGame.passSquareInThisCoordinate(destination)).getObject() == null;
    }

    public boolean withinRange(Coordinate coordinate, int range) {
        if (counterAttack.equals("Ranged") && getNormalDistance(coordinate) == 1)
            return false;
        return getManhatanDistance(coordinate) <= range;
    }

    public void setTarget(Card card, Square CardSquare) {
        //todo checkIfAttackedCardIsValid to class target
        //todo check kone ke to classe targete card (one/all/column/row) hast
        //todo age square hast ya distance dare check kone
        //todo ArrayList e target ro to classe target bere bezare

    }

    public int getManhatanDistance(Coordinate coordinate) {
        return Math.abs(coordinate.getX() - position.getXCoordinate()) +
                Math.abs(coordinate.getY() - position.getYCoordinate());
    }

    public int getNormalDistance(Coordinate coordinate) {
        if (Math.abs(coordinate.getX() - position.getXCoordinate()) >= Math.abs(coordinate.getY() - position.getYCoordinate()))
            return Math.abs(coordinate.getX() - position.getXCoordinate());
        return Math.abs(coordinate.getY() - position.getYCoordinate());
    }

    public void attack(Card attackedCard) {
        if (attackedCard == null) {
            ErrorType.INVALID_CARD_ID.printMessage();
            return;
        }
        if (this instanceof Spell) {
            return;
        }
        if (!canAttack) {
            ErrorType.CAN_NOT_ATTACK.printMessage();
            return;
        }
        if (this instanceof Minion) {
            if (((Minion) this).getActivationTimeOfSpecialPower() == ActivationTimeOfSpecialPower.ON_ATTACK) {
                //todo affect special power
                setTarget(this, position);
                getChange().affect(player, this.getTargetClass().getTargets());
            }
        }

        if (!withinRange(attackedCard.position.getCoordinate(), attackRange)) {
            ErrorType.UNAVAILABLE_OPPONENT.printMessage();
            return;
        }
        attackedCard.changeHp(-ap + hpChangeAfterAttack);
        attackedCard.counterAttack(this);
        setCanAttack(false, 1);
    }

    public boolean isCanAttack() {
        return canAttack;
    }

    public void changeHp(int number) {
        hp += number;
        if (hp <= 0) {
            player.getGraveYard().addCardToGraveYard(this, position);
            position = null;
        }
    }

    public void counterAttack(Card theOneWhoAttacked) {
        boolean canCounterAttack = counterAttack.equals("Melee") &&
                getNormalDistance(theOneWhoAttacked.getPosition().getCoordinate()) == 1;
        if (!canCounterAttack)
            canCounterAttack = counterAttack.equals("Ranged") &&
                    getNormalDistance(theOneWhoAttacked.getPosition().getCoordinate()) != 1;
        if (!canCounterAttack)
            canCounterAttack = counterAttack.equals("Hybrid");
        if (this.canCounterAttack && canCounterAttack)
            theOneWhoAttacked.changeHp(-ap);
        if (theOneWhoAttacked instanceof Minion) {
            if (((Minion) theOneWhoAttacked).getActivationTimeOfSpecialPower() == ActivationTimeOfSpecialPower.ON_ATTACK) {
                //todo affect special power
                //setTarget(theOneWhoAttacked, position);
                // getChange().affect(, this.getTargetClass().getTargets());//todo chert momkene bashe
            }
        }
    }

    public void setCanAttack(boolean bool, int forHowManyTurn) {
        canAttack = bool;
        if (!bool) {
            setTurnOfCanNotAttack(Math.max(getTurnOfCanNotAttack(), forHowManyTurn));
        }
    }

    public Square getPosition() {
        return position;
    }

    public int getTurnOfCanNotAttack() {
        return turnOfCanNotAttack;
    }

    public void setTurnOfCanNotAttack(int number) {
        turnOfCanNotAttack = number;
    }

    public void setPosition(Square position) {
        this.position = position;
    }

    public void changeTurnOfCanNotAttack(int number) {
        turnOfCanNotAttack += number;
    }

    public void changeTurnOfCanNotCounterAttack(int number) {
        turnOfCanNotCounterAttack += number;
    }

    public void changeTurnOfCanNotMove(int number) {
        turnOfCanNotMove += number;
    }

    public void setCanMove(boolean canMove, int forHowManyTurn) {
        this.canMove = canMove;
        if (!canMove) {
            setTurnOfCanNotMove(Math.max(getTurnOfCanNotMove(), forHowManyTurn));
        }
    }

    public int getTurnOfCanNotMove() {
        return turnOfCanNotMove;
    }

    public void setTurnOfCanNotMove(int number) {
        turnOfCanNotMove = number;
    }

    public void setCanCounterAttack(boolean bool, int forHowManyTurn) {
        canCounterAttack = bool;
        if (!bool) {
            setTurnOfCanNotCounterAttack(Math.max(getTurnOfCanNotAttack(), forHowManyTurn));
        }
    }

    public void setCardIdFromClassCardId() {
    }

    public void addNewNameOfCardToCard(String cardName) {
    }

    public void decreaseNumberOfSameCard(String cardName) {

    }

    public void addToTurnsOfPickingUp(int turn) {
        turnsOfPickingUp.add(turn);
    }

    public void addToTurnOfpickingUp(int number) {
        turnsOfPickingUp.add(number);
    }

    public boolean equalCard(String cardId) {
        return this.cardId.getCardIdAsString().equals(cardId);
    }

    public void removeCounterAttack() {//TODO

    }

    public void changeAp(int number) {
        ap += number;
    }

    public void useSpecialPower(Coordinate coordinate) {
        ErrorType error;
        if (this instanceof Spell) {
            error = ErrorType.CAN_NOT_USE_SPECIAL_POWER;
            error.printMessage();
            return;
        }
        if (this instanceof Minion) {
            if (((Minion) this).getHaveSpecialPower()) {

                //todo AffectSpecialPower
                return;
            }

        }
        if (this instanceof Hero) {
            if (((Hero) this).getHaveSpecialPower()) {
                if (((Hero) this).getTurnNotUsedSpecialPower() <= ((Hero) this).getCoolDown()) {
                    //todo AffectSpecialPower
                    return;
                }
                ((Hero) this).setTurnNotUsedSpecialPower(0);
                return;
            }
        }
        error = ErrorType.DO_NOT_HAVE_SPECIAL_POWER;
        error.printMessage();
    }

    public static ArrayList<Hero> getHeroes(ArrayList<Card> cards) {
        ArrayList<Hero> heroes = new ArrayList<>();
        for (Card card : cards) {
            if (card instanceof Hero)
                heroes.add((Hero) card);
        }
        return heroes;
    }

    public static ArrayList<Minion> getMinions(ArrayList<Card> cards) {
        ArrayList<Minion> minions = new ArrayList<>();
        for (Card card : cards) {
            if (card instanceof Minion)
                minions.add((Minion) card);
        }
        return minions;
    }


    public static Card getCardById(String cardId, ArrayList<Card> cards) {
        for (Card card : cards) {
            if (card.getCardId().getCardIdAsString().equals(cardId))
                return card;
        }
        return null;
    }

    public CardId getCardId() {
        return cardId;
    }

    public void setCardId(CardId cardId) {
        this.cardId = cardId;
    }

    public static ArrayList<Spell> getSpells(ArrayList<Card> cards) {
        ArrayList<Spell> spells = new ArrayList<>();
        for (Card card : cards) {
            if (card instanceof Spell)
                spells.add((Spell) card);
        }
        return spells;
    }


    public void setHpChangeAfterAttack(int number) {
        hpChangeAfterAttack += number;
    }

    public void setLandOfGame(LandOfGame landOfGame) {
        this.landOfGame = landOfGame;
    }

    public Player getPlayer() {
        return player;
    }

    public Target getTargetClass() {
        return target;
    }

    public boolean isCanMove() {//maybe it have stun buff and can not move
        return canMove;
    }

    public int getTurnOfCanNotCounterAttack() {
        return turnOfCanNotCounterAttack;
    }

    public void setTurnOfCanNotCounterAttack(int number) {
        turnOfCanNotCounterAttack = number;
    }

    public boolean isCanCounterAttack() {
        return canCounterAttack;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public HashMap<Buff, ArrayList<Integer>> getBuffsOnThisCard() {
        return buffsOnThisCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRange() {
        return 0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCounterAttackName() {
        return counterAttack;
    }

    public void setCounterAttack(String counterAttack) {
        this.counterAttack = counterAttack;
    }

    public Boolean getCanMove() {
        return canMove;
    }

    public int getHp() {
        return hp;
    }

    public int getAp() {
        return ap;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }


}
