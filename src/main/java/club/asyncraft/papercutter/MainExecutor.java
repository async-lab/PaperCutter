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
                .setRunner((sender, command, label, args) -> sender.sendMessage(PaperCutter.translatableContext.translate("command.help")))
                .addStaticChildren(
                        new CutterExecutorSection("reload")
                                .setPermissionName(Reference.plugin_group + ".reload")
                                .setRunner((sender, command, label, args) -> {
                                    try {
                                        PaperCutter.instance.initConfig();
                                    } catch (Exception e) {
                                        PaperCutter.instance.getLogger().severe(e.toString());
                                        PaperCutter.disable();
                                    }
                                }),
                        new CutterExecutorSection("player")
                                .setRunner((sender, command, label, args) -> sender.sendMessage(PaperCutter.translatableContext.translate("command.player.usage")))
                                .addChildrenSuppliers(() -> Bukkit.getOnlinePlayers().stream().map(player -> new CutterExecutorSection(player.getName())
                                                .setRunner((sender, command, label, args) -> sender.sendMessage(player.getName())))
                                        .collect(Collectors.toList()))
                );
    }
}
