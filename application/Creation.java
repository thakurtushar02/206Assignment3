package application;

public class Creation {
	String _name;
	String _term;
	
	public Creation(String name, String term) {
		_name = name;
		_term = term;
	}
	
	public String getTerm() {
		return _term;
	}
	
	public String getName() {
		return _name;
	}
}
