# -*- coding: utf-8 -*-

import sys

class Topic():
	def __init__(self, id, title, image):
		self.id = id
		self.title = title
		self.image = image

class Item():
	def __init__(self, title, text, image, topicId):
		self.title = title
		self.text = text
		self.image = image
		self.topicId = topicId

def addTopic(topics, topic):
	for t in topics:
		if t.title == topic.title:
			return

	topics.append(topic)

def getTopicId(topics, topic):
	for t in topics:
		if t.title == topic:
			return t.id

	return None

def clear(text):
	while "\n\n" in text:
		text = text.replace("\n\n", " ")

	while "  " in text:
		text = text.replace("  ", " ")

	while "\t" in text:
		text = text.replace("\t", "")

	while text[0] == '\n':
		text = text[1: len(text)]

	while text[len(text) - 1] == '\n':
		text = text[0: len(text) - 1]

	return text


fileName = sys.argv[1]
topicsRaw = open(fileName, "r").read()
topicsRaw = topicsRaw.split("#ITEM")
del topicsRaw[0]

topics = []
items = []

id = 0
for t in topicsRaw:
	raw = t.split("#")
	topic = Topic(id, raw[1], "images/default.jpeg")
	addTopic(topics, topic)
	id += 1
	


for t in topicsRaw:
	raw = t.split("#")

	item = Item(raw[2], raw[4], raw[3], getTopicId(topics, raw[1]))
	
	
	items.append(item)


for topic in topics:
	print '#TOPIC'
	print '#' + clear(topic.title)
	print '#' + clear(topic.image) + "\n"
	for item in items:
		if item.topicId == topic.id:
			print '#ITEM'	
			print '#' + clear(item.title)
			print '#' + clear(item.text)
			print '#' + clear(item.image)
			print ""
