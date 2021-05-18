package nekogochan.pool;

import java.util.Queue;
import java.util.function.Consumer;

public class OneToOneConsumerThreadPoolResolver<Type> extends LoopThreadPool<Consumer<Type>> {

    public OneToOneConsumerThreadPoolResolver(Queue<Type> values) {
        super.init((consumer) -> () -> {
            var val = values.poll();
            if (val != null) {
                consumer.accept(val);
            }
        });
    }

}
