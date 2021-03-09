Zmienna objaśniająca – (inaczej zmienna egzogeniczna) zmienna w modelu statystycznym (czyli także np. w modelu ekonometrycznym), na podstawie której wylicza się zmienną objaśnianą (endogeniczną). Zwykle występuje ich wiele..

Zmienna objaśniana - (inaczej zmienna endogeniczna, zmienna odpowiedzi, zmienna prognozowana, zmienna wewnętrzna) – zmienna, której wartości są estymowane przez model statystyczny (w szczególności model ekonometryczny).

Model statystyczny – hipoteza lub układ hipotez, sformułowanych w sposób matematyczny (odpowiednio w postaci równania lub układu równań), który przedstawia zasadnicze powiązania występujące pomiędzy rozpatrywanymi zjawiskami rzeczywistymi.

Regresja – metoda statystyczna pozwalająca na opisanie współzmienności kilku zmiennych przez dopasowanie do nich funkcji. Umożliwia przewidywanie nieznanych wartości jednych wielkości na podstawie znanych wartości innych. Opisana funkcją \(Y = f(X, \beta) + \epsilon\)

Najpopularniejsze modele parametryczne:

Regresja liniowa - Model ten ma ogólną postać kombinacji liniowej wyrazów:
{{c2::\(Y=\beta _{0}+x_{1}\beta _{1}+x_{2}\beta _{2}+\dots +x_{n}\beta _{n}+\epsilon\)}}

Regresja liniowa z przekształceniami - model ten jest modyfikacją regresji liniowej, ale dopuszcza dowolne przekształcenia zmiennych np:
{{c2::\(Y=\beta _{0}+\beta _{1}Z+\beta _{2}Z^{2}+\beta _{3}{\sqrt Z}+\beta _{4}\log Z+\epsilon\)}}

Regresja liniowa z interakcjami - Szczególnym przypadek regresji liniowej, dopuszczający interakcje między zmiennymi wyrażonymi jako ich produkt, np.
{{c2::\(Y=\beta _{0}+\beta _{1}x_{1}+\beta _{2}x_{2}+\beta _{3}x_{1}x_{2}+\epsilon\)}}

Regresja nieliniowa - Regresja, w której postać modelu nie sprowadza się do liniowej kombinacji wyrazów. Na przykład:
\(Y=\beta _{0}+{\frac {\beta _{1}Z}{\beta _{2}+Z}}+\epsilon\)

Do problemu regresji liniowej w Pythonie potrzebujemy kilku bibliotek.
Przede wszystkim NumPy do wykonywania wydajnych operacji na jedno i wielowymiarowych macierzach.
Następnie scikit-learn zawierający zbiór narzędzi do uczenia maszynowego, w tym do regresji liniowej (preprocessing data, reducing dimensionality, implementing regression, classification, clustering, and more).
Jak potrzebujemy więcej narzędzi, możemy użyć statsmodels zawierający potężny zbiór narzędzi do modeli statystycznych.
Aby automatycznie odkryć optymalny model możemy użyć Auto-Sklearn