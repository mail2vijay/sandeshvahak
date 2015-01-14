/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.helper;

/**
 *
 * @author Vijay
 */
public class TicketManager {

    private static TicketManager _instance;
    private static String idBase;
    private final TicketCounter ticketCounter = TicketCounter.getInstance();

    public static TicketManager getInstance() {
        if (_instance == null) {
            _instance = new TicketManager();
        }
        return _instance;
    }

    public String getIdBase() {
        return TicketManager.idBase;
    }

    public TicketManager() {
        idBase = String.valueOf(Func.getNowUnixTimestamp());
    }

    public String generateId() {
        return (idBase + "-" + ticketCounter.nextTicket());
    }
}

