package Objects;
public class SPolygon extends SObject {


	public float [] vertexArray = { //define triangles in anti-clockwise direction
			// front face
			// triangle 1
			1,  -1, 1,
			0, 1,  0,
			-1,  -1,  1,
			// back face
			-1,  -1, -1,
			0, 1,  0,
			1,  -1,  -1,
			// left face
			-1,  -1, -1,
			0, 1,  0,
			-1,  -1,  1,
			// right face
			1,  -1, -1,
			0, 1,  0,
			1,  -1,  1,

			// bottom face
			1, -1,  1,    // triangle 1
			-1, -1,  1,
			1, -1, -1,
			-1, -1,  1,    // triangle 2
			-1, -1, -1,
			1, -1, -1

	};



	//same color on each side
	public float [] colorArray = {
			1, 0, 0,  //Front color
			1, 0, 0,
			1, 0, 0,
			1, 0, 0,
			1, 0, 0,
			1, 0, 0,
			0, 1, 0,  //Back color
			0, 1, 0,
			0, 1, 0,
			0, 1, 0,
			0, 1, 0,
			0, 1, 0,
			0, 0, 1,  //Left color
			0, 0, 1,
			0, 0, 1,
			0, 0, 1,
			0, 0, 1,
			0, 0, 1,
			1, 1, 0,  //Right color
			1, 1, 0,
			1, 1, 0,
			1, 1, 0,
			1, 1, 0,
			1, 1, 0,
			1, 0, 1,  //Top color
			1, 0, 1,
			1, 0, 1,
			1, 0, 1,
			1, 0, 1,
			1, 0, 1,
			0, 1, 1,  //Bottom color
			0, 1, 1,
			0, 1, 1,
			0, 1, 1,
			0, 1, 1,
			0, 1, 1,

	};

	@Override
	protected void genData() {

	}
}