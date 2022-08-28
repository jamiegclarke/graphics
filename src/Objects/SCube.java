package Objects;

public class SCube extends SObject {

	@Override
	protected void genData() {

	}

	public static float[] vertexCoord = { //define vertex coordinates
		// front face
				1, 1, 1,    // triangle 1
				-1, 1, 1,
				-1, -1, 1,
				1, 1, 1,    // triangle 2
				-1, -1, 1,
				1, -1, 1,
				// back face
				1, 1, -1,    // triangle 1
				-1, -1, -1,
				-1, 1, -1,
				1, 1, -1,    // triangle 2
				1, -1, -1,
				-1, -1, -1,
				// left face
				-1, 1, 1,    // triangle 1
				-1, 1, -1,
				-1, -1, 1,
				-1, 1, -1,    // triangle 2
				-1, -1, -1,
				-1, -1, 1,
				// right face
				1, 1, 1,    // triangle 1
				1, -1, 1,
				1, 1, -1,
				1, 1, -1,    // triangle 2
				1, -1, 1,
				1, -1, -1,
				// top face
				1, 1, 1,    // triangle 1
				1, 1, -1,
				-1, 1, 1,
				-1, 1, 1,    // triangle 2
				1, 1, -1,
				-1, 1, -1,
				// bottom face
				1, -1, 1,    // triangle 1
				-1, -1, 1,
				1, -1, -1,
				-1, -1, 1,    // triangle 2
				-1, -1, -1,
				1, -1, -1
	};

	public static float[] texCoord = { //define texture coordinates


			1, 0,   // front face
			0, 0,
			0, 1,

			1, 0,
			0, 1,
			1, 1,

			0, -1, //back face
			1, 0,
			1, -1,

			0, -1,
			0, 0,
			1, 0,

			0, -1, // left face
			-1, -1,
			0, 0,

			0, -1,
			0, 0,
			1, 0,

			-1, 0, // right face
			-1, 1,
			0, 0,

			0, 0,
			-1, 1,
			0, 1,

			1, 0, // top face
			1, -1,
			0, 0,

			0, 0,
			1, -1,
			0, -1,

			0, -1, // bottom face
			-1, -1,
			0, 0,

			-1, -1,
			-1, 0,
			0, 0,


	};

	public  static int[] vertexIndexs = { //define vertex indexes
			0, 1, 2,
			2, 1, 3
	};

	public  static float[] vertexColours = { //define vertex colours
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1,
			1, 1, 1

	};



}