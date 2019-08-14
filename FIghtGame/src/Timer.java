public class Timer
{
    private long startTime;
    private long totalTime;
    private boolean isOn;

    public static String toFormattedString(int totalSeconds)
    {
        int minute = totalSeconds / 60;
        int second = totalSeconds % 60;
        String min = String.format("%0"+ 2 +"d", minute);
        String sec = String.format("%0"+ 2 +"d", second);
        return min + ":" + sec;
    }

    public Timer()
    {
        this.totalTime = 0;
        this.startTime = System.currentTimeMillis();
        this.isOn = false;
    }

    public void start()
    {
        if (!isOn)
            startTime = System.currentTimeMillis();
        isOn = true;
    }

    public void stop()
    {
        if (isOn)
        {
            totalTime += System.currentTimeMillis() - startTime;
            startTime = System.currentTimeMillis();
        }
        isOn = false;
    }

    public int getSecondsElapsed()
    {
        int t;
        if (isOn)
        {
            stop();
            totalTime += System.currentTimeMillis() - startTime;
            t = (int) totalTime;
            start();
        }
        else
        {
            t = (int) totalTime;
        }
        return t / 1000;
    }

    public int getMilisecondsElapsed()
    {
        int t;
        if (isOn)
        {
            stop();
            totalTime += System.currentTimeMillis() - startTime;
            t = (int) totalTime;
            start();
        }
        else
        {
            t = (int) totalTime;
        }
        return t ;
    }

    public void zero(){
        totalTime = 0;
        startTime = System.currentTimeMillis();
    }
}