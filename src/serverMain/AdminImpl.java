package serverMain;

import Server.AdminSide.AdminInterfacePOA;
import Server.CommonInterface.CallBackInterface;
import Server.CommonObjects.GameResult;
import Server.CommonObjects.GameRules;
import Server.CommonObjects.User;
import Server.Exceptions.AlreadyLoggedInException;
import Server.Exceptions.LostConnectionException;
import Server.Exceptions.NoSuchUserFoundException;
import Server.Exceptions.NotLoggedInException;

public class AdminImpl extends AdminInterfacePOA {
    @Override
    public User login(CallBackInterface cb, String userId, String password) throws LostConnectionException, AlreadyLoggedInException {
        return null;
    }

    @Override
    public void createPlayer(String playerId, String password) throws LostConnectionException, NotLoggedInException {

    }

    @Override
    public void editUserDetails(String userId, String password) throws LostConnectionException, NotLoggedInException, NoSuchUserFoundException {

    }

    @Override
    public void banPlayer(String playerId) throws LostConnectionException, NotLoggedInException, NoSuchUserFoundException {

    }

    @Override
    public void deleteUser(String userId) throws LostConnectionException, NotLoggedInException, NoSuchUserFoundException {

    }

    @Override
    public User[] getUserList() throws LostConnectionException, NotLoggedInException {
        return new User[0];
    }

    @Override
    public User searchUser(String playerId) throws LostConnectionException, NotLoggedInException, NoSuchUserFoundException {
        return null;
    }

    @Override
    public GameRules changeRules(int newGuessTime, int newRounds, int newGuessLimit) throws LostConnectionException, NotLoggedInException {
        return null;
    }

    @Override
    public GameResult[] getGameHistory() throws LostConnectionException, NotLoggedInException {
        return new GameResult[0];
    }

    @Override
    public GameResult[] getPlayerHistory(String playerId) throws LostConnectionException, NotLoggedInException, NoSuchUserFoundException {
        return new GameResult[0];
    }

    @Override
    public String ping(String userId) throws LostConnectionException, NotLoggedInException {
        return "";
    }
}
