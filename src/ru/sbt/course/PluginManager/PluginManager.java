package ru.sbt.course.PluginManager;
/**
 * @author Hin7
 * @version 1.0
 */

import ru.sbt.course.Plugins.Plugin;

import java.io.File;
import java.net.URL;


public class PluginManager {
    private final String pluginRootDirectory;

    public PluginManager(String pluginRootDirectory) {
        this.pluginRootDirectory = pluginRootDirectory;
    }

    public Plugin load(String pluginName, String pluginClassName) throws Throwable {
        Plugin result;
        URL[] url = {new URL("file:" + pluginRootDirectory)};
        ClassLoader classLoader = new FromFileClassLoader(url, Plugin.class.getClassLoader());
        //new URLClassLoader(url, Plugin.class.getClassLoader());
        result = (Plugin) classLoader.loadClass(pluginClassName).newInstance();
        return result;
    }

    static public void main(String[] args) {
        PluginManager pM = new PluginManager("c:/Java/pluginRootDirectory/pluginName/");
        Plugin[] plugin = new Plugin[3];
        try {
            plugin[0] = pM.load("Plugin", "ru.sbt.course.Plugins.PrintHelloPlugin");
            plugin[1] = pM.load("Plugin", "ru.sbt.course.Plugins.Print123Plugin");
            plugin[2] = pM.load("Plugin", "ru.sbt.course.Plugins.PrintGoodbyePlugin");
            for (Plugin p : plugin) {
                p.doUsefull();
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        EncryptedClassLoader encryptLoader = new EncryptedClassLoader("3",
                new File("c:/Java/pluginRootDirectory/cryptPluginName/"), Plugin.class.getClassLoader());
        try {
            plugin[0] = (Plugin) encryptLoader.loadClass("ru.sbt.course.Plugins.PrintGoodbyePlugin").newInstance();
            plugin[1] = (Plugin) encryptLoader.loadClass("ru.sbt.course.Plugins.PrintHelloPlugin").newInstance();
            plugin[2] = (Plugin) encryptLoader.loadClass("ru.sbt.course.Plugins.Print123Plugin").newInstance();
            for (Plugin p : plugin) {
                p.doUsefull();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }


    }
}
