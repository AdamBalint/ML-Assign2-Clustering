import os
indir = 'Output/'
for root, dirs, filenames in os.walk(indir):
    for f in filenames:
		sp = f.split("-")
		if  sp[0] == "Classification":
			print(sp[1])