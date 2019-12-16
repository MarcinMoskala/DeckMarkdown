# AnkiMarkdown

This is my pet project where I experiment with my own markdown for Anki.

## The idea

Anki browser is terrible. Finding data is hard and collaboration is practically impossible. 
The idea is to introduce a text format for Anki cards. 
This format needs to be minimalistic to well serve as a note.
Thanks to that one could have the following workflow:

1) Making notes when reading source
2) Adding tags and automatically generate cards from this note (easy cards creation)
3) Cards should direct to this note in comment via a link (to give context)

Next, when we change our notes we want our cards updated as well. To make it possible cards needs to be identified. When we remove card it should be updated as well. 

All those capabilities are already implemented.

Ideally, this format should also allow public decks shared and updated socially. For that, cards need a different identifier than their deck id.

## Format

Here is an example of the current format:

```
This is text {1}

qa: My question
aq: My answer

q: Question 2
a: Answer 2

And this {text} number is {2}
```

The cards generated from that are the following:

```
Card.Cloze(text = "This is text {{c1::1}}"),
Card.BasicAndReverse(front = "My question", back = "My answer"),
Card.Basic(front = "Question 2", back = "Answer 2"),
Card.Cloze(text = "And this {{c1::text}} number is {{c2::2}}")
```

Clozes can be both marked using single brace (like `{aaa}`), as well as using double braces with name (like `{{c1::aaa}}`). 
Though after processing the letter is generated as it should be preferred. 

Also after those cards are added, ids are attached. They are necessery later for updating cards instead of duplicating them:

```
@1576516338844
This is text {{c1::1}}

@1576516338892
qa: My question
aq: My answer

@1576516338945
q: Question 2
a: Answer 2

@1576516338994
And this {{c1::text}} number is {{c2::2}}
```

Update your note with the generated one. 

To see more examples, chek out unit tests.

### Note types

Currently there are 4 card types supported:
* Plain text
* Cloze deletion
* Basic
* Basic and reversed

Types that should be introduced:
* List
* Set
* General - univerasal type that can cover any card type

### Anki Connect

Program uses (Anki Connect)[https://github.com/FooSoft/anki-connect]. Install it in your Anki, and for this program to work correctly Anki needs to be open. 

### Usage

It is a Kotlin Gradle project. Import it in IntelliJ and it should be ready to go (supposing that you have Anki Connect installed and Anki started).

Example program presenting existing notes from a deck in `ReadAnki`. Program adding or updating cards based on text in `StoreOrUpdateAnki`. Remamber to change deck names.

### Next steps

* New card types
* Adding missing card type
* Deck name and header should be encoded in the text header
* Universal id (to support multiple note maintainers)
* Support multiline code font
