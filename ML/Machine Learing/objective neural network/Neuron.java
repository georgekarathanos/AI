import java.lang.Math;

class Neuron{
	private String function;
	private double input;
	private double output;
	private double d_error;

	public Neuron(String function){
		this.function = function;
		input = -1;
		output = -1;
		d_error = -1;
	}

	public void setInput(double input){
		this.input = input;
	}

	public void setD_error(double term){
		d_error = getDerivative() * term;
	}

	public void setOutput(){
		if(function.equals("sigmoid")){
			output = 1 / (1 + Math.exp(-input));
		}else if(function.equals("tanh")){
			output = Math.tanh(input);
		}else if(function.equals("relu")){
			if(input<0){
				output = 0;
			}else{
				output = input;
			}
		}
	}

	public double getInput(){
		return input;
	}

	public double getOutput(){
		return output;
	}

	public double getD_error(){
		return d_error;
	}

	public double getDerivative(){
		double derivative = -1; //this should change
		if(function.equals("sigmoid")){
			double sigmoid = 1 / (1 + Math.exp(-input));
    		derivative = sigmoid * (1 - sigmoid);
		}else if(function.equals("tanh")){
			double tanh = Math.tanh(input);
   			derivative = 1 - tanh * tanh;
		}else if(function.equals("relu")){
			if(input<0){
				derivative = 0;
			}else{
				derivative = 1;
			}
		}return derivative;
	}
}