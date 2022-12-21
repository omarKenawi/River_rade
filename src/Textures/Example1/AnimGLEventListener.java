package Textures.Example1;

import Textures.AnimListener;
import Textures.TextureReader;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
public class AnimGLEventListener extends AnimListener {
    private static  long lastEneny=0;
    private static final long createEnemies=500;
    private long lastBulletFired = 0;
    private long fireRate = 500;
    //-----------------------------------------bullet--------------------------------------//
    private final int bulletIndex = 4;
    //-----------------------------------------generate--------------------------------------//
    private final int generateIndex = 5;

    //-----------------------------------------listener handle-----------------------------------//
    public BitSet keyBits = new BitSet(256);
    plane1 plane = new plane1();
    Enemies entity =new Enemies();
//--------------------------------------------------------------------------------------//

    int maxWidth = 10;
    int maxHeight = 100;
    int stop1 = 0;
    String[] textureNames = {plane.getFirstPic(), plane.getSecendPic(), plane.getTriedPic(), plane.getPlaneBoomed(),plane.getBulletPic(),entity.getFirstPic(),entity.getSecendPic() };
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];
    ArrayList<Enemies> Enemies = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();
    private double planeXposition = maxWidth / 2;
    private double planeYposition = 10;
    private int animationIndex = 0;
    private double rightXPlaneBoundry = 9, leftXPlaneBoundry = 0;

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

        handleKeyPress();
        moveEnemies();
        moveBullets();
        drowPlane(gl, planeXposition, planeYposition, animationIndex);
        //        drowEnemies(gl);

        CreateEnemies(gl);
        generateBullets(gl);
        removeEnemies();
        removeBullets();





        System.out.println(Enemies.size());
        System.out.println(bullets.size());
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    private void moveEnemies() {
        for (int i =0; i<Enemies.size(); i++) {
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
    private void CreateEnemies(GL gl){
        if (lastEneny + createEnemies < System.currentTimeMillis()) {
            lastEneny = System.currentTimeMillis();
            Enemies.add(new Enemies(Math.random()*8,0,1700));
        }
        for (Enemies bullet : Enemies) {
            bullet.validate();
            drawSprite(gl, bullet.x, bullet.y,(int)Math.random()+5, 1);
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
            bullets.get(i).y += 2;
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
    // handel palne movement
    public void handleKeyPress() {
        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            animationIndex = 2;
            if (planeXposition > leftXPlaneBoundry)
                planeXposition -= plane.getPlaneSpeed();
        } else if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            animationIndex = 1;
            if (planeXposition < rightXPlaneBoundry)
                planeXposition += plane.getPlaneSpeed();
            if (planeXposition > 9)
                planeXposition = 9;
        }
        if (isKeyPressed(KeyEvent.VK_SPACE)) {
            if (lastBulletFired + fireRate < System.currentTimeMillis()) {
                lastBulletFired = System.currentTimeMillis();
                bullets.add(new Bullet(planeXposition, planeYposition, 1100));
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

