package Objects;
public class SCone extends SObject{
	private float radius;
	private int slices;
	private int point;


	public SCone(float radius, int slices, int points){ //instead of stacks, here we have points which represent the point height of the cone
		super();
		this.radius = radius;
		this.slices = slices;
		this.point = points;
		update();
	}

	private void init(){
		this.radius = 1;
		this.slices = 20;
		this.point = 1;
	}

	@Override
	protected void genData() {
		int i,j,k;

		double deltaAngle=PI*2/slices;

		// Generate vertices coordinates and normal values
		numVertices = (slices+1+2);
		vertices = new float[numVertices*3];
		normals = new float[numVertices*3];


		//the point of cone
		normals[0] = 0;
		normals[1] = 0;
		normals[2] = 1;
		vertices[0] = 0;
		vertices[1] = 0;
		vertices[2] = point;

		//init k
		k = 1;
		//vertices on the cone
		for(i=0;i<=slices;i++){
			normals[3*k+1] = sin(deltaAngle*i);
			normals[3*k] = cos(deltaAngle*i);
			normals[3*k+2] = 0;
			vertices[3*k+2] = radius*normals[3*k+2];
			vertices[3*k] = radius*normals[3*k];
			vertices[3*k+1] = radius*normals[3*k+1];
			normals[3*k+1] = sin(deltaAngle*i+1);
			normals[3*k] = cos(deltaAngle*i+1); //set normals another time as lighting is not the same as sphere
			k++;
		}

		//base of cone
		normals[3*k+1] = 0;
		normals[3*k] = 0;
		normals[3*k+2] = -1;
		vertices[3*k+2] = 0;
		vertices[3*k] = 0;
		vertices[3*k+1] = 0;

		// Generate indices for triangular mesh
		numIndices = slices*6;
		indices = new int[numIndices];

		k = 0;
		//top point, numElement:slices*3
		for(j=1;j<=slices;j++){
			indices[k++] = 0;
			indices[k++] = j;
			indices[k++] = j+1;
		}

		//base, numElement:slices*3
		int temp = numVertices-1;
		for(j=1;j<=slices;j++){
			indices[k++] = temp;
			indices[k++] = j;
			indices[k++] = j+1;
		}

	}

	public void setRadius(float radius){
		this.radius = radius;
		updated = false;
	}

	public void setSlices(int slices){
		this.slices = slices;
		updated = false;
	}

	public void setpoints(int points){
		this.point = points;
		updated = false;
	}

	public float getRadius(){
		return radius;
	}

	public int getSlices(){
		return slices;
	}

	public int getpoint(){
		return point;
	}
}