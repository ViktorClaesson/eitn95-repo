clc
clear all

c = [1500;
     1575;
     420];
A = [4 5 1;
     5 3 2;];
b = [13;
    11;];
lb = [0;
     0;
     0];

[x,fval,exitflag,output] = glpk(c, A, b, lb, [], "LL", "III", 1)