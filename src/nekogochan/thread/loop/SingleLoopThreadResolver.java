package nekogochan.thread.loop;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLoopThreadResolver {

    private final AtomicBoolean alive = new AtomicBoolean(false);
    private final Runnable action;
    private final AtomicBoolean running;

    public SingleLoopThreadResolver(Runnable action) {
        this.action = action;
        this.running = new AtomicBoolean(false);
    }

    public SingleLoopThreadResolver(Runnable action, AtomicBoolean running) {
        this.running = running;
        this.action = action;
    }

    public void start() {
        running.set(true);
        LoopThread.runSingle(alive, running, action);
    }

    public void stop() {
        running.set(false);
    }

}
