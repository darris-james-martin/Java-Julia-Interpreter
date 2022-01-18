# Java-Julia-Interpreter
 Concepts of Programming Languages Class to make a Julia Interpreter for Java
 
### Details About Application
This was a group project shared between myself and Robert Hannah where we were asked to create a functional interpreter using Java to interpret the minimal form of Julia.

#### The project was divided into three parts:
The first assignment was to create a Lexical Analyzer. The output of this assignment will show the interpreter will properly be able to scan a source code and place the Lexemes into Tokens.

The second assignment was to create a Parser that will import the output of the Lexical Analyzer and provide a list of statements that prove that the Parser is interpreting the source code in the grammar associated with this basic form of Julia.

The third and final assignment was to create the Interpreter. This will capture the input stored in the Parser and execute each to output the solution. Because of all the extra work Robert and I put into the couplings, the interpreter became the easiest part of the project to implement. 

The challenging part of this project was that it changed constantly during production. Every week the professor would revise the specs, and after weeks of completely revising our code, Robert and I spent an evening in the lab creating a foolproof design using couplings that would be vastly modular. 

A coupling is an object that will take the Tokens and place them in containers, so that they are held together. For example, when the source code is “x=7”, the Parser will create an Int IdentifierObject which inherits from IdentifierCoupling. This class keeps the identifier x and the integer 7, so it is ready to execute. From the coupling class we can call getParsedGrammar() and this will output a string of the grammar of what this specific coupling is doing. We can also call executeStatement() and this will execute this coupling, but this will not get called until all couplings have been placed into statements.

 ![image](https://user-images.githubusercontent.com/93277335/148869056-260bf398-2fa4-488b-ac61-d622f1d90248.png)

<i>This is an example of the breakdown of coupling steps, wrapper, operation, and statements.</i>

For each coupling, the professors requirements were to output a document with the token and lexime. and to print to screen the grammar. In each coupling we had:

**getValue**() – returns the value of what the coupling was supposed to do

**getParsedGrammar**() – returns the written grammar of the language

**getStringIdentifier**() – outputs the written token or lexime, the keyword and identifier 

For example this is what adding two integers looks like.
```java
public class CouplingIntAdd extends CouplingObject implements IntValueObject {
    private final IntValueObject number1;
    private final IntValueObject number2;

    public CouplingIntAdd(IntValueObject number1, IntValueObject number2) {
        super(CoupleObjectType.NUMBER_ADD);
        this.number1 = number1;
        this.number2 = number2;
    }

    @Override
    public boolean hasReturnType() {
        return true;
    }

    @Override
    public KeyWord.VariableType getReturnType() {
        return KeyWord.VariableType.NUMBER;
    }


    @Override
    public String getStringIdentifier() {
        return "[" + coupleObjectType + " | " + number1.getStringIdentifier() + " | " + number2.getStringIdentifier() + " ]";
    }

  //See CouplingForStatement.java for explanation.
    @Override
    public String getParsedGrammar() {
        return "<arithmetic_expression> -> <binary_expression> \n"
                + "<binary_expression> -> <arithmetic_op> <arithmetic_expression> <arithmetic_expression> \n"
                + "<arithmetic_op> -> add_operator\n"
                + number1.getParsedGrammar() 
                + number2.getParsedGrammar();
    }


    @Override
    public int getValue() {
        return number1.getValue() + number2.getValue();
    }
}
```

Using this method allowed for variations when the professor would again change up the specifications. For example " I want you to also interpret the while loop function”. The design allowed us to copy over one of these couplings and revise the code to handle a while statement. 

## Input
Julia code
```julia
for i = 3 : 5
if != i 4
print ( i )
else
print ( 333 )
end
end
```

## Output Screen
```
[Token Keyword ID:24 FOR]
[Lexime Identifier  TYPELESS i ]
[Token Keyword ID:23 ASSIGN]
[Lexime Value NUMBER 3]
[Token Keyword ID:0 COLLEN]
[Lexime Value NUMBER 5]
[Token Keyword ID:17 IF]
[Token Keyword ID:7 NOT_EQUAL]
[Lexime Identifier  TYPELESS i ]
[Lexime Value NUMBER 4]
[Token Keyword ID:22 PRINT]
[Token Keyword ID:25 LEFT_PARENTHESIS]
[Lexime Identifier  TYPELESS i ]
[Token Keyword ID:26 RIGHT_PARENTHESIS]
[Token Keyword ID:19 ELSE]
[Token Keyword ID:22 PRINT]
[Token Keyword ID:25 LEFT_PARENTHESIS]
[Lexime Value NUMBER 333]
[Token Keyword ID:26 RIGHT_PARENTHESIS]
[Token Keyword ID:20 END]
[Token Keyword ID:20 END]
[Identifiers]=========================
[FOR 
 Iter Statement: [ITER | [ASSIGN_INT | ID:[INT_IDENTIFIER | [Lexime Identifier  TYPELESS i ] ] Value:[INT_VALUE | [Lexime Value NUMBER 3] ] ] | [INT_VALUE | [Lexime Value NUMBER 5] ] ] 
 Contained Statements
~     [IF 
 Boolean Value: [BOOLEAN_NOT_EQUAl | [INT_IDENTIFIER | [Lexime Identifier  TYPELESS i ] ][INT_VALUE | [Lexime Value NUMBER 4] ] ] 
 IF Statements
~     [PRINT_INT | ID:[INT_PARENTHESES | [INT_IDENTIFIER | [Lexime Identifier  TYPELESS i ] ]] ] ELSE Statements
~     [PRINT_INT | ID:[INT_PARENTHESES | [INT_VALUE | [Lexime Value NUMBER 333] ]] ]
]

]

[Grammer]=========================

<program> -> function id ( ) <block> end

<block> -> <statement> <block> 
<statement> -> <for_statement>
<for_statement> -> for id = <iter> <block> end
<iter> -> <arithmetic_expression> : <arithmetic_expression>

<block> -> <statement> <block> 
<statement> -> <assignment_statement> 
<assignment_statement> -> id assignment_operator <arithmetic_expression> 
<arithmetic_expression> -> literal_integer
<arithmetic_expression> -> literal_integer


<block> -> <statement> <block> 
<statement> -> <if_statement>
<if_statement> -> if <boolean_expression> <block> else <block> end
<boolean_expression> -> <relative_op> <arithmetic_expression> <arithmetic_expression>  
<relative_op> -> ne_operator
<arithmetic_expression> -> id
<arithmetic_expression> -> literal_integer


<block> -> <statement> <block> 
<statement> -> <print_statement> 
<print_statement> -> print (<arithmetic_expression>) 
<arithmetic_expression> -> id


<block> -> <statement> 
<statement> -> <print_statement> 
<print_statement> -> print (<arithmetic_expression>) 
<arithmetic_expression> -> literal_integer

[Interpreter]=========================
PRINT: 3
PRINT: 333
PRINT: 5

Process finished with exit code 0
```
