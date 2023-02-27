import java.util.Scanner;

class Main{
	public static void main(String[] args) {
		Implementor player= new Implementor();
		Scanner inter= new Scanner(System.in);
		System.out.print("Give dimension of maze(e.g. enter 5 for 5x5): ");
		int d= inter.nextInt();
		while(d<0 || d==0){
			System.out.println("Dimension must be a positive number!!!");
			System.out.print("Give dimension of maze(e.g. enter 5 for 5x5): ");
			d= inter.nextInt();
		}
		System.out.print("Give X position for S(beggining pos): ");
		int sX= inter.nextInt();
		while(sX<0 || sX>=d){
			System.out.println("Enter a number between 0 and "+(d-1));
			System.out.print("Give X position for S(beggining pos): ");
			sX= inter.nextInt();
		}
		System.out.print("Give Y position for S(beggining pos): ");
		int sY= inter.nextInt();
		while(sY<0 || sY>=d){
			System.out.println("Enter a number between 0 and "+(d-1));
			System.out.print("Give Y position for S(beggining pos): ");
			sY= inter.nextInt();
		}
		System.out.print("Give X position for G1(exit): ");
		int g1X= inter.nextInt();
		while(g1X<0 || g1X>=d){
			System.out.println("Enter a number between 0 and "+(d-1));
			System.out.print("Give X position for G1(exit): ");
			g1X= inter.nextInt();
		}
		System.out.print("Give Y position for G1(exit): ");
		int g1Y= inter.nextInt();
		while(g1Y<0 || g1Y>=d){
			System.out.println("Enter a number between 0 and "+(d-1));
			System.out.print("Give Y position for G1(exit): ");
			g1Y= inter.nextInt();
		}
		System.out.print("Give X position for G2(exit): ");
		int g2X= inter.nextInt();
		while(g2X<0 || g2X>=d){
			System.out.println("Enter a number between 0 and "+(d-1));
			System.out.print("Give X position for G2(exit): ");
			g2X= inter.nextInt();
		}
		System.out.print("Give Y position for G2(exit): ");
		int g2Y= inter.nextInt();
		while(g2Y<0 || g2Y>=d){
			System.out.println("Enter a number between 0 and "+(d-1));
			System.out.print("Give Y position for G2(exit): ");
			g2Y= inter.nextInt();
		}
		System.out.print("Give probability to block a cell: ");
		double prob= inter.nextDouble(); //probability to block a cell
		while(prob>1 || prob<0){
			System.out.print("Probability must be between 0...1. Give probability to block a cell: ");
			prob= inter.nextDouble();
		}Maze parent= player.createFirstMaze(d,sX,sY,g1X,g1Y,g2X,g2Y,prob);
		System.out.println("1. UCS\n2. A*\n3. Both");
		System.out.print("Which method you want to check? Press 1 or 2 or 3: ");
		int method= inter.nextInt(); //choose algorithm
		while((method!=1)&&(method!=2)&&(method!=3)){
			System.out.print("Read carefully! Press 1 for UCS or 2 for A* or 3 for both of them: ");
			method= inter.nextInt();
		}parent.printInfo(); //print info for the begining
		parent.print();
		if(method==1){
			player.UCS(parent);
		}else if(method==2){
			player.A(parent);
		}else{
			player.A(parent);
			player.UCS(parent);
		}
	}
}