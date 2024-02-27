package club.asyncraft.papercutter.api.i18n;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlMap extends HashMap<String, YamlMap.Node<?>> {

    public static class Node<T> {
        private T value;

        private Node(T value) {
            this.value = value;
        }

        public static Node<?> create(ConfigurationSection section, String path) {
//            if (section.isConfigurationSection(path))
//                return new Node<>(new YamlMap(section.getConfigurationSection(path)));
//            else
            if (section.isList(path))
                return new Node<List<?>>(section.getList(path));
            else if (section.isBoolean(path))
                return new Node<>(section.getBoolean(path));
            else if (section.isInt(path))
                return new Node<>(section.getInt(path));
            else if (section.isDouble(path))
                return new Node<>(section.getDouble(path));
            else if (section.isLong(path))
                return new Node<>(section.getLong(path));
            else if (section.isString(path))
                return new Node<>(section.getString(path));
            else if (section.isVector(path))
                return new Node<>(section.getVector(path));
            else if (section.isItemStack(path))
                return new Node<>(section.getItemStack(path));
            else if (section.isOfflinePlayer(path))
                return new Node<>(section.getOfflinePlayer(path));
            else if (section.isColor(path))
                return new Node<>(section.getColor(path));

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
        traverseSection(config, "", this);
    }

    private static void traverseSection(ConfigurationSection section, String pathPrefix, Map<String, Node<?>> resultMap) {
        for (String key : section.getKeys(false)) {
            String currentPath = pathPrefix.isEmpty() ? key : pathPrefix + "." + key;
            if (section.isConfigurationSection(key)) {
                traverseSection(section.getConfigurationSection(key), currentPath, resultMap);
            } else {
                Object o = section.get(key);
                if (o != null) {
                    resultMap.put(currentPath, Node.create(section, key));
                }
            }
        }
    }
}
