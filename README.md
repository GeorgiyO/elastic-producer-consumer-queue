##Programming challenges v4.0

#<span>#2: Elastic consumer-producer queue</span>

###Problem description: https://en.wikipedia.org/wiki/Producer%E2%80%93consumer_problem

<hr>

###Solution:

Solving one-to-one problem is easy:
* use ConcurrentCollection (i choose queue)
* bind consumer and producer threads
* make starting and closing threads together

Solving hungry many-to-many problem is just like one-to-one,
but there just many consumers and many producers which one dataset

Solving distributed many-to-many problem is most difficult, then one-to-one:<br>
you need to add item from queue to all consumers, then remove it from collection<br>

After some thinking i find next solution:<br>
* Each consumer have his own queue, from what it takes value to handle
* All producers have common queue, to what they push values
* Distributor-thread take value from Prod-queue, and push it to Cons-queue

to resolve out-of-memory problem, that appears when producers push values faster than consumers take them,
ProdConsQueue have size-limit, when queue is big producers waits, when consumers take value from it.

Distributed queue have two limits: one for prod queue, second form cons queues

### Main classes and methods:

* LoopThread.runSingle:<br>
  https://github.com/GeorgiyO/elastic-producer-consumer-queue/blob/f411c8b5a3201b487bd8a68cc2beff904c8246b6/src/nekogochan/thread/loop/LoopThread.java#L7

* SingleLoopThreadResolver:<br>
  https://github.com/GeorgiyO/elastic-producer-consumer-queue/blob/f411c8b5a3201b487bd8a68cc2beff904c8246b6/src/nekogochan/thread/loop/SingleLoopThreadResolver.java#L5

* LoopThreadPool:<br>
  https://github.com/GeorgiyO/elastic-producer-consumer-queue/blob/f411c8b5a3201b487bd8a68cc2beff904c8246b6/src/nekogochan/pool/LoopThreadPool.java#L10



<hr>

### One to one problem:

One producer - one consumer:<br>
https://github.com/GeorgiyO/elastic-producer-consumer-queue/blob/master/src/nekogochan/pc/queue/ProducerConsumerResolver.java

<hr>

### Many to many problem:

many consumers, many producers, one dataset:

Solutions:

'Hungry' consumers solution: data from dataset removes, then only on consumer take it:
<br>https://github.com/GeorgiyO/elastic-producer-consumer-queue/blob/master/src/nekogochan/pool/OneToOneConsumerThreadPoolResolver.java

'Distributed' solution: data from dataset removes, then each of consumers take it:
<br>https://github.com/GeorgiyO/elastic-producer-consumer-queue/blob/master/src/nekogochan/pool/DistributedConsumerThreadPoolResolver.java

