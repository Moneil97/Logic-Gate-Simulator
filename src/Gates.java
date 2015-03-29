public enum Gates {
	AND, OR, NOT, XOR;
	
	public static Gates getValue(EComponent c){
		if (c instanceof AND) return AND;
		if (c instanceof OR) return OR;
		if (c instanceof NOT) return NOT;
		if (c instanceof XOR) return XOR;
		return null;
	}
}
