public enum Ratios {

	// AND
	AND_GATE_BOUNDS_X_RATIOS(new float[] { 0.17417417f, 0.17492492f, 0.5788288f, 0.6651652f, 0.734985f, 0.783033f, 0.8175676f, 0.8220721f,
			0.8243243f, 0.8078078f, 0.774024f, 0.7222222f, 0.6531532f, 0.5735736f }),

	AND_GATE_BOUNDS_Y_RATIOS(new float[] { 0.0f, 0.99749684f, 0.99499375f, 0.9586984f, 0.8760951f, 0.7684606f, 0.62578225f, 0.5206508f,
			0.4580726f, 0.3279099f, 0.20650813f, 0.10888611f, 0.03128911f, 0.0f }),

	AND_GATE_TOP_INPUT_X_RATIOS(new float[] { 0.0f, 0.18f, 0.18f, 0.0f }),

	AND_GATE_TOP_INPUT_Y_RATIOS(new float[] { 0.13f, 0.13f, 0.4f, 0.4f }),

	AND_GATE_BOTTOM_INPUT_X_RATIOS(new float[] { 0.0f, 0.18f, 0.18f, 0.0f }),

	AND_GATE_BOTTOM_INPUT_Y_RATIOS(new float[] { 0.6f, 0.6f, 0.85f, 0.85f }),

	AND_GATE_OUTPUT_X_RATIOS(new float[] { 0.8f, 1.0f, 1.0f, 0.8f }),

	AND_GATE_OUTPUT_Y_RATIOS(new float[] { 0.33f, 0.33f, 0.64f, 0.64f }),

	// OR
	OR_GATE_BOUNDS_X_RATIOS(new float[] { 0.12387097f, 0.12258065f, 0.15612903f, 0.18064517f, 0.18967742f, 0.18774194f, 0.17806451f,
			0.14903226f, 0.12516129f, 0.12387097f, 0.4180645f, 0.5232258f, 0.5929032f, 0.663871f, 0.73096776f, 0.7858065f, 0.82064515f,
			0.82f, 0.783871f, 0.7109677f, 0.6180645f, 0.52064514f, 0.43419355f }),

	OR_GATE_BOUNDS_Y_RATIOS(new float[] { 0.0012515645f, 0.02503129f, 0.1514393f, 0.29411766f, 0.4117647f, 0.61702126f, 0.7221527f,
			0.8785983f, 0.9687109f, 0.99499375f, 0.99499375f, 0.96370465f, 0.9123905f, 0.8385482f, 0.7371715f, 0.6245307f, 0.5181477f,
			0.47309136f, 0.3667084f, 0.21652065f, 0.10387985f, 0.027534418f, 0.0f }),

	OR_GATE_TOP_INPUT_X_RATIOS(new float[] { 0.0f, 0.17419355f, 0.17419355f, 6.451613E-4f }),

	OR_GATE_TOP_INPUT_Y_RATIOS(new float[] { 0.16020025f, 0.15894869f, 0.35043806f, 0.3516896f }),

	OR_GATE_BOTTOM_INPUT_X_RATIOS(new float[] { -6.451613E-4f, 0.1748387f, 0.17419355f, -6.451613E-4f }),

	OR_GATE_BOTTOM_INPUT_Y_RATIOS(new float[] { 0.64956194f, 0.64956194f, 0.8372966f, 0.836045f }),

	OR_GATE_OUTPUT_X_RATIOS(new float[] { 0.8245161f, 0.99935484f, 0.9987097f, 0.8232258f}),

	OR_GATE_OUTPUT_Y_RATIOS(new float[] { 0.4330413f, 0.4330413f, 0.5694618f, 0.5694618f});

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

	AND_GATE_BOUNDS_RATIOS(new float[][] { Ratios.AND_GATE_BOUNDS_X_RATIOS.getRatio(), Ratios.AND_GATE_BOUNDS_Y_RATIOS.getRatio() }),

	OR_GATE_HOVER_RATIOS(new float[][] { Ratios.OR_GATE_TOP_INPUT_X_RATIOS.getRatio(), Ratios.OR_GATE_TOP_INPUT_Y_RATIOS.getRatio(),
			Ratios.OR_GATE_BOTTOM_INPUT_X_RATIOS.getRatio(), Ratios.OR_GATE_BOTTOM_INPUT_Y_RATIOS.getRatio(),
			Ratios.OR_GATE_OUTPUT_X_RATIOS.getRatio(), Ratios.OR_GATE_OUTPUT_Y_RATIOS.getRatio() }),

	OR_GATE_BOUNDS_RATIOS(new float[][] { Ratios.OR_GATE_BOUNDS_X_RATIOS.getRatio(), Ratios.OR_GATE_BOUNDS_Y_RATIOS.getRatio() });

	private float[][] ratioGroup;

	RatioGroups(float[][] f) {
		ratioGroup = f;
	}

	float[][] getRatioGroup() {
		return ratioGroup;
	}

}