package club.asyncraft.papercutter;

import club.asyncraft.papercutter.api.executor.CutterExecutor;
import club.asyncraft.papercutter.api.i18n.TranslatableContext;
import club.asyncraft.papercutter.util.Reference;
import lombok.extern.java.Log;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Log
public class PaperCutter extends JavaPlugin {

    public static PaperCutter instance;

    public static TranslatableContext translatableContext;

    public static CutterExecutor executor;

    @Override
    public void onEnable() {
        try {
            PaperCutter.instance = this;

            this.initConfig();

            PaperCutter.executor = new MainExecutor();
            Objects.requireNonNull(this.getServer().getPluginCommand("PaperCutter")).setExecutor(executor);
            Objects.requireNonNull(this.getServer().getPluginCommand("PaperCutter")).setTabCompleter(executor);

            this.getLogger().info(PaperCutter.translatableContext.translate("debug.loaded"));
        } catch (Exception e) {
            PaperCutter.disable();
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        this.getLogger().info(PaperCutter.translatableContext.translate("debug.disabled"));
    }

    public void initConfig() throws Exception {
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
        this.reloadConfig();
        String langConfig = this.getConfig().getString("lang");
        //初始化i18n
        translatableContext = new TranslatableContext(this, Reference.plugin_langs, langConfig);
    }

    public static void disable() {
        PaperCutter.instance.getServer().getPluginManager().disablePlugin(PaperCutter.instance);
    }
}
