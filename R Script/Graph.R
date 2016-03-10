data <- read.csv("../Output/Classification-813202513504082", sep="",header=FALSE)
x <- data[1]
y <- data[2]


attach(data)
names(getwd())
names(x)

png("Graphs/graph.png")
matplot(x, y, pch=20, col=34, bg=34)
#plot(data[0])