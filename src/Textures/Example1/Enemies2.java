package Textures.Example1;

public class Enemies2 {
    long start, lifeTime;
    public double x, y;
    boolean create;
    double initX, initY;
    private final String firstPic= "Eplane.png";

    public Enemies2(){
    }
    public Enemies2( double x, double y, int lifetime){
        this.x = initX = x;
        this.y = initY = y+110;
        this.create = true;
        start = System.currentTimeMillis();
        lifeTime = lifetime;
    }
    public void validate(){
        create = start+lifeTime > System.currentTimeMillis();
    }
    public String getFirstPic() {
        return firstPic;
    }
}

