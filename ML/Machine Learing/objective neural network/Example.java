class Example{
	private double posX;
	private double posY;
	private int correctCategory;
	private double[] realCategory;

	public Example(double posX, double posY){
		this.posX = posX;
		this.posY = posY;
		this.realCategory = new double[3];
		this.correctCategory = -1;
	}

	public double calculateDifference(){
		double[] encodedCorrectCategory = {0,0,0};
		if(correctCategory==1){
			encodedCorrectCategory[0] = 1;
		}else if(correctCategory==2){
			encodedCorrectCategory[1] = 1;
		}else if(correctCategory==3){
			encodedCorrectCategory[2] = 1;
		}
		return calculateEuclidianDistance(encodedCorrectCategory, realCategory);
	}

	public double calculateEuclidianDistance(double[] array1, double[] array2){
		double distance = 0;
		for(int i=0;i<3;i++){
			distance += Math.pow(array1[i] - array2[i],2);
		}
		return Math.sqrt(distance);
	}

	public void setPosition(double posX, double posY){
		this.posX = posX;
		this.posY = posY;
	}

	public void setRealCategory(double[] category){
		for(int i=0;i<category.length;i++){
			realCategory[i] = category[i];
		}
	}

	public void setCorrectCategory(int category){
		correctCategory = category;
	}

	public double getPosX(){
		return posX;
	}

	public double getPosY(){
		return posY;
	}

	public int getCorrectCategory(){
		return correctCategory;
	}

	public double[] getRealCategory(){
		return realCategory;
	}
}