package org.focuszl.workflow;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;



public class FuntionTest {
	private List<Data> datas;
	private CompletionService<Boolean> completionService;
	public FuntionTest(){
		datas = Collections.synchronizedList(new ArrayList<Data>());
//		datas = new ArrayList<Data>();
		datas.add(new Data());
		completionService = new ExecutorCompletionService<Boolean>(Executors.newCachedThreadPool());
		Runtime.getRuntime().addShutdownHook(getSystemShutdownHook());
//		try {
//			Runtime.getRuntime().exec("");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	public static void main(String[] args) {
		FuntionTest funtionTest = new FuntionTest();
		boolean success = funtionTest.test();
		if(success){
			System.out.println("child success");
			System.exit(0);
		}
		
		
	}
	private class SystemShutdownHook extends Thread{
		
		public SystemShutdownHook(){
			super();		
			
		}
		
		@Override
		public void run(){
			System.out.println("exit");//虚拟机结束或者调用system.exit(0)时调用			
		}
	};
	
	public SystemShutdownHook getSystemShutdownHook(){
		return new SystemShutdownHook();
	}

	public boolean test(){
		//创建多个线程，将datas的copy传给它们，看它们是否能保证执行顺序
		final 
		Callable<Boolean> callable = new Callable<Boolean>() {			
			public Boolean call() throws InterruptedException {
				final List<Data> copy = new ArrayList<Data>(datas);
				for(int i = 0; i < 3; i++){
					System.out.println("begin cycle");
					ExecutorService excutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
						private final ThreadFactory factory = Executors.defaultThreadFactory();
						@Override
						public Thread newThread(Runnable r) {
							Thread thread = factory.newThread(r);
							thread.setDaemon(true);
							return thread;
						}
					});
//					ExecutorService excutor = Executors.newFixedThreadPool(3);
					Callable<Boolean> task = new Callable<Boolean>() {
						 public Boolean call(){
							try {
								System.out.println(Thread.currentThread().getName() + " begin");
								copy.get(0).name = Thread.currentThread().getName();
								Thread.sleep(1000);
								System.out.println(copy.get(0).name + " end");
								System.out.println();
							} catch (Exception e) {
								e.printStackTrace();
							}
							return true;
						}
					};
					List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
					tasks.add(task);
					excutor.invokeAll(tasks);//任务执行完毕后才返回，也就是才能进行下一次循环!如果父任务不需要子任务返回，则子任务就可以重新开启一个不需要返回的线程来执行自身
					//子任务的概念很重要，一个任务开启了下一个任务（子任务），子任务完成后返回——要理解嵌套关系！
				}
				return true;
			}
		};
		completionService.submit(callable);
		try {
			if(completionService.take().get()){
				return true;
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}



}
class Data{
	public String name = "init";
}