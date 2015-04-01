public enum Gates {
	AND, OR, NOT, XOR, SPLITTER, HUB;
	
	public static Gates getValue(EComponent c){
		if (c instanceof AND) return AND;
		if (c instanceof OR) return OR;
		if (c instanceof NOT) return NOT;
		if (c instanceof XOR) return XOR;
		if (c instanceof Hub) return HUB;
		return null;
	}
}
