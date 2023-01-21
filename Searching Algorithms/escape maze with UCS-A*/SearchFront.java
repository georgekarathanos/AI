import java.util.ArrayList;

class SearchFront{ //metwpo anzhthshs.
	int numberOfMazes;
	private ArrayList<Maze> searchFront=new ArrayList<Maze>();

	public void addMaze(Maze m){ //prosthiki katastashs sto metwpo anazhthshs.
		searchFront.add(m);
		numberOfMazes++;
	}

	public void removeMaze(Maze m){ //diagrafh katastashs apo to metwpo anazhthshs.
		searchFront.remove(m);
		numberOfMazes--;
	}

	public Maze findMinG(){ //vres th katastash me to mikrotero g(n) sto metwpo anazhthshs.
		double minG= searchFront.get(0).getG();
		int a=0;
		for(int i=0;i<numberOfMazes;i++){
			if(searchFront.get(i).getG()<minG){
				minG= searchFront.get(i).getG();
				a=i;
			}
		}return searchFront.get(a);
	}

	public Maze findMinA(){ //vres th katastash me to mikrotero e(n)=g(n)+h(n) sto metwpo anazhthshs.
		double minA= searchFront.get(0).getG() + searchFront.get(0).getH();
		int a=0;
		for(int i=0;i<numberOfMazes;i++){
			if((searchFront.get(i).getG() + searchFront.get(i).getH())<minA){
				minA= searchFront.get(i).getG() + searchFront.get(i).getH();
				a=i;
			}
		}return searchFront.get(a);
	}

	public boolean isEmpty(){  //elegxos an einai adeio to metwpo anazhthshs.
		if(numberOfMazes==0){
			return true;
		}return false;
	}
}