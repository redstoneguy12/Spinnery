package spinnery.widget;

import com.google.common.collect.ImmutableSet;
import spinnery.client.BaseRenderer;
import spinnery.widget.api.*;

import java.util.*;

public class WVerticalBoxContainer extends WAbstractWidget implements WDrawableCollection, WModifiableCollection, WDelegatedEventListener {
	protected Set<WAbstractWidget> widgets = new HashSet<>();
	protected List<WLayoutElement> orderedWidgets = new ArrayList<>();

	public int topBottomPadding = 0;
	public boolean topBottomOverride = false;

	public int leftRightPadding = 0;
	public boolean leftRightOverride = false;

	public int intermediaryPadding = 0;
	public boolean intermediaryOverride = false;

	public int outerBorderWidth = 1;

	public boolean hasBorder;

	public <W extends WVerticalBoxContainer> W setBorder(boolean hasBorder) {
		this.hasBorder = hasBorder;
		return (W) this;
	}

	public boolean hasBorder() {
		return hasBorder;
	}

	public <W extends WVerticalBoxContainer> W setTopBottomPadding(int topBottomPadding) {
		this.topBottomPadding = topBottomPadding;
		this.topBottomOverride = true;
		return (W) this;
	}

	public int getTopBottomPadding() {
		return topBottomPadding;
	}

	public <W extends WVerticalBoxContainer> W clearTopBottomPadding() {
		topBottomPadding = 0;
		topBottomOverride = false;
		return (W) this;
	}

	public <W extends WVerticalBoxContainer> W setLeftRightPadding(int leftRightPadding) {
		this.leftRightPadding = leftRightPadding;
		this.leftRightOverride = true;
		return (W) this;
	}

	public int getLeftRightPadding() {
		return leftRightPadding;
	}

	public <W extends WVerticalBoxContainer> W clearLeftRightPadding() {
		leftRightPadding = 0;
		leftRightOverride = false;
		return (W) this;
	}

	public <W extends WVerticalBoxContainer> W setIntermediaryPadding(int intermediaryPadding) {
		this.intermediaryPadding = intermediaryPadding;
		this.intermediaryOverride = true;
		return (W) this;
	}

	public int getIntermediaryPadding() {
		return intermediaryPadding;
	}

	public <W extends WVerticalBoxContainer> W clearIntermediaryPadding() {
		intermediaryPadding = 0;
		intermediaryOverride = false;
		return (W) this;
	}

	public <W extends WVerticalBoxContainer> W setOuterBorderWidth(int outerBorderWidth) {
		this.outerBorderWidth = outerBorderWidth;
		return (W) this;
	}

	public int getOuterBorderWidth() {
		return outerBorderWidth;
	}

	@Override
	public Collection<? extends WEventListener> getEventDelegates() {
		Set<WAbstractWidget> delegates = new HashSet<>(widgets);
		return ImmutableSet.copyOf(delegates);
	}

	@Override
	public void recalculateCache() {
		orderedWidgets = new ArrayList<>(getWidgets());
		Collections.sort(orderedWidgets);
		Collections.reverse(orderedWidgets);
	}

	@Override
	public List<WLayoutElement> getOrderedWidgets() {
		return orderedWidgets;
	}

	@Override
	public void add(WAbstractWidget... widgets) {
		this.widgets.addAll(Arrays.asList(widgets));

		updateContents();
		recalculateCache();
	}

	@Override
	public void remove(WAbstractWidget... widgets) {
		this.widgets.removeAll(Arrays.asList(widgets));

		updateContents();
		recalculateCache();
	}

	@Override
	public Set<WAbstractWidget> getWidgets() {
		return widgets;
	}

	@Override
	public boolean contains(WAbstractWidget... widgets) {
		return this.widgets.containsAll(Arrays.asList(widgets));
	}

	public void updateContents() {
		if (!leftRightOverride) leftRightPadding = (int) ((0.1) * (float) getWidth());
		if (!topBottomOverride) topBottomPadding = (int) ((0.05) * (float) getHeight());
		if (!intermediaryOverride) intermediaryPadding = (int) ((0.025) * (float) getHeight());

		int totalWidgetSizeX = getWidth() - (2 * leftRightPadding);
		int totalWidgetSizeY = getHeight() - (2 * topBottomPadding) - ((this.widgets.size() > 1 ? this.widgets.size() - 1 : 0) * intermediaryPadding);

		int lastPositionY = topBottomPadding;
		int positionX = leftRightPadding;
		int widgetSizeX = totalWidgetSizeX;
		int widgetSizeY = totalWidgetSizeY / Math.max(this.widgets.size(), 1);

		for (WAbstractWidget widget : widgets) {
			Size newWidgetSize = Size.of(widgetSizeX, widgetSizeY);

			if (widget.getMaximumAutoSize().isSmallerInWidthOrHeight(newWidgetSize)) {
				newWidgetSize = widget.getMaximumAutoSize();
			}

			Position newWidgetPosition = Position.of(this, positionX, lastPositionY);

			widget.setPosition(newWidgetPosition);
			widget.setSize(newWidgetSize);
			lastPositionY += newWidgetSize.getHeight() + intermediaryPadding;
		}
	}

	@Override
	public void onLayoutChange() {
		super.onLayoutChange();
		updateContents();
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		BaseRenderer.enableCropping();

		for (WAbstractWidget widget : widgets) {
			BaseRenderer.crop(this);
			widget.draw();
		}

		BaseRenderer.crop(this);

		if (hasBorder()) {
			BaseRenderer.drawRectangle(getX(), getY(), getZ(), getWidth(), outerBorderWidth, getStyle().asColor("border"));
			BaseRenderer.drawRectangle(getX(), getY(), getZ(), outerBorderWidth, getHeight(), getStyle().asColor("border"));
			BaseRenderer.drawRectangle(getX(), getY() + getHeight() - 1, getZ(), getWidth(), outerBorderWidth, getStyle().asColor("border"));
			BaseRenderer.drawRectangle(getX() + getWidth() - 1, getY(), getZ(), outerBorderWidth, getHeight(), getStyle().asColor("border"));
		}

		BaseRenderer.disableCropping();
	}
}
