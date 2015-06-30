/**
 * 
 */
package org.focuszl.workflow.message;

/**
 * @author focuszl
 *
 */
public interface Subscription {
	
	public Connection getConnection();
	public void addListenter(MessageListenter messageListenter);
	public void deletiListenter(MessageListenter messageListenter);
}
