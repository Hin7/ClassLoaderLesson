package ru.sbt.course.PluginManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Загрузчик классов с дешифрованием.
 * Алгоритм дешифрования: сдвиг вправо каждого байта на key бит.
 *
 * @author Hin7
 * @version 1.0 27/03/2020
 */

public class EncryptedClassLoader extends ClassLoader {
    private final int key;
    private final File dir;

    public EncryptedClassLoader(String key, File dir, ClassLoader parent) {
        super(parent);
        this.key = Integer.parseInt(key);
        this.dir = dir;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] cdata;

        try {
            //String filename = name.substring(22);
            String filename = name.replace(".", "\\") + ".class";
            cdata = Files.readAllBytes(Paths.get(dir.getPath(), filename));
        } catch (IOException e) {
            throw new ClassNotFoundException(e.toString());
        }

        if (cdata == null) return null;
        byte[] data = new byte[cdata.length];
        int i = 0;
        for (byte b : cdata) {
            int ib = b & 0xFF;
            data[i++] = (byte) ((ib >> key) | ((ib << (8 - key))));
        }
        return defineClass(name, data, 0, data.length);
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
