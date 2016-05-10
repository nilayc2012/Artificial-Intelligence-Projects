#!/bin/bash
a=0
while [ "$a" -lt 50 ]    # this is loop1
do
./bastar "input/input"$a".txt" "output/output"$a".txt" >> result.txt
#echo "###################################" >> result.txt
a=`expr $a + 1`
done
