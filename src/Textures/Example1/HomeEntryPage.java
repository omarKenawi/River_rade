package Textures.Example1;

import Textures.AnimListener;

import java.awt.*;
import java.awt.event.*;

import com.sun.opengl.util.FPSAnimator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.media.opengl.*;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//import static GlutObjectsApp.animator;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
//
//import com.jogamp.opengl.*;
//import com.jogamp.opengl.awt.GLCanvas;
//import com.jogamp.opengl.util.FPSAnimator;

public class HomeEntryPage extends AnimListener implements GLEventListener, ActionListener {
   public int count = 0;

    Anim a ;
    AnimGLEventListener ar;

    {

        ar = new AnimGLEventListener();
    }

   String name ="";
    String textureName = "Bground.png";
    Textures.TextureReader.Texture texture;
    int textureIndex[] = new int[1];

    private static final int WINDOW_WIDTH = 1000; //640
    private static final int WINDOW_HEIGHT = 900;

    private static final String TITLE_STRING = "River Raid ";
    private static final int FPS = 60;


    private Timer Timer;


    private JFrame frame;
    private GLCanvas canvas;
    private FPSAnimator animator;
    private JButton startButton;
    private JButton optionsButton;
    private JButton ChooseplaneButton;
    private JButton HelpButton;
    private JButton exitButton;
    private     JButton instraction;
    public HomeEntryPage() {
        // Set up the JFrame
        frame = new JFrame(TITLE_STRING);
//    private final JPanel my_main_panel = new JPanel();
//    JPanel p = new JPanel();
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setBackground(Color.white);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//EXIT_ON_CLOSE
        frame.setLayout(new BorderLayout());

        // Set up the GLCanvas and FPSAnimator
        canvas = new GLCanvas();
        canvas.addGLEventListener(this);
        frame.add(canvas, BorderLayout.CENTER);
        animator = new FPSAnimator(canvas, FPS);

        // Set up the buttons

        instraction  = new JButton ( "instraction" );
        instraction.addActionListener(this);
        startButton = new JButton("Start Game");
        startButton.addActionListener(this);
        optionsButton = new JButton("Levels");
        optionsButton.addActionListener(this);
        ChooseplaneButton = new JButton(" "+ "Choose a plane ");

        ChooseplaneButton.addActionListener(this);
        ChooseplaneButton.setVisible(false);
        HelpButton = new JButton("Info");
        HelpButton.addActionListener(this);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
//    instraction.setBackground(Color.GRAY);
//     startButton .setBackground(Color.white);
//
        buttonPanel.setBackground(Color.BLUE);
        buttonPanel.setSize(530, 450);
        buttonPanel.add(startButton);
        buttonPanel.add(optionsButton);
        buttonPanel.add(ChooseplaneButton);

        buttonPanel.add(HelpButton);
        buttonPanel.add(instraction);
     //   exitButton.setSize(700, 780);
        buttonPanel.add(exitButton);
        // frame.setSize(140, 290);
        frame.add(buttonPanel, BorderLayout.NORTH);
       frame.setLocationRelativeTo(null);
//    getContentPane().add(canvas, BorderLayout.CENTER);
//        getContentPane().add( buttonPanel, BorderLayout.NORTH);

        frame.setIconImage(new ImageIcon(
                "C:\\Users\\Abdou\\Desktop\\icon2.png").getImage());

        // instraction.setVisible(false);
        frame.setVisible(true);
        frame.setSize(900, 1000);
       // setSize();
    }

    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();
        if (source == startButton) {
            // Start the game

              name=JOptionPane.showInputDialog(frame,"Enter Your Name");
            HelpButton.setVisible(false);
            instraction.setVisible(false);
            optionsButton.setVisible(false);
            frame. setVisible(false);

            String s = "HI  "+name+" Are you ready ?";
            int     response = JOptionPane.showConfirmDialog(frame,  s  , "ready?", JOptionPane.YES_NO_OPTION);
            if ( response==JOptionPane.YES_OPTION) {
                Anim f= null;

                    f = new Anim(new AnimGLEventListener( ));

                newGame();
                f.setVisible(true);
                frame. dispose();

            }

            if (response==JOptionPane.NO_OPTION) {
                new HomeEntryPage();
                frame. dispose();
            }

//            Anim f= null;
//            try {
//                f = new Anim(new AnimGLEventListener());
//            } catch (UnsupportedAudioFileException ex) {
//                throw new RuntimeException(ex);
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            } catch (LineUnavailableException ex) {
//                throw new RuntimeException(ex);
//            }
//
//            f.setVisible(true);
//            frame. dispose();

//                  HomeEntryPage h=new HomeEntryPage();
//         canvas.removeGLEventListener(h);
//            canvas.addGLEventListener(ar);
//            canvas.addKeyListener(ar);
//             canvas.requestFocus();
//            setSize(700, 700);
        } else if (source == optionsButton) {
            // Show the options menu
        } else if (source == ChooseplaneButton) {
            // Show the Chooseplane screen

        }
        else if (e.getSource() == instraction) {

            JOptionPane.showMessageDialog(frame,
                    "1.Use the up down left right bottun to move the plane \n"
                            + "2.press Space to shoot bults.\n"
                            +"3.Try to survive and overcome the enemy."
            );

        }
        else if (source == exitButton) {
            //  to Exit the game
            System.exit(0);

        }else if (source == HelpButton) {

            JOptionPane.showMessageDialog(frame,  " River Raid is ..");
        }

    }

    public void init(GLAutoDrawable drawable) {
        // Set up the OpenGL context here

        GL gl = drawable.getGL();
        //  gl.glClearColor(0.0f, 1.0f, 1.0f, 1.0f);

        // gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black
        gl.glClearColor(0.0f, 0.9f, 0.5f, 0.0f); //This Will Clear The Background Color To blue+green


        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        //number of textures, array to hold the indeces
        gl.glGenTextures(1, textureIndex, 0);

        try {
            texture = Textures.TextureReader.readTexture(assetsFolderName + "//" + textureName, true);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[0]);

//          mipmapsFromPNG(gl, new GLU(), texture[i]);
            new GLU().gluBuild2DMipmaps(
                    GL.GL_TEXTURE_2D,
                    GL.GL_RGBA, // Internal Texel Format,
                    texture.getWidth(), texture.getHeight(),
                    GL.GL_RGBA, // External format from image,
                    GL.GL_UNSIGNED_BYTE,
                    texture.getPixels() // Imagedata
            );
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }
    public void mousePressed(MouseEvent e) {

    }


    public void mouseReleased(MouseEvent e) {

    }


    public void mouseEntered(MouseEvent e) {

    }


    public void mouseExited(MouseEvent e) {

    }

//    public static void main(String[] args) {
//        new HomeEntryPage();
//    }


    public void focusGained(FocusEvent e) {

    }


    public void focusLost(FocusEvent e) {

    }

    @Override
    public void display(GLAutoDrawable glad) {

        GL gl = glad.getGL();
      //  gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();
      //  gl.glDisable(GL.GL_TEXTURE_2D);
        DrawBackground(gl);
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {

        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public void displayChanged(GLAutoDrawable glad, boolean bln, boolean bln1) {
        //      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public void DrawBackground(GL gl) {

        gl.glEnable(GL.GL_BLEND);	// Turn Blending On
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[0]);

        //gl.glScaled(0.5, 0.5, 0);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1f, -1f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();

        gl.glDisable(GL.GL_BLEND);
    }
    @Override
    public void keyTyped(KeyEvent e) {
        //      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //write code hire


        //       throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //write code to make color


        //      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public void newGame() {
        Timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //start counter in text field
                count++;


            }
        });
        Timer.start();
    }
}
