#!/bin/ruby

for i in 1..10 do
  file = File.open("stats"+i+".txt","r")
  total=0
  divisor=0
  while(line = file.gets)
    divisor++
    total+=line.to_i()
  end
  average = total/divisor
  file.close
  file = File.open("stats.txt","a")
  file.puts(""+i.to_i+" "+average+"\n")
  file.close
  
  #Nasty non parameterized code repeat, I know
  file = File.open("statsNormative"+i+".txt","r")
  total=0
  divisor=0
  while(line = file.gets)
    divisor++
    total+=line.to_i()
  end
  average = total/divisor
  file.close
  file = File.open("statsNormative.txt","a")
  file.puts(""+i.to_i+" "+average+"\n")
  file.close
end