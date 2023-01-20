class Example extends Element{
	private double minDistance;

	public Example(double posX, double posY){
		super(posX,posY);
		this.minDistance = -1; //minDistance=1 until the example gets clustered for the first time
	}

	public void setMinDistance(double minDistance){
		this.minDistance = minDistance;
	}

	public double getMinDistance(){
		return minDistance;
	}
}