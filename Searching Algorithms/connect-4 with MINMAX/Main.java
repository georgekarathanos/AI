//Use M=4, N=4, K=3 to play the game.
//For more complicated input there is going to be a problem with the memory, as minimax searches for the final conditions.
//You can use less complicated inputs like: M=3, N=3, K=3.

import java.util.Scanner;

class Main{
	public static void main(String[] args) {
		//USE THIS CODE TO GET INPUTS FROM USER
		/*
		Scanner input= new Scanner(System.in);
		System.out.print("Give number of rows M: ");
		int M= input.nextInt(); //dwse arithmo grammwn.
		System.out.print("Give number of columns N: ");
		int N= input.nextInt(); //dwse arithmo sthlwn.
		System.out.print("Give K: ");
		int K= input.nextInt(); //dwse to K.
		*/
		//USE THIS CODE TO USE CONSTANTS AS INPUT FOR THE ALGORITHM
		int K = 3;
		int M = 4;
		int N = 4;
		Implementor i= new Implementor(K);
		Table t= new Table(M,N);
		i.play(t,K); //paikse to paixnidi.
	}
}