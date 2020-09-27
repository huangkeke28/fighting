package pratice0921;

//单例模式：保证一个类仅有一个实例，并提供一个访问它的全局结点
public class Singleton {
    //由于jvm具有指令重排序的特性，顺序可能变为132，单线程他不会出现问题，但是多线程的情况下
    //但是多线程下会导致一个线程获得未初始化的实例，
    private static volatile Singleton singleton;
    private Singleton(){}
    public static Singleton getInstance() {
        //提高代码执行效率，当实例创建好了之后，之后并发执行代码，不用进去同步代码块，竞争锁，直接返回前面创建好的实例即可
        if (singleton == null) {
            synchronized (Singleton.class) {
                //防止实例多次创建，比如线程t1调用getInstance，代码执行到if (singleton == null)之后
                //由于资源被线程t2抢占了，这时，由于singleton并没有被实例化，线程t2同样可以通过第一个if
                //然后继续往下执行，同步代码块，第二个if也通过，t2创建了一个实例sington，这是资源又回到t1
                //如果没有第二个if判断，那么，t1也会创建一个singleton实例，这时，就会出现创建多个实例的情况
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+" ：" + Singleton.getInstance().hashCode());
                }
            }).start();
        }
    }
}
//构造单例的两个要素：
//通过该类的构造方法定义为私有方法，这样其他处的代码就无法通过调用该类的构造方法来实例化该类的对象
//该类提供一个静态方法，当我们调用这个方法时，如果类持有的引用不为空就返回这个引用，如果类保持的引用为空
//就创建该类的实例并将实例的引用赋予该类保持的引用
