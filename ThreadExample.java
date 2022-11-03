// Java program to demonstrate inter-thread communication
// (wait(), join() and notify())

public class ThreadExample
{
	public static void main(String[] args) throws InterruptedException
	{
		final PC pc = new PC();

		Runnable aRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					pc.produce();
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		};

		// Create a thread object that calls pc.produce()
		Thread t1 = new Thread(aRunnable);

		// Create another thread object that calls
		// pc.consume()
		Thread t2 = new Thread(aRunnable);

		Thread t3 = new Thread(aRunnable);

		// Start both threads
		t1.start();
		t2.start();
		t3.start();

		// t1 finishes before t2
		t1.join();
		t2.join();
		t3.join();
	}

	// PC (Produce Consumer) class with produce() and
	// consume() methods.
	public static class PC
	{
		private int count = 0;
		// Prints a string and waits for consume()
		public void produce()throws InterruptedException
		{
			// synchronized block ensures only one thread
			// running at a time.
			synchronized(this)
			{
				count ++;

				if (count == 3){
					// notifies the produce thread that it
                	// can wake up.
					System.out.println("notifying, count: " + count);
					notifyAll();
					System.out.println("i go first!");
				}else{
					// releases the lock on shared resource
					System.out.println("waiting, count: " + count);
					wait();
					System.out.println("back here");
				}
			}
		}
	}
}
