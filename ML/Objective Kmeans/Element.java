import java.lang.Math;

class Element{
	protected double posX;
	protected double posY;
	protected int cluster;

	public Element(double posX, double posY){
		this.posX = posX;
		this.posY = posY;
		this.cluster = -1;
	}

	public void setPosition(double posX, double posY){
		this.posX = posX;
		this.posY = posY;
	}

	public void setCluster(int cluster){
		this.cluster = cluster;
	}

	public double getPosX(){
		return posX;
	}

	public double getPosY(){
		return posY;
	}

	public int getCluster(){
		return cluster;
	}

	public double calculateEuclidianDistanceFromOtherElement(double otherPosX, double otherPosY){
		return Math.sqrt(Math.pow(otherPosX - posX, 2) + Math.pow(otherPosY - posY, 2));
	}
}