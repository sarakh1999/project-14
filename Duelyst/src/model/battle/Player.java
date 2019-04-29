package model.battle;

import model.Item.Collectable;
import model.Item.Flag;
import model.account.Account;
import model.card.Card;
import model.card.Hero;
import model.land.LandOfGame;
import model.land.Square;
import model.requirment.Coordinate;
import view.enums.ErrorType;

import java.util.ArrayList;

public abstract class Player {
    private Account account;
    protected Deck mainDeck;
    private Hand hand;
    protected String type;
    private Match match;
    private int turnsPlayed = 0;
    private int mana;
    private GraveYard graveYard = new GraveYard(this);
    protected Player opponent;
    ArrayList<Flag> flags;
    protected ArrayList<Card> cardsOnLand = new ArrayList<>();

    public abstract void move(Card card, Square newPosition);
    public abstract void attack(Card card, Square target);
    public abstract void putCardOnLand(Card playerCard, Coordinate coordinate, LandOfGame land);
    public abstract void useSpecialPower(Card card);

    public Hero getHero() {
        return mainDeck.getHero();

    }

    public void putCollectableItemOnLand(Coordinate coordinate,String  collectableItemId) {
        //todo
    }

    public void addItemToCollectables(Collectable collectable) {
        hand.getCollectableItems().add(collectable);
    }

    public Card passCardInGame(String cardId) {
        Card card = hand.passCardInHand(cardId);
        if (card != null)
            return card;
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(cardsOnLand);
        for (Card outPutCard : cards) {
            if (outPutCard.equalCard(cardId) && outPutCard.getPlayerName().equals(account.getUserName()))
                card = outPutCard;
        }
        return card;
    }

    public Player getOpponent() {
        return opponent;
    }



    public void initPerTurn() {
        hand.checkTheHandAndAddToIt();
        for (Card card : cardsOnLand) {
            card.changeTurnOfCanNotAttack(-1);
            card.changeTurnOfCanNotCounterAttack(-1);
            card.changeTurnOfCanNotMove(-1);
            if (card.getTurnOfCanNotAttack() <= 0)
                card.setCanCounterAttack(true);
            if (card.getTurnOfCanNotCounterAttack() <= 0)
                card.setCanCounterAttack(true);
            if (card.getTurnOfCanNotMove() <= 0)
                card.setCanMove(true);
        }
        turnsPlayed++;
        mana++;
    }

    public void counterAttack(Card card, Card theOneWhoAttacked) {
//todo
    }

    public void addToCardsOfLand(Card card) {
        cardsOnLand.add(card);
    }

    public GraveYard getGraveYard() {
        return graveYard;
    }

    public Account getAccount() {
        return account;
    }

    public Deck getMainDeck() {
        return mainDeck;
    }

    public Hand getHand() {
        return hand;
    }

    public String getType() {
        return type;
    }

    public Match getMatch() {
        return match;
    }

    public int getTurnsPlayed() {
        return turnsPlayed;
    }

    public void setHand() {
        hand = new Hand(mainDeck);
        hand.setCards();
    }


    public abstract void playTurn();

    public abstract void addToAccountWins();

    public abstract void addMatchInfo(MatchInfo matchInfo);

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setMainDeck(Deck mainDeck) {
        this.mainDeck = mainDeck;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public void setTurnsPlayed(int turnsPlayed) {
        this.turnsPlayed = turnsPlayed;
    }

    public void setGraveYard(GraveYard graveYard) {
        this.graveYard = graveYard;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }
}
