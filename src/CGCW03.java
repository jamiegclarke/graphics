import static com.jogamp.opengl.GL3.*;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import Basic.ShaderProg;
import Basic.Transform;
import Basic.Vec4;
import Objects.SCube;
import Objects.SObject;
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

public class CGCW03{
	private GLWindow window;

	final FPSAnimator animator=new FPSAnimator(60, true);
	final Renderer renderer = new Renderer();


	public CGCW03() {
		// Get OpenGL version 3 profile, and
		// enable the canvas use version 3
		GLProfile glp = GLProfile.get(GLProfile.GL3);
		GLCapabilities caps = new GLCapabilities(glp);
		window = GLWindow.create(caps);

		//Set the canvas to listen GLEvents from renderer
		window.addGLEventListener(renderer);
		//Set the canvas to listen mouse events from renderer
		window.addMouseListener(renderer);

		// Animator act on canvas
		animator.add(window);

		window.setTitle("CGCW03");
		window.setSize(500,500);
		window.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);
		//The above line will only close the window but not exit the program
		//Add System.exit(0) in dispose() function will cause it exit
		window.setVisible(true);

		animator.start();
		//window.requestFocus();
	}

	public static void main(String[] args) {

		new CGCW03();
	}

	class Renderer implements GLEventListener, MouseListener {

		private Transform T = new Transform();

		//VAOs and VBOs parameters
		private int idPoint=0, numVAOs = 1;
		private int idBuffer=0, numVBOs = 1;
		private int idElement=0, numEBOs = 1;
		private int[] VAOs = new int[numVAOs];
		private int[] VBOs = new int[numVBOs];
		private int[] EBOs = new int[numEBOs];

		//Model parameters
		private int numVertices = 36;

		//Transformation parameters
		private int ModelView;
		private int NormalTransform;
		private int Projection;

		//Lighting parameter
		private int AmbientProduct;
		private int DiffuseProduct;
		private int SpecularProduct;
		private int Shininess;

		private float[] ambient;
		private float[] diffuse;
		private float[] specular;
		private float  materialShininess;

		//texture parameters
		ByteBuffer texImg;
		private int texWidth, texHeight;

		//Transformation parameters
		private float scale = 0.5f;
		private float tx = 0;
		private float ty = 0;
		private float rx = 0;
		private float ry = 0;

		//Mouse position
		private int xMouse = 0;
		private int yMouse = 0;

		@Override
		public void display(GLAutoDrawable drawable) {
			GL3 gl = drawable.getGL().getGL3();

			gl.glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

			gl.glPointSize(5);
			gl.glLineWidth(5);

			T.initialize();
			T.scale(scale, scale, scale);
			T.rotateX(rx);
			T.rotateY(ry);
			T.translate(tx, ty, 0);

			gl.glUniformMatrix4fv( ModelView, 1, true, T.getTransformv(), 0 );
			gl.glUniformMatrix4fv( NormalTransform, 1, true, T.getInvTransformTv(), 0 );

			//send other uniform variables to shader
			gl.glUniform4fv( AmbientProduct, 1, ambient,0 );
			gl.glUniform4fv( DiffuseProduct, 1, diffuse, 0 );
			gl.glUniform4fv( SpecularProduct, 1, specular, 0 );
			gl.glUniform1f( Shininess, materialShininess);

			//draw cube
			gl.glDrawArrays(GL_TRIANGLES, 0, numVertices);
		}

		@Override
		public void dispose(GLAutoDrawable drawable) {
			System.exit(0);
		}

		@Override
		public void init(GLAutoDrawable drawable) {
			GL3 gl = drawable.getGL().getGL3(); // Get GL pipeline object

			try {
				texImg = readImage("WelshDragon.jpg");
			} catch (IOException ex) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
			}

			gl.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, texWidth, texHeight,
					0, GL_BGR, GL_UNSIGNED_BYTE, texImg);

			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

			gl.glEnable(GL_DEPTH_TEST);

			SCube cube = new SCube();

			FloatBuffer data = FloatBuffer.wrap(cube.vertexCoord);
			FloatBuffer colours = FloatBuffer.wrap(cube.vertexColours);
			FloatBuffer textures = FloatBuffer.wrap(cube.texCoord);

			gl.glGenBuffers(numVBOs, VBOs,0);
			gl.glBindBuffer(GL_ARRAY_BUFFER, VBOs[idBuffer]);
			gl.glGenVertexArrays(numVAOs,VAOs,0);
			gl.glBindVertexArray(VAOs[idPoint]);
			gl.glGenBuffers(numEBOs, EBOs,0);
			gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBOs[idElement]);


			//creates empty buffer and null pointer for data values
			long coordSize = cube.vertexCoord.length*(Float.SIZE/8);
			long colourSize = cube.vertexColours.length*(Float.SIZE/8);
			long texSize = cube.texCoord.length*(Float.SIZE/8);
			gl.glBufferData(GL_ARRAY_BUFFER, coordSize + colourSize + texSize,
					null, GL_STATIC_DRAW); // pay attention to *Float.SIZE/8
			long indexSize = cube.vertexIndexs.length*(Integer.SIZE/8);
			IntBuffer elements = IntBuffer.wrap(SCube.vertexIndexs);
			gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexSize,
					elements, GL_STATIC_DRAW);


			gl.glBufferSubData( GL_ARRAY_BUFFER, 0, coordSize, data );
			gl.glBufferSubData( GL_ARRAY_BUFFER, coordSize, colourSize, colours );
			gl.glBufferSubData( GL_ARRAY_BUFFER, coordSize + colourSize, texSize, textures );


			//create shader program and link to shader files
			ShaderProg shaderproc = new ShaderProg(gl, "Texture.vert", "Texture.frag");
			int program = shaderproc.getProgram();
			gl.glUseProgram(program);

			//init vertex position in vertex shader
			int vPosition = gl.glGetAttribLocation( program, "vPosition");
			//init vertex colour in vertex shader
			int vColour = gl.glGetAttribLocation( program, "vColour");
			//init vertex texture in vertex shader
			int vTexCoord = gl.glGetAttribLocation( program, "vTexCoord");

			//get connected with ModelView matrix in vertex shader
			ModelView = gl.glGetUniformLocation(program, "ModelView");
			NormalTransform = gl.glGetUniformLocation(program, "NormalTransform");
			Projection = gl.glGetUniformLocation(program, "Projection");

			AmbientProduct = gl.glGetUniformLocation(program, "AmbientProduct");
			DiffuseProduct = gl.glGetUniformLocation(program, "DiffuseProduct");
			SpecularProduct = gl.glGetUniformLocation(program, "SpecularProduct");
			Shininess = gl.glGetUniformLocation(program, "Shininess");

			// Initialize shader lighting parameters
			float[] lightPosition = {10.0f, 3.0f, -10.0f, 0.0f};
			Vec4 lightAmbient = new Vec4(0.3f, 0.3f, 0.3f, 1.0f);
			Vec4 lightDiffuse = new Vec4(0.7f, 0.7f, 0.7f, 1.0f);
			Vec4 lightSpecular = new Vec4(1.0f, 1.0f, 1.0f, 1.0f);

			gl.glUniform4fv( gl.glGetUniformLocation(program, "LightPosition"),
					1, lightPosition, 0 );

			gl.glUniform1i( gl.glGetUniformLocation(program, "tex"), 0 );


			gl.glEnableVertexAttribArray(vPosition);
			gl.glVertexAttribPointer(vPosition, 3, GL_FLOAT, false, 0, 0L);

			gl.glEnableVertexAttribArray(vColour);
			gl.glVertexAttribPointer(vColour, 3, GL_FLOAT, false, 0, coordSize);

			gl.glEnableVertexAttribArray( vTexCoord );
			gl.glVertexAttribPointer( vTexCoord, 2, GL_FLOAT, false, 0, coordSize+colourSize);


			//set material for cube
			Vec4 materialAmbient = new Vec4(1.0f, 1.0f, 1.0f, 1.0f);
			Vec4 materialDiffuse = new Vec4(0.55f, 0.55f, 0.55f, 1.0f);
			Vec4 materialSpecular = new Vec4(0.70f, 0.70f, 0.70f, 1.0f);
			materialShininess = 0.25f;

			//set gouraud lighting products
			Vec4 ambientProduct = lightAmbient.times(materialAmbient);
			ambient = ambientProduct.getVector();
			Vec4 diffuseProduct = lightDiffuse.times(materialDiffuse);
			diffuse = diffuseProduct.getVector();
			Vec4 specularProduct = lightSpecular.times(materialSpecular);
			specular = specularProduct.getVector();


		}

		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int w,
							int h) {

			GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this

			gl.glViewport(x, y, w, h);

			T.initialize();


			if(h<1){h=1;}
			if(w<1){w=1;}
			float a = (float) w/ h;   //aspect
			if (w < h) {
				T.ortho(-1, 1, -1/a, 1/a, -1, 1);
			}
			else{
				T.ortho(-1*a, 1*a, -1, 1, -1, 1);
			}

			// Convert right-hand to left-hand coordinate system
			T.reverseZ();
			gl.glUniformMatrix4fv( Projection, 1, true, T.getTransformv(), 0 );

		}

		private ByteBuffer readImage(String filename) throws IOException {
			//reads welsh flag jpg

			ByteBuffer imgbuf;
			BufferedImage img = ImageIO.read(new FileInputStream(filename));

			texWidth = img.getWidth();
			texHeight = img.getHeight();
			DataBufferByte datbuf = (DataBufferByte) img.getData().getDataBuffer();
			imgbuf = ByteBuffer.wrap(datbuf.getData());
			return imgbuf;
		}


		@Override
		public void mouseDragged(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();

			//left button down, move the object
			if((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0){
				tx += (x-xMouse) * 0.01;
				ty -= (y-yMouse) * 0.01;
				xMouse = x;
				yMouse = y;
			}

			//right button down, rotate the object
			if((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0){
				ry += (x-xMouse) * 1;
				rx += (y-yMouse) * 1;
				xMouse = x;
				yMouse = y;
			}

			//middle button down, scale the object
			if((e.getModifiers() & InputEvent.BUTTON2_MASK) != 0){
				scale *= Math.pow(1.1, (y-yMouse) * 0.5);
				xMouse = x;
				yMouse = y;
			}
		}

		// mouse wheel to view 3D cube

		@Override
		public void mouseWheelMoved(MouseEvent mouseEvent) {

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			xMouse = e.getX();
			yMouse = e.getY();
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}







}

