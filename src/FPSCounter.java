class FPSCounter{
    private static long lastTime;
    private static long lastAverageTime;
    private static int frames = 0;
    private static double averageFPS;
    
    /**
     * starts or resets the fps counter
     */
    public static void initialize(){
        lastTime = System.nanoTime();
        lastAverageTime = System.nanoTime();
        frames = 0;
        averageFPS = 0;
    }

    /**
     * returns the fps of the last frame
     */
    public static double getFPS(){
        long currentTime = System.nanoTime();
        double fps = 1 / ((currentTime - lastTime) / 1_000_000_000.0);
        lastTime = currentTime;
        return fps;
    }

    /**
     * returns the average fps of the last frameTarget frames once the frameTarget is reached
     */
    public static double getAverageFPS( int frameTarget){
        if(++frames == frameTarget)
        {
            long currentTime = System.nanoTime();
            averageFPS = (1_000_000_000.0 * frames) / ((currentTime - lastAverageTime));
            frames = 0;
            lastAverageTime = currentTime;
        }
        return averageFPS;
    }
    

}