import kotlinx.coroutines.coroutineScope
import parse.AnkiApi

suspend fun main() = coroutineScope<Unit> {
    val api = AnkiApi()
    val deckName = "Wiedza::Techniczne::Data_Mining"
    val comment = ""
    val noteContent = """
For {{c2::classification}} problems, it is natural to measure a classifier’s performance in terms of the {{c1::error rate}}. The {{c2::classifier}} predicts the class of each instance: if it is correct, that is counted as a success; if not, it is an error. The {{c1::error rate}} is just the {{c3::proportion of errors made over a whole set of instances}}, and it measures the overall performance of the classifier.

q: How do we call error rate on the training data?
a: Resubstitution error, because it is calculated by resubstituting the training instances into a classifier that was constructed from them. Although it is not a reliable predictor of the true error rate on new data, it is nevertheless often useful to know.

q: Is resubstitution error	a reliable predictor of the true error rate? (error rate on the training data)
a: It is not a reliable predictor of the true error rate on new data, it is nevertheless often useful to know.

q: How should we predict the performance of a classifier on a new data?
a: We need to assess its error rate on a dataset that played no part in the formation of the classifier. This independent dataset is called the test set.

qa: Test set
aq: This independent dataset that played no part in the formation of the model. Used to predict the true performance on a new data. 

The {{c1::training}} data is used by {{c2::one or more learning methods to come up with classifiers}}. The {{c3::validation}} data is used to {{c4::optimize parameters of those classifiers, or to select a particular one}}. Then the {{c5::test}} data is used to {{c6::calculate the error rate of the final, optimized, method}}. Each of the three sets must be chosen {{c7::independently}}: the {{c3::validation}} set must be different from the {{c1::training}} set to obtain good performance in the optimization or selection stage, and the {{c5::test}} set must be different from both to obtain a reliable estimate of the true error rate.

q: Once we determined true error rate, can the test set be bundled back into the training data?
a: Yes, there is nothing wrong with this: it is just a way of maximizing the amount of data used to generate the classifier that will actually be employed in practice. What is important is that error rates are not quoted based on any of this data.

q: Once the validation data has been used, can it be bundled back into the training data?
a: Yes. To retrain that learning scheme, maximizing the use of data.
    """.trimMargin()
    storeOrUpdateNote(api, deckName, noteContent, comment)
        .processedText
        .let(::print)
}