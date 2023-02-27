import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;

class Maze{ //the maze
	private int N; //NxN is the number of cells of the array.
	private Cell[][] maze; //2d array that represents the maze.
	private int[] s=new int[2]; //beggining cell.
	private int[] g1=new int[2],g2=new int[2]; //final cells.
	private int[] position=new int[2];
	private double g;
	private double h;
	private ArrayList<Maze> path;

	public Maze(){}

	public Maze(int size,int sX, int sY, int g1X, int g1Y, int g2X, int g2Y,double pr,double h){ //constructor to create first maze.
		path= new ArrayList<Maze>();
		g=0;
		this.h=h;
		N=size;
		s[0]=sX;
		s[1]=sY;
		g1[0]=g1X;
		g1[1]=g1Y;
		g2[0]=g2X;
		g2[1]=g2Y;
		position[0]=sX;
		position[1]=sY;
		maze= new Cell[size][size];
		Random rand = new Random();
		int willBlock,setvalue;
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				maze[i][j]=new Cell(false,0);
				willBlock= rand.nextInt(10);
				//we can't put obstacle at s,g1,g2 and also need to check propability
				if((i==s[0]&&j==s[1]) || (i==g1[0]&&j==g1[1]) || (i==g2[0]&&j==g2[1]) || (willBlock>=pr*10)){ //that cell has no obstacle.
					maze[i][j].setCondition(true);
					setvalue= rand.nextInt(4);
					maze[i][j].setValue(setvalue+1);
				}
			}
		}
	}

	public Maze(Maze other, int posX, int posY, double g, double h){ //copy constructor to create children
		path=new ArrayList<Maze>();
		int num= other.path.size();
		for(int i=0;i<num;i++){
			this.path.add(other.path.get(i));
		}this.path.add(other);
		this.position[0]=posX;
		this.position[1]=posY;
		this.g=g;
		this.h=h;
		this.N=other.N;
		this.s[0]=other.s[0];
		this.s[1]=other.s[1];
		this.g1[0]=other.g1[0];
		this.g1[1]=other.g1[1];
		this.g2[0]=other.g2[0];
		this.g2[1]=other.g2[1];
		maze= new Cell[N][N];
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				this.maze[i][j]= new Cell(other.maze[i][j]);
			}
		}
	}

	public double calculateH1(){ //calculates h(n) for g1
		int posX=position[0];
		int posY=position[1];
		double h_n= 0;
		int y= Math.abs(posY-g1[1]); //y axe difference
		int x= Math.abs(posX-g1[0]); //x axe difference
		int xR= Math.abs(x-y);
		if(posX==g1[0]){ //same column
			if(y%2==0){
				h_n= y*0.5;
			}else{
				h_n= ((y-1)*0.5)+1;
			}
		}else{
			if(xR%2==0){ //Xr artios
				if(x>y){
					h_n= (y*0.5)+(xR*0.5);
				}else{
					h_n= (x*0.5)+(xR*0.5);
				}
			}else{ //Xr perittos
				if(x>y){
					h_n= (y*0.5)+((xR-1)*0.5)+1;
				}else{
					h_n= (x*0.5)+((xR-1)*0.5)+1;
				}
			}
		}return h_n;
	}

	public double calculateH2(){ //calculates h(n) for g2
		int posX=position[0];
		int posY=position[1];
		double h_n= 0;
		int y= Math.abs(posY-g2[1]); //y axe difference
		int x= Math.abs(posX-g2[0]); //x axe difference
		int xR= Math.abs(x-y);
		if(posX==g2[0]){ //same column
			if(y%2==0){
				h_n= y*0.5;
			}else{
				h_n= ((y-1)*0.5)+1;
			}
		}else{
			if(xR%2==0){
				if(x>y){
					h_n= (y*0.5)+(xR*0.5);
				}else{
					h_n= (x*0.5)+(xR*0.5);
				}
			}else{
				if(x>y){
					h_n= (y*0.5)+((xR-1)*0.5)+1;
				}else{
					h_n= (x*0.5)+((xR-1)*0.5)+1;
				}
			}
		}return h_n;
	}
	
	public double calculateH(){
		double h1= this.calculateH1();
		double h2= this.calculateH2();
		if(h1<h2){
			return h1;
		}else{
			return h2;
		}
	}

	public Cell[][] getMaze(){
		return maze;
	}

	public int[] getG1(){
		return g1;
	}

	public int[] getG2(){
		return g2;
	}

	public int[] getPos(){
		return position;
	}

	public double getG(){
		return g;
	}

	public double getH(){
		return h;
	}

	public void setH(double h){
		this.h=h;
	}

	public int getN(){
		return N;
	}

	public int getValueOfCell(int x, int y){
		return maze[x][y].getValue();
	}

	public boolean isX(int x, int y){ //check if cell is blocked
		if(maze[x][y].getValue()==0){
			return true;
		}return false;
	}

	public void print(){
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				if(maze[i][j].getCondition()){
					System.out.print(maze[i][j].getValue()+" ");
				}else{
					System.out.print("X ");
				}
			}System.out.println();
		}
	}

	public void printInfo(){
		System.out.println("N= "+N);
		System.out.println("position in maze= ("+position[0]+","+position[1]+")");
		System.out.println("g(["+position[0]+","+position[1]+"])= "+g);
		System.out.println("h(["+position[0]+","+position[1]+"])= "+h);
		System.out.println("s= ("+s[0]+","+s[1]+")");
		System.out.println("g1= ("+g1[0]+","+g1[1]+")");
		System.out.println("g2= ("+g2[0]+","+g2[1]+")");
	}

	public void printPath(){
		int num=path.size();
		String finalPath="Path is: ";
		for(int i=0;i<num;i++){
			finalPath= finalPath+"["+path.get(i).getPos()[0]+","+path.get(i).getPos()[1]+"]->";
		}finalPath= finalPath+"["+position[0]+","+position[1]+"].";
		System.out.println(finalPath);
	}
}