package client_java.model.login;

import Server.CommonInterface.CallBackInterface;
import Server.CommonInterface.CallBackInterfaceHelper;
import Server.CommonObjects.User;
import Server.PlayerSide.GameSession;
import Server.PlayerSide.GameSessionHelper;
import Server.PlayerSide.PlayerInterface;
import Server.PlayerSide.PlayerInterfaceHelper;
import Server.Exceptions.AlreadyLoggedInException;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import serverMain.ExceptionImpl;

import javax.swing.*;
import java.util.Arrays;
import java.util.Properties;

public class LoginModel {
    private PlayerInterface playerServer;

    public LoginModel() {
        try {
            System.out.println("[Client] Initializing ORB...");
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialHost", "localhost");
            props.put("org.omg.CORBA.ORBInitialPort", "1049");
            props.put("com.sun.CORBA.transport.ORBConnectTimeout", "10000"); // Increased timeout

            ORB orb = ORB.init(new String[0], props);
            System.out.println("[Client] ORB initialized");

            // Wait for server to be ready
            Thread.sleep(2000);

            System.out.println("[Client] Resolving NameService...");
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            System.out.println("[Client] NameService resolved");

            System.out.println("[Client] Looking up PlayerServer...");
            playerServer = PlayerInterfaceHelper.narrow(ncRef.resolve_str("PlayerServer"));
            System.out.println("[Client] PlayerServer reference obtained: " + (playerServer != null));

        } catch (Exception e) {
            System.err.println("[Client] Connection error:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Connection failed: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String authenticate(String username, char[] password) {
        try {
            System.out.println("[Client] Creating callback...");
            ExceptionImpl callback = new ExceptionImpl();

            System.out.println("[Client] Getting root POA...");
            ORB orb = ORB.init(new String[0], null);
            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));

            System.out.println("[Client] Activating callback...");
            org.omg.CORBA.Object callbackRef = rootPOA.servant_to_reference(callback);
            CallBackInterface cbInterface = CallBackInterfaceHelper.narrow(callbackRef);

            System.out.println("[Client] Attempting login for: " + username);
            String userId = playerServer.login(cbInterface, username, new String(password));
            Arrays.fill(password, '0');

            System.out.println("[Client] Login successful! Token: " + userId);
            return userId;
        } catch (Exception e) {
            System.err.println("[Client] Login failed:");
            e.printStackTrace();
            return null;
        }
    }
}