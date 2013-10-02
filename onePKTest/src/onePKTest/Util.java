package onePKTest;

import java.util.Scanner;

public class Util {

	private static Scanner scanner = new Scanner(System.in);

	public String getParams(String dispTxt, String defaultVal){
		String tmp = "";
		
		System.out.print(dispTxt + "(" + defaultVal + "): ");
		tmp=scanner.nextLine();
		if(!tmp.isEmpty())
			return tmp;
		else
			return defaultVal;
	}
}
