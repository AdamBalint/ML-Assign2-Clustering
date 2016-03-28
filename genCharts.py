import os
import subprocess
indir = 'Output/'
for root, dirs, filenames in os.walk(indir):
    for f in filenames:
		sp = f.split("-")
		if  sp[1] == "Classification.txt":
			print(sp[0])
			cmd = ["Rscript", "R-Script/Graph.R", sp[0]]
			subprocess.Popen(cmd)
		#print("Test:" + f)