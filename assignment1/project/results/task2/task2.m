x = csvread("b_result.txt");

time = x(:, 1);
abuf = x(:, 2);
bbuf = x(:, 3);

plot(time, abuf, "r;aInBuffer;", time, bbuf, "b;bInBuffer;");
title("Amount of jobs in buffer");