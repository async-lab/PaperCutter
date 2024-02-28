package club.asyncraft.papercutter;

import club.asyncraft.papercutter.api.executor.CutterExecutor;
import club.asyncraft.papercutter.api.executor.CutterExecutorSection;
import org.bukkit.Bukkit;

import java.util.stream.Collectors;

public class MainExecutor extends CutterExecutor {
    public MainExecutor() {
        super("papercutter");
        this.getRootSection()
                .addStaticChildren(new CutterExecutorSection("reload"))
                .addStaticChildren(new CutterExecutorSection("help")
                        .addChildrenSuppliers(() -> Bukkit.getOnlinePlayers().stream().map(player -> new CutterExecutorSection(player.getName())
                                        .setRunner((sender, command, label, args) -> {
                                            sender.sendMessage(player.getName());
                                            return true;
                                        }))
                                .collect(Collectors.toList()))
                )
                .addStaticChildren(new CutterExecutorSection("revive"));
    }
}
