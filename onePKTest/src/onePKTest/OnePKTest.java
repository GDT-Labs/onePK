package onePKTest;

import com.cisco.onep.discovery.ServiceSetDescription;
import com.cisco.onep.element.*;
import com.cisco.onep.interfaces.InterfaceFilter;
import com.cisco.onep.interfaces.NetworkInterface;
import com.cisco.onep.policy.Policy;
import com.cisco.onep.system.ElementProcess;
import com.cisco.onep.core.exception.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class OnePKTest {

	NetworkApplication 	na = null;
	List<VRFContainer> vrfContainerList = null;
	
	public static void main(String[] args) {
		Util utility = new Util();
		OnePKTest pkT = new OnePKTest();
		String appName = "HolyGoatCheeseAKAoneExplorer";
		
		/*// Create a connection to the jabber.org server.
		Connection conn1 = new XMPPConnection("im");
		try {
			conn1.connect();
			conn1.login("brett.kugler@gdt.com", "");
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ChatManager chatmanager = conn1.getChatManager();
		Chat newChat = chatmanager.createChat("brett.kugler@gdt.com", new MessageListener() {
		    public void processMessage(Chat chat, Message message) {
		        System.out.println("Received message: " + message);
		    }
		});

		try {
		    newChat.sendMessage("Howdy!");
		}
		catch (XMPPException e) {
		    System.out.println("Error Delivering block");
		}
		*/
		appName	= utility.getParams("Enter Application Name", appName);
		pkT.start(pkT, appName);
    }
	
	public void start(OnePKTest pkT, String applicationName)
	{
		NetElemExerciser netElem = new NetElemExerciser();
		
		na = NetworkApplication.getInstance();
		
        try{
            if(na.getName()==null)
                na.setName(applicationName);
        }
        catch (OnepInvalidSettingsException eISE){
            System.out.println("\nCaught invalid settings exception.");
        }
        
		netElem.connectNetworkElement(na);
		pkT.evangelize();
		netElem.teardownNetworkElement();
		
		//pkT.evangelize();
	}
	
	public void evangelize() {
		
        System.out.println("\nWe have " + na.getConnectedElementCount() + " connected elements.");
		System.out.println("\nSinging the praises -");
        List<NetworkElement> ne_List = na.getElementList();
       
        if(ne_List.isEmpty()) {
            System.out.println("No network elements attached.");
        }
        else {
    	    System.out.println("Iterating over network element list:");
    	   

    	    for(NetworkElement neI : ne_List) {
            	NetworkElement element= neI;
            	System.out.println(element.getAddress());
            	
            	evangelizeSystemInfo(element);
            	evangelizeServiceSet(element);
            	evangelizeInterfaces(element);
            	evangelizeFRU(element);
            	evangelizeProcess(element);
            	evangelizePolicy(element);
            	evangelizeVRF(element);
            	
            	TopologyExerciser topo = new TopologyExerciser(element);
            	topo.evangelizeTopolgy();
            	
            }
    	}
	}

	public void evangelizeServiceSet(NetworkElement ne){
		List<ServiceSetDescription> ssdList = null;

		try {
			System.out.println("\nService Set defined on " + ne.getAddress() + ":");
			ssdList = ne.discoverServiceSetList();
			if(ssdList.isEmpty()){
				System.out.println("No Service Set Descriptors found.");
			}
			else{	
				for(ServiceSetDescription ssdI : ssdList){
					ServiceSetDescription ssdElement = ssdI;
					System.out.println(ssdElement.getServiceSetList());
	/*				try {
						TimeUnit.SECONDS.sleep(3);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				}
			}
		} catch (OnepException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void evangelizeInterfaces(NetworkElement ne){
		List<NetworkInterface> niList = null;

		try {
			System.out.println("\nInterfaces defined on " + ne.getAddress() + ":");
			InterfaceFilter iFilter = new InterfaceFilter();
			
			niList = ne.getInterfaceList(iFilter);
			if(niList.isEmpty()){
				System.out.println("No interfaces found.");
			}
			else{				
				vrfContainerList = new ArrayList<VRFContainer>();
				for(NetworkInterface niI : niList){
					NetworkInterface niElement = niI;
					System.out.println(niElement);
					try {
						String elementVRFName = niElement.getVRFName();
						//System.out.println("VRF: " + elementVRFName);
						
						addVRFtoContainer(elementVRFName, niElement.getName());
						
					} catch (OnepRemoteProcedureException e){
						addVRFtoContainer("empty", niElement.getName());
						//System.out.println("No VRF found for this interface.");
					}
				}	
			}
		} catch (OnepIllegalArgumentException | OnepRemoteProcedureException | OnepConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addVRFtoContainer(String elementVRFName, String niElementName){
		VRFContainer vrf = new VRFContainer(elementVRFName);
		if(vrfContainerList.isEmpty() || !vrfContainerList.contains(vrf))
		{
			vrf.addInterface(niElementName);
			vrfContainerList.add(vrf);
		}
		else {
			for(VRFContainer v : vrfContainerList){
				if(v.equals(vrf))
					v.addInterface(niElementName);
			}
		}
	}
	
	public void evangelizeFRU(NetworkElement ne){
		List<FRU> fruList = null;

		try {
			System.out.println("\nField Replaceable Units on " + ne.getAddress() + ":");
			
			fruList = ne.getFRUList();
			if(fruList.isEmpty()){
				System.out.println("No FRUs found.");
			}
			else {
				for(FRU fruI : fruList){
					FRU fruElement = fruI;
					System.out.println(fruElement);
				}		
			}
		} catch (OnepRemoteProcedureException | OnepConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void evangelizeSystemInfo(NetworkElement ne){
		System.out.println("\nSystem information for " + ne.getAddress() + ":");
		try {
			System.out.println("Free memory: " + ne.getFreeSystemMemory());
			System.out.println("Total memory: " + ne.getTotalSystemMemory());
			System.out.println("CPU Utilization: " + ne.getSystemCPUUtilization() +"%");
		} catch (OnepException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ElementLicense licElement = ne.getLicensing();
		System.out.println("PID:                      " + licElement.getPID());
		System.out.println("Serial Number :           " + licElement.getSN());
		System.out.println("Unique Device Identifier: " + licElement.getUDI());
		System.out.println("Version ID:               " + licElement.getVID());
	}
	
	public void evangelizePolicy(NetworkElement ne){
		List<Policy> policyList = null;

		System.out.println("\nPolicy information for " + ne.getAddress() + ":");
		
		policyList = ne.getPolicyList();
		if(policyList.isEmpty()){
			System.out.println("No policies found.");
		}
		else{
			for(Policy polI : policyList){
				Policy policyElement = polI;
				System.out.println(policyElement);
			}
		}
	}
	
	public void evangelizeProcess(NetworkElement ne){
		List<ElementProcess> proList = null;

		System.out.println("\nProcess information for " + ne.getAddress() + ":");
		
		try {
			proList = ne.getProcessList();
			if(proList.isEmpty()){
				System.out.println("No processes found.");
			}
			else{
				for(ElementProcess proI : proList){
					ElementProcess proElement = proI;
					System.out.println("\nProcess " + proElement.getProcessName() + "(" + proElement.getProcessID() + ")\n" +
					           "\tMem Allocated: " + proElement.getAllocatedMemory() + "\n" +
					           "\tMem Used     : " + proElement.getUsedMemory() + "\n" +
					           "\tMem Freed    : " + proElement.getFreedMemory() + "\n" +
							   "\tCPU: " + proElement.getCPUUtilization() + "%");
				}
				System.out.println("\nTotal processes found: " + proList.size());
			}
		} catch (OnepException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void evangelizeVRF(NetworkElement ne){
		System.out.println("\nListing VRFs found on all interfaces:");
		for(VRFContainer v : vrfContainerList){
			v.evangelizeVRF();
        	RoutingExplorer rtExp = new RoutingExplorer();
        	if(!v.getVRFName().equals("empty"))
        		rtExp.evangelizeVRFRoutes(ne, v.getVRFName());
        }
	}
	
} // end of class


