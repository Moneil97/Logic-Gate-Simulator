public enum Gates {
	AND, OR, NOT, XOR, SPLITTER, HUB, LCD, HALF_ADDER, FULL_ADDER;
	
	public static Gates getValue(EComponent c){
		if (c instanceof AND) return AND;
		if (c instanceof OR) return OR;
		if (c instanceof NOT) return NOT;
		if (c instanceof XOR) return XOR;
		if (c instanceof Hub) return HUB;
		return null;
	}
}

enum Sizes{
	
	small(.5f), medium(1), large(1.5f), custom(2);
	
	private float ratio;
	
	Sizes(float ratio){
		this.ratio = ratio;
	}

	public float getRatio() {
		return ratio;
	}
	
}
