package club.asyncraft.papercutter;

import club.asyncraft.papercutter.api.executor.CutterExecutor;
import club.asyncraft.papercutter.api.executor.CutterExecutorSection;
import club.asyncraft.papercutter.util.Reference;
import org.bukkit.Bukkit;

public class MainExecutor extends CutterExecutor {
    public MainExecutor() {
        super(Reference.plugin_group);
        this.section.setChildren(
                new CutterExecutorSection("reload"),
                new CutterExecutorSection("help").setChildrenSupplier(
                        () -> Bukkit.getOnlinePlayers().stream().map(p -> {
                            String name = p.getName();
                            return new CutterExecutorSection(name);
                        }).toList()
                ),
                new CutterExecutorSection("version")
        );
    }
}
