
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class InputParser {
	private List<String> expressions;
	private List<TokenType> tokens;
	
	public InputParser(String input) {
		expressions = new ArrayList<>();
		tokens = new ArrayList<>();
		
		expressions.add("("); expressions.add("+"); expressions.add("-");
		expressions.add(")"); expressions.add("*"); expressions.add("/");
		
		String temp = "";
		String str = "";
		TokenType tp;
		//Get rid of space
		for(int i=0; i < input.length(); i++) {
			if(input.charAt(i) != ' ')
				str += input.substring(i, i+1);
		}
		System.out.println(str); 
		
		for(int j=0; j<str.length(); j++) {
			if(expressions.contains(str.substring(j, j+1))) {
				if(!temp.isEmpty()) {
					tp = new TokenType(temp.trim());
					tokens.add(tp);
					temp = "";
				}
				tp = new TokenType(str.substring(j, j+1).trim());
				tokens.add(tp);
			}
			else {
				temp += str.substring(j, j+1);
			}
		}
		if(!temp.isEmpty()) {
			tp = new TokenType(temp.trim());
			tokens.add(tp);
		}
		tp = new TokenType("$");
		tokens.add(tp);
	}
	
	public List<TokenType> getTokens() {
		return tokens;
	}

	public void setTokens(List<TokenType> tokens) {
		this.tokens = tokens;
	}

	public void printTokens() {
		Iterator<TokenType> itr = tokens.iterator();
		System.out.println("Tokens has " + tokens.size() + " element(s)");
		while(itr.hasNext()) {
			TokenType see = itr.next();
			if(see.isNumber())
				System.out.print(see.getTokNum());
			else
				System.out.print(see.getTokStr());
		}
		System.out.println("\nList size is " + tokens.size());
	}

	public static void main(String[] args) {
		System.out.print("Enter an expression:  ");
		Scanner sc = new Scanner(System.in);
		String str = sc.nextLine();
		
		InputParser prs = new InputParser(str);
		prs.printTokens();
		sc.close();
	}

}
