package players;

import game.Card;
import game.Evaluator;
import game.HandRanks;
import game.Player;

import java.util.ArrayList;

public class CardAssassin extends Player {

    private int notedValue1 = 0;
    private int notedValue2 = 0;
    private int notedSuit1 = 0;
    private int notedSuit2 = 0;
    private int handEval = 0;
    private double betEval;
    private boolean hasRaised;

    private Evaluator evalTool;

    public CardAssassin(String name) {
        super(name);
        evalTool = new Evaluator();
    }

    @Override
    protected void takePlayerTurn() {
        System.out.println("my hand is " + evaluatePlayerHand());
        ArrayList<Card> pseudoList = new ArrayList<>();
        System.out.println("The table has a " + evalTool.evaluatePlayerHand(getGameState().getTableCards(), pseudoList));
//        HandRanks myHand = evaluatePlayerHand();
//        switch (myHand) {
//            case PAIR:
//                break;
//            case TWO_PAIR:
//
//        }
        notedValue1 = getHandCards().get(0).getValue();
        notedValue2 = getHandCards().get(1).getValue();
        notedSuit1 = getHandCards().get(0).getSuit();
        notedSuit2 = getHandCards().get(1).getSuit();

        evalHand();
        if (shouldFold()) {
            fold();
        } else if (shouldAllIn()) {
            allIn();
        } else if (shouldRaise()) {
            if(!hasRaised) {
                hasRaised = true;
                raise((int) (getGameState().getTableMinBet() * betEval));
            } else call();
        } else {
            if (shouldCall()) {
                call();
            } else if (shouldCheck()) {
                check();
            } else {
                fold();
            }
        }
    }

    @Override
    protected boolean shouldFold() {
        HandRanks myHand = evaluatePlayerHand();
        if (getGameState().getNumRoundStage() != 0){
            if (notedValue1 == 2 && notedValue2 == 7 && notedSuit1 != notedSuit2) {
                return true;
            } else if (notedValue1 == 7 && notedValue2 == 2 && notedSuit1 != notedSuit2) {
                return true;
            } else if (notedValue1 == 2 && notedValue2 == 8 && notedSuit1 != notedSuit2) {
                return true;
            } else if (notedValue1 == 8 && notedValue2 == 2 && notedSuit1 != notedSuit2) {
                return true;
            } else if (notedValue1 == 3 && notedValue2 == 8 && notedSuit1 != notedSuit2) {
                return true;
            } else if (notedValue1 == 8 && notedValue2 == 3 && notedSuit1 != notedSuit2) {
                return true;
            } else if (notedValue1 == 2 && notedValue2 == 9 && notedSuit1 != notedSuit2) {
                return true;
            } else if (notedValue1 == 9 && notedValue2 == 2 && notedSuit1 != notedSuit2) {
                return true;
            } else if (notedValue1 == 2 && notedValue2 == 6 && notedSuit1 != notedSuit2) {
                return true;
            } else if (notedValue1 == 6 && notedValue2 == 2 && notedSuit1 != notedSuit2) {
                return true;
            } else {
                if (getGameState().getTableBet() > getBank() * 0.5 && handEval == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            switch (myHand){
                case HIGH_CARD:
                    if(isBetActive() && getGameState().getNumRoundStage() > 2){
                        return true;
                    } else return false;
                case PAIR:
                    if (isBetActive() && getGameState().getNumRoundStage() > 3){
                        return true;
                    } else return false;
            }
            return false;
        }


        //return false;
    }

    @Override
    protected boolean shouldCheck() {
        ArrayList<Card> pseudoList = new ArrayList<>();
        if(!isBetActive()) {
            if (getGameState().getNumRoundStage() <= 1) {
                return true;
            } else {
                HandRanks myHand = evaluatePlayerHand();
                switch (myHand) {
                    case ROYAL_FLUSH, STRAIGHT_FLUSH, FOUR_OF_A_KIND, FULL_HOUSE, FLUSH, STRAIGHT, THREE_OF_A_KIND, TWO_PAIR:
                        return true;
                    case PAIR:
                        if (getGameState().getNumRoundStage() < 5) {
                            return true;
                        } else return false;
                    default:
                        return false;
                }
            }
        } else {
            if(shouldCall()){
                call();
            }
            return false;}
    }

    @Override
    protected boolean shouldCall() {
        ArrayList<Card> pseudoList = new ArrayList<>();
        HandRanks myHand = evaluatePlayerHand();
        if (isBetActive()) {
            switch (myHand) {
                case ROYAL_FLUSH, STRAIGHT_FLUSH, FOUR_OF_A_KIND, FULL_HOUSE, FLUSH, STRAIGHT, THREE_OF_A_KIND:
                    if (evalTool.evaluatePlayerHand(getGameState().getTableCards(), pseudoList) != evaluatePlayerHand()) {
                        return true;
                    } else return false;
                case TWO_PAIR, PAIR:
                    if (getGameState().getNumRoundStage() < 2) {
                        return true;
                    } else {
                        return false;
                    }
                case HIGH_CARD:
                    if (getGameState().getNumRoundStage() < 1) {
                        return true;
                    } else {
                        return false;
                    }
                default:
                    return false;

            }
        } else {return false;}
    }
        @Override
        protected boolean shouldRaise () {
            ArrayList<Card> pseudoList = new ArrayList<>();
            if (isBetActive()) {
                return false;
            } else {
                HandRanks myHand = evaluatePlayerHand();
                if (!shouldAllIn()) {
                    switch (myHand) {
                        case ROYAL_FLUSH, STRAIGHT_FLUSH, FOUR_OF_A_KIND, FULL_HOUSE, FLUSH, STRAIGHT, THREE_OF_A_KIND:
                            if (getGameState().getNumRoundStage() > 1 && evalTool.evaluatePlayerHand(getGameState().getTableCards(), pseudoList) != evaluatePlayerHand()) {
                                return true;
                            } else {
                                return false;
                            }
                        case TWO_PAIR, PAIR:
                            if (getGameState().getNumRoundStage() > 2 && evalTool.evaluatePlayerHand(getGameState().getTableCards(), pseudoList) != evaluatePlayerHand()){
                                return true;
                            } else {
                                return false;
                            }
                        default:
                            return false;

                    }
                } else {
                    return false;
                }
            }
        }

        @Override
        protected boolean shouldAllIn () {
            HandRanks myHand = evaluatePlayerHand();
                switch (myHand) {
                    case ROYAL_FLUSH, STRAIGHT_FLUSH, FOUR_OF_A_KIND:
                        if (getGameState().getNumRoundStage() > 3) { return true; }
                        else{ return false; }
                        //maybe separate these to wait a little longer just in case
                    default:
                        return false;
                }
        }

        private void evalHand () {
            ArrayList<Card> pseudoList = new ArrayList<>();
            HandRanks myHand = evaluatePlayerHand();
            if (evalTool.evaluatePlayerHand(getGameState().getTableCards(), pseudoList) != myHand) {
                switch (myHand) {
                    case ROYAL_FLUSH:
                        betEval = 5.5;
                        handEval = 10;
                        break;
                    case STRAIGHT_FLUSH:
                        betEval = 5;
                        handEval = 9;
                        break;
                    case FOUR_OF_A_KIND:
                        betEval = 4.5;
                        handEval = 8;
                        break;
                    case FULL_HOUSE:
                        betEval = 4;
                        handEval = 7;
                        break;
                    case FLUSH:
                        betEval = 3.5;
                        handEval = 6;
                        break;
                    case STRAIGHT:
                        betEval = 3;
                        handEval = 5;
                        break;
                    case THREE_OF_A_KIND:
                        betEval = 2.5;
                        handEval = 4;
                        break;
                    case TWO_PAIR:
                        betEval = 2;
                        handEval = 3;
                        break;
                    case PAIR:
                        betEval = 1.5;
                        handEval = 2;
                        break;
                    case HIGH_CARD:
                        betEval = 1;
                        if (getGameState().getNumRoundStage() == 0) {
                            handEval = 1;
                        } else handEval = 0;
                        break;
                    default:
                        handEval = 0;
                        break;
                }
            } else scrutinizeHand();

        }

        private void scrutinizeHand(){
            HandRanks myHand = evaluatePlayerHand();
            switch (myHand){
                case ROYAL_FLUSH, STRAIGHT_FLUSH, FOUR_OF_A_KIND, FULL_HOUSE:
                    betEval = 2.5;
                    handEval = 2;
                    break;
                case FLUSH, STRAIGHT, THREE_OF_A_KIND:
                    betEval = 1.5;
                    handEval = 1;
                    break;
                default:
                    betEval = 1;
                    handEval = 0;
                    break;
            }
        }


}
