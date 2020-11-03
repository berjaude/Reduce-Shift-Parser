
public class TokenType {
	private String terminal;
	private String tokStr;
	private int tokNum;
	private int state;

	public TokenType() {
		
	}
	
	public TokenType(String str) {
		try {
			int num = Integer.parseInt(str);
			this.tokNum = num;
		} catch (NumberFormatException e) {
			this.tokStr = str;
		}
	}

	public TokenType(String str, int st) {
		try {
			int num = Integer.parseInt(str);
			this.tokNum = num;
		} catch (NumberFormatException e) {
			this.tokStr = str;
		}

		this.state = st;
	}

	public String getTokStr() {
		return tokStr;
	}

	public void setTokStr(String tokStr) {
		this.tokStr = tokStr;
	}

	public int getTokNum() {
		return tokNum;
	}

	public void setTokNum(int tokNum) {
		this.tokNum = tokNum;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public boolean isNumber() {
		return this.getTokStr() == null;
	}
}
