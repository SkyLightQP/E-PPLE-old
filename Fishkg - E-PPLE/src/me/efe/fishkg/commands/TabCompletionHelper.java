package me.efe.fishkg.commands;

import java.util.ArrayList;
import java.util.List;

public class TabCompletionHelper {
	
	public static List<String> getPossibleCompletions(String[] args, String... completions) {
		String arg = args[args.length - 1];
		
		List<String> list = new ArrayList<String>();
		
		for (String string : completions) {
			if (string.startsWith(arg)) {
				list.add(string);
			}
		}
		
		return list;
	}
	
	public static List<String> getPossibleCompletions(String[] args, List<String> completions) {
		String arg = args[args.length - 1];
		
		List<String> list = new ArrayList<String>();
		
		for (String string : completions) {
			if (string.startsWith(arg)) {
				list.add(string);
			}
		}
		
		return list;
	}
}