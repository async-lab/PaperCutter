package club.asyncraft.papercutter;

import club.asyncraft.papercutter.api.executor.CutterExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class PaperCutter extends JavaPlugin {

    public static PaperCutter instance;

    public static CutterExecutor executor;

    @Override
    public void onEnable() {
        PaperCutter.instance = this;
        PaperCutter.executor = new MainExecutor();
        Objects.requireNonNull(this.getServer().getPluginCommand("PaperCutter")).setExecutor(executor);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return executor.onCommand(sender, command, label, args);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return executor.onTabComplete(sender, command, alias, args);
    }
}
