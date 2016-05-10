# mira.py
# -------
# Licensing Information: Please do not distribute or publish solutions to this
# project. You are free to use and extend these projects for educational
# purposes. The Pacman AI projects were developed at UC Berkeley, primarily by
# John DeNero (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# For more info, see http://inst.eecs.berkeley.edu/~cs188/sp09/pacman.html

# Mira implementation
import util
PRINT = True

class MiraClassifier:
  """
  Mira classifier.
  
  Note that the variable 'datum' in this code refers to a counter of features
  (not to a raw samples.Datum).
  """
  def __init__( self, legalLabels, max_iterations):
    self.legalLabels = legalLabels
    self.type = "mira"
    self.automaticTuning = False 
    self.C = 0.001
    self.legalLabels = legalLabels
    self.max_iterations = max_iterations
    self.initializeWeightsToZero()

  def initializeWeightsToZero(self):
    "Resets the weights of each label to zero vectors" 
    self.weights = {}
    for label in self.legalLabels:
      self.weights[label] = util.Counter() # this is the data-structure you should use
  
  def train(self, trainingData, trainingLabels, validationData, validationLabels):
    "Outside shell to call your method. Do not modify this method."  
      
    self.features = trainingData[0].keys() # this could be useful for your code later...
    
    if (self.automaticTuning):
        Cgrid = [0.001, 0.002, 0.004, 0.008]
    else:
        Cgrid = [self.C]
        
    return self.trainAndTune(trainingData, trainingLabels, validationData, validationLabels, Cgrid)

  def trainAndTune(self, trainingData, trainingLabels, validationData, validationLabels, Cgrid):
    """
    This method sets self.weights using MIRA.  Train the classifier for each value of C in Cgrid, 
    then store the weights that give the best accuracy on the validationData.
    
    Use the provided self.weights[label] data structure so that 
    the classify method works correctly. Also, recall that a
    datum is a counter from features to values for those features
    representing a vector of values.
    """
    scores = util.Counter()
    self.features = trainingData[0].keys()
    max_count = 0

    for c in Cgrid:
      print "C Iteration ", c, "..."
      self.initializeWeightsToZero()
      for label in self.legalLabels:
        self.weights[label].incrementAll(self.features,1)

      for iteration in range(self.max_iterations):
        for i in range(len(trainingData)):
          features = util.Counter()
          keys1=[j for j in trainingData[i].keys() if trainingData[i][j] == 1]
          keys0=[j for j in trainingData[i].keys() if trainingData[i][j] == 0]
          features.incrementAll(keys1,1)
          features.incrementAll(keys0,0)

          for label in self.legalLabels:
              temp = features.copy()
              scores[label] = temp.__mul__(self.weights[label])
          y = scores.argMax()

          #If not predicted correctly, update weights
          if trainingLabels[i] != y:
            tau = (self.weights[y].__mul__(features) - self.weights[trainingLabels[i]].__mul__(features) + 1.0)/(2.0*features.totalCount())
            tau = min(tau,c)
            temp = features.copy()
            for keys, value in temp.items():
              temp[keys] = value*tau

            self.weights[y] = self.weights[y].__sub__(temp)
            self.weights[trainingLabels[i]] = self.weights[trainingLabels[i]].__add__(temp)

      #testing on validation data
      guesses = self.classify(validationData)
      correct = [guesses[i] == validationLabels[i] for i in range(len(validationLabels))].count(True)
      print ("correct : ",correct)
      #updating c
      if correct > max_count:
        max_count = correct
        bestc = c
    
    #Using best C
    print ("bestc is : ",bestc)
    self.initializeWeightsToZero()
    for label in self.legalLabels:
        self.weights[label].incrementAll(self.features,1)

    for iteration in range(self.max_iterations):
        print "Starting iteration ", iteration, "..."
        for i in range(len(trainingData)):
          features = util.Counter()
          keys1=[j for j in trainingData[i].keys() if trainingData[i][j] == 1]
          keys0=[j for j in trainingData[i].keys() if trainingData[i][j] == 0]
          features.incrementAll(keys1,1)
          features.incrementAll(keys0,0)

          for label in self.legalLabels:
              temp = features.copy()
              scores[label] = temp.__mul__(self.weights[label])
          y = scores.argMax()

          if trainingLabels[i] != y:
            tau = (self.weights[y].__mul__(features) - self.weights[trainingLabels[i]].__mul__(features) + 1.0)/(2.0*features.__mul__(features))
            tau = min(tau,bestc)
            temp = features.copy()
            for keys, value in temp.items():
              temp[keys] = value*tau

            self.weights[y] = self.weights[y].__sub__(temp)
            self.weights[trainingLabels[i]] = self.weights[trainingLabels[i]].__add__(temp)

  def classify(self, data ):
    """
    Classifies each datum as the label that most closely matches the prototype vector
    for that label.  See the project description for details.
    
    Recall that a datum is a util.counter... 
    """
    guesses = []
    for datum in data:
      vectors = util.Counter()
      for l in self.legalLabels:
        vectors[l] = self.weights[l] * datum
      guesses.append(vectors.argMax())
    return guesses

  
  def findHighOddsFeatures(self, label1, label2):
    """
    Returns a list of the 100 features with the greatest difference in feature values
                     w_label1 - w_label2

    """
    featuresOdds = []

    "*** YOUR CODE HERE ***"

    return featuresOdds

