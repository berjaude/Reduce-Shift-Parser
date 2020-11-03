
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class LR1 {
	// parser stack
	private Stack<TokenType> stack;
	// input queue
	private Queue<TokenType> queue;
	// most recent operator waiting to be executed : including +, -, *, /, and (
	private Stack<String> currentOp;

	public LR1() {
		/*
		 * Initialize relevant variables for the parser
		 */
		stack = new Stack<>();
		queue = new LinkedList<>();
		currentOp = new Stack<>();
	}

	public void parser(List<TokenType> tokens) {
		// set the input queue
		for (TokenType to : tokens) {
			queue.add(to);
		}

		/*
		 * Set the first token of the stack as 0 and its state as 0. The next token must
		 * be either a number or an open parenthesis '('. Start computation by calling
		 * rule 0
		 */
		stack.push(new TokenType("0", 0));
		printStack(); // prints out stack
		rule0();
	}

	/*
	 * Get the next token from the input queue. Next step: Go to rule 5 if it got a
	 * number Go to rule 4 if it got an ( Otherwise, throw an error
	 */
	private void rule0() {
		System.out.println("Rule 0");
		TokenType tk = queue.poll();
		try {
			if (tk.isNumber()) {
				tk.setTerminal("n");
				tk.setState(5);
				stack.push(tk);
				printStack(); // prints out stack
				rule5();
			} else if (tk.getTokStr().equals("(")) {
				tk.setState(4);
				currentOp.add(tk.getTokStr()); // update waiting operator stack
				stack.push(tk);
				printStack(); // prints out stack
				rule4();
			}
		} catch (NumberFormatException e) {
			System.out.println("Wrong token! at 0");
		}
	}

	/*
	 * Reads the next token in the input queue Next step: Go to rule 6 if it reads a
	 * + or - Successfully end the parser if it reads $ Otherwise, throws an error
	 */
	private void rule1() {
		System.out.println("Rule 1");
		switch (queue.peek().getTokStr()) {
		case "+":
		case "-":
			rule6();
			break;
		case "$":
			if (currentOp.isEmpty()) { //safe guard condition
				System.out.println("valid Expression, value = " + stack.peek().getTokNum());
				//System.exit(0);
			}
			break;
		default:
			System.err.println("Wrong token! at 1");
			System.exit(0);
		}
	}

	/*
	 * Reads the next token in the input queue Condition: Only reads +, - or $ Next
	 * step: Go to rule 1 if there is no parenthesis Go to rule 8 if there is a
	 * precedent ')'
	 */
	private void rule2() {
		System.out.println("Rule 2");
		switch (queue.peek().getTokStr()) {
		case "+":
		case "-":
			stack.peek().setTerminal("E");
			stack.peek().setState(1);
			printStack(); // prints out stack
			rule1();
		case "$":
			if (!currentOp.isEmpty() && currentOp.peek().equals("(")) {
				stack.peek().setTerminal("E");
				stack.peek().setState(8);
				printStack(); // prints out stack
				rule8();
			} else {
				stack.peek().setTerminal("E");
				stack.peek().setState(1);
				printStack(); // prints out stack
				rule1();
			}
			break;
		case ")":
			stack.peek().setTerminal("E");
			stack.peek().setState(8);
			printStack(); // prints out stack
			rule8();
			break;
		}
	}

	/*
	 * Reads the next token in the input queue Next step: Go to rule 9 if it reads +
	 * or - and the waiting operator is + or - if it reads $ and the waiting
	 * operator is + or - Go to rule 2 if it reads + or - and there is no waiting
	 * operator if reads $ and there is no waiting operator Go to rule 7 if it reads
	 * * or / Otherwise, throws an error
	 */
	private void rule3() {
		System.out.println("Rule 3");
		switch (queue.peek().getTokStr()) {
		case "+":
		case "-":
			stack.peek().setTerminal("T");
			if (!currentOp.isEmpty() && (currentOp.peek().equals("+") || currentOp.peek().equals("-"))) {
				stack.peek().setState(9);
				printStack(); // prints out stack
				rule9();
			} else {
				stack.peek().setState(2);
				printStack(); // prints out stack
				rule2();
			}
			break;
		case "*":
		case "/":
			stack.peek().setTerminal("T");
			stack.peek().setState(9);
			rule7();
			break;
		case ")": // FIGURE OUT THIS PIECE OF CODE FOR )
			stack.peek().setTerminal("T");
			if (!currentOp.isEmpty() && (currentOp.peek().equals("+") || currentOp.peek().equals("-"))) {
				stack.peek().setState(9);
				printStack(); // prints out stack
				rule9();
			} else {
				stack.peek().setState(2);
				printStack(); // prints out stack
				rule2();
			}
			break;
		case "$":
			stack.peek().setTerminal("T");
			if (!currentOp.isEmpty() && (currentOp.peek().equals("+") || currentOp.peek().equals("-"))) {
				stack.peek().setState(9);
				printStack(); // prints out stack
				rule9();
			} else {
				stack.peek().setState(2);
				printStack(); // prints out stack
				rule2();
			}
			break;
		default:
			System.err.println("Wrong token! at 3");
			System.exit(0);
		}
	}

	private void rule4() {
		System.out.println("Rule 4");
		rule0();
	}

	/*
	 * Reads the next token in the input queue Next step: Go to rule 3 if it reads
	 * +, -, *, /, ) or $ and the waiting operator isn't * or / Go to rule 10 if the
	 * waiting operator is * or / Otherwise, throws an error
	 */
	private void rule5() {
		System.out.println("Rule 5");
		switch (queue.peek().getTokStr()) {
		case "+":
		case "-":
			if (!currentOp.isEmpty() && (currentOp.peek().equals("*") || currentOp.peek().equals("/"))) {
				stack.peek().setState(10);
				printStack(); // prints out stack
				rule10();
			} else if (!currentOp.isEmpty() && (currentOp.peek().equals("("))) {
				stack.peek().setState(3);
				printStack(); // prints out stack
				rule3();
			} else {
				stack.peek().setState(3);
				printStack(); // prints out stack
				rule3();
			}
			break;
		case "$":
		case "*":
		case "/":
			stack.peek().setTerminal("F");
			if (!currentOp.isEmpty() && (currentOp.peek().equals("*") || currentOp.peek().equals("/"))) {
				stack.peek().setState(10);
				printStack(); // prints out stack
				rule10();
			} else if (!currentOp.isEmpty() && (currentOp.peek().equals("("))) {
				stack.peek().setState(3);
				printStack(); // prints out stack
				rule3();
			} else {
				stack.peek().setState(3);
				printStack(); // prints out stack
				rule3();
			}
			break;
		case ")":
			stack.peek().setTerminal("F");
			if (!currentOp.isEmpty() && (currentOp.peek().equals("*") || currentOp.peek().equals("/"))) {
				stack.peek().setState(10);
				printStack(); // prints out stack
				rule10();
			} else if (!currentOp.isEmpty() && (currentOp.peek().equals("("))) {
				stack.peek().setState(3);
				printStack(); // prints out stack
				rule3();
			} else {
				stack.peek().setState(3);
				printStack(); // prints out stack
				rule3();
			}
			break;
		default:
			System.err.println("Wrong token! at 5");
			System.exit(0);
		}
	}

	/*
	 * Get the next token from the input queue Condition: the token must be a + or -
	 * else: throws an error 
	 * Next step: Go back to rule 0 to get the next token
	 */
	private void rule6() {
		System.out.println("Rule 6");
		TokenType tk = queue.poll();
		try {
			if (tk.getTokStr().equals("+") || tk.getTokStr().equals("-")) {
				currentOp.push(tk.getTokStr()); // update the waiting operator
				tk.setState(6);
				stack.push(tk);
				printStack(); // prints out stack
				rule0();
			}
		} catch (NumberFormatException e) {
			System.out.println("Wrong token! at 6");
		}
	}

	/*
	 * Get the next token from the input queue Condition: the token must be a * or /
	 * else: throws an error 
	 * Next step: Go back to rule 0 to get the next token
	 */
	private void rule7() {
		System.out.println("Rule 7");
		TokenType tk = queue.poll();
		try {
			if (tk.getTokStr().equals("*") || tk.getTokStr().equals("/")) {
				currentOp.push(tk.getTokStr()); // update the waiting operator
				tk.setState(7);
				stack.push(tk);
				printStack(); // prints out stack
				rule0();
			}
		} catch (NumberFormatException e) {
			System.out.println("Wrong token! at 7");
		}
	}

	/*
	 * Reads the next token in the input queue 
	 * Next step: Go to rule 6 if it reads + or -
	 *  		  Go to rule 11 if it reads )
	 */
	private void rule8() {
		System.out.println("Rule 8");
		switch (queue.peek().getTokStr()) {
		case "+":
		case "-":
			rule6();
			break;
		case ")":
			rule11();
			break;
		default:
			System.err.println("Wrong token! at 8");
			System.exit(0);
		}
	}

	/*
	 * Reads the next token in the input queue
	 * Performs addition or subtraction only if next token is not * or /
	 * Next step: Go to rule 1 if the next token is not * or /
	 * 			  Go to rule 7 if the next token is * or /
	 * Otherwise, throws an error
	 */
	private void rule9() {
		System.out.println("Rule 9");
		switch (queue.peek().getTokStr()) {
		case "+":
		case "-":
			int arg1 = stack.pop().getTokNum();
			String op = stack.pop().getTokStr();
			int arg2 = stack.pop().getTokNum();

			if (!currentOp.isEmpty())
				currentOp.pop(); // remove used operator

			TokenType newVal = new TokenType();
			newVal.setTokNum(operate(arg2, arg1, op));
			newVal.setTerminal("E");
			newVal.setState(1);
			stack.push(newVal); // update parser stack
			printStack(); // prints out stack
			rule1(); // goes back to rule 1
			break;
		case "*":
		case "/":
			rule7(); // goes to rule7
			break;
		case ")":
		case "$":
			arg1 = stack.pop().getTokNum();
			op = stack.pop().getTokStr();
			arg2 = stack.pop().getTokNum();

			if (!currentOp.isEmpty())
				currentOp.pop(); // remove used operator

			newVal = new TokenType();
			newVal.setTokNum(operate(arg2, arg1, op));
			newVal.setTerminal("E");
			if (!currentOp.isEmpty() && (currentOp.peek().equals("("))) {
				newVal.setState(8);
				stack.push(newVal); // update parser stack
				printStack(); // prints out stack
				rule8(); // goes back to rule 8
			} else if (!currentOp.isEmpty() && (currentOp.peek().equals("+") || currentOp.peek().equals("-"))) {
				newVal.setState(1);
				stack.push(newVal); // update parser stack
				printStack(); // prints out stack
				rule1(); // goes back to rule 1
			} else {
				newVal.setState(1);
				stack.push(newVal); // update parser stack
				printStack(); // prints out stack
				rule1(); // goes back to rule 1
			}
			break;
		default:
			System.err.println("Wrong token! at 9");
			System.exit(0);
		}
	}

	/*
	 * Reads the next token in the input queue 
	 * Performs multiplication and division despite the precedence of the next token
	 * Next step: Go to rule 2 if it reads + or - if it reads $ and there is no waiting operator
	 * 			  Go to rule 7 if it reads * or /
	 * 		   	  Go to rule 9 if it reads $ and the waiting operator is + or -
	 * Otherwise, throws an error
	 */
	private void rule10() {
		System.out.println("Rule 10");
		int arg1 = stack.pop().getTokNum();
		String op = stack.pop().getTokStr();
		int arg2 = stack.pop().getTokNum();

		if (!currentOp.isEmpty())
			currentOp.pop(); // remove used operator

		TokenType newVal = new TokenType();
		newVal.setTokNum(operate(arg2, arg1, op));
		newVal.setTerminal("T");
		switch (queue.peek().getTokStr()) {
		case "+":
		case "-":
			newVal.setState(2);
			stack.push(newVal);
			printStack(); // prints out stack
			rule2(); // goes to 2
			break;
		case "*":
		case "/":
			newVal.setState(9);
			stack.push(newVal);
			printStack(); // prints out stack
			rule7(); // goes to 7
			break;
		case ")": // FIGURED OUT THIS PIECE OF CODE FOR )
		case "$":
			if (!currentOp.isEmpty() && (currentOp.peek().equals("("))) {
				newVal.setState(2);
				stack.push(newVal); // update parser stack
				printStack(); // prints out stack
				rule2(); // goes back to rule 2
			} else if (!currentOp.isEmpty() && (currentOp.peek().equals("+") || currentOp.peek().equals("-"))) {
				newVal.setState(9);
				stack.push(newVal); // update parser stack
				printStack(); // prints out stack
				rule9(); // goes back to rule 9
			} else {
				newVal.setState(2);
				stack.push(newVal); // update parser stack
				printStack(); // prints out stack
				rule2(); // goes back to rule 2
			}
			break;
		default:
			System.err.println("Wrong token! at 10");
			System.exit(0);
		}
	}

	/*
	 * Get the next token in the input queue 
	 * Condition: it must be a closing parenthesis ')'
	 * Get rid of the parenthesis combination 
	 * Reads the next token in the input queue 
	 * Next step: If it reads +, -, *, /, $, and )
	 * 			  and the waiting operator is * or / (Go to rule 10)
	 * 			  otherwise Go to rule 3 
	 */
	private void rule11() {
		System.out.println("Rule 11");
		TokenType tk = queue.poll();
		try {
			if (tk.getTokStr().equals(")")) {
				tk.setState(11);
				stack.push(tk);
				printStack(); // prints out stack

				// pop () in the parser stack
				stack.pop(); // pop )
				TokenType arg = stack.pop(); // pop )
				stack.pop(); // pop (
				stack.push(arg);
				// pop ( in waiting operator stack
				currentOp.pop();

				switch (queue.peek().getTokStr()) {
				case "+":
				case "-":
				case "*":
				case "/":
				case ")":
				case "$":
					if (!currentOp.isEmpty() && (currentOp.peek().equals("*") || currentOp.peek().equals("/"))) {
						stack.peek().setTerminal("F");
						stack.peek().setState(10);
						printStack(); // prints out stack
						rule10();
					} else {
						stack.peek().setTerminal("F");
						stack.peek().setState(3);
						printStack(); // prints out stack
						rule3();
					}
					break;
				}
			}
		} catch (NumberFormatException e) {
			System.out.println("Wrong token! at 11");
			System.exit(0);
		}
	}
	
	/*
	 * Prints out stack elements along with Terminals and state.
	 */
	private void printStack() {
		Iterator<TokenType> itr = stack.iterator();
		System.out.print("[");
		while (itr.hasNext()) {
			TokenType temp = itr.next();
			if (temp.getState() == 0)
				System.out.format("([]:%d) ", temp.getState());
			else if (temp.isNumber())
				System.out.format("(%s=%d:%d) ", temp.getTerminal(), temp.getTokNum(), temp.getState());
			else
				System.out.format("(%s:%d) ", temp.getTokStr(), temp.getState());
		}
		System.out.print("]\t");
		Iterator<TokenType> it = queue.iterator();
		System.out.print("[");
		while (it.hasNext()) {
			TokenType temp = it.next();
			if (temp.isNumber())
				System.out.print(temp.getTokNum() + " ");
			else
				System.out.print(temp.getTokStr() + " ");
		}
		System.out.print("]\n");
	}
	
	/*
	 * Performs specific operation given two operands
	 * Returns an integer
	 */
	private int operate(int arg1, int arg2, String op) {
		int ret = 0;
		switch (op) {
		case "+":
			ret = arg1 + arg2;
			break;
		case "-":
			ret = arg1 - arg2;
			break;
		case "*":
			ret = arg1 * arg2;
			break;
		case "/":
			ret = arg1 / arg2;
			break;
		}
		return ret;
	}

	public static void main(String[] args) {
		System.out.print("Enter an expression:  ");
		Scanner sc = new Scanner(System.in);
		String str = sc.nextLine();

		InputParser prs = new InputParser(str);

		List<TokenType> tokens = prs.getTokens();
		LR1 me = new LR1();
		me.parser(tokens);

		sc.close();
	}

}
