package ru.sbt.course.HelloLoader;

/**
 * Домашнее задание от Крамарева по лекции СБТ 7. Classloaders.
 * HelloClassloader - класс, загружающий "правильный" класс SayHello с методом SayHello.
 *
 * @author Hin7
 * @version 1.0 13/05/2020
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class HelloClassloader extends ClassLoader {

    final private String FirstDirName;
    final private String SecondDirName;
    final private String CorrectMethodName;

    /**
     * @param FirsDir       - имя первого каталога для поиска
     * @param SecondDir     - имя второго каталога для поиска
     * @param CorrectMethod - корректное имя метода, который должен быть в загружаемом классе
     */
    public HelloClassloader(String FirsDir, String SecondDir, String CorrectMethod, ClassLoader parent) {
        super(parent);
        FirstDirName = FirsDir;
        SecondDirName = SecondDir;
        CorrectMethodName = CorrectMethod;
    }

    /**
     * Метод без делегирования к родителям. Делегирование только для родителя класса( т.е. для java.lang.object)
     * Только поиск ранее загруженного и загрузка из файла.
     *
     * @param name - имя класса (ждем SayHello)
     * @return - загруженный из корректного каталога класс
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
     * Поиск файла с классом в двух каталогах, загрузка и проверка на наличие метода CorrectMethodName
     *
     * @param name - имя класса (ждем SayHello)
     * @return - загруженный корректный класс
     * @throws ClassNotFoundException - если не нашли файла с классом
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        ClassNotFoundException classNotFoundException = null;

        try {
            return defineClassByDir(FirstDirName, name);
        } catch (IOException | NoSuchMethodException e) {
            classNotFoundException = new ClassNotFoundException("Класс " + name + " не найден", e);
        }
        try {
            // в этом месте выдает LinkageError: attempt duplicate class definition for name: "SayHello"
            // не может один класслоадер загрузить два класса с одинаковым именем ((((
            return defineClassByDir(SecondDirName, name);
        } catch (IOException | NoSuchMethodException e) {
            ClassNotFoundException exception = new ClassNotFoundException("Класс " + name + " не найден", e);
            if (classNotFoundException != null)
                exception.addSuppressed(classNotFoundException);
            throw exception;
        }
    }

    /**
     * Восвращает класс по дирректории и имени класса.
     *
     * @param dir  - директория, где искать файл с классом.
     * @param name - имя класса.
     * @return - класс, удовлетворяющий условию (есть метод CorrectMethodName)
     * @throws IOException           - если не удалось открыть файл с классом
     * @throws NoSuchMethodException - если метода CorrectMethodName нет в классе.
     */
    private Class<?> defineClassByDir(String dir, String name) throws IOException, NoSuchMethodException {
        byte cdata[] = Files.readAllBytes(Paths.get(dir, name + ".class"));
        Class<?> cl = defineClass(name, cdata, 0, cdata.length);
        cl.getDeclaredMethod(CorrectMethodName);
        return cl;
    }
}
