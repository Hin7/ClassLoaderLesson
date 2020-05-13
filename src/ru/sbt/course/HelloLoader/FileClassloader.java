package ru.sbt.course.HelloLoader;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * FileClassloader - загрузчик классов из файла.
 * В принципе можно было бы и URLClassloader использовать, но для тренировки реализуем свой.
 * Написан после того, как не заработал HelloClassloader на основе его исходников.
 *
 * @author Hin7
 * @version 1.1 13/05/2020
 */
public class FileClassloader extends ClassLoader {

    final private String DirName;

    public FileClassloader(String Directory, ClassLoader parent) {
        super(parent);
        DirName = Directory;
    }

    /**
     * Метод без делегирования к родителям. Делегирование только для родителя класса( т.е. для java.lang.object)
     * Только поиск ранее загруженного и загрузка из файла.
     *
     * @param name - имя класса (ждем SayHello)
     * @return - загруженный из каталога класс
     * @throws ClassNotFoundException - если не найдем корректный класс
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        //для системных классов оставляем все как есть
        if (name.startsWith("java."))
            return super.loadClass(name);
        //поиск ранее загруженного
        Class<?> c = findLoadedClass(name);
        //загрузка, если не нашли
        if (c == null)
            c = findClass(name);
        return c;
    }

    /**
     * Поиск файла с классом и загрузка
     *
     * @param name - имя класса (ждем SayHello)
     * @return - загруженный корректный класс
     * @throws ClassNotFoundException - если не нашли файла с классом
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        ClassNotFoundException classNotFoundException = null;

        try {
            byte cdata[] = Files.readAllBytes(Paths.get(DirName, name + ".class"));
            return defineClass(name, cdata, 0, cdata.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Класс " + name + " не найден", e);
        }
    }
}
