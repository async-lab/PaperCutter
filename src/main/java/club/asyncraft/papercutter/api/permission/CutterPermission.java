package club.asyncraft.papercutter.api.permission;

import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * <p>权限包装类</p>
 * <p>Bukkit.getPluginManager().addPermission(permission);</p>
 */
public class CutterPermission extends Permission {

    Supplier<String> descriptionSupplier;

    public CutterPermission(@NotNull String name) {
        super(name);
        this.descriptionSupplier = null;
    }

    public CutterPermission(@NotNull Permission permission) {
        super(permission.getName(), permission.getDescription(), permission.getDefault(), permission.getChildren());
        this.descriptionSupplier = null;
    }

    public CutterPermission setDescriptionSupplier(@Nullable Supplier<String> descriptionSupplier) {
        this.descriptionSupplier = descriptionSupplier;
        return this;
    }

    @Override
    public @NotNull String getDescription() {
        return descriptionSupplier == null ? super.getDescription() : descriptionSupplier.get();
    }

    public CutterPermission addChild(@NotNull Permission permission) {
        //这里的true表示继承父权限
        permission.addParent(this, true);
        return this;
    }
}
