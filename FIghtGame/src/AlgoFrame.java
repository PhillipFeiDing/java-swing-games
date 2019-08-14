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
    private Character me, enemy;
    public void render(Character me, Character enemy){
        this.me = me;
        this.enemy = enemy;
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
            AlgoVisHelper.setColor(g2d, AlgoVisHelper.White);
            AlgoVisHelper.fillRectangle(g2d, 0,0, canvasWidth, canvasHeight);

            if (AlgoVisualizer.getOutcomePage() == false)
            {
                //血条
                AlgoVisHelper.setColor(g2d, AlgoVisHelper.Black);
                g2d.drawString("你的HP:", 10, 50);
                g2d.drawString(enemy.getName() + "的HP:", 10, 110);
                AlgoVisHelper.setColor(g2d, AlgoVisHelper.Blue);
                AlgoVisHelper.strokeRectangle(g2d, 10, 60, me.getTotalHP(), 20);
                AlgoVisHelper.fillRectangle(g2d, 10, 60, me.getCurrentHP(), 20);
                AlgoVisHelper.setColor(g2d, AlgoVisHelper.Red);
                AlgoVisHelper.strokeRectangle(g2d, 10, 120, enemy.getTotalHP(), 20);
                AlgoVisHelper.fillRectangle(g2d, 10, 120, enemy.getCurrentHP(), 20);

                // 界面
                AlgoVisHelper.setColor(g2d, AlgoVisHelper.Black);
                if (AlgoVisualizer.getIsRunning() == false)
                {
                    g2d.drawString("按空格键开始／继续游戏。", 10, 20);
                }
                else
                {
                    g2d.drawString("按空格键暂停。", 10, 20);
                }
                g2d.drawString("j键攻击，h键回血。", 280, 190);

                // 画猪和拳头
                int n;
                if (enemy.getTime() < enemy.REACTION_LAG / 2) {n = enemy.getSide();}
                else if (enemy.getTime() < enemy.REACTION_LAG) {n = 4;}
                else {n = 5;}
                    AlgoVisHelper.putImage(g2d, enemy.imagePositions[n][0], enemy.imagePositions[n][1], enemy.getImage(n));

                if (n <= 3)
                    AlgoVisHelper.putImage(g2d, me.imagePositions[n][0], me.imagePositions[n][1], me.getImage(n));

            }
            else
            {
                AlgoVisHelper.setColor(g2d, AlgoVisHelper.Black);
                if (enemy.getCurrentHP() <= 0)
                {
                    g2d.drawString("恭喜你打爆了" + enemy.getName() + "的猪头。", 10, 50);
                    if (AlgoVisualizer.getLevel() < Level.ENEMY_DEFENSE.length)
                        g2d.drawString("按r建进入第" + (AlgoVisualizer.getLevel() + 1) + "关。", 10, 70);
                    else
                        g2d.drawString("按r建重开始新一轮挑战。", 10, 70);
                    AlgoVisHelper.putImage(g2d, enemy.imagePositions[2][0], enemy.imagePositions[2][1], enemy.getImage(2));
                    // AlgoVisHelper.putImage(g2d, canvasWidth / 2 - 148 / 2, 300, me.getImage(2));
                }
                else
                    if (me.getCurrentHP() <= 0)
                    {
                        g2d.drawString("你挂了。按r建再次向TA发起挑战。", 10, 50);
                        AlgoVisHelper.putImage(g2d, enemy.imagePositions[5][0], enemy.imagePositions[5][1], enemy.getImage(5));
                        g2d.drawString("哈哈，你个菜逼～～", canvasWidth / 2 - 148 / 2, 460);
                    }
                        else
                        {g2d.drawString("按r键即可开始新游戏。", 10, 50);}
            }

            g2d.drawString("第" + AlgoVisualizer.getLevel() + "关", 350, 20);
        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(canvasWidth, canvasHeight);
        }
    }
}

