public class CoronaYoddha implements Runnable{
    
    GUI gui=new GUI();
    
    public static void main(String[] args)
    {
    	new Thread(new CoronaYoddha()).start();
    }
    
    @Override
    public void run() {
        while(true){
            gui.repaint();
        }
    }
    
}