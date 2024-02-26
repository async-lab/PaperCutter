package club.asyncraft.papercutter.api.executor;

import com.google.common.base.Supplier;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class CutterExecutorSection {

    @FunctionalInterface
    public interface SectionRunner {
        void run(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);
    }

    private String name;

    private SectionRunner runner;

    private Supplier<List<CutterExecutorSection>> childrenSupplier;

    public CutterExecutorSection(String name) {
        this.name = name;
        this.childrenSupplier = ArrayList::new;
    }

    public List<CutterExecutorSection> getChildren() {
        return childrenSupplier.get();
    }

    public CutterExecutorSection setChildren(CutterExecutorSection... children) {
        this.childrenSupplier = () -> Arrays.asList(children);
        return this;
    }

    public CutterExecutorSection setChildrenSupplier(Supplier<List<CutterExecutorSection>> childrenSupplier) {
        this.childrenSupplier = childrenSupplier;
        return this;
    }

}
