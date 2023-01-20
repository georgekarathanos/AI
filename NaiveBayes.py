# Importing the libraries
import pandas as pd
import csv
from sklearn.naive_bayes import GaussianNB

# Create x_train, y_train, x_test
hTrain= pd.read_csv("train.csv")
X = hTrain.drop(columns=["id"])
xTrain = X.drop(columns=["type"])
yTrain= hTrain["type"].values
for i in range(hTrain.shape[0]):
    if(xTrain["color"][i]=="clear"):  #clear=1/6
        xTrain["color"][i]=1/6
    elif(xTrain["color"][i]=="white"):  #white=2/6
        xTrain["color"][i]=2/6
    elif(xTrain["color"][i]=="green"):  #green=3/6
        xTrain["color"][i]=3/6
    elif(xTrain["color"][i]=="blue"):  #blue=4/6
        xTrain["color"][i]=4/6
    elif(xTrain["color"][i]=="black"):  #black=5/6
        xTrain["color"][i]=5/6
    elif(xTrain["color"][i]=="blood"):  #blood=6/6
        xTrain["color"][i]=1


hTest= pd.read_csv("test.csv")
xTest = hTest.drop(columns=["id"])
for i in range(hTest.shape[0]):
    if(xTest["color"][i]=="clear"):  #clear=1/6
        xTest["color"][i]=1/6
    elif(xTest["color"][i]=="white"):  #clear=2/6
        xTest["color"][i]=2/6
    elif(xTest["color"][i]=="green"):  #clear=3/6
        xTest["color"][i]=3/6
    elif(xTest["color"][i]=="blue"):  #clear=4/6
        xTest["color"][i]=4/6
    elif(xTest["color"][i]=="black"):  #clear=5/6
        xTest["color"][i]=5/6
    elif(xTest["color"][i]=="blood"):  #clear=6/6
        xTest["color"][i]=1

def createOutput(yTest,name):
    f= open(name,"w")
    writer = csv.writer(f)
    writer.writerow(["id","type"])
    finalList=[]
    length=len(yTest)
    for i in range(length):
        currentList=[]
        currentList.append(hTest["id"][i])
        currentList.append(yTest[i])
        finalList.append(currentList)
    writer.writerows(finalList)
    f.close()

model= GaussianNB()
model.fit(xTrain,yTrain)
yTest= model.predict(xTest)
createOutput(yTest,"NaiveBayes_output.csv") ##create .csv file to upload to Kaggle
print("Output file has been created.")