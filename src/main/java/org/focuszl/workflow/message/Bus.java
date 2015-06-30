/**
 * 
 */
package org.focuszl.workflow.message;

/**
 * @author focuszl
 *订阅-发布系统的总线，Bus -> Channel -> Connection -> Subscrition(注册事件）
 *                                            -> DeliveryMechanism（传输器）
 *定义系统边界，即系统的输入输出（可能是一组动作，可能是数据）和系统的功能，明确系统和别的模块怎么交互，怎么调用，才能更好的实现系统
 */
public interface Bus {
	public Channel newChannel();
}
