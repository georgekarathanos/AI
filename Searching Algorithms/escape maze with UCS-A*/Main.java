import java.util.Scanner;

class Main{
	public static void main(String[] args) {
		Implementor player= new Implementor();
		Scanner inter= new Scanner(System.in);
		System.out.print("Give dimension of maze(e.g. enter 5 for 5x5): ");
		int d= inter.nextInt(); //diastasi lavyrinthu
		while(d<0 || d==0){
			System.out.println("Dimension must be a positive number!!!");
			System.out.print("Give dimension of maze(e.g. enter 5 for 5x5): ");
			d= inter.nextInt(); //diastasi lavyrinthu
		}
		System.out.print("Give X position for S(beggining pos): ");
		int sX= inter.nextInt(); //x sydetagmeni toy S.
		while(sX<0 || sX>=d){
			System.out.println("Enter a number between 0 and "+(d-1));
			System.out.print("Give X position for S(beggining pos): ");
			sX= inter.nextInt(); //x sydetagmeni toy S.
		}
		System.out.print("Give Y position for S(beggining pos): ");
		int sY= inter.nextInt(); //y sydetagmeni toy S.
		while(sY<0 || sY>=d){
			System.out.println("Enter a number between 0 and "+(d-1));
			System.out.print("Give Y position for S(beggining pos): ");
			sY= inter.nextInt(); //x sydetagmeni toy S.
		}
		System.out.print("Give X position for G1(exit): ");
		int g1X= inter.nextInt(); //x sydetagmeni toy G1.
		while(g1X<0 || g1X>=d){
			System.out.println("Enter a number between 0 and "+(d-1));
			System.out.print("Give X position for G1(exit): ");
			g1X= inter.nextInt(); //x sydetagmeni toy S.
		}
		System.out.print("Give Y position for G1(exit): ");
		int g1Y= inter.nextInt(); //y sydetagmeni toy G1.
		while(g1Y<0 || g1Y>=d){
			System.out.println("Enter a number between 0 and "+(d-1));
			System.out.print("Give Y position for G1(exit): ");
			g1Y= inter.nextInt(); //x sydetagmeni toy S.
		}
		System.out.print("Give X position for G2(exit): ");
		int g2X= inter.nextInt(); //x sydetagmeni toy G2.
		while(g2X<0 || g2X>=d){
			System.out.println("Enter a number between 0 and "+(d-1));
			System.out.print("Give X position for G2(exit): ");
			g2X= inter.nextInt(); //x sydetagmeni toy S.
		}
		System.out.print("Give Y position for G2(exit): ");
		int g2Y= inter.nextInt(); //y sydetagmeni toy G2.
		while(g2Y<0 || g2Y>=d){
			System.out.println("Enter a number between 0 and "+(d-1));
			System.out.print("Give Y position for G2(exit): ");
			g2Y= inter.nextInt(); //x sydetagmeni toy S.
		}
		System.out.print("Give probability to block a cell: ");
		double prob= inter.nextDouble(); //pithanotita na exei empodio to kathe keli.
		while(prob>1 || prob<0){
			System.out.print("Probability must be between 0...1. Give probability to block a cell: ");
			prob= inter.nextDouble();
		}Maze parent= player.createFirstMaze(d,sX,sY,g1X,g1Y,g2X,g2Y,prob);
		System.out.println("1. UCS\n2. A*\n3. Both");
		System.out.print("Which method you want to check? Press 1 or 2 or 3: ");
		int method= inter.nextInt(); //epilogh algorithmou.
		while((method!=1)&&(method!=2)&&(method!=3)){
			System.out.print("Read carefully! Press 1 for UCS or 2 for A* or 3 for both of them: ");
			method= inter.nextInt();
		}parent.printInfo(); //typwse plirofories ths arxikhs katastashs
		parent.print(); //typwse thn arxikh katastash
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