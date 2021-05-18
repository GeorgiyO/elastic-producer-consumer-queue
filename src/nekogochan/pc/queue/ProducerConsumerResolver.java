package nekogochan.pc.queue;

import nekogochan.thread.loop.LoopThread;
import nekogochan.thread.loop.SingleLoopThreadResolver;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ProducerConsumerResolver<Type> {

    private final Queue<Type> values = new ConcurrentLinkedDeque<>();

    private final AtomicBoolean running = new AtomicBoolean(false);

    private final SingleLoopThreadResolver producer;
    private final SingleLoopThreadResolver consumer;

    public ProducerConsumerResolver(Supplier<Type> producer, Consumer<Type> consumer, int maxSize) {

        this.producer = new SingleLoopThreadResolver(() -> {
            if (values.size() < maxSize) {
                values.add(producer.get());
            }
        }, running);

        this.consumer = new SingleLoopThreadResolver(() -> {
            if (values.size() > 0) {
                consumer.accept(values.poll());
            }
        }, running);

    }

    public void start() {
        running.set(true);
        producer.start();
        consumer.start();
    }

    public void stop() {
        running.set(false);
    }

}
