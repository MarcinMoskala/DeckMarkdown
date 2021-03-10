If you want to start working with Spark SQL with PySpark, you’ll need to start a *SparkSession* first: you can use this to *create DataFrames, register DataFrames as tables, execute SQL over the tables and read parquet files*.
<pre><code>
from pyspark.sql import *SparkSession*
spark = *SparkSession* \
    .*builder* \
    .*appName*("Python Spark SQL basic example") \
    .*config*("spark.some.config.option", "some-value") \
    .*getOrCreate()*
</code></pre>

*Question: df.show()*

Answer: Display the content of df

*Question: df.head(n) and df.take(n)*

Answer: Two ways to return first n rows from df as a list

*Question: df.first()*

Answer: Return first row from df

*Question: df.dtypes*

Answer: Return df column names and data types
[('PassengerId', 'int'),
 ('Survived', 'int'),
 ('Pclass', 'int'),
 ('Name', 'string'),
 ('Sex', 'string'),
 ...

*Question: df.schema*

Answer: Return the schema of df
StructType(List(StructField(PassengerId,IntegerType,true),StructField(Survived,IntegerType,true),...

*Question: df.describe().show()*

Answer: Compute summary statistics of df
<img src="pyspark_describe.png" style="zoom:50%;" />

*Question: df.printSchema()*

Answer: Print the schema of df
root
 |-- PassengerId: integer (nullable = true)
 |-- Survived: integer (nullable = true)
 |-- Name: string (nullable = true)
 ...

*Question: df.columns*

Answer: Return the columns of df
['PassengerId',
 'Survived',
 'Pclass',
 ...

*Question: What to ask for first, to understand data better?*

Answer: df.printSchema(), df.describe().show(), df.count(), df.dropDuplicates().count()

*Question: df.count()*

Answer: Count the number of rows in df

*Question: df.dropDuplicates() or df.distinct()*

Answer: Drop duplicate from the df

*Question: df.distinct().count() or df.dropDuplicates().count()*

Answer: Count the number of distinct rows in df

*Question: dropDuplicates vs distinct*

Answer: dropDuplicates() was introduced in 1.4 as a replacement for distinct(), as you can use it's overloaded methods to get unique rows based on subset of columns.

*Question: Show columns firstName and lastName from df.*

Answer: df.select("firstName","lastName").show()

*Question: Show columns firstName and age + 1 from df.*

Answer: df.select(df["firstName"],df["age"]+ 1).show()
+--------------------+---------+
|                Name|(Age + 1)|
+--------------------+---------+
|Braund, Mr. Owen ...|     23.0|
|Cumings, Mrs. Joh...|     39.0|
|Heikkinen, Miss. ...|     27.0|
...

*Question: Show column firstName and 1 when age > 30, 0 otherwise*

Answer: from pyspark.sql.functions import when
df.select("firstName", when(df.age > 30, 1).otherwise(0)).show()

*Question: Show df rows with firstName either "Jane" or "Boris"*

Answer: df[df.firstName.isin("Jane","Boris")].show()

*Question: Return all the elements of the dataset as an array at the driver program. This is usually useful after a filter or other operation that returns a sufficiently small subset of the data.*

Answer: ds.collect()

*Question: Show df minimal age*

Answer: from pyspark.sql import functions as F
df.agg(F.min(df.age)).show()

*Question: df.filter(df["age"]>24).show() or df.where(df["age"]>24).show()*

Answer: Keep only rows with age > 24. (2 ways)

*Question: filter vs where*

Answer: Identical

*Question: Adding columns in PySpark*

Answer: df = df.withColumn('city',df.address.city) \
   .withColumn('postalCode',df.address.postalCode) \
   .withColumn('state',df.address.state) \
   .withColumn('streetAddress',df.address.streetAddress) \

*Question: How to rename a column in PySpark?*

Answer: df = df.withColumnRenamed('telePhoneNumber', 'phoneNumber')

*Question: Remove a column in pyspark*

Answer: df = df.drop("address", "phoneNumber")

*Question: Count rows with each age in df*

Answer: df.groupBy("age").count().show()

*Question: What does df.groupBy("something") return?*

Answer: GroupedData

*Question: Take rows from df with age between 22 and 24*

Answer: df.select(df.age.between(22, 24)).show()

*Question: Take column with only first 4 letters from firstName*

Answer: df.select(df.firstName.substr(0, 3).alias("name")).collect()

*Question: Take lastName ending with th*

Answer: df.select(df.lastName.endswith("th"))

*Question: Average survival rate for each age*

Answer: df.groupBy("Age").mean("Survived").show()

*Question: Average survival rate for each age decade*

Answer: from pyspark.sql.functions import col
def age_group(age):
   return 10 * floor(age / 10)
df.groupBy(age_group(col('Age'))).mean("Survived").show()

*Question: Two ways to sort descending by age*

Answer: df.sort(df.age.desc()).collect()
df.sort("age", ascending=False).collect()

*Question: Sort by age descending, and by city ascending*

Answer: df.orderBy(["age","city"],ascending=[0,1]).collect()

*Question: Fill lack of values in df with ""*

Answer: df.na.fill("") or df.fillna("")

*Question: Replace value in df*

Answer: df.replace(0, 100).show()

*Question: Convert df into an RDD*

Answer: rdd1 = df.rdd

*Question: Return the contents of df as Pandas DataFrame*

Answer: df.toPandas()

*Question: Read df from json*

Answer: df = spark.read.json("customer.json")

*Question: Read df from csv, where the first line is header*

Answer: df = spark.read.csv("customer.json",header = 'True',inferSchema='True')

*Question: Save df as json*

Answer: df.write.save("data.json",format="json")

*Question: Save df as csv*

Answer: df.write.save("data.csv",format="csv")

*Question: Stopping Spark Session*

Answer: spark.stop()