import static com.jogamp.opengl.GL3.*;
import static java.lang.Thread.sleep;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.System;

import Basic.ShaderProg;
import Basic.Transform;
import Basic.Vec4;
import Objects.SCube;
import Objects.SObject;
import Objects.SPolygon;
import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.event.InputEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.imageio.ImageIO;

public class CGCW04{

	private GLWindow window; //Define a canvas
	final FPSAnimator animator=new FPSAnimator(60, true);
	final Renderer renderer = new Renderer();

	public CGCW04() {
		// Get OpenGL version 3 profile, and
		// enable the canvas use version 3
		GLProfile glp = GLProfile.get(GLProfile.GL3);
		GLCapabilities caps = new GLCapabilities(glp);
		window = GLWindow.create(caps);


		//Set the canvas to listen GLEvents from renderer
		window.addGLEventListener(renderer);

		// Animator act on canvas
		animator.add(window);

		window.setTitle("CGCW04");
		window.setSize(500,500);
		window.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);
		window.setVisible(true);

		animator.start();
		//window.requestFocus();
	}

	public static void main(String[] args) {
		new CGCW04();

	}

	class Renderer implements GLEventListener {

		// Define a Transformation instance
		// Transformation matrix is initialised as Identity;
		private Transform T = new Transform();

		//VAOs and VBOs parameters
		private int idPoint=0, numVAOs = 1;
		private int idBuffer=0, numVBOs = 1;
		private int idElement=0, numEBOs = 2;
		private int[] VAOs = new int[numVAOs];
		private int[] VBOs = new int[numVBOs];
		private int[] EBOs = new int[numEBOs];

		//Model parameters
		private int numVertices = 36;
		private int vPosition;
		private int vColor;

		//Transformation parameters
		private int ModelView;
		private int NormalTransform;
		private int Projection;
		private float scale = 0.5f;
		private float tx = 0;
		private float ty = 0;
		private float rx = 0;
		private float ry = 0;
		


		@Override
		public void display(GLAutoDrawable drawable) {
			GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this

			gl.glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

			gl.glEnable(GL_BLEND);
			gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

			gl.glPointSize(5);
			gl.glLineWidth(5);

			T.initialize();
			T.scale(scale, scale, scale);
			T.rotateX(tx);
			T.rotateY(ty);
			T.translate(rx, ry, 0);

			//constantly manipulating x and y axis which allows polygon object to spin and rotate within the scene
			tx += 0.8;
			ty += 0.8;

			T.lookAt(0, 0, 0, 0, 0, -100, 0, 1, 0);  //default parameters

			//draw pyramid
			gl.glDrawArrays(GL_TRIANGLES, 0, numVertices);
			gl.glUniformMatrix4fv( ModelView, 1, true, T.getTransformv(), 0 );
			gl.glUniformMatrix4fv( NormalTransform, 1, true, T.getInvTransformTv(), 0 );


		}

		@Override
		public void dispose(GLAutoDrawable drawable) {
			System.exit(0);
		}

		@Override
		public void init(GLAutoDrawable drawable) {
			GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this

			gl.glGenVertexArrays(numVAOs,VAOs,0);
			gl.glBindVertexArray(VAOs[idPoint]);

			//create pyramid object
			SPolygon pyramid = new SPolygon();

			FloatBuffer vertices = FloatBuffer.wrap(pyramid.vertexArray);
			FloatBuffer colors = FloatBuffer.wrap(pyramid.colorArray);

			gl.glGenBuffers(numVBOs, VBOs,0);
			gl.glBindBuffer(GL_ARRAY_BUFFER, VBOs[idBuffer]);

			//creates empty buffer and null pointer for data values
			long vertexSize = pyramid.vertexArray.length*(Float.SIZE/8);
			long colorSize = pyramid.colorArray.length*(Float.SIZE/8);
			gl.glBufferData(GL_ARRAY_BUFFER, vertexSize +colorSize,
					null, GL_STATIC_DRAW);

			gl.glBufferSubData( GL_ARRAY_BUFFER, 0, vertexSize, vertices );
			gl.glBufferSubData( GL_ARRAY_BUFFER, vertexSize, colorSize, colors );

			ShaderProg shaderproc = new ShaderProg(gl, "polygon.vert", "polygon.frag");
			int program = shaderproc.getProgram();
			gl.glUseProgram(program);

			//init vertex position in vertex shader
			vPosition = gl.glGetAttribLocation( program, "vPosition" );
			gl.glEnableVertexAttribArray(vPosition);
			gl.glVertexAttribPointer(vPosition, 3, GL_FLOAT, false, 0, 0L);

			//init vertex colour in vertex shader
			vColor = gl.glGetAttribLocation( program, "vColor" );
			gl.glEnableVertexAttribArray(vColor);
			gl.glVertexAttribPointer(vColor, 3, GL_FLOAT, false, 0, vertexSize);

			// Get connected with the ModelView, NormalTransform, and Projection matrices
			// in the vertex shader
			ModelView = gl.glGetUniformLocation(program, "ModelView");
			NormalTransform = gl.glGetUniformLocation(program, "NormalTransform");
			Projection = gl.glGetUniformLocation(program, "Projection");

			// Generate VAOs, VBOs, and EBOs
			gl.glGenVertexArrays(numVAOs,VAOs,0);
			gl.glGenBuffers(numVBOs, VBOs,0);
			gl.glGenBuffers(numEBOs, EBOs,0);

			gl.glEnable(GL_DEPTH_TEST);

		}

		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {

			GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this

			gl.glViewport(x, y, w, h);

			T.initialize();

			if(h<1){h=1;}
			if(w<1){w=1;}
			float a = (float) w/ h;   //aspect
			if (w < h) {
				T.ortho(-1, 1, -1/a, 1/a, -1, 1);
//				T.Frustum(-1, 1, -1/a, 1/a, 0.1f, 1000);
			}
			else{
				T.ortho(-1*a, 1*a, -1, 1, -1, 1);
//				T.Frustum(-1*a, 1*a, -1, 1, 0.1f, 1000);
			}
//			T.Perspective(60, a, 0.1f, 1000);

			// Convert right-hand to left-hand coordinate system
			T.reverseZ();
			gl.glUniformMatrix4fv( Projection, 1, true, T.getTransformv(), 0 );

		}

	}

}

