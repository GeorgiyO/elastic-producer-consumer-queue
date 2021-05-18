package nekogochan.pc.queue.elastic;

import nekogochan.pool.DistributedConsumerThreadPoolResolver;
import nekogochan.pool.ProducerThreadPoolResolver;

public class DistributedProducerConsumerQueue<Type> extends ProducerConsumerQueue<Type> {

    public DistributedProducerConsumerQueue(int maxValues, int maxLocalValues) {
        super.setProducerPool(new ProducerThreadPoolResolver<>(super.values, maxValues));
        super.setConsumerPool(new DistributedConsumerThreadPoolResolver<>(super.values, maxLocalValues));
    }

}
