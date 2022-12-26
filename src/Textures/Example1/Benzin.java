package Textures.Example1;

public class Benzin {
    long start, lifeTime;
    public double x, y;
    boolean create;
    double initX, initY;
    private final String firstPic="full.png";
    public Benzin(){
    }
    public Benzin( double x, double y, int lifetime){
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


