package Textures.Example1;

import Textures.AnimListener;
import Textures.TextureReader;
import com.sun.opengl.util.GLUT;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

public class AnimGLEventListener extends AnimListener {
    private static final long createEnemies = 1000;
    private static final long createBenzine = 5357;
    private Timer Timer;
    private long counter=0;

    private static long lastBenzine = 0;
    private static long lastEnemy = 0;
    //-----------------------------------------generate--------------------------------------//
    //-----------------------------------------listener handle-----------------------------------//
    public BitSet keyBits = new BitSet(256);
    Benzin ben = new Benzin();
    plane1 plane = new plane1();
    int tank = plane.getMaxFuel();
    boolean isExist = plane.isExist();
    Enemies entity = new Enemies();
    GLUT g = new GLUT();
    //--------------------------------------------------------------------------------------//
    int maxWidth = 110;
    int maxHeight = 110;
    double[] ybr = {0.7, 0.625, 0.55, 0.475, 0.4, 0.325, 0.25, 0.175, 0.1, 0.025, -0.05, -0.125, -0.2, -0.275, -0.35, -0.425, -0.5, -0.575, -0.65, -0.725};
    double[] ybc = {0.7, 0.625, 0.55, 0.475, 0.4, 0.325, 0.25, 0.175, 0.1, 0.025, -0.05, -0.125, -0.2, -0.275, -0.35, -0.425, -0.5, -0.575, -0.65, -0.725};
    double[] xbr = {0.54, 0.6, 0.53, 0.55, 0.53, 0.57, 0.6, 0.56, 0.59, 0.54, 0.55, 0.58, 0.55, 0.6, 0.59, 0.53, 0.54, 0.58, 0.53, 0.6};
    double[] xbl = {-0.55, -0.55, -0.53, -0.6, -0.56, -0.6, -0.57, -0.55, -0.59, -0.56, -0.54, -0.6, -0.57, -0.53, -0.55, -0.6, -0.6, -0.57, -0.53, -0.55};
    String[] textureNames = {plane.getFirstPic(), plane.getSecendPic(), plane.getTriedPic(), plane.getPlaneBoomed(), plane.getBulletPic(), entity.getFirstPic(), entity.getSecendPic(), "block.png", ben.getFirstPic()};
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];
    ArrayList<Enemies> Enemies = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<Benzin> benzine = new ArrayList<>();
    int counter2 = 1;
    int counter1 = 100;
//    File gameBegan = new File("Assets/dramatic-reveal-21469.wav");
//    File gamePlay = new File("Assets/commercial-aircraft-in-flight-announcement-5-17499.wav");
//    File shot = new File("Assets/laser-zap-90575.wav");
//    File hit = new File("Assets/explosion-6055.wav");
//    File Crash = new File("Assets/crash-7075.wav");
//    File gameOver = new File("Assets/mixkit-sad-game-over-trombone-471.wav");
//    AudioInputStream gameBeganAudioStream = AudioSystem.getAudioInputStream(gameBegan);
//    AudioInputStream gamePlayAudioStream = AudioSystem.getAudioInputStream(gamePlay);
//    AudioInputStream shotAudioStream = AudioSystem.getAudioInputStream(shot);
//    AudioInputStream hitAudioStream = AudioSystem.getAudioInputStream(hit);
//    AudioInputStream crashAudioStream = AudioSystem.getAudioInputStream(Crash);
//    AudioInputStream gameOverAudioStream = AudioSystem.getAudioInputStream(gameOver);
//    Clip gameBeganClip = AudioSystem.getClip();
//    Clip gamePlayClip = AudioSystem.getClip();
//    Clip chotClip = AudioSystem.getClip();
//    Clip shotClip = AudioSystem.getClip();
//    Clip clipHit = AudioSystem.getClip();
//    Clip gameOverClip = AudioSystem.getClip();
    private int score = 0;
    private long lastBulletFired = 0;
    private double planeXPosition = maxWidth / 2.0;
    private final double planeYPosition = 10;
    private int animationIndex = 0;


    public AnimGLEventListener() {
        Open("crash-7075.wav");
        Open("commercial-aircraft-in-flight-announcement-5-17499.wav");
        Open("laser-zap-90575.wav");
        Open("crash-7075.wav");
        Open("explosion-6055.wav");
        Open("mixkit-sad-game-over-trombone-471.wav");
    }

    //            main method
    public static void main(String[] args) {

    }
    public void Sound(String name) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream input = AudioSystem.getAudioInputStream(new File(assetsFolderName + "//" + name).getAbsoluteFile());
            clip.open(input);
            clip.start();
            clip.setMicrosecondPosition(0);
            if(name.endsWith("blaster-2-81267.wav")){
                clip.loop(0);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
    public void Open(String name){
        try {
            File A=new File(assetsFolderName + "//" + name);
            AudioInputStream B = AudioSystem.getAudioInputStream(A);
            Clip p=AudioSystem.getClip();
            p.open(B);
            if(name.equalsIgnoreCase("commercial-aircraft-in-flight-announcement-5-17499.wav")){
                p.start();
                p.loop(1);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public void init(GLAutoDrawable gld) {
newGame();
        GL gl = gld.getGL();
        //This Will Clear The Background Color To Blue
        gl .glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);


        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(), GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE, texture[i].getPixels() // Imagedata
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(0.0f, 0.5f, 0.9f, 0.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();
        moveEnemies();
        moveBullets();
        moveBenzine();
        handleKeyPress();
        drawPlane(gl, planeXPosition, animationIndex);
        CreateEnemies(gl);
        Benzine(gl);
        burningFuel();
        generateBullets(gl);
        resolveBulletCollision(gl);
        resolvePlaneCollision();
        resolveBenzineCollision();
        removeEnemies();
        removeBullets();
        removeBenzine();
        drawMap(gl);
        displayVar(g,gld);
        distance();
        EndGame();

    }
    private void displayVar(GLUT g,GLAutoDrawable gld){
        GL gl2 = gld.getGL();
        gl2 .glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        gl2.glRasterPos2f(-.8f, .9f);
        g.glutBitmapString(5, "Score ");
        g.glutBitmapString(5, Integer.toString(score));
        gl2.glRasterPos2f(-.8f, .8f);
        g.glutBitmapString(5, "tank  ");

        g.glutBitmapString(5, Integer.toString(tank/10));
        gl2.glRasterPos2f(-.8f, .7f);
        g.glutBitmapString(5, "Timer  ");

        g.glutBitmapString(5, Long.toString(counter));

        gl2.glEnd();


    }

    private void EndGame() {
        if (!isExist) {
            System.out.println("GameOver");
            JOptionPane.showMessageDialog(null, "GameOver.", "GameOver",
                    JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    private void Benzine(GL gl) {
        if (lastBenzine + createBenzine < System.currentTimeMillis()) {
            lastBenzine = System.currentTimeMillis();

            benzine.add(new Benzin(10 + ((int) (Math.random() * 80)), 0, 2 * 2600));
        }
        for (Benzin ben : benzine) {
            ben.validate();
            drawSprite(gl, ben.x, ben.y, 8, 1);
        }


    }

    private void resolveBenzineCollision() {
        for (Benzin ben : benzine) {
            if ((ben.y <= planeYPosition + 3 && ben.y >= planeYPosition - 3 && ben.x < planeXPosition + 9 && ben.x >= planeXPosition - 9)) {
                tank += 30;
                Sound("mixkit-retro-game-notification-212.wav");
                if (tank > plane.getMaxFuel())
                    tank = plane.getMaxFuel();
            }
        }

    }

    private void removeBenzine() {

        benzine.removeIf(c -> !c.create);
    }

    private void moveBenzine() {
        for (Benzin benzin : benzine) {
            benzin.y -= 1;

        }
    }

    private void burningFuel() {
        if (tank > 0)
            tank--;
        if (tank <= 0)
            isExist = false;
    }


    private void blocksCollision(int i) {
        if ((transXcoordinates(planeXPosition) >= xbr[i] - 0.09 && transYcoordinates(planeYPosition) >= ybr[i] && transYcoordinates(planeYPosition) <= ybr[i] + 0.3) || (transXcoordinates(planeXPosition) <= xbl[i] + 0.09 && transYcoordinates(planeYPosition) >= ybr[i] && transYcoordinates(planeYPosition) <= ybr[i] + 0.3)) {
            {
                Sound("mixkit-sad-game-over-trombone-471.wav");
                isExist = false;

                System.exit(0);
            }

        }
    }



    private void generateBlocks(int i) {
        if (ybr[i] < -0.75) {
            ybr[i] = 0.7;
            xbr[i] = getRandomNumber(0.52, 0.6);
            xbl[i] = getRandomNumber(-0.52, -0.6);

        }
        if (ybc[i] < -1.9)

            ybc[i] = 0.7;
    }

    private void blocksSpeed(int i) {
        ybr[i] -= 0.0075;
        ybc[i] -= 0.0075;
    }

    private void drawMap(GL gl) {
        for (int i = 0; i <= xbr.length - 1; i++) {
            drawBlocks(gl, xbr[i], ybr[i]);
            drawLeftBlocks(gl, xbl[i], ybr[i]);
            blocksSpeed(i);
            generateBlocks(i);
            blocksCollision(i);

            if (ybr[i] >= 0.575 && counter2 >= (int) ((Math.random() * 400) + 700)) {
                xbr[i] = 0.03;

                counter2 = 0;
            }

            if (ybr[i] >= 0.6 && counter1 >= (int) ((Math.random() * 400) + 500)) {
                xbl[i] = -0.03;
                counter1 = 100;
            }
//            drawCenterBlocks(gl,xbl[i]+0.4,xbr[i]-0.1,ybc[i]+1,1);

        }

    }
    public void newGame() {
        Timer = new Timer(900, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //start counter in text field
                counter++;
                System.out.println(counter);

            }
        });
        Timer.start();
    }
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    private void moveEnemies() {
        for (Textures.Example1.Enemies enemy : Enemies) {
            enemy.y -= 1;
        }


    }

    private void removeEnemies() {
        Enemies.removeIf(b -> !b.create);
    }


    //                       drow methods
    private void CreateEnemies(GL gl) {
        if (lastEnemy + createEnemies < System.currentTimeMillis()) {
            lastEnemy = System.currentTimeMillis();
            Enemies.add(new Enemies(10 + ((int) (Math.random() * 80)), 0, 2 * 2600));
        }
        for (Enemies enemies : Enemies) {
            enemies.validate();
            int direction = (int) ( Math.random() * 2 +1);
            if (direction==1&&enemies.y==110) {
                enemies.isRight = true;
            } else if (direction==2&&enemies.y==110) {
                enemies.isRight = false;
            }
            drawEnemies(gl, enemies.x, enemies.y, 6, 1,enemies.isRight);

        }


    }
    public void drawEnemies(GL gl, double x, double y, int index, float scale,boolean right) {
        if (right)
            if (index==5)
               index++;
        else if (index==6)
            index--;
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        gl.glBegin(GL.GL_QUADS);
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

    private void drawPlane(GL gl, double x, int index) {
        drawSprite(gl, x, 10.0, index, 1);
    }

    public void drawSprite(GL gl, double x, double y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        gl.glBegin(GL.GL_QUADS);
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
        for (Bullet bullet : bullets) {
            bullet.y += 1.4;
        }
    }

    private void removeBullets() {
        bullets.removeIf(b -> !b.fired);
    }

    private void generateBullets(GL gl) {
        for (Bullet bullet : bullets) {
            bullet.invalidate();
            drowBullet(gl, bullet.x, bullet.y);
        }

    }


    private void drowBullet(GL gl, double x, double y) {
        gl.glEnable(GL.GL_BLEND);
        //-----------------------------------------bullet--------------------------------------//
        int bulletIndex = 4;
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[bulletIndex]);    // Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated((x) / (maxWidth / 2.0) - 0.9, (y) / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.02 * (float) 1, 0.02 * (float) 1, 1);
        gl.glBegin(GL.GL_QUADS);
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

    private double zone(double x, double y, double a, double b) {
        return Math.sqrt(Math.pow((x - a), 2) + Math.pow((y - b), 2));
    }

    //collision between bullet and ship
    private void resolveBulletCollision(GL gl) {
        outer:
        for (Enemies Enemies : Enemies) {
            for (Bullet bullet : bullets) {
                if ((bullet.x >= (Enemies.x - 9) && (bullet.x) <= (Enemies.x + 8)) && ((bullet.y >= (Enemies.y - 1) && (bullet.y) <= (Enemies.y + 2)))) {
                    Sound("explosion-6055.wav");

                    Enemies.create = false;
                    bullet.fired = false;
                    drawSprite(gl, Enemies.x, Enemies.y, 3, 1.5f);
                    score += 10;
                    break outer;
                }
            }
        }

        for (Benzin fuel : benzine) {
            for (Bullet bullet : bullets) {
                if ((bullet.x >= (fuel.x - 3) && (bullet.x) <= (fuel.x + 3)) && ((bullet.y >= (fuel.y - 1) && (bullet.y) <= (fuel.y + 2)))) {
                    fuel.create = false;
                    bullet.fired = false;
                    Sound("explosion-6055.wav");
                    drawSprite(gl, fuel.x, fuel.y, 3, 3f);
                    for (Enemies Enemies : Enemies) {
                        if (zone(Enemies.x, Enemies.y, fuel.x, fuel.y) <= 30.0) {
                            Enemies.create = false;
                            score += 20;
                            drawSprite(gl, Enemies.x, Enemies.y, 3, 1.5f);
                        }

                    }


                }
            }


        }

    }
    private void resolvePlaneCollision() {
        for (Enemies Enemies : Enemies) {
            if ((Enemies.y < planeYPosition + 4 && Enemies.y >= planeYPosition - 4 && Enemies.x < planeXPosition + 4 && Enemies.x >= planeXPosition - 4)
                    || planeYPosition + 4 == Enemies.y && Enemies.x <= (planeXPosition + 9) && Enemies.x >= (planeXPosition - 9)
            ) {

                Sound("mixkit-sad-game-over-trombone-471.wav");
                isExist = false;
            }
        }


    }

    private void drawBlocks(GL gl, double x, double y) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textureNames.length - 2]);    // Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x, y, 0);
        gl.glScaled((float) 0.6, (float) 0.6, 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex3f((float) x, (float) y, -1.0f);
        gl.glVertex3f((float) (x + (50 - x)), (float) y, -1.0f);
        gl.glVertex3f((float) (x + (50 - x)), (float) (y + 0.2), -1.0f);
        gl.glVertex3f((float) x, (float) (y + 0.2), -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public double getRandomNumber(double min, double max) {
        return ((Math.random() * (max - min)) + min);
    }

    private void drawLeftBlocks(GL gl, double x, double y) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textureNames.length - 2]);    // Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x, y, 0);
        gl.glScaled((float) 0.6, (float) 0.6, 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex3f((float) (x - 2), (float) y, -1.0f);
        gl.glVertex3f((float) x, (float) y, -1.0f);
        gl.glVertex3f((float) x, (float) (y + 0.2), -1.0f);
        gl.glVertex3f((float) (x - 2), (float) (y + 0.2), -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public double transXcoordinates(double x) {
        if (x >= 50 && x <= 100) {
            return (x / 100) - 0.5;
        }
        return -0.5 + (x / 100);
    }

    public double transYcoordinates(double y) {
        if (y >= 50 && y <= 100) {
            return (y / 100) - 0.5;
        }
        return -0.5 + (y / 100);

    }

    // handel palne movement

    public void handleKeyPress() {
        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            animationIndex = 2;
            double leftXPlaneBoundry = 0;
            if (planeXPosition > leftXPlaneBoundry)
                planeXPosition -= plane.getPlaneSpeed();
            if (planeXPosition < leftXPlaneBoundry)
                planeXPosition = leftXPlaneBoundry;
        } else if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            animationIndex = 1;
            double rightXPlaneBoundry = 100;
            if (planeXPosition < rightXPlaneBoundry)
                planeXPosition += plane.getPlaneSpeed();
            if (planeXPosition > rightXPlaneBoundry)
                planeXPosition = rightXPlaneBoundry;
        }
        if (isKeyPressed(KeyEvent.VK_SPACE)) {
            long fireRate = 500;
            if (lastBulletFired + fireRate < System.currentTimeMillis()) {
                lastBulletFired = System.currentTimeMillis();
                bullets.add(new Bullet(planeXPosition, planeYPosition, 1500));
                Sound("GunShotSnglShotIn PE1097906.wav");
            }
        }
    }
    private void distance() {
        for (Enemies Enemies : Enemies) {
            System.out.println("Enemies.x" + Enemies.x);
            if (Enemies.y - planeYPosition < 90) {
                if (Enemies.isRight) {
                    ++Enemies.x;
                    if (Enemies.x == 90) {
                        Enemies.isRight = false;
                    }
                } else {
                    --Enemies.x;
                    if (Enemies.x <= 10) {
                        Enemies.isRight = true;
                    }
                }
            }
        }
    }


    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);
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