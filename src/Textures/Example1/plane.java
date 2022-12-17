package Textures.Example1;

public class plane {
    //x position & y position  & the assets for plane
    public double xPosition ;
    public double yPosition ;
   private final String firstPic="plane_1.png";
   private final String secendPic="plane_2.png";
   private final String triedPic="plane_3.png";

   private final   String planeBoomed="plane_boom.png";

    private final String bulletPic="";

    public plane() {

    }

    public double getxPosition() {
        return xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }

    public String getBulletPic() {
        return bulletPic;
    }

    public String getFirstPic() {
        return firstPic;
    }

    public String getSecendPic() {
        return secendPic;
    }

    public String getTriedPic() {
        return triedPic;
    }
    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }
}
