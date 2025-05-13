package serverMain;

import Server.CommonInterface.CallBackInterfacePOA;
import Server.CommonObjects.GameResult;

public class ExceptionImpl extends CallBackInterfacePOA {
    @Override
    public void receivePing(String client_id) {

    }

    @Override
    public void heartbeat() {

    }

    @Override
    public void updatePlayerList(String[] players) {

    }

    @Override
    public void gameStarted() {

    }

    @Override
    public void yourTurn() {

    }

    @Override
    public void wordToGuess(String wordWithBlanks) {

    }

    @Override
    public void guessResult(boolean correct, char letter) {

    }

    @Override
    public void gameEnded(GameResult result) {

    }
}
