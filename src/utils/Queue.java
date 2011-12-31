/**
 * 
 */
package utils;

/**
 * @author 怪叔叔
 *
 */
public class Queue {
    /**
     * Queue內的資料，ObjectArray
     */
    private Object[] data;
    /**
     * Queue的大小
     */
    private int size;
    /**
     * Queue的開頭
     */
    private int head;
    /**
     * Queue的結尾
     */
    private int tail;
    /**
     * Queue的建構元，初始化Queue所需要的Array大小
     * 
     * @param maxLen Queue的大小
     */
    public Queue(int maxLen) {
        data = new Object[maxLen];
    }
    /**
     * 抓Queue中開頭的Object
     * 
     * @return Object Queue中開頭的Object
     */
    public synchronized Object deQueue() {
        while (size == 0) {
            // Let current Thread wait this object(to sleeping mode)
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Queue InterruptedException");
            }
        }
        Object tmp = data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        if (size == data.length) {
            // wake up all Threads waiting this object
            notifyAll();
        }
        size--;
        return tmp;
    }
    /**
     * 將傳入的Object加入到Queue中
     * @param in 要存入Queue中的Object
     */
    public synchronized void enQueue(Object in) {
        while (size == data.length) {
            // Let current thread wait this object(to sleeping mode)
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Queue InterruptedException");
            }
        }
        data[tail++] = in;
        tail %= data.length;
        size++;
        if (size == 1) {
            // wake up all Threads waiting this object
            notifyAll();
        }
    }
    /**
     * 抓取Queue的大小
     * @return int Queue的大小
     */
    public int getSize() {
        return size;
    }
    /**
     * 將Queue中的Object全部清空，直到Queue中沒有資料
     *
     */
    public void clearn() {
        for (int i = 0 ; i < this.size ; i ++) {
            this.deQueue();
        }
    }
}
