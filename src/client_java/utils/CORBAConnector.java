package client_java.utils;

import GameServer.GameServerInterface;
import GameServer.GameServerInterfaceHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class CORBAConnector {
    private static GameServerInterface gameServerRef;

    public static GameServerInterface getGameServerRef() {
        if (gameServerRef != null) return gameServerRef;

        try {
            // Set up ORB with default parameters (can be overridden by command line args)
            String[] args = {
                    "-ORBInitialPort", "4321",  // Default port, matches common CORBA usage
                    "-ORBInitialHost", "localhost"
            };
            ORB orb = ORB.init(args, null);

            // Get the root naming context
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");

            // Use NamingContextExt which is part of the Interoperable Naming Service
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Resolve the object reference in naming
            String name = "Server";
            gameServerRef = GameServerInterfaceHelper.narrow(ncRef.resolve_str(name));

            System.out.println("Connected to Hangman Server");

        } catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace();
        }

        return gameServerRef;
    }
}