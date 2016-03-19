import os
indir = 'Output/'
for root, dirs, filenames in os.walk(indir):
    for f in filenames:
        print(f)