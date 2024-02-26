package club.asyncraft.papercutter.api.executor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class CutterExecutor implements TabExecutor {

    @NotNull
    protected String group;

    @NotNull
    protected CutterExecutorSection section;

    public CutterExecutor(@NotNull String group) {
        this.group = group;
        section = new CutterExecutorSection("root");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> hint = new ArrayList<>();
        List<CutterExecutorSection> children = section.getChildren();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i].toLowerCase();
            for (CutterExecutorSection s : children) {
                if (i == args.length - 1 && s.getName().startsWith(arg)) {
                    hint.add(s.getName());
                }
                if (s.getName().equals(arg)) {
                    children = s.getChildren();
                    break;
                }
            }
        }

        return hint;
    }

}
