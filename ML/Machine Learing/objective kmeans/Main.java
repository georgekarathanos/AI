import java.util.Scanner;

class Main{ //User Interface
	public static void main(String[] args) {
		Implementor i = new Implementor();
		Scanner inter= new Scanner(System.in);
		System.out.print("Do you want to generate new dataset first?(type 'yes' if you want to, or type any other thing to keep the old one): ");
		String answer = inter.next();
		if(answer.equals("yes")){ //generate new dataset
			i.setupDataset();
			System.out.println("New dataset is generated");
		}
		System.out.println("---------");
		i.parseExamples("examples.txt"); //fill data structures with dataset values
		i.exportDesicionFiles(); //run kmeans and export results in txt files
		System.out.println("Kmeans ended successfully!");
		System.out.println("Error: "+i.calculateError()); //print kmeans-clustering error
	}
}