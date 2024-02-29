package club.asyncraft.papercutter.api.executor;

import club.asyncraft.papercutter.MainExecutor;
import club.asyncraft.papercutter.PaperCutter;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>包装的Executor</p>
 * <p>使用方法为继承这个类，然后向rootSection添加元素</p>
 * <p>可以参考{@link MainExecutor}的使用方法</p>
 */
@Getter
@Setter
public abstract class CutterExecutor implements TabExecutor {

    @NotNull
    protected CutterExecutorSection rootSection;

    public CutterExecutor(@NotNull CutterExecutorSection rootSection) {
        this.rootSection = rootSection;
    }

    public CutterExecutor(String rootSectionName) {
        this(new CutterExecutorSection(rootSectionName));
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        CutterExecutorSection section = rootSection;

        for (String string : args) {
            String arg = string.toLowerCase();
            Permission permission = Bukkit.getServer().getPluginManager().getPermission(section.getPermissionName());
            if (permission != null && !sender.hasPermission(permission)) {
                sender.sendMessage(section.getUsage(PaperCutter.translatableContext.translate("api_default.without_permission")));
                return true;
            }
            CutterExecutorSection childSection = section.getChildren(sender, command, label, args).stream()
                    .filter(s -> s.getSectionName().equalsIgnoreCase(arg))
                    .findFirst().orElse(null);

            if (childSection == null) {
                sender.sendMessage(section.getUsage(PaperCutter.translatableContext.translate("api_default.wrong_usage")));
                return true;
            }

            section = childSection;
        }

        Permission permission = Bukkit.getServer().getPluginManager().getPermission(section.getPermissionName());
        if (permission != null && !sender.hasPermission(permission)) {
            sender.sendMessage(PaperCutter.translatableContext.translate("api_default.without_permission"));
        } else if (section.getHandler() != null) {
            section.getHandler().run(sender, command, label, args);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> hint = new ArrayList<>();
        List<CutterExecutorSection> children = rootSection.getChildren(sender, command, alias, args);

        for (int i = 0; i < args.length - 1; i++) {
            String arg = args[i].toLowerCase();
            CutterExecutorSection section = children.stream()
                    .filter(s -> {
                        Permission permission = Bukkit.getServer().getPluginManager().getPermission(s.getPermissionName());
                        return (permission == null || sender.hasPermission(permission)) && s.getSectionName().equalsIgnoreCase(arg);
                    })
                    .findFirst().orElse(null);

            if (section == null) {
                break;
            }

            children = section.getChildren(sender, command, alias, args);
        }

        children.stream()
                .filter(s -> {
                    Permission permission = Bukkit.getServer().getPluginManager().getPermission(s.getPermissionName());
                    return (permission == null || sender.hasPermission(permission)) && s.getSectionName().toLowerCase().startsWith(args[args.length - 1].toLowerCase());
                })
                .map(CutterExecutorSection::getSectionName)
                .forEach(hint::add);

        return hint;
    }

}
