package ru.sbt.course.HelloLoader;

/**
 * Домашнее задание от Крамарева по лекции СБТ 7. Classloaders.
 * HelloClassloader2 - класс, загружающий "правильный" класс SayHello с методом SayHello,
 * выбирая из 2-х разных каталогов. На самом деле не является классическим класслоадером, поскольку не наследутся
 * от Classloader, а содержит в себе 2 класслоадера. Реализует единственный метод loadclass.
 * написан после неудачного эксперимента с HelloClassloader.
 *
 * @author Hin7
 * @version 1.0 13/05/2020
 */


public class HelloClassloader2 {
    final private FileClassloader fileClassloader1;
    final private FileClassloader fileClassloader2;
    final private String CorrectMethodName;

    /**
     * @param FirsDir       - имя первого каталога для поиска
     * @param SecondDir     - имя второго каталога для поиска
     * @param CorrectMethod - корректное имя метода, который должен быть в загружаемом классе
     */
    public HelloClassloader2(String FirsDir, String SecondDir, String CorrectMethod, ClassLoader parent) {
        fileClassloader1 = new FileClassloader(FirsDir, parent);
        fileClassloader2 = new FileClassloader(SecondDir, parent);
        CorrectMethodName = CorrectMethod;
    }

    /**
     * Пробует загрузить класс двумя класслоадерами из разных каталогов.
     * Затем проверяет загруженный файл на наличие метода CorrectMethodName.
     *
     * @param name - имя класса (ждем SayHello)
     * @return - загруженный из каталога класс с методом CorrectMethodName
     * @throws ClassNotFoundException - если не найдем корректный класс
     */
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        ClassNotFoundException exception = null;
        try {
            Class<?> cl = fileClassloader1.loadClass(name);
            cl.getDeclaredMethod(CorrectMethodName);
            return cl;
        } catch (ClassNotFoundException e) {
            exception = e;
        } catch (NoSuchMethodException e) {
            exception = new ClassNotFoundException("Класс " + name + " не найден", e);
        }
        try {
            Class<?> cl = fileClassloader2.loadClass(name);
            cl.getDeclaredMethod(CorrectMethodName);
            return cl;
        } catch (ClassNotFoundException e) {
            e.addSuppressed(exception);
            throw e;
        } catch (NoSuchMethodException e) {
            ClassNotFoundException exception1 = new ClassNotFoundException("Класс " + name + " не найден", e);
            exception1.addSuppressed(exception);
            throw exception1;
        }
    }

}
