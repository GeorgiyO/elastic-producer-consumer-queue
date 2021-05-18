package nekogochan.pc.queue.elastic;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ProducerConsumerQueueI<Type> {

    ProducerConsumerQueueI<Type> addProducer(Supplier<Type> prod);
    ProducerConsumerQueueI<Type> addConsumer(Consumer<Type> cons);

    ProducerConsumerQueueI<Type> removeProducer(Supplier<Type> prod);
    ProducerConsumerQueueI<Type> removeConsumer(Consumer<Type> cons);

    ProducerConsumerQueueI<Type> start();
    ProducerConsumerQueueI<Type> stop();

}
