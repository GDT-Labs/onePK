package onePKTest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import com.cisco.onep.core.exception.OnepConnectionException;
import com.cisco.onep.core.exception.OnepIllegalArgumentException;
import com.cisco.onep.core.exception.OnepRemoteProcedureException;
import com.cisco.onep.element.NetworkElement;
import com.cisco.onep.interfaces.NetworkPrefix;
import com.cisco.onep.routing.*;
import com.cisco.onep.routing.L3UnicastScope.AFIType;
import com.cisco.onep.routing.L3UnicastScope.SAFIType;
import com.cisco.onep.routing.RouteRange.RangeType;


public class RoutingExplorer {

	List<Route> queriedRoutes = null;
	L3UnicastScope scope = null;
	L3UnicastRIBFilter filter = null;
	L3UnicastRouteRange range = null;
	RIB rib = null;
	Routing routeService = null;
	NetworkPrefix networkPrefix = null;
	
	public void evangelizeVRFRoutes(NetworkElement ne, String vrfName){
		
		try {
			routeService = Routing.getInstance(ne);
			rib = routeService.getRib();
			
			scope = new L3UnicastScope(vrfName.equals("default")?"":vrfName,AFIType.IPV4,SAFIType.UNICAST,"base");
			filter = new L3UnicastRIBFilter();
			// Network Prefix address is the base address to start from
			// the Prefix size determines the mask to apply on the range to filter
			// it's the opposite of / notation, starting from the left i.e.
			// 8 =  11111111 00000000 00000000 00000000
			// 16 = 11111111 11111111 00000000 00000000
			// etc.
			// so anything 0'd by the prefix size in the address will be ignored
	        networkPrefix = new NetworkPrefix(InetAddress.getByName("0.0.0.0"), 32);
			range = new L3UnicastRouteRange(networkPrefix,RangeType.EQUAL_OR_LARGER,0);
			
			System.out.println("\nRoutes belonging to VRF " + vrfName);
			try {
				queriedRoutes = rib.getRouteList(scope,filter,range);
				for(Route r : queriedRoutes){
					System.out.print(r);
					Set<L3UnicastNextHop> nextHop = ((L3UnicastRoute)r).getNextHopList();
					for(L3UnicastNextHop nxt : nextHop){
						System.out.println("\tNext Hop: " + nxt.getAddress());
					}
				}
			} catch (OnepConnectionException | OnepRemoteProcedureException | OnepIllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (OnepConnectionException | OnepIllegalArgumentException | UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
}
