/**
 * 
 */
package org.csstudio.alarm.table.logTable;

import org.csstudio.alarm.table.dataModel.JMSMessage;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.csstudio.alarm.table.dataModel.JMSMessage;


/**
 * Sorter for alarm messages. Insert messages with the highest
 * severity at the top and if two messages have the same serverity
 * the newest at the top.
 * 
 * @author jhatje
 *
 */
public class JMSMessageSorterAlarm extends ViewerSorter {

	public int compare(Viewer viewer, Object o1, Object o2) {

		JMSMessage jmsm1;
		JMSMessage jmsm2;
		
		if ((o1 != null) && (o2 != null)) {
			jmsm1 = (JMSMessage) o2;
			jmsm2 = (JMSMessage) o1;
		} else {
			return 0;
		}
			
		Integer one;
		try {
			one = new Integer(jmsm1.getSeverityNumber());
		} catch (NumberFormatException e) {
			one = new Integer(-1);
		}

		Integer two;
		try {
			two = new Integer(jmsm2.getSeverityNumber());
//			two = JMSMessage.severityToNumber(jmsm2.getProperty("SEVERITY"));
		} catch (NumberFormatException e) {
			two = new Integer(-1);
		}
		
		//same severity -> newest message at the top
		if (two == one) {
			return (collator.compare(jmsm2.getProperty("EVENTTIME"), jmsm1.getProperty("EVENTTIME")));
		}
		
		return (two - one);
	}
}
