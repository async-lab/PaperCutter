package club.asyncraft.papercutter.api.executor;

import club.asyncraft.papercutter.PaperCutter;
import com.google.common.base.Supplier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>命令的一个部分</p>
 * <p>当name为null时表示这个部分是一个任意参数</p>
 */
@Getter
@Setter
@Accessors(chain = true)
public class CutterExecutorSection {

    @FunctionalInterface
    public interface SectionHandler {
        void run(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);
    }

    @FunctionalInterface
    public interface ChildrenSupplier {
        List<CutterExecutorSection> get(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args);
    }

    private String name;

    @NotNull
    private String permissionName = "";

    private Supplier<String> usageSupplier = () -> PaperCutter.getInstance().getTranslatableContext().translate("api_default.default");

    private SectionHandler handler = (sender, command, label, args) -> sender.sendMessage(this.usageSupplier.get());

    private CutterExecutorSection parent = null;

    private List<CutterExecutorSection> staticChildren = new ArrayList<>();

    private List<ChildrenSupplier> childrenSuppliers = new ArrayList<>();

    public CutterExecutorSection(String name) {
        this.name = name;
    }

    public List<CutterExecutorSection> getChildren(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<CutterExecutorSection> children = new ArrayList<>(this.staticChildren);
        children.addAll(this.childrenSuppliers.stream().map(childrenSupplier -> childrenSupplier.get(sender, command, alias, args)).flatMap(List::stream).collect(Collectors.toList()));
        return children;
    }

    public CutterExecutorSection addStaticChild(CutterExecutorSection section) {
        this.staticChildren.add(section);
        return this;
    }

    public CutterExecutorSection addStaticChildren(CutterExecutorSection... sections) {
        for (CutterExecutorSection section : sections) {
            this.addStaticChild(section);
        }
        return this;
    }

    public CutterExecutorSection addChildrenSuppliers(ChildrenSupplier childrenSupplier) {
        this.childrenSuppliers.add(childrenSupplier);
        return this;
    }

    public CutterExecutorSection setUsageSupplier(Supplier<String> supplier) {
        this.usageSupplier = supplier;
        return this;
    }

    public String getUsage() {
        if (this.usageSupplier == null) {
            return PaperCutter.getInstance().getTranslatableContext().translate("api_default.default");
        }

        return this.usageSupplier.get();
    }

    public String getUsage(String orElse) {
        return usageSupplier == null ? orElse : this.getUsage();
    }
}
