import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.RenderingHints;
import java.util.ArrayList;

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
    private Body[] bodies;
    private MapDimension mapDimension;
    private Timer timer;
    private Track track;
    private boolean isRunning;

    public void render(Body[] bodies, Circle[] circles, MovingBall movingBall, MapDimension mapDimension,
                       Timer timer, Track track, Boolean isRunning){
        this.circles = circles;
        this.movingBall = movingBall;
        this.bodies = bodies;
        this.isRunning = isRunning;
        this.mapDimension = mapDimension;
        this.timer = timer;
        this.track = track;
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
            // 分别绘制非暂停和暂停状态下的场景
            if (isRunning)
            {paintRunningScene(g2d);}
            else {paintPausedScene(g2d);}

            // 绘制得分 显示速度 显示时间
            paintTextPanel(g2d);
        }

        private void paintRunningScene(Graphics2D g2d)
        {
            // 计算偏移量
            int shiftX = (int) movingBall.getX() - canvasWidth / 2;
            int shiftY = (int) movingBall.getY() - canvasHeight / 2;

            // 绘制黑色背景
            AlgoVisHelper.setColor(g2d, AlgoVisHelper.Black);
            AlgoVisHelper.fillRectangle(g2d, 0, 0, canvasWidth, canvasHeight);

            // 绘制静态星球及其有效重力场
            for (Body body : bodies) {
                AlgoVisHelper.setColor(g2d, body.getGravityColor());
                AlgoVisHelper.fillCircle(g2d, (int) body.getX() - shiftX, (int) body.getY() - shiftY, (int) body.getGravityR());
            }
            for (Body body : bodies) {
                AlgoVisHelper.setColor(g2d, body.getBoundColor());
                AlgoVisHelper.setStrokeWidth(g2d, 2);
                AlgoVisHelper.strokeCircle(g2d, (int) body.getX() - shiftX, (int) body.getY() - shiftY, (int) body.getGravityR());
            }
            for (Body body : bodies) {
                AlgoVisHelper.setColor(g2d, body.getBodyColor());
                AlgoVisHelper.fillCircle(g2d, (int) body.getX() - shiftX, (int) body.getY() - shiftY, (int) body.getBodyR());
            }

            // 绘制静态球
            for (Circle circle : circles) {
                AlgoVisHelper.setColor(g2d, circle.getColor());
                if (circle.getStillExists())
                    AlgoVisHelper.fillCircle(g2d, (int) circle.getX() - shiftX, (int) circle.getY() - shiftY, (int) circle.getR());

            }

            //绘制轨迹
            // Track track = new Track(movingBall, mapDimension, bodies);
            // AlgoVisHelper.setColor(g2d, AlgoVisHelper.White);
            AlgoVisHelper.setStrokeWidth(g2d,2);
            int color = 255; int i = 0;
            while (color >= 10 && i < track.length()){
                AlgoVisHelper.setColor(g2d, new Color(color,color,color));
                AlgoVisHelper.fillCircle(g2d, (int)track.getPoint(i).x() - shiftX,(int) track.getPoint(i).y() - shiftY,2);
                color -= 10;
                i += 5;
            }

            // 绘制移动球
            AlgoVisHelper.setColor(g2d, movingBall.getColor());
            AlgoVisHelper.fillCircle(g2d, (int) movingBall.getX() - shiftX, (int) movingBall.getY() - shiftY, (int) movingBall.getR());
            // 绘制速度矢量
            double px = canvasWidth / 2 + movingBall.getVx() * AlgoVisualizer.PAUSE;
            double py = canvasHeight / 2 + movingBall.getVy() * AlgoVisualizer.PAUSE;
            ArrowComponent v = new ArrowComponent(canvasWidth / 2, canvasHeight / 2, px, py);
            if (v.length() >= 5) {
                AlgoVisHelper.setColor(g2d, AlgoVisHelper.Yellow);
                AlgoVisHelper.drawLine(g2d, v.ox(), v.oy(), v.px(), v.py());
                AlgoVisHelper.drawLine(g2d, v.px1(), v.py1(), v.px(), v.py());
                AlgoVisHelper.drawLine(g2d, v.px2(), v.py2(), v.px(), v.py());
            }

            // 绘制边界
            AlgoVisHelper.setStrokeWidth(g2d, 5);
            AlgoVisHelper.setColor(g2d, mapDimension.boundaryColor);
            AlgoVisHelper.strokeRectangle(g2d, (0 - mapDimension.getWidth() / 2) - shiftX, (0 - mapDimension.getHeight() / 2) - shiftY, mapDimension.getWidth(), mapDimension.getHeight());
        }

        private void paintPausedScene(Graphics2D g2d)
        {

            // 计算缩小量
            double multiplierX = mapDimension.getWidth() / (double) canvasWidth;
            double multiplierY = mapDimension.getHeight() / (double) canvasHeight;
            double multiplier = Math.max(multiplierX, multiplierY);
            double centerX = canvasWidth / 2;
            double centerY = canvasHeight / 2;

            // 绘制黑色背景
            AlgoVisHelper.setColor(g2d, AlgoVisHelper.Black);
            AlgoVisHelper.fillRectangle(g2d, 0, 0, canvasWidth, canvasHeight);

            // 绘制静态星球及其重力场
            for (Body body : bodies) {
                AlgoVisHelper.setColor(g2d, body.getGravityColor());
                AlgoVisHelper.fillCircle(g2d, (int) (body.getX() / multiplier + centerX), (int) (body.getY() / multiplier + centerY), (int) (body.getGravityR() / multiplier));
            }
            for (Body body : bodies) {
                AlgoVisHelper.setColor(g2d, body.getBoundColor());
                AlgoVisHelper.setStrokeWidth(g2d, 1);
                AlgoVisHelper.strokeCircle(g2d, (int) (body.getX() / multiplier + centerX), (int) (body.getY() / multiplier + centerY), (int) (body.getGravityR() / multiplier));
            }
            for (Body body : bodies) {
                AlgoVisHelper.setColor(g2d, body.getBodyColor());
                AlgoVisHelper.fillCircle(g2d, (int) (body.getX() / multiplier + centerX), (int) (body.getY() / multiplier + centerY), (int) (body.getBodyR() / multiplier));
            }

            // 绘制静态球
            for (Circle circle : circles) {
                AlgoVisHelper.setColor(g2d, circle.getColor());
                if (circle.getStillExists())
                    AlgoVisHelper.fillCircle(g2d, (int) (circle.getX() / multiplier + centerX), (int) (circle.getY() / multiplier + centerY), (int) (circle.getR() / multiplier));

            }

            // 绘制移动球
            AlgoVisHelper.setColor(g2d, movingBall.getColor());
            AlgoVisHelper.fillCircle(g2d, (int) (movingBall.getX() / multiplier + centerX), (int) (movingBall.getY() / multiplier + centerY), (int) movingBall.getR());

            // 绘制轨迹
            AlgoVisHelper.setColor(g2d, AlgoVisHelper.White);
            AlgoVisHelper.setStrokeWidth(g2d,1);
            for (int i = 0; i < track.length() - 1; i = i + 1)
                AlgoVisHelper.drawLine(g2d, track.getPoint(i).x() / multiplier + centerX, track.getPoint(i).y() / multiplier + centerY,
                        track.getPoint(i + 1 ).x() / multiplier + centerX, track.getPoint(i + 1).y() / multiplier + centerY);
        }

        private void paintTextPanel(Graphics2D g2d)
        {
            AlgoVisHelper.setColor(g2d, AlgoVisHelper.White);
            AlgoVisHelper.drawTextCenterAlligned(g2d, "Score: " + movingBall.getScore(), canvasWidth / 2, canvasHeight - 10);
            AlgoVisHelper.drawTextCenterAlligned(g2d, "Velocity: ", canvasWidth / 16, 10);
            AlgoVisHelper.drawTextCenterAlligned(g2d, "vX: " + Math.round(movingBall.getVx() * AlgoVisualizer.PAUSE), canvasWidth / 16, 20);
            AlgoVisHelper.drawTextCenterAlligned(g2d, "vY: " + -Math.round(movingBall.getVy() * AlgoVisualizer.PAUSE), canvasWidth / 16, 30);
            AlgoVisHelper.drawTextCenterAlligned(g2d, "Position: ", canvasWidth / 16 * 15, 10);
            AlgoVisHelper.drawTextCenterAlligned(g2d, "X: " + Math.round(movingBall.getX()), canvasWidth / 16 * 15, 20);
            AlgoVisHelper.drawTextCenterAlligned(g2d, "Y: " + -Math.round(movingBall.getY()), canvasWidth / 16 * 15, 30);
            AlgoVisHelper.drawTextCenterAlligned(g2d, "Time: ", canvasWidth / 2, 10);
            AlgoVisHelper.drawTextCenterAlligned(g2d, Timer.toFormattedString(timer.getSecondsElapsed()), canvasWidth / 2, 25);
        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(canvasWidth, canvasHeight);
        }
    }
}

