package model.land;

import model.battle.Match;
import model.card.Hero;
import model.card.Minion;
import model.card.Spell;
import model.item.Collectible;
import model.item.Flag;
import model.item.Usable;
import model.requirment.Coordinate;

public class LandOfGame {
    private static final int NUMBER_OF_ROWS = 5;
    private static final int NUMBER_OF_COLUMNS = 9;
    private Square[][] squares = new Square[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];

    public LandOfGame() {
        Coordinate coordinate;
        for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
            for (int j = 0; j < NUMBER_OF_ROWS; j++) {
                coordinate = new Coordinate();
                coordinate.setX(j);
                coordinate.setY(i);
                squares[j][i] = new Square(coordinate);
            }
        }
    }

    public Square passSquareInThisCoordinate(Coordinate coordinate) {
        int x = coordinate.getX();
        int y = coordinate.getY();
        if (x < 0 || x > 4 || y < 0 || y > 8)
            return null;
        return squares[coordinate.getX()][coordinate.getY()];
    }
//    public void removeCardFromAnSquare(Coordinate coordinate){
//        squares[coordinate.getX()][coordinate.getY()].removeCardFromSquare();
//    }
//    public void addCardToAnSquare(Coordinate coordinate, Card card){
//        squares[coordinate.getX()][coordinate.getY()].putCard(card);
//    }


    public Square[][] getSquares() {
        return squares;
    }

    public int getNumberOfColumns() {
        return NUMBER_OF_COLUMNS;
    }

    public int getNumberOfRows() {
        return NUMBER_OF_ROWS;
    }

    public void showMap(Match match) {
        Object object;
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 5; i++) {
                object = squares[i][j].getObject();
                System.out.print("|");
                if (object instanceof Flag)
                    System.out.println("F");
                if(object instanceof Collectible)
                    System.out.println("C");
                if(object instanceof Usable)
                    System.out.println("U");
                if(object instanceof Hero)
                    System.out.println("H");
                if(object instanceof Spell)
                    System.out.println("S");
                if(object instanceof Minion)
                    System.out.println("M");
                else
                    System.out.println(" ");

            }

        }
    }

}
