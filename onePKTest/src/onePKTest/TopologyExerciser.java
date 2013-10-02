package onePKTest;

import com.cisco.onep.core.exception.OnepConnectionException;
import com.cisco.onep.core.exception.OnepIllegalArgumentException;
import com.cisco.onep.core.exception.OnepRemoteProcedureException;
import com.cisco.onep.element.NetworkElement;
import com.cisco.onep.topology.Graph;
import com.cisco.onep.topology.Topology;
import com.cisco.onep.topology.Topology.TopologyType;

public class TopologyExerciser {
		NetworkElement netElem=null;
	
		public TopologyExerciser(NetworkElement ne){
			setNetworkElement(ne);
		}
		
		public void setNetworkElement(NetworkElement ne){
			netElem = ne;
		}
		
		public void evangelizeTopolgy(){
			if(netElem==null)
				System.out.println("\nNo network element provided for topology.");
			else {
				try {
					Topology topo = new Topology(netElem,TopologyType.CDP);
					Graph netGraph = topo.getGraph();
					System.out.println("\nThe topology graph for " + netElem.getAddress() + " is:");
					System.out.println(netGraph);
				} catch (OnepIllegalArgumentException | OnepConnectionException | OnepRemoteProcedureException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
}
