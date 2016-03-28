setwd("~/University/Year 4/COSC 4P76/Assign2/R-Script")
args <- commandArgs(trailingOnly = TRUE)

#args[0]

a = paste("../Output/", args[1], "-Classification.txt", sep="")
a
data <- read.csv(a, sep="",header=FALSE)
#data <- read.csv("../Output/2251973593357283-Classification.txt", sep="",header=FALSE)
x <- data[1]
y <- data[2]


b = paste("../Output/", args[1], "-Centroids.txt", sep="")
data2 <- read.csv(b, sep="",header=FALSE)
#data2 <- read.csv("../Output/2251973593357283-Centroids.txt", sep="",header=FALSE)
x2 <- data2[1]
y2 <- data2[2]

png(file=paste("Graphs/", args[1], "-Graph.png", sep =""), width=800, height=800)
#png(file="Graphs/Graph.png", width=800, height=800)
palette(rainbow(17))
par=(bg="whitesmoke")
plot(x=data$V1, y=data$V2, pch=20, ps=1, col=(data$V3+1), main=args[1], xlab="X", ylab="Y")
palette("default")
points(data2$V1, data2$V2, pch=15, col=2)
points(data2$V1, data2$V2, pch=7, col=1)

dev.off()

