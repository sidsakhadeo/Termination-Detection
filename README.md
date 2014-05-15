Termination-Detection
=====================

## Description

Implementing tree-based Dijkstra-Scholten termination detection algorithm. Assuming there are n (given) nodes in the system. The nodes implement Lamport’s logical clock with increment value 1. One of the nodes initiates the termination detection algorithm. When a node A wants to send a message to another node B and A is not directly connected to B then A first establishes a TCP connection with B. The TCP connection is alive until A receives all ACKs (as per the termination detection algorithm) from B.

You are given a file that contains a series of events that should be simulated on the nodes. The program is ex-pected to read these events from the file and executes them on corresponding nodes. First line of the file gives the number of nodes in the system. Each of the subsequent lines of the file represents an event that is denoted by a tuple <nodeid, clockval, type, [param]>; where: nodeid is the node where the event should occur, clockval is the logical clock value at nodeid when the event occurs, type is type of the event and param is an optional parameter. The types of the event could be one of the following:

Event |   Meaning
------|----------------------------------------
INIT  |		Initiate the termination protocol.
SEND  |		Send a message to node given in param.
IDLE  | 	The node becomes idle and it may trigger some events according to the algorithm.
TICK A| 	“Tick” of the logical clock after delay given in param (ms).

In addition to above described events, “RECV” a message by a node is also considered as an “event”.
Once the initiator detects the termination of the computation, it displays proper messages and quits. Your program should also display proper log messages either on screen or to a file.