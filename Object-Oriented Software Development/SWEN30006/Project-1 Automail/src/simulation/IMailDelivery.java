package simulation;

import automail.MailItem;

/**
 * a MailDelivery is used by the Robot to deliver mail once it has arrived at
 * the correct location
 */
public interface IMailDelivery {

     /**
      * Delivers an item at its floor
      * 
      * @param mailItem the mail item being delivered.
      */
     void deliver(MailItem mailItem, boolean charge);

}