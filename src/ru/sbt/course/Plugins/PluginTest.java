package ru.sbt.course.Plugins;
/**
 * Проверка работы плагинов
 * @author Hin7
 */

public class PluginTest {
    static public void main(String[] args){
        System.out.println("Тест плагинов");
        Plugin[] plugins = new Plugin[3];
        plugins[0] = new PrintHelloPlugin();
        plugins[1] = new Print123Plugin();
        plugins[2] = new PrintGoodbyePlugin();
        for (Plugin pl : plugins) {
            pl.doUsefull();
        }

    }
}
