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

### Thread类中的状态相关代码
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

### 测试代码
```java
package com.xuguoqin.awesomejava.concurrent;

/**
 * java中的线程状态
 *
 * @author xuguoqin
 * @date 2019/8/14 4:38 PM
 */
public class ThreadState {

    static class NewState implements Runnable {

        @Override
        public void run() {

        }
    }

    static class WaitingState implements Runnable {

        @Override
        public void run() {
            synchronized (WaitingState.class) {
                try {
                    WaitingState.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class BlockedState implements Runnable {

        @Override
        public void run() {
            synchronized (BlockedState.class) {
                try {
                    // there con't use object.wait because this method will release the monitor lock they hold
//                    BlockedState.class.wait();
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class TimeWaitingState implements Runnable {

        @Override
        public void run() {
            synchronized (TimeWaitingState.class) {
                try {
                    TimeWaitingState.class.wait(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // test new state
        Thread newStateThread = new Thread(new NewState(), "new");
        System.out.println(newStateThread.getState());

        // test waiting state
        Thread waitingStateThread = new Thread(new WaitingState(), "waiting");
        waitingStateThread.start();
        Thread.sleep(1000);
        System.out.println(waitingStateThread.getState());

        // test blocked state
        Thread waitingThread = new Thread(new BlockedState(), "blocked1");
        Thread blockedThread = new Thread(new BlockedState(), "blocked2");
        waitingThread.start();
        blockedThread.start();
        Thread.sleep(1000);
        System.out.println(blockedThread.getState());

        // test time_waiting state
        Thread timeWaitingStateThread = new Thread(new TimeWaitingState(), "time_waiting");
        timeWaitingStateThread.start();
        Thread.sleep(1000);
        System.out.println(timeWaitingStateThread.getState());


    }
}

```

### 测试输出结果
```
NEW
WAITING
BLOCKED
TIMED_WAITING
```