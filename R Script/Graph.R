data <- read.csv("../Output/Classification-814698944939384.txt", sep="",header=FALSE)
x <- data[1]
y <- data[2]

data2 <- read.csv("../Output/Centroids-814698944939384.txt", sep="",header=FALSE)
x2 <- data2[1]
y2 <- data2[2]

attach(data)
names(getwd())
names(x)

png(file=paste("/Graphs/graph.png", ".png", sep=""), width=800, height=800)
palette(rainbow(17))
par=(bg="whitesmoke")
plot(data$V1, data$V2, pch=20, ps=1, col=data$V3, )
palette("default")
points(data2$V1, data2$V2, pch=15, col=2)
points(data2$V1, data2$V2, pch=7, col=1)

#matplot(x, y, pch=20, col=34, bg=34)
#matplot(x2, y2, pch=1, col=10, bg=10)

#plot(data[0])