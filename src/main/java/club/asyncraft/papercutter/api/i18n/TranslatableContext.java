package club.asyncraft.papercutter.api.i18n;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class TranslatableContext {

    public YamlMap langMap = new YamlMap();

    public TranslatableContext(JavaPlugin plugin, List<String> langs, String langConfig) throws Exception {
        //导出所有语言文件
        for (String lang : langs) {
            String langRelativePath = "locals/" + lang + ".yml";
            if (!new File(plugin.getDataFolder(), langRelativePath).exists()) {
                plugin.saveResource(langRelativePath, false);
            }
        }

        String langFilePath = Paths.get(plugin.getDataFolder().getAbsolutePath(), "locals", langConfig + ".yml").toString();
        File langFile = new File(langFilePath);
        if (!langFile.exists()) {
            throw new Exception("Lang file is not found!");
        }

        this.langMap = new YamlMap(YamlConfiguration.loadConfiguration(langFile));
    }

    public String translate(String key) {
        YamlMap.Node<?> node = this.langMap.get(key);
        if (node == null) {
            return key;
        }

        return (String) node.get();
    }
}
