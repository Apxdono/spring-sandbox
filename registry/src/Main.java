import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 10.04.14
 * Time: 19:29
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) throws RemoteException {
//        System.setSecurityManager(new RMISecurityManager());
        LocateRegistry.createRegistry(1199);
        while (true){

        }
    }
}
