package nekogochan.pool;

import nekogochan.thread.loop.SingleLoopThreadResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class LoopThreadPool<Key> {

    private Function<Key, Runnable> keyToAction;

    private final Map<Key, SingleLoopThreadResolver> resolvers = new HashMap<>();

    private final AtomicBoolean running = new AtomicBoolean(false);

    public void init(Function<Key, Runnable> keyToAction) {
        this.keyToAction = keyToAction;
    }

    public Key add(Key key) {
        var threadResolver = new SingleLoopThreadResolver(keyToAction.apply(key));
        if (running.get()) {
            threadResolver.start();
        }
        resolvers.put(key, threadResolver);
        return key;
    }

    public void remove(Key key) {
        resolvers.remove(key).stop();
    }

    public void start() {
        if (running.compareAndSet(false, true)) {
            resolvers.values().forEach(SingleLoopThreadResolver::start);
        }
    }

    public void stop() {
        resolvers.values().forEach(SingleLoopThreadResolver::stop);
        running.set(false);
    }



}
