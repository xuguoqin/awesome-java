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
