package me.efe.efeserver.util;

import java.util.ArrayList;
import java.util.List;

public class TabCompletionHelper {
	
	public static List<String> getPossibleCompletions(String[] args, String... completions) {
		String arg = args[args.length - 1];
		
		List<String> list = new ArrayList<String>();
		
		for (String string : completions) {
			if (string.toLowerCase().startsWith(arg.toLowerCase())) {
				list.add(string);
			}
		}
		
		return list;
	}
	
	public static List<String> getPossibleCompletions(String[] args, List<String> completions) {
		String arg = args[args.length - 1];
		
		List<String> list = new ArrayList<String>();
		
		for (String string : completions) {
			if (string.toLowerCase().startsWith(arg.toLowerCase())) {
				list.add(string);
			}
		}
		
		return list;
	}
}