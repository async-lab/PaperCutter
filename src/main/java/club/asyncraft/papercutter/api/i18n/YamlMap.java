package club.asyncraft.papercutter.api.i18n;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;

public class YamlMap extends HashMap<String, YamlMap.Node<?>> {

    @Getter
    @Setter
    public static class Node<T> {
        private T value;

        private Node(T value) {
            this.value = value;
        }

        public static Node<?> of(Object value) {
            if (value instanceof List<?>)
                return new Node<>((List<?>) value);
            else if (value instanceof Boolean)
                return new Node<>((Boolean) value);
            else if (value instanceof Integer)
                return new Node<>((Integer) value);
            else if (value instanceof Double)
                return new Node<>((Double) value);
            else if (value instanceof Long)
                return new Node<>((Long) value);
            else if (value instanceof String)
                return new Node<>((String) value);
            else if (value instanceof Vector)
                return new Node<>((Vector) value);
            else if (value instanceof ItemStack)
                return new Node<>((ItemStack) value);
            else if (value instanceof OfflinePlayer)
                return new Node<>((OfflinePlayer) value);
            else if (value instanceof Color)
                return new Node<>((Color) value);
            else
                return new Node<>(null);
        }

        public T get() {
            return value;
        }
    }

    public YamlMap() {
        super();
    }

    public YamlMap(ConfigurationSection config) {
        super();
        for (String key : config.getKeys(true)) {
            Object o = config.get(key);
            if (o != null && !config.isConfigurationSection(key)) {
                this.put(key, Node.of(o));
            }
        }
    }
}
