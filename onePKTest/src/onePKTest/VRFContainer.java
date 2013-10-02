package onePKTest;

import java.util.ArrayList;

public class VRFContainer {
	String sVRFName = null;
	ArrayList<String> interfaceList = null;
	
	public VRFContainer(String name){
		sVRFName = name;
		interfaceList = new ArrayList<String>();
	}
	
	@Override
	public boolean equals(Object b){
		boolean result =false;
		
			if(b != null && b instanceof VRFContainer){
				result = this.getVRFName().equals(((VRFContainer)b).getVRFName());
			}
			
		return result;
	}
	
	public void addInterface(String interfaceName)
	{
		if(interfaceList!=null)
			interfaceList.add(interfaceName);
	}
	
	public void evangelizeVRF(){
		System.out.println("\nVRF " + sVRFName + " is used by interfaces:");
		for(String i : interfaceList){
			System.out.println("\t" + i);
		}
	}
	
	public String getVRFName(){
		return sVRFName;
	}
}
