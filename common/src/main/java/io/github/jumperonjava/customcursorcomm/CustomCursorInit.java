package io.github.jumperonjava.customcursorcomm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.keksuccino.konkrete.Konkrete;
import io.github.jumperonjava.customcursorcomm.util.FileReadWrite;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.function.Function;

public class CustomCursorInit
{
	public static final String MOD_ID = "customcursorcomm";
	public static final MinecraftClient client = MinecraftClient.getInstance();
	public static final Logger LOGGER = LoggerFactory.getLogger("CustomCursor");
	private static CursorConfigStorage config;
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	public static void entrypoint(Function<String,Boolean> isModLoaded) {
		getConfig();
		if(isModLoaded.apply("konkrete"))
			Konkrete.getEventHandler().registerEventsFrom(CursorRenderer.class);
	}
	private static CursorConfigStorage loadConfig() {
		if(getConfigFile().exists()){
			return gson.fromJson(FileReadWrite.read(getConfigFile()), CursorConfigStorage.class);
		}
		else {
			return new CursorConfigStorage();
		}
	}

	private static File getConfigFile(){
		return client.getResourcePackDir().resolve("../config/customcursor.json").toFile();
	}

	public static CursorConfigStorage getConfig() {
		if(config==null)
			setConfig(loadConfig());
		return config;
	}
	public static void setConfig(CursorConfigStorage config){
		var json = gson.toJson(config);
		FileReadWrite.write(getConfigFile(),json);
		CustomCursorInit.config = config;
	}
}