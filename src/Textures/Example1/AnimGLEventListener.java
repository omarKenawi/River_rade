package Textures.Example1;

import Textures.AnimListener;
import Textures.TextureReader;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.BitSet;
/*
ali yasser
generateEnemies
drowEnemies
moveEnemies
*/

public class AnimGLEventListener extends AnimListener {

    private static final int DI = 110;
    private static final int DJ = 110;
    private static final int MAX_ENEMIES = 1;
    private final int[][] entity = new int[DI][DJ];
    private final int[][] enemiesDirection = new int[DI][DJ];
    //-----------------------------------------bullet--------------------------------------//
    private final int bulletIndex = 4;
    //-----------------------------------------listener handle-----------------------------------//
    public BitSet keyBits = new BitSet(256);
    double[] bulletX = new double[50];
    double[] bulletY = new double[50];
    int currBullet = 0;

    //--------------------------------------------------------------------------------------//
    int maxWidth = 10;
    int maxHeight = 100;
    int stop1 = 0,stop2 = 0;
    String[] textureNames = {"plane_1.png", "plane_2.png", "plane_3.png", "plane_boom.png", "bullet.png", "boat1.png", "boat2.png"};
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];
    private double planeXposition = maxWidth / 2;
    private double planeYposition = 10;
    private int animationIndex = 0;
    //
    private double rightXPlaneBoundry = 9, leftXPlaneBoundry = .5;

    public AnimGLEventListener() {

    }

    //            main method
    public static void main(String[] args) {
        new Anim(new AnimGLEventListener());


    }

    /*
     5 means gun in array pos
     x and y coordinate for gun
     */
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
//        drowBullets(gl);
        drowPlane(gl, planeXposition, planeYposition, animationIndex);
        //genarate enemies after 1sec
        if (stop1-- == 0)
            generateEnemies();
        drowEnemies(gl);
        for (int i = 0; i < 50; i++) {
            if (bulletX[i] == 0 && bulletY[i] == 0)
                break;
            bulletY[i]++;
            drowBullet(gl, bulletX[i], bulletY[i], 1);
        }
        handleBulletPress();



//        gl.glColor3f(1,1,1);
//        gl.glBegin(GL.GL_POLYGON);
//        gl.glVertex2d(0,0);
//        gl.glVertex2d(0,1);
//        gl.glVertex2d(1,1);
//        gl.glVertex2d(1,0);
//        gl.glEnd();



    }

    private void moveBullets() {
        for (int i = 0; i < 50; i++) {
            if (bulletX[i] == 0 && bulletY[i] == 0)
                break;
            bulletY[i]++;
        }
    }


    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    private void moveEnemies() {
        for (int i = 0; i < DI; i++) {
            for (int j = 0; j < DJ; j++) {
                if (entity[i][j] == 2) {
                    entity[i][j] = 0;
                    entity[i][j - 4] = 2;
                    enemiesDirection[i][j - 4] = enemiesDirection[i][j];
                    enemiesDirection[i][j] = 0;
                }
            }
        }


        for (int j = 0; j < DJ; j++) {
            if (entity[j][0] == 2) {
                entity[j][0] = 0;
            }
        }
    }

    private void generateEnemies() {
        int y = 0;
        int cnt = MAX_ENEMIES;
        while (cnt-- > 0) {
            int x = (int) (Math.random() * 50);
            y = 100;
            entity[x][y] = 2;
            int random = (int) ((Math.random() * 2) + 1);
            enemiesDirection[x][y] = random;
            stop1 = 1;
        }

    }




    private void drowEnemies(GL gl) {

        for (int i = 0; i < DI; i++) {

            for (int j = 0; j < DJ; j++) {

                if (entity[i][j] == 2) {

                    drowSpriteEnemy(gl, i, j);

                }
            }
        }
    }
    //                         drow methods

    private void drowSpriteEnemy(GL gl, int x, int y) {

        drawSprite(gl, x, y, texture.length - enemiesDirection[x][y], 1);

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

    private void drowBullet( GL gl, double x, double y, float scale) {
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
            System.out.println("left");
            animationIndex = 2;
            if (planeXposition > leftXPlaneBoundry)
                planeXposition -= .7;
        } else if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            System.out.println("right");
            System.out.println(planeXposition);
            animationIndex = 1;
            if (planeXposition < rightXPlaneBoundry)
                planeXposition += .7;
            if (planeXposition > 9)
                planeXposition = 9;
        }

        System.out.println(stop2);

    }


    /*
     * KeyListener
     */
    // disable space key for time
    private void handleBulletPress(){
        if (stop2>0)
            stop2-=.1;
//                break;

        System.out.println(stop2);
        if (stop2==0&&isKeyPressed(KeyEvent.VK_SPACE)) {
            bulletX[currBullet] = planeXposition;
            bulletY[currBullet] = planeYposition + 4;
            currBullet++;
            currBullet %= 50;
            stop2=3;
        }

    }
    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
//        if (keyCode==KeyEvent.VK_SPACE)
//            stop2=10;
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
        // don't care
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }
}