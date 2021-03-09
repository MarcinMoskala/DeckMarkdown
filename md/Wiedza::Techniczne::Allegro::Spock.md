## Specification

A specification is represented as a Groovy class that extends from `spock.lang.Specification`.

Order of elements in specification
class MyFirstSpecification extends Specification {
  // fields
  // fixture methods
  // feature methods
  // helper methods
}

Class Specification contains a number of useful methods for writing specifications. Furthermore it instructs JUnit to run specification with Sputnik, Spock’s JUnit runner. Thanks to Sputnik, Spock specifications can be run by most modern Java IDEs and build tools.

## Fields

```groovy
def obj = new ClassUnderSpecification()
def coll = new Collaborator()
```

Q: What is the difference in Speck between initialising fields right at the point of declaration and in setup function?
A: Those are equivalents because Speck will initialise properties separately for every test case.

Sometimes you need to share an object between feature methods. For example, the object might be very expensive to create, or you might want your feature methods to interact with each other. To achieve this, declare a `@Shared` field. Again it’s best to initialize the field right at the point of declaration. (Semantically, this is equivalent to initializing the field at the very beginning of the `setupSpec()` method.)

```groovy
@Shared res = new VeryExpensiveResource()
```

Q: What is @Shared doing in Groovy?
A: Field will be static and one for all tests.

## Fixture methods

There are following fixture methods in Spock Specification:
def setup() {}          // run before every feature method
def cleanup() {}        // run after every feature method
def setupSpec() {}      // run before the first feature method
def cleanupSpec() {}    // run after the last feature method

## Feature Methods

```groovy
def "pushing an element on the stack"() 
  // blocks go here

```

Conceptually, a feature method consists of four phases:

* Set up the feature’s fixture
* Provide a *stimulus* to the system under specification
* Describe the *response* expected from the system
* Clean up the feature’s fixture

## Blocks

In Spock feature methods there are the following blocks: setup, when, then, expect, cleanup, and where. Any statements between the beginning of the method and the first explicit block belong to an implicit setup block.

Q: Can he have feature method without any explicit block?
A: No, the presence of an explicit block is what makes a method a feature method. Blocks divide a method into distinct sections, and cannot be nested.



![Blocks2Phases](/Users/marcin.moskala/Projects/AnkiMarkdown/notes/media/Blocks2Phases.png)