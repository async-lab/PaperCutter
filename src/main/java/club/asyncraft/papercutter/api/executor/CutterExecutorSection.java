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

@Getter
@Setter
@Accessors(chain = true)
public class CutterExecutorSection {

    @FunctionalInterface
    public interface SectionRunner {
        boolean run(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);
    }

    private String sectionName;

    @NotNull
    private String permissionName;

    private Supplier<String> usageSupplier;

    private SectionRunner runner;

    private List<CutterExecutorSection> staticChildren;

    private List<Supplier<List<CutterExecutorSection>>> childrenSuppliers;

    public CutterExecutorSection(String sectionName) {
        this.sectionName = sectionName;
        this.staticChildren = new ArrayList<>();
        this.childrenSuppliers = new ArrayList<>();

        this.permissionName = "";
        this.usageSupplier = null;
    }

    public List<CutterExecutorSection> getChildren() {
        List<CutterExecutorSection> children = new ArrayList<>(this.staticChildren);
        children.addAll(this.childrenSuppliers.stream().map(Supplier::get).flatMap(List::stream).toList());
        return children;
    }

    public CutterExecutorSection addStaticChildren(CutterExecutorSection section) {
        this.staticChildren.add(section);
        return this;
    }

    public CutterExecutorSection addChildrenSuppliers(Supplier<List<CutterExecutorSection>> childrenSupplier) {
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
