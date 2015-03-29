public enum Ratios {

	AND_GATE_BOUNDS_X_RATIOS(new float[] { 0.17417417f, 0.17492492f, 0.5788288f, 0.6651652f, 0.734985f, 0.783033f, 0.8175676f, 0.8220721f,
			0.8243243f, 0.8078078f, 0.774024f, 0.7222222f, 0.6531532f, 0.5735736f }),

	AND_GATE_BOUNDS_Y_RATIOS(new float[] { 0.0f, 0.99749684f, 0.99499375f, 0.9586984f, 0.8760951f, 0.7684606f, 0.62578225f, 0.5206508f,
			0.4580726f, 0.3279099f, 0.20650813f, 0.10888611f, 0.03128911f, 0.0f }),

	AND_GATE_TOP_INPUT_X_RATIOS(new float[] { 0.0f, 0.18f, 0.18f, 0.0f }),

	AND_GATE_TOP_INPUT_Y_RATIOS(new float[] { 0.13f, 0.13f, 0.4f, 0.4f }),

	AND_GATE_BOTTOM_INPUT_X_RATIOS(new float[] { 0.18f, 0.0f, 0.0f, 0.18f }),

	AND_GATE_BOTTOM_INPUT_Y_RATIOS(new float[] { 0.85f, 0.85f, 0.6f, 0.6f }),

	AND_GATE_OUTPUT_X_RATIOS(new float[] { 0.8f, 1.0f, 1.0f, 0.8f }),

	AND_GATE_OUTPUT_Y_RATIOS(new float[] { 0.33f, 0.33f, 0.64f, 0.64f });

	private float[] ratio;

	Ratios(float[] f) {
		ratio = f;
	}

	float[] getRatio() {
		return ratio;
	}

}

enum RatioGroups {

	AND_GATE_HOVER_RATIOS(new float[][] { Ratios.AND_GATE_TOP_INPUT_X_RATIOS.getRatio(), Ratios.AND_GATE_TOP_INPUT_Y_RATIOS.getRatio(),
			Ratios.AND_GATE_BOTTOM_INPUT_X_RATIOS.getRatio(), Ratios.AND_GATE_BOTTOM_INPUT_Y_RATIOS.getRatio(),
			Ratios.AND_GATE_OUTPUT_X_RATIOS.getRatio(), Ratios.AND_GATE_OUTPUT_Y_RATIOS.getRatio() }),

	AND_GATE_BOUNDS_RATIOS(new float[][] { Ratios.AND_GATE_BOUNDS_X_RATIOS.getRatio(), Ratios.AND_GATE_BOUNDS_Y_RATIOS.getRatio() });

	private float[][] ratioGroup;

	RatioGroups(float[][] f) {
		ratioGroup = f;
	}

	float[][] getRatioGroup() {
		return ratioGroup;
	}

}