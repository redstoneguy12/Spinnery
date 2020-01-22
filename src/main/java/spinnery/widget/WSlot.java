package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import spinnery.Spinnery;
import spinnery.client.BaseRenderer;
import spinnery.registry.NetworkRegistry;

import java.util.Map;

public class WSlot extends WWidget implements WClient, WServer {

	public static final int TOP_LEFT = 0;
	public static final int BOTTOM_RIGHT = 1;
	public static final int BACKGROUND_FOCUSED = 2;
	public static final int BACKGROUND_UNFOCUSED = 3;
	protected int slotNumber;
	protected ItemStack previewStack = ItemStack.EMPTY;
	protected int inventoryNumber;
	protected boolean ignoreOnRelease = false;

	public WSlot(WAnchor anchor, int positionX, int positionY, int positionZ, int sizeX, int sizeY, int slotNumber, int inventoryNumber, WInterface linkedPanel) {
		setInterface(linkedPanel);

		setAnchor(anchor);

		setAnchoredPositionX(positionX);
		setAnchoredPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setTheme("default");

		setSlotNumber(slotNumber);
		setInventoryNumber(inventoryNumber);
	}

	public static void addSingle(WAnchor anchor, int positionX, int positionY, int positionZ, int sizeX, int sizeY, int slotNumber, int inventoryNumber, WInterface linkedPanel) {
		linkedPanel.add(new WSlot(anchor, positionX, positionY, positionZ, sizeX, sizeY, slotNumber, inventoryNumber, linkedPanel));
	}

	public static void addArray(WAnchor anchor, int arrayX, int arrayY, int positionX, int positionY, int positionZ, int sizeX, int sizeY, int slotNumber, int inventoryNumber, WInterface linkedPanel) {
		for (int y = 0; y < arrayY; ++y) {
			for (int x = 0; x < arrayX; ++x) {
				WSlot.addSingle(anchor, positionX + (sizeX * x), positionY + (sizeY * y), positionZ, sizeX, sizeY, slotNumber++, inventoryNumber, linkedPanel);
			}
		}
	}

	public static void addPlayerInventory(int positionZ, int sizeX, int sizeY, int inventoryNumber, WInterface linkedPanel) {
		int temporarySlotNumber = 0;
		addArray(
				WAnchor.MC_ORIGIN,
				9,
				1,
				4,
				linkedPanel.getSizeY() - 18 - 4,
				positionZ,
				sizeX,
				sizeY,
				temporarySlotNumber,
				inventoryNumber,
				linkedPanel);
		temporarySlotNumber = 9;
		addArray(
				WAnchor.MC_ORIGIN,
				9,
				3,
				4,
				linkedPanel.getSizeY() - 72 - 6,
				positionZ,
				sizeX,
				sizeY,
				temporarySlotNumber,
				inventoryNumber,
				linkedPanel);
	}

	public static WWidget.Theme of(Map<String, String> rawTheme) {
		WWidget.Theme theme = new WWidget.Theme();
		theme.add(TOP_LEFT, WColor.of(rawTheme.get("top_left")));
		theme.add(BOTTOM_RIGHT, WColor.of(rawTheme.get("bottom_right")));
		theme.add(BACKGROUND_FOCUSED, WColor.of(rawTheme.get("background_focused")));
		theme.add(BACKGROUND_UNFOCUSED, WColor.of(rawTheme.get("background_unfocused")));
		return theme;
	}

	public ItemStack getStack() {
		try {
			return getLinkedInventory().getInvStack(getSlotNumber());
		} catch (ArrayIndexOutOfBoundsException exception) {
			Spinnery.logger.log(Level.ERROR, "Cannot access slot " + getSlotNumber() + ", as it does exist in the inventory!");
			return ItemStack.EMPTY;
		}
	}

	public void setStack(ItemStack stack) {
		try {
			getLinkedInventory().setInvStack(getSlotNumber(), stack);
		} catch (ArrayIndexOutOfBoundsException exception) {
			Spinnery.logger.log(Level.ERROR, "Cannot access slot " + getSlotNumber() + ", as it does exist in the inventory!");
		}
	}

	public ItemStack getPreviewStack() {
		return previewStack;
	}

	public void setPreviewStack(ItemStack previewStack) {
		this.previewStack = previewStack;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}

	public int getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(int inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public Inventory getLinkedInventory() {
		return getInterface().getContainer().getInventories().get(inventoryNumber);
	}

	@Override
	public void onMouseReleased(double mouseX, double mouseY, int mouseButton) {
		if (getFocus()) {
			PlayerEntity playerEntity = getInterface().getContainer().getLinkedPlayerInventory().player;

			if (!ignoreOnRelease && mouseButton == 0 && !Screen.hasShiftDown() && !playerEntity.inventory.getCursorStack().isEmpty()) {
				getInterface().getContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.PICKUP, playerEntity);
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.PICKUP));
			} else if (!ignoreOnRelease && mouseButton == 1 && !Screen.hasShiftDown() && !playerEntity.inventory.getCursorStack().isEmpty()) {
				getInterface().getContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 1, SlotActionType.PICKUP, playerEntity);
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 1, SlotActionType.PICKUP));
			}

			ignoreOnRelease = false;
		}

		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (getFocus()) {
			PlayerEntity playerEntity = getInterface().getContainer().getLinkedPlayerInventory().player;

			if (mouseButton == 0 && Screen.hasShiftDown()) {
				getInterface().getContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.QUICK_MOVE, playerEntity);
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.QUICK_MOVE));
			} else if (mouseButton == 0 && !Screen.hasShiftDown() && playerEntity.inventory.getCursorStack().isEmpty()) {
				ignoreOnRelease = true;
				getInterface().getContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.PICKUP, playerEntity);
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.PICKUP));
			} else if (mouseButton == 1 && !Screen.hasShiftDown() && playerEntity.inventory.getCursorStack().isEmpty()) {
				ignoreOnRelease = true;
				getInterface().getContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 1, SlotActionType.PICKUP, playerEntity);
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 1, SlotActionType.PICKUP));
			} else if (mouseButton == 2) {
				getInterface().getContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 2, SlotActionType.CLONE, playerEntity);
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 2, SlotActionType.CLONE));
			}
		}
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
		if (getFocus() && Screen.hasShiftDown() && mouseButton == 0) {
			PlayerEntity playerEntity = getInterface().getContainer().getLinkedPlayerInventory().player;

			getInterface().getContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), mouseButton, SlotActionType.QUICK_MOVE, MinecraftClient.getInstance().player);
			ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), mouseButton, SlotActionType.QUICK_MOVE));
		}

		super.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
	}

	@Override
	public void setTheme(String theme) {
		if (getInterface().isClient()) {
			super.setTheme(theme);
		}
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		int x = getPositionX();
		int y = getPositionY();
		int z = getPositionZ();

		int sX = getSizeX();
		int sY = getSizeY();

		BaseRenderer.drawBeveledPanel(x, y, z, sX, sY, getColor(TOP_LEFT), getFocus() ? getColor(BACKGROUND_FOCUSED) : getColor(BACKGROUND_UNFOCUSED), getColor(BOTTOM_RIGHT));

		RenderSystem.enableLighting();

		BaseRenderer.getItemRenderer().renderGuiItem(getPreviewStack().isEmpty() ? getStack() : getPreviewStack(), 1 + x + (sX - 18) / 2, 1 + y + (sY - 18) / 2);
		BaseRenderer.getItemRenderer().renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer, getPreviewStack().isEmpty() ? getStack() : getPreviewStack(), 1 + x + (sX - 18) / 2, 1 + y + (sY - 18) / 2);

		RenderSystem.disableLighting();
	}
}
