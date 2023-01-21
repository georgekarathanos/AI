import java.util.Scanner;

class Main{ //User Interface
	public static void main(String[] args) {
		Implementor i = new Implementor();
		Scanner inter= new Scanner(System.in);
		System.out.print("Do you want to generate new dataset first?(type 'yes' if you want to, or type any other thing to keep the old one): ");
		String answer1 = inter.next();
		if(answer1.equals("yes")){ //generate new dataset
			i.exportSet("data");
			System.out.println("New dataset is generated");
		}else{
			i.parseDataExamples(); //fill data structures with existing testset values
		}
		System.out.println("---------");
		System.out.print("Do you want to generate new testset first?(type 'yes' if you want to, or type any other thing to keep the old one): ");
		String answer2 = inter.next();
		if(answer2.equals("yes")){ //generate new dataset
			i.exportSet("test");
			System.out.println("New dataset is generated");
		}else{
			i.parseTestExamples(); //fill data structures with existing testset values
		}
		System.out.println("Choose function for hidden neurons: (press '1' or '2' or '3')");
		System.out.println("1. ReLu");
		System.out.println("2. Tanh");
		System.out.println("3. Sigmoid");
		int answer3 = inter.nextInt();
		while(answer3!=1 && answer3!=2 && answer3!=3){
			System.out.println("Choose function for hidden neurons: (press '1' or '2' or '3')");
			System.out.println("1. ReLu");
			System.out.println("2. Tanh");
			System.out.println("3. Sigmoid");
			answer3 = inter.nextInt();
		}String hiddenFunc = "";
		if(answer3==1){
			hiddenFunc = "relu";
		}
		if(answer3==2){
			hiddenFunc = "tanh";
		}
		if(answer3==3){
			hiddenFunc = "sigmoid";
		}System.out.println("---------");
		i.gradient_descent(40, hiddenFunc, "sigmoid"); //FIRST ARGUMENT IS NUMBER OF EXAMPLES PER GROUP FOR GRADIENT DESCENT.
		i.exportClassifiedTestset(); //run neural netwrok and export result in the classified.txt file
	}
}