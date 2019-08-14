import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.RenderingHints;

import javax.swing.*;

public class AlgoFrame extends JFrame{

    private int canvasWidth;
    private int canvasHeight;

    public AlgoFrame(String title, int canvasWidth, int canvasHeight){

        super(title);

        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;

        AlgoCanvas canvas = new AlgoCanvas();
        setContentPane(canvas);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setVisible(true);
    }

    public AlgoFrame(String title){

        this(title, 1024, 768);
    }

    public int getCanvasWidth(){return canvasWidth;}
    public int getCanvasHeight(){return canvasHeight;}

    // TODO: 设置自己的数据
    private Circle[] circles;
    private MovingBall movingBall;
    private Timer timer;

    public void render(Circle[] circles, MovingBall movingBall, Timer timer){
        this.circles = circles;
        this.movingBall = movingBall;
        this.timer = timer;
        repaint();
    }

    private class AlgoCanvas extends JPanel{

        public AlgoCanvas(){
            // 双缓存
            super(true);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D)g;

            // 抗锯齿
            RenderingHints hints = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.addRenderingHints(hints);

            // 具体绘制
            // TODO： 绘制自己的数据data
            // 绘制静态球
            AlgoVisHelper.setStrokeWidth(g2d, 1);
            for(Circle circle: circles)
            {
                AlgoVisHelper.setColor(g2d, circle.getColor());
                if (circle.getStillExists())
                    AlgoVisHelper.fillCircle(g2d, (int) circle.getX(), (int) circle.getY(), (int) circle.getR());

            }
            // 绘制移动球
            AlgoVisHelper.setColor(g2d, movingBall.getColor());
            AlgoVisHelper.fillCircle(g2d, (int) movingBall.getX(), (int) movingBall.getY(), (int) movingBall.getR());
            // 绘制得分
            AlgoVisHelper.setColor(g2d, AlgoVisHelper.Black);
            AlgoVisHelper.drawTextCenterAlligned(g2d, "Score: " + movingBall.getScore(), canvasWidth / 2, canvasHeight - 10);
            AlgoVisHelper.drawTextCenterAlligned(g2d, "Time " + Timer.toFormattedString(timer.getSecondsElapsed())
                                                , canvasWidth / 2,  10);
        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(canvasWidth, canvasHeight);
        }
    }
}

