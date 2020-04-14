package spinnery.registry;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import spinnery.Spinnery;
import spinnery.util.ResourceListener;
import spinnery.widget.api.Theme;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * Registers all the resource-related
 * assortments Spinnery makes use of,
 * including our theme parser and
 * reload listeners.
 */
@Environment(EnvType.CLIENT)
public class ResourceRegistry {
	public static final ResourceListener RESOURCE_LISTENER = new ResourceListener();

	public static void initialize() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(RESOURCE_LISTENER);
	}

	public static void clear() {
		ThemeRegistry.clear();
	}

	public static void load(ResourceManager resourceManager) {
		Collection<Identifier> themeFiles = resourceManager.findResources("spinnery",
				(string) -> string.endsWith(".theme.json5"));

		for (Identifier id : themeFiles) {
			try {
				Identifier themeId = new Identifier(id.getNamespace(),
						id.getPath().replaceFirst("^spinnery/", "").replaceFirst("\\.theme\\.json5", ""));
				register(themeId, resourceManager.getResource(id).getInputStream());
			} catch (IOException e) {
				Spinnery.LOGGER.warn("[Spinnery] Failed to load theme {}.", id);
			}
		}
	}

	public static void register(Identifier id, InputStream inputStream) {
		try {
			JsonObject themeDef = Jankson.builder().build().load(inputStream);
			Theme theme = Theme.of(id, themeDef);
			ThemeRegistry.register(theme);
		} catch (IOException e) {
			Spinnery.LOGGER.log(Level.ERROR, "Could not read theme file", e);
		} catch (SyntaxError syntaxError) {
			Spinnery.LOGGER.log(Level.ERROR, "Syntax error in theme file", syntaxError);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				Spinnery.LOGGER.log(Level.ERROR, "Could not close input stream", e);
			}
		}
	}
}
