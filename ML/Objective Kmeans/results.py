import pandas as pd
from matplotlib import pyplot as plt
import random

def generateRandomColors(fileName): #generate random colors for clusters
	df = pd.read_csv(fileName)
	number_of_colors = len(df)
	colors = []
	for _ in range(number_of_colors):
		r = random.randint(0, 255)
		g = random.randint(0, 255)
		b = random.randint(0, 255)
		colors.append('#{:02x}{:02x}{:02x}'.format(r, g, b))
	return colors

def showClusters(fileName1, fileName2): #call this function to show the plot
	colors = generateRandomColors("clusters.txt") #make a list with the colors of every cluster
	df1 = pd.read_csv(fileName1) #examples
	df2 = pd.read_csv(fileName2) #centers
	plt.scatter(df1['Position X'], df1['Position Y'], c=df1['Cluster'].apply(lambda x: colors[x % len(colors)])) #put all examples in plot
	plt.scatter(df2['Position X'], df2['Position Y'], c='black', marker='+') #put all centers in plot
	title = "Clusters = "
	title += str(len(colors))
	plt.title(title)
	plt.show()

def main(): #this is the code that will run
	showClusters("kmeans.txt","clusters.txt")

main()