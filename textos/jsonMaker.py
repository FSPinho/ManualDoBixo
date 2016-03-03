import sys
from time import time as getNow

#****************************

#****************************

filename = sys.argv[1]

filetext = open(filename, "r").read()
filetext = filetext.replace("\"", "\\\"");
filetext = filetext.replace("\n", "\\n");

now = getNow()

json = \
"{ \n" + \
"	\"lastUpdate\": " + str(now) + ", \n" + \
"	\"text\": \"" + filetext + "\" \n" + \
"}"

print json