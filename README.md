# nnet
A small neural network toy project written in Java.

Run nnet.app.LearningXOR as an example. It learns the XOR function using a backpropagation algorithm and shows the network afterwards.

Contains my take on the back-propagation learning algorithm for adjusting neuron connection weighs in order to minimize the output error (difference between target output and actual output). Figured out that it converges quite slowly on the MNIST data set but still finds good solution (94% acc.) after ~250 epochs. each epoch is on a random 50% selection out of all 60,000 training samples; each sample down-scaled from 28x28 to 14x14 pixels.
