package pratice0920;

public class SingletonEH {
    //饿汉就是类一旦加载，就把单例初始化完成，保证getInstance的时候，单例已经存在
    private static SingletonEH instance = new SingletonEH();
    private SingletonEH(){}
    public static SingletonEH getInstance() {
        System.out.println("instance" + instance);
        System.out.println("加载饿汉式。。。。");
        return instance;
    }
}
