import java.util.ArrayList;
import java.util.Scanner;

class Implementor{ //ylopoiisi tou algorithmou kai ths rohs paixnidiou.
	private int K;

	public Implementor(int K){
		this.K=K;
	}

	public int getK(){
		return K;
	}

	public Table createFirstTable(int M, int N){ //dimiurgise to arxiko adeio trapezi
		Table first= new Table(M,N);
		return first;
	}

	public Table createKid(Table parent, int column, int player){
		Table kid= new Table(parent, column, player);
		parent.addChild(kid);
		return kid;
	}

	public void createAllKids(Table parent, int player){ //dimiurgise tis kathe pithanes katastaseis-paidia.
		int columns= parent.getN();
		for(int i=0;i<columns;i++){
			if(parent.isAvailable(i)){
				this.createKid(parent, i, player);
			}
		}
	}

	public int minimax(Table t,int player){ //minimax algorithm me anadromi.
		if(t.isFull()){ //final katastasi me isopalia.
			return 0;
		}if(t.wins(getK(),-1)){ //final katastasi me nikiti ton min.
			return -1000;
		}if(t.wins(getK(),1)){ //final katastasi me nikiti ton max.
			return 1000;
		}if(player==1){ //code for max.
			int currentValue= -1000;
			createAllKids(t,1);
			for(int i=0;i<t.getChildren().size();i++){
				int score= minimax(t.getChildren().get(i), -1);
				if(score>currentValue){
					currentValue= score;
					t.getChildren().get(i).setValue(score); //tha prepei na parthei h megalyteri timh.
				}
			}return currentValue;
		}else{ //code for min.
			int currentValue= 1000;
			createAllKids(t,-1);
			for(int i=0;i<t.getChildren().size();i++){
				int score= minimax(t.getChildren().get(i), 1);
				if(score<currentValue){
					currentValue= score;
					t.getChildren().get(i).setValue(score); //tha prepei na parthei h mikroteri timh.
				}
			}return currentValue;
		}
	}

	public void play(Table t, int K){ //roh paixnidiou.
		Scanner input= new Scanner(System.in);
		int column=-1; //metavliti gia thn epilogi toy xristi
		t.printTable();
		if(t.isFull()){ //check if table is full after min played.
			System.out.println("No one won. Game over.");
			return;
		}if(t.wins(K,-1)){ //check if min won.
			System.out.println("______________");
			t.printTable();
			System.out.println("MIN won.");
			return;
		}System.out.println("______________Wait....");
		minimax(t,1); //max will play
		Table enemy= t.getMaxChild();
		if(enemy.wins(K,1)){ //check if max won.
			enemy.printTable();
			System.out.println("MAX won.");
			return;
		}enemy.printTable();
		if(enemy.isFull()){ //check if table is full after max played.
			System.out.println("No one won. Game over.");
			return;
		}
		System.out.println("______________MAX did his move.");
		System.out.print("In which column you want to put 'O'? (Should be between 0 to "+(enemy.getN()-1)+"):");
		column= input.nextInt();
		while(column<0 || column > (enemy.getN()-1) || (!enemy.isAvailable(column))){
			if(column<0 || column > (enemy.getN()-1)){
				System.out.print("In which column you want to put 'O'? (Should be between 0 to "+(enemy.getN()-1)+"):");
			}else if((!enemy.isAvailable(column))){
				System.out.println("That move is forbidden!!!");
				System.out.print("In which column you want to put 'O'? (Should be between 0 to "+(enemy.getN()-1)+"):");
			}column= input.nextInt();
		}enemy.put(-1,column);
		enemy.freeChildren();
		play(enemy,K); //ksanapaikse me th nea katastash pou odigithikes apo tin epilogh toy MIN.
	}
}