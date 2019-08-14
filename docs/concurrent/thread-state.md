### java中的线程状态
#### `NEW`
至今尚未启动的线程处于这种状态
 

#### `RUNNABLE`
正在 Java 虚拟机中执行的线程处于这种状态

#### `BLOCKED`
受阻塞并等待某个监视器锁的线程处于这种状态
- 处于受阻塞状态的某一线程正在等待监视器锁，以便进入一个同步的块/方法
- 或者在调用 Object.wait 之后再次进入同步的块/方法。

#### `WAITING`
无限期地等待另一个线程来执行某一特定操作的线程处于这种状态
- 不带超时值的 Object.wait
- 不带超时值的 Thread.join
- LockSupport.park

#### `TIMED_WAITING`
某一线程因为调用以下带有指定正等待时间的方法之一而处于定时等待状态
- Thread.sleep
- 带有超时值的 Object.wait
- 带有超时值的 Thread.join
- LockSupport.parkNanos
- LockSupport.parkUntil


#### `TERMINATED`
已退出的线程处于这种状态, 线程已经结束执行


> 在给定时间点上，一个线程只能处于一种状态。这些状态是虚拟机状态，它们并没有反映所有操作系统线程状态

```java
public
class Thread implements Runnable {
    /* Java thread status for tools,
     * initialized to indicate thread 'not yet started'
     */
    private volatile int threadStatus = 0;
    
    /**
     * 返回该线程的状态, 该方法用于监视系统状态, 不用于同步控制 
     */
    public State getState() {
        // get current thread state
        return sun.misc.VM.toThreadState(threadStatus); 
    }
}
```