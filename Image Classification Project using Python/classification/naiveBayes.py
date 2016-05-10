# naiveBayes.py
# -------------
# Licensing Information: Please do not distribute or publish solutions to this
# project. You are free to use and extend these projects for educational
# purposes. The Pacman AI projects were developed at UC Berkeley, primarily by
# John DeNero (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# For more info, see http://inst.eecs.berkeley.edu/~cs188/sp09/pacman.html

import util
import classificationMethod
import math

class NaiveBayesClassifier(classificationMethod.ClassificationMethod):
  """
  See the project description for the specifications of the Naive Bayes classifier.
  
  Note that the variable 'datum' in this code refers to a counter of features
  (not to a raw samples.Datum).
  """
  def __init__(self, legalLabels):
    self.legalLabels = legalLabels
    self.type = "naivebayes"
    self.k = 1 # this is the smoothing parameter, ** use it in your train method **
    self.automaticTuning = False # Look at this flag to decide whether to choose k automatically ** use this in your train method **
    
  def setSmoothing(self, k):
    """
    This is used by the main method to change the smoothing parameter before training.
    Do not modify this method.
    """
    self.k = k

  def train(self, trainingData, trainingLabels, validationData, validationLabels):
    """
    Outside shell to call your method. Do not modify this method.
    """  
      
    # might be useful in your code later...
    # this is a list of all features in the training set.
    self.features = list(set([ f for datum in trainingData for f in datum.keys() ]));
    
    if (self.automaticTuning):
        kgrid = [0, 0.001, 0.01, 0.05, 0.1, 0.5, 1, 5, 10, 20, 50]
    else:
        kgrid = [self.k]
        
    self.trainAndTune(trainingData, trainingLabels, validationData, validationLabels, kgrid)
      
  def trainAndTune(self, trainingData, trainingLabels, validationData, validationLabels, kgrid):
    """
    Trains the classifier by collecting counts over the training data, and
    stores the Laplace smoothed estimates so that they can be used to classify.
    Evaluate each value of k in kgrid to choose the smoothing parameter 
    that gives the best accuracy on the held-out validationData.
    
    trainingData and validationData are lists of feature Counters.  The corresponding
    label lists contain the correct label for each datum.
    
    To get the list of all possible features or labels, use self.features and 
    self.legalLabels.
    """
    global features
    features = []
    maxlen = len(trainingLabels)

    #initialize feature list for each label
    for labels in self.legalLabels:
        features.append(util.Counter())

    #get the count of features for each label
    for counter in range(0, maxlen):
        keys1=[i for i in trainingData[counter].keys() if trainingData[counter][i] == 1]
        keys0=[i for i in trainingData[counter].keys() if trainingData[counter][i] == 0]
        features[trainingLabels[counter]].incrementAll(keys1,1)
        features[trainingLabels[counter]].incrementAll(keys0,0)

    #get the count of elements of each label
    global classes 
    classes = util.Counter()
    classes.incrementAll(list(trainingLabels),1)

    #storing original values
    tempclasses = classes.copy()
    tempfeatures = []
    for labels in self.legalLabels:
        tempfeatures.append(features[labels].copy())

    global mode, bin
    mode = 1

    #smoothening
    max_count = 0
    for k in kgrid:
        for bin in range(0,2):
            classes = tempclasses.copy()
            features = []
            for labels in self.legalLabels:
                features.append(tempfeatures[labels].copy())

            if bin == 0:
                for labels in self.legalLabels:
                    features[labels].divideAll(-1)
                    features[labels].incrementAll(features[labels].keys(),classes[labels])

            classes.incrementAll(classes.keys(),2*k)

            #for each class get the probability of a feature, given class
            for labels in self.legalLabels:
                features[labels].incrementAll(features[labels].keys(),k)
                features[labels].divideAll(classes[labels])

            #To get the probability of each label; will be used to classify
            classes.divideAll(maxlen)

            #run classifier on validation set
            guesses = self.classify(validationData)

            #check correctness
            correct = [guesses[i] == validationLabels[i] for i in range(len(validationLabels))].count(True)

            if correct > max_count:
                max_count = correct
                self.k = k
                mode = bin

    #Assign best value of k
    print ("Selecting k as : ",self.k)
    classes = tempclasses.copy()
    features = []
    for labels in self.legalLabels:
        features.append(tempfeatures[labels].copy())

    classes.incrementAll(classes.keys(),2*self.k)
    for labels in self.legalLabels: 
        features[labels].incrementAll(features[labels].keys(),self.k)
        features[labels].divideAll(classes[labels])

    classes.divideAll(maxlen)
        
  def classify(self, testData):
    """
    Classify the data based on the posterior distribution over labels.
    
    You shouldn't modify this method.
    """
    guesses = []
    self.posteriors = [] # Log posteriors are stored for later data analysis (autograder).
    for datum in testData:
      posterior = self.calculateLogJointProbabilities(datum)
      guesses.append(posterior.argMax())
      self.posteriors.append(posterior)
    return guesses
      
  def calculateLogJointProbabilities(self, datum):
    """
    Returns the log-joint distribution over legal labels and the datum.
    Each log-probability should be stored in the log-joint counter, e.g.    
    logJoint[3] = <Estimate of log( P(Label = 3, datum) )>
    
    To get the list of all possible features or labels, use self.features and 
    self.legalLabels.
    """
    logJoint = util.Counter()

    keys1=[i for i in datum.keys() if datum[i] == 1]
    keys0=[i for i in datum.keys() if datum[i] == 0]

    if bin == 1:
        for labels in self.legalLabels:
            for indexes in keys1:
                cp = (features[labels][indexes])
                logJoint[labels] += (cp and math.log(cp) or 0)
            for indexes in keys0:
                cp = (1 - features[labels][indexes])
                logJoint[labels] += (cp and math.log(cp) or 0)
            pr = (classes[labels])
            logJoint[labels] += (pr and math.log(pr) or 0)
    else:
        for labels in self.legalLabels:
            for indexes in keys0:
                cp = (features[labels][indexes])
                logJoint[labels] += (cp and math.log(cp) or 0)
            for indexes in keys1:
                cp = (1 - features[labels][indexes])
                logJoint[labels] += (cp and math.log(cp) or 0)       
            pr = (classes[labels])
            logJoint[labels] += (pr and math.log(pr) or 0)

    return logJoint
  
  def findHighOddsFeatures(self, label1, label2):
    """
    Returns the 100 best features for the odds ratio:
            P(feature=1 | label1)/P(feature=1 | label2) 
    
    Note: you may find 'self.features' a useful way to loop through all possible features
    """
    featuresOdds = []
       
    "*** YOUR CODE HERE ***"
    util.raiseNotDefined()

    return featuresOdds
    

    
      
