package nekogochan.thread.loop;

import java.util.concurrent.atomic.AtomicBoolean;

public class LoopThread {

    public static void runSingle(AtomicBoolean threadFlag, AtomicBoolean runningFlag, Runnable action) {
        if (threadFlag.compareAndSet(false, true)) {
            run(runningFlag, action, () -> threadFlag.set(false));
        }
    }

    public static void run(AtomicBoolean runningFlag, Runnable action, Runnable after) {
        new Thread(() -> {
            while (runningFlag.get()) {
                action.run();
            }
            after.run();
        }).start();
    }
    public static void run(AtomicBoolean runningFlag, Runnable action) {
        run(runningFlag, action, () -> {});
    }

}
