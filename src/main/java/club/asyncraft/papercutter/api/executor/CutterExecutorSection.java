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

@Getter
@Setter
@Accessors(chain = true)
public class CutterExecutorSection {

    @FunctionalInterface
    public interface SectionRunner {
        void run(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);
    }

    @FunctionalInterface
    public interface ChildrenSupplier {
        List<CutterExecutorSection> get(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args);
    }

    private String sectionName;

    @NotNull
    private String permissionName;

    private Supplier<String> usageSupplier;

    private SectionRunner runner;

    private List<CutterExecutorSection> staticChildren;

    private List<ChildrenSupplier> childrenSuppliers;

    public CutterExecutorSection(String sectionName) {
        this.sectionName = sectionName;
        this.staticChildren = new ArrayList<>();
        this.childrenSuppliers = new ArrayList<>();

        this.permissionName = "";
        this.usageSupplier = null;
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

    public String getUsage() {
        if (this.usageSupplier == null) {
            return PaperCutter.translatableContext.translate("api_default.default");
        }

        return this.usageSupplier.get();
    }

    public String getUsage(String orElse) {
        return usageSupplier == null ? orElse : this.getUsage();
    }
}
