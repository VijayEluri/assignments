﻿#!/usr/bin/python

import copy

DEBUG_MODE = False

def dprint(*args):
	global DEBUG_MODE
	if DEBUG_MODE == True:
		print 'DEBUG:',
		for arg in args: print arg,
		print

def call_dfs(graph):
	# return immediately if no free space is found to color
	if graph.values().index(None) == -1: return graph

	outcome = graph
	for node in graph.keys():
		dprint('Calling dfs from base for node', node)
		outcome = dfs(node, graph)
		# if outcome is a valid graph, break, otherwise finish the loop
		if outcome != None: break

	# if solution found, returns the solution, else returns the original graph
	return outcome

def dfs(node, graph):
	global colors
	global adjacent

	dprint('DFS called for node:', node)
	dprint('Current graph:', graph)

	# only if the node is free
	if graph[node] == None:
		for color in colors:
			matched = False
			graph[node] = color
			dprint('Coloring node:', node, ', Graph status:', graph)

			# check if this color is good
			for neighbour in adjacent[node]:

				# color is bad
				if graph[node] == graph[neighbour]:
					dprint('Node', node, 'and neighbour', neighbour ,'have same color, will try the next color')

					# set the mached flag
					matched = True
					break
			# try the next color
			if matched == True: continue

			# color is good, prepare the next node to color
			if node < len(graph):
				return dfs(node+1, copy.deepcopy(graph))
			# there is no place to color
			else:
				return graph

	# return the same graph if the node is already colored
	return graph

def get_next_uncolored(graph):
	for node in graph.keys():
		if graph[node] == None:
			return node
	return -1

def bfs(queue):
	while queue != []:
		graph = queue.pop(0)

		dprint('Graph is now', graph)

		matched = False
		for node in graph.keys():
			for neighbour in adjacent[node]:
				if graph[node] != None and graph[node] == graph[neighbour]:
					matched = True
					break

		if matched == False:
			currentnode = get_next_uncolored(graph)
			if currentnode == -1:
				return graph

			for color in colors:
				childgraph = copy.deepcopy(graph)
				childgraph[currentnode] = color
				queue.append(childgraph)

		dprint('Current queue', queue)

if __name__ == '__main__':
	adjacent = {1: (3, 4, 5, 6),
		2: (5, 6),
		3: (1, 4, 5),
		4: (1, 3, 6),
		5: (1, 2, 3, 6),
		6: (1, 2, 4, 5)}

	colors = ['Red', 'Green', 'Blue']
	graph = dict(zip([x for x in range(1, 7)], [None for x in range(1, 7)]))

 	colored_graph = call_dfs(graph)
 	print 'Colored graph with dfs:', colored_graph
	colored_graph = bfs([graph])
	print 'Colored graph with bfs:', colored_graph

