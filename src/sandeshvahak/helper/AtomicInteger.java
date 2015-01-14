/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.helper;

/**
 *
 * @author Vijay
 */
public class AtomicInteger {
    
        private int uId = 0;

        public AtomicInteger(int value) {
            this.uId = value;
        }

        public int incrementAndGet() {
            synchronized (this) {
                this.uId++;
            }
            return this.uId;
        }
    }

