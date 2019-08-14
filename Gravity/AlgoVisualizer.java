import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AlgoVisualizer {

    // TODO: 创建自己的数据
    public static int PAUSE = 20;

    private Circle[] circles;
    private Body[] bodies;
    private MovingBall movingBall;
    private MapDimension mapDimension;
    private Timer timer;
    private Track track;
    private boolean isRunning;  // 数据
    private AlgoFrame frame;    // 视图
    public static int sceneWidth, sceneHeight;

    public AlgoVisualizer(int sceneWidth, int sceneHeight){

        // 初始化数据
        // TODO: 初始化数据

        // 初始化状态和计时器
        isRunning = false;
        timer = new Timer();
        timer.stop();

        // 初始化地图边界
        int maxX = 10240;
        int maxY = 7680;
        mapDimension = new MapDimension(maxX, maxY);

        // 初始化静态星球
        int N = 4;
        bodies = new Body[N];
        bodies[0] = new Body(0,1000,20000,200,1000);
        bodies[1] = new Body(-300,-300,5000,100,600);
        bodies[2] = new Body(700,-200,10000,150,800);
        bodies[3] = new Body(-1200,550,10000,150,800);

        // 初始化静态得分球
        N = 2;
        circles = new Circle[N];
        circles[0] = new Circle(400,200, 20);
        circles[1] = new Circle(600,400,20);

        // 初始化移动球
        double x = 0;
        double y = 0;
        double vx = 0;
        double vy = 0;
        int r = 5;
        Color ballColor = AlgoVisHelper.Red;
        movingBall = new MovingBall(x, y, vx, vy, r, ballColor);

        //初始化轨迹
        track = new Track(movingBall, mapDimension, bodies);

        // 初始化视图
        EventQueue.invokeLater(() -> {
            frame = new AlgoFrame("飞船", sceneWidth, sceneHeight);
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
            frame.render(bodies, circles, movingBall, mapDimension, timer, track, isRunning);
            if (isRunning){
                movingBall.move(-mapDimension.getWidth() / 2, -mapDimension.getHeight() / 2
                        , mapDimension.getWidth() / 2, mapDimension.getHeight() / 2, bodies, circles);
                track.update();
            }
            AlgoVisHelper.pause(PAUSE);
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
                {movingBall.increaseVy(-(movingBall.getR() / 10)); movingBall.setVy(movingBall.checkVelocity(movingBall.getVy())); track.recalculate(movingBall);}
                if (event.getKeyCode() == KeyEvent.VK_DOWN)
                {movingBall.increaseVy((movingBall.getR() / 10)); movingBall.setVy(movingBall.checkVelocity(movingBall.getVy())); track.recalculate(movingBall);}
                if (event.getKeyCode() == KeyEvent.VK_RIGHT)
                {movingBall.increaseVx((movingBall.getR() / 10)); movingBall.setVx(movingBall.checkVelocity(movingBall.getVx())); track.recalculate(movingBall);}
                if (event.getKeyCode() == KeyEvent.VK_LEFT)
                {movingBall.increaseVx(-(movingBall.getR() / 10)); movingBall.setVx(movingBall.checkVelocity(movingBall.getVx())); track.recalculate(movingBall);}

                if (event.getKeyChar() == 'w')
                {movingBall.increaseVy(-(movingBall.getR())); movingBall.setVy(movingBall.checkVelocity(movingBall.getVy())); track.recalculate(movingBall);}
                if (event.getKeyChar() == 's')
                {movingBall.increaseVy((movingBall.getR())); movingBall.setVy(movingBall.checkVelocity(movingBall.getVy())); track.recalculate(movingBall);}
                if (event.getKeyChar() == 'd')
                {movingBall.increaseVx((movingBall.getR())); movingBall.setVx(movingBall.checkVelocity(movingBall.getVx())); track.recalculate(movingBall);}
                if (event.getKeyChar() == 'a')
                {movingBall.increaseVx(-(movingBall.getR())); movingBall.setVx(movingBall.checkVelocity(movingBall.getVx())); track.recalculate(movingBall);}
            }

            if (event.getKeyChar() == 'r') {
                movingBall.reset(movingBall.getOriX(), movingBall.getOriY(), movingBall.getOriVx(), movingBall.getOriVy());
                track.recalculate(movingBall);
            }
            if (event.getKeyChar() == ' ')
            {
                isRunning = !isRunning;
                if (timer.getIsOn()) {timer.stop();}
                else {timer.start();}
            }
        }
    }
    private class AlgoMouseListener extends MouseAdapter
    {
        @Override
        public void mousePressed(MouseEvent event)
        {
            if (isRunning)
            {
                event.translatePoint(0,
                        -(frame.getBounds().height - frame.getCanvasHeight()));
                movingBall.clickAccelerate(event.getPoint(), sceneWidth, sceneHeight);
                track.recalculate(movingBall);
            }
        }
    }


    public static void main(String[] args) {

        // 画布大小
        sceneWidth = 1024;
        sceneHeight = 768;

        // TODO: 根据需要设置其他参数，初始化visualizer
        // 初始化画板
        AlgoVisualizer visualizer = new AlgoVisualizer(sceneWidth, sceneHeight);
    }
}
