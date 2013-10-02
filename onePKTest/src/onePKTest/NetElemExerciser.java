package onePKTest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cisco.onep.core.exception.OnepConnectionException;
import com.cisco.onep.core.exception.OnepException;
import com.cisco.onep.core.exception.OnepIllegalArgumentException;
import com.cisco.onep.core.exception.OnepInvalidSettingsException;
import com.cisco.onep.core.util.OnepConstants;
import com.cisco.onep.element.NetworkApplication;
import com.cisco.onep.element.NetworkElement;
import com.cisco.onep.element.SessionConfig;
import com.cisco.onep.element.SessionHandle;
import com.cisco.onep.element.SessionConfig.SessionTransportMode;

public class NetElemExerciser {

	SessionHandle  sh = null;
	NetworkElement ne = null;
	
	public void connectNetworkElement(NetworkApplication netApp) {

		Util utility = new Util();
		
		String strIPAddress = 	"10.249.249.2";
		String username = 		"admin";
		String password = 		"gtdlabs123";
		
		strIPAddress = utility.getParams("Enter IP Address", strIPAddress);
		username	 = utility.getParams("Enter Username", username);
		password	 = utility.getParams("Enter Password", password);		InetAddress ipAddress;

		try {
            ipAddress = InetAddress.getByName(strIPAddress);
            try {
            	ne = netApp.getNetworkElement(ipAddress);
            }
            catch (OnepInvalidSettingsException eISE){
            	System.out.println("Caught invalid settings exception.");
            }
            catch (OnepIllegalArgumentException ex) {
                Logger.getLogger(OnePKTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(OnePKTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        SessionConfig config = new SessionConfig(SessionTransportMode.SOCKET);
        config.setPort(OnepConstants.ONEP_PORT);

        try {
            sh = ne.connect("admin", "gdtlabs123", config);
        } catch (OnepConnectionException e) {
        } catch (OnepException e) {
        }
        
        if (sh == null) {
        	System.out.println("\nFailed to connect to NetworkElement - " + ne.getAddress());
        }
        else {
        	System.out.println("\nSuccessful connection to NetworkElement - " + ne.getAddress());
        }
	}

	public void teardownNetworkElement() {
		if(ne.isConnected())
		{
			System.out.println("\nDisconnecting Network Element " + ne.getAddress());
			ne.disconnect();
		}
	}
	
}
