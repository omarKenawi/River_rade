package Textures.Example1;

import Textures.AnimListener;
import Textures.TextureReader;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;



public class AnimGLEventListener extends AnimListener {
    private static final long createEnemies = 3000;
    private static long lastEneny = 0;
    private static final long createEnemies1 = 500;
    private static long lastEneny1 = 0;
    //-----------------------------------------bullet--------------------------------------//
    private final int bulletIndex = 4;
    //-----------------------------------------generate--------------------------------------//
    private final int generateIndex = 5;
    //-----------------------------------------listener handle-----------------------------------//
    public BitSet keyBits = new BitSet(256);
    int counter = 0;
    plane1 plane = new plane1();
    Enemies entity = new Enemies();
    //--------------------------------------------------------------------------------------//
    int maxWidth = 110;
    int maxHeight = 110;
    int stop1 = 0;
    double[] ybr = {0.7,0.625,0.55,0.475,0.4,0.325,0.25,0.175,0.1,0.025,-0.05,-0.125,-0.2,-0.275,-0.35,-0.425,-0.5,-0.575,-0.65,-0.725};
    double[] ybc = {0.7,0.625,0.55,0.475,0.4,0.325,0.25,0.175,0.1,0.025,-0.05,-0.125,-0.2,-0.275,-0.35,-0.425,-0.5,-0.575,-0.65,-0.725};
    double[] xbr = {0.54,0.6,0.53,0.55,0.53,0.57,0.6,0.56,0.59,0.54,0.55,0.58,0.55,0.6,0.59,0.53,0.54,0.58,0.53,0.6};
    double[] xbl = {-0.55,-0.55,-0.53,-0.6,-0.56,-0.6,-0.57,-0.55,-0.59,-0.56,-0.54,-0.6,-0.57,-0.53,-0.55,-0.6,-0.6,-0.57,-0.53,-0.55};
    String[] textureNames = {plane.getFirstPic(), plane.getSecendPic(), plane.getTriedPic(), plane.getPlaneBoomed(), plane.getBulletPic(), entity.getFirstPic(), entity.getSecendPic(),"block.png"};
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];
    ArrayList<Enemies> Enemies = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();
    private long lastBulletFired = 0;
    private long fireRate = 500;
    private double planeXposition = maxWidth / 2;
    private double planeYposition = 10;
    private int animationIndex = 0;
    private double rightXPlaneBoundry = 100, leftXPlaneBoundry = 0;

    int counter2=1;
    int counter1=100;
    int counter3=1;
    public AnimGLEventListener() {

    }

    //            main method
    public static void main(String[] args) {

    }

    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        //This Will Clear The Background Color To Blue
        gl.glClearColor(0.0f, 0.5f, 0.9f, 0.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);


        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(), GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE, texture[i].getPixels() // Imagedata
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }


    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();


        moveEnemies();
        moveBullets();
        handleKeyPress();
        drowPlane(gl, planeXposition, planeYposition, animationIndex);
        //        drowEnemies(gl);

        CreateEnemies(gl);
        generateBullets(gl);
        resolveBulletCollision(gl);
        resolvePlaneCollision(gl);
        removeEnemies();
        removeBullets();
        drawMap(gl);
//        if (Enemies.size()>0)
//        System.out.println(Enemies.get(0).x+"    "+Enemies.get(0).y);

//        counter1++;
//        counter2++;
//        counter3++;
    }

    private void blocksCollesion(int i)
    {
        if((transXcoordinates(planeXposition) >= xbr[i]-0.09 && transYcoordinates(planeYposition) >= ybr[i] && transYcoordinates(planeYposition) <= ybr[i]+0.3) || (transXcoordinates(planeXposition) <= xbl[i]+0.09 && transYcoordinates(planeYposition) >= ybr[i] && transYcoordinates(planeYposition) <= ybr[i]+0.3))
        {
            planeXposition = maxWidth / 2;
            planeYposition = 10;
        }
    }

    private void generateBlocks(int i)
    {
        if (ybr[i] < -0.75)
        {
            ybr[i] = 0.7;
            xbr[i] = getRandomNumber(0.52,0.6);
            xbl[i] = getRandomNumber(-0.52,-0.6);

        }
        if (ybc[i] < -1.9)

            ybc[i] = 0.7;
    }

    private void blocksSpeed(int i , double speed)
    {
        ybr[i] -= speed;
        ybc[i] -= speed;
    }

    private void drawMap(GL gl) {
        for (int i = 0; i <= xbr.length - 1; i++) {
            drawblocks(gl, xbr[i], ybr[i], 0.6f);
            drawLeftBlocks(gl, xbl[i], ybr[i], 0.6f);
            blocksSpeed(i, 0.0075);
            generateBlocks(i);
            blocksCollesion(i);

            if(ybr[i]>=0.575&& counter2>=(int) ((Math.random()*400) + 700) ){
                xbr[i] = 0.03;

                counter2=0;
            }

            if( ybr[i]>=0.6 && counter1>=(int) ((Math.random()*400) + 500)){
                xbl[i]=-0.03;
                counter1=100;
            }
//            drawCenterBlocks(gl,xbl[i]+0.4,xbr[i]-0.1,ybc[i]+1,1);

        }

    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    private void moveEnemies() {
        for (int i = 0; i < Enemies.size(); i++) {
            Enemies.get(i).y -= 1;

        }


    }

    private void removeEnemies() {
        Iterator itr = Enemies.iterator();

        while (itr.hasNext()) {
            Enemies b = (Enemies) itr.next();
            if (!b.create)
                itr.remove();
        }
    }


    //                       drow methods
    private void CreateEnemies(GL gl) {
        if (lastEneny + createEnemies < System.currentTimeMillis()) {
            lastEneny = System.currentTimeMillis();
            Enemies.add(new Enemies(10+((int)(Math.random() * 80)), 0, 2*2600));
        }
        for (Enemies enemies : Enemies) {
            enemies.validate();
            drawSprite(gl, enemies.x, enemies.y, (int) Math.random() + 5, 1);
        }


    }

    private void drowPlane(GL gl, double x, double y, int index) {
        drawSprite(gl, x, y, index, 1);
    }

    public void drawSprite(GL gl, double x, double y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    private void moveBullets() {
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).y += 1.4;
        }
    }

    private void removeBullets() {
        Iterator itr = bullets.iterator();

        while (itr.hasNext()) {
            Bullet b = (Bullet) itr.next();
            if (!b.fired)
                itr.remove();
        }
    }

    private void generateBullets(GL gl) {
        for (Bullet bullet : bullets) {
            bullet.invalidate();
            drowBullet(gl, bullet.x, bullet.y, 1);
        }

    }


    private void drowBullet(GL gl, double x, double y, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[bulletIndex]);    // Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated((x) / (maxWidth / 2.0) - 0.9, (y) / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.02 * scale, 0.02 * scale, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);

    }

    //collision between bullet and ship
    private void resolveBulletCollision(GL gl) {
        outer:
        for (Enemies Enemies : Enemies) {
            for (Bullet bullet : bullets) {

                if (((int) (bullet.x) >= (int) (Enemies.x - 0.3) && (int) (bullet.x) <= (int) (Enemies.x + 0.3)) && ((int) (bullet.y) >= (int) (Enemies.y - 0.1) && (int) (bullet.y) <= (int) (Enemies.y + 0.1))) {
                    Enemies.create = false;
                    bullet.fired = false;
                    for (int j = 0; j < 100; j++) {
                        drawSprite(gl, Enemies.x, Enemies.y, 3, 1);
                    }
                    //score+=10;

                    break outer;
                }
            }
        }
    }

    private void resolvePlaneCollision(GL gl) {
        for (Enemies Enemies : Enemies) {
            if ((Enemies.y <  planeYposition + 4 && Enemies.y >=  planeYposition - 4 &&Enemies.x <  planeXposition + 4 &&Enemies.x >=  planeXposition - 4)
                    ||planeYposition+4==Enemies.y&&Enemies.x <=  (planeXposition + 9) &&Enemies.x >=  (planeXposition - 9)
            )
            {
                System.out.println("GameOver");
                JOptionPane.showMessageDialog(null, "GameOver.", "GameOver",
                        JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }
        }


    }
    private void drawblocks(GL gl, double x, double y, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textureNames.length-1]);    // Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x, y, 0);
        gl.glScaled(  scale,  scale, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
//        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f((float)x,(float) y, -1.0f);
//        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f((float)(x+(50-x)), (float) y, -1.0f);
//        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f((float)(x+(50-x)), (float)(y+0.2), -1.0f);
//        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f((float)x, (float)(y+0.2), -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }
    public double getRandomNumber(double min, double max) {
        return  ((Math.random() * (max - min)) + min);
    }
    private void drawLeftBlocks(GL gl, double x, double y, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textureNames.length-1]);    // Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x, y, 0);
        gl.glScaled(  scale,  scale, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
//        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f((float)(x-2),(float) y, -1.0f);
//        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f((float)x, (float) y, -1.0f);
//        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f((float)x, (float)(y+0.2), -1.0f);
//        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f((float)(x-2), (float)(y+0.2), -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }
    private void drawCenterBlocks(GL gl, double xl,double xr, double y, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textureNames.length-1]);    // Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(xl, y, 0);
        gl.glScaled(  scale,  scale, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glVertex3f((float)xr, (float) y, -1.0f);
        gl.glVertex3f((float)xl,(float) y, -1.0f);
        gl.glVertex3f((float)xl, (float)(y+0.15), -1.0f);
        gl.glVertex3f((float)xr, (float)(y+0.15), -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }
    public double transXcoordinates(double x)
    {
        if (x >= 50 && x<= 100)
        {
            return (x/100)-0.5;
        }
        return -0.5+(x/100);
    }
    public double transYcoordinates(double y)
    {
        if (y >= 50 && y<= 100)
        {
            return (y/100)-0.5;
        }
        return -0.5+(y/100);

    }

    // handel palne movement

    public void handleKeyPress() {
        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            animationIndex = 2;
            if (planeXposition > leftXPlaneBoundry)
                planeXposition -= plane.getPlaneSpeed();
            if (planeXposition<leftXPlaneBoundry)
                planeXposition=leftXPlaneBoundry;
        } else if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            animationIndex = 1;
            if (planeXposition < rightXPlaneBoundry)
                planeXposition += plane.getPlaneSpeed();
            if (planeXposition> rightXPlaneBoundry)
                planeXposition = rightXPlaneBoundry;
        }

        if (isKeyPressed(KeyEvent.VK_SPACE)) {
            if (lastBulletFired + fireRate < System.currentTimeMillis()) {
                lastBulletFired = System.currentTimeMillis();
                bullets.add(new Bullet(planeXposition, planeYposition, 1500));
            }
        }
    }

    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);
        System.out.println("plane x pos "+planeXposition+" plane Y pos "+planeYposition);
                if (Enemies.size()>0)
        System.out.println("enemy x pos "+Enemies.get(0).x+"    "+" enemy y pos "+Enemies.get(0).y);

    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
        animationIndex = 0;
    }

    @Override
    public void keyTyped(final KeyEvent event) {
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }
}