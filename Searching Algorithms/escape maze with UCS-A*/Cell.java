class Cell{ //keli toy lavyrinthou.
	boolean free; // free=1 when the cell has not obstacle.
	int value;

	public Cell(boolean free, int value){
		this.free=free;
		this.value=value;
	}

	public Cell(Cell other){
		this.free=other.free;
		this.value=other.value;
	}

	public boolean getCondition(){
		return free;
	}

	public int getValue(){
		return value;
	}

	public void setCondition(boolean cond){
		free=cond;
	}

	public void setValue(int value){
		this.value=value;
	}
}