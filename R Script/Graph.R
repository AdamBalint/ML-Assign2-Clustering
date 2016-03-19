data <- read.csv("../Output/Classification-331218227056273.txt", sep="",header=FALSE)
x <- data[1]
y <- data[2]

data2 <- read.csv("../Output/Centroids-331218227056273.txt", sep="",header=FALSE)
x2 <- data2[1]
y2 <- data2[2]

png(file="Graphs/graph.png", width=800, height=800)
palette(rainbow(17))
par=(bg="whitesmoke")
plot(data$V1, data$V2, pch=20, ps=1, col=(data$V3+1))
palette("default")
points(data2$V1, data2$V2, pch=15, col=2)
points(data2$V1, data2$V2, pch=7, col=1)

dev.off()

