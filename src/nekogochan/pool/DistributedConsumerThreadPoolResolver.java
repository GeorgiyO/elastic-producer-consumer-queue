package nekogochan.pool;

import nekogochan.thread.loop.SingleLoopThreadResolver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

class ActionResolver<Type> {

    Queue<Type> values = new LinkedList<>();
    Runnable action;

    ActionResolver(Consumer<Type> consumer) {
        this.action = () -> consumer.accept(values.poll());
    }
}

public class DistributedConsumerThreadPoolResolver<Type> extends LoopThreadPool<Consumer<Type>> {

    private final Map<Consumer<Type>, ActionResolver<Type>> resolvers = new HashMap<>();

    private final SingleLoopThreadResolver distributor;

    public DistributedConsumerThreadPoolResolver(Queue<Type> values, int maxLocalQueueSize) {

        super.init((consumer) -> {
            var resolver = new ActionResolver<Type>(
                (val) -> {
                    if (val != null) {
                        consumer.accept(val);
                    }
                }
            );
            resolvers.put(consumer, resolver);
            return resolver.action;
        });

        this.distributor = new SingleLoopThreadResolver(() -> {

            var maxSize = resolvers.values()
                                   .stream()
                                   .map((resolver) -> resolver.values.size())
                                   .max(Integer::compare)
                                   .get();

            if (maxSize < maxLocalQueueSize) {
                var val = values.poll();
                if (val != null) {
                    resolvers.values().forEach((resolver) -> resolver.values.add(val));
                }
            }
        });
    }

    @Override
    public void remove(Consumer<Type> consumer) {
        resolvers.remove(consumer);
        super.remove(consumer);
    }

    @Override
    public void start() {
        distributor.start();
        super.start();
    }

    @Override
    public void stop() {
        distributor.stop();
        super.stop();
    }
}
