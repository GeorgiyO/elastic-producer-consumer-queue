package nekogochan.pc.queue.elastic;

import nekogochan.pool.LoopThreadPool;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class ProducerConsumerQueue<Type> implements ProducerConsumerQueueI<Type> {

    private LoopThreadPool<Supplier<Type>> producerPool;
    private LoopThreadPool<Consumer<Type>> consumerPool;
    protected Queue<Type> values = new ConcurrentLinkedQueue<>();

    private final AtomicBoolean running = new AtomicBoolean(false);

    protected void setProducerPool(LoopThreadPool<Supplier<Type>> producerPool) {
        this.producerPool = producerPool;
    }
    protected void setConsumerPool(LoopThreadPool<Consumer<Type>> consumerPool) {
        this.consumerPool = consumerPool;
    }

    @Override
    public ProducerConsumerQueueI<Type> addProducer(Supplier<Type> prod) {
        producerPool.add(prod);
        return this;
    }

    @Override
    public ProducerConsumerQueueI<Type> addConsumer(Consumer<Type> cons) {
        consumerPool.add(cons);
        return this;
    }

    @Override
    public ProducerConsumerQueueI<Type> removeProducer(Supplier<Type> prod) {
        producerPool.remove(prod);
        return this;
    }

    @Override
    public ProducerConsumerQueueI<Type> removeConsumer(Consumer<Type> cons) {
        consumerPool.remove(cons);
        return this;
    }

    @Override
    public ProducerConsumerQueueI<Type> start() {
        if (running.compareAndSet(false, true)) {
            producerPool.start();
            consumerPool.start();
        }
        return this;
    }

    @Override
    public ProducerConsumerQueueI<Type> stop() {
        running.set(false);
        producerPool.stop();
        consumerPool.stop();
        return this;
    }

}
