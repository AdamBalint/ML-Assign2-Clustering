data <- read.csv(file.choose(), sep="",header=FALSE)
x <- data[1]
y <- data[2]


attach(data)
names(getwd())
names(x)

png("/Graphs/graph.png")
matplot(x, y, typ="l")
#plot(data[0])
dev.off()




