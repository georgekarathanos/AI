import java.lang.Math;

class Implementor{ //periexei thn ylopoihsh twn methodwn
	private SearchFront searchFront[];

	public Implementor(){
		searchFront= new SearchFront[2];
		searchFront[0]= new SearchFront(); //searchfront for UCS
		searchFront[1]= new SearchFront(); //searchfront for A*
	}

	public Maze createFirstMaze(int size,int sX, int sY, int g1X, int g1Y, int g2X, int g2Y,double pr){
		Maze maze=new Maze(size,sX,sY,g1X,g1Y,g2X,g2Y,pr,0);
		maze.setH(maze.calculateH());
		return maze;
	}


	public Maze createKidMaze(Maze parent,int newP){
		int same=0;
		double cost;
		int N= parent.getN();
		int newPosX,newPosY;
		int parentPosX= parent.getPos()[0];
		int parentPosY= parent.getPos()[1];
		double parentG= parent.getG();
		if(newP==0){
			newPosX= parentPosX;
			newPosY= parentPosY-1;
		}else if(newP==1){
			newPosX= parentPosX+1;
			newPosY= parentPosY-1;
			same=1;
		}else if(newP==2){
			newPosX= parentPosX+1;
			newPosY= parentPosY;
		}else if(newP==3){
			newPosX= parentPosX+1;
			newPosY= parentPosY+1;
			same=1;
		}else if(newP==4){
			newPosX= parentPosX;
			newPosY= parentPosY+1;
		}else if(newP==5){
			newPosX= parentPosX-1;
			newPosY= parentPosY+1;
			same=1;
		}else if(newP==6){
			newPosX= parentPosX-1;
			newPosY= parentPosY;
		}else{
			newPosX= parentPosX-1;
			newPosY= parentPosY-1;
			same=1;
		}int parentValue=parent.getValueOfCell(parentPosX,parentPosY);
		int myValue= parent.getValueOfCell(newPosX,newPosY);
		if(same==0){
			cost= Math.abs(parentValue-myValue)+1;
		}else{
			cost= Math.abs(parentValue-myValue)+0.5;
		}double kidG= parent.getG() + cost;
		Maze kid= new Maze(parent,newPosX,newPosY,kidG,0);
		kid.setH(kid.calculateH());
		return kid;
	}

	public void UCS(Maze parent){ //ucs algorithm
		System.out.println("______________--");
		int cost=0;
		int posX=0,posY=0;
		int ext= 0; //number of ext.
		searchFront[0].addMaze(parent);
		System.out.println("Waiting for UCS....");
		while(true){
			Maze check= searchFront[0].findMinG();
			if(searchFront[0].isEmpty()){
				break;
			}
			posX=check.getPos()[0];
			posY=check.getPos()[1];
			if((posX==check.getG1()[0])&&(posY==check.getG1()[1])){ //found g1
				System.out.println("UCS found G1 with cost:"+check.getG()+". Number of extensions:"+ext);
				check.printPath();
				break;
			}else if((posX==check.getG2()[0])&&(posY==check.getG2()[1])){ //found g2
				System.out.println("UCS found G2 with cost:"+check.getG()+". Number of extensions:"+ext);
				check.printPath();
				break;
			}else{
				for(int i=0;i<8;i++){
					if(i==0){
						if(posY==0){
							continue;
						}if(check.isX(posX,posY-1)){
							continue;
						}
					}else if(i==1){
						if((posY==0) || (posX==check.getN()-1)){
							continue;
						}if(check.isX(posX+1,posY-1)){
							continue;
						}
					}else if(i==2){
						if(posX==check.getN()-1){
							continue;
						}if(check.isX(posX+1,posY)){
							continue;
						}
					}else if(i==3){
						if((posY==check.getN()-1) || (posX==check.getN()-1)){
							continue;
						}if(check.isX(posX+1,posY+1)){
							continue;
						}
					}else if(i==4){
						if(posY==check.getN()-1){
							continue;
						}if(check.isX(posX,posY+1)){
							continue;
						}
					}else if(i==5){
						if((posY==check.getN()-1) || (posX==0)){
							continue;
						}if(check.isX(posX-1,posY+1)){
							continue;
						}
					}else if(i==6){
						if(posX==0){
							continue;
						}if(check.isX(posX-1,posY)){
							continue;
						}
					}else if(i==7){
						if((posY==0) || (posX==0)){
							continue;
						}if(check.isX(posX-1,posY-1)){
							continue;
						}
					}searchFront[0].addMaze(this.createKidMaze(check,i));
				}searchFront[0].removeMaze(check);
			}ext++;
		}
	}

	public void A(Maze parent){ //A* algorithm
		System.out.println("______________--");
		int cost=0;
		int posX=0,posY=0;
		int ext= 0; //number of ext.
		searchFront[1].addMaze(parent);
		System.out.println("Waiting for A*....");
		while(true){
			Maze check= searchFront[1].findMinA();
			if(searchFront[1].isEmpty()){
				break;
			}
			if((check.getPos()[0]==check.getG1()[0])&&(check.getPos()[1]==check.getG1()[1])){ //found g1
				System.out.println("A* found G1 with cost:"+check.getG()+". Number of extensions:"+ext);
				check.printPath();
				break;
			}else if((check.getPos()[0]==check.getG2()[0])&&(check.getPos()[1]==check.getG2()[1])){ //found g2
				System.out.println("A* found G2 with cost:"+check.getG()+". Number of extensions:"+ext);
				check.printPath();
				break;
			}else{
				posX=check.getPos()[0];
				posY=check.getPos()[1];
				for(int i=0;i<8;i++){
					if(i==0){
						if(posY==0){
							continue;
						}if(check.isX(posX,posY-1)){
							continue;
						}
					}else if(i==1){
						if((posY==0) || (posX==check.getN()-1)){
							continue;
						}if(check.isX(posX+1,posY-1)){
							continue;
						}
					}else if(i==2){
						if(posX==check.getN()-1){
							continue;
						}if(check.isX(posX+1,posY)){
							continue;
						}
					}else if(i==3){
						if((posY==check.getN()-1) || (posX==check.getN()-1)){
							continue;
						}if(check.isX(posX+1,posY+1)){
							continue;
						}
					}else if(i==4){
						if(posY==check.getN()-1){
							continue;
						}if(check.isX(posX,posY+1)){
							continue;
						}
					}else if(i==5){
						if((posY==check.getN()-1) || (posX==0)){
							continue;
						}if(check.isX(posX-1,posY+1)){
							continue;
						}
					}else if(i==6){
						if(posX==0){
							continue;
						}if(check.isX(posX-1,posY)){
							continue;
						}
					}else if(i==7){
						if((posY==0) || (posX==0)){
							continue;
						}if(check.isX(posX-1,posY-1)){
							continue;
						}
					}searchFront[1].addMaze(this.createKidMaze(check,i));
				}searchFront[1].removeMaze(check);
			}ext++;
		}
	}
}
