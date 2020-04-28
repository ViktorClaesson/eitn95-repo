x = csvread("0.txt");
y = csvread("1.txt");
z = csvread("2.txt");

time = [1:1000];
res0 = x(:, 1);
res1 = y(:, 1);
res2 = z(:, 1);

plot(time, res0, "-r;task 1;", time, res1, "-b;task 2;", time, res2, "-g;task 3;");