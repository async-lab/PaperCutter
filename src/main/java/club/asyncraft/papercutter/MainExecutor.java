package club.asyncraft.papercutter;

import club.asyncraft.papercutter.api.executor.CutterExecutor;
import club.asyncraft.papercutter.api.executor.CutterExecutorSection;
import club.asyncraft.papercutter.util.Reference;
import org.bukkit.Bukkit;

import java.util.stream.Collectors;

public class MainExecutor extends CutterExecutor {
    public MainExecutor() {
        super(Reference.plugin_id);
        this.getRootSection()
                .setHandler((sender, command, label, args) -> sender.sendMessage(PaperCutter.translatableContext.translate("command.help")))
                .addStaticChildren(
                        new CutterExecutorSection("reload")
                                .setPermissionName(Reference.plugin_group + ".reload")
                                .setHandler((sender, command, label, args) -> {
                                    try {
                                        PaperCutter.instance.initConfig();
                                        sender.sendMessage(PaperCutter.translatableContext.translate("command.reload.reloaded"));
                                    } catch (Exception e) {
                                        PaperCutter.instance.getLogger().severe(e.toString());
                                        PaperCutter.disable();
                                    }
                                }),
                        new CutterExecutorSection("player")
                                .setHandler((sender, command, label, args) -> sender.sendMessage(PaperCutter.translatableContext.translate("command.player.usage")))
                                .addChildrenSuppliers(
                                        (sender, command, alias, args) -> Bukkit.getOnlinePlayers().stream().map(player -> new CutterExecutorSection(player.getName())
                                                        .setHandler((sender1, command1, label1, args1) -> sender1.sendMessage(player.getName())))
                                                .collect(Collectors.toList()))
                );
    }
}
