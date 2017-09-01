# Gluttonous-Snake-With-A-Star-Algorithm
A simple example of A-Star Algorithm in Java
##Rule
You are controlling the back snake with arrow keys, try avoiding rushing into any obstacles but reaching the target point in cyan, which appears randomly.
##Environment
This is a NetBeans project, you can move to any other IDEs easily as you like.
Or you can run the demo from ~/dist/SnakeFighting.jar directly.
##Algorithm
For purpose of studying A Star Algorithm:
f(n) = g(n) + h(n)
This example adds another varible to evaluate the crashing risk:
f(n) = g(n) + h(n) + p(n)
