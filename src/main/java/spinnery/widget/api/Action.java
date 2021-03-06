package spinnery.widget.api;

public enum Action {
	PICKUP,
	PICKUP_ALL,
	QUICK_MOVE,
	CLONE,
	DRAG_SPLIT,
	DRAG_SINGLE,
	DRAG_SPLIT_PREVIEW,
	DRAG_SINGLE_PREVIEW;

	public static Action of(int button, boolean mode) {
		switch (button) {
			case 0:
				return mode ? DRAG_SPLIT : DRAG_SPLIT_PREVIEW;
			case 1:
				return mode ? DRAG_SINGLE : DRAG_SINGLE_PREVIEW;
			default:
				return null;
		}
	}

	public boolean isPreview() {
		return this == DRAG_SINGLE_PREVIEW || this == DRAG_SPLIT_PREVIEW;
	}

	public boolean isSplit() {
		return this == DRAG_SPLIT || this == DRAG_SPLIT_PREVIEW;
	}

	public boolean isSingle() {
		return this == DRAG_SINGLE || this == DRAG_SINGLE_PREVIEW;
	}
}
