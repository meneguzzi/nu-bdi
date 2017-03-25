#!/bin/ruby

for i in 1..40 do
  fileIn = File.open("stats"+i.to_s()+".txt","r")
  fileOut = File.open("stats.txt","a")
  while(line = fileIn.gets)
    fileOut.puts(""+i.to_s()+" "+line.to_s())  
  end
  fileIn.close
  fileOut.close
  
  #Nasty non parameterized code repeat, I know
  fileIn = File.open("statsNormative"+i.to_s()+".txt","r")
  fileOut = File.open("statsNormative.txt","a")
  while(line = fileIn.gets)
    fileOut.puts(""+i.to_s()+" "+line.to_s())  
  end
  fileIn.close
  fileOut.close
end
