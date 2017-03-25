#!/bin/ruby

def getValues(filename)
  file = File.open(filename,"r")
  values = Array.new
  while(line = file.gets)
    v = Array.[](line.to_i())
    values = values + v
  end
  file.close()
  return values;
end

def writeValues(i,v,error,filename)
  file = File.open(filename,"a")
  # This only works on 1.9
  # file.puts(""+i.to_s()+" "+v.round(2).to_s()+" "+error.round(2).to_s())
  file.printf("%d %0.2f %0.2f\n",i,v,error)
  file.close()
end

def mean(population)
  mean = 0.0
  divisor = 0
  total = 0
  population.each { |v|
    total += v
    divisor += 1
  }
  mean = Float(total)/divisor
  return mean
end


# def variance(population)
#     n = 0
#     mean = 0.0
#     s = 0.0
#     population.each { |x|
#       n = n + 1
#       delta = x – mean
#       mean = mean + (delta / n)
#       s = s + delta * (x – mean)
#     }
#     # if you want to calculate std deviation
#     # of a sample change this to "s / (n-1)"
#     return s / n
# end
def variance2(population, mean)
  sum = 0
  n = population.length()
  population.each { |x|
    sum = sum + (x - mean)**2
  }
  return sum/(n-1)
end

def variance(population)
  mean = mean(population)
  return variance2(population,mean)
end
 
# calculate the standard deviation of a population
# accepts: an array, the population
# returns: the standard deviation
def standard_deviation(population)
  Math.sqrt(variance(population))
end

# Testing code
# population = getValues("stats"+i.to_s()+".txt","r")
# population = getValues("stats"+1.to_s()+".txt")
# mean = mean(population)
# variance = variance2(population,mean)
# std_dev = standard_deviation(population)
# puts("Mean = "+mean.to_s())
# puts("Variance = "+variance.to_s())
# puts("Std Dev = "+std_dev.to_s())

###############################################################################
# Actual Script to calculate the totals
###############################################################################

maxBombs = 40
puts("Gathering stats for "+maxBombs.to_s()+" bombs ")

File.delete("stats.txt")
File.delete("statsNormative.txt")

for i in 1..maxBombs do
  population = getValues("stats"+i.to_s()+".txt")
  mean = mean(population)
  stdev = standard_deviation(population)
  writeValues(i,mean,stdev,"stats.txt")
  
  population = getValues("statsNormative"+i.to_s()+".txt")
  mean = mean(population)
  stdev = standard_deviation(population)
  writeValues(i,mean,stdev,"statsNormative.txt")
end

# for i in 1..40 do
#   file = File.open("stats"+i.to_s()+".txt","r")
#   population = Array.new
#   while(line = file.gets)
#     
#     total+=line.to_i()
#   end
#   divisor=0
#   divisor+=1
#   average = Float(total)/(divisor*1000)
#   file.close
#   file = File.open("stats.txt","a")
#   file.puts(""+i.to_s()+" "+average.to_s()+"\n")
#   file.close
#   
#   #Nasty non parameterized code repeat, I know
#   file = File.open("statsNormative"+i.to_s()+".txt","r")
#   total=0
#   divisor=0
#   while(line = file.gets)
#     divisor+=1
#     total+=line.to_i()
#   end
#   average = Float(total)/(divisor*1000)
#   file.close
#   file = File.open("statsNormative.txt","a")
#   file.puts(""+i.to_s()+" "+average.to_s()+"\n")
#   file.close
# end
