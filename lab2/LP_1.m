clc
clear all

c = [13;
     11];
A = [4 5;
     5 3;
     1 2];
b = [1500;
    1575;
    420];

[x,fval,exitflag,output] = glpk(c, A, b, [], [], "UUU", "II", -1)