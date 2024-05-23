package club.asyncraft.papercutter;

import club.asyncraft.papercutter.api.executor.CutterExecutor;
import club.asyncraft.papercutter.api.executor.CutterExecutorSection;
import club.asyncraft.papercutter.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class MainExecutor extends CutterExecutor {
    public MainExecutor() {
        super(Reference.plugin_id);
        this.getRootSection()
                .setHandler((sender, command, label, args) -> sender.sendMessage(PaperCutter.getInstance().getTranslatableContext().translate("command.help")))
                .addStaticChildren(
                        new CutterExecutorSection("reload")
                                .setPermissionName(Reference.plugin_group + ".reload")
                                .setHandler(this::onReload),
                        new CutterExecutorSection("player")
                                .setUsageSupplier(() -> PaperCutter.getInstance().getTranslatableContext().translate("command.player.usage"))
                                .addChildrenSuppliers(
                                        (sender, command, alias, args) -> Bukkit.getOnlinePlayers().stream().map(
                                                        player -> new CutterExecutorSection(player.getName())
                                                                .setHandler((sender1, command1, label1, args1) -> sender1.sendMessage(player.getName()))
                                                )
                                                .collect(Collectors.toList()))
                );
    }

    private void onReload(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            PaperCutter.getInstance().initConfig();
        } catch (Exception e) {
            PaperCutter.getInstance().getLogger().severe(e.toString());
            PaperCutter.disable();
        }
        commandSender.sendMessage(PaperCutter.getInstance().getTranslatableContext().translate("command.reload.reloaded"));
    }
}
