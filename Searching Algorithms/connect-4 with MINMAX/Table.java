import java.util.ArrayList;

class Table{
	private int M; //rows
	private int N; //collumns
	private int value; //value of the table
	private int[][] table; //table of current condition.
	private ArrayList<Table> children; //children of a condition in a list.

	public Table(int M, int N){ //use to create starting table
		children= new ArrayList<Table>();
		this.M=M;
		this.N=N;
		value=0;
		table=new int[M][N];
		for(int i=0;i<M;i++){
			for(int j=0;j<N;j++){
				table[i][j]=0;
			}
		}
	}

	public Table(Table other, int column, int player){ //copy constructor for children
		children= new ArrayList<Table>();
		this.M=other.M;
		this.N=other.N;
		value=0;
		table=new int[M][N];
		for(int i=0;i<M;i++){
			for(int j=0;j<N;j++){
				this.table[i][j]= other.table[i][j];
			}
		}this.put(player, column);
	}

	public boolean isFull(){ //gemise o pinakas
		for(int i=0;i<N;i++){
			if(this.isAvailable(i)){
				return false;
			}
		}return true;
	}

	public boolean wins(int K, int player){ //elegxos an kerdise o paiktis player(MAX=1, MIN=-1) me K synexomena symbola.
		int counter=0;
		for(int i=0;i<N;i++){ //check columns
			for(int j=0;j<M;j++){
				if(table[j][i]==player){
					counter++;
					if(counter==K){
						return true;
					}
				}else{
					counter=0;
				}
			}counter=0;
		}
		for(int i=0;i<M;i++){ //check rows
			for(int j=0;j<N;j++){
				if(table[i][j]==player){
					counter++;
					if(counter==K){
						return true;
					}
				}else{
					counter=0;
				}
			}counter=0;
		}int indexX=0, indexY=0;
		for(int i=0;i<M;i++){  //diagonial 1/4
			indexX=i;
			indexY=0;
			while(indexX<M && indexY<N){
				if(table[indexX][indexY]==player){
					indexX++;
					indexY++;
					counter++;
					if(counter==K){
						return true;
					}
				}else{
					counter=0;
					indexX++;
					indexY++;
				}
			}counter=0;
		}for(int i=0;i<M;i++){  //diagonial 2/4
			indexX=i;
			indexY=N-1;
			while(indexX>=0 && indexY>=0){
				if(table[indexX][indexY]==player){
					indexX--;
					indexY--;
					counter++;
					if(counter==K){
						return true;
					}
				}else{
					counter=0;
					indexX--;
					indexY--;
				}
			}counter=0;
		}for(int i=0;i<M;i++){  //diagonial 3/4
			indexX=i;
			indexY=0;
			while(indexX>=0 && indexY<N){
				if(table[indexX][indexY]==player){
					indexX--;
					indexY++;
					counter++;
					if(counter==K){
						return true;
					}
				}else{
					counter=0;
					indexX--;
					indexY++;
				}
			}counter=0;
		}for(int i=0;i<M;i++){  //diagonial 4/4
			indexX=i;
			indexY=N-1;
			while(indexX<M && indexY>=0){
				if(table[indexX][indexY]==player){
					indexX++;
					indexY--;
					counter++;
					if(counter==K){
						return true;
					}
				}else{
					counter=0;
					indexX++;
					indexY--;
				}
			}counter=0;
		}
		return false; //den kerdise kapoios
	}

	public void freeChildren(){
		children.clear();
	}

	public int put(int player, int column){ //topothetise to symbolo toy player sth stili column
		int current= table[M-1][column];
		int before=0;
		while(current!=0){
			before++;
			current= table[M-1-before][column];
		}table[M-1-before][column]= player;
		return M-1-before;
	}

	public boolean isAvailable(int column){ //elegxos an h sthlh column xoraei extra symbolo
		if(table[0][column]==0){
			return true;
		}return false;
	}

	public int getValue(){
		return value;
	}

	public void setValue(int value){
		this.value=value;
	}

	public int getN(){
		return N;
	}

	public void addChild(Table child){
		children.add(child);
	}

	public ArrayList<Table> getChildren(){
		return children;
	}

	public Table getMaxChild(){ //pare to paidi me th megalyteri timi
		int max= children.get(0).getValue();
		int a=0;
		for(int i=0;i<children.size();i++){
			if(children.get(i).getValue()>max){
				max= children.get(i).getValue();
				a=i;
			}
		}return children.get(a);
	}

	public void printTable(){ //ektypose to paixnidi me '-' gia tis kenes theseis, 'X' gia tis theseis toy MAX kai 'O' gia tis theseis toy MIN.
		for(int i=0;i<N;i++){
			System.out.print(i+" ");
		}System.out.println();
		for(int i=0;i<M;i++){
			for(int j=0;j<N;j++){
				if(table[i][j]==1){
					System.out.print("X ");
				}else if(table[i][j]==-1){
					System.out.print("O ");
				}else{
					System.out.print("- ");
				}
			}System.out.println();
		}for(int i=0;i<N;i++){
			System.out.print(i+" ");
		}System.out.println();
	}
}