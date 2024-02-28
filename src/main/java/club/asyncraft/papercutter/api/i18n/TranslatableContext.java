package club.asyncraft.papercutter.api.i18n;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

/**
 * <p>国际化上下文</p>
 * <p>语言文件夹为resources/locals</p>
 * <p>实例化后使用实例的translate方法，输入完全限定名(类似于command.help)作为参数来获得字符串</p>
 */
public class TranslatableContext {

    public YamlMap langMap = new YamlMap();

    /**
     * <p>构造函数</p>
     *
     * @param plugin     插件实例
     * @param langs      支持的语言列表
     * @param langConfig 当前使用的语言
     * @throws Exception 当语言文件不存在时抛出异常
     */
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
