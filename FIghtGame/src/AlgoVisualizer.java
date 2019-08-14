import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AlgoVisualizer {

    // TODO: 创建自己的数据
    private Character me,enemy;        // 数据
    private static boolean isRunning = false; //暂停或运行
    private static boolean outcomePage = false; //结果页面
    private static int level = 1;
    private AlgoFrame frame;    // 视图

    public static boolean getIsRunning() {return isRunning;}
    public static boolean getOutcomePage() {return outcomePage;}
    public static int getLevel() {return level;}

    public AlgoVisualizer(int sceneWidth, int sceneHeight, Character enemy, Character me){

        // 初始化数据
        // TODO: 初始化数据
        this.me = me;
        this.enemy = enemy;

        // 初始化视图
        EventQueue.invokeLater(() -> {
            frame = new AlgoFrame("狂扁" + enemy.getName(), sceneWidth, sceneHeight);
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
            AlgoVisHelper.pause(20);

            if (isRunning)
            {
                if (Math.random() < 0.1)
                    enemy.attacks(me,15);

                if (enemy.getCurrentHP() != 0 & Math.random() < 0.05)
                    enemy.selfHealsByAmount(Math.min(5 * (level / 3), 10));

                if (enemy.getCurrentHP() <= 0 | me.getCurrentHP() <= 0)
                {
                    outcomePage = true;
                    isRunning = false;
                }

            }

            frame.render(me, enemy);

        }
    }

    // TODO: 根据情况决定是否实现键盘鼠标等交互事件监听器类
    private class AlgoKeyListener extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent event)
        {
            if (event.getKeyChar() == ' ' & !outcomePage)
                isRunning = !isRunning;
            if (event.getKeyChar() == 'j' & isRunning){
                me.attacks(enemy, 15 + (int)(Math.random() * 10 - 5));
                int n = (int) (Math.random() * 4);
                enemy.changeSide(n);
                me.changeSide(n);
            }
            if (event.getKeyChar() == 'q' & isRunning)
                me.kills(enemy);
            if (event.getKeyChar() == 'h' & isRunning)
                me.selfHealsByAmount(10);
            if (event.getKeyChar() == 'w' & isRunning)
                me.selfHeals();
            if (event.getKeyChar() == 'r' & outcomePage)
            {
                isRunning =false;
                if (enemy.getCurrentHP() <= 0)
                {
                    level++;
                    if (level > Level.ENEMY_DEFENSE.length) {
                        level = 1;
                    }

                    enemy.setDefence(Level.ENEMY_DEFENSE[level - 1]);
                    enemy.setOffence(Level.ENEMY_OFFENSE[level - 1]);
                    enemy.setTotalHP(Level.ENEMY_HP[level - 1]);
                }
                me.selfHeals();
                enemy.selfHeals();
                outcomePage = false;
            }
        }
    }

    private class AlgoMouseListener extends MouseAdapter{ }

    public static void main(String[] args) {

        int sceneWidth = 400;
        int sceneHeight = 600;

        Character enemy = Initialization.initializeEnemy();
        Character me = Initialization.initializeMe();

         String s = "大白猪";
         // s = JOptionPane.showInputDialog("输入你最讨厌的人的名字:");
         enemy.changeName(s);
        // TODO: 根据需要设置其他参数，初始化visualizer
        AlgoVisualizer visualizer = new AlgoVisualizer(sceneWidth, sceneHeight, enemy, me);
    }
}
