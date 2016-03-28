import os
import subprocess
indir = '../Output/'
for root, dirs, filenames in os.walk(indir):
    for f in filenames:
		sp = f.split("-")
		if  sp[1] == "Classification.txt":
			print(sp[0])
			mypath = os.path.abspath(__file__)
			mydir = os.path.dirname(mypath)
			
			start = os.path.join(mydir, "Graph.R ")
			print("Path: " + start + "\n")
			print("Calling Rscript \n")
			#cmd = ["Rscript", start, sp[0]]
			#x = subprocess.Popen(cmd)
			
			x = subprocess.call("Rscript \"" + start+"\" " + sp[0], shell=True)
			#print("Success: " + x)
		#print("Test:" + f)
