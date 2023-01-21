import pandas as pd
from matplotlib import pyplot as plt

def copy_file_without_last_line(src_file, dest_file):
    # Open the source file in read mode
    with open(src_file, "r") as f:
        # Read in all the lines of the file
        lines = f.readlines()

    # Remove the last line
    lines = lines[:-1]

    # Open the destination file in write mode
    with open(dest_file, "w") as f:
        # Write the modified contents to the file
        f.writelines(lines)

def show(fileName1, fileName2): #call this function to show the plot
	colors = ['black','purple','green', 'red']
	df1 = pd.read_csv(fileName1) #examples
	plt.figure()
	plt.scatter(df1['Position X'], df1['Position Y'], c=df1['Class'].apply(lambda x: colors[x % len(colors)]), marker='+') #put all examples in plot
	plt.title("Classified test set.")

	df2 = pd.read_csv(fileName2) #examples
	plt.figure()
	plt.scatter(df2['Position X'], df2['Position Y'], c=df2['Class'].apply(lambda x: colors[x % len(colors)]), marker='+') #put all examples in plot
	plt.title("Real test set.")
	plt.show()

def main(): #this is the code that will run
	copy_file_without_last_line("testset.txt", "modified.txt")
	show("classified.txt", "modified.txt")

main()