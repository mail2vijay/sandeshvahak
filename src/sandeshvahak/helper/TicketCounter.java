/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.helper;

/**
 *
 * @author Vijay
 */
public class TicketCounter {

    private final AtomicInteger id = new AtomicInteger(-1);
    private static TicketCounter ticketCounter;

    private TicketCounter() {

    }

    public static TicketCounter getInstance() {
        if (ticketCounter == null) {
            ticketCounter = new TicketCounter();
        }
        return ticketCounter;
    }

    public int nextTicket() {
        return id.incrementAndGet();
    }

    public String makeId(String prefix) {
        int num = nextTicket();
        return (prefix + num);
    }
}
