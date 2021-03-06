package spinnery.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import spinnery.widget.api.Color;
import spinnery.widget.api.Position;

public class TextRenderer {
	public static RenderPass pass() {
		return new RenderPass();
	}

	public static int height() {
		return height(Font.DEFAULT);
	}

	public static int height(Font font) {
		return getTextRenderer(font).fontHeight;
	}

	public static net.minecraft.client.font.TextRenderer getTextRenderer(Font font) {
		switch (font) {
			case ENCHANTMENT:
				return MinecraftClient.getInstance().getFontManager()
						.getTextRenderer(MinecraftClient.ALT_TEXT_RENDERER_ID);
			case DEFAULT:
			default:
				return MinecraftClient.getInstance().textRenderer;
		}
	}

	public static int width(char character) {
		return width(character, Font.DEFAULT);
	}

	public static int width(char character, Font font) {
		return (int) getTextRenderer(font).getCharWidth(character);
	}

	public static int width(String string) {
		return width(string, Font.DEFAULT);
	}

	public static int width(String string, Font font) {
		return getTextRenderer(font).getStringWidth(string);
	}

	public static int width(Text text, Font font) {
		return width(text.asFormattedString(), font);
	}

	public static int width(Text text) {
		return width(text.asFormattedString(), Font.DEFAULT);
	}

	public enum Font {
		DEFAULT,
		ENCHANTMENT,
	}

	public static class RenderPass {
		private String text;
		private String shadowText;
		private int x;
		private int y;
		private int z;
		private int color = 0xffffffff;
		private int shadowColor = 0xff3e3e3e;
		private double scale = 1.0;
		private boolean shadow;
		private Integer maxWidth;
		private Font font = Font.DEFAULT;

		public RenderPass text(String text) {
			this.text = text;
			this.shadowText = text.replaceAll("§[0-9a-f]", "");
			return this;
		}

		public RenderPass text(char c) {
			this.text = String.valueOf(c);
			this.shadowText = text;
			return this;
		}

		public RenderPass text(Text text) {
			this.text = text.asFormattedString();
			this.shadowText = this.text.replaceAll("§[0-9a-f]", "");
			return this;
		}

		public RenderPass at(Position position) {
			return at(position.getX(), position.getY(), position.getZ());
		}

		public RenderPass at(Number x, Number y, Number z) {
			this.x = x.intValue();
			this.y = y.intValue();
			this.z = z.intValue();
			return this;
		}

		public RenderPass size(int size) {
			return scale(size / 9D);
		}

		public RenderPass scale(double scale) {
			this.scale = scale;
			return this;
		}

		public RenderPass color(Color color) {
			return color(color.ARGB);
		}

		public RenderPass color(int color) {
			this.color = color;
			return this;
		}

		public RenderPass shadow(boolean shadow) {
			this.shadow = shadow;
			return this;
		}

		public RenderPass shadowColor(Color color) {
			return shadowColor(color.ARGB);
		}

		public RenderPass shadowColor(int color) {
			this.shadowColor = color;
			return this;
		}

		public RenderPass maxWidth(Integer maxWidth) {
			this.maxWidth = maxWidth;
			return this;
		}

		public RenderPass font(Font font) {
			this.font = font;
			return this;
		}

		public void render() {
			float oX = x * (1f - (float) scale);
			float oY = y * (1f - (float) scale);
			RenderSystem.pushMatrix();
			RenderSystem.translatef(oX, oY, z);
			RenderSystem.scaled(scale, scale, 1);
			if (maxWidth != null) {
				if (shadow)
					getTextRenderer(font).drawTrimmed(shadowText, x + 1, y + 1, maxWidth, shadowColor);
				getTextRenderer(font).drawTrimmed(text, x, y, maxWidth, color);
			} else {
				if (shadow) getTextRenderer(font).draw(shadowText, x + 1, y + 1, shadowColor);
				getTextRenderer(font).draw(text, x, y, color);
			}
			RenderSystem.popMatrix();
		}
	}
}
