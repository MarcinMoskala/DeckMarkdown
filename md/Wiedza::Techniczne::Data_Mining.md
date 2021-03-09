For classification problems, it is natural to measure a classifier’s performance in terms of the error rate. The classifier predicts the class of each instance: if it is correct, that is counted as a success; if not, it is an error. The error rate is just the proportion of errors made over a whole set of instances, and it measures the overall performance of the classifier.

Q: How do we call error rate on the training data?
A: Resubstitution error, because it is calculated by resubstituting the training instances into a classifier that was constructed from them. Although it is not a reliable predictor of the true error rate on new data, it is nevertheless often useful to know.

Q: Is resubstitution error	a reliable predictor of the true error rate? (error rate on the training data)
A: It is not a reliable predictor of the true error rate on new data, it is nevertheless often useful to know.

Q: How should we predict the performance of a classifier on a new data?
A: We need to assess its error rate on a dataset that played no part in the formation of the classifier. This independent dataset is called the test set.

Q/A: Test set
A/Q: This independent dataset that played no part in the formation of the model. Used to predict the true performance on a new data.

The training data is used by one or more learning methods to come up with classifiers. The validation data is used to optimize parameters of those classifiers, or to select a particular one. Then the test data is used to calculate the error rate of the final, optimized, method. Each of the three sets must be chosen independently: the validation set must be different from the training set to obtain good performance in the optimization or selection stage, and the test set must be different from both to obtain a reliable estimate of the true error rate.

Q: Once we determined true error rate, can the test set be bundled back into the training data?
A: Yes, there is nothing wrong with this: it is just a way of maximizing the amount of data used to generate the classifier that will actually be employed in practice. What is important is that error rates are not quoted based on any of this data.

Q: Once the validation data has been used, can it be bundled back into the training data?
A: Yes. To retrain that learning scheme, maximizing the use of data.

Q/A: Bernoulli process
A/Q: In statistics, a succession of independent events that either succeed or fail. The classic example is coin tossing. Each toss is an independent event.

Q/A: Test set stratification
A/Q: When we balance samples in the test set to have each category properly represented.

Q: Stratified holdout vs repeated holdout method of error rate estimation
A: In stratified holdout we test once, with balanced test set. In repeated holdout we test multiple times on multiple test sets.

Q/A: Cross-validation
A/Q: When we calculate the model many times every time on a different training set, and then test on a different test set. As a result we have unbiased success rate estimation.

Q/A: Leave-one-out validation
A/Q: Simply n-fold cross-validation, where n is the number of instances in the dataset. Each instance in turn is left out, and the learning method is trained on all the remaining instances

Q: The problem with leave-one-out validation
A: Apart from the computational expense, it always teats on unstratified test set. A dramatic, although highly artificial, illustration of the problems this might cause is to imagine a classes. The best that an inducer can do with random data is to predict the majority class, giving a true error rate of 50%. But in each fold of leave-oneout, the opposite class to the test instance is in the majority—and therefore the predictions will always be incorrect, leading to an estimated error rate of 100%!completely random dataset that contains the same number of each of two

Q/A: The bootstrap validation
A/Q: For this, a dataset of n instances is sampled n times, with replacement, to give another dataset of n instances. Because some elements in this second dataset will (almost certainly) be repeated, there must be some instances in the original dataset that have not been picked: we will use these as test instances

Q/A: Mean squared error
A/Q: Mean of the difference between prediction and actual value, squared

Q/A: Square operation x
A/Q: x * x (name)

Q: 25 square
A: 625

Q/A: Square root x
A/Q: sqrt(x), or y where y * y = x

Q: Square root of 25
A: 5

Q/A: Quadratic loss function
A/Q: Sum of the differences between label probability and it being chosen or not (0 or 1). So if 3 classes, y_true = [b, a] then a = [[0, 1, 0], [1, 0, 0]]. If predicted probabilities are [[0.25, 0.5, 0.25], [0.75, 0.25, 0]] then the result is 0.25^2 + 0.5^2 + 0.25^2 + 0.25^2 + 0.25^2 = 0.375 + 0.125 = 0.5

Q/A: Informational loss function
A/Q: Sum of -log2 p of the chosen class. We want to minimize it - the higher probabiliy the closer to 0, The lower probability, the bigger the loss is.

Q/A: True positiove
A/Q: Classified correctly as yes

Q/A: True negative
A/Q: Classified correctly as no

Q/A: False positiove
A/Q: Classified incorrectly as yes

Q/A: False negative
A/Q: Classified incorrectly as no

Q/A: Confusion matrix
A/Q: Matrix in which element shows the number of test examples for which the actual class is the row and the predicted class is the column.

Q: What does Kappa statistic measure?
A: The agreement between two confusion matrixes

Q: ROC stands for
A: Receiver Operating Characteristic.