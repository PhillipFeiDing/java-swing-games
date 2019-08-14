public class Character
{
    public static final int DEFAULT_HP = 100;
    public static final int DEFAULT_OFFENCE = 100;
    public static final int DEFAULT_DEFENCE = 100;

    public final int REACTION_LAG = 500;
    public int[][] imagePositions;

    private String name;
    private int totalHP,currentHP;
    private int offence,defence;
    private String[] imageFileNames;

    private Timer timer;
    private int side = 0;

    // the Constructors
    public Character(String name, int totalHP, int offence, int defence, String[] imageFileNames, int[][] imagePositions) {
        this.name = name;
        this.totalHP = totalHP;
        this.currentHP = this.totalHP;
        this.offence = offence;
        this.defence = defence;
        this.imageFileNames = imageFileNames;
        this.imagePositions = imagePositions;
        this.timer = new Timer();
        timer.zero(); timer.start();
    }

    // the Accessors
    public int getTotalHP() {return totalHP;}
    public int getCurrentHP() {return currentHP;}
    public int getOffence() {return offence;}
    public int getDefence() {return defence;}
    public String getName() {return name;}
    public int getSide() {return side;}
    public String getImage(int index) {
        if (index >=0 && index < imageFileNames.length){
            return imageFileNames[index];
        }
        else throw new IllegalArgumentException("image request in array out of bound!");
    }

    public int getTime() {return timer.getMilisecondsElapsed();}

    // the Mutators
    public void setTotalHP(int totalHP) {this.totalHP = totalHP; checkHP();}
    public void selfHealsByAmount(int amount) {currentHP += amount; checkHP();}
    public void isHurtByAmount(int amount) {
        currentHP -= amount; checkHP();
        timer.zero();
        timer.start();
    }
    public void selfHeals() {currentHP = totalHP;}
    public void setOffence(int offence) {this.offence = offence;}
    public void setDefence(int defence) {this.defence = defence; checkDefence();}
    public void changeName(String s) {this.name = s;}
    public void changeSide(int n) {this.side = n;}
    private void checkHP()
    {
        currentHP = Math.max(0, currentHP);
        currentHP = Math.min(totalHP, currentHP);
    }
    private void checkDefence() {defence = Math.max(1, defence);}

    // the Interactors
    public void healsByAmount(Character enemy, int amount) {enemy.selfHealsByAmount(amount);}
    public void heals(Character enemy) {enemy.selfHeals();}
    public void attacksByAmount(Character enemy, int amount)
    {
        if (currentHP > 0)
            enemy.isHurtByAmount(amount);
    }
    public void kills(Character enemy)
    {
        if (currentHP > 0)
            enemy.isHurtByAmount(enemy.getCurrentHP());
    }
    public void attacks(Character enemy, int amount)
    {
        if (currentHP > 0)
            enemy.isHurtByAmount
                    ( (int) Math.round( (double)(amount * offence) / enemy.getDefence() ) );
    }
}