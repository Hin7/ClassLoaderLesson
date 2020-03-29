package ru.sbt.course.PluginManager;
/**
 * Загрузчик слассов из файлов *.class на базе URLClassLoader
 * создан, чтобы изменить порядок делегирования
 * для загрузки всегда из файла
 *
 * @author HIN7
 * @version 1.1 29/03/2020
 */

import java.net.URL;
import java.net.URLClassLoader;

public class FromFileClassLoader extends URLClassLoader {
    public FromFileClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        //для интерфейса и системных классов оставляем все как есть
        if (name.endsWith(".Plugin") || name.startsWith("java."))
            return super.loadClass(name);

        Class<?> c = findLoadedClass(name);

        //убираем делегирование к родителям, чтобы загружать только из файла
        if (c == null)
            c = findClass(name);
        return c;
    }
}
