package server;

import GameServer.GameServerInterface;
import GameServer.GameServerInterfaceHelper;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

public class HangmanServer  {

    static Thread orbThread;
    public static void main(String[] args) {
        try {
            // =============  initialize the ORB architecture ============== //
            ORB orb = ORB.init(args, null);

            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // ============= creating the servant ================ //
            HangmanServant server = new HangmanServant();
            server.setORB(orb);

            org.omg.CORBA.Object obj = rootpoa.servant_to_reference(server);
            GameServerInterface gameServerInterface = GameServerInterfaceHelper.narrow(obj);

            org.omg.CORBA.Object objectref = orb.resolve_initial_references("NameService");
            NamingContextExt nc = NamingContextExtHelper.narrow(objectref);

            NameComponent[] serverPath = nc.to_name("Server");
            nc.rebind(serverPath, gameServerInterface);

            orbThread = new Thread(orb::run);
            orbThread.start();
            System.out.println("Server started....");

        } catch (InvalidName | AdapterInactive | WrongPolicy | ServantNotActive |
                 org.omg.CosNaming.NamingContextPackage.InvalidName | CannotProceed | NotFound e) {
            throw new RuntimeException(e);
        }
    }
}
