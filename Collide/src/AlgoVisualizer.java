import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AlgoVisualizer {

    // TODO: 创建自己的数据
    private Circle[] circles;
    private MovingBall movingBall;
    private Timer timer;
    private boolean isRunning;  // 数据
    private AlgoFrame frame;    // 视图
    public static int sceneWidth, sceneHeight;

    public AlgoVisualizer(int sceneWidth, int sceneHeight){

        // 初始化数据
        // TODO: 初始化数据

        // 初始化状态和计时器
        timer = new Timer();
        isRunning = false;
        timer.stop();

        // 初始化静态球
        int rows = 10;
        double r = 20;
        Color circleColor = AlgoVisHelper.Blue;
        int columns = sceneWidth / (int) (2 * r);
        int N = rows * columns;
        circles = new Circle[N];
        for (int i = 0; i < N; i ++)
        {
            double cx = (i % columns) * 2 * r + r;
            double cy = (i / columns) * 2 * r + r;
            circles[i] = new Circle(cx, cy, r, circleColor);
        }

        // 初始化移动球
        double x = sceneWidth / 2;
        double y = sceneHeight / 2;
        double vx = 0;
        double vy = 0;
        r = 10;
        Color ballColor = AlgoVisHelper.Red;
        movingBall = new MovingBall(x, y, vx, vy, r, ballColor);

        // 初始化视图
        EventQueue.invokeLater(() -> {
            frame = new AlgoFrame("吃球", sceneWidth, sceneHeight);
            // TODO: 根据情况决定是否加入键盘鼠标事件监听器
            frame.addKeyListener(new AlgoKeyListener());
            frame.addMouseListener(new AlgoMouseListener());
            new Thread(() -> {
                run();
            }).start();
        });
    }

    // 动画逻辑
    private void run(){

        // TODO: 编写自己的动画逻辑
        while (true)
        {
            frame.render(circles, movingBall, timer);
            if (isRunning)
                movingBall.move(0, 0, frame.getCanvasWidth(), frame.getCanvasHeight(), circles);
            AlgoVisHelper.pause(20);
        }
    }

    // TODO: 根据情况决定是否实现键盘鼠标等交互事件监听器类
    private class AlgoKeyListener extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent event)
        {
            if (isRunning)
            {
                if (event.getKeyCode() == KeyEvent.VK_UP)
                    movingBall.increaseVy(-(movingBall.getR() / 25));
                if (event.getKeyCode() == KeyEvent.VK_DOWN)
                    movingBall.increaseVy((movingBall.getR() / 25));
                if (event.getKeyCode() == KeyEvent.VK_RIGHT)
                    movingBall.increaseVx((movingBall.getR() / 25));
                if (event.getKeyCode() == KeyEvent.VK_LEFT)
                    movingBall.increaseVx(-(movingBall.getR() / 25));
            }

            if (event.getKeyChar() == ' ')
            {
                if (isRunning) {timer.stop();}
                else {timer.start();}
                isRunning = !isRunning;
            }
        }
    }
    private class AlgoMouseListener extends MouseAdapter{ }


    public static void main(String[] args) {

        // 画布大小
        sceneWidth = 800;
        sceneHeight = 600;

        // TODO: 根据需要设置其他参数，初始化visualizer
        // 初始化画板
        AlgoVisualizer visualizer = new AlgoVisualizer(sceneWidth, sceneHeight);
    }
}
