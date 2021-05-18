package nekogochan.pool;

import java.util.Queue;
import java.util.function.Supplier;

public class ProducerThreadPoolResolver<Type> extends LoopThreadPool<Supplier<Type>> {

    public ProducerThreadPoolResolver(Queue<Type> values, int valuesMaxSize) {
        super.init((supplier) -> () -> {
            if (values.size() <= valuesMaxSize) {
                values.add(supplier.get());
            }
        });
    }

}