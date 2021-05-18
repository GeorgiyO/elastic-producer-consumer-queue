package nekogochan;

import nekogochan.pc.queue.ProducerConsumerResolver;
import nekogochan.pc.queue.elastic.DistributedProducerConsumerQueue;
import nekogochan.pc.queue.elastic.HungryProducerConsumerQueue;
import nekogochan.pc.queue.elastic.ProducerConsumerQueueI;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

class LambdaDelayGen<Type> {

    Consumer<Integer> sleep = (delay) -> {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };

    BiFunction<Supplier<Type>, Supplier<Integer>, Supplier<Type>> prodWithDelay = (sup, delay) -> () -> {
        sleep.accept(delay.get());
        return sup.get();
    };

    BiFunction<Consumer<Type>, Supplier<Integer>, Consumer<Type>> consWithDelay = (cons, delay) -> (val) -> {
        cons.accept(val);
        sleep.accept(delay.get());
    };

}

public class Main {

    static LambdaDelayGen<Integer> gen = new LambdaDelayGen<>();
    static AtomicInteger iter = new AtomicInteger(0);
    static Random rand = new Random();

    static void testProdConsResolver() {
        var prodConsResolver = new ProducerConsumerResolver<Integer>(
            gen.prodWithDelay.apply(iter::getAndIncrement, () -> rand.nextInt(2000)),
            gen.consWithDelay.apply(System.out::println, () -> 1000),
            10
        );

        prodConsResolver.start();
        prodConsResolver.stop();
        prodConsResolver.start();
        prodConsResolver.stop();
        prodConsResolver.start();
        prodConsResolver.start();
        prodConsResolver.stop();
        prodConsResolver.start();
        prodConsResolver.start();
    }

    static void testHungryProdConsQueue(int maxValuesInQueue) {
        testProdConsQueue(new HungryProducerConsumerQueue<>(10));
    }

    // difference between values count in prod queue and cons queue must be less or equals then:
    // maxValuesInConsQueue - maxValuesInProdQueue
    static void testDistributedProdConsQueue(int maxValuesInProdQueue, int maxValuesInConsQueue) {
        testProdConsQueue(new DistributedProducerConsumerQueue<>(maxValuesInProdQueue, maxValuesInConsQueue));
    }

    static void testProdConsQueue(ProducerConsumerQueueI<Integer> queue) {

        queue
            .addProducer(gen.prodWithDelay.apply(iter::getAndIncrement, () -> rand.nextInt(1000)))
            .addProducer(gen.prodWithDelay.apply(iter::getAndIncrement, () -> rand.nextInt(1000)))
            .addProducer(gen.prodWithDelay.apply(iter::getAndIncrement, () -> rand.nextInt(1000)))
            .addConsumer(gen.consWithDelay.apply((s) -> System.out.println("first:\t" + s), () -> 500))
            .addConsumer(gen.consWithDelay.apply((s) -> System.out.println("second:\t" + s), () -> 750))
            .addConsumer(gen.consWithDelay.apply((s) -> System.out.println("third:\t" + s), () -> 1000))

            .start()
            .stop()
            .start()
            .stop()
            .start()
            .stop()

            .start()
            .start()
            .start()
            .start()
        ;

    }

    public static void main(String[] args) {
        testDistributedProdConsQueue(5, 5);
    }

}
