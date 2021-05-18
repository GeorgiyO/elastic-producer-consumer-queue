package nekogochan.pc.queue.elastic;

import nekogochan.pool.OneToOneConsumerThreadPoolResolver;
import nekogochan.pool.ProducerThreadPoolResolver;

public class HungryProducerConsumerQueue<Type> extends ProducerConsumerQueue<Type> {

    public HungryProducerConsumerQueue(int maxValues) {
        super.setProducerPool(new ProducerThreadPoolResolver<>(super.values, maxValues));
        super.setConsumerPool(new OneToOneConsumerThreadPoolResolver<>(super.values));
    }
}
