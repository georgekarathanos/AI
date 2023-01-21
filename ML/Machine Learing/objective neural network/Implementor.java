import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.lang.Math;

class Implementor{
	private ArrayList<Example> dataset; //data structure for dataset
	private ArrayList<Example> testset; //data structure for testset
	private ArrayList<Example> classifiedset; //data structure for output
	private final int numberOfExamples = 4000;
	private final int numberOfClasses = 3; //always 3 for this implementation
	private final int inputDimension = 2; //x,y
	private final int lenH1 = 15; //number of neurons in the first hidden layer
	private final int lenH2 = 15; //number of neurons in the second hidden layer
	private final int lenH3 = 15; //number of neurons in the third hidden layer
	private final int MAX_EPOCHS = 2000;
	private final int MIN_EPOCHS = 700;
	private final double TERMINATE_TRAINING_THRESHOLD = 0.001;
	private ArrayList<ArrayList<Neuron>> neurons;
	private ArrayList<ArrayList<ArrayList<Double>>> weights; //structure for weights
	private ArrayList<ArrayList<ArrayList<Double>>> partial_derivatives; //structure for derivatives of the weights
	private ArrayList<ArrayList<ArrayList<Double>>> partial_derivatives_sum; //structure that holds the sum for each weight in gradient descent
	private double n = 0.01; //learning rate

	public void neural_network(){ //classifies the testset based on the trained neural netowork
		classifiedset =  new ArrayList<Example>();
		double currentX, currentY;
		int currentClass, counter = 0, correctCounter = 0;
		double[] x, y={0,0,0};
		for(int i=0;i<testset.size();i++){
			currentX = testset.get(i).getPosX();
			currentY = testset.get(i).getPosY();
			x = encodeInput(currentX, currentY); //input
			forward_pass(x, inputDimension, y, numberOfClasses); //y has the output vector
			currentClass = findCategory(y);
			classifiedset.add(new Example(currentX, currentY));
			classifiedset.get(counter).setCorrectCategory(currentClass);
			if(classifiedset.get(counter).getCorrectCategory() == testset.get(counter).getCorrectCategory()){
				correctCounter++;
			}else{
				classifiedset.get(counter).setCorrectCategory(4); //put 4 as category for mistaken classifications so we recognize it at plot drawing.
			}
			counter++;
		} double correctPercentage = (double)(correctCounter)/numberOfExamples;
		correctPercentage = correctPercentage*100;
		System.out.println("Percentage of correct classified examples = "+ correctPercentage+"%");
	}

	public void gradient_descent(int B, String hiddenNeuronsFunction, String outputNeuronsFunction){ //gradient descent algorithm for nn's training
		initializeStructures(hiddenNeuronsFunction, outputNeuronsFunction); //STEP1: INITIALIZE ALL THE STRUCTURES
		double[] x,t, currentOutput;
		int epoch=0, counter;
		double pastError=-10, newError=0;
		while(epoch<MAX_EPOCHS/*wait until the error dont differ a lot in 2 iterations*/){ //STEP 2: FOR EACH EPOCH DO THE ALGORITHM
			setToZero(partial_derivatives); //set all derivatives to 0.
			counter = 0;
			for(int i=0;i<dataset.size();i++){ //for each example
				//x=[example.x,example.y].........t=[encode(example.category)]
				x = encodeInput(dataset.get(i).getPosX(), dataset.get(i).getPosY());
				t = encodeCategory(dataset.get(i).getCorrectCategory());
				currentOutput = back_prop(x,inputDimension, t,numberOfClasses); //do backpropagation
				dataset.get(i).setRealCategory(currentOutput); //keep the real outputs
				updatesum(); //update sum
				counter++;
				if(counter==B){ //update weights if current group is over
					counter=0;
					updateWeights();
					setToZero(partial_derivatives_sum);
				}
			}epoch++;
			newError = calculateError();
			System.out.println("Epoch: "+epoch+" with error= "+ newError); //print error for this epoch
			if(epoch>MIN_EPOCHS && Math.abs(newError-pastError)<TERMINATE_TRAINING_THRESHOLD){ break; } //stop training if error dont differ and at least 700 epochs has passed
			pastError = newError;
		}System.out.println("Neural Network trained successfully!");
	}

	public void updateWeights(){
		double newWeight;
		for (int i = 0; i < weights.size(); i++) {
		    for (int j = 0; j < weights.get(i).size(); j++) {
		        for (int k = 0; k < weights.get(i).get(j).size(); k++) {
		        	newWeight = weights.get(i).get(j).get(k) - n*partial_derivatives_sum.get(i).get(j).get(k);
		        	weights.get(i).get(j).set(k, newWeight);
		        }
		    }
		}
	}

	public double calculateError(){
		double sum=0, currentDistance;
		double[] currentCorrectCategory, currentRealCategory;
		for(int i=0;i<dataset.size();i++){
			currentDistance = dataset.get(i).calculateDifference();
			sum += currentDistance;
		}return sum*0.5;
	}

	public double[] back_prop(double[] x, int d, double[] t, int K){ //for input x calculates weights- partial derivatives
		double[] realOutput = new double[K];
		forward_pass(x,d,realOutput,K);

		//STEP 1: CALCULATE ERROR FOR EACH NEURON
		double currentTerm;
		for(int i = 3;i>=0;i--){ //for each layer
			if(i==3){ //output layer
				currentTerm = 0;
				for(int j = 0;j<numberOfClasses;j++){ //for each neuron
					currentTerm = realOutput[j] - t[j];
					neurons.get(i).get(j).setD_error(currentTerm);
				}
			}
			else if(i==2){ //3rd hidden layer
				for(int j = 0;j<lenH3;j++){ //for each neuron
					currentTerm = 0;
					for(int k=0;k<numberOfClasses;k++){
						currentTerm += weights.get(i+1).get(j).get(k)*neurons.get(i+1).get(k).getD_error();
					}
					neurons.get(i).get(j).setD_error(currentTerm);
				}
			}
			else if(i==1){ //2nd hidden layer
				for(int j = 0;j<lenH2;j++){ //for each neuron
					currentTerm = 0;
					for(int k=0;k<lenH3;k++){
						currentTerm += weights.get(i+1).get(j).get(k)*neurons.get(i+1).get(k).getD_error();
					}
					neurons.get(i).get(j).setD_error(currentTerm);
				}
			}
			else if(i==0){ //1st hidden layer
				for(int j = 0;j<lenH1;j++){ //for each neuron
					currentTerm = 0;
					for(int k=0;k<lenH2;k++){
						currentTerm += weights.get(i+1).get(j).get(k)*neurons.get(i+1).get(k).getD_error();
					}
					neurons.get(i).get(j).setD_error(currentTerm);
				}
			}
		}

		//STEP 2: CALCULATE PARTIAL DERIVATIVE FOR EACH WEIGHT (their initialized values are random)
		double currentDerivative;
		for(int i=0;i<4;i++){ //for each weight-layer
			if(i==0){ //input layer
				for(int j=0; j<d; j++){ //for each neuron except the first polarization
					for(int k=0;k<lenH1;k++){ //for each weight coming from neuron j
						currentDerivative = neurons.get(i).get(k).getD_error() * x[j];
						partial_derivatives.get(i).get(j).set(k, currentDerivative);
					}
				}
				for(int k=0;k<lenH1;k++){ //for each weight coming from the first polarization
					currentDerivative = neurons.get(i).get(k).getD_error();
					partial_derivatives.get(i).get(d).set(k, currentDerivative);
				}
			}
			else if(i==1){ //first layer
				for(int j=0; j<lenH1; j++){ //for each neuron except the second polarization
					for(int k=0;k<lenH2;k++){ //for each weight coming from neuron j
						currentDerivative = neurons.get(i).get(k).getD_error() * neurons.get(i-1).get(j).getOutput();
						partial_derivatives.get(i).get(j).set(k, currentDerivative);
					}
				}
				for(int k=0;k<lenH2;k++){ //for each weight coming from the second polarization
					currentDerivative = neurons.get(i).get(k).getD_error();
					partial_derivatives.get(i).get(lenH1).set(k, currentDerivative);
				}
			}
			else if(i==2){ //second layer
				for(int j=0; j<lenH2; j++){ //for each neuron except the third polarization
					for(int k=0;k<lenH3;k++){ //for each weight coming from neuron j
						currentDerivative = neurons.get(i).get(k).getD_error() * neurons.get(i-1).get(j).getOutput();
						partial_derivatives.get(i).get(j).set(k, currentDerivative);
					}
				}
				for(int k=0;k<lenH3;k++){ //for each weight coming from the third polarization
					currentDerivative = neurons.get(i).get(k).getD_error();
					partial_derivatives.get(i).get(lenH2).set(k, currentDerivative);
				}
			}
			else if(i==3){ //third layer
				for(int j=0; j<lenH3; j++){ //for each neuron except the fourth polarization
					for(int k=0;k<K;k++){ //for each weight coming from neuron j
						currentDerivative = neurons.get(i).get(k).getD_error() * neurons.get(i-1).get(j).getOutput();
						partial_derivatives.get(i).get(j).set(k, currentDerivative);
					}
				}
				for(int k=0;k<K;k++){ //for each weight coming from the fourth polarization
					currentDerivative = neurons.get(i).get(k).getD_error();
					partial_derivatives.get(i).get(lenH3).set(k, currentDerivative);
				}
			}
		}
		return realOutput;
	}

	public void forward_pass(double[] x, int d, double[] y, int K){ //for input x gives the output y
		double result;
		for(int i = 0;i<4;i++){ //for each layer
			if(i==0){ //1st hidden layer
				for(int j = 0; j<lenH1; j++){ //for each neuron
					result = 0;
					for(int k = 0;k<d;k++){ //for every input weight coming from neurons
						result += x[k]*weights.get(i).get(k).get(j); 
					}result += weights.get(i).get(d).get(j);
					neurons.get(i).get(j).setInput(result);
					neurons.get(i).get(j).setOutput();
				}
			}
			else if(i==1){ //2nd hidden layer
				for(int j = 0; j<lenH2; j++){ //for each neuron
					result = 0;
					for(int k = 0;k<lenH1;k++){ //for every input weight coming from neurons
						result += neurons.get(i-1).get(k).getOutput()*weights.get(i).get(k).get(j); 
					}result += weights.get(i).get(lenH1).get(j);
					neurons.get(i).get(j).setInput(result);
					neurons.get(i).get(j).setOutput();
				}
			}
			else if(i==2){ //3rd hidden layer
				for(int j = 0; j<lenH3; j++){ //for each neuron
					result = 0;
					for(int k = 0;k<lenH2;k++){ //for every input weight coming from neurons
						result += neurons.get(i-1).get(k).getOutput()*weights.get(i).get(k).get(j); 
					}result += weights.get(i).get(lenH2).get(j);
					neurons.get(i).get(j).setInput(result);
					neurons.get(i).get(j).setOutput();
				}
			}
			else if(i==3){ //output layer
				for(int j = 0; j<numberOfClasses; j++){ //for each neuron
					result = 0;
					for(int k = 0;k<lenH3;k++){ //for every input weight coming from neurons
						result += neurons.get(i-1).get(k).getOutput()*weights.get(i).get(k).get(j); 
					}result += weights.get(i).get(lenH3).get(j);
					neurons.get(i).get(j).setInput(result);
					neurons.get(i).get(j).setOutput();
				}
			}
		}
		for(int i = 0;i<numberOfClasses;i++){
			y[i] = neurons.get(3).get(i).getOutput();
		}
	}

	public void initializeStructures(String function, String outFunction){ //this function must be called before the training to make all structures available
		//INITIALIZE NEURONS AND ERRORS
		neurons = new ArrayList<ArrayList<Neuron>>();
		for(int i=0;i<4;i++){ //initialize neurons
			ArrayList<Neuron> neuronLayer = new ArrayList<Neuron>();
			if(i==0){ //1st hidden layer
				for(int j = 0; j<lenH1; j++){ //for each neuron
					neuronLayer.add(new Neuron(function));
				}
			}
			else if(i==1){ //2nd hidden layer
				for(int j = 0; j<lenH2; j++){ //for each neuron
					neuronLayer.add(new Neuron(function));
				}
			}
			else if(i==2){ //3rd hidden layer
				for(int j = 0; j<lenH3; j++){ //for each neuron
					neuronLayer.add(new Neuron(function));
				}
			}
			else if(i==3){ //output layer
				for(int j = 0; j<numberOfClasses; j++){ //for each neuron
					neuronLayer.add(new Neuron(outFunction));
				}
			}neurons.add(neuronLayer);
		}

		//INITIALIZE WEIGHTS
		weights = new ArrayList<ArrayList<ArrayList<Double>>>(); //length=4
		partial_derivatives = new ArrayList<ArrayList<ArrayList<Double>>>();
		partial_derivatives_sum = new ArrayList<ArrayList<ArrayList<Double>>>();
		Random rand = new Random();
		double randomWeight;
		for(int i=0;i<4;i++){ //for ever layer
			ArrayList<ArrayList<Double>> layer = new ArrayList<ArrayList<Double>>(); //holds neurons for a layer
			ArrayList<ArrayList<Double>> layer_deri = new ArrayList<ArrayList<Double>>(); //holds neurons for a layer
			ArrayList<ArrayList<Double>> layer_deri_sum = new ArrayList<ArrayList<Double>>(); //holds neurons for a layer
			if(i==0){//input layer
				for(int j =0;j<3;j++){ //for each neuron
					ArrayList<Double> ws = new ArrayList<Double>(); //holds weight for a neuron
					ArrayList<Double> ws_deri = new ArrayList<Double>(); //holds weight for a neuron
					ArrayList<Double> ws_deri_sum = new ArrayList<Double>(); //holds weight for a neuron
					for(int k =0;k<lenH1;k++){ //for each weight
						randomWeight = (double)(rand.nextInt(201))/100 - 1.0; //-1...1
						ws.add(randomWeight);
						ws_deri.add(0.0);
						ws_deri_sum.add(0.0);
					}layer.add(ws);
					layer_deri.add(ws_deri);
					layer_deri_sum.add(ws_deri_sum);
				}
			}else if(i==1){//1st hidden
				for(int j =0;j<=lenH1;j++){ //for each neuron
					ArrayList<Double> ws = new ArrayList<Double>();
					ArrayList<Double> ws_deri = new ArrayList<Double>(); //holds weight for a neuron
					ArrayList<Double> ws_deri_sum = new ArrayList<Double>(); //holds weight for a neuron
					for(int k =0;k<lenH2;k++){ //for each weight
						randomWeight = (double)(rand.nextInt(201))/100 - 1.0; //-1...1
						ws.add(randomWeight);
						ws_deri.add(0.0);
						ws_deri_sum.add(0.0);
					}layer.add(ws);
					layer_deri.add(ws_deri);
					layer_deri_sum.add(ws_deri_sum);
				}
			}else if(i==2){//2nd hidden
				for(int j =0;j<=lenH2;j++){ //for each neuron
					ArrayList<Double> ws = new ArrayList<Double>();
					ArrayList<Double> ws_deri = new ArrayList<Double>(); //holds weight for a neuron
					ArrayList<Double> ws_deri_sum = new ArrayList<Double>(); //holds weight for a neuron
					for(int k =0;k<lenH3;k++){ //for each weight
						randomWeight = (double)(rand.nextInt(201))/100 - 1.0; //-1...1
						ws.add(randomWeight);
						ws_deri.add(0.0);
						ws_deri_sum.add(0.0);
					}layer.add(ws);
					layer_deri.add(ws_deri);
					layer_deri_sum.add(ws_deri_sum);
				}
			}else if(i==3){//3rd hidden
				for(int j =0;j<=lenH3;j++){ //for each neuron
					ArrayList<Double> ws = new ArrayList<Double>();
					ArrayList<Double> ws_deri = new ArrayList<Double>(); //holds weight for a neuron
					ArrayList<Double> ws_deri_sum = new ArrayList<Double>(); //holds weight for a neuron
					for(int k =0;k<numberOfClasses;k++){ //for each weight
						randomWeight = (double)(rand.nextInt(201))/100 - 1.0; //-1...1
						ws.add(randomWeight);
						ws_deri.add(0.0);
						ws_deri_sum.add(0.0);
					}layer.add(ws);
					layer_deri.add(ws_deri);
					layer_deri_sum.add(ws_deri_sum);
				}
			}weights.add(layer);
			partial_derivatives.add(layer_deri);
			partial_derivatives_sum.add(layer_deri_sum);
		}
	}

	//ALL THE FUNCTIONS BELOW ARE HELP-METHODS FOR THE MANAGEMENT OF -> THE FILES AND THE GENERAL CODE.
	public void exportClassifiedTestset(){
		try{
			neural_network();
			FileWriter writer = new FileWriter("classified.txt");
			writer.write("Position X,Position Y,Class");
	      	writer.write("\n");
	      	String currentExample;
	      	for(int i=0;i<numberOfExamples;i++){
	      		currentExample="";
	      		currentExample+= classifiedset.get(i).getPosX();
	      		currentExample+=",";
	      		currentExample+= classifiedset.get(i).getPosY();
	      		currentExample+=",";
	      		currentExample+= classifiedset.get(i).getCorrectCategory();
	      		writer.write(currentExample);
	      		writer.write("\n");
	      	}writer.close();
		}catch(IOException e){
			System.out.println("Can't export output file!");
		}
	}

	public void exportSet(String kindOfSet){ //kindOfSet=dataset or testset
		try{
			FileWriter writer;
			if(kindOfSet.equals("data")){
				initializeDataset();
				writer = new FileWriter("dataset.txt");
				writer.write("Position X,Position Y,Class");
		      	writer.write("\n");
		      	String currentClass;
		      	for(int i=0;i<numberOfExamples;i++){
		      		currentClass="";
		      		currentClass+= dataset.get(i).getPosX();
		      		currentClass+=",";
		      		currentClass+= dataset.get(i).getPosY();
		      		currentClass+=",";
		      		currentClass+= dataset.get(i).getCorrectCategory();
		      		writer.write(currentClass);
		      		writer.write("\n");
		      	}writer.write("$");//flag for end of the file
		      	writer.close();
			}else{
				initializeTestset();
				writer = new FileWriter("testset.txt");
				writer.write("Position X,Position Y,Class");
		      	writer.write("\n");
		      	String currentClass;
		      	for(int i=0;i<numberOfExamples;i++){
		      		currentClass="";
		      		currentClass+= testset.get(i).getPosX();
		      		currentClass+=",";
		      		currentClass+= testset.get(i).getPosY();
		      		currentClass+=",";
		      		currentClass+= testset.get(i).getCorrectCategory();
		      		writer.write(currentClass);
		      		writer.write("\n");
		      	}writer.write("$");//flag for end of the file
		      	writer.close();
			}
		}catch(IOException e){
			if(kindOfSet.equals("data")){
				System.out.println("Can't export dataset.");
			}else{
				System.out.println("Can't export testset.");
			}
		}
	}

	public void parseDataExamples(){ //parse the examples that we generated
		try{
			BufferedReader reader = new BufferedReader(new java.io.FileReader("dataset.txt"));
			String line = reader.readLine();
			String currentArray[];
			double currentX, currentY;
			dataset = new ArrayList<Example>();
			int counter = 0;
			while(!line.equals("$")){
				line = reader.readLine();
				if(line.equals("$")){
					continue;
				}
				currentArray = line.split(",");
				currentX = Double.parseDouble(currentArray[0]);
				currentY = Double.parseDouble(currentArray[1]);
				dataset.add(new Example(currentX, currentY));
				dataset.get(counter).setCorrectCategory(Integer.parseInt(currentArray[2]));
				counter++;
			}
		}catch(IOException e){
			System.out.println("Can't read the examples!");
		}
	}
	
	public void parseTestExamples(){ //parse the examples that we generated
		try{
			BufferedReader reader = new BufferedReader(new java.io.FileReader("testset.txt"));
			String line = reader.readLine();
			String currentArray[];
			double currentX, currentY;
			testset = new ArrayList<Example>();
			int counter = 0;
			while(!line.equals("$")){
				line = reader.readLine();
				if(line.equals("$")){
					continue;
				}
				currentArray = line.split(",");
				currentX = Double.parseDouble(currentArray[0]);
				currentY = Double.parseDouble(currentArray[1]);
				testset.add(new Example(currentX, currentY));
				testset.get(counter).setCorrectCategory(Integer.parseInt(currentArray[2]));
				counter++;
			}
		}catch(IOException e){
			System.out.println("Can't read the examples!");
		}
	}

	public void initializeDataset(){
		dataset = new ArrayList<Example>();
		int examplesCounter=0;
		Random rand = new Random();
		double currentX,currentY;
		//initialize the examples
		for(int i=0;i<numberOfExamples;i++){ //for currentX,currentY we have 3 decimal digits
			currentX = rand.nextFloat()*2 - 1; //-1...1
			currentY = rand.nextFloat()*2 - 1; //-1...1
			dataset.add(new Example(currentX, currentY));
			if((Math.pow(currentX - 0.5, 2) + Math.pow(currentY - 0.5, 2) < 0.2) && currentY>0.5){//1
				dataset.get(examplesCounter).setCorrectCategory(1);
			}else if((Math.pow(currentX - 0.5, 2) + Math.pow(currentY - 0.5, 2) < 0.2) && currentY<0.5){//2
				dataset.get(examplesCounter).setCorrectCategory(2);
			}else if((Math.pow(currentX + 0.5, 2) + Math.pow(currentY + 0.5, 2) < 0.2) && currentY>-0.5){//3
				dataset.get(examplesCounter).setCorrectCategory(1);
			}else if((Math.pow(currentX + 0.5, 2) + Math.pow(currentY + 0.5, 2) < 0.2) && currentY<-0.5){//4
				dataset.get(examplesCounter).setCorrectCategory(2);
			}else if((Math.pow(currentX - 0.5, 2) + Math.pow(currentY + 0.5, 2) < 0.2) && currentY>-0.5){//5
				dataset.get(examplesCounter).setCorrectCategory(1);
			}else if((Math.pow(currentX - 0.5, 2) + Math.pow(currentY + 0.5, 2) < 0.2) && currentY<-0.5){//6
				dataset.get(examplesCounter).setCorrectCategory(2);
			}else if((Math.pow(currentX + 0.5, 2) + Math.pow(currentY - 0.5, 2) < 0.2) && currentY>0.5){//7
				dataset.get(examplesCounter).setCorrectCategory(1);
			}else if((Math.pow(currentX + 0.5, 2) + Math.pow(currentY - 0.5, 2) < 0.2) && currentY<0.5){//8
				dataset.get(examplesCounter).setCorrectCategory(2);
			}else{//9
				dataset.get(examplesCounter).setCorrectCategory(3);
			}
			examplesCounter++;
		}
		//end of initialization of the examples
	}

	public void initializeTestset(){
		testset = new ArrayList<Example>();
		int examplesCounter=0;
		Random rand = new Random();
		double currentX,currentY;
		//initialize the examples
		for(int i=0;i<numberOfExamples;i++){ //for currentX,currentY we have 3 decimal digits
			currentX = rand.nextFloat()*2 - 1; //-1...1
			currentY = rand.nextFloat()*2 - 1; //-1...1
			testset.add(new Example(currentX, currentY));
			if((Math.pow(currentX - 0.5, 2) + Math.pow(currentY - 0.5, 2) < 0.2) && currentY>0.5){//1
				testset.get(examplesCounter).setCorrectCategory(1);
			}else if((Math.pow(currentX - 0.5, 2) + Math.pow(currentY - 0.5, 2) < 0.2) && currentY<0.5){//2
				testset.get(examplesCounter).setCorrectCategory(2);
			}else if((Math.pow(currentX + 0.5, 2) + Math.pow(currentY + 0.5, 2) < 0.2) && currentY>-0.5){//3
				testset.get(examplesCounter).setCorrectCategory(1);
			}else if((Math.pow(currentX + 0.5, 2) + Math.pow(currentY + 0.5, 2) < 0.2) && currentY<-0.5){//4
				testset.get(examplesCounter).setCorrectCategory(2);
			}else if((Math.pow(currentX - 0.5, 2) + Math.pow(currentY + 0.5, 2) < 0.2) && currentY>-0.5){//5
				testset.get(examplesCounter).setCorrectCategory(1);
			}else if((Math.pow(currentX - 0.5, 2) + Math.pow(currentY + 0.5, 2) < 0.2) && currentY<-0.5){//6
				testset.get(examplesCounter).setCorrectCategory(2);
			}else if((Math.pow(currentX + 0.5, 2) + Math.pow(currentY - 0.5, 2) < 0.2) && currentY>0.5){//7
				testset.get(examplesCounter).setCorrectCategory(1);
			}else if((Math.pow(currentX + 0.5, 2) + Math.pow(currentY - 0.5, 2) < 0.2) && currentY<0.5){//8
				testset.get(examplesCounter).setCorrectCategory(2);
			}else{//9
				testset.get(examplesCounter).setCorrectCategory(3);
			}
			examplesCounter++;
		}
		//end of initialization of the examples
	}

	public void updatesum(){
		double new_sum;
		for (int i = 0; i < weights.size(); i++) {
		    for (int j = 0; j < weights.get(i).size(); j++) {
		        for (int k = 0; k < weights.get(i).get(j).size(); k++) {
		        	new_sum = partial_derivatives_sum.get(i).get(j).get(k) + partial_derivatives.get(i).get(j).get(k);
		        	partial_derivatives_sum.get(i).get(j).set(k, new_sum);
		        }
		    }
		}
	}

	public void setToZero(ArrayList<ArrayList<ArrayList<Double>>> list) {
	    for (ArrayList<ArrayList<Double>> outerList : list) {
	        for (ArrayList<Double> innerList : outerList) {
	            for (int i = 0; i < innerList.size(); i++) {
	                innerList.set(i, 0.0);
	            }
	        }
	    }
	}

	public int findCategory(double[] array){
		double maxValue = array[0];
		int maxPosition = 0;
		for(int i=0; i<array.length;i++){
			if(array[i]>maxValue){
				maxValue=array[i];
				maxPosition=i;
			}
		}return maxPosition+1;
	}

	public double[] encodeCategory(int category){
		double[] output = {0,0,0};
		if(category==1){
			output[0]=1;
		}
		else if(category==2){
			output[1]=1;
		}
		if(category==3){
			output[2]=1;
		}
		return output;
	}

	public double[] encodeInput(double inputX, double inputY){
		double[] input = {inputX, inputY};
		return input;
	}
}