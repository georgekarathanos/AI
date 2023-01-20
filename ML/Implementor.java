import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedReader;

class Implementor{
	private final int numberOfClusters = 9; //THIS SHOULD BE A NUMBER > 0
	private final int numberOfExamples = 1200;
	private final int MAX_ITERATIONS = 500;
	private ArrayList<Center> centers; //data structure for the centers
	private Example examples[]; //data structure for the examples

	//this function fills centers/arraylist with the final k centers and updates the 1200 examples from examples/array with the final cluster for each one of them
	public void kmeans(){ //kmeans algorithm implementation
		initializeCenters();
		double exampleX, exampleY, currentDistance;
		double clusterX, clusterY,convergeSUM;
		double currentMedX, currentMedY;
		int converge = 0, iterations = 0, currentNumOfExamples, currentCluster;
		while(converge==0 && iterations<MAX_ITERATIONS){
			for(int i=0;i<numberOfExamples;i++){ //STEP 1 		for every example decide it's cluster
				exampleX = examples[i].getPosX();
				exampleY = examples[i].getPosY();
				for(int j=0;j<numberOfClusters;j++){ //calculate distance from every center and choose the smaller one
					clusterX = centers.get(j).getPosX();
					clusterY = centers.get(j).getPosY();
					currentDistance = examples[i].calculateEuclidianDistanceFromOtherElement(clusterX, clusterY);
					if(examples[i].getMinDistance()>currentDistance || examples[i].getMinDistance()==-1){ //change cluster
						examples[i].setCluster(centers.get(j).getCluster());
					}
					if(examples[i].getCluster()!=-1){ //new distance to the cluster the example belongs
						currentCluster = examples[i].getCluster();
						clusterX = centers.get(currentCluster-1).getPosX();
						clusterY = centers.get(currentCluster-1).getPosY();
						examples[i].setMinDistance(examples[i].calculateEuclidianDistanceFromOtherElement(clusterX, clusterY));
					}
				}
			}

			convergeSUM=0;
			for(int j=0;j<numberOfClusters;j++){ //STEP 2 		for every cluster update it's center
				clusterX=0;
				clusterY=0;
				currentNumOfExamples=0;
				for(int i=0;i<numberOfExamples;i++){ //set new center
					if(centers.get(j).getCluster()==examples[i].getCluster()){
						clusterX+= examples[i].getPosX();
						clusterY+= examples[i].getPosY();
						currentNumOfExamples++;
					}
				}
				currentMedX = clusterX/currentNumOfExamples;
				currentMedY = clusterY/currentNumOfExamples;
				convergeSUM+= centers.get(j).calculateEuclidianDistanceFromOtherElement(currentMedX, currentMedY);
				centers.get(j).setPosition(currentMedX, currentMedY);
			}iterations++;
			System.out.println("Iteration: "+iterations);
			if(convergeSUM==0){ //end if centers haven't moved
				converge=1;
			}
		}
	}

	public double calculateError(){ //returns the clustering error
		double error = 0, clusterX, clusterY;
		int currentCluster;
		for(int i=0;i<numberOfExamples;i++){
			currentCluster = examples[i].getCluster();
			clusterX = centers.get(currentCluster-1).getPosX();
			clusterY = centers.get(currentCluster-1).getPosY();
			error+= examples[i].calculateEuclidianDistanceFromOtherElement(clusterX,clusterY);
		}return error;
	}

	public void setupDataset(){ //generates the examples and saves them in example.txt
		try{
			initializeExamples();
			writeKmeans("examples.txt", true);
		}catch(IOException e){
			System.out.println("Can't export input-data file!");
		}
	}

	public void exportDesicionFiles(){ //export a txt file that labels every example
		try{
			kmeans();
			writeKmeans("kmeans.txt", false);
			writeClusters();
		}catch(IOException e){
			System.out.println("Can't export input-data file!");
		}
	}
	
	public void parseExamples(String fileName){ //parse the examples that we generated
		try{
			BufferedReader reader = new BufferedReader(new java.io.FileReader(fileName));
			String line = reader.readLine();
			String currentArray[];
			double currentX, currentY;
			int examplesCounter = 0;
			examples = new Example[numberOfExamples];
			while(!line.equals("$")){
				line = reader.readLine();
				if(line.equals("$")){
					continue;
				}
				currentArray = line.split(",");
				currentX = Double.parseDouble(currentArray[0]);
				currentY = Double.parseDouble(currentArray[1]);
				examples[examplesCounter] = new Example(currentX,currentY);
				examplesCounter++;
			}
		}catch(IOException e){
			System.out.println("Can't read the examples!");
		}
	}


	public void writeClusters() throws IOException{
		FileWriter writer = new FileWriter("clusters.txt");
		writer.write("Position X,Position Y,Cluster");
      	writer.write("\n");
      	String currentCluster;
      	for(int i=0;i<numberOfClusters;i++){
      		currentCluster="";
      		currentCluster+= centers.get(i).getPosX();
      		currentCluster+=",";
      		currentCluster+= centers.get(i).getPosY();
      		currentCluster+=",";
      		currentCluster+=centers.get(i).getCluster();
      		writer.write(currentCluster);
      		writer.write("\n");
      	}
      	writer.close();
	}

	public void writeKmeans(String name, boolean isInitialization) throws IOException{
		FileWriter writer = new FileWriter(name);
		writer.write("Position X,Position Y,Cluster");
      	writer.write("\n");
      	String currentExample;
      	for(int i=0;i<numberOfExamples;i++){
      		currentExample="";
      		currentExample+= examples[i].getPosX();
      		currentExample+=",";
      		currentExample+= examples[i].getPosY();
      		currentExample+=",";
      		currentExample+= examples[i].getCluster();
      		writer.write(currentExample);
      		writer.write("\n");
      	}if(isInitialization){
      		writer.write("$");//flag for end of the file
      	}
      	writer.close();
	}

	public void initializeExamples(){
		int examplesCounter = 0;
		examples = new Example[numberOfExamples];
		Random rand = new Random();
		double currentX,currentY;
		//initialize the examples
		for(int i=0;i<150;i++){ //random everywhere
			currentX = (double)(rand.nextInt(201))/100; //0...2
			currentY = (double)(rand.nextInt(201))/100; //0...2
			examples[examplesCounter] = new Example(currentX,currentY);
			examplesCounter++;
		}
		for(int i=0;i<150;i++){ //panw deksia
			currentX = (double)(rand.nextInt(51))/100 + 1.5; //1.5...2
			currentY = (double)(rand.nextInt(51))/100 + 1.5; //1.5...2
			examples[examplesCounter] = new Example(currentX,currentY);
			examplesCounter++;
		}
		for(int i=0;i<150;i++){ //katw deksia
			currentX = (double)(rand.nextInt(51))/100 + 1.5; //1.5...2
			currentY = (double)(rand.nextInt(51))/100; //0...0.5
			examples[examplesCounter] = new Example(currentX,currentY);
			examplesCounter++;
		}
		for(int i=0;i<150;i++){ //panw aristera
			currentX = (double)(rand.nextInt(51))/100; //0...0.5
			currentY = (double)(rand.nextInt(51))/100 + 1.5; //1.5...2
			examples[examplesCounter] = new Example(currentX,currentY);
			examplesCounter++;
		}
		for(int i=0;i<150;i++){ //katw aristera
			currentX = (double)(rand.nextInt(51))/100; //0...0.5
			currentY = (double)(rand.nextInt(51))/100; //0...0.5
			examples[examplesCounter] = new Example(currentX,currentY);
			examplesCounter++;
		}
		for(int i=0;i<150;i++){ //kentro
			currentX = (double)(rand.nextInt(41))/100 + 0.8; //0.8...1.2
			currentY = (double)(rand.nextInt(41))/100 + 0.8; //0.8...1.2
			examples[examplesCounter] = new Example(currentX,currentY);
			examplesCounter++;
		}
		for(int i=0;i<75;i++){ //panw
			currentX = (double)(rand.nextInt(41))/100 + 0.8; //0.8...1.2
			currentY = (double)(rand.nextInt(41))/100 + 1.6; //1.6...2
			examples[examplesCounter] = new Example(currentX,currentY);
			examplesCounter++;
		}
		for(int i=0;i<75;i++){ //katw
			currentX = (double)(rand.nextInt(41))/100 + 0.8; //0.8...1.2
			currentY = (double)(rand.nextInt(41))/100; //0...0.4
			examples[examplesCounter] = new Example(currentX,currentY);
			examplesCounter++;
		}
		for(int i=0;i<75;i++){ //aristera
			currentX = (double)(rand.nextInt(41))/100 + 0.3; //0.3...0.7
			currentY = (double)(rand.nextInt(41))/100 + 0.8; //0.8...1.2
			examples[examplesCounter] = new Example(currentX,currentY);
			examplesCounter++;
		}
		for(int i=0;i<75;i++){ //deksia
			currentX = (double)(rand.nextInt(41))/100 + 1.3; //1.3...1.7
			currentY = (double)(rand.nextInt(41))/100 + 0.8; //0.8...1.2
			examples[examplesCounter] = new Example(currentX,currentY);
			examplesCounter++;
		}
		//end of initialization of the examples
	}

	public void initializeCenters(){
		centers = new ArrayList<Center>();
		Random rand = new Random();
		Center currentCenter;
		int randomPosition;
		double currentX,currentY;
		ArrayList<Integer> cache = new ArrayList<Integer>(); //prevent same centers
		for(int i=0;i<numberOfClusters;i++){
			randomPosition =  rand.nextInt(numberOfExamples);
			while(cache.contains(randomPosition)){
				randomPosition =  rand.nextInt(numberOfExamples);
			}cache.add(randomPosition);
			currentX = examples[randomPosition].getPosX();
			currentY = examples[randomPosition].getPosY();
			centers.add(new Center(currentX,currentY, i+1));
		}
	}
}