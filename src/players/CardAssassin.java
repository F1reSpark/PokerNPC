package players;

import game.HandRanks;
import game.Player;

public class CardAssassin extends Player {

    private int notedValue1 = 0;
    private int notedValue2 = 0;
    private int notedSuit1 = 0;
    private int notedSuit2 = 0;
    private int handEval = 0;

    public CardAssassin(String name) {
        super(name);
    }

    @Override
    protected void takePlayerTurn() {
        System.out.println("my hand is " + evaluatePlayerHand());
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
            raise(getGameState().getTableMinBet() + handEval);
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
            return false;
        }


        //return false;
    }

    @Override
    protected boolean shouldCheck() {
        if(getGameState().getNumRoundStage() == 0 || getGameState().getNumRoundStage() == 1){
            return true;
        }else {
            HandRanks myHand = evaluatePlayerHand();
            switch (myHand) {
                case ROYAL_FLUSH, STRAIGHT_FLUSH, FOUR_OF_A_KIND, FULL_HOUSE, FLUSH, STRAIGHT, THREE_OF_A_KIND, TWO_PAIR:
                    return true;
                case PAIR:
                    if (getGameState().getNumRoundStage() == 0 || getGameState().getNumRoundStage() == 1 || getGameState().getNumRoundStage() == 2) {
                        return true;
                    } else return false;
                case HIGH_CARD:
                    if (getGameState().getNumRoundStage() == 1 || getGameState().getNumRoundStage() == 0) {
                        return true;
                    } else return false;
                default:
                    return false;
            }
        }
    }

    @Override
    protected boolean shouldCall() {
        HandRanks myHand = evaluatePlayerHand();
        if (isBetActive()) {
            switch (myHand) {
                case ROYAL_FLUSH, STRAIGHT_FLUSH, FOUR_OF_A_KIND, FULL_HOUSE, FLUSH, STRAIGHT, THREE_OF_A_KIND:
                    return true;
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
            if (isBetActive()) {
                return false;
            } else {
                HandRanks myHand = evaluatePlayerHand();
                if (!shouldAllIn()) {
                    switch (myHand) {
                        case ROYAL_FLUSH, STRAIGHT_FLUSH, FOUR_OF_A_KIND, FULL_HOUSE, FLUSH, STRAIGHT, THREE_OF_A_KIND:
                            if (getGameState().getNumRoundStage() > 1) {
                                return true;
                            } else {
                                return false;
                            }
                        case TWO_PAIR, PAIR:
                            if (getGameState().getNumRoundStage() > 2) {
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
                    case ROYAL_FLUSH, STRAIGHT_FLUSH, FOUR_OF_A_KIND, FULL_HOUSE, FLUSH, STRAIGHT:
                        if (getGameState().getNumRoundStage() != 0 && getGameState().getNumRoundStage() != 1) { return true; }
                        else{ return false; }
                    default:
                        return false;
                }
        }

        private void evalHand () {
            HandRanks myHand = evaluatePlayerHand();
                switch (myHand) {
                    case ROYAL_FLUSH:
                        handEval = 25;
                        break;
                    case STRAIGHT_FLUSH:
                        handEval = 22;
                        break;
                    case FOUR_OF_A_KIND:
                        handEval = 18;
                        break;
                    case FULL_HOUSE:
                        handEval = 15;
                        break;
                    case FLUSH:
                        handEval = 12;
                        break;
                    case STRAIGHT:
                        handEval = 10;
                        break;
                    case THREE_OF_A_KIND:
                        handEval = 8;
                        break;
                    case TWO_PAIR:
                        handEval = 5;
                        break;
                    case PAIR:
                        handEval = 3;
                        break;
                    case HIGH_CARD:
                        if (getGameState().getNumRoundStage() == 0){
                            handEval = 1;
                        } else handEval = 0;
                        break;
                    default:
                        handEval = 0;
                        break;
                }

        }


}
