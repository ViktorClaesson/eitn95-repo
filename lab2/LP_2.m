clc
clear all

c = [1 1 1 1 1 1 1]';
A = [1 0 0 1 1 1 1;
     1 1 0 0 1 1 1;
     1 1 1 0 0 1 1;
     1 1 1 1 0 0 1;
     1 1 1 1 1 0 0;
     0 1 1 1 1 1 0;
     0 0 1 1 1 1 1];
b = [8 6 5 4 6 7 9]';

[x,fval,exitflag,output] = glpk(c, A, b, [], [], "LLLLLLL", "CCCCCCC", 1)