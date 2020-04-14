package spinnery.widget.api;

/**
 * Generic interface providing standard event hook methods.
 */
public interface WEventListener {
	/**
	 * Event dispatched when a keyboard key is pressed.
	 *
	 * @param keyCode     Keycode associated with pressed key.
	 * @param character   Character associated with pressed key.
	 * @param keyModifier Modifier(s) associated with pressed key.
	 */
	void onKeyPressed(int keyCode, int character, int keyModifier);

	/**
	 * Event dispatched when a keyboard key is released.
	 *
	 * @param keyCode     Keycode associated with pressed key.
	 * @param character   Character associated with pressed key.
	 * @param keyModifier Modifier(s) associated with pressed key.
	 */
	void onKeyReleased(int keyCode, int character, int keyModifier);

	/**
	 * Event dispatched when a key with a valid associated character is called.
	 *
	 * @param character Character associated with key pressed.
	 * @param keyCode   Keycode associated with key pressed.
	 */
	void onCharTyped(char character, int keyCode);

	/**
	 * Event dispatched when a widget gains focus.
	 */
	void onFocusGained();

	/**
	 * Event dispatched when a widget loses focus.
	 */
	void onFocusReleased();

	/**
	 * Event dispatched when the mouse is released.
	 *
	 * @param mouseX      Horizontal position of mouse cursor.
	 * @param mouseY      Vertical position of mouse cursor.
	 * @param mouseButton Mouse button released.
	 */
	void onMouseReleased(int mouseX, int mouseY, int mouseButton);

	/**
	 * Event dispatched when the mouse is clicked.
	 *
	 * @param mouseX      Horizontal position of mouse cursor.
	 * @param mouseY      Vertical position of mouse cursor.
	 * @param mouseButton Mouse button clicked.
	 */
	void onMouseClicked(int mouseX, int mouseY, int mouseButton);

	/**
	 * Event dispatched when the mouse is dragged.
	 *
	 * @param mouseX      Horizontal position of mouse cursor.
	 * @param mouseY      Vertical position of mouse cursor.
	 * @param mouseButton Mouse button dragged.
	 * @param deltaX      Horizontal delta of mouse drag.
	 * @param deltaY      Vertical delta of mouse drag.
	 */
	void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY);

	/**
	 * Event dispatched when the mouse is moved.
	 *
	 * @param mouseX Horizontal position of mouse cursor.
	 * @param mouseY Vertical position of mouse cursor.
	 */
	void onMouseMoved(int mouseX, int mouseY);

	/**
	 * Event dispatched when the mouse wheel is scrolled.
	 *
	 * @param mouseX Horizontal position of the mouse cursor.
	 * @param mouseY Vertical position of the mouse cursor.
	 * @param deltaY Vertical delta of mouse scroll.
	 */
	void onMouseScrolled(int mouseX, int mouseY, double deltaY);

	/**
	 * Event dispatched when a tooltip should be drawn, however, currently not implemented.
	 *
	 * @param mouseX Horizontal position of mouse cursor.
	 * @param mouseY Vertical position of mouse cursor.
	 */
	void onDrawTooltip(int mouseX, int mouseY);

	/**
	 * Event dispatched on game GUI alignment, e.g. when the game window is resized.
	 */
	void onAlign();
}
