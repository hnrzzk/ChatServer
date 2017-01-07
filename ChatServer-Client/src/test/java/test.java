import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhangkai on 2017/1/4.
 */
public class test {
    private static int i=0;
    public static void main(String[] args){
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                System.out.println(i++);
                if (4==i){
                    this.cancel();
                    //System.gc();//对Timer对象最后的引用完成后，并且所有未处理的任务都已执行完成后，计时器的任务执行线程会正常终止并且成为垃圾回收的对象。但是这可能要很长时间后才发生
                }
            }
        },0,1000);

        System.out.println("timer is end!!");
    }
}
